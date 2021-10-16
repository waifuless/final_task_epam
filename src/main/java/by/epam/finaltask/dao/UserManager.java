package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.User;

import java.sql.SQLException;

public interface UserManager {

    static UserManager getInstance() throws DataSourceDownException {
        return MariaUserManager.getInstance();
    }

    void save(User user) throws SQLException, DataSourceDownException, InterruptedException;

    boolean isUserExist(String email) throws SQLException, DataSourceDownException, InterruptedException;

    User findUserByEmailAndPassword(String email, String password) throws SQLException, DataSourceDownException, InterruptedException;

    void deleteUser(int id) throws SQLException, DataSourceDownException, InterruptedException;
}
