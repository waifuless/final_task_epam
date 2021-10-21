package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.UserInfo;

import java.sql.SQLException;

public interface UserInfoManager extends Dao<UserInfo> {

    static UserInfoManager getInstance() throws DataSourceDownException {
        return MariaUserInfoManager.getInstance();
    }

    boolean isUserInfoExist(long id) throws SQLException, DataSourceDownException, InterruptedException;
}
