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

import space.lingu.imagehosting.data.dto.HttpResponseEntity;
import space.lingu.imagehosting.data.dto.MessagePackage;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.lingu.Todo;
import space.lingu.imagehosting.common.ErrorCode;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RollW
 */
@UserApi
@RestController
public class UserLoginController {

    @RequestMapping(value = "/login",
            method = {RequestMethod.GET, RequestMethod.PUT})
    @Todo(todo = "use Spring Security")
    public HttpResponseEntity<UserInfo> login(HttpServletRequest request,
                                              @RequestParam String username,
                                              @RequestParam String password) {
        if (StringUtils.isEmpty(username)) {
            return HttpResponseEntity.failure(
                    "Username cannot be empty.",
                    ErrorCode.ERROR_PARAM_MISSING);
        }
        if (StringUtils.isEmpty(password)) {
            return HttpResponseEntity.failure(
                    "Password cannot be empty.",
                    ErrorCode.ERROR_PARAM_MISSING);
        }

        MessagePackage<UserInfo> infoMessagePackage =
                userService.loginByUsername(request, username, password);
        return HttpResponseEntity.create(infoMessagePackage.toResponseBody());
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

    private UserService userService;

    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }

}
