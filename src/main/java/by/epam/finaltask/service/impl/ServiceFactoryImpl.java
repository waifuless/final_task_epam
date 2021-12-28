package by.epam.finaltask.service.impl;

import by.epam.finaltask.dao.*;
import by.epam.finaltask.service.*;

import java.io.IOException;

public class ServiceFactoryImpl implements ServiceFactory {

    private static volatile ServiceFactory factoryInstance;
    private static volatile UserService userServiceInstance;
    private static volatile LotService lotServiceInstance;
    private static volatile ImagesService imagesServiceInstance;
    private static volatile CategoryService categoryServiceInstance;
    private static volatile CityOrDistrictService cityOrDistrictServiceInstance;
    private static volatile RegionService regionServiceInstance;
    private static volatile AuctionParticipationService auctionParticipationServiceInstance;
    private static volatile BidService bidServiceInstance;

    private ServiceFactoryImpl() {
    }

    public static ServiceFactory getFactoryInstance() {
        if (factoryInstance == null) {
            synchronized (ServiceFactoryImpl.class) {
                if (factoryInstance == null) {
                    factoryInstance = new ServiceFactoryImpl();
                }
            }
        }
        return factoryInstance;
    }

    public UserService userService() {
        if (userServiceInstance == null) {
            synchronized (UserServiceImpl.class) {
                if (userServiceInstance == null) {
                    userServiceInstance = new UserServiceImpl(UserDao.getInstance());
                }
            }
        }
        return userServiceInstance;
    }

    public LotService lotService() {
        if (lotServiceInstance == null) {
            synchronized (LotServiceImpl.class) {
                if (lotServiceInstance == null) {
                    lotServiceInstance = new LotServiceImpl(LotDao.getInstance(), ImagesDao.getInstance());
                }
            }
        }
        return lotServiceInstance;
    }

    public ImagesService imagesService() throws IOException {
        if (imagesServiceInstance == null) {
            synchronized (ImagesServiceImpl.class) {
                if (imagesServiceInstance == null) {
                    imagesServiceInstance = new ImagesServiceImpl(ImagesDao.getInstance());
                }
            }
        }
        return imagesServiceInstance;
    }

    public CategoryService categoryService() {
        if (categoryServiceInstance == null) {
            synchronized (CategoryServiceImpl.class) {
                if (categoryServiceInstance == null) {
                    categoryServiceInstance = new CategoryServiceImpl(CategoryDao.getInstance());
                }
            }
        }
        return categoryServiceInstance;
    }

    public CityOrDistrictService cityOrDistrictService() {
        if (cityOrDistrictServiceInstance == null) {
            synchronized (CityOrDistrictServiceImpl.class) {
                if (cityOrDistrictServiceInstance == null) {
                    cityOrDistrictServiceInstance =
                            new CityOrDistrictServiceImpl(CityOrDistrictDao.getInstance());
                }
            }
        }
        return cityOrDistrictServiceInstance;
    }

    public RegionService regionService() {
        if (regionServiceInstance == null) {
            synchronized (RegionServiceImpl.class) {
                if (regionServiceInstance == null) {
                    regionServiceInstance = new RegionServiceImpl(RegionDao.getInstance());
                }
            }
        }
        return regionServiceInstance;
    }

    public AuctionParticipationService auctionParticipationService() {
        if (auctionParticipationServiceInstance == null) {
            synchronized (AuctionParticipationServiceImpl.class) {
                if (auctionParticipationServiceInstance == null) {
                    auctionParticipationServiceInstance =
                            new AuctionParticipationServiceImpl(AuctionParticipationDao.getInstance());
                }
            }
        }
        return auctionParticipationServiceInstance;
    }

    public BidService bidService() {
        if (bidServiceInstance == null) {
            synchronized (BidServiceImpl.class) {
                if (bidServiceInstance == null) {
                    bidServiceInstance =
                            new BidServiceImpl(BidDao.getInstance());
                }
            }
        }
        return bidServiceInstance;
    }
}
