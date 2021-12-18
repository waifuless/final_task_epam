package by.epam.finaltask.service;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;

import java.math.BigDecimal;

public interface BidService {

    void addBid(long userId, String lotId, String amount)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    BigDecimal findBestBidAmount(long requesterId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;
}
