package by.epam.finaltask.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Fields should be null if you do not mean filter by they
 */
public class LotContext {

    private final Long ownerId;
    private final String category;
    private final String auctionType;
    private final String title;
    private final BigDecimal minInitialPrice;
    private final BigDecimal maxInitialPrice;
    private final String region;
    private final String cityOrDistrict;
    private final String auctionStatus;
    private final String productCondition;

    private LotContext(Long ownerId, String category, String auctionType, String title,
                       BigDecimal minInitialPrice, BigDecimal maxInitialPrice, String region,
                      String cityOrDistrict, String auctionStatus,
                      String productCondition) {
        this.ownerId = ownerId;
        this.category = category;
        this.auctionType = auctionType;
        this.title = title;
        this.minInitialPrice = minInitialPrice;
        this.maxInitialPrice = maxInitialPrice;
        this.region = region;
        this.cityOrDistrict = cityOrDistrict;
        this.auctionStatus = auctionStatus;
        this.productCondition = productCondition;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getCategory() {
        return category;
    }

    public String getAuctionType() {
        return auctionType;
    }

    public String getTitle() {
        return title;
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

    public String getAuctionStatus() {
        return auctionStatus;
    }

    public String getProductCondition() {
        return productCondition;
    }



    public static LotContextBuilder builder(){
        return new LotContextBuilder();
    }

    public static class LotContextBuilder {
        private Long ownerId;
        private String category;
        private String auctionType;
        private String title;
        private BigDecimal minInitialPrice;
        private BigDecimal maxInitialPrice;
        private String region;
        private String cityOrDistrict;
        private String auctionStatus;
        private String productCondition;

        public LotContextBuilder setOwnerId(Long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public LotContextBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public LotContextBuilder setAuctionType(String auctionType) {
            this.auctionType = auctionType;
            return this;
        }

        public LotContextBuilder setTitle(String title) {
            this.title = title;
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

        public LotContextBuilder setAuctionStatus(String auctionStatus) {
            this.auctionStatus = auctionStatus;
            return this;
        }

        public LotContextBuilder setProductCondition(String productCondition) {
            this.productCondition = productCondition;
            return this;
        }

        public LotContext build() {
            return new LotContext(ownerId, category, auctionType, title, minInitialPrice,
                    maxInitialPrice, region, cityOrDistrict, auctionStatus, productCondition);
        }
    }
}
