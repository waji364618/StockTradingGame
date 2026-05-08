package stockgame.business.stockmarket.simulation;

import java.util.Random;

class DecliningState implements StockState{

    private static final Random random = new Random();

    @Override
    public double calculatePriceChange(LiveStock stock) {

        // større chance for at gå ned
        double change = -(random.nextDouble() * 2);

        double r = random.nextDouble();

        if (r < 0.10) {
            stock.setState(new SteadyState());
        }
        else if (r < 0.15) {
            stock.setState(new GrowingState());
        }

        return change;
    }

    @Override
    public String getName() {
        return "Declining";
    }
}

