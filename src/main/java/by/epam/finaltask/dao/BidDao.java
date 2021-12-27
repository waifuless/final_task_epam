package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.BidDaoImpl;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Bid;

import java.sql.SQLException;
import java.util.Optional;

public interface BidDao extends Dao<Bid> {

    static BidDao getInstance() throws DataSourceDownException {
        return BidDaoImpl.getInstance();
    }

    Optional<Bid> findMaxBid(long lotId) throws SQLException, DataSourceDownException, InterruptedException;

    Optional<Bid> findMinBid(long lotId) throws SQLException, DataSourceDownException, InterruptedException;

    void deleteByUserIdAndLotId(long userId, long lotId)
            throws SQLException, DataSourceDownException, InterruptedException;
}
