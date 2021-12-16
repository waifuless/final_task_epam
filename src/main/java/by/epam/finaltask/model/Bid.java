package by.epam.finaltask.model;

import java.math.BigDecimal;

public class Bid implements DaoEntity<Bid>{

    private final long bidId;
    private final long userId;
    private final BigDecimal amount;
    private final long lotId;

    public Bid(long bidId, long userId, BigDecimal amount, long lotId) {
        this.bidId = bidId;
        this.userId = userId;
        this.amount = amount;
        this.lotId = lotId;
    }

    public long getBidId() {
        return bidId;
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public long getLotId() {
        return lotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bid bid = (Bid) o;

        if (bidId != bid.bidId) return false;
        if (userId != bid.userId) return false;
        if (lotId != bid.lotId) return false;
        return amount != null ? amount.equals(bid.amount) : bid.amount == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (bidId ^ (bidId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (int) (lotId ^ (lotId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "bidId=" + bidId +
                ", userId=" + userId +
                ", amount=" + amount +
                ", lotId=" + lotId +
                '}';
    }

    @Override
    public Bid createWithId(long id) {
        return new Bid(id, userId, amount, lotId);
    }

    public static BidBuilder builder(){
        return new BidBuilder();
    }

    public static class BidBuilder {
        private long bidId;
        private long userId;
        private BigDecimal amount;
        private long lotId;

        public BidBuilder setBidId(long bidId) {
            this.bidId = bidId;
            return this;
        }

        public BidBuilder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public BidBuilder setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public BidBuilder setLotId(long lotId) {
            this.lotId = lotId;
            return this;
        }

        public Bid build() {
            return new Bid(bidId, userId, amount, lotId);
        }
    }
}
