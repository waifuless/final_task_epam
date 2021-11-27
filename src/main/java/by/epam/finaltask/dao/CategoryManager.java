package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Category;

import java.sql.SQLException;

public interface CategoryManager extends Dao<Category> {

    static CategoryManager getInstance() throws DataSourceDownException {
        return MariaCategoryManager.getInstance();
    }

    boolean isCategoryExists(String category) throws SQLException, DataSourceDownException, InterruptedException;

}
