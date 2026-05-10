package stockgame.business.feestrategies;

public interface FeeStrategy {

    double calculateFee(double price, int quantity);
}
