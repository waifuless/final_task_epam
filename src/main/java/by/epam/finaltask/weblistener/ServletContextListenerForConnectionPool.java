package by.epam.finaltask.weblistener;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class ServletContextListenerForConnectionPool implements ServletContextListener {

    private final static Logger LOG = LoggerFactory.getLogger(ServletContextListenerForConnectionPool.class);

    private final static String DESTROY_EXCEPTION_MCG = "exception while destroying servlet";
    private final static String INIT_EXCEPTION_MCG = "exception while initializing servlet";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            LotService lotService = ServiceFactory.getFactoryInstance().lotService();
            lotService.startLotsAuctionStatusAutoUpdate();
        } catch (Exception e) {
            LOG.error(INIT_EXCEPTION_MCG, e);
            throw new Error(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            LotService lotService = ServiceFactory.getFactoryInstance().lotService();
            lotService.stopLotsAuctionStatusAutoUpdate();
            ConnectionPool.getInstance().close();
        } catch (Exception e) {
            LOG.error(DESTROY_EXCEPTION_MCG, e);
        }
    }
}
