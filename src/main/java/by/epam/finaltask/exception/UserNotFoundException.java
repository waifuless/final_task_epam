package by.epam.finaltask.exception;

public class UserNotFoundException extends RuntimeException {

    private final static String EXCEPTION_MSG = "User with email: \"%s\" not found. Or password is invalid";

    public UserNotFoundException(String userEmail) {
        super(String.format(EXCEPTION_MSG, userEmail));
    }
}
