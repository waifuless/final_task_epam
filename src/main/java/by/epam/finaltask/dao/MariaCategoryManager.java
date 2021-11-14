package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MariaCategoryManager extends GenericDao<Category> implements CategoryManager {

    private final static String CATEGORY_ID_COLUMN = "category_id";
    private final static String CATEGORY_NAME_COLUMN = "category_name";
    private final static String TABLE_NAME = "category";
    private final static String SAVE_CATEGORY_QUERY =
            "INSERT INTO category(category_name)" +
                    " VALUES(?);";
    private final static String FIND_ALL_CATEGORIES_QUERY =
            "SELECT category_id AS category_id, category_name AS category_name FROM category";
    private final static String FIND_CATEGORY_BY_ID_QUERY = FIND_ALL_CATEGORIES_QUERY + " WHERE category_id=?";
    private final static String UPDATE_CATEGORY_QUERY =
            "UPDATE category SET category_name = ? WHERE category_id = ?";
    private final static String DELETE_CATEGORY_QUERY =
            "DELETE FROM category WHERE category_id = ?";

    private static volatile MariaCategoryManager instance;

    private MariaCategoryManager() throws DataSourceDownException {
        super(SAVE_CATEGORY_QUERY, FIND_ALL_CATEGORIES_QUERY, FIND_CATEGORY_BY_ID_QUERY, UPDATE_CATEGORY_QUERY,
                DELETE_CATEGORY_QUERY, TABLE_NAME, ConnectionPool.getInstance());
    }

    public static MariaCategoryManager getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (MariaCategoryManager.class) {
                if (instance == null) {
                    instance = new MariaCategoryManager();
                }
            }
        }
        return instance;
    }

    @Override
    protected void prepareSaveStatement(PreparedStatement statement, Category category) throws SQLException {
        statement.setString(1, category.getCategoryName());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Category category) throws SQLException {
        statement.setString(1, category.getCategoryName());
        statement.setLong(2, category.getCategoryId());
    }

    @Override
    protected Category extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return new Category(resultSet.getLong(CATEGORY_ID_COLUMN), resultSet.getString(CATEGORY_NAME_COLUMN));
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }
}
