package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.AuctionParticipationDaoImpl;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.model.AuctionParticipation;
import by.epam.finaltask.model.AuctionStatus;

import java.util.List;
import java.util.Optional;

public interface AuctionParticipationDao {

    static AuctionParticipationDao getInstance() {
        return AuctionParticipationDaoImpl.getInstance();
    }

    void saveParticipation(AuctionParticipation participation)
            throws DaoException;

    void updateParticipation(AuctionParticipation participation)
            throws DaoException;

    boolean isUserParticipateInLotAuction(long userId, long lotId)
            throws DaoException;

    Optional<AuctionParticipation> findParticipation(long userId, long lotId)
            throws DaoException;

    void deleteParticipation(long userId, long lotId)
            throws DaoException;

    List<AuctionParticipation> findUsersParticipations(long userId, long offset, long count)
            throws DaoException;

    List<AuctionParticipation> findUsersParticipations(long userId, long offset, long count,
                                                       AuctionStatus auctionStatus)
            throws DaoException;

    long findUsersParticipationsCount(long userId)
            throws DaoException;

    long findUsersParticipationsCount(long userId, AuctionStatus auctionStatus)
            throws DaoException;
}
