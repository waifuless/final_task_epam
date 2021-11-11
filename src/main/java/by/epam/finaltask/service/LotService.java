package by.epam.finaltask.service;

import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.LotWithImages;

import java.util.List;
import java.util.Optional;

public interface LotService {

    Optional<LotWithImages> findLot(int id) throws ServiceCanNotCompleteCommandRequest;

    List<LotWithImages> findLotsByPage(int pageNumber) throws ServiceCanNotCompleteCommandRequest;
}
