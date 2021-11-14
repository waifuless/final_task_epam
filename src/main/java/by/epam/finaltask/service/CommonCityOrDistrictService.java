package by.epam.finaltask.service;

import by.epam.finaltask.dao.CityOrDistrictManager;
import by.epam.finaltask.dao.UserManager;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.CityOrDistrict;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CommonCityOrDistrictService implements CityOrDistrictService{

    private final static Logger LOG = LogManager.getLogger(CommonCityOrDistrictService.class);

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
