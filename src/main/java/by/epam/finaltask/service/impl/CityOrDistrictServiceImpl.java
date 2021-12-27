package by.epam.finaltask.service.impl;

import by.epam.finaltask.dao.CityOrDistrictDao;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.CityOrDistrict;
import by.epam.finaltask.service.CityOrDistrictService;
import by.epam.finaltask.validation.StringClientParameterValidator;
import by.epam.finaltask.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CityOrDistrictServiceImpl implements CityOrDistrictService {

    private final static Logger LOG = LoggerFactory.getLogger(CityOrDistrictServiceImpl.class);
    private final static int MAX_CITY_OR_DISTRICT_SIZE = 60;

    private final StringClientParameterValidator stringValidator = ValidatorFactory.getFactoryInstance()
            .stringParameterValidator();

    private final CityOrDistrictDao cityOrDistrictDao;

    CityOrDistrictServiceImpl(CityOrDistrictDao cityOrDistrictDao) {
        this.cityOrDistrictDao = cityOrDistrictDao;
    }

    @Override
    public List<CityOrDistrict> findCityOrDistrictsByRegion(String regionName)
            throws ServiceCanNotCompleteCommandRequest {
        try {
            stringValidator.validateNotEmpty(regionName);
            if (regionName.length() > MAX_CITY_OR_DISTRICT_SIZE) {
                throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
            }
            return cityOrDistrictDao.findByRegion(regionName);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }
}
