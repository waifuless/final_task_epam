package by.epam.finaltask.model;

import by.epam.finaltask.security.PasswordEncoder;

public class UserFactory {

    private final static String EMPTY_STRING = "";
    private final static int DEFAULT_ID = -1;

    private static volatile UserFactory instance;
    private final PasswordEncoder encoder;

    private UserFactory() {
        encoder = PasswordEncoder.getInstance();
    }

    public static UserFactory getInstance() {
        if (instance == null) {
            synchronized (UserFactory.class) {
                if (instance == null) {
                    instance = new UserFactory();
                }
            }
        }
        return instance;
    }

    public User createUserWithoutPassword(int user_id, String email, Role role) {
        return new User(user_id, email, EMPTY_STRING, role);
    }

    public User createUser(int user_id, String email, String password) {
        return new User(user_id, email, encoder.encode(password));
    }

    public User createUser(int user_id, String email, String password, Role role) {
        return new User(user_id, email, encoder.encode(password), role);
    }

    public User createUser(String email, String password) {
        return createUser(DEFAULT_ID, email, password);
    }

    public User createUser(String email, String password, Role role) {
        return createUser(DEFAULT_ID, email, password, role);
    }
}
