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

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import space.lingu.imagehosting.common.ErrorCode;
import space.lingu.imagehosting.data.dto.HttpResponseEntity;
import space.lingu.imagehosting.data.dto.MessagePackage;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.data.dto.request.UserLoginRequest;
import space.lingu.imagehosting.service.user.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RollW
 */
@UserApi
@RestController
public class UserLoginController {
    private final UserService userService;

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResponseEntity<UserInfo> login(HttpServletRequest request,
                                              @RequestBody UserLoginRequest userLoginRequest) {
        if (StringUtils.isEmpty(userLoginRequest.username())) {
            return HttpResponseEntity.failure(
                    "Username cannot be empty.",
                    ErrorCode.ERROR_PARAM_MISSING);
        }
        if (StringUtils.isEmpty(userLoginRequest.password())) {
            return HttpResponseEntity.failure(
                    "Password cannot be empty.",
                    ErrorCode.ERROR_PARAM_MISSING);
        }

        MessagePackage<UserInfo> infoMessagePackage =
                userService.loginByUsername(request, userLoginRequest.username(), userLoginRequest.password());
        return HttpResponseEntity.create(infoMessagePackage.toResponseBody());
    }

    @PostMapping("/test")
    public HttpResponseEntity<UserInfo> test(@RequestBody UserLoginRequest request) {
        return HttpResponseEntity.success(request.toString());
    }

    @GetMapping("/current")
    public HttpResponseEntity<UserInfo> current(HttpServletRequest request) {
        UserInfo userInfo = userService.getCurrentUser(request);
        if (userInfo == null) {
            return HttpResponseEntity.failure(
                    "No user login.",
                    ErrorCode.ERROR_USER_NOT_LOGIN,
                    null);
        }
        return HttpResponseEntity.success(userInfo);
    }

    @GetMapping("/logout")
    public HttpResponseEntity<String> logout(HttpServletRequest request) {
        userService.logout(request);
        return HttpResponseEntity.success();
    }


    @GetMapping("/message")
    public HttpResponseEntity<String> needsLoginFirst() {
        return HttpResponseEntity.failure("need login first.",
                ErrorCode.ERROR_USER_NOT_LOGIN,
                "need login first");
    }


}
