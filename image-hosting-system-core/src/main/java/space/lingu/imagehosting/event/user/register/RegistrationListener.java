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

package space.lingu.imagehosting.event.user.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.Todo;
import space.lingu.imagehosting.common.MailConstant;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.service.user.UserService;
import space.lingu.imagehosting.util.SimpleMailMessageBuilder;

import java.util.UUID;

/**
 * @author RollW
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Override
    public void onApplicationEvent(@NonNull OnRegistrationCompleteEvent event) {
        handleRegistration(event);
    }

    @Async
    void handleRegistration(OnRegistrationCompleteEvent event) {
        UserInfo user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        if (MailConstant.EMAIL_DISABLED.equals(mailProperties.getUsername())) {
            // if disables mail, directly activate user.
            userService.verifyToken(token);
            return;
        }

        String emailAddress = user.email();
        String subject = "[Lingu] Registration Confirmation";
        String confirmUrl = event.getUrl() + "/api/user/register/confirm/" + token;

        SimpleMailMessage mailMessage = new SimpleMailMessageBuilder()
                .setTo(emailAddress)
                .setSubject(subject)
                .setText(("You are now registering a new account, " +
                        "click %s to confirm activate.").formatted(confirmUrl))
                .setFrom(mailProperties.getUsername())
                .build();
        mailSender.send(mailMessage);
    }

    @Todo(todo = "i18n")
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
