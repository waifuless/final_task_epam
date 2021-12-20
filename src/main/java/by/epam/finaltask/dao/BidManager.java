package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Bid;

import java.sql.SQLException;
import java.util.Optional;

public interface BidManager extends Dao<Bid> {

    static BidManager getInstance() throws DataSourceDownException {
        return MariaBidManager.getInstance();
    }

    Optional<Bid> findMaxBid(long lotId) throws SQLException, DataSourceDownException, InterruptedException;

    Optional<Bid> findMinBid(long lotId) throws SQLException, DataSourceDownException, InterruptedException;

    void deleteByUserIdAndLotId(long userId, long lotId)
            throws SQLException, DataSourceDownException, InterruptedException;
}
