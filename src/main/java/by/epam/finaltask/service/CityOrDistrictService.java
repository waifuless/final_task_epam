package by.epam.finaltask.service;

import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.CityOrDistrict;

import java.util.List;

public interface CityOrDistrictService {

    List<CityOrDistrict> findCityOrDistrictsByRegion(String regionName) throws ServiceCanNotCompleteCommandRequest;

}
