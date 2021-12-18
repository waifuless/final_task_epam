package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.AuctionParticipation;
import by.epam.finaltask.model.Bid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MariaAuctionParticipationManager implements AuctionParticipationManager{

    private final static Logger LOG = LoggerFactory.getLogger(MariaAuctionParticipationManager.class);

    private final static String PARTICIPATION_EXISTENCE_COLUMN = "participation_existence";
    private final static String PARTICIPANT_ID_COLUMN = "participant_id";
    private final static String LOT_ID_COLUMN = "lot_id";
    private final static String DEPOSIT_COLUMN = "deposit";
    private final static String DEPOSIT_IS_RETURNED_COLUMN = "deposit_is_returned";

    private final static String SAVE_PARTICIPATION_QUERY = "INSERT INTO auction_participation(participant_id, lot_id," +
            " deposit, deposit_is_returned)" +
            " VALUES (?,?,?,?)";
    private final static String IS_USER_PARTICIPATE_QUERY = "SELECT EXISTS(SELECT 1 FROM auction_participation" +
            " WHERE participant_id=? AND lot_id=?) as participation_existence";
    private final static String FIND_ALL_USER_PARTICIPATES_QUERY = "SELECT participant_id AS participant_id," +
            " lot_id AS lot_id, deposit AS deposit, deposit_is_returned AS deposit_is_returned" +
            " FROM auction_participation";
    private final static String FIND_USER_PARTICIPATE_QUERY = FIND_ALL_USER_PARTICIPATES_QUERY +
            " WHERE participant_id=? AND lot_id=?";
    private final static String DELETE_USER_PARTICIPATE_QUERY = "DELETE FROM auction_participation" +
            " WHERE participant_id=? AND lot_id=?";

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

    @Override
    public Optional<AuctionParticipation> findParticipation(long userId, long lotId)
            throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_USER_PARTICIPATE_QUERY);
            statement.setLong(1, userId);
            statement.setLong(2, lotId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(extractEntity(resultSet));
            }else{
                return Optional.empty();
            }
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
    public void deleteParticipation(long userId, long lotId)
            throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_USER_PARTICIPATE_QUERY);
            statement.setLong(1, userId);
            statement.setLong(2, lotId);
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

    protected AuctionParticipation extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return new AuctionParticipation(resultSet.getLong(PARTICIPANT_ID_COLUMN), resultSet.getLong(LOT_ID_COLUMN),
                    resultSet.getBigDecimal(DEPOSIT_COLUMN), resultSet.getBoolean(DEPOSIT_IS_RETURNED_COLUMN));
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }
}
