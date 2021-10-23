package by.epam.finaltask.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Images implements DaoEntity<Images>{

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

    public static class Image implements DaoEntity<Image>{

        private final long id;
        private final Blob value;

        public Image(Blob value) {
            this.id = -1;
            this.value = value;
        }

        public Image(long id, Blob value) {
            this.id = id;
            this.value = value;
        }

        public long getId() {
            return id;
        }

        public Blob getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            if (id != image.id) return false;
            return value != null ? value.equals(image.value) : image.value == null;
        }

        @Override
        public int hashCode() {
            int result = (int) (id ^ (id >>> 32));
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Image{" +
                    "id=" + id +
                    ", value=" + value +
                    '}';
        }

        @Override
        public Image createWithId(long id) {
            return new Image(id, value);
        }
    }
}
