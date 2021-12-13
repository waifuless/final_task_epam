package by.epam.finaltask.service;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.User;
import by.epam.finaltask.model.UserContext;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<RegisterError> register(String email, String password, String passwordRepeat)
            throws ServiceCanNotCompleteCommandRequest;

    Optional<User> authenticate(String email, String password) throws ServiceCanNotCompleteCommandRequest;

    Optional<User> findUserById(long id) throws ServiceCanNotCompleteCommandRequest;

    List<User> findUsersByPageAndContext(long pageNumber, UserContext context)
            throws ServiceCanNotCompleteCommandRequest;

    long findUsersPagesCount(UserContext context) throws ServiceCanNotCompleteCommandRequest;

    void changeUserBannedStatus(long id, String action) throws ServiceCanNotCompleteCommandRequest, ClientErrorException;
}
