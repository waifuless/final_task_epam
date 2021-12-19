package by.epam.finaltask.service;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.AuctionResult;
import by.epam.finaltask.model.Bid;
import by.epam.finaltask.model.LotWithImages;

import java.util.Map;

public interface BidService {

    void addBid(long userId, String lotId, String amount)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    Bid findBestBid(long requesterId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    Map<LotWithImages, AuctionResult> findUserWonLotsWithAuctionResult(long page, long userId)
            throws ServiceCanNotCompleteCommandRequest;

    Map<LotWithImages, AuctionResult> findUsersEndedLotsWithAuctionResult(long page, long userId)
            throws ServiceCanNotCompleteCommandRequest;
}
