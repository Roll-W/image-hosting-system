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

package space.lingu.imagehosting.data.hadoop;

import org.apache.hadoop.fs.*;
import space.lingu.imagehosting.file.ServerFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author RollW
 */
public class HdfsServerFile implements ServerFile {
    private final Path path;
    private final FileSystem fileSystem;
    private FileStatus fileStatus;

    protected HdfsServerFile(String path, FileSystem fileSystem) {
        this(new Path(path), fileSystem);
    }

    protected HdfsServerFile(Path path, FileSystem fileSystem) {
        this.path = path;
        this.fileSystem = fileSystem;
    }

    @Override
    public String getName() {
        return path.getName();
    }

    @Override
    public String getPath() {
        return path.toUri().getPath();
    }

    @Override
    public long length() throws IOException {
        checkFileStatus();
        return fileStatus.getLen();
    }

    @Override
    public long lastModified() throws IOException {
        checkFileStatus();
        return fileStatus.getModificationTime();
    }

    @Override
    public OutputStream openOutput(boolean overwrite) throws IOException {
        return fileSystem.create(path, overwrite);
    }


    @Override
    public InputStream openInput() throws IOException {
        return fileSystem.open(path);
    }

    @Override
    public boolean delete() throws IOException {
        return delete(true);
    }

    @Override
    public boolean delete(boolean recursive) throws IOException {
        return fileSystem.delete(path, recursive);
    }

    @Override
    public boolean exists() throws IOException {
        return fileSystem.exists(path);
    }

    @Override
    public boolean createFile() throws IOException {
        return fileSystem.createNewFile(path);
    }

    @Override
    public boolean mkdirs() throws IOException {
        return fileSystem.mkdirs(path);
    }

    @Override
    public boolean isFile() throws IOException {
        checkFileStatus();
        return fileStatus.isFile();
    }

    @Override
    public boolean isDirectory() throws IOException {
        checkFileStatus();
        return fileStatus.isDirectory();
    }

    @Override
    public ServerFile getParent() throws IOException {
        return new HdfsServerFile(path.getParent(), fileSystem);
    }

    @Override
    public List<ServerFile> listFiles() throws IOException {
        return listFiles(false);
    }

    @Override
    public List<ServerFile> listFiles(boolean recursive) throws IOException {
        RemoteIterator<LocatedFileStatus> statusRemoteIterator =
                fileSystem.listFiles(path, recursive);
        List<ServerFile> serverFiles = new ArrayList<>();
        while (statusRemoteIterator.hasNext()) {
            LocatedFileStatus status = statusRemoteIterator.next();
            Path listPath = status.getPath();
            HdfsServerFile file = new HdfsServerFile(listPath, fileSystem);
            serverFiles.add(file);
        }
        return Collections.unmodifiableList(serverFiles);
    }

    private void checkFileStatus() throws IOException {
        if (fileStatus == null) {
            fileStatus = fileSystem.getFileStatus(path);
        }
    }

    public Path toPath() {
        return path;
    }

}
