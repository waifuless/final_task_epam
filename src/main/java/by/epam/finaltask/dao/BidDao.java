package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.BidDaoImpl;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.model.Bid;

import java.util.Optional;

public interface BidDao extends Dao<Bid> {

    static BidDao getInstance() {
        return BidDaoImpl.getInstance();
    }

    Optional<Bid> findMaxBid(long lotId) throws DaoException;

    Optional<Bid> findMinBid(long lotId) throws DaoException;

    void deleteByUserIdAndLotId(long userId, long lotId)
            throws DaoException;
}
