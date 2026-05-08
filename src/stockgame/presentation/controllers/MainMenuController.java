package stockgame.presentation.controllers;

import javafx.fxml.FXML;
import stockgame.presentation.viewmodels.MainMenuViewModel;

public class MainMenuController {

    private MainMenuViewModel viewModel;

    public void setViewModel(MainMenuViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    private void newGame() {
        viewModel.newGame();
    }

    @FXML
    private void goToMarket() {
        viewModel.goToMarket();
    }

    @FXML
    private void goToPortfolio() {
        viewModel.goToPortfolio();
    }
}


