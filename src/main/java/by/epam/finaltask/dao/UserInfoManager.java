package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.User;
import by.epam.finaltask.model.UserInfo;

import java.sql.SQLException;

public interface UserInfoManager {

    static UserInfoManager getInstance() throws DataSourceDownException {
        return MariaUserInfoManager.getInstance();
    }

    void save(UserInfo userInfo) throws SQLException, DataSourceDownException, InterruptedException;

    boolean isUserInfoExist(int userId) throws SQLException, DataSourceDownException, InterruptedException;

    UserInfo findUserInfo(int userId) throws SQLException, DataSourceDownException, InterruptedException;

    void updateUserInfo(int userId, UserInfo newUserInfo) throws SQLException, DataSourceDownException,
            InterruptedException;

    void deleteUserInfo(int userId) throws SQLException, DataSourceDownException, InterruptedException;
}
