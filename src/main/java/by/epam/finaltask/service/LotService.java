package by.epam.finaltask.service;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.LotContext;
import by.epam.finaltask.model.LotWithImages;

import java.util.List;
import java.util.Optional;

public interface LotService {

    Optional<LotWithImages> findLot(int id) throws ServiceCanNotCompleteCommandRequest;

    List<LotWithImages> findLotsByPage(int pageNumber) throws ServiceCanNotCompleteCommandRequest;

    void createAndSaveLot(long userId, String mainImagePath, String[] otherImagePaths, String title, String category,
                          String auctionType, String condition, String description, String initPrice,
                          String auctionStart, String duration, String region, String cityOrDistrict)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    List<LotWithImages> findLotsByPageAndContext(int pageNumber, LotContext context)
            throws ServiceCanNotCompleteCommandRequest;

    void updateLotsAuctionStatus(String[] lotIds, String newStatus)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;
}
