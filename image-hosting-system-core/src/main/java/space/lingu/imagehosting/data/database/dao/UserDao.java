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

import space.lingu.Dangerous;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.data.entity.User;
import space.lingu.light.*;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(User... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Dangerous(message = "Don't call this outside repository.")
    public abstract long insert(User user);// with user id

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<User> users);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(User... users);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(List<User> users);

    @Delete
    public abstract void delete(User... users);

    @Delete("DELETE FROM user WHERE user_id = {info.id()}")
    public abstract void delete(UserInfo info);

    @Delete
    public abstract void delete(List<User> users);

    @Query("SELECT user_id, user_name, user_email FROM user")
    public abstract List<UserInfo> userInfos();

    @Query("SELECT * FROM user WHERE user_id = {id}")
    public abstract User getUserById(long id);

    @Query("SELECT * FROM user WHERE user_name = {name}")
    public abstract User getUserByName(String name);

    @Query("SELECT * FROM user WHERE user_email = {email}")
    public abstract User getUserByEmail(String email);

    @Query("SELECT user_name FROM user WHERE user_email = {email}")
    public abstract String getUsernameByEmail(String email);

    @Query("SELECT user_name FROM user WHERE user_id = {id}")
    public abstract String getUsernameById(long id);
}
