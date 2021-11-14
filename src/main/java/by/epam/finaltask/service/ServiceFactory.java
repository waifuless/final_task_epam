package by.epam.finaltask.service;

import by.epam.finaltask.dao.*;

import java.io.IOException;

public class ServiceFactory {

    private static volatile ServiceFactory factoryInstance;
    private static volatile UserService userServiceInstance;
    private static volatile LotService lotServiceInstance;
    private static volatile ImagesService imagesServiceInstance;
    private static volatile CategoryService categoryServiceInstance;
    private static volatile CityOrDistrictService cityOrDistrictServiceInstance;
    private static volatile RegionService regionServiceInstance;

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

    public ImagesService imagesService() throws IOException {
        if (imagesServiceInstance == null) {
            synchronized (CommonImagesService.class) {
                if (imagesServiceInstance == null) {
                    imagesServiceInstance = new CommonImagesService(ImagesManager.getInstance());
                }
            }
        }
        return imagesServiceInstance;
    }

    public CategoryService categoryService() {
        if (categoryServiceInstance == null) {
            synchronized (CommonCategoryService.class) {
                if (categoryServiceInstance == null) {
                    categoryServiceInstance = new CommonCategoryService(CategoryManager.getInstance());
                }
            }
        }
        return categoryServiceInstance;
    }

    public CityOrDistrictService cityOrDistrictService() {
        if (cityOrDistrictServiceInstance == null) {
            synchronized (CommonCityOrDistrictService.class) {
                if (cityOrDistrictServiceInstance == null) {
                    cityOrDistrictServiceInstance =
                            new CommonCityOrDistrictService(CityOrDistrictManager.getInstance());
                }
            }
        }
        return cityOrDistrictServiceInstance;
    }

    public RegionService regionService() {
        if (regionServiceInstance == null) {
            synchronized (CommonRegionService.class) {
                if (regionServiceInstance == null) {
                    regionServiceInstance = new CommonRegionService(RegionManager.getInstance());
                }
            }
        }
        return regionServiceInstance;
    }
}
