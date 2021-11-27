package by.epam.finaltask.service;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Category;

import java.util.List;

public interface CategoryService {

    void saveCategory(String categoryName) throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    void updateCategory(String idParam, String newCategoryName) throws ServiceCanNotCompleteCommandRequest,
            ClientErrorException;

    void deleteCategories(String[] categoryIds) throws ServiceCanNotCompleteCommandRequest, ClientErrorException;

    List<Category> findAllCategories() throws ServiceCanNotCompleteCommandRequest;

}
