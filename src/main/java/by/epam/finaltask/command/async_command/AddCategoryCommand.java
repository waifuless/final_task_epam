package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.CategoryService;
import by.epam.finaltask.service.ImagesService;
import by.epam.finaltask.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddCategoryCommand implements AjaxCommand {

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN));

    private final CategoryService categoryService = ServiceFactory.getFactoryInstance().categoryService();

    AddCategoryCommand() throws IOException {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        String categoryName = request.getParameter("category-name");
        categoryService.saveCategory(categoryName);
        return new AjaxCommandResponse("text", "Category successfully added");
    }
}
