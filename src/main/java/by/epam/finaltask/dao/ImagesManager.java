package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Images;

public interface ImagesManager extends Dao<Images> {

    static ImagesManager getInstance() throws DataSourceDownException {
        return MariaImagesManager.getInstance();
    }
}
