package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.CityOrDistrictDaoImpl;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.model.CityOrDistrict;

import java.util.List;

public interface CityOrDistrictDao extends Dao<CityOrDistrict> {

    static CityOrDistrictDao getInstance() {
        return CityOrDistrictDaoImpl.getInstance();
    }

    List<CityOrDistrict> findByRegion(String regionName)
            throws DaoException;
}
