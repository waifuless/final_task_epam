package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Images;

import java.sql.SQLException;

public interface ImagesManager extends Dao<Images> {

    static ImagesManager getInstance() throws DataSourceDownException {
        return MariaImagesManager.getInstance();
    }

    void saveImagePath(String path) throws SQLException, DataSourceDownException, InterruptedException;
}
