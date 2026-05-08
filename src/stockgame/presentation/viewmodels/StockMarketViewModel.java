package stockgame.presentation.viewmodels;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import stockgame.business.stockmarket.services.StockListenerService;
import stockgame.business.stockmarket.services.TradingService;
import stockgame.domain.Stock;
import stockgame.presentation.core.NotificationManager;
import stockgame.presentation.core.ViewManager;
import java.util.HashMap;
import java.util.Map;

public class StockMarketViewModel {


    private final TradingService tradingService;
    private final StockListenerService stockListenerService;
    private final ViewManager viewManager;
    private final NotificationManager notificationManager;

    // tabel data
    private Label balanceLabel;
    private TableView<Stock> stockTable;
    private Label messageLabel;
    private ObservableList<Stock> stockList =
            FXCollections.observableArrayList();

    // chart data - en serie per aktie symbol
    private final Map<String, XYChart.Series<Number, Number>> seriesMap
            = new HashMap<>();

    // tæller til x-aksen - går op med 1 hver gang markedet opdaterer
    private int tick = 0;

    // max antal punkter på grafen før vi fjerner de ældste
    private static final int MAX_POINTS = 50;

    public StockMarketViewModel(TradingService tradingService, StockListenerService stockListenerService,
                                ViewManager viewManager, NotificationManager notificationManager) {
        this.tradingService = tradingService;
        this.stockListenerService = stockListenerService;
        this.viewManager = viewManager;
        this.notificationManager = notificationManager;
    }

    public void init(Label balanceLabel,
                     LineChart<Number, Number> priceChart,
                     NumberAxis xAxis,
                     NumberAxis yAxis,
                     TableView<Stock> stockTable,
                     TableColumn<Stock, String> symbolCol,
                     TableColumn<Stock, Double> priceCol,
                     TableColumn<Stock, String> stateCol,
                     Label messageLabel) {

        this.balanceLabel = balanceLabel;
        this.stockTable   = stockTable;
        this.messageLabel = messageLabel;

        // setup chart udseende
        xAxis.setLabel("Tid");
        yAxis.setLabel("Pris (kr.)");
        priceChart.setCreateSymbols(false);
        priceChart.setAnimated(false);

        // ryd chart og series FØR vi tilføjer nye
        priceChart.getData().clear();
        seriesMap.clear();
        tick = 0;

        // opret en linje per aktie i grafen
        for (Stock stock : tradingService.getAvailableStocks()) {

            XYChart.Series<Number, Number> series =
                    new XYChart.Series<>();

            series.setName(stock.getSymbol());
            seriesMap.put(stock.getSymbol(), series);
            priceChart.getData().add(series);
        }

        // setup tabel kolonner
        symbolCol.setCellValueFactory(
                new PropertyValueFactory<>("symbol"));
        stateCol.setCellValueFactory(
                new PropertyValueFactory<>("currentState"));

        // pris kolonne - formater til 2 decimaler
        priceCol.setCellValueFactory(
                new PropertyValueFactory<>("currentPrice"));
        priceCol.setCellFactory(col -> new TableCell<Stock, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f kr.", price));
                }
            }
        });

        // indlæs aktier i tabel
        stockList.setAll(tradingService.getAvailableStocks());
        stockTable.setItems(stockList);

        // vis balance
        updateBalance();

        // lyt til live opdateringer fra StockListenerService
        stockListenerService.addListener(event -> {
            Platform.runLater(() -> {

                // opdater tabel
                stockList.setAll(tradingService.getAvailableStocks());
                updateBalance();

                // tilføj nyt punkt til den rigtige aktie linje
                XYChart.Series<Number, Number> series =
                        seriesMap.get(event.getSymbol());

                if (series != null) {
                    tick++;
                    series.getData().add(
                            new XYChart.Data<>(tick, event.getPrice()));

                    // fjern ældste punkt hvis grafen er for lang
                    if (series.getData().size() > MAX_POINTS) {
                        series.getData().remove(0);
                    }
                }
            });
        });
    }

    public void buyStock(String input) {

        try {
            int quantity = Integer.parseInt(input);

            // hent valgt aktie fra tabellen
            Stock selected = stockTable
                    .getSelectionModel()
                    .getSelectedItem();

            if (selected == null) {
                notificationManager.showError("Vælg en aktie i tabellen!");
                return;
            }

            tradingService.buyStock(selected.getSymbol(), quantity);

            updateBalance();
            notificationManager.showInfo("Købt " + quantity
                    + " aktier af " + selected.getSymbol());

            if (tradingService.hasWon()) {
                notificationManager.showInfo("Tillykke! Du har vundet! "
                        + "Balance: " + tradingService.getBalance());
            }

        } catch (NumberFormatException e) {
            notificationManager.showError("Skriv et gyldigt antal!");
        }
    }

    private void updateBalance() {
        balanceLabel.setText("Balance: "
                + String.format("%.0f", tradingService.getBalance())
                + " kr.");
    }

    public void goBack() {
        viewManager.showView("mainmenu.fxml");
    }

    public void goToPortfolio() {
        viewManager.showView("portfolio.fxml");
    }
}
