/*
 * Copyright (C) 2022 Lingu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.imagehosting.service.image;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import space.lingu.Todo;
import space.lingu.imagehosting.common.ErrorCode;
import space.lingu.imagehosting.common.SystemFileException;
import space.lingu.imagehosting.data.database.repository.ImageStorageRepository;
import space.lingu.imagehosting.data.database.repository.UserGroupRepository;
import space.lingu.imagehosting.data.dto.MessagePackage;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.data.dto.UserUsageInfo;
import space.lingu.imagehosting.data.entity.ImageStorage;
import space.lingu.imagehosting.data.entity.UserUploadImageStorage;
import space.lingu.imagehosting.data.hadoop.HadoopFileServer;
import space.lingu.imagehosting.file.LocalFileServer;
import space.lingu.imagehosting.file.ServerFile;
import space.lingu.imagehosting.properties.DirectoriesProperties;
import space.lingu.imagehosting.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author RollW
 */
@Service
public class ImageServiceImpl implements ImageService {
    // Progress:
    // 1. 上传图片后，先行保存到Hdfs，同时维持本地cache文件不变
    // 2. 每次访问，都更新一次lastAccessTime。
    //    按照定时任务，删除超过时长未访问过的文件（配置策略）。
    // 3. 访问时：如果文件不存在时，
    //    下载一份到cache，更新cachePath

    final UserService userService;
    final ImageStorageRepository imageStorageRepository;
    final UserGroupRepository userGroupRepository;
    final LocalFileServer localFileServer;
    final HadoopFileServer hadoopFileServer;
    DirectoriesProperties properties;

    @Autowired
    public void setProperties(DirectoriesProperties properties) {
        this.properties = properties;
    }

    public ImageServiceImpl(UserService userService,
                            ImageStorageRepository imageStorageRepository,
                            UserGroupRepository userGroupRepository,
                            LocalFileServer localFileServer,
                            HadoopFileServer hadoopFileServer) {

        this.userService = userService;
        this.imageStorageRepository = imageStorageRepository;
        this.userGroupRepository = userGroupRepository;
        this.localFileServer = localFileServer;
        this.hadoopFileServer = hadoopFileServer;
    }

    @Override// TODO
    @Todo(todo = "todo")
    public MessagePackage<String> saveImage(HttpServletRequest request,
                                            byte[] bytes) throws IOException {
        UserInfo userInfo = userService.getCurrentUser(request);
        if (userInfo == null) {
            return new MessagePackage<>(ErrorCode.ERROR_USER_NOT_LOGIN,
                    "user not login", null);
        }
        int dataLength = bytes.length;
        UserUsageInfo userUsageInfo =
                userGroupRepository.getUsageInfo(userInfo.id());
        if (userUsageInfo.nowUsedNum() + 1 > userUsageInfo.maxNum()) {
            return new MessagePackage<>(ErrorCode.ERROR_USAGE_LIMIT,
                    "Files number up to the limit.", null);
        }
        int usedSize = userUsageInfo.nowUsedSize() + (dataLength / 1024 / 1024);
        if (usedSize > userUsageInfo.maxSize()) {
            return new MessagePackage<>(ErrorCode.ERROR_USAGE_LIMIT,
                    "Files size up to the limit.", null);
        }

        String fileName = DigestUtils.md5Hex(bytes);
        ServerFile cacheDir =
                localFileServer.getServerFileProvider()
                        .openFile(properties.getCacheDirectory());
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        ServerFile tempFile = localFileServer.getServerFileProvider()
                .openFile(cacheDir, fileName);

        boolean needsWriteFile = false;
        if (!tempFile.exists()) {
            needsWriteFile = true;
            if (!tempFile.createFile()) {
                throw new SystemFileException(ErrorCode.ERROR_IO,
                        "Error creating file.");
            }
        }

        if (needsWriteFile) {
            saveFileData(tempFile, bytes);
        }

        ServerFile remoteFileDir = hadoopFileServer.getServerFileProvider()
                .openFile(properties.getHdfsDirectory() + "images/");
        if (!remoteFileDir.exists()) {
            remoteFileDir.mkdirs();
        }

        ServerFile remoteFile = hadoopFileServer.getServerFileProvider()
                .openFile(remoteFileDir, fileName);
        boolean upload =
                hadoopFileServer.upload(tempFile, remoteFile, false);
        logger.info("upload file local: {}, remote {}, state: {}",
                tempFile.getPath(), remoteFile.getPath(), upload);

        ImageStorage storage = new ImageStorage();
        storage.setId(fileName)
                .setPath(remoteFile.getPath())
                .setCachePath(tempFile.getPath())
                .setFileSize(tempFile.length())
                .setLastAccessTime(System.currentTimeMillis());
        UserUploadImageStorage userUploadImageStorage =
                new UserUploadImageStorage(userInfo.id(), fileName, tempFile.length());

        imageStorageRepository.save(storage);
        imageStorageRepository.save(userUploadImageStorage);

        return new MessagePackage<>(ErrorCode.SUCCESS, "", storage.getId());
    }

    @Async
    void saveFileData(ServerFile outFile, byte[] bytes) {
        try (OutputStream outputStream = new BufferedOutputStream(
                outFile.openOutput(true))) {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            throw new SystemFileException(ErrorCode.ERROR_FILE, e);
        }
    }

    public void deleteLocalTempFile(String id) throws IOException {
        ImageStorage storage =
                imageStorageRepository.getById(id);
        ServerFile tempFile =
                localFileServer.getServerFileProvider().openFile(storage.getCachePath());
        tempFile.delete();
        imageStorageRepository.save(storage);
    }

    @Override
    public byte[] getImageBytes(Long userId, String imageId) throws IOException {
        UserUploadImageStorage userUploadImageStorage =
                imageStorageRepository.getByUserAndImage(userId, imageId);
        if (userUploadImageStorage == null) {
            throw new SystemFileException(ErrorCode.ERROR_FILE_NOT_FOUND,
                    "file not exist");
        }

        ImageStorage storage =
                imageStorageRepository.getById(userUploadImageStorage.imageId());
        if (storage == null) {
            throw new SystemFileException(ErrorCode.ERROR_FILE_NOT_FOUND,
                    "file not exist");
        }
        storage.setLastAccessTime(System.currentTimeMillis());

        ServerFile file = localFileServer.getServerFileProvider()
                .openFile(storage.getCachePath());
        if (!file.exists()) {
            ServerFile remoteFile = hadoopFileServer.getServerFileProvider()
                    .openFile(storage.getPath());
            hadoopFileServer.download(remoteFile, file, true);
        }
        try (InputStream stream = file.openInput()) {
            return stream.readAllBytes();
        }
    }

    private final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
}
