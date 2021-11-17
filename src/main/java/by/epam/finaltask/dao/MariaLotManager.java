package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MariaLotManager extends GenericDao<Lot> implements LotManager {

    private final static Logger LOG = LoggerFactory.getLogger(MariaLotManager.class);

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
    private final static String TABLE_NAME = "lot";
    private final static String SAVE_LOT_QUERY =
            "INSERT INTO lot(owner_id, category_id, auction_type_id, title, start_datetime, end_datetime," +
                    " initial_price, region_id, city_or_district_id, description, auction_status_id," +
                    " product_condition_id)" +
                    " VALUES(?, findCategoryId(?), findAuctionTypeId(?), ?, ?, ?, ?, findRegionId(?)," +
                    " findCityOrDistrictId(?), ?, findAuctionStatusId(?)," +
                    " findProductConditionId(?));";
    private final static String FIND_ALL_LOTS_QUERY =
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
    private final static String FIND_LOT_BY_ID_QUERY = FIND_ALL_LOTS_QUERY + " WHERE lot.lot_id=?;";
    private final static String FIND_LOT_OFFSET_QUERY =
            FIND_ALL_LOTS_QUERY + " ORDER BY lot_id DESC LIMIT ? OFFSET ?";
    private final static String FIND_LOT_OFFSET_BY_USER_ID_QUERY =
            FIND_ALL_LOTS_QUERY + " WHERE owner_id = ?" +
                    " ORDER BY lot_id DESC LIMIT ? OFFSET ?";
    private final static String UPDATE_LOT_QUERY =
            "UPDATE lot SET owner_id = ?, category_id = findCategoryId(?), auction_type_id = findAuctionTypeId(?)," +
                    " title = ?, start_datetime = ?, end_datetime = ?, initial_price = ?, region_id = findRegionId(?)," +
                    " city_or_district_id = findCityOrDistrictId(?)," +
                    " description = ?, auction_status_id = findAuctionStatusId(?)," +
                    " product_condition_id = findProductConditionId(?)" +
                    " WHERE lot_id = ?";
    private final static String DELETE_LOT_QUERY =
            "DELETE FROM lot WHERE lot_id = ?;";
    private final static String EQUALS_TEMPLATE = " %s = ? ";
    private final static String GREATER_THAN_OR_EQUALS_TEMPLATE = " %s>=? ";
    private final static String LESS_THAN_OR_EQUALS_TEMPLATE = " %s<=? ";

    private static volatile MariaLotManager instance;

    private MariaLotManager() throws DataSourceDownException {
        super(SAVE_LOT_QUERY, FIND_ALL_LOTS_QUERY, FIND_LOT_BY_ID_QUERY, UPDATE_LOT_QUERY, DELETE_LOT_QUERY, TABLE_NAME,
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
        statement.setString(8, lot.getRegion());
        statement.setString(9, lot.getCityOrDistrict());
        statement.setString(10, lot.getDescription());
        statement.setString(11, lot.getAuctionStatus().name());
        statement.setString(12, lot.getProductCondition().name());
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
        statement.setString(9, lot.getRegion());
        statement.setString(10, lot.getCityOrDistrict());
        statement.setString(11, lot.getDescription());
        statement.setString(12, lot.getAuctionStatus().name());
        statement.setString(13, lot.getProductCondition().name());
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
                    resultSet.getTimestamp(END_DATETIME_COLUMN),
                    resultSet.getBigDecimal(INITIAL_PRICE_COLUMN),
                    resultSet.getString(REGION_NAME_COLUMN),
                    resultSet.getString(CITY_OR_DISTRICT_NAME_COLUMN),
                    resultSet.getString(DESCRIPTION_COLUMN),
                    AuctionStatus.valueOf(resultSet.getString(AUCTION_STATUS_NAME_COLUMN)),
                    ProductCondition.valueOf(resultSet.getString(PRODUCT_CONDITION_NAME_COLUMN)));
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
    public List<Lot> findByLotContext(LotContext context, long offset, long count)
            throws SQLException, DataSourceDownException, InterruptedException {
        LinkedHashMap<Object, Integer> params = new LinkedHashMap<>();
        String findByContextQuery = FIND_ALL_LOTS_QUERY + createFilterByContextAndFillParamsMap(context, params)+
                " ORDER BY lot_id DESC LIMIT ? OFFSET ?";
        return findListWithPreparator(findByContextQuery, statement ->
        {
            int i=0;
            for (Map.Entry<Object, Integer> entry : params.entrySet()) {
                statement.setObject(++i, entry.getKey(), entry.getValue());
            }
            statement.setLong(++i, count);
            statement.setLong(++i, offset);
        });
    }

    private String createFilterByContextAndFillParamsMap(LotContext context, LinkedHashMap<Object, Integer> params) {
        StringBuilder filter = new StringBuilder(" WHERE ");
        if (context.getOwnerId() != null) {
            filter.append(String.format(" %s = ? ", OWNER_ID_COLUMN));
            params.put(context.getOwnerId(), Types.INTEGER);
        }
        checkParamAndFillFilterAndParamMap(context.getCategory(), Types.VARCHAR, EQUALS_TEMPLATE,
                "category.category_name", filter, params);
        checkParamAndFillFilterAndParamMap(context.getAuctionType(), Types.VARCHAR, EQUALS_TEMPLATE,
                "auction_type.type_name", filter, params);
        checkParamAndFillFilterAndParamMap(context.getTitle(), Types.VARCHAR, EQUALS_TEMPLATE, "title",
                filter, params);
        checkParamAndFillFilterAndParamMap(context.getMinInitialPrice(), Types.DECIMAL, GREATER_THAN_OR_EQUALS_TEMPLATE,
                "initial_price", filter, params);
        checkParamAndFillFilterAndParamMap(context.getMaxInitialPrice(), Types.DECIMAL, LESS_THAN_OR_EQUALS_TEMPLATE,
                "initial_price", filter, params);
        checkParamAndFillFilterAndParamMap(context.getRegion(), Types.VARCHAR, EQUALS_TEMPLATE,
                "region_name", filter, params);
        checkParamAndFillFilterAndParamMap(context.getCityOrDistrict(), Types.VARCHAR, EQUALS_TEMPLATE,
                "city_or_district_name", filter, params);
        checkParamAndFillFilterAndParamMap(context.getAuctionStatus(), Types.VARCHAR, EQUALS_TEMPLATE,
                "auction_status.status_name", filter, params);
        checkParamAndFillFilterAndParamMap(context.getProductCondition(), Types.VARCHAR, EQUALS_TEMPLATE,
                "pc.product_condition_name", filter, params);
        LOG.debug("Filter query: {}", filter);
        LOG.debug("Params map: {}", params);
        if(params.isEmpty()){
            return "";
        }
        return new String(filter);
    }

    private void checkParamAndFillFilterAndParamMap(Object param, Integer paramType, String filterPartTemplate,
                                                    String columnName, StringBuilder filter,
                                                    LinkedHashMap<Object, Integer> params) {
        if (param != null) {
            if (!params.isEmpty()) {
                filter.append(" AND ");
            }
            filter.append(String.format(filterPartTemplate, columnName));
            params.put(param, paramType);
        }
    }

    private List<Lot> findListWithPreparator(String query, StatementPreparator preparator)
            throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            preparator.accept(statement);
            ResultSet resultSet = statement.executeQuery();
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
