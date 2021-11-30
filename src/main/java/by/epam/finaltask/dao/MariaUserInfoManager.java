package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MariaUserInfoManager extends GenericDao<UserInfo> implements UserInfoManager {

    private final static Logger LOG = LoggerFactory.getLogger(MariaUserInfoManager.class);

    private final static String USER_INFO_EXISTENCE_COLUMN = "user_info_existence";
    private final static String ID_COLUMN = "user_id";
    private final static String PHONE_NUMBER_COLUMN = "phone_number";
    private final static String FIRST_NAME_COLUMN = "first_name";
    private final static String LAST_NAME_COLUMN = "last_name";
    private final static String ADDRESS_NAME_COLUMN = "address_name";
    private final static String CITY_OR_DISTRICT_NAME_COLUMN = "city_or_district_name";
    private final static String REGION_NAME_COLUMN = "region_name";
    private final static String POSTAL_CODE_NAME_COLUMN = "postal_code_name";
    private final static String TABLE_NAME = "user_info";
    private final static String SAVE_USER_INFO_QUERY =
            "SET @regionId = findRegionId(?);" +
                    "INSERT INTO user_info(user_id, phone_number, first_name, last_name," +
                    " address_id, city_or_district_id, region_id, postal_code_id)" +
                    "VALUES(?,?,?,?," +
                    "insertAddressIfNotExistAndSelectId(?)," +
                    "findCityOrDistrictId(?, @regionId)," +
                    "@regionId," +
                    "insertPostalCodeIfNotExistAndSelectId(?));";
    private final static String IS_USER_INFO_EXIST_QUERY =
            "SELECT EXISTS(SELECT 1 FROM user_info WHERE user_id=?) AS user_info_existence";
    private final static String FIND_ALL_USER_INFO_QUERY =
            "SELECT user_id AS user_id, phone_number AS phone_number, first_name AS first_name, last_name AS last_name," +
                    " address_name AS address_name, city_or_district_name AS city_name, region_name AS region_name," +
                    " postal_code_name AS postal_code_name" +
                    " FROM user_info" +
                    " LEFT JOIN address a on user_info.address_id = a.address_id" +
                    " LEFT JOIN city_or_district c on user_info.city_or_district_id = c.city_or_district_id" +
                    " LEFT JOIN region r on user_info.region_id = r.region_id" +
                    " LEFT JOIN postal_code pc on user_info.postal_code_id = pc.postal_code_id";
    private final static String FIND_USER_INFO_BY_ID_QUERY = FIND_ALL_USER_INFO_QUERY + " WHERE user_id=?";
    private final static String UPDATE_USER_INFO_QUERY =
            "SET @regionId = findRegionId(?);" +
                    "UPDATE user_info SET phone_number = ?, first_name = ?, last_name = ?," +
                    " address_id = insertAddressIfNotExistAndSelectId(?)," +
                    " city_or_district_id = findCityOrDistrictId(?, @regionId)," +
                    " region_id = @regionId," +
                    " postal_code_id = insertPostalCodeIfNotExistAndSelectId(?)" +
                    " WHERE user_id = ?";
    private final static String DELETE_USER_INFO_QUERY =
            "DELETE FROM user_info WHERE user_id = ?";

    private static volatile MariaUserInfoManager instance;

    private MariaUserInfoManager() throws DataSourceDownException {
        super(SAVE_USER_INFO_QUERY, FIND_ALL_USER_INFO_QUERY, FIND_USER_INFO_BY_ID_QUERY, UPDATE_USER_INFO_QUERY,
                DELETE_USER_INFO_QUERY, TABLE_NAME, ConnectionPool.getInstance());
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
    protected void prepareSaveStatement(PreparedStatement statement, UserInfo userInfo) throws SQLException {
        statement.setString(1, userInfo.getRegion());
        statement.setLong(2, userInfo.getUserId());
        statement.setString(3, userInfo.getPhoneNumber());
        statement.setString(4, userInfo.getFirstName());
        statement.setString(5, userInfo.getLastName());
        statement.setString(6, userInfo.getAddress());
        statement.setString(7, userInfo.getCity());
        statement.setString(8, userInfo.getPostalCode());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, UserInfo userInfo) throws SQLException {
        statement.setString(1, userInfo.getRegion());
        statement.setString(2, userInfo.getPhoneNumber());
        statement.setString(3, userInfo.getFirstName());
        statement.setString(4, userInfo.getLastName());
        statement.setString(5, userInfo.getAddress());
        statement.setString(6, userInfo.getCity());
        statement.setString(7, userInfo.getPostalCode());
        statement.setLong(8, userInfo.getUserId());
    }

    @Override
    protected UserInfo extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return new UserInfo(resultSet.getLong(ID_COLUMN), resultSet.getString(PHONE_NUMBER_COLUMN),
                    resultSet.getString(FIRST_NAME_COLUMN), resultSet.getString(LAST_NAME_COLUMN),
                    resultSet.getString(ADDRESS_NAME_COLUMN), resultSet.getString(CITY_OR_DISTRICT_NAME_COLUMN),
                    resultSet.getString(REGION_NAME_COLUMN), resultSet.getString(POSTAL_CODE_NAME_COLUMN));
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean isUserInfoExist(long id) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(IS_USER_INFO_EXIST_QUERY);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(USER_INFO_EXISTENCE_COLUMN);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }
}
