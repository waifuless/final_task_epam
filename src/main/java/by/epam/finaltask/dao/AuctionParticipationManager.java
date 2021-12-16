package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.AuctionParticipation;

import java.sql.SQLException;

public interface AuctionParticipationManager {

    static AuctionParticipationManager getInstance() throws DataSourceDownException {
        return MariaAuctionParticipationManager.getInstance();
    }
    void saveParticipation(AuctionParticipation participation)
            throws SQLException, DataSourceDownException, InterruptedException;

    boolean isUserParticipateInLotAuction(long userId, long lotId)
            throws SQLException, DataSourceDownException, InterruptedException;
}
