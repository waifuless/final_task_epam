package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.AuctionParticipation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MariaAuctionParticipationManager implements AuctionParticipationManager{

    private final static Logger LOG = LoggerFactory.getLogger(MariaAuctionParticipationManager.class);

    private final static String PARTICIPATION_EXISTENCE_COLUMN = "participation_existence";
    private final static String SAVE_PARTICIPATION_QUERY = "INSERT INTO auction_participation(participant_id, lot_id," +
            " deposit, deposit_is_returned)" +
            " VALUES (?,?,?,?)";
    private final static String IS_USER_PARTICIPATE_QUERY = "SELECT EXISTS(SELECT COUNT(1) FROM auction_participation" +
            " WHERE participant_id=? AND lot_id=?) as participation_existence";

    private static volatile MariaAuctionParticipationManager instance;

    protected final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private MariaAuctionParticipationManager() throws DataSourceDownException {
    }

    public static MariaAuctionParticipationManager getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (MariaAuctionParticipationManager.class) {
                if (instance == null) {
                    instance = new MariaAuctionParticipationManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void saveParticipation(AuctionParticipation participation)
            throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_PARTICIPATION_QUERY);
            statement.setLong(1, participation.getParticipantId());
            statement.setLong(2, participation.getLotId());
            statement.setBigDecimal(3, participation.getDeposit());
            statement.setBoolean(4, participation.isDepositIsReturned());
            statement.execute();
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    @Override
    public boolean isUserParticipateInLotAuction(long userId, long lotId)
            throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(IS_USER_PARTICIPATE_QUERY);
            statement.setLong(1, userId);
            statement.setLong(2, lotId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(PARTICIPATION_EXISTENCE_COLUMN);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }
}
