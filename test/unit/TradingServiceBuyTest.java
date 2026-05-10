package unit;

import stockgame.business.feestrategies.PercentageFeeStrategy;
import stockgame.business.stockmarket.services.TradingService;
import stockgame.domain.OwnedStock;
import stockgame.domain.Portfolio;
import stockgame.domain.Stock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TradingServiceBuyTest {

    @Test void shouldByOneStock() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow, new PercentageFeeStrategy());

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        uow.getPortfolios().add(new Portfolio(1, 1000));

        // Act
        service.buyStock("AAPL", 1);

        // Assert
        assertEquals(1, uow.getOwnedStocks().size());

        assertTrue(uow.getPortfolios().get(0).getCurrentBalance() < 1000);
    }

    @Test void shouldFailWhenQuantityIsZero() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow, new PercentageFeeStrategy());

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        uow.getPortfolios().add(new Portfolio(1, 1000));

        // Assert
        assertThrows(IllegalArgumentException.class, () -> service.buyStock("AAPL", 0));
    }

    @Test void shouldFailWhenNotEnoughMoney() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow, new PercentageFeeStrategy());

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        // for lav balance
        uow.getPortfolios().add(new Portfolio(1, 50));

        // Assert
        assertThrows(IllegalArgumentException.class, () -> service.buyStock("AAPL", 1));
    }

    @Test
    void shouldFailWhenStockNotFound() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow, new PercentageFeeStrategy());

        uow.getPortfolios().add(new Portfolio(1, 1000));

        // Assert
        assertThrows(RuntimeException.class, () -> service.buyStock("AAPL", 1));
    }

    @Test void shouldUpdateExistingStock() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow, new PercentageFeeStrategy());

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        uow.getPortfolios().add(new Portfolio(1, 1000));

        // allerede ejer 1
        uow.getOwnedStocks().add(new OwnedStock(1, 1, "AAPL", 1));

        // Act
        service.buyStock("AAPL", 2);

        // Assert
        assertEquals(3, uow.getOwnedStocks().get(0).getNumberOfShares());
    }

    @Test void shouldBuyWhenBalanceIsExactlyEnough() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow, new PercentageFeeStrategy());

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        // præcis nok inkl. gebyr
        uow.getPortfolios().add(new Portfolio(1, 102));

        // Act
        service.buyStock("AAPL", 1);

        // Assert
        assertEquals(1, uow.getOwnedStocks().size());
    }

    @Test void shouldFailWhenStockIsBankrupt() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow, new PercentageFeeStrategy());

        uow.getStocks().add(new Stock("AAPL", "Apple", 0, "Bankrupt"));

        uow.getPortfolios().add(new Portfolio(1, 1000));

        // Assert
        assertThrows(RuntimeException.class, () -> service.buyStock("AAPL", 1));
    }

    @Test void shouldFailWhenQuantityIsNegative() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow, new PercentageFeeStrategy());

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        uow.getPortfolios().add(new Portfolio(1, 1000));

        // Assert
        assertThrows(IllegalArgumentException.class, () -> service.buyStock("AAPL", -1));
    }

    @Test
    void shouldFailWhenTotalCostExceedsBalanceByOneCent() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow, new PercentageFeeStrategy());

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        // 0.01 for lidt
        uow.getPortfolios().add(new Portfolio(1, 101.99));

        // Assert
        assertThrows(IllegalArgumentException.class, () -> service.buyStock("AAPL", 1));
    }
}