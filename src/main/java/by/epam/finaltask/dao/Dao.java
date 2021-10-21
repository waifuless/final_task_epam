package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.DaoEntity;

import java.sql.SQLException;

public interface Dao<T extends DaoEntity> {

    long save(T t) throws SQLException, DataSourceDownException, InterruptedException;

    T find(long id) throws SQLException, DataSourceDownException, InterruptedException;

    void update(T t) throws SQLException, DataSourceDownException, InterruptedException;

    void delete(long id) throws SQLException, DataSourceDownException, InterruptedException;

}
