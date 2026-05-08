package stockgame.presentation.controllers;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import stockgame.domain.Stock;
import stockgame.presentation.viewmodels.StockMarketViewModel;

public class StockMarketController {

    // chart felter
    @FXML private LineChart<Number, Number> priceChart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;

    // tabel felter
    @FXML private TableView<Stock> stockTable;
    @FXML private TableColumn<Stock, String> symbolCol;
    @FXML private TableColumn<Stock, Double> priceCol;
    @FXML private TableColumn<Stock, String> stateCol;

    // andre felter
    @FXML private Label balanceLabel;
    @FXML private TextField quantityField;
    @FXML private Label messageLabel;

    private StockMarketViewModel viewModel;

    public void setViewModel(StockMarketViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // JavaFX kalder denne automatisk efter @FXML felter er klar
    @FXML
    private void initialize() {
        if (viewModel != null) {
            viewModel.init(
                    balanceLabel,
                    priceChart, xAxis, yAxis,
                    stockTable, symbolCol, priceCol, stateCol,
                    messageLabel
            );
        }
    }

    @FXML
    private void goBack() {
        viewModel.goBack();
    }

    @FXML
    private void goToPortfolio() {
        viewModel.goToPortfolio();
    }

    @FXML
    private void buyStock() {
        viewModel.buyStock(quantityField.getText());
    }
}