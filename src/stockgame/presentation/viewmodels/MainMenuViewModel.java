package stockgame.presentation.viewmodels;

import stockgame.business.stockmarket.services.TradingService;
import stockgame.presentation.core.ViewManager;

public class MainMenuViewModel {


    private final ViewManager viewManager;

    public MainMenuViewModel(TradingService tradingService,
                             ViewManager viewManager) {

        this.viewManager = viewManager;
    }

    public void newGame() {
        viewManager.showView("stockmarket.fxml");
    }

    public void goToMarket() {
        viewManager.showView("stockmarket.fxml");
    }

    public void goToPortfolio() {
        viewManager.showView("portfolio.fxml");
    }
}
