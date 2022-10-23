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

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import space.lingu.imagehosting.data.database.ImageHostDatabase;
import space.lingu.imagehosting.data.database.dao.SettingDao;
import space.lingu.imagehosting.data.entity.SettingItem;

/**
 * @author RollW
 */
@Repository
public class SettingRepository {
    private final SettingDao settingDao;

    public SettingRepository(ImageHostDatabase database) {
        settingDao = database.getSettingDao();
        retrieveAll();
    }

    private void retrieveAll() {
        settingDao.get();
    }


    @Cacheable(cacheNames = "setting", key = "#settingItem.key()")
    public SettingItem put(SettingItem settingItem) {
        return settingItem;
    }

    public SettingItem put(String key, String value) {
        return put(new SettingItem(key, value));
    }

    public SettingItem remove(String key, String value) {
        return remove(new SettingItem(key, value));
    }

    public SettingItem remove(SettingItem settingItem) {
        return settingItem;
    }
}