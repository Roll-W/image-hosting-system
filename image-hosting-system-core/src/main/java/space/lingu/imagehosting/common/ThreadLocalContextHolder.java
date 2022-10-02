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

import org.springframework.util.Assert;

/**
 * @author RollW
 */
public class ThreadLocalContextHolder {
    private static final ThreadLocal<Context> contextHolder =
            new ThreadLocal<>();

    public void clearContext() {
        contextHolder.remove();
    }

    public Context getContext() {
        Context ctx = contextHolder.get();
        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }
        return ctx;
    }

    public void setContext(Context context) {
        Assert.notNull(context, "Only non-null Context instances are permitted");
        contextHolder.set(context);
    }

    public Context createEmptyContext() {
        return new Context();
    }
}
