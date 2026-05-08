package unit;

import stockgame.business.stockmarket.services.TradingService;
import stockgame.domain.OwnedStock;
import stockgame.domain.Portfolio;
import stockgame.domain.Stock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TradingServiceSellTest {

    @Test void shouldSellStockSuccessfully() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow);

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        uow.getPortfolios().add(new Portfolio(1, 1000));

        // ejer 3 aktier
        uow.getOwnedStocks().add(new OwnedStock(1, 1, "AAPL", 3));

        // Act
        service.sellStock("AAPL", 2);

        // Assert
        // shares reduceret
        assertEquals(1, uow.getOwnedStocks().get(0).getNumberOfShares());

        // balance steget
        assertTrue(uow.getPortfolios().get(0).getCurrentBalance() > 1000);
    }

    @Test void shouldFailWhenSellQuantityIsZero() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow);

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        uow.getPortfolios().add(new Portfolio(1, 1000));

        uow.getOwnedStocks().add(new OwnedStock(1, 1, "AAPL", 3));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.sellStock("AAPL", 0));
    }

    @Test void shouldFailWhenNotEnoughShares() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow);

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        uow.getPortfolios().add(new Portfolio(1, 1000));

        // ejer kun 2 aktier
        uow.getOwnedStocks().add(new OwnedStock(1, 1, "AAPL", 2));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.sellStock("AAPL", 5));
    }

    @Test void shouldFailWhenStockNotOwned() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow);

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        uow.getPortfolios().add(new Portfolio(1, 1000));

        // Act + Assert
        assertThrows(RuntimeException.class, () -> service.sellStock("AAPL", 1));
    }

    @Test void shouldDeleteOwnedStockWhenAllSharesSold() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow);

        uow.getStocks().add(new Stock("AAPL", "Apple", 100, "Steady"));

        uow.getPortfolios().add(new Portfolio(1, 1000));

        // ejer 2 aktier
        uow.getOwnedStocks().add(new OwnedStock(1, 1, "AAPL", 2));

        // Act
        service.sellStock("AAPL", 2);

        // Assert
        assertEquals(0, uow.getOwnedStocks().size());

        assertTrue(uow.getPortfolios().get(0).getCurrentBalance() > 1000);
    }

    // Query test

    @Test void shouldReturnCorrectBalance() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow);

        uow.getPortfolios().add(new Portfolio(1, 500));

        // Act
        double balance = service.getBalance();

        // Assert
        assertEquals(500, balance);
    }

    @Test void shouldReturnOwnedStocks() {

        // Arrange
        MockUnitOfWork uow = new MockUnitOfWork();
        TradingService service = new TradingService(uow);

        uow.getOwnedStocks().add(new OwnedStock(1, 1, "AAPL", 3));

        // Act
        var list = service.getOwnedStocks();

        // Assert
        assertEquals(1, list.size());
    }
}

