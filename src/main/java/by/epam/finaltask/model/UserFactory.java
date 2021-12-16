package by.epam.finaltask.model;

import by.epam.finaltask.security.PasswordEncoder;

import java.math.BigDecimal;

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

    public User createUserWithoutPassword(int user_id, String email, Role role, boolean banned, BigDecimal cashAmount) {
        return new User(user_id, email, EMPTY_STRING, role, banned, cashAmount);
    }

    public User createUser(int user_id, String email, String password) {
        return new User(user_id, email, password);
    }

    public User createUser(int user_id, String email, String password, Role role, boolean banned,
                           BigDecimal cashAmount) {
        return new User(user_id, email, password, role, banned, cashAmount);
    }

    public User createUserAndHashPassword(String email, String password) {
        return new User(DEFAULT_ID, email, encoder.encode(password));
    }

    public User createUserAndHashPassword(String email, String password, Role role, boolean banned,
                                          BigDecimal cashAmount) {
        return new User(DEFAULT_ID, email, encoder.encode(password), role, banned, cashAmount);
    }
}
