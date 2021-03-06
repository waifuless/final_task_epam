package by.epam.finaltask.connection_pool.impl;

import by.epam.finaltask.connection_pool.DataSource;

import java.sql.*;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class PooledConnection implements Connection {

    private final Connection connection;
    private final Set<Statement> openedStatements;
    private final Set<Savepoint> savepoints;
    private final DataSource dataSource;

    PooledConnection(Connection connection, DataSource dataSource) {
        this.connection = connection;
        this.dataSource = dataSource;
        openedStatements = Collections.newSetFromMap(new ConcurrentHashMap<>());
        savepoints = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Override
    public Statement createStatement() throws SQLException {
        Statement statement = connection.createStatement();
        registerStatement(statement);
        return connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        registerStatement(statement);
        return statement;
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        CallableStatement statement = connection.prepareCall(sql);
        registerStatement(statement);
        return statement;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return connection.nativeSQL(sql);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    @Override
    public void close() throws SQLException {
        if (!getAutoCommit()) {
            rollback();
            setAutoCommit(true);
        }
        closeAndUnregisterAllSavepoints();
        closeAndUnregisterAllStatements();
        dataSource.takeBack(this);
    }

    @Override
    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        connection.setReadOnly(readOnly);
    }

    @Override
    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        connection.setCatalog(catalog);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        connection.setTransactionIsolation(level);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        connection.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency);
        registerStatement(statement);
        return statement;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        registerStatement(statement);
        return statement;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        CallableStatement statement = connection.prepareCall(sql, resultSetType, resultSetConcurrency);
        registerStatement(statement);
        return statement;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return connection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        connection.setTypeMap(map);
    }

    @Override
    public int getHoldability() throws SQLException {
        return connection.getHoldability();
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        connection.setHoldability(holdability);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        Savepoint savepoint = connection.setSavepoint();
        releaseSavepoint(savepoint);
        return savepoint;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        Savepoint savepoint = connection.setSavepoint(name);
        releaseSavepoint(savepoint);
        return savepoint;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        connection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        unregisterSavepoint(savepoint);
        connection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        registerStatement(statement);
        return statement;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        registerStatement(statement);
        return statement;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        CallableStatement statement = connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        registerStatement(statement);
        return statement;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql, autoGeneratedKeys);
        registerStatement(statement);
        return statement;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql, columnIndexes);
        registerStatement(statement);
        return statement;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql, columnNames);
        registerStatement(statement);
        return statement;
    }

    @Override
    public Clob createClob() throws SQLException {
        return connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return connection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return connection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        connection.setClientInfo(name, value);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return connection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return connection.getClientInfo();
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        connection.setClientInfo(properties);
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return connection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return connection.createStruct(typeName, attributes);
    }

    @Override
    public String getSchema() throws SQLException {
        return connection.getSchema();
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        connection.setSchema(schema);
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        connection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        connection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return connection.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return connection.unwrap(iface);
    }

    void realClose() throws SQLException {
        connection.close();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return connection.isWrapperFor(iface);
    }

    private void registerStatement(Statement statement) {
        openedStatements.add(statement);
    }

    private void registerSavepoint(Savepoint savepoint) {
        savepoints.add(savepoint);
    }

    private void unregisterSavepoint(Savepoint savepoint) throws SQLException {
        savepoints.remove(savepoint);
    }

    private void closeAndUnregisterAllStatements() throws SQLException {
        for (Statement openedStatement : openedStatements) {
            openedStatement.close();
        }
        openedStatements.clear();
    }

    private void closeAndUnregisterAllSavepoints() throws SQLException {
        for (Savepoint savepoint : savepoints) {
            releaseSavepoint(savepoint);
        }
        savepoints.clear();
    }
}
