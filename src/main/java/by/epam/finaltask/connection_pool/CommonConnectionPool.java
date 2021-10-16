package by.epam.finaltask.connection_pool;

import by.epam.finaltask.exception.DataSourceDownException;

import java.sql.Connection;

public class CommonConnectionPool implements ConnectionPool {

    private final static String DATABASE_URL = "jdbc:mariadb://localhost:3306/racingSite";
    private final static String LOGIN = System.getenv("MYSQL_RACING_SITE_USER");
    private final static String PASSWORD = System.getenv("MYSQL_RACING_SITE_PASSWORD");
    private final static String DRIVER_NAME = "org.mariadb.jdbc.Driver";

    private volatile static CommonConnectionPool instance;

    private final DataSource dataSource;

    private CommonConnectionPool() throws DataSourceDownException {
        dataSource = new CommonDataSource(DATABASE_URL, LOGIN, PASSWORD, DRIVER_NAME);
    }

    public static CommonConnectionPool getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (CommonConnectionPool.class) {
                if (instance == null) {
                    instance = new CommonConnectionPool();
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws InterruptedException, DataSourceDownException {
        return dataSource.getConnection();
    }

    @Override
    public void close() throws Exception {
        dataSource.close();
    }
}
