package View;

import javafx.scene.control.Alert;

public class AlertMessage {

    public static void showAlert(String message){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(message);
            alert.show();
    }
}
