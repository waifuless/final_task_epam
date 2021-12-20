package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.CategoryService;
import by.epam.finaltask.service.ServiceFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeleteCategoriesCommand implements AjaxCommand {

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN));

    private final CategoryService categoryService = ServiceFactory.getFactoryInstance().categoryService();

    DeleteCategoriesCommand() {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        String[] categoryIds = request.getParameterValues("ids[]");
        categoryService.deleteCategories(categoryIds);
        return new AjaxCommandResponse("text", "Categories successfully removed");
    }
}
