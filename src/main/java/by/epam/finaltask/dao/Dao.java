package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.DaoEntity;

import java.sql.SQLException;
import java.util.Optional;

public interface Dao<T extends DaoEntity<T>> {

    Optional<T> save(T t) throws SQLException, DataSourceDownException, InterruptedException;

    Optional<T> find(long id) throws SQLException, DataSourceDownException, InterruptedException;

    void update(T t) throws SQLException, DataSourceDownException, InterruptedException;

    void delete(long id) throws SQLException, DataSourceDownException, InterruptedException;

    long count() throws SQLException, DataSourceDownException, InterruptedException;

}
