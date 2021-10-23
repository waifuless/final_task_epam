package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.AuctionStatus;
import by.epam.finaltask.model.AuctionType;
import by.epam.finaltask.model.Lot;
import by.epam.finaltask.model.ProductCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MariaLotManager extends GenericDao<Lot> implements LotManager {

    private final static Logger LOG = LoggerFactory.getLogger(MariaLotManager.class);

    private final static String ID_COLUMN = "lot_id";
    private final static String OWNER_ID_COLUMN = "owner_id";
    private final static String CATEGORY_NAME_COLUMN = "category_name";
    private final static String AUCTION_TYPE_NAME_COLUMN = "auction_type_name";
    private final static String TITLE_COLUMN = "title";
    private final static String START_DATETIME_COLUMN = "start_datetime";
    private final static String END_DATETIME = "end_datetime";
    private final static String INITIAL_PRICE_COLUMN = "initial_price";
    private final static String ORIGIN_PLACE_COLUMN = "origin_place";
    private final static String DESCRIPTION_COLUMN = "description";
    private final static String AUCTION_STATUS_NAME_COLUMN = "auction_status_name";
    private final static String PRODUCT_CONDITION_NAME_COLUMN = "product_condition_name";
    private final static String TABLE_NAME = "lot";
    private final static String SAVE_LOT_QUERY =
            "INSERT INTO lot(owner_id, category_id, auction_type_id, title, start_datetime, end_datetime," +
                    " initial_price, origin_place, description, auction_status_id, product_condition_id)" +
                    " VALUES(?, findCategoryId(?), findAuctionTypeId(?), ?, ?, ?, ?, ?, ?, findAuctionStatusId(?)," +
                    " findProductConditionId(?));" +
                    " SELECT LAST_INSERT_ID();";
    private final static String SAVE_IMAGE =
            "INSERT INTO lot_image(lot_id, image_value, main_image) VALUES(?, ?, ?);";
    private final static String FIND_ALL_LOTS_QUERY =
            "SELECT lot.lot_id AS lot_id, owner_id AS owner_id, category.category_name AS category_name," +
                    " auction_type.type_name" +
                    " AS auction_type_name, title AS title, start_datetime AS start_datetime," +
                    " end_datetime AS end_datetime, initial_price AS initial_price, origin_place AS origin_place," +
                    " description AS description, auction_status.status_name AS auction_status_name," +
                    " pc.product_condition_name AS product_condition_name FROM lot" +
                    " INNER JOIN category ON lot.category_id = category.category_id" +
                    " INNER JOIN auction_type ON lot.auction_type_id = auction_type.type_id" +
                    " INNER JOIN auction_status ON lot.auction_status_id = auction_status.status_id" +
                    " INNER JOIN product_condition pc ON lot.product_condition_id = pc.product_condition_id";
    private final static String FIND_LOT_BY_ID_QUERY = FIND_ALL_LOTS_QUERY + " WHERE lot.lot_id=?;";
    private final static String FIND_LOT_OFFSET_QUERY =
            FIND_ALL_LOTS_QUERY + " ORDER BY lot_id DESC LIMIT ? OFFSET ?";
    private final static String FIND_LOT_OFFSET_BY_CATEGORY_QUERY =
            FIND_ALL_LOTS_QUERY + " WHERE lot.category_id = findCategoryId(?)" +
                    " ORDER BY lot_id DESC LIMIT ? OFFSET ?";
    private final static String FIND_LOT_OFFSET_BY_USER_ID_QUERY =
            FIND_ALL_LOTS_QUERY + " WHERE owner_id = ?" +
                    " ORDER BY lot_id DESC LIMIT ? OFFSET ?";
    private final static String UPDATE_LOT_QUERY =
            "UPDATE lot SET owner_id = ?, category_id = findCategoryId(?), auction_type_id = findAuctionTypeId(?)," +
                    " title = ?, start_datetime = ?, end_datetime = ?, initial_price = ?, origin_place = ?," +
                    " description = ?, auction_status_id = findAuctionStatusId(?)," +
                    " product_condition_id = findProductConditionId(?)" +
                    " WHERE lot_id = ?";
    private final static String DELETE_LOT_QUERY =
            "DELETE FROM lot WHERE lot_id = ?;";

    private static volatile MariaLotManager instance;

    private MariaLotManager() throws DataSourceDownException {
        super(SAVE_LOT_QUERY, FIND_LOT_BY_ID_QUERY, UPDATE_LOT_QUERY, DELETE_LOT_QUERY, TABLE_NAME,
                ConnectionPool.getInstance());
    }

    public static MariaLotManager getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (MariaLotManager.class) {
                if (instance == null) {
                    instance = new MariaLotManager();
                }
            }
        }
        return instance;
    }

    @Override
    protected void prepareSaveStatement(PreparedStatement statement, Lot lot) throws SQLException {
        statement.setLong(1, lot.getOwnerId());
        statement.setString(2, lot.getCategory());
        statement.setString(3, lot.getAuctionType().name());
        statement.setString(4, lot.getTitle());
        statement.setTimestamp(5, lot.getStartDatetime());
        statement.setTimestamp(6, lot.getEndDatetime());
        statement.setBigDecimal(7, lot.getInitialPrice());
        statement.setString(8, lot.getOriginPlace());
        statement.setString(9, lot.getDescription());
        statement.setString(10, lot.getAuctionStatus().name());
        statement.setString(11, lot.getProductCondition().name());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Lot lot) throws SQLException {
        statement.setLong(1, lot.getLotId());
        statement.setLong(2, lot.getOwnerId());
        statement.setString(3, lot.getCategory());
        statement.setString(4, lot.getAuctionType().name());
        statement.setString(5, lot.getTitle());
        statement.setTimestamp(6, lot.getStartDatetime());
        statement.setTimestamp(7, lot.getEndDatetime());
        statement.setBigDecimal(8, lot.getInitialPrice());
        statement.setString(9, lot.getOriginPlace());
        statement.setString(10, lot.getDescription());
        statement.setString(11, lot.getAuctionStatus().name());
        statement.setString(12, lot.getProductCondition().name());
    }

    @Override
    protected Lot extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return new Lot(resultSet.getLong(ID_COLUMN),
                    resultSet.getLong(OWNER_ID_COLUMN),
                    resultSet.getString(CATEGORY_NAME_COLUMN),
                    AuctionType.valueOf(resultSet.getString(AUCTION_TYPE_NAME_COLUMN)),
                    resultSet.getString(TITLE_COLUMN),
                    resultSet.getTimestamp(START_DATETIME_COLUMN),
                    resultSet.getTimestamp(END_DATETIME),
                    resultSet.getBigDecimal(INITIAL_PRICE_COLUMN),
                    resultSet.getString(ORIGIN_PLACE_COLUMN),
                    resultSet.getString(DESCRIPTION_COLUMN),
                    AuctionStatus.valueOf(resultSet.getString(AUCTION_STATUS_NAME_COLUMN)),
                    ProductCondition.valueOf(resultSet.getString(PRODUCT_CONDITION_NAME_COLUMN)));
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }

    @Override
    protected long extractId(ResultSet resultSet) throws ExtractionException {
        try {
            return resultSet.getLong(ID_COLUMN);
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<Lot> find(long offset, long count) throws SQLException, DataSourceDownException, InterruptedException {
        return findListWithPreparator(FIND_LOT_OFFSET_QUERY, statement ->
        {
            statement.setLong(1, count);
            statement.setLong(2, offset);
        });
    }

    @Override
    public List<Lot> findByCategory(String category, long offset, long count)
            throws SQLException, DataSourceDownException, InterruptedException {
        return findListWithPreparator(FIND_LOT_OFFSET_BY_CATEGORY_QUERY, statement ->
        {
            statement.setString(1, category);
            statement.setLong(2, count);
            statement.setLong(3, offset);
        });
    }

    @Override
    public List<Lot> findByUserId(long userId, long offset, long count)
            throws SQLException, DataSourceDownException, InterruptedException {
        return findListWithPreparator(FIND_LOT_OFFSET_BY_USER_ID_QUERY, statement ->
        {
            statement.setLong(1, userId);
            statement.setLong(2, count);
            statement.setLong(3, offset);
        });
    }

    private List<Lot> findListWithPreparator(String query, StatementPreparator preparator)
            throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            preparator.accept(statement);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return extractAll(resultSet);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    private List<Lot> shet(String query, StatementPreparator preparator)
            throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement statement1 = connection.prepareStatement(SAVE_LOT_QUERY);
            PreparedStatement statement2 = connection.prepareStatement(UPDATE_LOT_QUERY);
            PreparedStatement statement3 = connection.prepareStatement(FIND_ALL_LOTS_QUERY);
            PreparedStatement statement4 = connection.prepareStatement(DELETE_LOT_QUERY);
            preparator.accept(statement);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return extractAll(resultSet);
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
