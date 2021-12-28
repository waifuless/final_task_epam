package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.CategoryDaoImpl;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.model.Category;

public interface CategoryDao extends Dao<Category> {

    static CategoryDao getInstance() {
        return CategoryDaoImpl.getInstance();
    }

    boolean isCategoryExists(String category) throws DaoException;

}
