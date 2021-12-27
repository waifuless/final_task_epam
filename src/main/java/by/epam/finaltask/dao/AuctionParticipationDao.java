package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.AuctionParticipationDaoImpl;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.AuctionParticipation;
import by.epam.finaltask.model.AuctionStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AuctionParticipationDao {

    static AuctionParticipationDao getInstance() throws DataSourceDownException {
        return AuctionParticipationDaoImpl.getInstance();
    }

    void saveParticipation(AuctionParticipation participation)
            throws SQLException, DataSourceDownException, InterruptedException;

    void updateParticipation(AuctionParticipation participation)
            throws SQLException, DataSourceDownException, InterruptedException;

    boolean isUserParticipateInLotAuction(long userId, long lotId)
            throws SQLException, DataSourceDownException, InterruptedException;

    Optional<AuctionParticipation> findParticipation(long userId, long lotId)
            throws SQLException, DataSourceDownException, InterruptedException;

    void deleteParticipation(long userId, long lotId)
            throws SQLException, DataSourceDownException, InterruptedException;

    List<AuctionParticipation> findUsersParticipations(long userId, long offset, long count)
            throws SQLException, DataSourceDownException, InterruptedException;

    List<AuctionParticipation> findUsersParticipations(long userId, long offset, long count,
                                                       AuctionStatus auctionStatus)
            throws SQLException, DataSourceDownException, InterruptedException;

    long findUsersParticipationsCount(long userId)
            throws SQLException, DataSourceDownException, InterruptedException;

    long findUsersParticipationsCount(long userId, AuctionStatus auctionStatus)
            throws SQLException, DataSourceDownException, InterruptedException;
}
