package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.User;
import by.epam.finaltask.model.UserContext;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserManager extends Dao<User> {

    static UserManager getInstance() throws DataSourceDownException {
        return MariaUserManager.getInstance();
    }

    boolean isUserExists(String email) throws SQLException, DataSourceDownException, InterruptedException;

    Optional<User> findUserByEmailAndPassword(String email, String password)
            throws SQLException, DataSourceDownException, InterruptedException;

    List<User> findByUserContext(UserContext context, long offset, long count)
            throws SQLException, DataSourceDownException, InterruptedException;

    long findUsersCount(UserContext context) throws SQLException, DataSourceDownException, InterruptedException;
}
