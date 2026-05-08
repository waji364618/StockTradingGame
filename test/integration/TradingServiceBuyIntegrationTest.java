package integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stockgame.business.stockmarket.services.TradingService;
import stockgame.domain.Portfolio;
import stockgame.domain.Stock;
import stockgame.persistence.fileimplementation.FilePortfolioDao;
import stockgame.persistence.fileimplementation.FileStockDao;
import stockgame.persistence.fileimplementation.FileUnitOfWork;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TradingServiceBuyIntegrationTest {

    private TradingService tradingService;
    private FileUnitOfWork uow;
    private String testDirPath;

    @BeforeEach void setup() {

        // unik test mappe
        testDirPath = "test-" + UUID.randomUUID();

        uow = new FileUnitOfWork(testDirPath);

        tradingService = new TradingService(uow);

        // ryd data
        uow.beginTransaction();

        uow.getStocks().clear();
        uow.getPortfolios().clear();
        uow.getOwnedStocks().clear();
        uow.getTransactions().clear();

        // dao'er
        FileStockDao stockDao = new FileStockDao(uow);
        FilePortfolioDao portfolioDao = new FilePortfolioDao(uow);

        // test stock
        stockDao.create(new Stock("AAPL", "Apple", 100.0, "Steady"));

        // test portfolio
        portfolioDao.create(new Portfolio(1, 10000));

        uow.commit();
    }

    @AfterEach void cleanup() throws IOException {

        Files.walk(Path.of(testDirPath))
                .map(Path::toFile)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(file -> file.delete());
    }

    @Test void buyStock_ShouldCreateOwnedStock() {

        // ACT
        tradingService.buyStock("AAPL", 5);

        // ASSERT
        assertEquals(1, tradingService.getOwnedStocks().size());

        assertEquals(5, tradingService.getOwnedStocks().get(0).getNumberOfShares());
    }

    @Test void buyStock_ShouldReduceBalance() {

        // ACT
        tradingService.buyStock("AAPL", 5);

        // ASSERT
        assertTrue(tradingService.getBalance() < 10000);
    }

    @Test void buyStock_ShouldCreateTransaction() {

        // ACT
        tradingService.buyStock("AAPL", 5);

        // ASSERT
        assertEquals(1, tradingService.getTransactions().size());
    }

    @Test void buyStock_ShouldThrowException_WhenBalanceTooLow() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> tradingService.buyStock("AAPL", 100000));
    }

}

