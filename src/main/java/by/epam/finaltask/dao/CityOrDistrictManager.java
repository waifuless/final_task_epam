package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.CityOrDistrict;

import java.sql.SQLException;
import java.util.List;

public interface CityOrDistrictManager extends Dao<CityOrDistrict> {

    static CityOrDistrictManager getInstance() throws DataSourceDownException {
        return MariaCityOrDistrictManager.getInstance();
    }

    List<CityOrDistrict> findByRegion(String regionName)
            throws SQLException, DataSourceDownException, InterruptedException;
}
