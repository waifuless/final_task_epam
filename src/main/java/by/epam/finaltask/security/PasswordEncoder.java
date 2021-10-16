package by.epam.finaltask.security;

public interface PasswordEncoder {

    static PasswordEncoder getInstance() {
        return BcryptWithLocalSaltPasswordEncoder.getInstance();
    }

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
