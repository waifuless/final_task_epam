package by.epam.finaltask.dao.maria_impl;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.dao.LotDao;
import by.epam.finaltask.dao.StatementPreparator;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LotDaoImpl extends GenericDao<Lot> implements LotDao {

    private final static Logger LOG = LoggerFactory.getLogger(LotDaoImpl.class);

    private final static String ID_COLUMN = "lot_id";
    private final static String OWNER_ID_COLUMN = "owner_id";
    private final static String CATEGORY_NAME_COLUMN = "category_name";
    private final static String AUCTION_TYPE_NAME_COLUMN = "auction_type_name";
    private final static String TITLE_COLUMN = "title";
    private final static String START_DATETIME_COLUMN = "start_datetime";
    private final static String END_DATETIME_COLUMN = "end_datetime";
    private final static String INITIAL_PRICE_COLUMN = "initial_price";
    private final static String REGION_NAME_COLUMN = "region_name";
    private final static String CITY_OR_DISTRICT_NAME_COLUMN = "city_or_district_name";
    private final static String DESCRIPTION_COLUMN = "description";
    private final static String AUCTION_STATUS_NAME_COLUMN = "auction_status_name";
    private final static String PRODUCT_CONDITION_NAME_COLUMN = "product_condition_name";
    private final static String ROWS_COUNT_COLUMN = "rows_count";
    private final static String TABLE_NAME = "lot";
    private final static String SAVE_LOT_QUERY =
            "SET @regionId = findRegionId(?);" +
                    " INSERT INTO lot(owner_id, category_id, auction_type_id, title, start_datetime, end_datetime," +
                    " initial_price, region_id, city_or_district_id, description, auction_status_id," +
                    " product_condition_id)" +
                    " VALUES(?, findCategoryId(?), findAuctionTypeId(?), ?, ?, ?, ?, @regionId," +
                    " findCityOrDistrictId(?, @regionId), ?, findAuctionStatusId(?)," +
                    " findProductConditionId(?));";
    private final static String SELECT_LOTS_WITH_ALL_JOINS =
            "SELECT lot.lot_id AS lot_id, owner_id AS owner_id, category.category_name AS category_name," +
                    " auction_type.type_name" +
                    " AS auction_type_name, title AS title, start_datetime AS start_datetime," +
                    " end_datetime AS end_datetime, initial_price AS initial_price, region_name AS region_name," +
                    " city_or_district_name AS city_or_district_name," +
                    " description AS description, auction_status.status_name AS auction_status_name," +
                    " pc.product_condition_name AS product_condition_name FROM lot" +
                    " LEFT JOIN category ON lot.category_id = category.category_id" +
                    " LEFT JOIN auction_type ON lot.auction_type_id = auction_type.type_id" +
                    " LEFT JOIN region r on lot.region_id = r.region_id" +
                    " LEFT JOIN city_or_district c on lot.city_or_district_id = c.city_or_district_id" +
                    " LEFT JOIN auction_status ON lot.auction_status_id = auction_status.status_id" +
                    " LEFT JOIN product_condition pc ON lot.product_condition_id = pc.product_condition_id";
    private final static String FIND_ALL_LOTS_QUERY =
            "SELECT * FROM ( " + SELECT_LOTS_WITH_ALL_JOINS + " ) as inner_lot";
    private final static String FIND_LOT_BY_ID_QUERY = FIND_ALL_LOTS_QUERY + " WHERE inner_lot.lot_id=?;";
    private final static String FIND_LOT_OFFSET_QUERY =
            FIND_ALL_LOTS_QUERY + " ORDER BY lot_id DESC LIMIT ? OFFSET ?";
    private final static String FIND_LOT_OFFSET_BY_USER_ID_QUERY =
            FIND_ALL_LOTS_QUERY + " WHERE owner_id = ?" +
                    " ORDER BY lot_id DESC LIMIT ? OFFSET ?";
    private final static String UPDATE_LOT_QUERY =
            "SET @regionId = findRegionId(?);" +
                    " UPDATE lot SET owner_id = ?, category_id = findCategoryId(?)," +
                    " auction_type_id = findAuctionTypeId(?), title = ?, start_datetime = ?, end_datetime = ?," +
                    " initial_price = ?, region_id = @regionId," +
                    " city_or_district_id = findCityOrDistrictId(?, @regionId)," +
                    " description = ?, auction_status_id = findAuctionStatusId(?)," +
                    " product_condition_id = findProductConditionId(?)" +
                    " WHERE lot_id = ?";
    private final static String DELETE_LOT_QUERY =
            "DELETE FROM lot WHERE lot_id = ?;";
    private final static String COUNT_QUERY_WITH_JOINS = "SELECT COUNT(1) AS rows_count FROM ( " +
            SELECT_LOTS_WITH_ALL_JOINS + " ) as inner_lot";
    private final static String DROP_RENEW_SCHEDULE = "DROP EVENT IF EXISTS every_hour_auction_status_renew;";
    private final static String RENEW_LOTS_QUERY = "UPDATE lot SET " +
            " auction_status_id = findAuctionStatusId('RUNNING')" +
            " WHERE auction_status_id = findAuctionStatusId('APPROVED_BY_ADMIN') AND NOW() >= start_datetime;" +
            " UPDATE lot SET " +
            " auction_status_id = findAuctionStatusId('ENDED')" +
            " WHERE auction_status_id = findAuctionStatusId('RUNNING') AND NOW() >= end_datetime;" +
            " UPDATE lot SET " +
            " auction_status_id = findAuctionStatusId('DENIED')" +
            " WHERE auction_status_id = findAuctionStatusId('NOT_VERIFIED') AND NOW() >= start_datetime;";
    private final static String CREATE_RENEW_SCHEDULE = DROP_RENEW_SCHEDULE +
            " CREATE EVENT every_hour_auction_status_renew" +
            " ON SCHEDULE EVERY 1 HOUR STARTS ? DO BEGIN " + RENEW_LOTS_QUERY + " END;";
    private final static String EQUALS_TEMPLATE = " %s = ? ";
    private final static String GREATER_THAN_OR_EQUALS_TEMPLATE = " %s>=? ";
    private final static String LESS_THAN_OR_EQUALS_TEMPLATE = " %s<=? ";
    private final static String LIKE_STRING_TEMPLATE = " %s LIKE ? ";

    private static volatile LotDaoImpl instance;

    private LotDaoImpl() throws DataSourceDownException {
        super(SAVE_LOT_QUERY, FIND_ALL_LOTS_QUERY, FIND_LOT_BY_ID_QUERY, UPDATE_LOT_QUERY, DELETE_LOT_QUERY, TABLE_NAME,
                ConnectionPool.getInstance());
    }

    public static LotDaoImpl getInstance() {
        if (instance == null) {
            synchronized (LotDaoImpl.class) {
                if (instance == null) {
                    instance = new LotDaoImpl();
                }
            }
        }
        return instance;
    }

    @Override
    protected void prepareSaveStatement(PreparedStatement statement, Lot lot) throws SQLException {
        statement.setString(1, lot.getRegion());
        statement.setLong(2, lot.getOwnerId());
        statement.setString(3, lot.getCategory());
        statement.setString(4, lot.getAuctionType().name());
        statement.setString(5, lot.getTitle());
        statement.setTimestamp(6, lot.getStartDatetime());
        statement.setTimestamp(7, lot.getEndDatetime());
        statement.setBigDecimal(8, lot.getInitialPrice());
        statement.setString(9, lot.getCityOrDistrict());
        statement.setString(10, lot.getDescription());
        statement.setString(11, lot.getAuctionStatus().name());
        statement.setString(12, lot.getProductCondition().name());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Lot lot) throws SQLException {
        statement.setString(1, lot.getRegion());
        statement.setLong(2, lot.getOwnerId());
        statement.setString(3, lot.getCategory());
        statement.setString(4, lot.getAuctionType().name());
        statement.setString(5, lot.getTitle());
        statement.setTimestamp(6, lot.getStartDatetime());
        statement.setTimestamp(7, lot.getEndDatetime());
        statement.setBigDecimal(8, lot.getInitialPrice());
        statement.setString(9, lot.getCityOrDistrict());
        statement.setString(10, lot.getDescription());
        statement.setString(11, lot.getAuctionStatus().name());
        statement.setString(12, lot.getProductCondition().name());
        statement.setLong(13, lot.getLotId());
    }

    @Override
    protected Lot extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return Lot.builder().setLotId(resultSet.getLong(ID_COLUMN)).setOwnerId(resultSet.getLong(OWNER_ID_COLUMN)).
                    setCategory(resultSet.getString(CATEGORY_NAME_COLUMN))
                    .setAuctionType(AuctionType.valueOf(resultSet.getString(AUCTION_TYPE_NAME_COLUMN)))
                    .setTitle(resultSet.getString(TITLE_COLUMN))
                    .setStartDatetime(resultSet.getTimestamp(START_DATETIME_COLUMN))
                    .setEndDatetime(resultSet.getTimestamp(END_DATETIME_COLUMN))
                    .setInitialPrice(resultSet.getBigDecimal(INITIAL_PRICE_COLUMN))
                    .setRegion(resultSet.getString(REGION_NAME_COLUMN))
                    .setCityOrDistrict(resultSet.getString(CITY_OR_DISTRICT_NAME_COLUMN))
                    .setDescription(resultSet.getString(DESCRIPTION_COLUMN))
                    .setAuctionStatus(AuctionStatus.valueOf(resultSet.getString(AUCTION_STATUS_NAME_COLUMN)))
                    .setProductCondition(ProductCondition.valueOf(resultSet.getString(PRODUCT_CONDITION_NAME_COLUMN)))
                    .build();
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }

    @Override
    public List<Lot> find(long offset, long count) throws DaoException {
        return findListWithPreparator(FIND_LOT_OFFSET_QUERY, statement ->
        {
            statement.setLong(1, count);
            statement.setLong(2, offset);
        });
    }

    @Override
    public List<Lot> findByLotContext(LotContext context, long offset, long count)
            throws DaoException {
        List<Pair<Object, Integer>> params = new ArrayList<>();
        String findByContextQuery = FIND_ALL_LOTS_QUERY + createFilterByContextAndFillParamsList(context, params) +
                " ORDER BY lot_id DESC LIMIT ? OFFSET ?";
        return findListWithPreparator(findByContextQuery, statement ->
        {
            int i = 0;
            for (Pair<Object, Integer> param : params) {
                statement.setObject(++i, param.getL(), param.getR());
            }
            statement.setLong(++i, count);
            statement.setLong(++i, offset);
        });
    }

    @Override
    public long findLotsCount(LotContext context)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            List<Pair<Object, Integer>> params = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(COUNT_QUERY_WITH_JOINS +
                    createFilterByContextAndFillParamsList(context, params));
            int i = 0;
            for (Pair<Object, Integer> param : params) {
                statement.setObject(++i, param.getL(), param.getR());
            }
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getLong(ROWS_COUNT_COLUMN);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    @Override
    public void executeLotsRenewAndCreateEventSchedule(Timestamp scheduleStart)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            PreparedStatement schedulePreparedStatement = connection.prepareStatement(CREATE_RENEW_SCHEDULE);
            statement.execute(RENEW_LOTS_QUERY);
            schedulePreparedStatement.setTimestamp(1, scheduleStart);
            schedulePreparedStatement.execute();
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    @Override
    public void dropLotsRenewEventSchedule()
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(DROP_RENEW_SCHEDULE);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    private String createFilterByContextAndFillParamsList(LotContext context, List<Pair<Object, Integer>> params) {
        StringBuilder filter = new StringBuilder(" WHERE ");
        if (context.getOwnerId() != null) {
            filter.append(String.format(" %s = ? ", OWNER_ID_COLUMN));
            params.add(new Pair<>(context.getOwnerId(), Types.INTEGER));
        }
        checkParamAndFillFilterAndParamList(context.getCategory(), Types.VARCHAR, EQUALS_TEMPLATE,
                CATEGORY_NAME_COLUMN, filter, params);
        checkParamAndFillFilterAndParamList(context.getAuctionType(), Types.VARCHAR, EQUALS_TEMPLATE,
                AUCTION_TYPE_NAME_COLUMN, filter, params);
        String title = context.getTitle();
        title = title != null ? "%" + title + "%" : null;
        checkParamAndFillFilterAndParamList(title, Types.VARCHAR, LIKE_STRING_TEMPLATE, TITLE_COLUMN,
                filter, params);
        checkParamAndFillFilterAndParamList(context.getMinInitialPrice(), Types.DECIMAL, GREATER_THAN_OR_EQUALS_TEMPLATE,
                INITIAL_PRICE_COLUMN, filter, params);
        checkParamAndFillFilterAndParamList(context.getMaxInitialPrice(), Types.DECIMAL, LESS_THAN_OR_EQUALS_TEMPLATE,
                INITIAL_PRICE_COLUMN, filter, params);
        checkParamAndFillFilterAndParamList(context.getRegion(), Types.VARCHAR, EQUALS_TEMPLATE,
                REGION_NAME_COLUMN, filter, params);
        checkParamAndFillFilterAndParamList(context.getCityOrDistrict(), Types.VARCHAR, EQUALS_TEMPLATE,
                CITY_OR_DISTRICT_NAME_COLUMN, filter, params);
        checkParamAndFillFilterAndParamList(context.getAuctionStatus(), Types.VARCHAR, EQUALS_TEMPLATE,
                AUCTION_STATUS_NAME_COLUMN, filter, params);
        checkParamAndFillFilterAndParamList(context.getProductCondition(), Types.VARCHAR, EQUALS_TEMPLATE,
                PRODUCT_CONDITION_NAME_COLUMN, filter, params);
        LOG.debug("Filter query: {}", filter);
        LOG.debug("Params map: {}", params);
        if (params.isEmpty()) {
            return "";
        }
        return new String(filter);
    }

    private void checkParamAndFillFilterAndParamList(Object param, Integer paramType, String filterPartTemplate,
                                                     String columnName, StringBuilder filter,
                                                     List<Pair<Object, Integer>> params) {
        if (param != null) {
            if (!params.isEmpty()) {
                filter.append(" AND ");
            }
            filter.append(String.format(filterPartTemplate, columnName));
            params.add(new Pair<>(param, paramType));
        }
    }

    private List<Lot> findListWithPreparator(String query, StatementPreparator preparator)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            preparator.accept(statement);
            ResultSet resultSet = statement.executeQuery();
            return extractAll(resultSet);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }
}
