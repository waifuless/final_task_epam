package by.epam.finaltask.dao.maria_impl;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.dao.CategoryDao;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDaoImpl extends GenericDao<Category> implements CategoryDao {

    private final static Logger LOG = LoggerFactory.getLogger(CategoryDaoImpl.class);

    private final static String CATEGORY_EXISTENCE_COLUMN = "category_existence";
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
    private final static String IS_CATEGORY_EXISTS_QUERY =
            "SELECT EXISTS(SELECT 1 FROM category WHERE category_name=?) AS category_existence";

    private static volatile CategoryDaoImpl instance;

    private CategoryDaoImpl() throws DataSourceDownException {
        super(SAVE_CATEGORY_QUERY, FIND_ALL_CATEGORIES_QUERY, FIND_CATEGORY_BY_ID_QUERY, UPDATE_CATEGORY_QUERY,
                DELETE_CATEGORY_QUERY, TABLE_NAME, ConnectionPool.getInstance());
    }

    public static CategoryDaoImpl getInstance() {
        if (instance == null) {
            synchronized (CategoryDaoImpl.class) {
                if (instance == null) {
                    instance = new CategoryDaoImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isCategoryExists(String category) throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(IS_CATEGORY_EXISTS_QUERY);
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(CATEGORY_EXISTENCE_COLUMN);
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
