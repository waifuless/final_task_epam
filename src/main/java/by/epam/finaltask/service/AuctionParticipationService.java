package by.epam.finaltask.service;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.AuctionParticipation;
import by.epam.finaltask.model.AuctionStatus;
import by.epam.finaltask.model.LotWithImages;

import java.util.Map;
import java.util.Optional;

public interface AuctionParticipationService {

    void saveParticipant(long requestedUserId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    void deleteParticipation(long requestedUserId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    void retrieveWinnerParticipationByLotOwner(long requestedUserId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    boolean isUserParticipateInLotAuction(long userId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    boolean isUserParticipateInLotAuction(long userId, long lotId)
            throws ServiceCanNotCompleteCommandRequest;

    Map<LotWithImages, AuctionParticipation> findLotsParticipatedByUser(long page, long userId)
            throws ServiceCanNotCompleteCommandRequest;

    long findPageCountParticipatedByUser(long userId)
            throws ServiceCanNotCompleteCommandRequest;

    Map<LotWithImages, AuctionParticipation> findLotsParticipatedByUser(long page, long userId,
                                                                        AuctionStatus status)
            throws ServiceCanNotCompleteCommandRequest;

    long findPageCountParticipatedByUser(long userId, AuctionStatus status)
            throws ServiceCanNotCompleteCommandRequest;

    Optional<AuctionParticipation> findParticipation(long userId, long lotId)
            throws ServiceCanNotCompleteCommandRequest;
}
