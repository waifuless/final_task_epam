package by.epam.finaltask.service;

import by.epam.finaltask.dao.ImagesManager;
import by.epam.finaltask.dao.LotManager;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CommonLotService implements LotService {

    private final static Logger LOG = LogManager.getLogger(CommonLotService.class);

    private final static String LOT_DID_NOT_SAVED_ERROR_MESSAGE =
            "Error occurred while saving lot to database. Lot: {}";

    private final static int MAX_BIAS_IN_MINUTES = 5;
    private final static int ZERO_BIAS = 0;
    private final static int LOTS_PER_PAGE = 8;
    private final static int MIN_DAYS_BEFORE_START_AUCTION = 4;
    private final static int MIN_DURATION_HOURS = 2;
    private final static int MAX_DURATION_HOURS = 24;
    private final static int ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;

    private final LotManager lotManager;
    private final ImagesManager imagesManager;

    CommonLotService(LotManager lotManager, ImagesManager imagesManager) {
        this.lotManager = lotManager;
        this.imagesManager = imagesManager;
    }

    @Override
    public Optional<LotWithImages> findLot(long id) throws ServiceCanNotCompleteCommandRequest {
        try {
            Optional<Lot> optionalLot = lotManager.find(id);
            if (optionalLot.isPresent()) {
                LotWithImages lotWithImages = new LotWithImages(optionalLot.get(),
                        imagesManager.find(id).orElse(null));
                return Optional.of(lotWithImages);
            }
            return Optional.empty();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public List<LotWithImages> findLotsByPage(long pageNumber) throws ServiceCanNotCompleteCommandRequest {
        try {
            List<Lot> lots = lotManager.find((pageNumber - 1) * LOTS_PER_PAGE, LOTS_PER_PAGE);
            return findLotsWithImages(lots);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void createAndSaveLot(long userId, String mainImagePath, String[] otherImagePaths, String title,
                                 String category, String auctionType, String condition, String description,
                                 String initPrice, String auctionStart, String duration, String region,
                                 String cityOrDistrict)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            validateCreationParams(mainImagePath, otherImagePaths, title, category, auctionType, condition,
                    description, initPrice, auctionStart, duration, region, cityOrDistrict);
            Timestamp startDatetime = Timestamp.valueOf(reformatForTimestamp(auctionStart));
            int intDuration = Integer.parseInt(duration);
            validateAuctionStartDate(startDatetime, MAX_BIAS_IN_MINUTES);
            startDatetime = roundTimeToHours(startDatetime);
            validateDuration(intDuration);
            Timestamp endDatetime =
                    new Timestamp(startDatetime.getTime() + (long) Integer.parseInt(duration) * ONE_HOUR_IN_MILLIS);
            Lot lot = Lot.builder().setOwnerId(userId).setCategory(category).
                    setAuctionType(AuctionType.valueOf(auctionType)).setTitle(title).setStartDatetime(startDatetime)
                    .setEndDatetime(endDatetime).setInitialPrice(new BigDecimal(initPrice)).setRegion(region)
                    .setCityOrDistrict(cityOrDistrict).setDescription(description)
                    .setAuctionStatus(AuctionStatus.NOT_VERIFIED)
                    .setProductCondition(ProductCondition.valueOf(condition)).build();
            Optional<Lot> optionalLot = lotManager.save(lot);
            if (optionalLot.isPresent()) {
                Lot savedLot = optionalLot.get();
                List<String> imagePaths = otherImagePaths == null ? Collections.emptyList()
                        : Arrays.asList(otherImagePaths);
                Images images = new Images(savedLot.getLotId(), mainImagePath, imagePaths);
                imagesManager.save(images);
            } else {
                LOG.error(LOT_DID_NOT_SAVED_ERROR_MESSAGE, lot);
                throw new ServiceCanNotCompleteCommandRequest(LOT_DID_NOT_SAVED_ERROR_MESSAGE);
            }
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void validateAuctionStartDate(String auctionStart) throws ClientErrorException {
        Timestamp startDatetime = Timestamp.valueOf(reformatForTimestamp(auctionStart));
        validateAuctionStartDate(startDatetime, ZERO_BIAS);
    }

    @Override
    public List<LotWithImages> findLotsByPageAndContext(long pageNumber, LotContext context)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            List<Lot> lots = lotManager.findByLotContext(context,
                    (pageNumber - 1) * LOTS_PER_PAGE, LOTS_PER_PAGE);
            return findLotsWithImages(lots);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public long findLotPagesCount(LotContext context) throws ServiceCanNotCompleteCommandRequest {
        try {
            long lotsCount = lotManager.findLotsCount(context);
            return lotsCount / LOTS_PER_PAGE + (lotsCount % LOTS_PER_PAGE == 0 ? 0 : 1);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void updateLotsAuctionStatus(String[] lotIds, String newStatus)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            if (lotIds == null || lotIds.length < 1 || newStatus == null || newStatus.trim().isEmpty()) {
                throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
            }
            AuctionStatus auctionStatus = AuctionStatus.valueOf(newStatus);
            int[] ids = new int[lotIds.length];
            for (int i = 0; i < lotIds.length; i++) {
                ids[i] = Integer.parseInt(lotIds[i]);
            }
            for (int lotId : ids) {
                Optional<Lot> optionalLot = lotManager.find(lotId);
                if (optionalLot.isPresent()) {
                    lotManager.update(Lot.builder().setLot(optionalLot.get())
                            .setAuctionStatus(auctionStatus).build());
                }
            }
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage());
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        } catch (IllegalArgumentException ex) {
            LOG.warn(ex.getMessage());
            throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public long findLotPagesCount() throws ServiceCanNotCompleteCommandRequest {
        try {
            long lotsCount = lotManager.count();
            return lotsCount / LOTS_PER_PAGE + (lotsCount % LOTS_PER_PAGE == 0 ? 0 : 1);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void startLotsAuctionStatusAutoUpdate() throws ServiceCanNotCompleteCommandRequest {
        try {
            LocalDateTime scheduleStart = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(1);
            LOG.debug("Start of schedule to renew lots: {}", scheduleStart);
            lotManager.executeLotsRenewAndCreateEventSchedule(Timestamp.valueOf(scheduleStart));
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void stopLotsAuctionStatusAutoUpdate() throws ServiceCanNotCompleteCommandRequest {
        try {
            lotManager.dropLotsRenewEventSchedule();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    private List<LotWithImages> findLotsWithImages(List<Lot> lots)
            throws java.sql.SQLException, InterruptedException {
        List<LotWithImages> lotsWithImages = new LinkedList<>();
        for (Lot lot : lots) {
            lotsWithImages.add(new LotWithImages(lot, imagesManager.find(lot.getLotId())
                    .orElse(new Images(lot.getLotId(), new Images.Image("")))));
        }
        LOG.debug("LotsWithImages: {}", lotsWithImages);
        return lotsWithImages;
    }

    private Timestamp roundTimeToHours(Timestamp timestamp){
        return Timestamp.valueOf(timestamp.toLocalDateTime().truncatedTo(ChronoUnit.HOURS));
    }

    private void validateCreationParams(String mainImagePath, String[] otherImagePaths, String title,
                                        String category, String auctionType, String condition, String description,
                                        String initPrice, String auctionStart, String duration, String region,
                                        String cityOrDistrict) throws ClientErrorException {
        //todo: add validation to date and images(should be in database)
        if (isStringEmpty(mainImagePath) || isStringEmpty(title) || isStringEmpty(category)
                || isStringEmpty(auctionType) || isStringEmpty(condition) || isStringEmpty(description)
                || isStringEmpty(initPrice) || isStringEmpty(auctionStart) || isStringEmpty(duration)
                || isStringEmpty(region) || isStringEmpty(cityOrDistrict)) {
            LOG.debug("One of required fields is empty");
            throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
        }
    }

    private boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private String reformatForTimestamp(String time) {
        time = time.replace("T", " ");
        return time.indexOf(':') == time.lastIndexOf(':') ? time.concat(":00") : time;
    }

    private void validateAuctionStartDate(Timestamp startTimeStamp, int biasMinutes) throws ClientErrorException {
        LocalDate startDate = startTimeStamp.toLocalDateTime().toLocalDate();
        if (startDate.minusDays(MIN_DAYS_BEFORE_START_AUCTION).compareTo(LocalDateTime.now()
                .minusMinutes(biasMinutes).toLocalDate()) < 0) {
            throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
        }
    }

    private void validateDuration(int duration) throws ClientErrorException {
        if (duration < MIN_DURATION_HOURS || duration > MAX_DURATION_HOURS) {
            throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
        }
    }
}