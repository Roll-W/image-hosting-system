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

package space.lingu.imagehosting.data.file;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import space.lingu.imagehosting.common.ErrorCode;
import space.lingu.imagehosting.properties.HdfsProperties;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * @author RollW
 */
//@Component
public class HdfsManager {
    private final HdfsProperties hdfsProperties;
    private final Configuration conf;
    private final FileSystem fileSystem;

    public HdfsManager(HdfsProperties properties) {
        this.hdfsProperties = properties;

        conf = new Configuration();
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        try {
            fileSystem = FileSystem.get(URI.create(hdfsProperties.getUrl()), conf);
        } catch (IOException e) {
            throw new HdfsRuntimeException(ErrorCode.ERROR_FILE, e);
        }
    }

    /**
     * 上传至HDFS <br>
     *
     * @param sourcePath 服务器源路径
     * @param destPath   Hdfs目标路径
     * @return {@link ErrorCode}
     */
    public ErrorCode upload(String sourcePath, String destPath) {
        if (fileSystem == null) {
            return ErrorCode.ERROR_NULL;
        }

        Path sp = new Path(sourcePath);
        Path dp = new Path(destPath);
        try {
            fileSystem.create(dp);
            fileSystem.copyFromLocalFile(sp, dp);
        } catch (IOException e) {
            return ErrorCode.ERROR_IO;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 从HDFS下载文件 <br>
     * 注意：此方法为下载到服务器内
     *
     * @param sourcePath Hadoop源路径
     * @param destPath   目标路径
     * @return {@link ErrorCode}
     */
    public ErrorCode download(String sourcePath, String destPath) {
        Path sp = new Path(sourcePath);
        if (fileSystem == null) {
            return ErrorCode.ERROR_NULL;
        }
        try {
            if (!fileSystem.exists(sp)) {
                return ErrorCode.ERROR_FILE_NOT_FOUND;
            }
            FSDataInputStream fsDataInputStream = fileSystem.open(sp);
            FileOutputStream outputStream = new FileOutputStream(destPath);
            IOUtils.copyBytes(fsDataInputStream, outputStream, conf);
            outputStream.close();
        } catch (IOException e) {
            return ErrorCode.ERROR_IO;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 从HDFS拷贝输出流 <br>
     *
     * @param sourcePath Hadoop源路径
     * @param stream     输出流
     * @return {@link ErrorCode}
     */
    public ErrorCode download(String sourcePath, OutputStream stream) {
        Path sp = new Path(sourcePath);
        if (fileSystem == null) {
            return ErrorCode.ERROR_NULL;
        }
        try {
            if (!fileSystem.exists(sp)) {
                return ErrorCode.ERROR_FILE_NOT_FOUND;
            }
            FSDataInputStream fsDataInputStream = fileSystem.open(sp);
            IOUtils.copyBytes(fsDataInputStream, stream, conf);
        } catch (IOException e) {
            return ErrorCode.ERROR_IO;
        }
        return ErrorCode.SUCCESS;
    }

    public ErrorCode delete(String destPath) {
        Path dest = new Path(destPath);
        try {
            if (!fileSystem.exists(dest)) {
                return ErrorCode.ERROR_FILE_NOT_FOUND;
            }
            fileSystem.delete(dest, true);
        } catch (IOException e) {
            return ErrorCode.ERROR_IO;
        }
        return ErrorCode.SUCCESS;
    }

    public ErrorCode mkdirs(String path) {
        Path p = new Path(path);
        try {
            fileSystem.mkdirs(p);
        } catch (IOException e) {
            return ErrorCode.ERROR_FILE;
        }
        return ErrorCode.SUCCESS;
    }

    public static String getReadableSize(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %cB", value / 1024.0, ci.current());
    }

}
