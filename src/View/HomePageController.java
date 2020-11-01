package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class HomePageController {

    static private Stage stage;
    static private MyViewModel viewModel;
    @FXML
    public AnchorPane mainPane;

    public static void setViewModel(MyViewModel viewModel) {
        HomePageController.viewModel = viewModel;
    }

    public static void setStage(Stage stage) {
        HomePageController.stage = stage;
    }

    public void switchMenuStage(MouseEvent event) //switch scenes from Home Page to play
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/MyView.fxml")); //the second area
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
            Music.stopGameMusic();
            Music.playGameMusic();

        } catch (IOException e) {
            AlertMessage.showAlert("Can't switch to menu");
        }
    }

    public void switchInstructions(MouseEvent event)  //switching scenes from home page to instructions
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/Instructions.fxml")); //to instruction
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
            Music.stopGameMusic();
            Music.playGameMusic();

        } catch (IOException e) {
            AlertMessage.showAlert("Can't switch to instructions");
        }
    }

    public void switchCharacters(MouseEvent event)  //switching scenes from home page to instructions
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/Characters.fxml")); //to instruction
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
            Music.stopGameMusic();
            Music.playGameMusic();

        } catch (IOException e) {
            AlertMessage.showAlert("Can't switch to characters");
        }
    }

    public void Exit(MouseEvent event) { //exit of the game
        viewModel.Exit();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
