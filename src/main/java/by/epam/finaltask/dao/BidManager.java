package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.Bid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BidManager extends GenericDao<Bid> implements Dao<Bid> {

    private final static Logger LOG = LoggerFactory.getLogger(BidManager.class);

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

    private static volatile BidManager instance;

    private BidManager() throws DataSourceDownException {
        super(SAVE_BID_QUERY, FIND_ALL_BIDS_QUERY, FIND_BID_BY_ID_QUERY, UPDATE_BID_QUERY, DELETE_BID_QUERY,
                TABLE_NAME, ConnectionPool.getInstance());
    }

    public static BidManager getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (BidManager.class) {
                if (instance == null) {
                    instance = new BidManager();
                }
            }
        }
        return instance;
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
}
