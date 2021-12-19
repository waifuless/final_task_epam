package by.epam.finaltask.model;

import java.math.BigDecimal;

public class AuctionResult {

    private final BigDecimal winnerBidAmount;
    private final String emailToContact;
    private final BigDecimal deposit;

    public AuctionResult(BigDecimal winnerBidAmount, String emailToContact, BigDecimal deposit) {
        this.winnerBidAmount = winnerBidAmount;
        this.emailToContact = emailToContact;
        this.deposit = deposit;
    }

    public BigDecimal getWinnerBidAmount() {
        return winnerBidAmount;
    }

    public String getEmailToContact() {
        return emailToContact;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionResult that = (AuctionResult) o;

        if (winnerBidAmount != null ? winnerBidAmount.compareTo(that.winnerBidAmount)!=0 : that.winnerBidAmount != null)
            return false;
        if (emailToContact != null ? !emailToContact.equals(that.emailToContact) : that.emailToContact != null)
            return false;
        return deposit != null ? deposit.compareTo(that.deposit)==0 : that.deposit == null;
    }

    @Override
    public int hashCode() {
        int result = winnerBidAmount != null ? winnerBidAmount.toPlainString().hashCode() : 0;
        result = 31 * result + (emailToContact != null ? emailToContact.hashCode() : 0);
        result = 31 * result + (deposit != null ? deposit.toPlainString().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuctionResult{" +
                "winnerBidAmount=" + winnerBidAmount +
                ", emailToContact='" + emailToContact + '\'' +
                ", deposit=" + deposit +
                '}';
    }
}
