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

package space.lingu.imagehosting.file;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * @author RollW
 */
public class LocalFile implements ServerFile {
    private final File file;


    public LocalFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        this.file = file;
    }

    public LocalFile(String path) {
        this(new File(path));
    }

    public File getFile() {
        return file;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getPath() {
        return file.getAbsolutePath();
    }

    @Override
    public long length() throws IOException {
        var res = Files.readAttributes(file.toPath(), "size");
        return (long) res.get("size");
    }

    @Override
    public long lastModified() throws IOException {
        return Files.getLastModifiedTime(file.toPath()).toMillis();
    }

    @Override
    public OutputStream openOutput(boolean overwrite) throws IOException {
        return new FileOutputStream(file, !overwrite);
    }

    @Override
    public InputStream openInput() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public boolean delete() {
        return file.delete();
    }

    @Override
    public boolean delete(boolean recursive) {
        return false;
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public boolean createFile() throws IOException {
        return file.createNewFile();
    }

    @Override
    public boolean mkdirs() {
        return file.mkdirs();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public ServerFile getParent() throws IOException {
        return new LocalFile(file.getParentFile());
    }

    @Override
    public List<ServerFile> listFiles() throws IOException {
        return null;
    }

    @Override
    public List<ServerFile> listFiles(boolean recursive) throws IOException {
        return null;
    }
}
