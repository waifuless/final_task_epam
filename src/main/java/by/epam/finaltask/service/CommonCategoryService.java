package by.epam.finaltask.service;

import by.epam.finaltask.dao.CategoryManager;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CommonCategoryService implements CategoryService {

    private final static Logger LOG = LogManager.getLogger(CommonCategoryService.class);

    private final CategoryManager categoryManager;

    CommonCategoryService(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    @Override
    public List<Category> findAllCategories() throws ServiceCanNotCompleteCommandRequest {
        try {
            return categoryManager.findAll();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }
}
