package View;

import Server.Configurations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class PropertiesController {

    static private Stage stage;
    @FXML
    private AnchorPane rootPane;
    @FXML
    public Label Type_Maze;
    @FXML
    public Label SearchingAlgorithm;
    @FXML
    public Label numThreads;
    @FXML
    public TextField textField_typeMaze;
    @FXML
    public TextField textField_searchingAlgo;
    @FXML
    public TextField textField_numThreads;

    public static void setStage(Stage stage) {
        PropertiesController.stage = stage;
    }

    public void initialize()
    {
        textField_typeMaze.setText(Configurations.getTypeMaze().getName()); //type maze
        textField_searchingAlgo.textProperty().setValue(Configurations.getSearchAlgorithm().getName());
        textField_numThreads.textProperty().setValue(String.valueOf(Configurations.getNumberThreads()));
    }

    public  void switchSceneMenu(ActionEvent event) { //switching scenes from properties to menu
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml")); //the second area
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
