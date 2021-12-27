package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.CategoryDaoImpl;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Category;

import java.sql.SQLException;

public interface CategoryDao extends Dao<Category> {

    static CategoryDao getInstance() throws DataSourceDownException {
        return CategoryDaoImpl.getInstance();
    }

    boolean isCategoryExists(String category) throws SQLException, DataSourceDownException, InterruptedException;

}
