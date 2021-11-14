package by.epam.finaltask.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Lot implements DaoEntity<Lot> {

    private final long lotId;
    private final long ownerId;
    private final String category;
    private final AuctionType auctionType;
    private final String title;
    private final Timestamp startDatetime;
    private final Timestamp endDatetime;
    private final BigDecimal initialPrice;
    private final String region;
    private final String cityOrDistrict;
    private final String description;
    private final AuctionStatus auctionStatus;
    private final ProductCondition productCondition;

    public Lot(long ownerId, String category, AuctionType auctionType, String title, Timestamp startDatetime,
               Timestamp endDatetime, BigDecimal initialPrice, String region, String cityOrDistrict,
               String description, AuctionStatus auctionStatus, ProductCondition productCondition) {
        this.lotId = -1;
        this.ownerId = ownerId;
        this.category = category;
        this.auctionType = auctionType;
        this.title = title;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.initialPrice = initialPrice;
        this.region = region;
        this.cityOrDistrict = cityOrDistrict;
        this.description = description;
        this.auctionStatus = auctionStatus;
        this.productCondition = productCondition;
    }

    public Lot(long lotId, long ownerId, String category, AuctionType auctionType, String title,
               Timestamp startDatetime, Timestamp endDatetime, BigDecimal initialPrice, String region,
               String cityOrDistrict, String description, AuctionStatus auctionStatus,
               ProductCondition productCondition) {
        this.lotId = lotId;
        this.ownerId = ownerId;
        this.category = category;
        this.auctionType = auctionType;
        this.title = title;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.initialPrice = initialPrice;
        this.region = region;
        this.cityOrDistrict = cityOrDistrict;
        this.description = description;
        this.auctionStatus = auctionStatus;
        this.productCondition = productCondition;
    }

    public long getLotId() {
        return lotId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public String getCategory() {
        return category;
    }

    public AuctionType getAuctionType() {
        return auctionType;
    }

    public String getTitle() {
        return title;
    }

    public Timestamp getStartDatetime() {
        return startDatetime;
    }

    public Timestamp getEndDatetime() {
        return endDatetime;
    }

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public String getRegion() {
        return region;
    }

    public String getCityOrDistrict() {
        return cityOrDistrict;
    }

    public String getDescription() {
        return description;
    }

    public AuctionStatus getAuctionStatus() {
        return auctionStatus;
    }

    public ProductCondition getProductCondition() {
        return productCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lot lot = (Lot) o;

        if (lotId != lot.lotId) return false;
        if (ownerId != lot.ownerId) return false;
        if (category != null ? !category.equals(lot.category) : lot.category != null) return false;
        if (auctionType != lot.auctionType) return false;
        if (title != null ? !title.equals(lot.title) : lot.title != null) return false;
        if (startDatetime != null ? !startDatetime.equals(lot.startDatetime) : lot.startDatetime != null) return false;
        if (endDatetime != null ? !endDatetime.equals(lot.endDatetime) : lot.endDatetime != null) return false;
        if (initialPrice != null ? !initialPrice.equals(lot.initialPrice) : lot.initialPrice != null) return false;
        if (region != null ? !region.equals(lot.region) : lot.region != null) return false;
        if (cityOrDistrict != null ? !cityOrDistrict.equals(lot.cityOrDistrict) : lot.cityOrDistrict != null)
            return false;
        if (description != null ? !description.equals(lot.description) : lot.description != null) return false;
        if (auctionStatus != lot.auctionStatus) return false;
        return productCondition == lot.productCondition;
    }

    @Override
    public int hashCode() {
        int result = (int) (lotId ^ (lotId >>> 32));
        result = 31 * result + (int) (ownerId ^ (ownerId >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (auctionType != null ? auctionType.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (startDatetime != null ? startDatetime.hashCode() : 0);
        result = 31 * result + (endDatetime != null ? endDatetime.hashCode() : 0);
        result = 31 * result + (initialPrice != null ? initialPrice.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (cityOrDistrict != null ? cityOrDistrict.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (auctionStatus != null ? auctionStatus.hashCode() : 0);
        result = 31 * result + (productCondition != null ? productCondition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Lot{" +
                "lotId=" + lotId +
                ", ownerId=" + ownerId +
                ", category='" + category + '\'' +
                ", auctionType=" + auctionType +
                ", title='" + title + '\'' +
                ", startDatetime=" + startDatetime +
                ", endDatetime=" + endDatetime +
                ", initialPrice=" + initialPrice +
                ", region='" + region + '\'' +
                ", cityOrDistrict='" + cityOrDistrict + '\'' +
                ", description='" + description + '\'' +
                ", auctionStatus=" + auctionStatus +
                ", productCondition=" + productCondition +
                '}';
    }

    @Override
    public Lot createWithId(long id) {
        return new Lot(id, ownerId, category, auctionType, title, startDatetime, endDatetime, initialPrice, region,
                cityOrDistrict, description, auctionStatus, productCondition);
    }


}
