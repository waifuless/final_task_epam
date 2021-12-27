package by.epam.finaltask.service.impl;

import by.epam.finaltask.dao.BidDao;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.*;
import by.epam.finaltask.service.BidService;
import by.epam.finaltask.service.BigDecimalNormalizer;
import by.epam.finaltask.service.ServiceFactory;
import by.epam.finaltask.validation.NumberValidator;
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

public class BidServiceImpl implements BidService {

    private final static Logger LOG = LoggerFactory.getLogger(BidServiceImpl.class);

    private final static BigDecimal MIN_AUCTION_STEP_COEFFICIENT = BigDecimal.valueOf(0.05);
    private final static int MAX_BIG_DECIMAL_LENGTH = 15;

    private final BigDecimalNormalizer bigDecimalNormalizer = BigDecimalNormalizer.getInstance();

    private final StringClientParameterValidator stringValidator =
            ValidatorFactory.getFactoryInstance().stringParameterValidator();
    private final NumberValidator numberValidator = ValidatorFactory.getFactoryInstance().idValidator();

    private final ServiceFactory serviceFactory = ServiceFactory.getFactoryInstance();

    private final BidDao bidDao;

    BidServiceImpl(BidDao bidDao) {
        this.bidDao = bidDao;
    }

    @Override
    public void addBid(long userId, String lotId, String amount)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            numberValidator.validateNumberIsPositive(userId);
            stringValidator.validateNotEmpty(lotId, amount);
            long longLotId = Long.parseLong(lotId);
            numberValidator.validateNumberIsPositive(longLotId);
            BigDecimal bigDecimalAmount = bigDecimalNormalizer.normalize(new BigDecimal(amount));
            validateBid(userId, longLotId, bigDecimalAmount);
            bidDao.save(Bid.builder().setUserId(userId).setLotId(longLotId).setAmount(bigDecimalAmount).build());
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
        numberValidator.validateNumberIsPositive(requesterId);
        stringValidator.validateNotEmpty(lotId);
        return findBestBid(requesterId, Long.parseLong(lotId));
    }

    @Override
    public Optional<Bid> findBestBid(long requesterId, long lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            numberValidator.validateNumberIsPositive(requesterId, lotId);
            Lot lot = serviceFactory.lotService()
                    .findLot(lotId).orElseThrow(() -> new ClientErrorException(ClientError.NOT_FOUND));
            validateUserParticipationOrOwner(requesterId, lot);
            Optional<Bid> optionalBid;
            if (lot.getAuctionType().equals(AuctionType.FORWARD)) {
                optionalBid = bidDao.findMaxBid(lotId);
            } else {
                optionalBid = bidDao.findMinBid(lotId);
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
            numberValidator.validateNumberIsPositive(page, userId);
            Map<LotWithImages, AuctionParticipation> lotsWithAuctionParticipations = serviceFactory
                    .auctionParticipationService()
                    .findLotsParticipatedByUser(page, userId, AuctionStatus.ENDED);
            Optional<Bid> optionalBid;
            Map<LotWithImages, AuctionResult> participationByWonLots = new LinkedHashMap<>();
            for (Map.Entry<LotWithImages, AuctionParticipation> entry : lotsWithAuctionParticipations.entrySet()) {
                if (entry.getKey().getAuctionType().equals(AuctionType.FORWARD)) {
                    optionalBid = bidDao.findMaxBid(entry.getKey().getLotId());
                } else {
                    optionalBid = bidDao.findMinBid(entry.getKey().getLotId());
                }
                if (optionalBid.isPresent()) {
                    Bid bid = optionalBid.get();
                    if (bid.getUserId() == userId) {
                        Optional<User> optionalUser = serviceFactory.userService()
                                .findUserById(entry.getKey().getOwnerId());
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
            numberValidator.validateNumberIsPositive(page, userId);
            List<LotWithImages> lots = serviceFactory.lotService()
                    .findLotsByPageAndContext(page, LotContext.builder().setOwnerId(userId)
                            .setAuctionStatus(AuctionStatus.ENDED.name()).build());
            Optional<Bid> optionalBid;
            Map<LotWithImages, AuctionResult> wonEmailByUserLots = new LinkedHashMap<>();
            for (LotWithImages lot : lots) {
                if (lot.getAuctionType().equals(AuctionType.FORWARD)) {
                    optionalBid = bidDao.findMaxBid(lot.getLotId());
                } else {
                    optionalBid = bidDao.findMinBid(lot.getLotId());
                }
                if (optionalBid.isPresent()) {
                    Bid bid = optionalBid.get();
                    Optional<User> optionalUser = serviceFactory.userService().findUserById(bid.getUserId());
                    if (optionalUser.isPresent()) {
                        Optional<AuctionParticipation> optionalParticipation = serviceFactory
                                .auctionParticipationService()
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
            numberValidator.validateNumberIsPositive(userId, lotId);
            bidDao.deleteByUserIdAndLotId(userId, lotId);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    private void validateBid(long userId, long lotId, BigDecimal amount)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException, SQLException, InterruptedException {
        Lot lot = serviceFactory.lotService()
                .findLot(lotId).orElseThrow(() -> new ClientErrorException(ClientError.NOT_FOUND));
        if (!lot.getAuctionStatus().equals(AuctionStatus.RUNNING)) {
            throw new ClientErrorException(ClientError.LOT_NOT_RUNNING);
        }
        validateUserParticipation(userId, lotId);
        validateBidAmount(lot, amount);
    }

    private void validateUserParticipation(long userId, long lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        if (!serviceFactory.auctionParticipationService().isUserParticipateInLotAuction(userId, lotId)) {
            throw new ClientErrorException(ClientError.FORBIDDEN);
        }
    }

    private void validateUserParticipationOrOwner(long userId, Lot lot)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        if (!serviceFactory.auctionParticipationService().isUserParticipateInLotAuction(userId, lot.getLotId())
                && userId != lot.getOwnerId()) {
            throw new ClientErrorException(ClientError.FORBIDDEN);
        }
    }

    private void validateBidAmount(Lot lot, BigDecimal amount)
            throws ClientErrorException, SQLException, InterruptedException {
        BigDecimal minStep = lot.getInitialPrice().multiply(MIN_AUCTION_STEP_COEFFICIENT);
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || integerDigits(amount) > MAX_BIG_DECIMAL_LENGTH) {
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        }
        if (lot.getAuctionType().equals(AuctionType.FORWARD)) {
            if (lot.getInitialPrice().compareTo(amount) >= 0) {
                throw new ClientErrorException(ClientError.BID_AMOUNT_INVALID);
            }
            Optional<Bid> optionalBestBid = bidDao.findMaxBid(lot.getLotId());
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
            Optional<Bid> optionalBestBid = bidDao.findMinBid(lot.getLotId());
            if (optionalBestBid.isPresent()) {
                Bid bestBid = optionalBestBid.get();
                if (bestBid.getAmount().subtract(amount).compareTo(minStep) < 0) {
                    throw new ClientErrorException(ClientError.BID_AMOUNT_INVALID);
                }
            }
        }
    }

    private int integerDigits(BigDecimal n) {
        n = n.stripTrailingZeros();
        return n.precision() - n.scale();
    }
}
