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

import space.lingu.NonNull;
import space.lingu.Todo;

/**
 * @author RollW
 */
@Todo(todo = "TODO: doesn't work")
// TODO: doesn't work
public class ContextHolder {
    private static final ThreadLocalContextHolder contextHolder = new ThreadLocalContextHolder();

    @NonNull
    public static Context requireContext() {
        return contextHolder.getContext();
    }

    public static void invalidate() {
        requireContext().invalid();
        contextHolder.clearContext();
    }
}
