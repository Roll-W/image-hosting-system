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
import space.lingu.imagehosting.data.database.dao.VerificationTokenDao;
import space.lingu.imagehosting.data.entity.RegisterVerificationToken;
import space.lingu.imagehosting.data.entity.User;

/**
 * @author RollW
 */
@Repository
public class VerificationTokenRepository {
    private final VerificationTokenDao dao;

    public VerificationTokenRepository(ImageHostDatabase database) {
        dao = database.getVerificationTokenDao();
    }

    public RegisterVerificationToken findByToken(String token) {
        return dao.findByToken(token);
    }

    public RegisterVerificationToken findByUser(User user) {
        return dao.findByUserId(user.getId());
    }

    public RegisterVerificationToken findByUser(long id) {
        return dao.findByUserId(id);
    }

    @Async
    public void insert(RegisterVerificationToken verificationToken) {
        dao.insert(verificationToken);
    }

    @Async
    public void update(RegisterVerificationToken verificationToken) {
        dao.update(verificationToken);
    }

    @Async
    public void update(String token, boolean used) {
        dao.update();
    }
}
