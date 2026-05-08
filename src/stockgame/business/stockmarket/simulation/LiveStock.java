package stockgame.business.stockmarket.simulation;

import stockgame.shared.configuration.AppConfig;

public class LiveStock {

    private String symbol;
    private double currentPrice;
    private StockState currentState;

    public LiveStock(String symbol) {
        this.symbol = symbol;
        this.currentPrice = AppConfig.getInstance().getStockResetValue();
        this.currentState = new SteadyState();
    }

    public void updatePrice() {

        double change = currentState.calculatePriceChange(this);

        currentPrice += change;

        if (currentPrice <= 0) {
            currentPrice = 0;
            setState(new BankruptState());
        }
    }
    // package protected - kun state klasser må kalde denne
    void setState(StockState state) {
        this.currentState = state;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public String getStateName() {
        return currentState.getName();
    }

}
