package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.nio.file.Paths;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Home Page
        FXMLLoader fxmlLoaderHome = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent rootHome = fxmlLoaderHome.load();
        primaryStage.setScene(new Scene(rootHome, 283, 425));
        HomePageController.setStage(primaryStage);
        primaryStage.show();
        Music.playGameMusic();

        //MyViewController and connection between observer and observable
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = new MyViewController();
        MyViewController.setStage(primaryStage);
        MazeDisplayer mazeDisplayer = new MazeDisplayer();
        MyViewController.setMazeDisplayer(mazeDisplayer);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        HomePageController.setViewModel(viewModel);

        //Properties
        PropertiesController.setStage(primaryStage);

        //About
        AboutController.setStage(primaryStage);

        //Help
        HelpController.setStage(primaryStage);

        //Instructions
        InstructionsController.setStage(primaryStage);

        //Characters
        CharactersController.setStage(primaryStage);
        CharactersController.setMyViewController(view);

    }

    public static void main(String[] args) {
        launch(args);
    }

}
