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

package space.lingu.imagehosting.data.database;

import space.lingu.imagehosting.data.database.dao.*;
import space.lingu.imagehosting.data.entity.*;
import space.lingu.light.Database;
import space.lingu.light.LightConfiguration;
import space.lingu.light.LightDatabase;

/**
 * @author RollW
 */
@Database(name = "image_host_system", version = 1,
        tables = {User.class, ImageStorage.class,
                UserUploadImageStorage.class, RegisterVerificationToken.class,
                UserGroupConfig.class, GroupedUser.class,
                SettingItem.class
        },
        configuration = @LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "255"))
public abstract class ImageHostDatabase extends LightDatabase {
    public abstract ImageStorageDao getImageStorageDao();

    public abstract UserDao getUserDao();

    public abstract VerificationTokenDao getVerificationTokenDao();

    public abstract UserGroupDao getUserGroupConfigDao();

    public abstract SettingDao getSettingDao();

}
