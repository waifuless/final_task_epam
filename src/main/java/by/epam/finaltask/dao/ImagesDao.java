package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.ImagesDaoImpl;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.OperationNotSupportedException;
import by.epam.finaltask.model.Images;

import java.sql.SQLException;
import java.util.List;

public interface ImagesDao extends Dao<Images> {

    static ImagesDao getInstance() throws DataSourceDownException {
        return ImagesDaoImpl.getInstance();
    }

    void saveImagePath(String path) throws SQLException, DataSourceDownException, InterruptedException;

    @Override
    default List<Images> findAll() throws SQLException, DataSourceDownException, InterruptedException {
        throw new OperationNotSupportedException();
    }
}
