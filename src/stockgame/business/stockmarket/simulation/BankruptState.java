package stockgame.business.stockmarket.simulation;

import stockgame.shared.configuration.AppConfig;

import java.util.Random;

class BankruptState implements StockState{

    private static final Random random = new Random();

    @Override
    public double calculatePriceChange(LiveStock stock) {

        // pris er 0 når bankrupt
        double r = random.nextDouble();

        // 20% chance for reset
        if (r < 0.20) {

            double resetPrice =
                    AppConfig.getInstance().getStockResetValue();

            stock.setState(new SteadyState());

            return resetPrice;
        }

        return 0;
    }

    @Override
    public String getName() {
        return "Bankrupt";
    }
}

