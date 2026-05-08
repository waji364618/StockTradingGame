package stockgame.presentation.core;

import javafx.util.Callback;
import stockgame.presentation.controllers.MainMenuController;
import stockgame.presentation.controllers.PortfolioController;
import stockgame.presentation.controllers.StockMarketController;


public class ControllerFactory implements Callback<Class<?>, Object> {

    private final ApplicationContext context;

    //  kun context som parameter - ikke viewManager
    public ControllerFactory(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public Object call(Class<?> clazz) {

        if (clazz == MainMenuController.class) {
            MainMenuController controller = new MainMenuController();
            // henter ViewModel fra context
            controller.setViewModel(context.getMainMenuViewModel());
            return controller;
        }

        if (clazz == StockMarketController.class) {
            StockMarketController controller = new StockMarketController();
            // henter ViewModel fra context
            controller.setViewModel(context.getStockMarketViewModel());
            return controller;
        }

        if (clazz == PortfolioController.class) {
            PortfolioController controller = new PortfolioController();
            // henter ViewModel fra context
            controller.setViewModel(context.getPortfolioViewModel());
            return controller;
        }

        return null;
    }
}
