package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.CityOrDistrict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MariaCityOrDistrictManager extends GenericDao<CityOrDistrict> implements CityOrDistrictManager {

    private final static String CITY_OR_DISTRICT_ID_COLUMN = "city_or_district_id";
    private final static String CITY_OR_DISTRICT_NAME_COLUMN = "city_or_district_name";
    private final static String REGION_ID_COLUMN = "region_id";
    private final static String TABLE_NAME = "city_or_district";
    private final static String SAVE_CITY_OR_DISTRICT_QUERY =
            "INSERT INTO city_or_district(city_or_district_name, region_id) VALUES(?, ?);";
    private final static String FIND_ALL_CITY_OR_DISTRICTS_QUERY =
            "SELECT city_or_district_id AS city_or_district_id, city_or_district_name AS city_or_district_name," +
                    " region_id AS region_id FROM city_or_district";
    private final static String FIND_CITY_OR_DISTRICT_BY_ID_QUERY = FIND_ALL_CITY_OR_DISTRICTS_QUERY +
            " WHERE city_or_district_id=?";
    private final static String UPDATE_CITY_OR_DISTRICT_QUERY =
            "UPDATE city_or_district SET city_or_district_name = ?, region_id = ? WHERE city_or_district_id = ?";
    private final static String DELETE_CITY_OR_DISTRICT_QUERY =
            "DELETE FROM city_or_district WHERE city_or_district_id = ?";
    private final static String FIND_CITY_OR_DISTRICT_BY_REGION_NAME_QUERY = FIND_ALL_CITY_OR_DISTRICTS_QUERY +
            " INNER JOIN region ON region.region_id = city_or_district.region_id WHERE region.region_name=?";

    private final static Logger LOG = LoggerFactory.getLogger(MariaCityOrDistrictManager.class);

    private static volatile MariaCityOrDistrictManager instance;

    private MariaCityOrDistrictManager() throws DataSourceDownException {
        super(SAVE_CITY_OR_DISTRICT_QUERY, FIND_ALL_CITY_OR_DISTRICTS_QUERY, FIND_CITY_OR_DISTRICT_BY_ID_QUERY,
                UPDATE_CITY_OR_DISTRICT_QUERY, DELETE_CITY_OR_DISTRICT_QUERY, TABLE_NAME, ConnectionPool.getInstance());
    }

    public static MariaCityOrDistrictManager getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (MariaCityOrDistrictManager.class) {
                if (instance == null) {
                    instance = new MariaCityOrDistrictManager();
                }
            }
        }
        return instance;
    }

    @Override
    public List<CityOrDistrict> findByRegion(String regionName)
            throws SQLException, DataSourceDownException, InterruptedException{
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_CITY_OR_DISTRICT_BY_REGION_NAME_QUERY);
            statement.setString(1, regionName);
            ResultSet resultSet = statement.executeQuery();
            return extractAll(resultSet);
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
    protected void prepareSaveStatement(PreparedStatement statement, CityOrDistrict cityOrDistrict)
            throws SQLException {
        statement.setString(1, cityOrDistrict.getCityOrDistrictName());
        statement.setLong(2, cityOrDistrict.getRegionId());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, CityOrDistrict cityOrDistrict)
            throws SQLException {
        statement.setString(1, cityOrDistrict.getCityOrDistrictName());
        statement.setLong(2, cityOrDistrict.getCityOrDistrictId());
    }

    @Override
    protected CityOrDistrict extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return new CityOrDistrict(resultSet.getLong(CITY_OR_DISTRICT_ID_COLUMN),
                    resultSet.getString(CITY_OR_DISTRICT_NAME_COLUMN), resultSet.getLong(REGION_ID_COLUMN));
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }
}
