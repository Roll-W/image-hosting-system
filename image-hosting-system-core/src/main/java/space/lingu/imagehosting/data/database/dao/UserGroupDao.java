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

import space.lingu.NonNull;
import space.lingu.imagehosting.data.entity.GroupedUser;
import space.lingu.imagehosting.data.entity.UserGroupConfig;
import space.lingu.light.*;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class UserGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(UserGroupConfig... configs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(UserGroupConfig config);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<UserGroupConfig> configs);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(UserGroupConfig... configs);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(List<UserGroupConfig> configs);

    @Delete
    public abstract void delete(UserGroupConfig... configs);

    @Delete
    public abstract void delete(List<UserGroupConfig> configs);

    @Query("SELECT * FROM user_group_config_table WHERE group_name = {name}")
    public abstract UserGroupConfig getGroupConfigByName(String name);

    @NonNull
    public UserGroupConfig getGroupConfigByNameOrDefault(String name) {
        UserGroupConfig config = getGroupConfigByName(name);
        if (config == null) {
            return UserGroupConfig.DEFAULT;
        }
        return config;
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertGroupedUser(GroupedUser... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertGroupedUser(GroupedUser user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertGroupedUser(List<GroupedUser> users);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateGroupedUser(GroupedUser... users);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateGroupedUser(List<GroupedUser> users);

    @Delete
    public abstract void deleteGroupedUser(GroupedUser... users);

    @Delete
    public abstract void deleteGroupedUser(List<GroupedUser> users);

    @Query("SELECT * FROM grouped_user_table WHERE group_name = {name}")
    public abstract List<GroupedUser> getGroupedUsersByName(String name);

    @Query("SELECT * FROM grouped_user_table WHERE user_id = {id}")
    public abstract GroupedUser getGroupedUserById(long id);


}
