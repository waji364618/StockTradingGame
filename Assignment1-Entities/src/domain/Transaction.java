package domain;

public class Transaction {

    private final int id;
    private final int portfolioId;    // FK
    private final String stockSymbol; // FK
    private final String type;
    private final int quantity;
    private final double pricePerShare;
    private final double totalAmount;
    private final double fee;
    private final long timestamp;

    public Transaction(int id, int portfolioId, String stockSymbol, String type,
                       int quantity, double pricePerShare, double totalAmount,
                       double fee, long timestamp) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.stockSymbol = stockSymbol;
        this.type = type;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.totalAmount = totalAmount;
        this.fee = fee;
        this.timestamp = timestamp;
    }
    public int getId() {
        return id;
    }

    public int getPortfolioId() {
        return portfolioId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getFee() {
        return fee;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
