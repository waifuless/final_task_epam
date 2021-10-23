package by.epam.finaltask.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LotWithImages extends Lot{

    private Images images;

    public LotWithImages(long ownerId, String category, AuctionType auctionType, String title,
                         Timestamp startDatetime, Timestamp endDatetime, BigDecimal initialPrice, String originPlace,
                         String description, AuctionStatus auctionStatus, ProductCondition productCondition,
                         Images images) {
        super(ownerId, category, auctionType, title, startDatetime, endDatetime, initialPrice, originPlace,
                description, auctionStatus, productCondition);
        this.images = images;
    }

    public LotWithImages(long lotId, long ownerId, String category, AuctionType auctionType, String title,
                         Timestamp startDatetime, Timestamp endDatetime, BigDecimal initialPrice, String originPlace,
                         String description, AuctionStatus auctionStatus, ProductCondition productCondition,
                         Images images) {
        super(lotId, ownerId, category, auctionType, title, startDatetime, endDatetime, initialPrice, originPlace,
                description, auctionStatus, productCondition);
        this.images = images;
    }

    public Images getImages() {
        return images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        LotWithImages that = (LotWithImages) o;

        return images != null ? images.equals(that.images) : that.images == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (images != null ? images.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LotWithImages{" +
                super.toString()+
                "images=" + images +
                '}';
    }

    @Override
    public LotWithImages createWithId(long id) {
        return new LotWithImages(id, getOwnerId(), getCategory(), getAuctionType(), getTitle(), getStartDatetime(),
                getEndDatetime(), getInitialPrice(), getOriginPlace(), getDescription(), getAuctionStatus(),
                getProductCondition(), images);
    }
}
