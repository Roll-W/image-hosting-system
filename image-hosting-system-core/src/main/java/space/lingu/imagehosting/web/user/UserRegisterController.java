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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.lingu.imagehosting.configuration.WebUrls;
import space.lingu.imagehosting.data.dto.HttpResponseEntity;
import space.lingu.imagehosting.data.dto.MessagePackage;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.event.register.OnRegistrationCompleteEvent;
import space.lingu.imagehosting.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author RollW
 */
@UserApi
@RestController
public class UserRegisterController {


    @GetMapping("/register")
    public HttpResponseEntity<UserInfo> register(HttpServletRequest request,
                                                 @RequestParam String username,
                                                 @RequestParam String password,
                                                 @RequestParam String email) {

        MessagePackage<UserInfo> infoMessagePackage =
                userService.registerUser(username, password, email);

        if (infoMessagePackage.errorCode().getState()) {
            OnRegistrationCompleteEvent event = new OnRegistrationCompleteEvent(
                    infoMessagePackage.data(),
                    Locale.getDefault(),
                    webUrls.backendUrl());
            eventPublisher.publishEvent(event);
        }
        return HttpResponseEntity.create(
                infoMessagePackage.toResponseBody()
        );
    }

    UserService userService;

    ApplicationEventPublisher eventPublisher;

    WebUrls webUrls;

    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    public void setWebUrls(WebUrls webUrls) {
        this.webUrls = webUrls;
    }
}
