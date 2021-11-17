package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Lot;
import by.epam.finaltask.model.LotContext;

import java.sql.SQLException;
import java.util.List;

public interface LotManager extends Dao<Lot> {

    static LotManager getInstance() throws DataSourceDownException {
        return MariaLotManager.getInstance();
    }

    List<Lot> find(long offset, long count) throws SQLException, DataSourceDownException, InterruptedException;

    List<Lot> findByLotContext(LotContext context, long offset, long count)
            throws SQLException, DataSourceDownException, InterruptedException;
}
