package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class InstructionsController {

    static private Stage stage;

    public static void setStage(Stage stage) {
        InstructionsController.stage = stage;
    }

    public void switchHomePage(ActionEvent event)  //switching scenes from home page to instructions
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/HomePage.fxml")); //the second area
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
            Music.stopGameMusic();
            Music.playGameMusic();

        } catch (IOException e) {
            AlertMessage.showAlert("Can't switch to home page");
        }
    }

}
