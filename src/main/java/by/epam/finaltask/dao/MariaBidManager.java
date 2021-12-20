package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.Bid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MariaBidManager extends GenericDao<Bid> implements BidManager {

    private final static Logger LOG = LoggerFactory.getLogger(MariaBidManager.class);

    private final static String BID_ID_COLUMN = "bid_id";
    private final static String USER_ID_COLUMN = "user_id";
    private final static String AMOUNT_COLUMN = "amount";
    private final static String LOT_ID_COLUMN = "lot_id";
    private final static String TABLE_NAME = "bid";
    private final static String SAVE_BID_QUERY =
            "INSERT INTO bid(user_id, amount, lot_id) " +
                    " VALUES(?,?,?);";
    private final static String FIND_ALL_BIDS_QUERY =
            "SELECT bid_id AS bid_id, user_id AS user_id, amount AS amount, lot_id AS lot_id FROM bid";
    private final static String FIND_BID_BY_ID_QUERY = FIND_ALL_BIDS_QUERY + " WHERE bid_id=?";
    private final static String UPDATE_BID_QUERY =
            "UPDATE bid SET user_id=?, amount=?, lot_id=?" +
                    " WHERE bid_id = ?";
    private final static String DELETE_BID_QUERY =
            "DELETE FROM bid WHERE bid_id = ?";
    private final static String FIND_MAX_BID_QUERY = FIND_ALL_BIDS_QUERY + " WHERE amount = " +
            " (SELECT MAX(amount) FROM bid WHERE lot_id = ?) ORDER BY bid_id LIMIT 1";
    private final static String FIND_MIN_BID_QUERY = FIND_ALL_BIDS_QUERY + " WHERE amount = " +
            " (SELECT MIN(amount) FROM bid WHERE lot_id = ?) ORDER BY bid_id LIMIT 1";

    private static volatile MariaBidManager instance;

    private MariaBidManager() throws DataSourceDownException {
        super(SAVE_BID_QUERY, FIND_ALL_BIDS_QUERY, FIND_BID_BY_ID_QUERY, UPDATE_BID_QUERY, DELETE_BID_QUERY,
                TABLE_NAME, ConnectionPool.getInstance());
    }

    public static MariaBidManager getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (MariaBidManager.class) {
                if (instance == null) {
                    instance = new MariaBidManager();
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<Bid> findMaxBid(long lotId) throws SQLException, DataSourceDownException, InterruptedException {
        return findBestBid(lotId, FIND_MAX_BID_QUERY);
    }

    @Override
    public Optional<Bid> findMinBid(long lotId) throws SQLException, DataSourceDownException, InterruptedException {
        return findBestBid(lotId, FIND_MIN_BID_QUERY);
    }

    @Override
    protected void prepareSaveStatement(PreparedStatement statement, Bid bid) throws SQLException {
        statement.setLong(1, bid.getUserId());
        statement.setBigDecimal(2, bid.getAmount());
        statement.setLong(3, bid.getLotId());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Bid bid) throws SQLException {
        statement.setLong(1, bid.getUserId());
        statement.setBigDecimal(2, bid.getAmount());
        statement.setLong(3, bid.getLotId());
        statement.setLong(4, bid.getBidId());
    }

    @Override
    protected Bid extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return Bid.builder().setBidId(resultSet.getLong(BID_ID_COLUMN)).setUserId(resultSet.getLong(USER_ID_COLUMN))
                    .setAmount(resultSet.getBigDecimal(AMOUNT_COLUMN)).setLotId(resultSet.getLong(LOT_ID_COLUMN))
                    .build();
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }


    private Optional<Bid> findBestBid(long lotId, String query)
            throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, lotId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            } else {
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
}
