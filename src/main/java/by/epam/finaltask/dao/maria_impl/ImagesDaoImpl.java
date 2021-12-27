package by.epam.finaltask.dao.maria_impl;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.dao.ImagesDao;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Images;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.epam.finaltask.model.Images.Image;

public class ImagesDaoImpl implements ImagesDao {

    private final static Logger LOG = LoggerFactory.getLogger(ImagesDaoImpl.class);

    private final static String ROWS_COUNT_COLUMN = "rows_count";
    private final static String IMAGE_ID_COLUMN = "image_id";
    private final static String IMAGE_PATH_COLUMN = "image_path";
    private final static String MAIN_IMAGE_COLUMN = "main_image";
    private final static String LOT_ID_COLUMN = "lot_id";
    private final static String LAST_INSERT_ID_COLUMN = "last_id";
    private final static String SELECT_LAST_INSERT_ID_QUERY = "SELECT LAST_INSERT_ID() AS last_id;";
    private final static String SAVE_IMAGE =
            "INSERT INTO lot_image(lot_id, image_path, main_image) VALUES(?, ?, ?)" +
                    "ON DUPLICATE KEY UPDATE lot_id=VALUES(lot_id), main_image=VALUES(main_image);";
    private final static String FIND_IMAGE_QUERY =
            "SELECT lot_id AS lot_id, image_path AS image_path, main_image AS main_image FROM lot_image" +
                    " WHERE image_id=?;";
    private final static String FIND_ALL_LOT_IMAGES_QUERY =
            "SELECT image_id AS image_id, image_path AS image_path, main_image AS main_image FROM lot_image" +
                    " WHERE lot_id=?;";
    private final static String UPDATE_IMAGE_QUERY =
            "UPDATE lot_image SET image_path = ?" +
                    " WHERE image_id = ? AND lot_id = ?";
    private final static String DELETE_IMAGE_QUERY =
            "DELETE FROM lot_image WHERE image_id = ? AND lot_id = ?;";
    private final static String DELETE_ALL_LOT_IMAGES_QUERY =
            "DELETE FROM lot_image WHERE lot_id = ?;";
    private final static String COUNT_QUERY = "SELECT COUNT(1) AS rows_count FROM lot_image";
    private final static String SAVE_IMAGE_PATH_QUERY = "INSERT INTO lot_image(image_path) VALUES(?);";

    private static volatile ImagesDaoImpl instance;
    protected final ConnectionPool connectionPool;

    private ImagesDaoImpl() throws DataSourceDownException {
        connectionPool = ConnectionPool.getInstance();
    }

    public static ImagesDaoImpl getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (ImagesDaoImpl.class) {
                if (instance == null) {
                    instance = new ImagesDaoImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void saveImagePath(String path) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_IMAGE_PATH_QUERY);
            statement.setString(1, path);
            statement.execute();
        } catch (SQLException | DataSourceDownException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    void prepareSaveStatement(PreparedStatement statement, long lotId, String imagePath, boolean mainImage)
            throws SQLException {
        statement.setLong(1, lotId);
        statement.setString(2, imagePath);
        statement.setBoolean(3, mainImage);
    }

    @Override
    public Optional<Images> save(Images images) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(SAVE_IMAGE);
            PreparedStatement findLastInsertIdStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID_QUERY);
            final long lotId = images.getLotId();

            prepareSaveStatement(insertStatement, lotId, images.getMainImage().getPath(), true);
            insertStatement.execute();
            ResultSet resultSet = findLastInsertIdStatement.executeQuery();
            if (resultSet.next()) {
                Image savedMainImage = images.getMainImage().createWithId(resultSet.getLong(LAST_INSERT_ID_COLUMN));

                List<Image> otherSavedImages = new ArrayList<>();
                for (Image image : images.getOtherImages()) {
                    prepareSaveStatement(insertStatement, lotId, image.getPath(), false);
                    insertStatement.execute();
                    resultSet = findLastInsertIdStatement.executeQuery();
                    if (resultSet.next()) {
                        otherSavedImages.add(image.createWithId(resultSet.getLong(LAST_INSERT_ID_COLUMN)));
                    }
                }
                return Optional.of(new Images(lotId, savedMainImage, otherSavedImages));
            } else {
                return Optional.empty();
            }
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
    public Optional<Images> find(long id) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_LOT_IMAGES_QUERY);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Image mainImage = null;
            List<Image> otherImages = new ArrayList<>();
            Image newImage;
            while (resultSet.next()) {
                newImage = new Image(resultSet.getLong(IMAGE_ID_COLUMN), resultSet.getString(IMAGE_PATH_COLUMN));
                if (resultSet.getBoolean(MAIN_IMAGE_COLUMN)) {
                    mainImage = newImage;
                } else {
                    otherImages.add(newImage);
                }
            }
            if (mainImage != null) {
                return Optional.of(new Images(id, mainImage, otherImages));
            } else {
                return Optional.empty();
            }
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
    public void update(Images images) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_IMAGE_QUERY);
            List<Image> allImages = images.getOtherImages();
            allImages.add(images.getMainImage());
            statement.setLong(3, images.getLotId());
            for (Image image : allImages) {
                statement.setString(1, image.getPath());
                statement.setLong(2, image.getId());
                statement.execute();
            }
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
            PreparedStatement statement = connection.prepareStatement(DELETE_ALL_LOT_IMAGES_QUERY);
            statement.setLong(1, id);
            statement.execute();
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
            ResultSet resultSet = statement.executeQuery(COUNT_QUERY);
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
