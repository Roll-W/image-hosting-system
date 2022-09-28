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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.lingu.imagehosting.data.dto.HttpResponseEntity;
import space.lingu.imagehosting.data.dto.MessagePackage;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.service.user.UserService;

/**
 * @author RollW
 */
@Controller
@UserApi
public class UserVerifyController {
    private UserService userService;

    private final Logger logger =
            LoggerFactory.getLogger(UserVerifyController.class);

    @GetMapping(value = "/register/confirm/{token}")
    public HttpResponseEntity<UserInfo> confirmRegister(
            @PathVariable(value = "token") String token) {
        MessagePackage<UserInfo> infoMessagePackage =
                userService.verifyToken(token);

        return HttpResponseEntity.create(
                infoMessagePackage.toResponseBody());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
