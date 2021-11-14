package by.epam.finaltask.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LotContext {

    private final long ownerId;
    private final String category;
    private final AuctionType auctionType;
    private final String title;
    private final Timestamp startDatetime;
    private final Timestamp endDatetime;
    private final BigDecimal minInitialPrice;
    private final BigDecimal maxInitialPrice;
    private final String region;
    private final String cityOrDistrict;
    private final String description;
    private final AuctionStatus auctionStatus;
    private final ProductCondition productCondition;

    private LotContext(long ownerId, String category, AuctionType auctionType, String title, Timestamp startDatetime,
                      Timestamp endDatetime, BigDecimal minInitialPrice, BigDecimal maxInitialPrice, String region,
                      String cityOrDistrict, String description, AuctionStatus auctionStatus,
                      ProductCondition productCondition) {
        this.ownerId = ownerId;
        this.category = category;
        this.auctionType = auctionType;
        this.title = title;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.minInitialPrice = minInitialPrice;
        this.maxInitialPrice = maxInitialPrice;
        this.region = region;
        this.cityOrDistrict = cityOrDistrict;
        this.description = description;
        this.auctionStatus = auctionStatus;
        this.productCondition = productCondition;
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

    public BigDecimal getMinInitialPrice() {
        return minInitialPrice;
    }

    public BigDecimal getMaxInitialPrice() {
        return maxInitialPrice;
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

        LotContext that = (LotContext) o;

        if (ownerId != that.ownerId) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (auctionType != that.auctionType) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (startDatetime != null ? !startDatetime.equals(that.startDatetime) : that.startDatetime != null)
            return false;
        if (endDatetime != null ? !endDatetime.equals(that.endDatetime) : that.endDatetime != null) return false;
        if (minInitialPrice != null ? !minInitialPrice.equals(that.minInitialPrice) : that.minInitialPrice != null)
            return false;
        if (maxInitialPrice != null ? !maxInitialPrice.equals(that.maxInitialPrice) : that.maxInitialPrice != null)
            return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        if (cityOrDistrict != null ? !cityOrDistrict.equals(that.cityOrDistrict) : that.cityOrDistrict != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (auctionStatus != that.auctionStatus) return false;
        return productCondition == that.productCondition;
    }

    @Override
    public int hashCode() {
        int result = (int) (ownerId ^ (ownerId >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (auctionType != null ? auctionType.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (startDatetime != null ? startDatetime.hashCode() : 0);
        result = 31 * result + (endDatetime != null ? endDatetime.hashCode() : 0);
        result = 31 * result + (minInitialPrice != null ? minInitialPrice.hashCode() : 0);
        result = 31 * result + (maxInitialPrice != null ? maxInitialPrice.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (cityOrDistrict != null ? cityOrDistrict.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (auctionStatus != null ? auctionStatus.hashCode() : 0);
        result = 31 * result + (productCondition != null ? productCondition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LotContext{" +
                "ownerId=" + ownerId +
                ", category='" + category + '\'' +
                ", auctionType=" + auctionType +
                ", title='" + title + '\'' +
                ", startDatetime=" + startDatetime +
                ", endDatetime=" + endDatetime +
                ", minInitialPrice=" + minInitialPrice +
                ", maxInitialPrice=" + maxInitialPrice +
                ", region='" + region + '\'' +
                ", cityOrDistrict='" + cityOrDistrict + '\'' +
                ", description='" + description + '\'' +
                ", auctionStatus=" + auctionStatus +
                ", productCondition=" + productCondition +
                '}';
    }

    public static LotContextBuilder builder(){
        return new LotContextBuilder();
    }

    public static class LotContextBuilder {
        private long ownerId;
        private String category;
        private AuctionType auctionType;
        private String title;
        private Timestamp startDatetime;
        private Timestamp endDatetime;
        private BigDecimal minInitialPrice;
        private BigDecimal maxInitialPrice;
        private String region;
        private String cityOrDistrict;
        private String description;
        private AuctionStatus auctionStatus;
        private ProductCondition productCondition;

        public LotContextBuilder setOwnerId(long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public LotContextBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public LotContextBuilder setAuctionType(AuctionType auctionType) {
            this.auctionType = auctionType;
            return this;
        }

        public LotContextBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public LotContextBuilder setStartDatetime(Timestamp startDatetime) {
            this.startDatetime = startDatetime;
            return this;
        }

        public LotContextBuilder setEndDatetime(Timestamp endDatetime) {
            this.endDatetime = endDatetime;
            return this;
        }

        public LotContextBuilder setMinInitialPrice(BigDecimal minInitialPrice) {
            this.minInitialPrice = minInitialPrice;
            return this;
        }

        public LotContextBuilder setMaxInitialPrice(BigDecimal maxInitialPrice) {
            this.maxInitialPrice = maxInitialPrice;
            return this;
        }

        public LotContextBuilder setRegion(String region) {
            this.region = region;
            return this;
        }

        public LotContextBuilder setCityOrDistrict(String cityOrDistrict) {
            this.cityOrDistrict = cityOrDistrict;
            return this;
        }

        public LotContextBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public LotContextBuilder setAuctionStatus(AuctionStatus auctionStatus) {
            this.auctionStatus = auctionStatus;
            return this;
        }

        public LotContextBuilder setProductCondition(ProductCondition productCondition) {
            this.productCondition = productCondition;
            return this;
        }

        public LotContext build() {
            return new LotContext(ownerId, category, auctionType, title, startDatetime, endDatetime, minInitialPrice,
                    maxInitialPrice, region, cityOrDistrict, description, auctionStatus, productCondition);
        }
    }
}
