package stockgame.business.stockmarket.services;

import stockgame.business.events.StockUpdateEvent;
import stockgame.shared.logging.Logger;

public class StockAlertService {

    private final Logger logger = Logger.getInstance();

    public void onStockUpdate(StockUpdateEvent event){

        if (event.getState().equals("Bankrupt")){

            logger.log("WARNING", "ALERT: Stock bankrupt" + event.getSymbol());

        }

        if (event.getPrice()> 150){

            logger.log("INFO", "ALERT: High price" + event.getSymbol());
        }

    }

}
