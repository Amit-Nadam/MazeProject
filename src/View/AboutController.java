package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AboutController{

    static private Stage stage;

    public static void setStage(Stage primaryStage) {
        AboutController.stage = primaryStage;
    }

    public void switchMenu(ActionEvent event)  //switching from about to menu
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/MyView.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
            Music.stopGameMusic();
            Music.playGameMusic();
        } catch (IOException e) {
            AlertMessage.showAlert("Can't switch to menu");
        }
    }
}
