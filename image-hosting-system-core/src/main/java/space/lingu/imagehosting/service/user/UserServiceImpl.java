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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import space.lingu.imagehosting.Context;
import space.lingu.imagehosting.common.ErrorCode;
import space.lingu.imagehosting.data.database.repository.UserRepository;
import space.lingu.imagehosting.data.database.repository.VerificationTokenRepository;
import space.lingu.imagehosting.data.dto.MessagePackage;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.data.entity.Role;
import space.lingu.imagehosting.data.entity.User;
import space.lingu.imagehosting.data.entity.VerificationToken;
import space.lingu.imagehosting.web.SessionConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

/**
 * @author RollW
 */
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    public UserServiceImpl(@Autowired UserRepository userRepository,
                           @Autowired VerificationTokenRepository verificationTokenRepository) {
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
                .setPassword(password)
                .setEmail(email)
                .setRole(role);
        var info = UserChecker.checkUser(newUser);
        if (!info.code().getState()) {
            return new MessagePackage<>(info.code(), newUser.toInfo());
        }
        if (userRepository.isExistByEmail(email)) {
            return new MessagePackage<>(ErrorCode.ERROR_USER_EXISTED,
                    "Has an existing email address.", newUser.toInfo());
        }
        long time = System.currentTimeMillis();
        newUser.setPassword(info.password())
                .setSalt(info.salt())
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

        logger.debug("create user with " + newUser);
        return new MessagePackage<>(ErrorCode.SUCCESS, newUser.toInfo());
    }

    @Override
    public MessagePackage<UserInfo> loginByUsername(HttpServletRequest request, String username, String password) {
        HttpSession session = request.getSession();
        UserInfo current = getCurrentUser(request);
        if (getCurrentUser(request) != null) {
            return new MessagePackage<>(
                    ErrorCode.ERROR_USER_ALREADY_LOGIN,
                    "User already login, please logout first",
                    current);
        }

        User user = userRepository.getUserByName(username);
        if (user == null) {
            return new MessagePackage<>(ErrorCode.ERROR_USER_NOT_EXIST, "User not exist",
                    new UserInfo(-1, username, password));
        }
        if (verifyPassword(password, user.getPassword(), user.getSalt())) {
            logger.info("now logging in with username {}.", user.getUsername());
            UserInfo userInfo = user.toInfo();

            Context.requireContext().initial(userInfo);
            session.setAttribute(SessionConstants.USER_INFO_SESSION_ID, userInfo);
            return new MessagePackage<>(ErrorCode.SUCCESS, userInfo);
        }
        return new MessagePackage<>(ErrorCode.ERROR_PASSWORD_NOT_CORRECT, "Password not correct", null);
    }

    private boolean verifyPassword(String passwordIn, String password, String salt) {
        String calc1 = UserChecker.calcWithSalt(passwordIn, salt);
        return calc1.equals(password);
    }

    @Override
    public UserInfo getCurrentUser(HttpServletRequest request) {
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

        Context.invalidate();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByName(username);
    }

    @Override
    public void createVerificationToken(UserInfo info, String token) {
        long expiryTime = VerificationToken.calculateExpiryDate();
        VerificationToken verificationToken = new VerificationToken(token, info.id(), expiryTime);

        verificationTokenRepository.insert(verificationToken);
    }

    @Override
    public MessagePackage<UserInfo> verifyToken(String token) {
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token);
        if (isVerificationTokenExpired(verificationToken)) {
            return new MessagePackage<>(ErrorCode.ERROR_TOKEN_EXPIRED, null);
        }
        User user = userRepository
                .getUserById(verificationToken.userId());
        if (user.isEnabled()) {
            return new MessagePackage<>(ErrorCode.ERROR_USER_ALREADY_ACTIVATED, null);
        }

        user.setEnabled(true);
        userRepository.save(user);
        return new MessagePackage<>(ErrorCode.SUCCESS, user.toInfo());
    }

    private boolean isVerificationTokenExpired(VerificationToken token) {
        Calendar cal = Calendar.getInstance();
        return cal.getTime().getTime() > token.expiryDate();
    }
}
