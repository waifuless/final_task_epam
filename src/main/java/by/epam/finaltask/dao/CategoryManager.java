package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Category;

public interface CategoryManager extends Dao<Category>{

    static CategoryManager getInstance() throws DataSourceDownException {
        return MariaCategoryManager.getInstance();
    }
}
