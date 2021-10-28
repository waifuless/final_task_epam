package by.epam.finaltask.connection_pool;

import by.epam.finaltask.exception.DataSourceDownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class CommonConnectionPool implements ConnectionPool {

    private final static Logger LOG = LoggerFactory.getLogger(CommonConnectionPool.class);

    private final static String INIT_MSG = "Connection pool successfully initialized";
    private final static String CLOSE_MSG = "Connection pool successfully closed";
    private final static String PROPERTIES_FILE_NAME = "connection-pool.properties";
    private final static String DB_URL_PROPERTY_NAME = "db.url";
    private final static String DB_DRIVER_NAME_PROPERTY_NAME = "db.driver_name";
    private final static String LOGIN_ENV_NAME = "MYSQL_RACING_SITE_USER";
    private final static String PASSWORD_ENV_NAME = "MYSQL_RACING_SITE_PASSWORD";

    private volatile static CommonConnectionPool instance;

    private static DataSource dataSource;

    private CommonConnectionPool() throws DataSourceDownException {
        try(InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)){
            Properties properties = new Properties();
            properties.load(inputStream);
            dataSource = new CommonDataSource(properties.getProperty(DB_URL_PROPERTY_NAME),
                    System.getenv(LOGIN_ENV_NAME), System.getenv(PASSWORD_ENV_NAME),
                    properties.getProperty(DB_DRIVER_NAME_PROPERTY_NAME));
            LOG.info(INIT_MSG);
        }catch (Exception ex){
            LOG.error(ex.getMessage(), ex);
            throw new DataSourceDownException(ex);
        }
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
        LOG.info(CLOSE_MSG);
    }
}
