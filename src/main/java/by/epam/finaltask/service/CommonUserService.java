package by.epam.finaltask.service;

import by.epam.finaltask.dao.UserManager;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.User;
import by.epam.finaltask.model.UserFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class CommonUserService implements UserService {

    public final static Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    public final static int MAX_EMAIL_LENGTH = 254;
    private final static Logger LOG = LogManager.getLogger(CommonUserService.class);
    private final UserManager userManager;

    CommonUserService(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public List<RegisterError> register(String email, String password, String passwordRepeat)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            List<RegisterError> errors = validateParameters(email, password, passwordRepeat);
            if (errors.isEmpty()) {
                userManager.save(UserFactory.getInstance().createUser(email, password));
            }
            return errors;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public Optional<User> authenticate(String email, String password) throws ServiceCanNotCompleteCommandRequest {
        try {
            return userManager.findUserByEmailAndPassword(email, password);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8 && password.length() <= 256;
    }

    private boolean isEMailValid(String email) {
        return email != null && VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()
                && email.length() <= MAX_EMAIL_LENGTH;
    }

    private List<RegisterError> validateParameters(String email, String password, String passwordRepeat)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            List<RegisterError> errors = new LinkedList<>();
            if (!isEMailValid(email)) {
                errors.add(RegisterError.EMAIL_INVALID);
            }
            if (userManager.isUserExists(email)) {
                errors.add(RegisterError.USER_WITH_EMAIL_ALREADY_EXISTS);
            }
            if (!isPasswordValid(password)) {
                errors.add(RegisterError.PASSWORD_INVALID);
            }
            if (!password.equals(passwordRepeat)) {
                errors.add(RegisterError.PASSWORDS_NOT_MATCH);
            }
            return errors;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }
}
