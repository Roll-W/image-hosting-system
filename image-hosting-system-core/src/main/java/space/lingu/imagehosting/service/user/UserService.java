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

import org.springframework.security.core.userdetails.UserDetailsService;
import space.lingu.imagehosting.data.dto.MessagePackage;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.data.entity.Role;
import space.lingu.imagehosting.data.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RollW
 */
public interface UserService extends UserDetailsService, UserVerifyService {
    String INVALID = "INVALID";

    User INVALID_USER = new User(INVALID,
            INVALID, INVALID, Role.GUEST,
            0, INVALID);

    MessagePackage<UserInfo> registerUser(String username, String password, String email);

    MessagePackage<UserInfo> registerUser(String username, String password,
                                          String email, Role role);

    MessagePackage<UserInfo> loginByUsername(HttpServletRequest request, String username, String password);

    UserInfo getCurrentUser(HttpServletRequest request);

    void logout(HttpServletRequest request);
}
