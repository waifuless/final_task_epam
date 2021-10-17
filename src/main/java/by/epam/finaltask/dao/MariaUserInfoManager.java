package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MariaUserInfoManager implements UserInfoManager{

    private final static String USER_INFO_EXISTENCE_COLUMN = "user_info_existence";
    private final static String PHONE_NUMBER_COLUMN = "phone_number";
    private final static String FIRST_NAME_COLUMN = "first_name";
    private final static String LAST_NAME_COLUMN = "last_name";
    private final static String ADDRESS_NAME_COLUMN = "address_name";
    private final static String CITY_NAME_COLUMN = "city_name";
    private final static String REGION_NAME_COLUMN = "region_name";
    private final static String POSTAL_CODE_NAME_COLUMN = "postal_code_name";
    private final static String INSERT_USER_INFO_QUERY =
            "INSERT INTO user_info(user_id, phone_number, first_name, last_name," +
                    " address_id, city_id, region_id, postal_code_id)" +
                    "VALUES(?,?,?,?," +
                    "insertAddressIfNotExistAndSelectId(?)," +
                    "insertCityIfNotExistAndSelectId(?)," +
                    "insertRegionIfNotExistAndSelectId(?)," +
                    "insertPostalCodeIfNotExistAndSelectId(?))";
    private final static String IS_USER_INFO_EXIST_QUERY =
            "SELECT EXISTS(SELECT 1 FROM user_info WHERE user_id=?) AS user_info_existence";
    private final static String FIND_USER_INFO_QUERY =
            "SELECT phone_number AS phone_number, first_name AS first_name, last_name AS last_name," +
                    " address_name AS address_name, city_name AS city_name, region_name AS region_name," +
                    " postal_code_name AS postal_code_name" +
                    " FROM user_info" +
                    " INNER JOIN address a on user_info.address_id = a.address_id" +
                    " INNER JOIN city c on user_info.city_id = c.city_id" +
                    " INNER JOIN region r on user_info.region_id = r.region_id" +
                    " INNER JOIN postal_code pc on user_info.postal_code_id = pc.postal_code_id" +
                    " WHERE user_id=?";
    private final static String UPDATE_USER_INFO_QUERY =
            "UPDATE user_info SET phone_number = ?, first_name = ?, last_name = ?," +
                    " address_id = insertAddressIfNotExistAndSelectId(?)," +
                    " city_id = insertCityIfNotExistAndSelectId(?)," +
                    " region_id = insertRegionIfNotExistAndSelectId(?)," +
                    " postal_code_id = insertPostalCodeIfNotExistAndSelectId(?)" +
                    " WHERE user_id = ?";
    private final static String DELETE_USER_INFO_QUERY =
            "DELETE FROM user_info WHERE user_id = ?";

    private static volatile MariaUserInfoManager instance;

    private final ConnectionPool connectionPool;

    private MariaUserInfoManager() throws DataSourceDownException {
        connectionPool = ConnectionPool.getInstance();
    }

    public static MariaUserInfoManager getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (MariaUserInfoManager.class) {
                if (instance == null) {
                    instance = new MariaUserInfoManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void save(UserInfo userInfo) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(INSERT_USER_INFO_QUERY)) {
                statement.setInt(1, userInfo.getUserId());
                statement.setString(2, userInfo.getPhoneNumber());
                statement.setString(3, userInfo.getFirstName());
                statement.setString(4, userInfo.getLastName());
                statement.setString(5, userInfo.getAddress());
                statement.setString(6, userInfo.getCity());
                statement.setString(7, userInfo.getRegion());
                statement.setString(8, userInfo.getPostalCode());
                statement.execute();
            }
        }
    }

    @Override
    public boolean isUserInfoExist(int userId) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(IS_USER_INFO_EXIST_QUERY)) {
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                return resultSet.getBoolean(USER_INFO_EXISTENCE_COLUMN);
            }
        }
    }

    @Override
    public UserInfo findUserInfo(int userId) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(FIND_USER_INFO_QUERY)) {
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                return new UserInfo(userId, resultSet.getString(PHONE_NUMBER_COLUMN),
                        resultSet.getString(FIRST_NAME_COLUMN), resultSet.getString(LAST_NAME_COLUMN),
                        resultSet.getString(ADDRESS_NAME_COLUMN), resultSet.getString(CITY_NAME_COLUMN),
                        resultSet.getString(REGION_NAME_COLUMN), resultSet.getString(POSTAL_CODE_NAME_COLUMN));
            }
        }
    }

    @Override
    public void updateUserInfo(int userId, UserInfo newUserInfo)
            throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER_INFO_QUERY)) {
                statement.setString(1, newUserInfo.getPhoneNumber());
                statement.setString(2, newUserInfo.getFirstName());
                statement.setString(3, newUserInfo.getLastName());
                statement.setString(4, newUserInfo.getAddress());
                statement.setString(5, newUserInfo.getCity());
                statement.setString(6, newUserInfo.getRegion());
                statement.setString(7, newUserInfo.getPostalCode());
                statement.setInt(8, newUserInfo.getUserId());
                statement.execute();
            }
        }
    }

    @Override
    public void deleteUserInfo(int userId) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_INFO_QUERY)) {
                statement.setInt(1, userId);
                statement.execute();
            }
        }
    }
}
