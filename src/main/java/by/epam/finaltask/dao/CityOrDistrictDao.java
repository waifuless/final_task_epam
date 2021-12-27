package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.CityOrDistrictDaoImpl;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.CityOrDistrict;

import java.sql.SQLException;
import java.util.List;

public interface CityOrDistrictDao extends Dao<CityOrDistrict> {

    static CityOrDistrictDao getInstance() throws DataSourceDownException {
        return CityOrDistrictDaoImpl.getInstance();
    }

    List<CityOrDistrict> findByRegion(String regionName)
            throws SQLException, DataSourceDownException, InterruptedException;
}
