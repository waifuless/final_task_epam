package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.Lot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MariaLotManager extends GenericDao<Lot> implements LotManager{

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
    private final static String SAVE_LOT_QUERY =
            "INSERT INTO lot(owner_id, category_id, auction_type_id, title, start_datetime, end_datetime," +
                    "initial_price, origin_place," +
                    "description, auction_status_id, product_condition_id)"+
                    " VALUES(?,?,(SELECT role_id FROM role WHERE role_name = ?));" +
                    " SELECT LAST_INSERT_ID();";
    private final static String FIND_LOT_QUERY =
            "SELECT user_id as user_id, email as email," +
                    " role.role_name as role_name FROM app_user" +
                    " INNER JOIN role ON app_user.role_id = role.role_id" +
                    " WHERE user_id=?";
    private final static String UPDATE_LOT_QUERY =
            "UPDATE app_user SET email = ?, password_hash = ?," +
                    " role_id = (SELECT role_id FROM role WHERE role_name = ?)" +
                    " WHERE user_id = ?";
    private final static String DELETE_LOT_QUERY =
            "DELETE FROM app_user WHERE user_id = ?";

    private static volatile MariaLotManager instance;

    private MariaLotManager() throws DataSourceDownException {
        super(SAVE_LOT_QUERY, FIND_LOT_QUERY, UPDATE_LOT_QUERY, DELETE_LOT_QUERY,
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
    public List<Lot> findAll() throws SQLException, DataSourceDownException, InterruptedException {
        return null;
    }

    @Override
    public List<Lot> findByCategory(String category) throws SQLException, DataSourceDownException, InterruptedException {
        return null;
    }

    @Override
    protected void prepareSaveStatement(PreparedStatement statement, Lot lot) throws SQLException {

    }

    @Override
    protected void prepareFindStatement(PreparedStatement statement, long id) throws SQLException {

    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Lot lot) throws SQLException {

    }

    @Override
    protected void prepareDeleteStatement(PreparedStatement statement, long id) throws SQLException {

    }

    @Override
    protected Lot extractEntity(ResultSet resultSet) throws ExtractionException {
        return null;
    }

    @Override
    protected long extractId(ResultSet resultSet) throws ExtractionException {
        return 0;
    }


    private void shet(){
        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SAVE_LOT_QUERY);
            PreparedStatement statement2 = connection.prepareStatement(FIND_LOT_QUERY);
            PreparedStatement statement3 = connection.prepareStatement(UPDATE_LOT_QUERY);
            PreparedStatement statement4 = connection.prepareStatement(DELETE_LOT_QUERY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (DataSourceDownException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
