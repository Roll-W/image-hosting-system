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

package space.lingu.imagehosting;

import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.imagehosting.data.entity.User;
import space.lingu.NonNull;

/**
 * @author RollW
 */
public class Context {
    private UserInfo currentUser;

    private Context() {
    }

    public void initial(UserInfo info) {
        currentUser = info;
    }

    public void invalid() {
        currentUser = null;
    }

    public void initial(User user) {
        currentUser = user.toInfo();
    }

    public UserInfo getCurrentUser() {
        return currentUser;
    }

    private static final ThreadLocal<Context> sLocalContext =
            ThreadLocal.withInitial(Context::new);

    @NonNull
    public static Context requireContext() {
        return sLocalContext.get();
    }

    public static void invalidate() {
        requireContext().invalid();
        sLocalContext.remove();
    }
}
