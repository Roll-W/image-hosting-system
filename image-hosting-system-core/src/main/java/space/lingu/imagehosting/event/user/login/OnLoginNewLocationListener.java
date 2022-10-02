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

package space.lingu.imagehosting.event.user.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.imagehosting.common.MailConstant;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.service.user.UserService;
import space.lingu.imagehosting.util.SimpleMailMessageBuilder;

/**
 * @author RollW
 */
@Component
public class OnLoginNewLocationListener implements ApplicationListener<OnLoginNewLocationEvent> {
    @Override
    public void onApplicationEvent(@NonNull OnLoginNewLocationEvent event) {
        handleLoginEvent(event);
    }

    @Async
    void handleLoginEvent(OnLoginNewLocationEvent event) {
        if (MailConstant.EMAIL_DISABLED.equals(mailProperties.getUsername())) {
            // if disables mail, directly activate user.
            return;
        }
        UserInfo userInfo = event.getUserInfo();
        if (userInfo.id() > 0) {
            return;
        }
        // TODO: temp disable

        final String subject = "[Lingu] Login at a new position";
        String greeting = "Dear " + userInfo.username();
        final String message = greeting +
                ", \nYou are logging in through a new address, " +
                "if this is not you, please change your password promptly. \n\nLocation: 127.0.0.1.";

        SimpleMailMessage mailMessage = new SimpleMailMessageBuilder()
                .setTo(userInfo.email())
                .setSubject(subject)
                .setText(message)
                .setFrom(mailProperties.getUsername())
                .build();
        mailSender.send(mailMessage);
    }

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private JavaMailSender mailSender;

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private MailProperties mailProperties;

    @Autowired
    public void setMailProperties(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }
}
