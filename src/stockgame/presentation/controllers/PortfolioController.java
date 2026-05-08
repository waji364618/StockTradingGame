package stockgame.presentation.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import stockgame.domain.OwnedStock;
import stockgame.domain.Transaction;
import stockgame.presentation.viewmodels.PortfolioViewModel;

public class PortfolioController {

    @FXML private Label balanceLabel;
    @FXML private TableView<OwnedStock> ownedStockTable;
    @FXML private TableColumn<OwnedStock, String> ownedSymbolCol;
    @FXML private TableColumn<OwnedStock, Integer> ownedQuantityCol;
    @FXML private TextField sellQuantityField;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> txTypeCol;
    @FXML private TableColumn<Transaction, String> txSymbolCol;
    @FXML private TableColumn<Transaction, Integer> txQuantityCol;
    @FXML private TableColumn<Transaction, Double> txTotalCol;


    private PortfolioViewModel viewModel;

    public void setViewModel(PortfolioViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    private void initialize() {
        if (viewModel != null) {
            viewModel.init(
                    balanceLabel,
                    ownedStockTable, ownedSymbolCol, ownedQuantityCol,
                    transactionTable, txTypeCol, txSymbolCol,
                    txQuantityCol, txTotalCol);
        }
    }

    @FXML
    private void goBack() {
        viewModel.goBack();
    }

    @FXML
    private void sellStock() {
        viewModel.sellStock(sellQuantityField.getText());
    }
}
