package by.epam.finaltask.service;

import by.epam.finaltask.dao.CityOrDistrictManager;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.CityOrDistrict;
import by.epam.finaltask.validation.StringClientParameterValidator;
import by.epam.finaltask.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommonCityOrDistrictService implements CityOrDistrictService {

    private final static Logger LOG = LoggerFactory.getLogger(CommonCityOrDistrictService.class);
    private final static int MAX_CITY_OR_DISTRICT_SIZE = 60;

    private final StringClientParameterValidator stringValidator = ValidatorFactory.getFactoryInstance()
            .stringParameterValidator();

    private final CityOrDistrictManager cityOrDistrictManager;

    CommonCityOrDistrictService(CityOrDistrictManager cityOrDistrictManager) {
        this.cityOrDistrictManager = cityOrDistrictManager;
    }

    @Override
    public List<CityOrDistrict> findCityOrDistrictsByRegion(String regionName)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            stringValidator.validateNotEmpty(regionName);
            if (regionName.length() > MAX_CITY_OR_DISTRICT_SIZE) {
                throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
            }
            return cityOrDistrictManager.findByRegion(regionName);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }
}
