package stockgame.presentation.core;

import javafx.scene.control.Alert;

public class AlertNotificationManager implements NotificationManager {

    @Override
    public void showInfo(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Stock Trading Game");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Stock Trading Game");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
