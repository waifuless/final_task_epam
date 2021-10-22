package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Lot;

import java.sql.SQLException;
import java.util.List;

public interface LotManager extends Dao<Lot> {

    static LotManager getInstance() throws DataSourceDownException {
        return MariaLotManager.getInstance();
    }

    List<Lot> find(long offset, long count) throws SQLException, DataSourceDownException, InterruptedException;

    List<Lot> findByCategory(String category, long offset, long count)
            throws SQLException, DataSourceDownException, InterruptedException;

    List<Lot> findByUserId(long userId, long offset, long count)
            throws SQLException, DataSourceDownException, InterruptedException;
}
