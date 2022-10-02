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

package space.lingu.imagehosting.service.user;

import space.lingu.imagehosting.data.entity.Role;

import java.util.List;

/**
 * Admin
 *
 * @author RollW
 */
public interface UserManageService extends UserSettingService {
    void createUserDiscardEmail(String username, String password, String email, Role role);

    void createUserDiscardEmail(String username, String password, String email);

    void deleteUser(long userId);

    void deleteUser(List<Long> userId);

    void setRoleTo(long userId, Role role);

}
