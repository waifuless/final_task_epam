package by.epam.finaltask.service.impl;

import by.epam.finaltask.dao.ImagesDao;
import by.epam.finaltask.dao.LotDao;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.*;
import by.epam.finaltask.service.AuctionParticipationService;
import by.epam.finaltask.service.BigDecimalNormalizer;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;
import by.epam.finaltask.validation.NumberValidator;
import by.epam.finaltask.validation.StringClientParameterValidator;
import by.epam.finaltask.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class LotServiceImpl implements LotService {

    private final static Logger LOG = LoggerFactory.getLogger(LotServiceImpl.class);

    private final static String LOT_DID_NOT_SAVED_ERROR_MESSAGE =
            "Error occurred while saving lot to database. Lot: {}";

    private final static int MAX_IMAGE_PATH_SIZE = 256;
    private final static int MAX_TITLE_SIZE = 256;
    private final static int MAX_CATEGORY_SIZE = 64;
    private final static int MAX_REGION_SIZE = 60;
    private final static int MAX_CITY_OR_DISTRICT_SIZE = 60;
    private final static int MAX_INIT_PRICE_LENGTH = 14;
    private final static int MAX_BIAS_IN_MINUTES = 5;
    private final static int ZERO_BIAS = 0;
    private final static int LOTS_PER_PAGE = 8;
    private final static int MIN_DAYS_BEFORE_START_AUCTION = 4;
    private final static int MIN_DURATION_HOURS = 2;
    private final static int MAX_DURATION_HOURS = 24;
    private final static int ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;

    private final ServiceFactory serviceFactory = ServiceFactory.getFactoryInstance();

    private final BigDecimalNormalizer bigDecimalNormalizer = BigDecimalNormalizer.getInstance();
    private final StringClientParameterValidator stringValidator = ValidatorFactory.getFactoryInstance()
            .stringParameterValidator();
    private final NumberValidator numberValidator = ValidatorFactory.getFactoryInstance().idValidator();

    private final LotDao lotDao;
    private final ImagesDao imagesDao;

    LotServiceImpl(LotDao lotDao, ImagesDao imagesDao) {
        this.lotDao = lotDao;
        this.imagesDao = imagesDao;
    }

    @Override
    public Optional<LotWithImages> findLotWithImages(long id) throws ServiceCanNotCompleteCommandRequest {
        try {
            numberValidator.validateNumberIsPositive(id);
            Optional<Lot> optionalLot = lotDao.find(id);
            if (optionalLot.isPresent()) {
                LotWithImages lotWithImages = new LotWithImages(optionalLot.get(),
                        imagesDao.find(id).orElse(new Images(optionalLot.get().getLotId(),
                                new Images.Image(""))));
                return Optional.of(lotWithImages);
            }
            return Optional.empty();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public LotWithImages findLotWithImagesValidateUserAccess(long userId, String lotId, Role userRole)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            stringValidator.validateNotEmpty(lotId);
            long longLotId = Long.parseLong(lotId);
            numberValidator.validateNumberIsPositive(longLotId);
            if (userRole == null) {
                throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
            }
            Optional<LotWithImages> optionalLot = findLotWithImages(longLotId);
            LotWithImages lot = optionalLot.orElseThrow(() -> new ClientErrorException(ClientError.NOT_FOUND));
            if (lot.getAuctionStatus().equals(AuctionStatus.APPROVED_BY_ADMIN)
                    || userRole.equals(Role.ADMIN)
                    || lot.getOwnerId() == userId) {
                return lot;
            }
            if (userRole.equals(Role.NOT_AUTHORIZED)) {
                throw new ClientErrorException(ClientError.FORBIDDEN);
            }
            AuctionParticipationService participationService = serviceFactory.auctionParticipationService();
            if (!participationService.isUserParticipateInLotAuction(userId, longLotId)) {
                throw new ClientErrorException(ClientError.FORBIDDEN);
            }
            return lot;
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
    public Optional<Lot> findLot(long id) throws ServiceCanNotCompleteCommandRequest {
        try {
            numberValidator.validateNumberIsPositive(id);
            return lotDao.find(id);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public List<LotWithImages> findLotsByPage(long pageNumber) throws ServiceCanNotCompleteCommandRequest {
        try {
            numberValidator.validateNumberIsPositive(pageNumber);
            List<Lot> lots = lotDao.find((pageNumber - 1) * LOTS_PER_PAGE, LOTS_PER_PAGE);
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
            validateParams(userId, mainImagePath, otherImagePaths, title, category, auctionType, condition,
                    description, initPrice, auctionStart, duration, region, cityOrDistrict);
            Timestamp startDatetime = Timestamp.valueOf(reformatForTimestamp(auctionStart));
            validateAuctionStartDate(startDatetime, MAX_BIAS_IN_MINUTES);
            int intDuration = Integer.parseInt(duration);
            validateDuration(intDuration);
            startDatetime = roundTimeToHours(startDatetime);
            Timestamp endDatetime =
                    new Timestamp(startDatetime.getTime() + (long) Integer.parseInt(duration) * ONE_HOUR_IN_MILLIS);
            BigDecimal bigDecimalInitPrice = bigDecimalNormalizer.normalize(new BigDecimal(initPrice));
            Lot lot = Lot.builder().setOwnerId(userId).setCategory(category).
                    setAuctionType(AuctionType.valueOf(auctionType)).setTitle(title).setStartDatetime(startDatetime)
                    .setEndDatetime(endDatetime).setInitialPrice(bigDecimalInitPrice).setRegion(region)
                    .setCityOrDistrict(cityOrDistrict).setDescription(description)
                    .setAuctionStatus(AuctionStatus.NOT_VERIFIED)
                    .setProductCondition(ProductCondition.valueOf(condition)).build();
            Optional<Lot> optionalLot = lotDao.save(lot);
            if (optionalLot.isPresent()) {
                Lot savedLot = optionalLot.get();
                List<String> imagePaths = otherImagePaths == null ? Collections.emptyList()
                        : Arrays.asList(otherImagePaths);
                Images images = new Images(savedLot.getLotId(), mainImagePath, imagePaths);
                imagesDao.save(images);
            } else {
                LOG.error(LOT_DID_NOT_SAVED_ERROR_MESSAGE, lot);
                throw new ServiceCanNotCompleteCommandRequest(LOT_DID_NOT_SAVED_ERROR_MESSAGE);
            }
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
    public void validateAuctionStartDate(String auctionStart) throws ClientErrorException {
        Timestamp startDatetime = Timestamp.valueOf(reformatForTimestamp(auctionStart));
        validateAuctionStartDate(startDatetime, ZERO_BIAS);
    }

    @Override
    public List<LotWithImages> findLotsByPageAndContext(long pageNumber, LotContext context)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            numberValidator.validateNumberIsPositive(pageNumber);
            List<Lot> lots = lotDao.findByLotContext(context,
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
            long lotsCount = lotDao.findLotsCount(context);
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
            AuctionStatus newAuctionStatus = AuctionStatus.valueOf(newStatus);
            int[] ids = new int[lotIds.length];
            for (int i = 0; i < lotIds.length; i++) {
                ids[i] = Integer.parseInt(lotIds[i]);
            }
            for (int lotId : ids) {
                Optional<Lot> optionalLot = lotDao.find(lotId);
                if (optionalLot.filter(lot -> !lot.getAuctionStatus().equals(newAuctionStatus)
                        && !lot.getAuctionStatus().equals(AuctionStatus.ENDED)).isPresent()) {
                    lotDao.update(Lot.builder().setLot(optionalLot.get())
                            .setAuctionStatus(newAuctionStatus).build());
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
            long lotsCount = lotDao.count();
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
            lotDao.executeLotsRenewAndCreateEventSchedule(Timestamp.valueOf(scheduleStart));
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void stopLotsAuctionStatusAutoUpdate() throws ServiceCanNotCompleteCommandRequest {
        try {
            lotDao.dropLotsRenewEventSchedule();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    private List<LotWithImages> findLotsWithImages(List<Lot> lots)
            throws java.sql.SQLException, InterruptedException {
        List<LotWithImages> lotsWithImages = new LinkedList<>();
        for (Lot lot : lots) {
            lotsWithImages.add(new LotWithImages(lot, imagesDao.find(lot.getLotId())
                    .orElse(new Images(lot.getLotId(), new Images.Image("")))));
        }
        LOG.debug("LotsWithImages: {}", lotsWithImages);
        return lotsWithImages;
    }

    private Timestamp roundTimeToHours(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().truncatedTo(ChronoUnit.HOURS));
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

    private void validateParams(long userId, String mainImagePath, String[] otherImagePaths, String title,
                                String category, String auctionType, String condition, String description,
                                String initPrice, String auctionStart, String duration, String region,
                                String cityOrDistrict) throws ClientErrorException {
        numberValidator.validateNumberIsPositive(userId);
        stringValidator.validateNotEmpty(mainImagePath, title, category, auctionType, condition,
                description, initPrice, auctionStart, duration, region, cityOrDistrict);
        if (otherImagePaths != null) {
            for (String p : otherImagePaths) {
                if (p.length() > MAX_IMAGE_PATH_SIZE) {
                    throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
                }
            }
        }
        if (mainImagePath.length() > MAX_IMAGE_PATH_SIZE || title.length() > MAX_TITLE_SIZE
                || category.length() > MAX_CATEGORY_SIZE
                || integerDigits(new BigDecimal(initPrice)) > MAX_INIT_PRICE_LENGTH
                || region.length() > MAX_REGION_SIZE || cityOrDistrict.length() > MAX_CITY_OR_DISTRICT_SIZE) {
            throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
        }
    }

    private int integerDigits(BigDecimal n) {
        n = n.stripTrailingZeros();
        return n.precision() - n.scale();
    }
}