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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.imagehosting.configure.hadoop.HdfsProperties;
import space.lingu.imagehosting.file.FileServer;
import space.lingu.imagehosting.file.ServerFile;
import space.lingu.imagehosting.file.ServerFileProvider;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * @author RollW
 */
@Component
public class HadoopFileServer implements FileServer {
    private final FileSystem fileSystem;
    private final Configuration conf;
    private final HdfsProperties properties;
    private final HdfsFileProvider fileProvider;

    public HadoopFileServer(HdfsProperties properties) throws IOException {
        this.properties = properties;
        this.conf = initConfiguration();
        this.fileSystem = FileSystem.get(
                URI.create(properties.getUrl()), conf);
        this.fileProvider = new HdfsFileProvider(fileSystem);
    }

    private Configuration initConfiguration() {
        Configuration config = new Configuration();
        config.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        return config;
    }

    @Override
    public boolean upload(ServerFile localFile, ServerFile remoteFile, boolean overwrite) throws IOException {
        if (!localFile.exists()) {
            throw new FileNotFoundException("not found local file path:" + localFile.getPath());
        }
        boolean exist = remoteFile.exists();
        if (exist && !overwrite) {
            return false;
        }
        Path source = new Path(localFile.getPath());
        Path destination = new Path(remoteFile.getPath());
        if (!exist) {
            remoteFile.createFile();
        }


        fileSystem.copyFromLocalFile(source, destination);
        return true;
    }


    @Override
    public boolean download(ServerFile remoteFile, ServerFile localFile) throws IOException {
        return download(remoteFile, localFile, false);
    }

    @Override
    public boolean download(ServerFile remoteFile, ServerFile localFile, boolean overwrite) throws IOException {
        if (!remoteFile.exists()) {
            throw new FileNotFoundException("not found remote file path:" + remoteFile.getPath());
        }
        Path source = new Path(remoteFile.getPath());
        boolean exist = localFile.exists();
        if (exist && !overwrite) {
            return false;
        }
        if (!exist) {
            localFile.createFile();
        }
        FSDataInputStream fsDataInputStream = fileSystem.open(source);
        FileOutputStream outputStream = new FileOutputStream(localFile.getName());
        IOUtils.copyBytes(fsDataInputStream, outputStream, conf);
        outputStream.close();
        fsDataInputStream.close();

        return true;
    }


    @NonNull
    @Override
    public ServerFileProvider getServerFileProvider() {
        return fileProvider;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public HdfsProperties getProperties() {
        return properties;
    }
}
