package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;


public class CharactersController {

    public String imageFileNamePlayer = null;
    static MyViewController myViewController;
    static private Stage stage;

    public static void setMyViewController(MyViewController myViewController) {
        CharactersController.myViewController = myViewController;
    }

    public static void setStage(Stage stage) {
        CharactersController.stage = stage;
    }

    public void setImageFileNamePlayerSuper(MouseEvent event)
    {
        imageFileNamePlayer = "./src/Resources/Images/Superman.png";
        myViewController.getMazeDisplayer().setImageFileNamePlayer(imageFileNamePlayer);
    }

    public void setImageFileNamePlayerWonder(MouseEvent event)
    {
        imageFileNamePlayer = "./src/Resources/Images/wonderWoman.png";
        myViewController.getMazeDisplayer().setImageFileNamePlayer(imageFileNamePlayer);
    }

    public void switchToHomePage(MouseEvent event) //switching to scenes from characters to home page
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml")); //the second area
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
