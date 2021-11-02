package by.epam.finaltask.service;

import by.epam.finaltask.dao.UserManager;

public class ServiceFactory {

    private static volatile ServiceFactory factoryInstance;
    private static volatile UserService userServiceInstance;


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
}
