package stockgame.business.feestrategies;

public class PercentageFeeStrategy implements FeeStrategy {


    @Override
    public double calculateFee(double price, int quantity) {
        return price * quantity * 0.02;
    }
}

