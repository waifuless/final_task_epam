package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.model.User;
import by.epam.finaltask.model.UserFactory;
import by.epam.finaltask.security.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MariaUserManager extends GenericDao<User> implements UserManager {

    private final static Logger LOG = LoggerFactory.getLogger(MariaUserManager.class);

    private final static String USER_EXISTENCE_COLUMN = "user_existence";
    private final static String USER_ID_COLUMN = "user_id";
    private final static String EMAIL_COLUMN = "email";
    private final static String PASSWORD_HASH_COLUMN = "password_hash";
    private final static String ROLE_COLUMN = "role_name";
    private final static String TABLE_NAME = "app_user";
    private final static String SAVE_USER_QUERY =
            "INSERT INTO app_user(email, password_hash, role_id)" +
                    " VALUES(?,?,(SELECT role_id FROM role WHERE role_name = ?));";
    private final static String IS_EXIST_USER_QUERY =
            "SELECT EXISTS(SELECT 1 FROM app_user WHERE email=?) AS user_existence";
    private final static String FIND_USER_BY_EMAIL_QUERY =
            "SELECT user_id as user_id, password_hash as password_hash, role.role_name as role_name FROM app_user" +
                    " INNER JOIN role ON app_user.role_id = role.role_id" +
                    " WHERE email=?";
    private final static String FIND_USER_BY_ID_QUERY =
            "SELECT user_id as user_id, email as email," +
                    " role.role_name as role_name FROM app_user" +
                    " INNER JOIN role ON app_user.role_id = role.role_id" +
                    " WHERE user_id=?";
    private final static String UPDATE_USER_QUERY =
            "UPDATE app_user SET email = ?, password_hash = ?," +
                    " role_id = (SELECT role_id FROM role WHERE role_name = ?)" +
                    " WHERE user_id = ?";
    private final static String DELETE_USER_QUERY =
            "DELETE FROM app_user WHERE user_id = ?";

    private static volatile MariaUserManager instance;

    private final UserFactory userFactory;
    private final PasswordEncoder encoder;

    private MariaUserManager() throws DataSourceDownException {
        super(SAVE_USER_QUERY, FIND_USER_BY_ID_QUERY, UPDATE_USER_QUERY, DELETE_USER_QUERY, TABLE_NAME,
                ConnectionPool.getInstance());
        userFactory = UserFactory.getInstance();
        encoder = PasswordEncoder.getInstance();
    }

    public static MariaUserManager getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (MariaUserManager.class) {
                if (instance == null) {
                    instance = new MariaUserManager();
                }
            }
        }
        return instance;
    }

    @Override
    protected void prepareSaveStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPasswordHash());
        statement.setString(3, user.getRole().name());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPasswordHash());
        statement.setString(3, user.getRole().name());
        statement.setLong(4, user.getUserId());
    }

    @Override
    protected User extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return userFactory.createUserWithoutPassword(resultSet.getInt(USER_ID_COLUMN),
                    resultSet.getString(EMAIL_COLUMN), Role.valueOf(resultSet.getString(ROLE_COLUMN)));
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean isUserExist(String email) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(IS_EXIST_USER_QUERY);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(USER_EXISTENCE_COLUMN);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password)
            throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_EMAIL_QUERY);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String passwordHash = resultSet.getString(PASSWORD_HASH_COLUMN);
                if (encoder.matches(password, passwordHash)) {
                    return Optional.of(userFactory.createUserWithoutPassword(resultSet.getInt(USER_ID_COLUMN),
                            email, Role.valueOf(resultSet.getString(ROLE_COLUMN))));
                }
            }
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return Optional.empty();
    }
}
