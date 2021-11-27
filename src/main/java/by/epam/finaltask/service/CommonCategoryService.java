package by.epam.finaltask.service;

import by.epam.finaltask.dao.CategoryManager;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
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
    public void saveCategory(String categoryName) throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            if (categoryName == null || categoryName.trim().isEmpty()) {
                throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
            }
            if (categoryManager.isCategoryExists(categoryName)) {
                throw new ClientErrorException(ClientError.ENTITY_ALREADY_EXISTS);
            }
            categoryManager.save(new Category(-1, categoryName));
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void updateCategory(String idParam, String newCategoryName)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            long id = Integer.parseInt(idParam);
            if (newCategoryName == null || newCategoryName.trim().isEmpty()) {
                throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
            }
            if (categoryManager.isCategoryExists(newCategoryName)) {
                throw new ClientErrorException(ClientError.ENTITY_ALREADY_EXISTS);
            }
            categoryManager.update(new Category(id, newCategoryName));
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage());
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    @Override
    public void deleteCategories(String[] categoryIds)
            throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            if (categoryIds == null || categoryIds.length < 1) {
                throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
            }
            int[] ids = new int[categoryIds.length];
            for (int i = 0; i < categoryIds.length; i++) {
                ids[i] = Integer.parseInt(categoryIds[i]);
            }
            for (int categoryId : ids) {
                categoryManager.delete(categoryId);
            }
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage(), ex);
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
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
