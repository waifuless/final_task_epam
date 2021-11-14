package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.Region;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MariaRegionManager extends GenericDao<Region> implements RegionManager {

    private final static String REGION_ID_COLUMN = "region_id";
    private final static String REGION_NAME_COLUMN = "region_name";
    private final static String TABLE_NAME = "region";
    private final static String SAVE_REGION_QUERY =
            "INSERT INTO region(region_name)" +
                    " VALUES(?);";
    private final static String FIND_ALL_REGIONS_QUERY =
            "SELECT region_id AS region_id, region_name AS region_name FROM region";
    private final static String FIND_REGION_BY_ID_QUERY = FIND_ALL_REGIONS_QUERY + " WHERE region_id=?";
    private final static String UPDATE_REGION_QUERY =
            "UPDATE region SET region_name = ? WHERE region_id = ?";
    private final static String DELETE_REGION_QUERY =
            "DELETE FROM region WHERE region_id = ?";

    private static volatile MariaRegionManager instance;

    private MariaRegionManager() throws DataSourceDownException {
        super(SAVE_REGION_QUERY, FIND_ALL_REGIONS_QUERY, FIND_REGION_BY_ID_QUERY, UPDATE_REGION_QUERY,
                DELETE_REGION_QUERY, TABLE_NAME, ConnectionPool.getInstance());
    }

    public static MariaRegionManager getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (MariaRegionManager.class) {
                if (instance == null) {
                    instance = new MariaRegionManager();
                }
            }
        }
        return instance;
    }

    @Override
    protected void prepareSaveStatement(PreparedStatement statement, Region region) throws SQLException {
        statement.setString(1, region.getRegionName());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Region region) throws SQLException {
        statement.setString(1, region.getRegionName());
        statement.setLong(2, region.getRegionId());
    }

    @Override
    protected Region extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return new Region(resultSet.getLong(REGION_ID_COLUMN), resultSet.getString(REGION_NAME_COLUMN));
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }
}
