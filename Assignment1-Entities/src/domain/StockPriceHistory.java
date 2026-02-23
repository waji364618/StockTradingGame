package domain;

public class StockPriceHistory {

    private final int id;
    private final String stockSymbol; // FK
    private final double price;
    private final long timestamp;

    public StockPriceHistory(int id, String stockSymbol, double price, long timestamp) {
        this.id = id;
        this.stockSymbol = stockSymbol;
        this.price = price;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }
    public String getStockSymbol() {
        return stockSymbol;
    }
    public double getPrice() {
        return price;
    }
    public long getTimestamp() {
        return timestamp;
    }

}
