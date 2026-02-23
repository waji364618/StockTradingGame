package domain;

public class OwnedStock {

    private final int id;
    private final int portfolioId;   // FK
    private final String stockSymbol; // FK
    private int numberOfShares;

    public OwnedStock(int id, int portfolioId, String stockSymbol, int numberOfShares) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.stockSymbol = stockSymbol;
        this.numberOfShares = numberOfShares;
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
    public int getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(int numberOfShares) {

        this.numberOfShares = numberOfShares;
    }
}

