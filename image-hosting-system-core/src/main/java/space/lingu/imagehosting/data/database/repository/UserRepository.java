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
import space.lingu.imagehosting.data.database.dao.UserDao;
import space.lingu.imagehosting.data.entity.User;

/**
 * @author RollW
 */
@Repository
public class UserRepository {
    private final UserDao userDao;

    public UserRepository() {
        this.userDao = ImageHostDatabase.getDatabase().getUserDao();
    }

    public boolean isExistByEmail(String email) {
        return userDao.getUsernameByEmail(email) != null;
    }

    public boolean isExistById(long id) {
        return userDao.getUsernameById(id) != null;
    }

    @Async
    public void save(User user) {
        insertOrUpdate(user);
    }

    private void insertOrUpdate(User user) {
        if (isExistById(user.getId())) {
            userDao.update(user);
            return;
        }
        userDao.insert(user);
    }

    public long insert(User user) {
        return userDao.insert(user);
    }

    @Async
    public void update(User user) {
        userDao.update(user);
    }

    public User getUserById(long userId) {
        return userDao.getUserById(userId);
    }

    public User getUserByName(String username) {
        return userDao.getUserByName(username);
    }
}
