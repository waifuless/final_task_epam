package by.epam.finaltask.service;

import by.epam.finaltask.dao.AuctionParticipationManager;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.AuctionParticipation;
import by.epam.finaltask.model.AuctionStatus;
import by.epam.finaltask.model.Lot;
import by.epam.finaltask.model.LotWithImages;
import by.epam.finaltask.validation.StringClientParameterValidator;
import by.epam.finaltask.validation.ValidatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

public class CommonAuctionParticipationService implements AuctionParticipationService {

    private final static Logger LOG = LogManager.getLogger(CommonAuctionParticipationService.class);

    private final static BigDecimal DEFAULT_DEPOSIT_COEFFICIENT = BigDecimal.valueOf(0.1);
    private final static int LOTS_PER_PAGE = 8;

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();
    private final UserService userService = ServiceFactory.getFactoryInstance().userService();
    private final StringClientParameterValidator validator = ValidatorFactory
            .getFactoryInstance().stringParameterValidator();

    private final AuctionParticipationManager auctionParticipationManager;

    CommonAuctionParticipationService(AuctionParticipationManager auctionParticipationManager) {
        this.auctionParticipationManager = auctionParticipationManager;
    }

    @Override
    public void saveParticipant(long requestedUserId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            validator.validateNotEmpty(lotId);
            long longLotId = Long.parseLong(lotId);
            if (isUserParticipateInLotAuction(requestedUserId, longLotId)) {
                throw new ClientErrorException(ClientError.ENTITY_ALREADY_EXISTS);
            }
            Lot lot = lotService.findLot(longLotId).orElseThrow(() -> new ClientErrorException(ClientError.NOT_FOUND));
            if (lot.getOwnerId() == requestedUserId ||
                    !lot.getAuctionStatus().equals(AuctionStatus.APPROVED_BY_ADMIN)) {
                throw new ClientErrorException(ClientError.FORBIDDEN);
            }
            BigDecimal deposit = lot.getInitialPrice().multiply(DEFAULT_DEPOSIT_COEFFICIENT);
            //todo: transaction
            userService.minusFromCashAccount(requestedUserId, deposit);
            auctionParticipationManager
                    .saveParticipation(new AuctionParticipation(requestedUserId, longLotId, deposit));
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage());
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void deleteParticipation(long requestedUserId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            validator.validateNotEmpty(lotId);
            long longLotId = Long.parseLong(lotId);
            AuctionParticipation participation = auctionParticipationManager
                    .findParticipation(requestedUserId, longLotId)
                    .orElseThrow(() -> new ClientErrorException(ClientError.NOT_FOUND));
            Lot lot = lotService.findLot(longLotId).orElseThrow(() -> new ClientErrorException(ClientError.NOT_FOUND));
            if (lot.getOwnerId() == requestedUserId ||
                    !lot.getAuctionStatus().equals(AuctionStatus.APPROVED_BY_ADMIN)) {
                throw new ClientErrorException(ClientError.FORBIDDEN);
            }
            //todo: transaction
            userService.plusToCashAccount(requestedUserId, participation.getDeposit());
            auctionParticipationManager.deleteParticipation(requestedUserId, longLotId);
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage());
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public boolean isUserParticipateInLotAuction(long userId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            validator.validateNotEmpty(lotId);
            long longLotId = Long.parseLong(lotId);
            return isUserParticipateInLotAuction(userId, longLotId);
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage());
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        }
    }

    @Override
    public boolean isUserParticipateInLotAuction(long userId, long lotId)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            return auctionParticipationManager.isUserParticipateInLotAuction(userId, lotId);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public Map<LotWithImages, AuctionParticipation> findLotsParticipatedByUser(long page, long userId)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            List<AuctionParticipation> auctionParticipations = auctionParticipationManager
                    .findUsersParticipations(userId, (page - 1) * LOTS_PER_PAGE, LOTS_PER_PAGE);
            return findResultsByLotsWithImages(auctionParticipations);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public long findPageCountParticipatedByUser(long userId) throws ServiceCanNotCompleteCommandRequest {
        try {
            long participationsCount = auctionParticipationManager.findUsersParticipationsCount(userId);
            return participationsCount / LOTS_PER_PAGE + (participationsCount % LOTS_PER_PAGE == 0 ? 0 : 1);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public Map<LotWithImages, AuctionParticipation> findLotsParticipatedByUser(long page, long userId,
                                                                               AuctionStatus status)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            List<AuctionParticipation> auctionParticipations = auctionParticipationManager
                    .findUsersParticipations(userId,
                            (page - 1) * LOTS_PER_PAGE, LOTS_PER_PAGE, status);
            return findResultsByLotsWithImages(auctionParticipations);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public long findPageCountParticipatedByUser(long userId, AuctionStatus status)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            long participationsCount = auctionParticipationManager.findUsersParticipationsCount(userId, status);
            return participationsCount / LOTS_PER_PAGE + (participationsCount % LOTS_PER_PAGE == 0 ? 0 : 1);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public Optional<AuctionParticipation> findParticipation(long userId, long lotId)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            return auctionParticipationManager.findParticipation(userId, lotId);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    private Map<LotWithImages, AuctionParticipation> findResultsByLotsWithImages
            (List<AuctionParticipation> auctionParticipations)
            throws ServiceCanNotCompleteCommandRequest {
        Map<LotWithImages, AuctionParticipation> lotsWithParticipations = new LinkedHashMap<>();
        for (AuctionParticipation auctionParticipation : auctionParticipations) {
            Optional<LotWithImages> optionalLot = lotService.findLotWithImages(auctionParticipation.getLotId());
            optionalLot.ifPresent(lot -> lotsWithParticipations.put(lot, auctionParticipation));
        }
        return lotsWithParticipations;
    }
}
