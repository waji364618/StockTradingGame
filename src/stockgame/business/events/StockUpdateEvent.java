package stockgame.business.events;

public class StockUpdateEvent {

    private final String symbol;
    private final double price;
    private final String state;

    public StockUpdateEvent(String symbol, double price, String state) {
        this.symbol = symbol;
        this.price = price;
        this.state = state;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public String getState() {
        return state;
    }
}
