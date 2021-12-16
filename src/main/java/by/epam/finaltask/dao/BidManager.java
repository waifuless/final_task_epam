package by.epam.finaltask.dao;

import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Bid;

public interface BidManager extends Dao<Bid>{

    static BidManager getInstance() throws DataSourceDownException {
        return MariaBidManager.getInstance();
    }
}
