package by.epam.finaltask.service;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Lot;
import by.epam.finaltask.model.LotContext;
import by.epam.finaltask.model.LotWithImages;
import by.epam.finaltask.model.Role;

import java.util.List;
import java.util.Optional;

public interface LotService {

    Optional<LotWithImages> findLotWithImages(long id) throws ServiceCanNotCompleteCommandRequest;

    LotWithImages findLotWithImagesValidateUserAccess(long userId, String lotId, Role userRole)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    Optional<Lot> findLot(long id) throws ServiceCanNotCompleteCommandRequest;

    List<LotWithImages> findLotsByPage(long pageNumber) throws ServiceCanNotCompleteCommandRequest;

    void createAndSaveLot(long userId, String mainImagePath, String[] otherImagePaths, String title, String category,
                          String auctionType, String condition, String description, String initPrice,
                          String auctionStart, String duration, String region, String cityOrDistrict)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    void validateAuctionStartDate(String auctionStart) throws ClientErrorException;

    List<LotWithImages> findLotsByPageAndContext(long pageNumber, LotContext context)
            throws ServiceCanNotCompleteCommandRequest;

    long findLotPagesCount(LotContext context) throws ServiceCanNotCompleteCommandRequest;

    void updateLotsAuctionStatus(String[] lotIds, String newStatus)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    long findLotPagesCount() throws ServiceCanNotCompleteCommandRequest;

    void startLotsAuctionStatusAutoUpdate() throws ServiceCanNotCompleteCommandRequest;

    void stopLotsAuctionStatusAutoUpdate() throws ServiceCanNotCompleteCommandRequest;
}
