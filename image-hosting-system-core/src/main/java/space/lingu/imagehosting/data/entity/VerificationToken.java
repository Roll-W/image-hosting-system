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

package space.lingu.imagehosting.data.entity;

import space.lingu.light.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Verification Token
 *
 * @author RollW
 */
@DataTable(tableName = "verification_token_table", indices =
@Index(value = "verification_user_id"), configuration =
@LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "120"))
public record VerificationToken(
        @DataColumn(name = "verification_token")
        @PrimaryKey
        String token,

        @DataColumn(name = "verification_user_id")
        long userId,

        @DataColumn(name = "verification_expiry_time")
        long expiryDate) {

    private static final int EXPIRATION = 60 * 24;// min

    public static long calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return cal.getTime().getTime();
    }

    public static long calculateExpiryDate() {
        return calculateExpiryDate(EXPIRATION);
    }

    public Date toDate() {
        return new Date(expiryDate);
    }

}
