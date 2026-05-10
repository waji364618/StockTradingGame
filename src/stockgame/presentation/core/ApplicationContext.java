package stockgame.presentation.core;

import stockgame.adapters.AlertBoxAdapter;
import stockgame.business.feestrategies.FeeStrategy;
import stockgame.business.feestrategies.PercentageFeeStrategy;
import stockgame.business.stockmarket.services.StockAlertService;
import stockgame.business.stockmarket.services.StockBankruptService;
import stockgame.business.stockmarket.services.StockListenerService;
import stockgame.business.stockmarket.services.TradingService;
import stockgame.persistence.fileimplementation.FileUnitOfWork;
import stockgame.presentation.viewmodels.MainMenuViewModel;
import stockgame.presentation.viewmodels.PortfolioViewModel;
import stockgame.presentation.viewmodels.StockMarketViewModel;

public class ApplicationContext {

    private final TradingService tradingService;
    private final StockListenerService stockListenerService;
    private final NotificationManager notificationManager;
    private final StockBankruptService bankruptService;
    private final StockAlertService alertService;

    // ViewModels oprettes her
    private final MainMenuViewModel mainMenuViewModel;
    private final StockMarketViewModel stockMarketViewModel;
    private final PortfolioViewModel portfolioViewModel;
    private final FileUnitOfWork uow;

    public ApplicationContext(ViewManager viewManager) {
        this.uow = new FileUnitOfWork("data");

        // services
        FeeStrategy feeStrategy = new PercentageFeeStrategy();

        this.tradingService = new TradingService(this.uow, feeStrategy);
        this.stockListenerService = new StockListenerService(this.uow);
        this.notificationManager = new AlertBoxAdapter();
        this.bankruptService = new StockBankruptService(this.uow);
        this.alertService = new StockAlertService();



        // viewmodels oprettes med alle dependencies
        this.mainMenuViewModel = new MainMenuViewModel(
                tradingService, viewManager);

        this.stockMarketViewModel = new StockMarketViewModel(
                tradingService, stockListenerService,
                viewManager, notificationManager);

        this.portfolioViewModel = new PortfolioViewModel(
                tradingService, viewManager, notificationManager);
    }

    public TradingService getTradingService() {
        return tradingService;
    }

    public StockListenerService getStockListenerService() {
        return stockListenerService;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public MainMenuViewModel getMainMenuViewModel() {
        return mainMenuViewModel;
    }

    public StockMarketViewModel getStockMarketViewModel() {
        return stockMarketViewModel;
    }

    public PortfolioViewModel getPortfolioViewModel() {
        return portfolioViewModel;
    }
    public FileUnitOfWork getUow() {
        return uow;
    }
    public StockBankruptService getBankruptService() {
        return bankruptService;
    }

    public StockAlertService getAlertService() {
        return alertService;
    }
}
