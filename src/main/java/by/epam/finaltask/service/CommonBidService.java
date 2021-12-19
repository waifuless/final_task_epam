package by.epam.finaltask.service;

import by.epam.finaltask.dao.BidManager;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.AuctionStatus;
import by.epam.finaltask.model.AuctionType;
import by.epam.finaltask.model.Bid;
import by.epam.finaltask.model.Lot;
import by.epam.finaltask.validation.StringClientParameterValidator;
import by.epam.finaltask.validation.ValidatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

public class CommonBidService implements BidService {

    private final static Logger LOG = LogManager.getLogger(CommonBidService.class);

    private final static BigDecimal MIN_AUCTION_STEP_COEFFICIENT = BigDecimal.valueOf(0.05);

    private final StringClientParameterValidator clientParameterValidator =
            ValidatorFactory.getFactoryInstance().stringParameterValidator();
    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();
    private final AuctionParticipationService auctionParticipationService =
            ServiceFactory.getFactoryInstance().auctionParticipationService();

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
    public Bid findBestBid(long requesterId, String lotId)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            clientParameterValidator.validateNotEmpty(lotId);
            long longLotId = Long.parseLong(lotId);
            Lot lot = lotService.findLot(longLotId).orElseThrow(() -> new ClientErrorException(ClientError.NOT_FOUND));
            validateUserParticipation(requesterId, longLotId);
            Optional<Bid> optionalBid;
            if (lot.getAuctionType().equals(AuctionType.FORWARD)) {
                optionalBid = bidManager.findMaxBid(longLotId);
            } else {
                optionalBid = bidManager.findMinBid(longLotId);
            }
            return optionalBid.orElseThrow(() -> new ClientErrorException(ClientError.NOT_FOUND));
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

    private void validateBidAmount(Lot lot, BigDecimal amount)
            throws ClientErrorException, SQLException, InterruptedException {
        BigDecimal minStep = lot.getInitialPrice().multiply(MIN_AUCTION_STEP_COEFFICIENT);
        if (lot.getAuctionType().equals(AuctionType.FORWARD)) {
            if (lot.getInitialPrice().compareTo(amount) >= 0) {
                throw new ClientErrorException(ClientError.BID_AMOUNT_INVALID);
            }
            Optional<Bid> optionalBestBid = bidManager.findMaxBid(lot.getLotId());
            if(optionalBestBid.isPresent()){
                Bid bestBid = optionalBestBid.get();
                if(amount.subtract(bestBid.getAmount()).compareTo(minStep)<0){
                    throw new ClientErrorException(ClientError.BID_AMOUNT_INVALID);
                }
            }
        } else {
            if (lot.getInitialPrice().compareTo(amount) <= 0) {
                throw new ClientErrorException(ClientError.BID_AMOUNT_INVALID);
            }
            Optional<Bid> optionalBestBid = bidManager.findMinBid(lot.getLotId());
            if(optionalBestBid.isPresent()){
                Bid bestBid = optionalBestBid.get();
                if(bestBid.getAmount().subtract(amount).compareTo(minStep)<0){
                    throw new ClientErrorException(ClientError.BID_AMOUNT_INVALID);
                }
            }
        }
    }
}
