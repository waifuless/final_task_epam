package by.epam.finaltask.connection_pool;

import by.epam.finaltask.exception.DataSourceDownException;

import java.sql.Connection;

public interface DataSource extends AutoCloseable {

    Connection getConnection() throws InterruptedException, DataSourceDownException;

    void takeBack(Connection connection);
}
