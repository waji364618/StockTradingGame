package stockgame;

import javafx.application.Application;
import javafx.stage.Stage;
import stockgame.business.stockmarket.simulation.MarketTicker;
import stockgame.business.stockmarket.simulation.StockMarket;
import stockgame.domain.Stock;
import stockgame.persistence.fileimplementation.FileStockDao;
import stockgame.presentation.core.ApplicationContext;
import stockgame.presentation.core.ControllerFactory;
import stockgame.presentation.core.ViewManager;

public class MainApp extends Application{
    @Override
    public void start(Stage stage) {

        // 1. ViewManager
        ViewManager viewManager = new ViewManager(stage);

        // 2. ApplicationContext
        ApplicationContext context = new ApplicationContext(viewManager);

        // 3. ControllerFactory
        ControllerFactory factory = new ControllerFactory(context);
        viewManager.setControllerFactory(factory);

        // 4. StockMarket setup
        StockMarket market = StockMarket.getInstance();

        if (context.getTradingService().getAvailableStocks().isEmpty()) {

            // brug context UoW (IKKE new FileUnitOfWork)
            FileStockDao stockDao = new FileStockDao(context.getUow());

            context.getUow().beginTransaction();

            stockDao.create(new Stock("AAPL", "Apple", 100.0, "Steady"));
            stockDao.create(new Stock("TSLA", "Tesla", 100.0, "Steady"));
            stockDao.create(new Stock("MSFT", "Microsoft", 100.0, "Steady"));

            context.getUow().commit();

            market.addNewStock("AAPL");
            market.addNewStock("TSLA");
            market.addNewStock("MSFT");

        } else {

            for (Stock stock : context.getTradingService().getAvailableStocks()) {
                market.addNewStock(stock.getSymbol());
            }
        }

        // 5. KOBL ALLE SERVICES
        market.addListener(event ->
                context.getStockListenerService().onStockUpdate(event));

        market.addListener(event ->
                context.getBankruptService().onStockUpdate(event));

        market.addListener(event ->
                context.getAlertService().onStockUpdate(event));

        // 6. Start ticker
        Thread ticker = new Thread(new MarketTicker());
        ticker.setDaemon(true);
        ticker.start();

        // 7. Initialiser spil
        context.getTradingService().initializeGame();

        // 8. Start UI
        viewManager.showView("mainmenu.fxml");
        stage.setTitle("Stock Trading Game");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


