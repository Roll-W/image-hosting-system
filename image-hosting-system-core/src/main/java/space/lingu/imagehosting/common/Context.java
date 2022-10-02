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

package space.lingu.imagehosting.common;

import org.apache.commons.lang3.Validate;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.data.entity.User;

import java.util.Objects;

/**
 * @author RollW
 */
public class Context {
    private UserInfo currentUser;

    protected Context() {
    }

    public void initial(UserInfo info) {
        Validate.notNull(info, "UserInfo cannot be null");
        currentUser = info;
    }

    public void invalid() {
        currentUser = null;
    }

    public void initial(User user) {
        Validate.notNull(user, "user cannot be null");
        currentUser = user.toInfo();
    }

    public UserInfo getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context = (Context) o;
        return Objects.equals(currentUser, context.currentUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentUser);
    }
}
