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
import org.springframework.security.core.userdetails.UserDetails;
import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.light.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author RollW
 */
@DataTable(tableName = "user", configuration =
@LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "120"), indices = {
        @Index(value = "user_name", unique = true),
        @Index(value = "user_email")
})
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class User implements UserDetails {
    @PrimaryKey(autoGenerate = true)
    @DataColumn(name = "user_id")
    private long id;

    @DataColumn(name = "user_name")
    private String username;

    @DataColumn(name = "user_role", configuration =
    @LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "20"))
    private Role role;

    @DataColumn(name = "user_password")
    private String password;

    @DataColumn(name = "user_password_salt")
    private String salt;

    @DataColumn(name = "user_register_time")
    private long registerTime;

    @DataColumn(name = "user_email")
    private String email;

    @DataColumn(name = "user_enabled")
    private boolean enabled;

    @DataColumn(name = "user_locked")
    private boolean locked;

    @DataColumn(name = "user_account_expired")
    private boolean accountExpired;

    public User() {
        this.enabled = false;
    }

    public User(String username, String password, String salt, Role role,
                long registerTime, String email) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.role = role;
        this.registerTime = registerTime;
        this.email = email;
        this.enabled = false;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public User setLocked(boolean locked) {
        this.locked = locked;
        return this;
    }

    public User setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
        return this;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(getRole().toAuthority());
    }

    @Override
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserInfo toInfo() {
        return new UserInfo(id, username, email);
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public User setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public User setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && registerTime == user.registerTime && enabled == user.enabled && locked == user.locked && accountExpired == user.accountExpired && Objects.equals(username, user.username) && role == user.role && Objects.equals(password, user.password) && Objects.equals(salt, user.salt) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, role, password, salt, registerTime, email, enabled, locked, accountExpired);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", registerTime=" + registerTime +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", locked=" + locked +
                ", accountExpired=" + accountExpired +
                '}';
    }
}

