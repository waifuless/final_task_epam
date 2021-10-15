package by.epam.finaltask.connection_pool;

import by.epam.finaltask.exception.DataSourceDownException;

import java.sql.Connection;

public interface ConnectionPool extends AutoCloseable {

    static ConnectionPool getInstance() throws DataSourceDownException {
        return CommonConnectionPool.getInstance();
    }

    Connection getConnection() throws InterruptedException, DataSourceDownException;
}
