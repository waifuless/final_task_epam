package by.epam.finaltask.dao;

import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import by.epam.finaltask.model.Images;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.epam.finaltask.model.Images.Image;

public class MariaImagesManager implements ImagesManager{

    private final static Logger LOG = LoggerFactory.getLogger(GenericDao.class);

    private final static String ROWS_COUNT_COLUMN = "rows_count";
    private final static String IMAGE_ID_COLUMN = "image_id";
    private final static String IMAGE_VALUE_COLUMN = "image_value";
    private final static String MAIN_IMAGE_COLUMN = "main_image";
    private final static String LOT_ID_COLUMN = "lot_id";
    private final static String SAVE_IMAGE =
            "INSERT INTO lot_image(lot_id, image_value, main_image) VALUES(?, ?, ?);" +
                    "SELECT LAST_INSERT_ID() AS image_id;";
    private final static String FIND_IMAGE_QUERY =
            "SELECT lot_id AS lot_id, image_value AS image_value, main_image AS main_image FROM lot_image" +
                    " WHERE image_id=?;";
    private final static String FIND_ALL_LOT_IMAGES_QUERY =
            "SELECT image_id AS image_id, image_value AS image_value, main_image AS main_image FROM lot_image" +
                    " WHERE lot_id=?;";
    private final static String UPDATE_IMAGE_QUERY =
            "UPDATE lot_image SET image_value = ?" +
                    " WHERE image_id = ? AND lot_id = ?";
    private final static String DELETE_IMAGE_QUERY =
            "DELETE FROM lot_image WHERE image_id = ? AND lot_id = ?;";
    private final static String DELETE_ALL_LOT_IMAGES_QUERY =
            "DELETE FROM lot_image WHERE lot_id = ?;";
    private final static String COUNT_QUERY = "SELECT COUNT(1) AS rows_count FROM lot_image";

    protected final ConnectionPool connectionPool;

    private static volatile MariaImagesManager instance;

    private MariaImagesManager() throws DataSourceDownException {
        connectionPool = ConnectionPool.getInstance();
    }

    public static MariaImagesManager getInstance() throws DataSourceDownException {
        if (instance == null) {
            synchronized (MariaImagesManager.class) {
                if (instance == null) {
                    instance = new MariaImagesManager();
                }
            }
        }
        return instance;
    }

    void prepareSaveStatement(PreparedStatement statement, long lotId, Blob imageValue, boolean mainImage)
            throws SQLException {
        statement.setLong(1, lotId);
        statement.setBlob(2, imageValue);
        statement.setBoolean(3, mainImage);
    }

    @Override
    public Images save(Images images) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_IMAGE);
            final long lotId = images.getLotId();

            prepareSaveStatement(statement, lotId, images.getMainImage().getValue(), true);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Image savedMainImage = images.getMainImage().createWithId(resultSet.getLong(IMAGE_ID_COLUMN));

            List<Image> otherSavedImages = new ArrayList<>();
            for (Image image : images.getOtherImages()) {
                prepareSaveStatement(statement, lotId, image.getValue(), false);
                resultSet = statement.executeQuery();
                resultSet.next();
                otherSavedImages.add(image.createWithId(resultSet.getLong(IMAGE_ID_COLUMN)));
            }

            return new Images(lotId, savedMainImage, otherSavedImages);
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
    public Images find(long id) throws SQLException, DataSourceDownException, InterruptedException {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_LOT_IMAGES_QUERY);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Image mainImage = null;
            List<Image> otherImages = new ArrayList<>();
            Image newImage;
            while(resultSet.next()){
                newImage = new Image(resultSet.getLong(IMAGE_ID_COLUMN), resultSet.getBlob(IMAGE_VALUE_COLUMN));
                if(resultSet.getBoolean(MAIN_IMAGE_COLUMN)){
                    mainImage = newImage;
                }else{
                    otherImages.add(newImage);
                }
            }
            return new Images(id, mainImage, otherImages);
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
                statement.setBlob(1, image.getValue());
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
