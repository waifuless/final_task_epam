package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.RegionDaoImpl;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Region;

public interface RegionDao extends Dao<Region> {

    static RegionDao getInstance() throws DataSourceDownException {
        return RegionDaoImpl.getInstance();
    }
}
