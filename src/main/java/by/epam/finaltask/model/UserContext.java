package by.epam.finaltask.model;

public class UserContext {

    private final Long userId;
    private final String email;
    private final Role role;
    private final Boolean banned;

    private UserContext(Long userId, String email, Role role, Boolean banned) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.banned = banned;
    }

    public static UserContextBuilder builder() {
        return new UserContextBuilder();
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getBanned() {
        return banned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserContext that = (UserContext) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (role != that.role) return false;
        return banned != null ? banned.equals(that.banned) : that.banned == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (banned != null ? banned.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserContext{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", banned=" + banned +
                '}';
    }

    public static class UserContextBuilder {
        private Long userId;
        private String email;
        private Role role;
        private Boolean banned;

        public UserContextBuilder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserContextBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserContextBuilder setRole(Role role) {
            this.role = role;
            return this;
        }

        public UserContextBuilder setBanned(Boolean banned) {
            this.banned = banned;
            return this;
        }

        public UserContext build() {
            return new UserContext(userId, email, role, banned);
        }
    }
}
