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

    public static LotBuilder builder() {
        return new LotBuilder();
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
        return builder().setLotId(id).setOwnerId(ownerId).setCategory(category).setAuctionType(auctionType)
                .setTitle(title).setStartDatetime(startDatetime).setEndDatetime(endDatetime)
                .setInitialPrice(initialPrice).setRegion(region).setCityOrDistrict(cityOrDistrict)
                .setDescription(description).setAuctionStatus(auctionStatus).setProductCondition(productCondition)
                .build();
    }

    public static class LotBuilder {
        private long lotId;
        private long ownerId;
        private String category;
        private AuctionType auctionType;
        private String title;
        private Timestamp startDatetime;
        private Timestamp endDatetime;
        private BigDecimal initialPrice;
        private String region;
        private String cityOrDistrict;
        private String description;
        private AuctionStatus auctionStatus;
        private ProductCondition productCondition;

        private LotBuilder() {
        }

        public LotBuilder setLotId(long lotId) {
            this.lotId = lotId;
            return this;
        }

        public LotBuilder setOwnerId(long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public LotBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public LotBuilder setAuctionType(AuctionType auctionType) {
            this.auctionType = auctionType;
            return this;
        }

        public LotBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public LotBuilder setStartDatetime(Timestamp startDatetime) {
            this.startDatetime = startDatetime;
            return this;
        }

        public LotBuilder setEndDatetime(Timestamp endDatetime) {
            this.endDatetime = endDatetime;
            return this;
        }

        public LotBuilder setInitialPrice(BigDecimal initialPrice) {
            this.initialPrice = initialPrice;
            return this;
        }

        public LotBuilder setRegion(String region) {
            this.region = region;
            return this;
        }

        public LotBuilder setCityOrDistrict(String cityOrDistrict) {
            this.cityOrDistrict = cityOrDistrict;
            return this;
        }

        public LotBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public LotBuilder setAuctionStatus(AuctionStatus auctionStatus) {
            this.auctionStatus = auctionStatus;
            return this;
        }

        public LotBuilder setProductCondition(ProductCondition productCondition) {
            this.productCondition = productCondition;
            return this;
        }

        public LotBuilder setLot(Lot lot) {
            this.lotId = lot.lotId;
            this.ownerId = lot.ownerId;
            this.category = lot.category;
            this.auctionType = lot.auctionType;
            this.title = lot.title;
            this.startDatetime = lot.startDatetime;
            this.endDatetime = lot.endDatetime;
            this.initialPrice = lot.initialPrice;
            this.region = lot.region;
            this.cityOrDistrict = lot.cityOrDistrict;
            this.description = lot.description;
            this.auctionStatus = lot.auctionStatus;
            this.productCondition = lot.productCondition;
            return this;
        }

        public Lot build() {
            return new Lot(lotId, ownerId, category, auctionType, title, startDatetime, endDatetime, initialPrice,
                    region, cityOrDistrict, description, auctionStatus, productCondition);
        }
    }
}