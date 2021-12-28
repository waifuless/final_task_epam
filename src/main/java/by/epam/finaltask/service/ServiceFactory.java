package by.epam.finaltask.service;

import by.epam.finaltask.service.impl.ServiceFactoryImpl;

import java.io.IOException;

public interface ServiceFactory {

    static ServiceFactory getFactoryInstance() {
        return ServiceFactoryImpl.getFactoryInstance();
    }

    UserService userService();

    LotService lotService();

    ImagesService imagesService() throws IOException;

    CategoryService categoryService();

    CityOrDistrictService cityOrDistrictService();

    RegionService regionService();

    AuctionParticipationService auctionParticipationService();

    BidService bidService();
}
