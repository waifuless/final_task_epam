package by.epam.finaltask.service;

import by.epam.finaltask.dao.BidManager;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.*;
import by.epam.finaltask.validation.StringClientParameterValidator;
import by.epam.finaltask.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommonBidService implements BidService {

    private final static Logger LOG = LoggerFactory.getLogger(CommonBidService.class);

    private final static BigDecimal MIN_AUCTION_STEP_COEFFICIENT = BigDecimal.valueOf(0.05);

    private final StringClientParameterValidator clientParameterValidator =
            ValidatorFactory.getFactoryInstance().stringParameterValidator();
    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();
    private final AuctionParticipationService auctionParticipationService =
            ServiceFactory.getFactoryInstance().auctionParticipationService();
    private final UserService userService = ServiceFactory.getFactoryInstance().userService();

    private final BidManager bidManager;

    CommonBidService(BidManager bidManager) {
        this.bidManager = bidManager;
    }

    @Override
    public void addBid(long userId, String lotId, String amount)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            clientParameterValidator.validateNotEmpty(lotId, amount);
            long longLotId = Long.parseLong(lotId);
            BigDecimal bigDecimalAmount = new BigDecimal(amount);
            validateBid(userId, longLotId, bigDecimalAmount);
            bidManager.save(Bid.builder().setUserId(userId).setLotId(longLotId).setAmount(bigDecimalAmount).build());
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
    public Optional<Bid> findBestBid(long requesterId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        clientParameterValidator.validateNotEmpty(lotId);
        return findBestBid(requesterId, Long.parseLong(lotId));
    }

    @Override
    public Optional<Bid> findBestBid(long requesterId, long lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            Lot lot = lotService.findLot(lotId).orElseThrow(() -> new ClientErrorException(ClientError.NOT_FOUND));
            validateUserParticipationOrOwner(requesterId, lot);
            Optional<Bid> optionalBid;
            if (lot.getAuctionType().equals(AuctionType.FORWARD)) {
                optionalBid = bidManager.findMaxBid(lotId);
            } else {
                optionalBid = bidManager.findMinBid(lotId);
            }
            return optionalBid;
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
    public Map<LotWithImages, AuctionResult> findUserParticipatedEndedLotsWithAuctionResult(long page, long userId)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            Map<LotWithImages, AuctionParticipation> lotsWithAuctionParticipations = auctionParticipationService
                    .findLotsParticipatedByUser(page, userId, AuctionStatus.ENDED);
            Optional<Bid> optionalBid;
            Map<LotWithImages, AuctionResult> participationByWonLots = new LinkedHashMap<>();
            for (Map.Entry<LotWithImages, AuctionParticipation> entry : lotsWithAuctionParticipations.entrySet()) {
                if (entry.getKey().getAuctionType().equals(AuctionType.FORWARD)) {
                    optionalBid = bidManager.findMaxBid(entry.getKey().getLotId());
                } else {
                    optionalBid = bidManager.findMinBid(entry.getKey().getLotId());
                }
                if (optionalBid.isPresent()) {
                    Bid bid = optionalBid.get();
                    if (bid.getUserId() == userId) {
                        Optional<User> optionalUser = userService.findUserById(entry.getKey().getOwnerId());
                        if (optionalUser.isPresent()) {
                            participationByWonLots.put(entry.getKey(), new AuctionResult(bid.getAmount(),
                                    optionalUser.get().getEmail(), entry.getValue().getDeposit(),
                                    entry.getValue().isDepositIsTakenByOwner()));
                        } else {
                            participationByWonLots.put(entry.getKey(), null);
                        }
                    } else {
                        participationByWonLots.put(entry.getKey(), null);
                    }
                } else {
                    participationByWonLots.put(entry.getKey(), null);
                }
            }
            return participationByWonLots;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public Map<LotWithImages, AuctionResult> findUserOwnedEndedLotsWithAuctionResult(long page, long userId)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            List<LotWithImages> lots = lotService.findLotsByPageAndContext(page, LotContext.builder().setOwnerId(userId)
                    .setAuctionStatus(AuctionStatus.ENDED.name()).build());
            Optional<Bid> optionalBid;
            Map<LotWithImages, AuctionResult> wonEmailByUserLots = new LinkedHashMap<>();
            for (LotWithImages lot : lots) {
                if (lot.getAuctionType().equals(AuctionType.FORWARD)) {
                    optionalBid = bidManager.findMaxBid(lot.getLotId());
                } else {
                    optionalBid = bidManager.findMinBid(lot.getLotId());
                }
                if (optionalBid.isPresent()) {
                    Bid bid = optionalBid.get();
                    Optional<User> optionalUser = userService.findUserById(bid.getUserId());
                    if (optionalUser.isPresent()) {
                        Optional<AuctionParticipation> optionalParticipation = auctionParticipationService
                                .findParticipation(bid.getUserId(), bid.getLotId());
                        if (optionalParticipation.isPresent()) {
                            wonEmailByUserLots.put(lot, new AuctionResult(bid.getAmount(),
                                    optionalUser.get().getEmail(), optionalParticipation.get().getDeposit(),
                                    optionalParticipation.get().isDepositIsTakenByOwner()));
                        } else {
                            wonEmailByUserLots.put(lot, null);
                        }
                    } else {
                        wonEmailByUserLots.put(lot, null);
                    }
                } else {
                    wonEmailByUserLots.put(lot, null);
                }
            }
            return wonEmailByUserLots;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void deleteUsersLotBids(long userId, long lotId) throws ServiceCanNotCompleteCommandRequest {
        try {
            bidManager.deleteByUserIdAndLotId(userId, lotId);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    private void validateBid(long userId, long lotId, BigDecimal amount)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException, SQLException, InterruptedException {
        Lot lot = lotService.findLot(lotId).orElseThrow(() -> new ClientErrorException(ClientError.NOT_FOUND));
        if (!lot.getAuctionStatus().equals(AuctionStatus.RUNNING)) {
            throw new ClientErrorException(ClientError.LOT_NOT_RUNNING);
        }
        validateUserParticipation(userId, lotId);
        validateBidAmount(lot, amount);
    }

    private void validateUserParticipation(long userId, long lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        if (!auctionParticipationService.isUserParticipateInLotAuction(userId, lotId)) {
            throw new ClientErrorException(ClientError.FORBIDDEN);
        }
    }

    private void validateUserParticipationOrOwner(long userId, Lot lot)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        if (!auctionParticipationService.isUserParticipateInLotAuction(userId, lot.getLotId())
                && userId != lot.getOwnerId()) {
            throw new ClientErrorException(ClientError.FORBIDDEN);
        }
    }

    private void validateBidAmount(Lot lot, BigDecimal amount)
            throws ClientErrorException, SQLException, InterruptedException {
        BigDecimal minStep = lot.getInitialPrice().multiply(MIN_AUCTION_STEP_COEFFICIENT);
        if (lot.getAuctionType().equals(AuctionType.FORWARD)) {
            if (lot.getInitialPrice().compareTo(amount) >= 0) {
                throw new ClientErrorException(ClientError.BID_AMOUNT_INVALID);
            }
            Optional<Bid> optionalBestBid = bidManager.findMaxBid(lot.getLotId());
            if (optionalBestBid.isPresent()) {
                Bid bestBid = optionalBestBid.get();
                if (amount.subtract(bestBid.getAmount()).compareTo(minStep) < 0) {
                    throw new ClientErrorException(ClientError.BID_AMOUNT_INVALID);
                }
            }
        } else {
            if (lot.getInitialPrice().compareTo(amount) <= 0) {
                throw new ClientErrorException(ClientError.BID_AMOUNT_INVALID);
            }
            Optional<Bid> optionalBestBid = bidManager.findMinBid(lot.getLotId());
            if (optionalBestBid.isPresent()) {
                Bid bestBid = optionalBestBid.get();
                if (bestBid.getAmount().subtract(amount).compareTo(minStep) < 0) {
                    throw new ClientErrorException(ClientError.BID_AMOUNT_INVALID);
                }
            }
        }
    }
}
