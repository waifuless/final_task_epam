package by.epam.finaltask.service;

import by.epam.finaltask.dao.UserManager;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.User;
import by.epam.finaltask.model.UserContext;
import by.epam.finaltask.model.UserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class CommonUserService implements UserService {

    public final static Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    public final static int MAX_EMAIL_LENGTH = 254;
    private final static int USERS_PER_PAGE = 8;
    private final static Logger LOG = LoggerFactory.getLogger(CommonUserService.class);
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
                userManager.save(UserFactory.getInstance().createUserAndHashPassword(email, password));
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

    @Override
    public Optional<User> findUserById(long id) throws ServiceCanNotCompleteCommandRequest {
        try {
            return userManager.find(id);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public List<User> findUsersByPageAndContext(long pageNumber, UserContext context)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            return userManager.findByUserContext(context, (pageNumber - 1) * USERS_PER_PAGE, USERS_PER_PAGE);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public long findUsersPagesCount(UserContext context) throws ServiceCanNotCompleteCommandRequest {
        try {
            long lotsCount = userManager.findUsersCount(context);
            return lotsCount / USERS_PER_PAGE + (lotsCount % USERS_PER_PAGE == 0 ? 0 : 1);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void changeUserBannedStatus(long id, String action) throws ServiceCanNotCompleteCommandRequest,
            ClientErrorException {
        try {
            if (action == null || action.trim().isEmpty()) {
                throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
            }
            boolean bannedAction = action.equals("ban");
            Optional<User> optionalOldUserState = userManager.find(id);
            if (optionalOldUserState.isPresent() && optionalOldUserState.get().isBanned() != bannedAction) {
                User oldUserState = optionalOldUserState.get();
                userManager.update(new User(oldUserState.getUserId(), oldUserState.getEmail(),
                        oldUserState.getPasswordHash(), oldUserState.getRole(), bannedAction,
                        oldUserState.getCashAccount()));
            }
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void plusToCashAccount(long userId, BigDecimal cash)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        validateCash(cash);
        addToCashAccount(userId, cash);
    }

    @Override
    public void minusFromCashAccount(long userId, BigDecimal cash)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        validateCash(cash);
        addToCashAccount(userId, cash.negate());
    }

    private void validateCash(BigDecimal cash) throws ClientErrorException, ServiceCanNotCompleteCommandRequest {
        if (cash == null) {
            throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
        }
        if (cash.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceCanNotCompleteCommandRequest("Cash amount should be >= 0");
        }
    }

    private void addToCashAccount(long userId, BigDecimal cash)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            Optional<User> optionalOldUserState = userManager.find(userId);
            if (optionalOldUserState.isPresent()) {
                User oldUserState = optionalOldUserState.get();
                if (oldUserState.isBanned()) {
                    throw new ClientErrorException(ClientError.FORBIDDEN);
                }
                userManager.update(new User(oldUserState.getUserId(), oldUserState.getEmail(),
                        oldUserState.getPasswordHash(), oldUserState.getRole(), oldUserState.isBanned(),
                        oldUserState.getCashAccount().add(cash)));
            }
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage());
            throw ex;
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
