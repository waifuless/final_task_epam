package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommand;
import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.model.CityOrDistrict;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.CityOrDistrictService;
import by.epam.finaltask.service.ServiceFactory;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FindCitiesOrDistrictsByRegionCommand implements AjaxCommand {

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));

    private final CityOrDistrictService cityOrDistrictService = ServiceFactory
            .getFactoryInstance().cityOrDistrictService();

    FindCitiesOrDistrictsByRegionCommand() {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        String region = request.getParameter("region");
        if (region == null || region.trim().isEmpty()) {
            throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
        }
        List<CityOrDistrict> citiesOrDistricts = cityOrDistrictService.findCityOrDistrictsByRegion(region);
        String jsonCitiesOrDistricts = new Gson().toJson(citiesOrDistricts);
        return new AjaxCommandResponse("application/json", jsonCitiesOrDistricts);
    }
}
