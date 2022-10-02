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

package space.lingu.imagehosting.data.database.dao;

import space.lingu.imagehosting.data.entity.ImageStorage;
import space.lingu.imagehosting.data.entity.UserUploadImageStorage;
import space.lingu.light.*;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class ImageStorageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ImageStorage... storages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<ImageStorage> storages);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(ImageStorage... storages);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(List<ImageStorage> storages);

    @Delete
    public abstract void delete(ImageStorage... storages);

    @Delete
    public abstract void delete(List<ImageStorage> storages);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertUserUploadImageStorage(UserUploadImageStorage... storages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertUserUploadImageStorage(List<UserUploadImageStorage> storages);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateUserUploadImageStorage(UserUploadImageStorage... storages);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateUserUploadImageStorage(List<UserUploadImageStorage> storages);

    @Delete
    public abstract void deleteUserUploadImageStorage(UserUploadImageStorage... storages);

    @Delete
    public abstract void deleteUserUploadImageStorage(List<UserUploadImageStorage> storages);


    @Transaction
    @Delete("DELETE image_store_table WHERE image_id = {id}")
    public abstract void deleteByImageId(String id);

    @Query("SELECT * FROM image_store_table WHERE image_id = {id}")
    public abstract ImageStorage getByImageId(String id);

    @Query("SELECT * FROM user_upload_table WHERE user_id = {userId}")
    public abstract List<UserUploadImageStorage> getUploadedByUserId(long userId);
}
