package stockgame.adapters;

import stockgame.library.CustomAlertBox;
import stockgame.presentation.core.NotificationManager;

public class AlertBoxAdapter  implements NotificationManager {

    private final CustomAlertBox alertBox;

    public AlertBoxAdapter() {
        this.alertBox = new CustomAlertBox();
    }

    @Override
    public void showInfo(String message) {

        alertBox.showAlert(
                message,
                "Stock Trading Game",
                CustomAlertBox.AlertType.INFO
        );
    }

    @Override
    public void showError(String message) {

        alertBox.showAlert(
                message,
                "Stock Trading Game",
                CustomAlertBox.AlertType.ERROR
        );
    }


}
