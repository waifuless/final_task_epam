package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.DaoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDao<T extends DaoEntity> implements Dao<T> {

    private final static Logger LOG = LoggerFactory.getLogger(GenericDao.class);

    private final static String ROWS_COUNT_COLUMN = "rows_count";

    protected final ConnectionPool connectionPool;
    private final String saveQuery;
    private final String findQuery;
    private final String updateQuery;
    private final String deleteQuery;
    private String countQuery = "SELECT COUNT(1) AS rows_count FROM %s";

    protected GenericDao(String saveQuery, String findQuery, String updateQuery, String deleteQuery, String tableName,
                         ConnectionPool connectionPool) {
        this.saveQuery = saveQuery;
        this.findQuery = findQuery;
        this.updateQuery = updateQuery;
        this.deleteQuery = deleteQuery;
        this.countQuery = String.format(this.countQuery, tableName);
        this.connectionPool = connectionPool;
    }

    protected abstract void prepareSaveStatement(PreparedStatement statement, T t) throws SQLException;

    protected void prepareFindStatement(PreparedStatement statement, long id) throws SQLException{
        statement.setLong(1, id);
    }

    protected abstract void prepareUpdateStatement(PreparedStatement statement, T t) throws SQLException;

    protected void prepareDeleteStatement(PreparedStatement statement, long id) throws SQLException{
        statement.setLong(1, id);
    }

    protected abstract T extractEntity(ResultSet resultSet) throws ExtractionException;

    protected abstract long extractId(ResultSet resultSet) throws ExtractionException;

    protected List<T> extractAll(ResultSet resultSet) throws ExtractionException{
        List<T> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                list.add(extractEntity(resultSet));
            }
        }catch (Exception ex){
            LOG.error(ex.getMessage(), ex);
            throw new ExtractionException(ex.getMessage(), ex);
        }
        return list;
    }

    @Override
    public long save(T t) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            prepareSaveStatement(statement, t);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return extractId(resultSet);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    @Override
    public T find(long id) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(findQuery);
            prepareFindStatement(statement, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return extractEntity(resultSet);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    @Override
    public void update(T t) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            prepareUpdateStatement(statement, t);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    @Override
    public void delete(long id) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            prepareDeleteStatement(statement, id);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    @Override
    public long count() throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countQuery);
            resultSet.next();
            return resultSet.getLong(ROWS_COUNT_COLUMN);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }
}
