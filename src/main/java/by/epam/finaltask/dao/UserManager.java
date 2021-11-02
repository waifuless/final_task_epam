package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserManager extends Dao<User> {

    static UserManager getInstance() throws DataSourceDownException {
        return MariaUserManager.getInstance();
    }

    boolean isUserExist(String email) throws SQLException, DataSourceDownException, InterruptedException;

    Optional<User> findUserByEmailAndPassword(String email, String password)
            throws SQLException, DataSourceDownException, InterruptedException;
}
