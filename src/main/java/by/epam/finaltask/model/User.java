package by.epam.finaltask.model;

public class User implements DaoEntity<User> {

    private final static Role DEFAULT_ROLE = Role.USER;
    private final static boolean DEFAULT_BAN_STATUS = false;

    private final long userId;
    private final String email;
    private final String passwordHash;
    private final Role role;
    private final boolean banned;

    User(long userId, String email, String passwordHash) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = DEFAULT_ROLE;
        this.banned = DEFAULT_BAN_STATUS;
    }

    public User(long userId, String email, String passwordHash, Role role, boolean banned) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.banned = banned;
    }

    public long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public boolean isBanned() {
        return banned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (banned != user.banned) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (passwordHash != null ? !passwordHash.equals(user.passwordHash) : user.passwordHash != null) return false;
        return role == user.role;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (banned ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", role=" + role +
                ", banned=" + banned +
                '}';
    }

    @Override
    public User createWithId(long id) {
        return new User(id, email, getPasswordHash(), role, banned);
    }
}
