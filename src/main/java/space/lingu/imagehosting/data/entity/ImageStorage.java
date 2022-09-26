package space.lingu.imagehosting.data.entity;

import space.lingu.light.*;

/**
 * @author RollW
 */
@DataTable(tableName = "image_store_table", configuration =
@LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "120"))
public class ImageStorage {
    @DataColumn(name = "image_id")
    @PrimaryKey
    private String id;// md5

    @DataColumn(name = "image_path")
    private String path;

    @DataColumn(name = "image_cache_path", configuration =
    @LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "255"))
    private String cachePath;

    @DataColumn(name = "image_file_size")
    private long fileSize;

    @DataColumn(name = "image_last_access_time")
    private long lastAccessTime;

    public ImageStorage() {
    }

    @Constructor
    public ImageStorage(String id, String path, String cachePath, long fileSize, long lastAccessTime) {
        this.id = id;
        this.path = path;
        this.cachePath = cachePath;
        this.fileSize = fileSize;
        this.lastAccessTime = lastAccessTime;
    }

    public ImageStorage(String path, String cachePath, long fileSize) {
        this.path = path;
        this.cachePath = cachePath;
        this.fileSize = fileSize;
    }

    public String getId() {
        return id;
    }

    public ImageStorage setId(String id) {
        this.id = id;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ImageStorage setPath(String path) {
        this.path = path;
        return this;
    }

    public String getCachePath() {
        return cachePath;
    }

    public ImageStorage setCachePath(String cachePath) {
        this.cachePath = cachePath;
        return this;
    }

    public long getFileSize() {
        return fileSize;
    }

    public ImageStorage setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public ImageStorage setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
        return this;
    }
}
