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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.lingu.imagehosting.common.ContextHolder;
import space.lingu.imagehosting.common.ErrorCode;
import space.lingu.imagehosting.data.database.repository.UserRepository;
import space.lingu.imagehosting.data.database.repository.VerificationTokenRepository;
import space.lingu.imagehosting.data.dto.MessagePackage;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.data.dto.UserPasswordDto;
import space.lingu.imagehosting.data.entity.RegisterVerificationToken;
import space.lingu.imagehosting.data.entity.Role;
import space.lingu.imagehosting.data.entity.User;
import space.lingu.imagehosting.event.user.login.OnLoginNewLocationEvent;
import space.lingu.imagehosting.event.user.register.OnRegistrationCompleteEvent;
import space.lingu.imagehosting.properties.WebUrlsProperties;
import space.lingu.imagehosting.web.SessionConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author RollW
 */
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           VerificationTokenRepository verificationTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public MessagePackage<UserInfo> registerUser(String username,
                                                 String password,
                                                 String email) {
        return registerUser(username, password, email, Role.USER);
    }


    @Override
    public MessagePackage<UserInfo> registerUser(String username, String password,
                                                 String email, Role role) {
        User user = userRepository.getUserByName(username);
        if (user != null) {
            return new MessagePackage<>(ErrorCode.ERROR_USER_EXISTED,
                    "A user with same name is existed.", user.toInfo());
        }
        User newUser = new User()
                .setUsername(username)
                .setEmail(email)
                .setRole(role);
        ErrorCode errorCode = UserChecker.checkUser(
                new UserPasswordDto(username, password, email));

        if (!errorCode.getState()) {
            return new MessagePackage<>(errorCode, newUser.toInfo());
        }
        if (userRepository.isExistByEmail(email)) {
            return new MessagePackage<>(ErrorCode.ERROR_USER_EXISTED,
                    "Has an existing email address.", newUser.toInfo());
        }
        long time = System.currentTimeMillis();
        final String encodePassword = passwordEncoder.encode(password);

        newUser.setPassword(encodePassword)
                .setRegisterTime(time)
                .setLocked(false)
                .setEnabled(false)
                .setAccountExpired(false);

        long id = userRepository.insert(newUser);
        newUser.setId(id);
        if (id == 1) {
            logger.info("creating user with ID 1, defaults role to ADMIN.");
            newUser.setRole(Role.ADMIN)
                    .setEnabled(true);
            userRepository.update(newUser);
        }
        if (!newUser.isEnabled()) {
            OnRegistrationCompleteEvent event = new OnRegistrationCompleteEvent(
                    newUser.toInfo(),
                    Locale.getDefault(),
                    webUrls.getBackendUrl());
            eventPublisher.publishEvent(event);
        }
        logger.debug("create user with " + newUser);
        return new MessagePackage<>(ErrorCode.SUCCESS, newUser.toInfo());
    }

    @Override
    public MessagePackage<UserInfo> loginByUsername(HttpServletRequest request,
                                                    String username, String password) {
        UserInfo current = getCurrentUser(request);
        if (getCurrentUser(request) != null) {
            return new MessagePackage<>(
                    ErrorCode.ERROR_USER_ALREADY_LOGIN,
                    "User already login, please logout first",
                    current);
        }
        HttpSession session = request.getSession();
        User user = userRepository.getUserByName(username);
        if (user == null) {
            return new MessagePackage<>(ErrorCode.ERROR_USER_NOT_EXIST, "User not exist",
                    new UserInfo(-1, username, password));
        }
        if (verifyPassword(password, user.getPassword())) {
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(username, password);
            authentication = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("now logging in with username {}.", user.getUsername());
            UserInfo userInfo = user.toInfo();

            ContextHolder.requireContext().initial(user.toInfo());

            session.setAttribute(SessionConstants.USER_INFO_SESSION_ID, userInfo);

            OnLoginNewLocationEvent event =
                    new OnLoginNewLocationEvent(userInfo);
            eventPublisher.publishEvent(event);

            return new MessagePackage<>(ErrorCode.SUCCESS, userInfo);
        }
        return new MessagePackage<>(ErrorCode.ERROR_PASSWORD_NOT_CORRECT, "Password not correct", null);
    }

    private boolean verifyPassword(String passwordIn, String password) {
        return passwordEncoder.matches(passwordIn, password);
    }

    @Override
    public UserInfo getCurrentUser(HttpServletRequest request) {
        logger.info("get current user {}.", ContextHolder.requireContext().getCurrentUser());

        HttpSession session = request.getSession();
        Object o = session.getAttribute(SessionConstants.USER_INFO_SESSION_ID);
        if (o == null) {
            return null;
        }
        if (!(o instanceof UserInfo)) {
            throw new IllegalArgumentException("User info not correctly set.");
        }
        return (UserInfo) o;
    }

    @Override
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        ContextHolder.invalidate();
    }

    @Override
    public void createVerificationToken(UserInfo info, String token) {
        long expiryTime = RegisterVerificationToken.calculateExpiryDate();
        RegisterVerificationToken verificationToken =
                new RegisterVerificationToken(token, info.id(), expiryTime, false);

        verificationTokenRepository.insert(verificationToken);
    }

    @Override
    public MessagePackage<UserInfo> verifyToken(String token) {
        RegisterVerificationToken verificationToken =
                verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return new MessagePackage<>(ErrorCode.ERROR_TOKEN_NOT_EXIST,
                    "Verification token not exist.",
                    null);
        }
        if (verificationToken.used()) {
            return new MessagePackage<>(
                    ErrorCode.ERROR_TOKEN_USED,
                    "Verification Token has been used.",
                    null);
        }
        if (isVerificationTokenExpired(verificationToken)) {
            return new MessagePackage<>(
                    ErrorCode.ERROR_TOKEN_EXPIRED,
                    "Verification Token expired.",
                    null);
        }
        User user = userRepository
                .getUserById(verificationToken.userId());
        if (user == null) {
            return new MessagePackage<>(
                    ErrorCode.ERROR_USER_NOT_EXIST,
                    "User not exists",
                    null);
        }

        if (user.isEnabled()) {
            return new MessagePackage<>(
                    ErrorCode.ERROR_USER_ALREADY_ACTIVATED,
                    "User already activated, there is no need to repeat the operation.",
                    null);
        }

        makeTokenExpired(token);

        logger.info("verify user token: {}, userId: {}",
                token, user.getId());
        user.setEnabled(true);
        userRepository.save(user);
        return new MessagePackage<>(ErrorCode.SUCCESS, user.toInfo());
    }

    @Override
    public MessagePackage<Void> resendToken(long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            return new MessagePackage<>(ErrorCode.ERROR_USER_NOT_EXIST,
                    "User not exists", null);
        }
        if (user.isEnabled()) {
            return new MessagePackage<>(ErrorCode.ERROR_USER_ALREADY_ACTIVATED,
                    "User already activated, there is no need to repeat the operation.",
                    null);
        }
        RegisterVerificationToken verificationToken =
                verificationTokenRepository.findByUser(user);
        if (verificationToken != null && !isVerificationTokenExpired(verificationToken)) {
            makeTokenExpired(verificationToken.token());
        }

        OnRegistrationCompleteEvent event = new OnRegistrationCompleteEvent(
                user.toInfo(), Locale.getDefault(), webUrls.getBackendUrl());
        eventPublisher.publishEvent(event);
        return new MessagePackage<>(ErrorCode.SUCCESS, null);
    }

    private void makeTokenExpired(String token) {
        verificationTokenRepository.update(token, true);
    }

    private boolean isVerificationTokenExpired(RegisterVerificationToken token) {
        Calendar cal = Calendar.getInstance();
        return cal.getTime().getTime() > token.expiryDate();
    }

    ApplicationEventPublisher eventPublisher;

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    WebUrlsProperties webUrls;

    @Autowired
    public void setWebUrls(WebUrlsProperties webUrlsProperties) {
        this.webUrls = webUrlsProperties;
    }
}
