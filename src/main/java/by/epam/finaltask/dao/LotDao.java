package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.LotDaoImpl;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Lot;
import by.epam.finaltask.model.LotContext;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface LotDao extends Dao<Lot> {

    static LotDao getInstance() throws DataSourceDownException {
        return LotDaoImpl.getInstance();
    }

    List<Lot> find(long offset, long count) throws SQLException, DataSourceDownException, InterruptedException;

    List<Lot> findByLotContext(LotContext context, long offset, long count)
            throws SQLException, DataSourceDownException, InterruptedException;

    long findLotsCount(LotContext context) throws SQLException, DataSourceDownException, InterruptedException;

    void executeLotsRenewAndCreateEventSchedule(Timestamp scheduleStart)
            throws SQLException, DataSourceDownException, InterruptedException;

    void dropLotsRenewEventSchedule() throws SQLException, DataSourceDownException, InterruptedException;
}
