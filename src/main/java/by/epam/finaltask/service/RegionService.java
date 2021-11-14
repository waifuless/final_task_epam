package by.epam.finaltask.service;

import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Region;

import java.util.List;

public interface RegionService {

    List<Region> findAllRegions() throws ServiceCanNotCompleteCommandRequest;

}
