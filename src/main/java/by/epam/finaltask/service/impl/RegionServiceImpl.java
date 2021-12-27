package by.epam.finaltask.service.impl;

import by.epam.finaltask.dao.RegionDao;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Region;
import by.epam.finaltask.service.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RegionServiceImpl implements RegionService {

    private final static Logger LOG = LoggerFactory.getLogger(RegionServiceImpl.class);

    private final RegionDao regionDao;

    RegionServiceImpl(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    @Override
    public List<Region> findAllRegions() throws ServiceCanNotCompleteCommandRequest {
        try {
            return regionDao.findAll();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }
}
