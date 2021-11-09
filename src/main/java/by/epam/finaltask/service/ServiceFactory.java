package by.epam.finaltask.service;

import by.epam.finaltask.dao.ImagesManager;
import by.epam.finaltask.dao.LotManager;
import by.epam.finaltask.dao.UserManager;

public class ServiceFactory {

    private static volatile ServiceFactory factoryInstance;
    private static volatile UserService userServiceInstance;
    private static volatile LotService lotServiceInstance;

    private ServiceFactory() {
    }

    public static ServiceFactory getFactoryInstance() {
        if (factoryInstance == null) {
            synchronized (ServiceFactory.class) {
                if (factoryInstance == null) {
                    factoryInstance = new ServiceFactory();
                }
            }
        }
        return factoryInstance;
    }

    public UserService userService() {
        if (userServiceInstance == null) {
            synchronized (CommonUserService.class) {
                if (userServiceInstance == null) {
                    userServiceInstance = new CommonUserService(UserManager.getInstance());
                }
            }
        }
        return userServiceInstance;
    }

    public LotService lotService() {
        if (lotServiceInstance == null) {
            synchronized (CommonLotService.class) {
                if (lotServiceInstance == null) {
                    lotServiceInstance = new CommonLotService(LotManager.getInstance(), ImagesManager.getInstance());
                }
            }
        }
        return lotServiceInstance;
    }
}
