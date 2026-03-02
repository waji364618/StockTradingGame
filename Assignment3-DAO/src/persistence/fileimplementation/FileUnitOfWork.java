package persistence.fileimplementation;

import domain.OwnedStock;
import domain.Portfolio;
import domain.Stock;
import persistence.interfaces.UnitOfWork;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUnitOfWork implements UnitOfWork {

    private static final Object FILE_WRITE_LOCK = new Object();

    private final String directoryPath;

    private List<Stock> stocks;
    private List<Portfolio> portfolios;
    private List<OwnedStock> ownedStocks;

    public FileUnitOfWork(String directoryPath) {
        this.directoryPath = directoryPath;

        ensureFilesExist(
                getStockFilePath(),
                getPortfolioFilePath(),
                getOwnedStockFilePath()
        );
    }


    // TRANSACTION METHODS

    @Override
    public void beginTransaction() {
        resetLists();
    }

    @Override
    public void commit() {

        synchronized (FILE_WRITE_LOCK) {

            if (stocks != null) {
                writeStocksToFile();
            }

            if (portfolios != null) {
                writePortfoliosToFile();
            }
            if (ownedStocks != null) {
                writeOwnedStocksToFile();
            }
        }

        resetLists();
    }

    @Override
    public void rollback() {
        resetLists();
    }


    // GET METHODS

    public List<Stock> getStocks() {
        if (stocks == null) {
            stocks = loadStocksFromFile();
        }
        return stocks;
    }

    public List<Portfolio> getPortfolios() {
        if (portfolios == null) {
            portfolios = loadPortfoliosFromFile();
        }
        return portfolios;
    }
    public List<OwnedStock> getOwnedStocks() {
        if (ownedStocks == null) {
            ownedStocks = loadOwnedStocksFromFile();
        }
        return ownedStocks;
    }

    // PRIVATE HELPERS

    private void resetLists() {
        stocks = null;
        portfolios = null;
        ownedStocks = null;
    }

    private String getStockFilePath() {
        return directoryPath + "/stocks.txt";
    }

    private String getPortfolioFilePath() {
        return directoryPath + "/portfolios.txt";
    }

    private void ensureFilesExist(String... filePaths) {

        for (String path : filePaths) {
            try {
                Path filePath = Paths.get(path);

                if (!Files.exists(filePath)) {
                    Files.createDirectories(filePath.getParent());
                    Files.createFile(filePath);
                }

            } catch (IOException e) {
                throw new RuntimeException("Could not create file: " + path, e);
            }
        }
    }
    private String getOwnedStockFilePath() {
        return directoryPath + "/ownedstocks.txt";
    }



    // LOAD METHODS

    private List<Stock> loadStocksFromFile() {

        List<Stock> result = new ArrayList<>();

        try {
            List<String> lines =
                    Files.readAllLines(Paths.get(getStockFilePath()));

            for (String line : lines) {
                if (!line.isBlank()) {
                    result.add(fromPSV(line));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read stocks file", e);
        }

        return result;
    }

    private List<Portfolio> loadPortfoliosFromFile() {

        List<Portfolio> result = new ArrayList<>();

        try {
            List<String> lines =
                    Files.readAllLines(Paths.get(getPortfolioFilePath()));

            for (String line : lines) {
                if (!line.isBlank()) {
                    result.add(fromPSVPortfolio(line));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read portfolios file", e);
        }

        return result;
    }
    private List<OwnedStock> loadOwnedStocksFromFile() {

        List<OwnedStock> result = new ArrayList<>();

        try {
            List<String> lines =
                    Files.readAllLines(Paths.get(getOwnedStockFilePath()));

            for (String line : lines) {
                if (!line.isBlank()) {
                    result.add(fromPSVOwnedStock(line));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read ownedstocks file", e);
        }

        return result;
    }

    // WRITE METHODS

    private void writeStocksToFile() {

        List<String> lines = new ArrayList<>();

        for (Stock stock : stocks) {
            lines.add(toPSV(stock));
        }

        try {
            Files.write(Paths.get(getStockFilePath()), lines);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write stocks file", e);
        }
    }

    private void writePortfoliosToFile() {

        List<String> lines = new ArrayList<>();

        for (Portfolio portfolio : portfolios) {
            lines.add(toPSVPortfolio(portfolio));
        }

        try {
            Files.write(Paths.get(getPortfolioFilePath()), lines);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write portfolios file", e);
        }
    }

    private void writeOwnedStocksToFile() {

        List<String> lines = new ArrayList<>();

        for (OwnedStock os : ownedStocks) {
            lines.add(toPSVOwnedStock(os));
        }

        try {
            Files.write(Paths.get(getOwnedStockFilePath()), lines);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write ownedstocks file", e);
        }
    }


    // PSV CONVERSION

    private String toPSV(Stock stock) {
        return stock.getSymbol() + "|" +
                stock.getName() + "|" +
                stock.getCurrentPrice() + "|" +
                stock.getCurrentState();
    }

    private Stock fromPSV(String psv) {

        String[] parts = psv.split("\\|");

        return new Stock(
                parts[0],
                parts[1],
                Double.parseDouble(parts[2]),
                parts[3]
        );
    }

    private String toPSVPortfolio(Portfolio portfolio) {
        return portfolio.getId() + "|" +
                portfolio.getCurrentBalance();
    }

    private Portfolio fromPSVPortfolio(String psv) {

        String[] parts = psv.split("\\|");

        return new Portfolio(
                Integer.parseInt(parts[0]),
                Double.parseDouble(parts[1])
        );
    }
    private String toPSVOwnedStock(OwnedStock os) {
        return os.getId() + "|" +
                os.getPortfolioId() + "|" +
                os.getStockSymbol() + "|" +
                os.getNumberOfShares();
    }

    private OwnedStock fromPSVOwnedStock(String psv) {

        String[] parts = psv.split("\\|");

        return new OwnedStock(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                parts[2],
                Integer.parseInt(parts[3])
        );
    }
}