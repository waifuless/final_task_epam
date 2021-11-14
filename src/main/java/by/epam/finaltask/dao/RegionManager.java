package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Region;

public interface RegionManager extends Dao<Region>{

    static RegionManager getInstance() throws DataSourceDownException {
        return MariaRegionManager.getInstance();
    }
}
