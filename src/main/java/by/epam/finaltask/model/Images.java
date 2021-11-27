package by.epam.finaltask.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Images implements DaoEntity<Images> {

    private final long lotId;
    private final Image mainImage;
    private final List<Image> otherImages;

    public Images(long lotId, Image mainImage, List<Image> otherImages) {
        this.lotId = lotId;
        this.mainImage = mainImage;
        this.otherImages = otherImages;
    }

    public Images(long lotId, Image mainImage) {
        this.lotId = lotId;
        this.mainImage = mainImage;
        this.otherImages = Collections.emptyList();
    }

    public Images(long lotId, String mainImagePath, List<String> otherImagesPaths) {
        this.lotId = lotId;
        this.mainImage = new Image(mainImagePath);
        this.otherImages = new ArrayList<>();
        for (String otherImagePath : otherImagesPaths) {
            this.otherImages.add(new Image(otherImagePath));
        }
    }

    public long getLotId() {
        return lotId;
    }

    public Image getMainImage() {
        return mainImage;
    }

    public List<Image> getOtherImages() {
        return new ArrayList<>(otherImages);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Images images1 = (Images) o;

        if (lotId != images1.lotId) return false;
        if (mainImage != null ? !mainImage.equals(images1.mainImage) : images1.mainImage != null) return false;
        return otherImages != null ? otherImages.equals(images1.otherImages) : images1.otherImages == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (lotId ^ (lotId >>> 32));
        result = 31 * result + (mainImage != null ? mainImage.hashCode() : 0);
        result = 31 * result + (otherImages != null ? otherImages.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Images{" +
                "lotId=" + lotId +
                ", mainImage=" + mainImage +
                ", otherImages=" + otherImages +
                '}';
    }

    @Override
    public Images createWithId(long id) {
        return new Images(id, mainImage, otherImages);
    }

    public static class Image implements DaoEntity<Image> {

        private final long id;
        private final String path;

        public Image(String path) {
            this.id = -1;
            this.path = path;
        }

        public Image(long id, String path) {
            this.id = id;
            this.path = path;
        }

        public long getId() {
            return id;
        }

        public String getPath() {
            return path;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            if (id != image.id) return false;
            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            int result = (int) (id ^ (id >>> 32));
            result = 31 * result + (path != null ? path.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Image{" +
                    "id=" + id +
                    ", path='" + path + '\'' +
                    '}';
        }

        @Override
        public Image createWithId(long id) {
            return new Image(id, path);
        }
    }
}
