package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.LotDaoImpl;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.model.Lot;
import by.epam.finaltask.model.LotContext;

import java.sql.Timestamp;
import java.util.List;

public interface LotDao extends Dao<Lot> {

    static LotDao getInstance() {
        return LotDaoImpl.getInstance();
    }

    List<Lot> find(long offset, long count) throws DaoException;

    List<Lot> findByLotContext(LotContext context, long offset, long count)
            throws DaoException;

    long findLotsCount(LotContext context) throws DaoException;

    void executeLotsRenewAndCreateEventSchedule(Timestamp scheduleStart)
            throws DaoException;

    void dropLotsRenewEventSchedule() throws DaoException;
}
