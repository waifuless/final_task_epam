package by.epam.finaltask.service;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.AuctionResult;
import by.epam.finaltask.model.Bid;
import by.epam.finaltask.model.LotWithImages;

import java.util.Map;
import java.util.Optional;

public interface BidService {

    void addBid(long userId, String lotId, String amount)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    Optional<Bid> findBestBid(long requesterId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    Optional<Bid> findBestBid(long requesterId, long lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    Map<LotWithImages, AuctionResult> findUserParticipatedEndedLotsWithAuctionResult(long page, long userId)
            throws ServiceCanNotCompleteCommandRequest;

    Map<LotWithImages, AuctionResult> findUserOwnedEndedLotsWithAuctionResult(long page, long userId)
            throws ServiceCanNotCompleteCommandRequest;
}
