package space.lingu.imagehosting.data.entity;

import space.lingu.imagehosting.data.dto.UserInfo;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.LightConfiguration;
import space.lingu.light.PrimaryKey;

import java.util.Objects;

/**
 * @author RollW
 */
@DataTable(tableName = "user", configuration =
@LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "120"))
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class User {
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

    public User() {
    }

    public User(String username, String password, String salt, Role role,
                long registerTime, String email) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.role = role;
        this.registerTime = registerTime;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && registerTime == user.registerTime && Objects.equals(username, user.username) && role == user.role && Objects.equals(password, user.password) && Objects.equals(salt, user.salt) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, role, password, salt, registerTime, email);
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
                '}';
    }
}
