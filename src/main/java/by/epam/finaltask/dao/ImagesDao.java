package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.ImagesDaoImpl;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.exception.OperationNotSupportedException;
import by.epam.finaltask.model.Images;

import java.util.List;

public interface ImagesDao extends Dao<Images> {

    static ImagesDao getInstance() {
        return ImagesDaoImpl.getInstance();
    }

    void saveImagePath(String path) throws DaoException;

    @Override
    default List<Images> findAll() throws DaoException {
        throw new OperationNotSupportedException();
    }
}
