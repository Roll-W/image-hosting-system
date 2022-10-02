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

package space.lingu.imagehosting.data.database.repository;

import org.springframework.stereotype.Repository;
import space.lingu.imagehosting.data.database.ImageHostDatabase;
import space.lingu.imagehosting.data.database.dao.ImageStorageDao;
import space.lingu.imagehosting.data.database.dao.UserGroupDao;
import space.lingu.imagehosting.data.dto.UserUsageInfo;
import space.lingu.imagehosting.data.entity.GroupedUser;
import space.lingu.imagehosting.data.entity.UserGroupConfig;
import space.lingu.imagehosting.data.entity.UserUploadImageStorage;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author RollW
 */
@Repository
public class UserGroupRepository {
    private final ImageStorageDao imageStorageDao;
    private final UserGroupDao userGroupConfigDao;

    public UserGroupRepository() {
        this.imageStorageDao = ImageHostDatabase.getDatabase().getImageStorageDao();
        this.userGroupConfigDao = ImageHostDatabase.getDatabase().getUserGroupConfigDao();
    }


    public UserUsageInfo getUsageInfo(long userId) {
        GroupedUser groupedUser =
                userGroupConfigDao.getGroupedUserById(userId);
        UserGroupConfig config;
        if (groupedUser == null) {
            config = userGroupConfigDao.getGroupConfigByNameOrDefault("default");
        } else {
            config = userGroupConfigDao.getGroupConfigByNameOrDefault(groupedUser.getUserGroup());
        }
        List<UserUploadImageStorage> storages =
                imageStorageDao.getUploadedByUserId(userId);
        int mbSize = countToMb(storages);
        return new UserUsageInfo(userId, config.getName(),
                mbSize, storages.size(),
                config.getMaxFileSize(), config.getMaxNum());
    }

    private int countToMb(List<UserUploadImageStorage> storages) {
        AtomicLong res = new AtomicLong();
        storages.forEach(userUploadImageStorage ->
                res.addAndGet(userUploadImageStorage.fileSize()));
        return (int) (res.get() / 1024 / 1024);
    }


}
