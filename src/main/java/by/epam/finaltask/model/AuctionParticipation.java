package by.epam.finaltask.model;

import java.math.BigDecimal;

public class AuctionParticipation {

    private final static boolean DEFAULT_DEPOSIT_IS_RETURNED = false;

    private final long participantId;
    private final long lotId;
    private final BigDecimal deposit;
    private final boolean depositIsReturned;

    public AuctionParticipation(long participantId, long lotId, BigDecimal deposit, boolean depositIsReturned) {
        this.participantId = participantId;
        this.lotId = lotId;
        this.deposit = deposit;
        this.depositIsReturned = depositIsReturned;
    }

    public AuctionParticipation(long participantId, long lotId, BigDecimal deposit) {
        this.participantId = participantId;
        this.lotId = lotId;
        this.deposit = deposit;
        this.depositIsReturned = DEFAULT_DEPOSIT_IS_RETURNED;
    }

    public long getParticipantId() {
        return participantId;
    }

    public long getLotId() {
        return lotId;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public boolean isDepositIsReturned() {
        return depositIsReturned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionParticipation that = (AuctionParticipation) o;

        if (participantId != that.participantId) return false;
        if (lotId != that.lotId) return false;
        if (depositIsReturned != that.depositIsReturned) return false;
        return deposit != null ? deposit.compareTo(that.deposit)==0 : that.deposit == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (participantId ^ (participantId >>> 32));
        result = 31 * result + (int) (lotId ^ (lotId >>> 32));
        result = 31 * result + (deposit != null ? deposit.toPlainString().hashCode() : 0);
        result = 31 * result + (depositIsReturned ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuctionParticipation{" +
                "participantId=" + participantId +
                ", lotId=" + lotId +
                ", deposit=" + deposit +
                ", depositIsReturned=" + depositIsReturned +
                '}';
    }
}
