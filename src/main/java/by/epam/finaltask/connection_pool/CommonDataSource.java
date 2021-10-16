package by.epam.finaltask.connection_pool;

import by.epam.finaltask.exception.DataSourceDownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommonDataSource implements DataSource {

    private final static Logger LOG = LoggerFactory.getLogger(CommonDataSource.class);
    private final static String DEREGISTER_DRIVER_MCG = "deregistering jdbc driver: %s";
    private final static String ERROR_DEREGISTER_DRIVER_MCG = "Error deregistering driver %s";

    private final static int SIZE = 8;

    private final BlockingQueue<PooledConnection> availableConnections;//blocks when wait element (method take())
    private final Queue<PooledConnection> usingConnections; //does not block

    private final String databaseUrl;
    private final String login;
    private final String password;
    private final String driverName;

    private final AtomicBoolean dataSourceClosed = new AtomicBoolean(false);

    public CommonDataSource(String databaseUrl, String login, String password, String driverName)
            throws DataSourceDownException {
        this.databaseUrl = databaseUrl;
        this.login = login;
        this.password = password;
        this.driverName = driverName;
        try {
            registerDriver(driverName);
            availableConnections = new ArrayBlockingQueue<>(SIZE, true);
            usingConnections = new ConcurrentLinkedQueue<>();
            for (int i = 0; i < SIZE; i++) {
                availableConnections.add(new PooledConnection(DriverManager.
                        getConnection(databaseUrl, login, password), this));
            }
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
    public void takeBack(Connection connection) throws DataSourceDownException {
        if (dataSourceClosed.get()) {
            throw new DataSourceDownException();
        }
        try {
            if (usingConnections.remove((PooledConnection) connection)) {
                availableConnections.put((PooledConnection) connection);
            }
        } catch (InterruptedException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void close() throws Exception {
        if (dataSourceClosed.compareAndSet(false, true)) {
            deregisterDriver(driverName);
            closeConnections(availableConnections);
            closeConnections(usingConnections);
        }
    }


    private void registerDriver(String driverName)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName(driverName).newInstance();
    }

    private void deregisterDriver(String driverName) {
        try {
            Driver driver = DriverManager.getDriver(driverName);
            DriverManager.deregisterDriver(driver);
            LOG.info(String.format(DEREGISTER_DRIVER_MCG, driverName));
        } catch (SQLException e) {
            LOG.error(String.format(ERROR_DEREGISTER_DRIVER_MCG, driverName), e);
        }
    }

    private void closeConnections(Queue<PooledConnection> connections) throws SQLException {
        for (PooledConnection connection : connections) {
            connection.realClose();
        }
    }
}
