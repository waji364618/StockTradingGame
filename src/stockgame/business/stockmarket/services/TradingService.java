package stockgame.business.stockmarket.services;

import stockgame.domain.OwnedStock;
import stockgame.domain.Portfolio;
import stockgame.domain.Stock;
import stockgame.domain.Transaction;
import stockgame.persistence.fileimplementation.*;
import stockgame.shared.configuration.AppConfig;
import stockgame.shared.logging.Logger;

import java.util.List;

public class TradingService {

    private final FileUnitOfWork uow;
    private final FileStockDao stockDao;
    private final FileOwnedStockDao ownedStockDao;
    private final FilePortfolioDao portfolioDao;
    private final FileTransactionDao transactionDao;
    private final Logger logger = Logger.getInstance();

    public TradingService(FileUnitOfWork uow) {
        this.uow = uow;
        this.stockDao = new FileStockDao(uow);
        this.ownedStockDao = new FileOwnedStockDao(uow);
        this.portfolioDao = new FilePortfolioDao(uow);
        this.transactionDao = new FileTransactionDao(uow);
    }

    public void buyStock(String symbol, int quantity) {

        try {
            uow.beginTransaction();

            // validation
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }

            // find stock
            Stock stock = stockDao.getBySymbol(symbol);

            if (stock == null) {
                throw new RuntimeException("Stock not found");
            }

            if (stock.getCurrentState().equals("Bankrupt")) {
                throw new RuntimeException("Cannot buy bankrupt stock");
            }

            // get portfolio
            Portfolio portfolio = portfolioDao.getAll().get(0);

            // beregn pris
            double price = stock.getCurrentPrice();
            double fee = price * quantity * AppConfig.getInstance().getTransactionFee();
            double total = (price * quantity) + fee;

            if (portfolio.getCurrentBalance() < total) {
                throw new IllegalArgumentException("Not enough balance");
            }

            // check if already owned
            OwnedStock owned = ownedStockDao.getBySymbol(symbol);

            if (owned != null) {
                // update eksisterende
                owned.setNumberOfShares(owned.getNumberOfShares() + quantity);
                ownedStockDao.update(owned);

            } else {
                // create ny
                OwnedStock newOwned = new OwnedStock(0, portfolio.getId(), symbol, quantity);
                ownedStockDao.create(newOwned);
            }

            //  update balance
            portfolio.setCurrentBalance(portfolio.getCurrentBalance() - total);
            portfolioDao.update(portfolio);

            // save transaction
            Transaction transaction = new Transaction(0, portfolio.getId(), symbol, "BUY", quantity, price, total, fee,
                    System.currentTimeMillis());

            transactionDao.create(transaction);

            logger.log("INFO", "Bought " + quantity + " shares of " + symbol);

            uow.commit();

        } catch (Exception e) {

            logger.log("ERROR", e.getMessage());

            uow.rollback();

            throw e;
        }
    }

    public void sellStock(String symbol, int quantity) {

        try {
            uow.beginTransaction();

            // validation
            if (quantity <= 0) {
                throw new IllegalArgumentException("Invalid quantity");
            }

            // find stock
            Stock stock = stockDao.getBySymbol(symbol);

            if (stock == null) {
                throw new IllegalArgumentException("Stock not found");
            }

            // find portfolio
            Portfolio portfolio = portfolioDao.getAll().get(0);

            // find owned stock
            OwnedStock owned = ownedStockDao.getBySymbol(symbol);

            if (owned == null) {
                throw new IllegalArgumentException("You do not own this stock");
            }

            if (owned.getNumberOfShares() < quantity) {
                throw new IllegalArgumentException("Not enough shares");
            }

            // beregn penge
            double price = stock.getCurrentPrice();
            double fee = price * quantity * AppConfig.getInstance().getTransactionFee();
            double total = (price * quantity) - fee;

            // opdater shares
            owned.setNumberOfShares(owned.getNumberOfShares() - quantity);

            if (owned.getNumberOfShares() == 0) {
                ownedStockDao.delete(owned.getId());
            } else {
                ownedStockDao.update(owned);
            }

            // opdater balance
            portfolio.setCurrentBalance(portfolio.getCurrentBalance() + total);
            portfolioDao.update(portfolio);

            Transaction transaction = new Transaction(0, portfolio.getId(), symbol, "SELL", quantity, price, total, fee,
                    System.currentTimeMillis());

            transactionDao.create(transaction);

            logger.log("INFO", "Sold " + quantity + " shares of " + symbol);

            uow.commit();

        } catch (Exception e) {

            logger.log("ERROR", e.getMessage());
            uow.rollback();

            throw e;
        }
    }

    // Query methods
    public double getBalance() {
        return portfolioDao.getAll().get(0).getCurrentBalance();
    }

    public List<OwnedStock> getOwnedStocks() {
        return ownedStockDao.getAll();
    }

    public List<Stock> getAvailableStocks() {
        return stockDao.getAll();
    }

    public List<Transaction> getTransactions() {
        return transactionDao.getAll();
    }

    public double getPortfolioValue() {

        double total = getBalance();

        for (OwnedStock os : ownedStockDao.getAll()) {

            Stock stock = stockDao.getBySymbol(os.getStockSymbol());

            if (stock != null) {
                total += stock.getCurrentPrice() * os.getNumberOfShares();
            }
        }

        return total;
    }

    public boolean hasWon() {
        return getBalance() >= AppConfig.getInstance().getWinningBalance();

    }

    public void initializeGame() {
        if (portfolioDao.getAll().isEmpty()) {
            uow.beginTransaction();
            Portfolio portfolio = new Portfolio(0, AppConfig.getInstance().getStartingBalance());
            portfolioDao.create(portfolio);
            uow.commit();
        }
    }
}

