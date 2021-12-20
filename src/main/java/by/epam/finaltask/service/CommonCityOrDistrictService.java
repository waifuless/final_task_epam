package by.epam.finaltask.service;

import by.epam.finaltask.dao.CityOrDistrictManager;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.CityOrDistrict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommonCityOrDistrictService implements CityOrDistrictService {

    private final static Logger LOG = LoggerFactory.getLogger(CommonCityOrDistrictService.class);

    private final CityOrDistrictManager cityOrDistrictManager;

    CommonCityOrDistrictService(CityOrDistrictManager cityOrDistrictManager) {
        this.cityOrDistrictManager = cityOrDistrictManager;
    }

    @Override
    public List<CityOrDistrict> findCityOrDistrictsByRegion(String regionName)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            return cityOrDistrictManager.findByRegion(regionName);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }
}
