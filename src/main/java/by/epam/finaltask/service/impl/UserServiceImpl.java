package by.epam.finaltask.service.impl;

import by.epam.finaltask.dao.UserDao;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.User;
import by.epam.finaltask.model.UserContext;
import by.epam.finaltask.model.UserFactory;
import by.epam.finaltask.service.BigDecimalNormalizer;
import by.epam.finaltask.service.RegisterError;
import by.epam.finaltask.service.UserService;
import by.epam.finaltask.validation.NumberValidator;
import by.epam.finaltask.validation.StringClientParameterValidator;
import by.epam.finaltask.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserServiceImpl implements UserService {

    public final static Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    public final static int MAX_EMAIL_LENGTH = 254;
    public final static int MIN_PASSWORD_LENGTH = 8;
    public final static int MAX_PASSWORD_LENGTH = 40;
    private final static int USERS_PER_PAGE = 8;
    private final static int MAX_BIG_DECIMAL_LENGTH = 15;
    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final BigDecimalNormalizer bigDecimalNormalizer = BigDecimalNormalizer.getInstance();
    private final StringClientParameterValidator stringValidator = ValidatorFactory.getFactoryInstance()
            .stringParameterValidator();
    private final NumberValidator numberValidator = ValidatorFactory.getFactoryInstance().idValidator();

    private final UserDao userDao;

    UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<RegisterError> register(String email, String password, String passwordRepeat)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            List<RegisterError> errors = validateParameters(email, password, passwordRepeat);
            if (errors.isEmpty()) {
                userDao.save(UserFactory.getInstance().createUserAndHashPassword(email, password));
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
            if (!isEMailValid(email) || !isPasswordValid(password)) {
                throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
            }
            return userDao.findUserByEmailAndPassword(email, password);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public Optional<User> findUserById(long id) throws ServiceCanNotCompleteCommandRequest {
        try {
            numberValidator.validateNumberIsPositive(id);
            return userDao.find(id);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public List<User> findUsersByPageAndContext(long pageNumber, UserContext context)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            numberValidator.validateNumberIsPositive(pageNumber);
            return userDao.findByUserContext(context, (pageNumber - 1) * USERS_PER_PAGE, USERS_PER_PAGE);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public long findUsersPagesCount(UserContext context) throws ServiceCanNotCompleteCommandRequest {
        try {
            long lotsCount = userDao.findUsersCount(context);
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
            numberValidator.validateNumberIsPositive(id);
            stringValidator.validateNotEmpty(action);
            boolean bannedAction = action.equals("ban");
            Optional<User> optionalOldUserState = userDao.find(id);
            if (optionalOldUserState.isPresent() && optionalOldUserState.get().isBanned() != bannedAction) {
                User oldUserState = optionalOldUserState.get();
                userDao.update(new User(oldUserState.getUserId(), oldUserState.getEmail(),
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
        numberValidator.validateNumberIsPositive(userId);
        validateCash(cash);
        addToCashAccount(userId, cash);
    }

    @Override
    public void minusFromCashAccount(long userId, BigDecimal cash)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        numberValidator.validateNumberIsPositive(userId);
        validateCash(cash);
        addToCashAccount(userId, cash.negate());
    }

    private void validateCash(BigDecimal cash)
            throws ClientErrorException, ServiceCanNotCompleteCommandRequest {
        if (cash == null) {
            throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
        }
        if (cash.compareTo(BigDecimal.ZERO) <= 0 || integerDigits(cash) > MAX_BIG_DECIMAL_LENGTH) {
            throw new ServiceCanNotCompleteCommandRequest("Cash amount should be >= 0 and its length <" +
                    MAX_BIG_DECIMAL_LENGTH);
        }
    }

    private void addToCashAccount(long userId, BigDecimal cash)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            cash = bigDecimalNormalizer.normalize(cash);
            Optional<User> optionalOldUserState = userDao.find(userId);
            if (optionalOldUserState.isPresent()) {
                User oldUserState = optionalOldUserState.get();
                if (oldUserState.isBanned()) {
                    throw new ClientErrorException(ClientError.FORBIDDEN);
                }
                userDao.update(new User(oldUserState.getUserId(), oldUserState.getEmail(),
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
        password = password == null ? null : password.trim();
        return password != null && password.length() >= MIN_PASSWORD_LENGTH && password.length() <= MAX_PASSWORD_LENGTH;
    }

    private boolean isEMailValid(String email) {
        email = email == null ? null : email.trim();
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
            if (userDao.isUserExists(email)) {
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

    private int integerDigits(BigDecimal n) {
        n = n.stripTrailingZeros();
        return n.precision() - n.scale();
    }
}
