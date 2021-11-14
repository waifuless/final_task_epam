package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.OperationNotSupportedException;
import by.epam.finaltask.model.Images;

import java.sql.SQLException;
import java.util.List;

public interface ImagesManager extends Dao<Images> {

    static ImagesManager getInstance() throws DataSourceDownException {
        return MariaImagesManager.getInstance();
    }

    void saveImagePath(String path) throws SQLException, DataSourceDownException, InterruptedException;

    @Override
    default List<Images> findAll() throws SQLException, DataSourceDownException, InterruptedException {
        throw new OperationNotSupportedException();
    }
}
