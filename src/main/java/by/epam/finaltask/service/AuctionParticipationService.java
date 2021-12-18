package by.epam.finaltask.service;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;

public interface AuctionParticipationService {

    void saveParticipant(long requestedUserId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    void deleteParticipation(long requestedUserId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    boolean isUserParticipateInLotAuction(long userId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    boolean isUserParticipateInLotAuction(long userId, long lotId)
            throws ServiceCanNotCompleteCommandRequest;
}
