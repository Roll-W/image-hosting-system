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

import space.lingu.imagehosting.data.entity.SettingItem;
import space.lingu.light.*;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class SettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(SettingItem items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<SettingItem> items);

    @Query("SELECT * FROM sound_setting_table")
    public abstract List<SettingItem> get();

    @Query("SELECT * FROM sound_setting_table WHERE `key` = {key}")
    public abstract SettingItem get(String key);

    @Query("SELECT * FROM sound_setting_table WHERE `key` IN {keys}")
    public abstract List<SettingItem> get(List<String> keys);

    @Update
    public abstract void update(SettingItem items);

    @Update
    public abstract void update(List<SettingItem> items);

    @Delete
    public abstract void delete(SettingItem items);

    @Delete
    public abstract void delete(List<SettingItem> items);

    @Delete("DELETE FROM sound_setting_table")
    public abstract void delete();

    @Delete("DELETE FROM sound_setting_table WHERE `key` = {key}")
    public abstract void delete(String key);
}
