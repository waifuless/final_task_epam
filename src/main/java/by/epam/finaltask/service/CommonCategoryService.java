package by.epam.finaltask.service;

import by.epam.finaltask.dao.CategoryManager;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Category;
import by.epam.finaltask.validation.NumberValidator;
import by.epam.finaltask.validation.StringClientParameterValidator;
import by.epam.finaltask.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommonCategoryService implements CategoryService {

    private final static Logger LOG = LoggerFactory.getLogger(CommonCategoryService.class);
    private final static int MAX_CATEGORY_SIZE = 64;

    private final StringClientParameterValidator stringValidator = ValidatorFactory.getFactoryInstance()
            .stringParameterValidator();
    private final NumberValidator numberValidator = ValidatorFactory.getFactoryInstance().idValidator();

    private final CategoryManager categoryManager;

    CommonCategoryService(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    @Override
    public void saveCategory(String categoryName) throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            if (categoryName.length() > MAX_CATEGORY_SIZE) {
                throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
            }
            stringValidator.validateNotEmpty(categoryName);
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
            if (newCategoryName.length() > MAX_CATEGORY_SIZE) {
                throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
            }
            long id = Integer.parseInt(idParam);
            numberValidator.validateNumberIsPositive(id);
            stringValidator.validateNotEmpty(newCategoryName);
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
                if (categoryId > 0) {
                    categoryManager.delete(categoryId);
                }
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
