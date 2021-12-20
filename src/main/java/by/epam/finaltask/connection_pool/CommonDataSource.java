package by.epam.finaltask.connection_pool;

import by.epam.finaltask.exception.DataSourceDownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommonDataSource implements DataSource {

    private final static Logger LOG = LoggerFactory.getLogger(CommonDataSource.class);
    private final static String DEREGISTER_DRIVER_MCG = "deregistering jdbc driver: %s";
    private final static String ERROR_DEREGISTER_DRIVER_MCG = "Error deregistering driver %s";
    private final static String CONNECTION_ADDED_MCG = "Connection was added to connection pool {}";
    private final static String CONNECTION_CLOSED_MSG = "Close connection {}";

    private final static int SIZE = 8;

    private final List<PooledConnection> allConnections; //uses to close all connection without any lock
    private final BlockingQueue<PooledConnection> availableConnections;//blocks when wait element (method take())
    private final Queue<PooledConnection> usingConnections; //does not block

    private final String databaseUrl;

    private final AtomicBoolean dataSourceClosed = new AtomicBoolean(false);

    public CommonDataSource(String databaseUrl, String login, String password, String driverName)
            throws DataSourceDownException {
        this.databaseUrl = databaseUrl;
        try {
            registerDriver(driverName);
            allConnections = new ArrayList<>();
            availableConnections = new ArrayBlockingQueue<>(SIZE, true);
            usingConnections = new ConcurrentLinkedQueue<>();
            initConnections(databaseUrl, login, password);
        } catch (Exception ex) {
            DataSourceDownException downException =
                    new DataSourceDownException("DataSource can`t start", ex);
            LOG.error(downException.getMessage(), downException);
            throw downException;
        }
    }

    @Override
    public Connection getConnection() throws InterruptedException, DataSourceDownException {
        if (dataSourceClosed.get()) {
            throw new DataSourceDownException();
        }
        PooledConnection connection = availableConnections.take();
        usingConnections.add(connection);
        return connection;
    }

    @Override
    public void takeBack(Connection connection) {
        if (!dataSourceClosed.get()) {
            try {
                if (usingConnections.remove((PooledConnection) connection)) {
                    availableConnections.put((PooledConnection) connection);
                }
            } catch (InterruptedException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (dataSourceClosed.compareAndSet(false, true)) {
            closeConnections(allConnections);
            deregisterDriver(databaseUrl);
        }
    }

    private void initConnections(String databaseUrl, String login, String password) throws SQLException {
        for (int i = 0; i < SIZE; i++) {
            PooledConnection connection = new PooledConnection(DriverManager.
                    getConnection(databaseUrl + "?allowMultiQueries=true", login, password), this);
            allConnections.add(connection);
            availableConnections.add(connection);
            LOG.info(CONNECTION_ADDED_MCG, connection);
        }
    }

    private void registerDriver(String driverName)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName(driverName).newInstance();
    }

    private void deregisterDriver(String databaseUrl) {
        try {
            Driver driver = DriverManager.getDriver(databaseUrl);
            DriverManager.deregisterDriver(driver);
            LOG.info(String.format(DEREGISTER_DRIVER_MCG, databaseUrl));
        } catch (SQLException e) {
            LOG.error(String.format(ERROR_DEREGISTER_DRIVER_MCG, databaseUrl), e);
        }
    }

    private void closeConnections(List<PooledConnection> connections) throws SQLException {
        for (PooledConnection connection : connections) {
            connection.realClose();
            LOG.info(CONNECTION_CLOSED_MSG, connection);
        }
    }
}
