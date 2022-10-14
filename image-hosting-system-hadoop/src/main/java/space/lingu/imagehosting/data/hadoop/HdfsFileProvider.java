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

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import space.lingu.imagehosting.file.ServerFile;
import space.lingu.imagehosting.file.ServerFileProvider;

/**
 * File provider for Hadoop distributed file system.
 *
 * @author RollW
 */
public class HdfsFileProvider implements ServerFileProvider {
    private final FileSystem fileSystem;

    public HdfsFileProvider(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }


    @Override
    public ServerFile openFile(String path) {
        return new HdfsServerFile(path, fileSystem);
    }

    @Override
    public ServerFile openFile(String parent, String path) {
        return new HdfsServerFile(new Path(parent, path), fileSystem);
    }

    @Override
    public ServerFile openFile(ServerFile parent, String path) {
        if (parent instanceof HdfsServerFile serverFile) {
            return new HdfsServerFile(
                    new Path(serverFile.toPath(), path),
                    fileSystem);
        }
        return null;
    }
}
