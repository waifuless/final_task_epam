package by.epam.finaltask.service;

import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<RegisterError> register(String email, String password, String passwordRepeat)
            throws ServiceCanNotCompleteCommandRequest;

    Optional<User> authenticate(String email, String password) throws ServiceCanNotCompleteCommandRequest;
}
