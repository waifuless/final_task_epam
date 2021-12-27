package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.UserDaoImpl;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.User;
import by.epam.finaltask.model.UserContext;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao<User> {

    static UserDao getInstance() throws DataSourceDownException {
        return UserDaoImpl.getInstance();
    }

    boolean isUserExists(String email) throws SQLException, DataSourceDownException, InterruptedException;

    Optional<User> findUserByEmailAndPassword(String email, String password)
            throws SQLException, DataSourceDownException, InterruptedException;

    List<User> findByUserContext(UserContext context, long offset, long count)
            throws SQLException, DataSourceDownException, InterruptedException;

    long findUsersCount(UserContext context) throws SQLException, DataSourceDownException, InterruptedException;
}
