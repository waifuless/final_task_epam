package by.epam.finaltask.dao.maria_impl;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.dao.AuctionParticipationDao;
import by.epam.finaltask.dao.StatementPreparator;
import by.epam.finaltask.exception.DaoException;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.exception.ExtractionException;
import by.epam.finaltask.model.AuctionParticipation;
import by.epam.finaltask.model.AuctionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuctionParticipationDaoImpl implements AuctionParticipationDao {

    private final static Logger LOG = LoggerFactory.getLogger(AuctionParticipationDaoImpl.class);

    private final static String ROWS_COUNT_COLUMN = "rows_count";
    private final static String PARTICIPATION_EXISTENCE_COLUMN = "participation_existence";
    private final static String PARTICIPANT_ID_COLUMN = "participant_id";
    private final static String LOT_ID_COLUMN = "lot_id";
    private final static String DEPOSIT_COLUMN = "deposit";
    private final static String DEPOSIT_IS_TAKEN_BY_OWNER_COLUMN = "deposit_is_taken_by_owner";

    private final static String SAVE_PARTICIPATION_QUERY = "INSERT INTO auction_participation(participant_id, lot_id," +
            " deposit, deposit_is_taken_by_owner)" +
            " VALUES (?,?,?,?)";
    private final static String UPDATE_PARTICIPATION_QUERY = "UPDATE auction_participation " +
            " SET deposit = ?, deposit_is_taken_by_owner = ?" +
            " WHERE participant_id = ? AND lot_id = ?";
    private final static String IS_USER_PARTICIPATE_QUERY = "SELECT EXISTS(SELECT 1 FROM auction_participation" +
            " WHERE participant_id=? AND lot_id=?) as participation_existence";
    private final static String FIND_ALL_USER_PARTICIPATES_QUERY = "SELECT participant_id AS participant_id," +
            " auction_participation.lot_id AS lot_id, deposit AS deposit, deposit_is_taken_by_owner AS deposit_is_taken_by_owner" +
            " FROM auction_participation";
    private final static String FIND_USER_PARTICIPATE_QUERY = FIND_ALL_USER_PARTICIPATES_QUERY +
            " WHERE participant_id=? AND lot_id=?";
    private final static String DELETE_USER_PARTICIPATE_QUERY = "DELETE FROM auction_participation" +
            " WHERE participant_id=? AND lot_id=?";
    private final static String FIND_USERS_PARTICIPATIONS_QUERY = FIND_ALL_USER_PARTICIPATES_QUERY +
            " WHERE participant_id=? LIMIT ? OFFSET ?";
    private final static String FIND_USERS_PARTICIPATIONS_BY_AUCTION_STATUS_QUERY = FIND_ALL_USER_PARTICIPATES_QUERY +
            " JOIN lot ON auction_participation.lot_id = lot.lot_id " +
            " WHERE participant_id=? AND lot.auction_status_id = findAuctionStatusId(?) LIMIT ? OFFSET ?";
    private final static String FIND_COUNT_BY_USER_ID_QUERY = "SELECT COUNT(1) AS rows_count " +
            " FROM auction_participation " +
            " WHERE participant_id=?";
    private final static String FIND_COUNT_BY_USER_ID_AND_AUCTION_STATUS_QUERY = " SELECT COUNT(1) AS rows_count" +
            " FROM auction_participation JOIN lot ON auction_participation.lot_id = lot.lot_id" +
            " WHERE participant_id=? AND lot.auction_status_id = findAuctionStatusId(?)";

    private static volatile AuctionParticipationDaoImpl instance;

    protected final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private AuctionParticipationDaoImpl() throws DataSourceDownException {
    }

    public static AuctionParticipationDaoImpl getInstance() {
        if (instance == null) {
            synchronized (AuctionParticipationDaoImpl.class) {
                if (instance == null) {
                    instance = new AuctionParticipationDaoImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void saveParticipation(AuctionParticipation participation)
            throws DaoException {
        postParticipation(SAVE_PARTICIPATION_QUERY, (statement -> {
            statement.setLong(1, participation.getParticipantId());
            statement.setLong(2, participation.getLotId());
            statement.setBigDecimal(3, participation.getDeposit());
            statement.setBoolean(4, participation.isDepositIsTakenByOwner());
        }));
    }

    @Override
    public void updateParticipation(AuctionParticipation participation)
            throws DaoException {
        postParticipation(UPDATE_PARTICIPATION_QUERY, (statement -> {
            statement.setBigDecimal(1, participation.getDeposit());
            statement.setBoolean(2, participation.isDepositIsTakenByOwner());
            statement.setLong(3, participation.getParticipantId());
            statement.setLong(4, participation.getLotId());
        }));
    }

    @Override
    public boolean isUserParticipateInLotAuction(long userId, long lotId)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(IS_USER_PARTICIPATE_QUERY);
            statement.setLong(1, userId);
            statement.setLong(2, lotId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(PARTICIPATION_EXISTENCE_COLUMN);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<AuctionParticipation> findParticipation(long userId, long lotId)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_USER_PARTICIPATE_QUERY);
            statement.setLong(1, userId);
            statement.setLong(2, lotId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    @Override
    public void deleteParticipation(long userId, long lotId)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_USER_PARTICIPATE_QUERY);
            statement.setLong(1, userId);
            statement.setLong(2, lotId);
            statement.execute();
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    @Override
    public List<AuctionParticipation> findUsersParticipations(long userId, long offset, long count)
            throws DaoException {
        return findAuctionParticipations(FIND_USERS_PARTICIPATIONS_QUERY, (statement) -> {
            statement.setLong(1, userId);
            statement.setLong(2, count);
            statement.setLong(3, offset);
        });
    }

    @Override
    public List<AuctionParticipation> findUsersParticipations(long userId, long offset, long count,
                                                              AuctionStatus auctionStatus)
            throws DaoException {
        return findAuctionParticipations(FIND_USERS_PARTICIPATIONS_BY_AUCTION_STATUS_QUERY, (statement) -> {
            statement.setLong(1, userId);
            statement.setString(2, auctionStatus.name());
            statement.setLong(3, count);
            statement.setLong(4, offset);
        });
    }

    @Override
    public long findUsersParticipationsCount(long userId)
            throws DaoException {
        return findCount(FIND_COUNT_BY_USER_ID_QUERY, (statement) -> statement.setLong(1, userId));
    }

    @Override
    public long findUsersParticipationsCount(long userId, AuctionStatus auctionStatus)
            throws DaoException {
        return findCount(FIND_COUNT_BY_USER_ID_AND_AUCTION_STATUS_QUERY, (statement) -> {
            statement.setLong(1, userId);
            statement.setString(2, auctionStatus.name());
        });
    }

    protected AuctionParticipation extractEntity(ResultSet resultSet) throws ExtractionException {
        try {
            return new AuctionParticipation(resultSet.getLong(PARTICIPANT_ID_COLUMN), resultSet.getLong(LOT_ID_COLUMN),
                    resultSet.getBigDecimal(DEPOSIT_COLUMN), resultSet.getBoolean(DEPOSIT_IS_TAKEN_BY_OWNER_COLUMN));
        } catch (Exception ex) {
            throw new ExtractionException(ex.getMessage(), ex);
        }
    }

    protected List<AuctionParticipation> extractAll(ResultSet resultSet) throws ExtractionException {
        List<AuctionParticipation> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                list.add(extractEntity(resultSet));
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ExtractionException(ex.getMessage(), ex);
        }
        return list;
    }

    private long findCount(String query, StatementPreparator preparator)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            preparator.accept(statement);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getLong(ROWS_COUNT_COLUMN);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    private List<AuctionParticipation> findAuctionParticipations(String query, StatementPreparator preparator)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            preparator.accept(statement);
            ResultSet resultSet = statement.executeQuery();
            return extractAll(resultSet);
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }

    private void postParticipation(String query, StatementPreparator preparator)
            throws DaoException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            preparator.accept(statement);
            statement.execute();
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new DaoException(e);
        }
    }
}
