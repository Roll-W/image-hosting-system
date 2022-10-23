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

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import space.lingu.imagehosting.data.database.ImageHostDatabase;
import space.lingu.imagehosting.data.database.dao.ImageStorageDao;
import space.lingu.imagehosting.data.entity.ImageStorage;
import space.lingu.imagehosting.data.entity.UserUploadImageStorage;

/**
 * @author RollW
 */
@Repository
public class ImageStorageRepository {
    private final ImageStorageDao dao;

    public ImageStorageRepository(ImageHostDatabase database) {
        dao = database.getImageStorageDao();
    }

    @Async
    public void save(ImageStorage storage) {
        dao.insert(storage);
    }

    @Async
    public void save(UserUploadImageStorage storage) {
        dao.insertUserUploadImageStorage(storage);
    }

    public ImageStorage getById(String id) {
        return dao.getByImageId(id);
    }

    public UserUploadImageStorage getByUserAndImage(Long userId, String imageId) {
        if (userId == null || imageId == null) {
            return null;
        }
        return dao.getUploadByUserAndImageId(userId, imageId);
    }
}
