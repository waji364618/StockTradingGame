package unit;

import stockgame.domain.OwnedStock;
import stockgame.domain.Portfolio;
import stockgame.domain.Stock;
import stockgame.domain.Transaction;
import stockgame.persistence.fileimplementation.FileUnitOfWork;

import java.util.ArrayList;
import java.util.List;

public class MockUnitOfWork extends FileUnitOfWork {

    private List<Stock> stocks = new ArrayList<>();
    private List<Portfolio> portfolios = new ArrayList<>();
    private List<OwnedStock> ownedStocks = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    public MockUnitOfWork() {
        super("test"); // bruges ikke
    }

    @Override
    public void beginTransaction() {
        // gør ingenting
    }

    @Override
    public void commit() {
        // gør ingenting
    }

    @Override
    public void rollback() {
        // gør ingenting
    }

    @Override
    public List<Stock> getStocks() {
        return stocks;
    }

    @Override
    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    @Override
    public List<OwnedStock> getOwnedStocks() {
        return ownedStocks;
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

}


