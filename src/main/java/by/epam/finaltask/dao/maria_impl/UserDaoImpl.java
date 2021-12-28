package by.epam.finaltask.dao.maria_impl;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.dao.UserDao;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.*;
import by.epam.finaltask.security.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl extends GenericDao<User> implements UserDao {

    private final static Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);

    private final static String USER_EXISTENCE_COLUMN = "user_existence";
    private final static String USER_ID_COLUMN = "user_id";
    private final static String EMAIL_COLUMN = "email";
    private final static String PASSWORD_HASH_COLUMN = "password_hash";
    private final static String ROLE_COLUMN = "role_name";
    private final static String BANNED_COLUMN = "banned";
    private final static String CASH_ACCOUNT_COLUMN = "cash_account";
    private final static String ROWS_COUNT_COLUMN = "rows_count";
    private final static String TABLE_NAME = "app_user";
    private final static String SAVE_USER_QUERY =
            "INSERT INTO app_user(email, password_hash, role_id, banned, cash_account)" +
                    " VALUES(?,?,(SELECT role_id FROM role WHERE role_name = ?), ?, ?);";
    private final static String IS_EXIST_USER_QUERY =
            "SELECT EXISTS(SELECT 1 FROM app_user WHERE email=?) AS user_existence";
    private final static String FIND_ALL_USER_QUERY =
            "SELECT user_id as user_id, email as email, password_hash as password_hash," +
                    " role.role_name as role_name, banned as banned, cash_account as cash_account FROM app_user" +
                    " LEFT JOIN role ON app_user.role_id = role.role_id";
    private final static String FIND_USER_BY_EMAIL_QUERY = FIND_ALL_USER_QUERY + " WHERE email=?";
    private final static String FIND_USER_BY_ID_QUERY = FIND_ALL_USER_QUERY + " WHERE user_id=?";
    private final static String UPDATE_USER_QUERY =
            "UPDATE app_user SET email = ?, password_hash = ?," +
                    " role_id = (SELECT role_id FROM role WHERE role_name = ?), banned = ?, cash_account = ?" +
                    " WHERE user_id = ?";
    private final static String DELETE_USER_QUERY =
            "DELETE FROM app_user WHERE user_id = ?";
    private final static String COUNT_QUERY_WITH_JOINS = "SELECT COUNT(1) AS rows_count FROM app_user" +
            " LEFT JOIN role ON app_user.role_id = role.role_id";
    private final static String LIKE_STRING_TEMPLATE = " %s LIKE ? ";
    private final static String EQUALS_TEMPLATE = " %s = ? ";

    private static volatile UserDaoImpl instance;

    private final UserFactory userFactory;
    private final PasswordEncoder encoder;

    private UserDaoImpl() {
        super(SAVE_USER_QUERY, FIND_ALL_USER_QUERY, FIND_USER_BY_ID_QUERY, UPDATE_USER_QUERY, DELETE_USER_QUERY,
                TABLE_NAME, ConnectionPool.getInstance());
        userFactory = UserFactory.getInstance();
        encoder = PasswordEncoder.getInstance();
    }

    public static UserDaoImpl getInstance() {
        if (instance == null) {
            synchronized (UserDaoImpl.class) {
                if (instance == null) {
                    instance = new UserDaoImpl();
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
        statement.setBoolean(4, user.isBanned());
        statement.setBigDecimal(5, user.getCashAccount());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPasswordHash());
        statement.setString(3, user.getRole().name());
        statement.setBoolean(4, user.isBanned());
        statement.setBigDecimal(5, user.getCashAccount());
        statement.setLong(6, user.getUserId());
    }

    @Override
    protected User extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return userFactory.createUser(resultSet.getInt(USER_ID_COLUMN), resultSet.getString(EMAIL_COLUMN),
                    resultSet.getString(PASSWORD_HASH_COLUMN), Role.valueOf(resultSet.getString(ROLE_COLUMN)),
                    resultSet.getBoolean(BANNED_COLUMN), resultSet.getBigDecimal(CASH_ACCOUNT_COLUMN));
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean isUserExists(String email) throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(IS_EXIST_USER_QUERY);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(USER_EXISTENCE_COLUMN);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_EMAIL_QUERY);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String passwordHash = resultSet.getString(PASSWORD_HASH_COLUMN);
                if (encoder.matches(password, passwordHash)) {
                    return Optional.of(extractEntity(resultSet));
                }
            }
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findByUserContext(UserContext context, long offset, long count)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            List<Pair<Object, Integer>> params = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_USER_QUERY +
                    createFilterByContextAndFillParamsList(context, params) +
                    " ORDER BY user_id DESC LIMIT ? OFFSET ?");
            int i = prepareParams(statement, params);
            statement.setLong(++i, count);
            statement.setLong(++i, offset);
            ResultSet resultSet = statement.executeQuery();
            return extractAll(resultSet);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    @Override
    public long findUsersCount(UserContext context) throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            List<Pair<Object, Integer>> params = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(COUNT_QUERY_WITH_JOINS +
                    createFilterByContextAndFillParamsList(context, params));
            prepareParams(statement, params);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getLong(ROWS_COUNT_COLUMN);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    private int prepareParams(PreparedStatement statement, List<Pair<Object, Integer>> params) throws SQLException {
        int i = 0;
        for (Pair<Object, Integer> param : params) {
            statement.setObject(++i, param.getL(), param.getR());
        }
        return i;
    }

    private String createFilterByContextAndFillParamsList(UserContext context, List<Pair<Object, Integer>> params) {
        StringBuilder filter = new StringBuilder(" WHERE ");
        if (context.getUserId() != null) {
            filter.append(String.format(" %s = ? ", USER_ID_COLUMN));
            params.add(new Pair<>(context.getUserId(), Types.INTEGER));
        }
        String email = context.getEmail();
        email = email != null ? "%" + email + "%" : null;
        checkParamAndFillFilterAndParamList(email, Types.VARCHAR, LIKE_STRING_TEMPLATE, "email",
                filter, params);
        checkParamAndFillFilterAndParamList(context.getRole().name(), Types.VARCHAR, EQUALS_TEMPLATE,
                ROLE_COLUMN, filter, params);
        checkParamAndFillFilterAndParamList(context.getBanned(), Types.BOOLEAN, EQUALS_TEMPLATE,
                BANNED_COLUMN, filter, params);
        LOG.debug("Filter query: {}", filter);
        LOG.debug("Params map: {}", params);
        if (params.isEmpty()) {
            return "";
        }
        return new String(filter);
    }

    private void checkParamAndFillFilterAndParamList(Object param, Integer paramType, String filterPartTemplate,
                                                     String columnName, StringBuilder filter,
                                                     List<Pair<Object, Integer>> params) {
        if (param != null) {
            if (!params.isEmpty()) {
                filter.append(" AND ");
            }
            filter.append(String.format(filterPartTemplate, columnName));
            params.add(new Pair<>(param, paramType));
        }
    }
}
