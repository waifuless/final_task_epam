package by.epam.finaltask.service;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Bid;

import java.math.BigDecimal;

public interface BidService {

    void addBid(long userId, String lotId, String amount)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    Bid findBestBid(long requesterId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;
}
