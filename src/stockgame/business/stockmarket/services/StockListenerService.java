package stockgame.business.stockmarket.services;

import stockgame.business.events.StockUpdateEvent;
import stockgame.domain.Stock;
import stockgame.persistence.fileimplementation.FileStockDao;
import stockgame.persistence.fileimplementation.FileUnitOfWork;
import stockgame.shared.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StockListenerService {

    private final FileUnitOfWork uow;
    private final FileStockDao stockDao;
    private final Logger logger = Logger.getInstance();
    private final List<Consumer<StockUpdateEvent>> listeners = new ArrayList<>();


    public StockListenerService(FileUnitOfWork uow){

        this.uow = uow;
        this.stockDao = new FileStockDao(uow);
    }
    public void onStockUpdate(StockUpdateEvent event) {
        logger.log("INFO", "EVENT RECEIVED " + event.getSymbol());//

        uow.beginTransaction();

        Stock stock = stockDao.getBySymbol(event.getSymbol());

        if (stock != null) {

            stock.setCurrentPrice(event.getPrice());
            stock.setCurrentState(event.getState());

            stockDao.update(stock);

            logger.log("INFO", "Stock updated in file: " + stock.getSymbol());
        }

        uow.commit();
        notifyListeners(event);
    }

     public void addListener(Consumer<StockUpdateEvent> listener) {

         listeners.add(listener);
     }

    private void notifyListeners(StockUpdateEvent event) {

        for (Consumer<StockUpdateEvent> listener : listeners) {
            listener.accept(event);
        }
    }

}
