package by.epam.finaltask.service;

import by.epam.finaltask.dao.RegionManager;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Region;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CommonRegionService implements RegionService {

    private final static Logger LOG = LogManager.getLogger(CommonRegionService.class);

    private final RegionManager regionManager;

    CommonRegionService(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @Override
    public List<Region> findAllRegions() throws ServiceCanNotCompleteCommandRequest {
        try {
            return regionManager.findAll();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }
}
