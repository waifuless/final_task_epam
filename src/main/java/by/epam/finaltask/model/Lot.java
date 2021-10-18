package by.epam.finaltask.model;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.Map;

public class Lot {

    private int lotId;
    private int ownerId;
    private String category;
    private AuctionType auctionType;
    private String title;
    private Date startDate;
    private Date endDate;
    private BigDecimal initialPrice;
    private String originPlace;
    private String description;
    private AuctionStatus auctionStatus;
    private ProductCondition productCondition;
    private Map<String, String> extraAttributes;
    private Blob mainImage;

    public Lot(int lotId, int ownerId, String category, AuctionType auctionType, String title, Date startDate,
               Date endDate, BigDecimal initialPrice, String originPlace, String description,
               AuctionStatus auctionStatus, ProductCondition productCondition, Map<String, String> extraAttributes,
               Blob mainImage) {
        this.lotId = lotId;
        this.ownerId = ownerId;
        this.category = category;
        this.auctionType = auctionType;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.initialPrice = initialPrice;
        this.originPlace = originPlace;
        this.description = description;
        this.auctionStatus = auctionStatus;
        this.productCondition = productCondition;
        this.extraAttributes = extraAttributes;
        this.mainImage = mainImage;
    }

    public int getLotId() {
        return lotId;
    }

    public int getOwnerId() {
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public String getOriginPlace() {
        return originPlace;
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

    public Map<String, String> getExtraAttributes() {
        return extraAttributes;
    }

    public Blob getMainImage() {
        return mainImage;
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
        if (startDate != null ? !startDate.equals(lot.startDate) : lot.startDate != null) return false;
        if (endDate != null ? !endDate.equals(lot.endDate) : lot.endDate != null) return false;
        if (initialPrice != null ? !initialPrice.equals(lot.initialPrice) : lot.initialPrice != null) return false;
        if (originPlace != null ? !originPlace.equals(lot.originPlace) : lot.originPlace != null) return false;
        if (description != null ? !description.equals(lot.description) : lot.description != null) return false;
        if (auctionStatus != lot.auctionStatus) return false;
        if (productCondition != lot.productCondition) return false;
        if (extraAttributes != null ? !extraAttributes.equals(lot.extraAttributes) : lot.extraAttributes != null)
            return false;
        return mainImage != null ? mainImage.equals(lot.mainImage) : lot.mainImage == null;
    }

    @Override
    public int hashCode() {
        int result = lotId;
        result = 31 * result + ownerId;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (auctionType != null ? auctionType.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (initialPrice != null ? initialPrice.hashCode() : 0);
        result = 31 * result + (originPlace != null ? originPlace.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (auctionStatus != null ? auctionStatus.hashCode() : 0);
        result = 31 * result + (productCondition != null ? productCondition.hashCode() : 0);
        result = 31 * result + (extraAttributes != null ? extraAttributes.hashCode() : 0);
        result = 31 * result + (mainImage != null ? mainImage.hashCode() : 0);
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
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", initialPrice=" + initialPrice +
                ", originPlace='" + originPlace + '\'' +
                ", description='" + description + '\'' +
                ", auctionStatus=" + auctionStatus +
                ", productCondition=" + productCondition +
                ", extraAttributes=" + extraAttributes +
                ", mainImage=" + mainImage +
                '}';
    }
}
