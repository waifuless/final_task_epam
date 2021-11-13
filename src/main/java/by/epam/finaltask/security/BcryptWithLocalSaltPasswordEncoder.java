package by.epam.finaltask.security;


import at.favre.lib.crypto.bcrypt.BCrypt;

public class BcryptWithLocalSaltPasswordEncoder implements PasswordEncoder {

    private final static int DEFAULT_COST = 6;
    private final static String LOCAL_SALT = System.getenv("LOCAL_AUCTION_SALT");
    private static volatile BcryptWithLocalSaltPasswordEncoder instance;

    private BcryptWithLocalSaltPasswordEncoder() {
    }

    public static BcryptWithLocalSaltPasswordEncoder getInstance() {
        if (instance == null) {
            synchronized (BcryptWithLocalSaltPasswordEncoder.class) {
                if (instance == null) {
                    instance = new BcryptWithLocalSaltPasswordEncoder();
                }
            }
        }
        return instance;
    }

    @Override
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(DEFAULT_COST, (rawPassword + LOCAL_SALT).toCharArray());
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.verifyer()
                .verify((rawPassword + LOCAL_SALT).toCharArray(), encodedPassword.toCharArray()).verified;
    }
}
