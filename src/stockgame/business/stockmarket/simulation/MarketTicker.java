package stockgame.business.stockmarket.simulation;

import stockgame.shared.configuration.AppConfig;
import stockgame.shared.logging.Logger;

public class MarketTicker implements Runnable{

    private final StockMarket market;

    private final Logger logger = Logger.getInstance();

    private final int updateFrequency;


    public MarketTicker() {

        this.market = StockMarket.getInstance();

        this.updateFrequency = AppConfig.getInstance().getUpdateFrequencyInMs();
    }


    @Override
    public void run() {

        while (true) {

            market.updateAllStocks();

            logger.log("INFO", "Market tick");

            try {

                Thread.sleep(updateFrequency);

            } catch (InterruptedException e) {

                logger.log("ERROR", "Ticker interrupted");
            }
        }
    }
}


