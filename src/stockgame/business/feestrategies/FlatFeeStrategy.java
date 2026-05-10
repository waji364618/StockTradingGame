package stockgame.business.feestrategies;

public class FlatFeeStrategy implements FeeStrategy {

    @Override
    public double calculateFee(double price, int quantity) {
        return 10;
    }
}
