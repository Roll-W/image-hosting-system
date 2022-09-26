package space.lingu.imagehosting.data.entity;

/**
 * @author RollW
 */
public enum Role {
    USER,
    ADMIN,
    GUEST;

    public boolean hasPrivilege() {
        return this == ADMIN;
    }
}
