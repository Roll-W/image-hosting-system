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

import space.lingu.imagehosting.data.database.dao.ImageStorageDao;
import space.lingu.imagehosting.data.database.dao.UserDao;
import space.lingu.imagehosting.data.database.dao.VerificationTokenDao;
import space.lingu.imagehosting.data.entity.ImageStorage;
import space.lingu.imagehosting.data.entity.User;
import space.lingu.imagehosting.data.entity.UserUploadImageStorage;
import space.lingu.imagehosting.data.entity.VerificationToken;
import space.lingu.light.Database;
import space.lingu.light.Light;
import space.lingu.light.LightDatabase;
import space.lingu.light.connect.simple.DisposableConnectionPool;
import space.lingu.light.log.LightSlf4jLogger;
import space.lingu.light.sql.MySQLDialectProvider;

/**
 * @author RollW
 */
@Database(name = "image_host_system", version = 1,
        tables = {User.class, ImageStorage.class,
                UserUploadImageStorage.class, VerificationToken.class
        })
public abstract class ImageHostDatabase extends LightDatabase {
    public abstract ImageStorageDao getImageStorageDao();
    public abstract UserDao getUserDao();
    public abstract VerificationTokenDao getVerificationTokenDao();

    private static volatile ImageHostDatabase DATABASE;

    public static ImageHostDatabase getDatabase() {
        if (DATABASE == null) {
            synchronized (ImageHostDatabase.class) {
                if (DATABASE == null) {
                    DATABASE = Light.databaseBuilder(ImageHostDatabase.class,
                                    MySQLDialectProvider.class)
                            .setConnectionPool(DisposableConnectionPool.class)
                            .setLogger(LightSlf4jLogger.createLogger(ImageHostDatabase.class))
                            .deleteOnConflict()
                            .build();
                }
            }
        }
        return DATABASE;
    }

}
