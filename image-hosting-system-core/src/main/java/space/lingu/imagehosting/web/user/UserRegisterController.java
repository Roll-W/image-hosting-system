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

package space.lingu.imagehosting.web.user;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.lingu.imagehosting.data.dto.HttpResponseEntity;
import space.lingu.imagehosting.data.dto.MessagePackage;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.data.dto.request.UserRegisterRequest;
import space.lingu.imagehosting.service.user.UserService;

/**
 * @author RollW
 */
@UserApi
@RestController
public class UserRegisterController {
    static final String MISSING_PARAM = "Param %s cannot be empty.";

    @PostMapping("/register")
    public HttpResponseEntity<UserInfo> register(@RequestBody UserRegisterRequest registerRequest) {


        Validate.notEmpty(registerRequest.getUsername(), MISSING_PARAM, "username");
        Validate.notEmpty(registerRequest.getPassword(), MISSING_PARAM, "password");
        Validate.notEmpty(registerRequest.getEmail(), MISSING_PARAM, "email");

        MessagePackage<UserInfo> infoMessagePackage =
                userService.registerUser(
                        registerRequest.getUsername(),
                        registerRequest.getPassword(),
                        registerRequest.getEmail()
                );
        return HttpResponseEntity.create(
                infoMessagePackage.toResponseBody()
        );
    }

    UserService userService;
    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }
}
