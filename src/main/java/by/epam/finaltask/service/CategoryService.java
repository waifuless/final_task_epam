package by.epam.finaltask.service;

import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAllCategories() throws ServiceCanNotCompleteCommandRequest;

}
