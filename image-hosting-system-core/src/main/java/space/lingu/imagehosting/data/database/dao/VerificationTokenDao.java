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

import space.lingu.imagehosting.data.entity.RegisterVerificationToken;
import space.lingu.light.*;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class VerificationTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(RegisterVerificationToken... tokens);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(RegisterVerificationToken token);// with user id

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<RegisterVerificationToken> tokens);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(RegisterVerificationToken... tokens);

    @Transaction
    @Delete("UPDATE verification_token_table SET verification_used = {used} WHERE verification_token = {token}")
    public abstract void updateUsedByToken(String token, boolean used);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(List<RegisterVerificationToken> tokens);

    @Delete
    public abstract void delete(RegisterVerificationToken... tokens);

    @Transaction
    @Delete("DELETE FROM verification_token_table WHERE verification_token = {token.token()}")
    public abstract void delete(RegisterVerificationToken token);

    @Delete
    public abstract void delete(List<RegisterVerificationToken> users);

    @Query("SELECT * FROM verification_token_table WHERE verification_token = {token}")
    public abstract RegisterVerificationToken findByToken(String token);

    @Query("SELECT * FROM verification_token_table WHERE verification_user_id = {userId} LIMIT 1")
    public abstract RegisterVerificationToken findByUserId(long userId);
}
