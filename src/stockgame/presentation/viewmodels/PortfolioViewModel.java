package stockgame.presentation.viewmodels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import stockgame.business.stockmarket.services.TradingService;
import stockgame.domain.OwnedStock;
import stockgame.domain.Transaction;
import stockgame.presentation.core.NotificationManager;
import stockgame.presentation.core.ViewManager;

public class PortfolioViewModel {

    private final TradingService tradingService;
    private final ViewManager viewManager;

    private Label balanceLabel;
    private TableView<OwnedStock> ownedStockTable;
    private TableView<Transaction> transactionTable;


    private ObservableList<OwnedStock> ownedList =
            FXCollections.observableArrayList();
    private ObservableList<Transaction> transactionList =
            FXCollections.observableArrayList();
    private final NotificationManager notificationManager;

    public PortfolioViewModel(TradingService tradingService,
                              ViewManager viewManager, NotificationManager notificationManager) {
        this.tradingService = tradingService;
        this.viewManager = viewManager;
        this.notificationManager = notificationManager;
    }

    public void init(Label balanceLabel,
                     TableView<OwnedStock> ownedTable,
                     TableColumn<OwnedStock, String> symbolCol,
                     TableColumn<OwnedStock, Integer> quantityCol,
                     TableView<Transaction> txTable,
                     TableColumn<Transaction, String> txTypeCol,
                     TableColumn<Transaction, String> txSymbolCol,
                     TableColumn<Transaction, Integer> txQuantityCol,
                     TableColumn<Transaction, Double> txTotalCol) {

        this.balanceLabel = balanceLabel;
        this.ownedStockTable = ownedTable;
        this.transactionTable = txTable;


        // sæt kolonner op
        symbolCol.setCellValueFactory(new PropertyValueFactory<>("stockSymbol"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("numberOfShares"));

        txTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        txSymbolCol.setCellValueFactory(new PropertyValueFactory<>("stockSymbol"));
        txQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        txTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        txTotalCol.setCellFactory(col -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f kr.", total));
                }
            }
        });

        // indlæs data
        refreshData();
    }

    public void sellStock(String input) {

        try {
            int quantity = Integer.parseInt(input);

            OwnedStock selected = ownedStockTable
                    .getSelectionModel().getSelectedItem();

            if (selected == null) {
                notificationManager.showError("Vælg en aktie i tabellen!");
                return;
            }
            // tjek om man har nok aktier FØR man sælger
            if (selected.getNumberOfShares() < quantity) {
                notificationManager.showError("Du ejer kun "
                        + selected.getNumberOfShares()
                        + " aktier af "
                        + selected.getStockSymbol());
                return;
            }

            tradingService.sellStock(selected.getStockSymbol(), quantity);

            refreshData();
            notificationManager.showInfo("Solgt " + quantity
                    + " aktier af " + selected.getStockSymbol());

            if (tradingService.hasWon()) {
                notificationManager.showInfo("Tillykke! Du har vundet! "
                        + "Din balance er over 50.000 kr.!");
            }

        } catch (NumberFormatException e) {
            notificationManager.showError("Skriv et gyldigt antal!");
        }
    }

    private void refreshData() {
        ownedList.clear();
        ownedList.addAll(tradingService.getOwnedStocks());
        ownedStockTable.setItems(ownedList);

        transactionList.clear();
        transactionList.addAll(tradingService.getTransactions());
        transactionTable.setItems(transactionList);

        balanceLabel.setText("Balance: "
                + String.format("%.0f", tradingService.getBalance())
                + " kr.");
    }

    public void goBack() {
        viewManager.showView("stockmarket.fxml");
    }
}
