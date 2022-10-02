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

import org.apache.commons.lang3.StringUtils;
import space.lingu.NonNull;
import space.lingu.imagehosting.common.ErrorCode;
import space.lingu.imagehosting.data.dto.UserPasswordDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author RollW
 */
public class UserChecker {
    @NonNull
    public static ErrorCode checkUser(UserPasswordDto user) {
        if (!checkUsername(user.username())) {
            return ErrorCode.ERROR_USERNAME_NON_COMPLIANCE;
        }
        if (!checkPassword(user.password())) {
            return ErrorCode.ERROR_PASSWORD_NON_COMPLIANCE;
        }

        if (!checkEmail(user.email())) {
            return ErrorCode.ERROR_EMAIL_NON_COMPLIANCE;
        }

        return ErrorCode.SUCCESS;
    }

    public static final String USERNAME_REGEX = "^[a-zA-Z]\\w{3,18}$";
    public static final String PASSWORD_REGEX = "^[A-Za-z\\d._~!@#$^&*]{3,20}$";
    public static final String EMAIL_REGEX = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    private static final Pattern sUsernamePattern = Pattern.compile(USERNAME_REGEX);
    private static final Pattern sPasswordPattern = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern sEmailPattern = Pattern.compile(EMAIL_REGEX);

    /**
     * 检查用户名是否合规<br>
     * 用户名规范：
     * <br>
     * <ul>
     *     <li>允许数字、字母及下划线</li>
     *     <li>最短3位，最长20位</li>
     *     <li>禁止除下划线外的特殊字符</li>
     * </ul>
     *
     * @param username 用户名
     * @return 是否合规
     */
    private static boolean checkUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        if (username.length() > 20 || username.length() < 3) {
            return false;
        }
        Matcher matcher = sUsernamePattern.matcher(username);
        return matcher.matches();
    }

    /**
     * 检查密码是否合规<br>
     * 密码规范：<br>
     * <ul>
     *     <li>允许数字、字母及部分特殊字符</li>
     *     <li>最短3位，最长18位</li>
     * </ul>
     *
     * @param password 密码
     * @return 是否合规
     */
    private static boolean checkPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        if (password.length() > 18 || password.length() < 3) {
            return false;
        }
        Matcher matcher = sPasswordPattern.matcher(password);
        return matcher.matches();
    }

    private static boolean checkEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        Matcher matcher = sEmailPattern.matcher(email);
        return matcher.matches();
    }
}
