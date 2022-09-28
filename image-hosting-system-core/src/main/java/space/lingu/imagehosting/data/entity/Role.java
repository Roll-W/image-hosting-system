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

package space.lingu.imagehosting.data.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author RollW
 */
public enum Role {
    USER(new SimpleGrantedAuthority("USER")),
    ADMIN(new SimpleGrantedAuthority("ADMIN")),
    GUEST(new SimpleGrantedAuthority("GUEST"));

    private final SimpleGrantedAuthority authority;

    Role(SimpleGrantedAuthority authority) {
        this.authority = authority;
    }

    public boolean hasPrivilege() {
        return this == ADMIN;
    }

    public GrantedAuthority toAuthority() {
        return authority;
    }
}
