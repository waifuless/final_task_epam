package by.epam.finaltask.dao;

import by.epam.finaltask.dao.maria_impl.UserDaoImpl;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.model.User;
import by.epam.finaltask.model.UserContext;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao<User> {

    static UserDao getInstance() {
        return UserDaoImpl.getInstance();
    }

    boolean isUserExists(String email) throws DaoException;

    Optional<User> findUserByEmailAndPassword(String email, String password)
            throws DaoException;

    List<User> findByUserContext(UserContext context, long offset, long count)
            throws DaoException;

    long findUsersCount(UserContext context) throws DaoException;
}
