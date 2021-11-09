package by.epam.finaltask.service;

import by.epam.finaltask.dao.ImagesManager;
import by.epam.finaltask.dao.LotManager;
import by.epam.finaltask.dao.UserManager;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Lot;
import by.epam.finaltask.model.LotWithImages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CommonLotService implements LotService{

    private final static Logger LOG = LogManager.getLogger(CommonLotService.class);

    private final static int LOTS_PER_PAGE = 9;

    private final LotManager lotManager;
    private final ImagesManager imagesManager;

    CommonLotService(LotManager lotManager, ImagesManager imagesManager) {
        this.lotManager = lotManager;
        this.imagesManager = imagesManager;
    }

    @Override
    public Optional<LotWithImages> findLot(int id) throws ServiceCanNotCompleteCommandRequest {
        try {
            Optional<Lot> optionalLot = lotManager.find(id);
            if(optionalLot.isPresent()){
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
    public List<LotWithImages> findLotsByPage(int pageNumber) throws ServiceCanNotCompleteCommandRequest {
        try {
            List<Lot> lots = lotManager.find((long) (pageNumber-1)*LOTS_PER_PAGE, LOTS_PER_PAGE);
            List<LotWithImages> lotsWithImages = new LinkedList<>();
            for (Lot lot : lots) {
                lotsWithImages.add(new LotWithImages(lot, imagesManager.find(lot.getLotId()).orElse(null)));
            }
            return lotsWithImages;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }
}
