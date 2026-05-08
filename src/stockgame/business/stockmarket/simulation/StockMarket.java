package stockgame.business.stockmarket.simulation;

import stockgame.business.events.StockUpdateEvent;
import stockgame.shared.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StockMarket {

    private static StockMarket instance;

    private final List<LiveStock> liveStocks = new ArrayList<>();
    private final List<Consumer<StockUpdateEvent>> listeners = new ArrayList<>();//New, update for assign 5
    private final Logger logger = Logger.getInstance();


    private StockMarket() {
    }


    public static synchronized StockMarket getInstance() {

        if (instance == null) {
            instance = new StockMarket();
        }

        return instance;
    }


    public void addNewStock(String symbol) {

        LiveStock stock = new LiveStock(symbol);

        liveStocks.add(stock);

    logger.log("INFO", "Added stock: " + symbol);

    }

    public void updateAllStocks() {

        for (LiveStock stock : liveStocks) {

    stock.updatePrice();
    logger.log("INFO", stock.getSymbol() + " price=" + stock.getCurrentPrice() + " state=" + stock.getStateName());

            StockUpdateEvent event = new StockUpdateEvent(stock.getSymbol(), stock.getCurrentPrice(), stock.getStateName());//New, update for assignment 5
            notifyListeners(event);
        }
    }


    public List<LiveStock> getLiveStocks() {
        return liveStocks;
    }
    public void addListener(Consumer<StockUpdateEvent> listener) { // New, update for assignment 5
        listeners.add(listener);

    }
    private void notifyListeners(StockUpdateEvent event) { // New, update for assignment 5

        for (Consumer<StockUpdateEvent> listener : listeners) {
            listener.accept(event);
        }

    }

}

