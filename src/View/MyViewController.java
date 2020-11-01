package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.Observable;
import java.util.Observer;


public class MyViewController implements IView, Observer {
    static private MyViewModel viewModel;
    static private Maze maze;
    static private Solution mazeSolution;
    static private Stage stage;
    public static MazeDisplayer mazeDisplayer;
    @FXML
    public BorderPane rootPane;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public ChoiceBox<String> choiceBox;
    @FXML
    public ChoiceBox<String> SearchableAlgo;
    @FXML
    public Button generateMaze;
    @FXML
    public Button SolveMaze;
    @FXML
    public Label Maze_rows;
    @FXML
    public Label Maze_columns;
    @FXML
    public Label Maze_Type;
    @FXML
    public MenuItem save;
    @FXML
    public MenuItem load;
    @FXML
    public Pane mazePane;
    @FXML
    public MenuItem music;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public static void setStage(Stage stage) {
        MyViewController.stage = stage;
    }

    public static void setMazeDisplayer(MazeDisplayer mazeDisplayer) {
        if(MyViewController.mazeDisplayer == null)
            MyViewController.mazeDisplayer = mazeDisplayer;
    }

    public static MazeDisplayer getMazeDisplayer() {
        return mazeDisplayer;
    }

    public void initialize() { //the display of the game, how it looks like

        SolveMaze.setVisible(false); //not show this button
        SearchableAlgo.setVisible(false);
        save.setVisible(false);
        mazeDisplayer.widthProperty().bind(mazePane.widthProperty());
        mazeDisplayer.heightProperty().bind(mazePane.heightProperty());
        mazeDisplayer.setImageFileNameWall("./src/Resources/Images/GreyWall.jpg");
        mazeDisplayer.changeCharacter();
        mazeDisplayer.setImageFileNameTarget("./src/Resources/Images/Target.png");
        mazeDisplayer.setOnScroll(event -> Zoom());
        mazeDisplayer.setOnKeyPressed(event -> keyPressed(event));
        if(mazePane != null)
            mazePane.getChildren().add(mazeDisplayer);
    }

    @Override
    public void generateMaze() { //creating the maze by the servers and show it in the canvas
        //6*25 - height // 176 + row*2, Max(col*2,6*25) - window //  row*2, Max(col*2,6*25)
        mazeDisplayer.setScaleX(1);
        mazeDisplayer.setScaleY(1);

        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        String typeMaze = choiceBox.getValue(); //get the value of type;
        textField_mazeRows.textProperty().bindBidirectional(viewModel.setRowProperty(textField_mazeRows.getText()));
        textField_mazeColumns.textProperty().bindBidirectional(viewModel.setColProperty(textField_mazeColumns.getText()));
        if(typeMaze !=null)
            choiceBox.valueProperty().bindBidirectional(viewModel.setChoiceBoxProperty(choiceBox.getValue()));

        Maze maze = mazeDisplayer.getMaze();
        if (maze != null) {
            mazeSolution = null;
            mazeDisplayer.setSolution(null);
        }
        maze = viewModel.generateMaze(rows, cols, typeMaze);
        this.maze = maze;
        mazeDisplayer.setMaze(maze);
        if (maze == null)
            return;

        //draw maze of the maze that we get from the server
        mazeDisplayer.drawMaze();
        //not show those buttons
        SolveMaze.setVisible(true);
        SearchableAlgo.setVisible(true);
        save.setVisible(true);
        Music.stopGameMusic();
        Music.playGameMusic();
    }

    @Override
    public void solveMaze() { //solving the maze which is now playing and show the path ot the target
        String algorithmSolve = SearchableAlgo.getValue();
        if(algorithmSolve != null)
            mazeSolution = viewModel.solveMaze(algorithmSolve);
        mazeDisplayer.setMazeSolution(mazeSolution);
        mazeDisplayer.setImageFileNameRoad("./src/Resources/Images/steps.png");
        mazeDisplayer.drawMaze();
        Music.stopGameMusic();
        Music.playGameMusic();
    }


    public void switchSceneProperties(ActionEvent event) {  //switch between scenes - menu to properties
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Properties.fxml")); //the second area
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
            Music.stopGameMusic();
            Music.playGameMusic();
        } catch (IOException e) {
            AlertMessage.showAlert("Can't switch to properties");
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) { //this is moving the player in the maze by the rules and show alert when it illegal move
        int row_player = mazeDisplayer.getRow_player();
        int col_player = mazeDisplayer.getCol_player();
        switch (keyEvent.getCode()) {

            //the right numbers in keyboard and the top numbers in keyboard
            case DIGIT2: case NUMPAD2:  //down
                if (viewModel.canMove(row_player + 1, col_player)) { //check if this move is ok
                    mazeDisplayer.set_player_position(row_player + 1, col_player);
                } else {
                    AlertMessage.showAlert("This move isn't valid");
                    Music.playCrash();
                }
                break;
            case DIGIT8: case NUMPAD8:  //up
                if (viewModel.canMove(row_player - 1, col_player)) {
                    mazeDisplayer.set_player_position(row_player - 1, col_player);
                } else {
                    AlertMessage.showAlert("This move isn't valid");
                    Music.playCrash();
                }
                break;
            case DIGIT6: case NUMPAD6: //right
                if (viewModel.canMove(row_player, col_player + 1)) {
                    mazeDisplayer.set_player_position(row_player, col_player + 1);
                } else {
                    AlertMessage.showAlert("This move isn't valid");
                    Music.playCrash();
                }
                break;
            case DIGIT4: case NUMPAD4: //left
                if (viewModel.canMove(row_player, col_player - 1)) {
                    mazeDisplayer.set_player_position(row_player, col_player - 1);
                } else {
                    AlertMessage.showAlert("This move isn't valid");
                    Music.playCrash();
                }
                break;
            case DIGIT3: case NUMPAD3: //right down diagonal line
                if (viewModel.canMove(row_player + 1, col_player + 1)) {
                    mazeDisplayer.set_player_position(row_player + 1, col_player + 1);
                } else {
                    AlertMessage.showAlert("This move isn't valid");
                    Music.playCrash();
                }
                break;
            case DIGIT1: case NUMPAD1: //left down diagonal line
                if (viewModel.canMove(row_player + 1, col_player - 1)) {
                    mazeDisplayer.set_player_position(row_player + 1, col_player - 1);
                } else {
                    AlertMessage.showAlert("This move isn't valid");
                    Music.playCrash();
                }
                break;
            case DIGIT7: case NUMPAD7: //left up diagonal line
                if (viewModel.canMove(row_player - 1, col_player - 1)) {
                    mazeDisplayer.set_player_position(row_player - 1, col_player - 1);
                } else {
                    AlertMessage.showAlert("This move isn't valid");
                    Music.playCrash();
                }
                break;
            case DIGIT9: case NUMPAD9: //right up diagonal line
                if (viewModel.canMove(row_player - 1, col_player + 1)) {
                    mazeDisplayer.set_player_position(row_player - 1, col_player + 1);
                } else {
                    AlertMessage.showAlert("This move isn't valid");
                    Music.playCrash();
                }
                break;
            default:
                mazeDisplayer.set_player_position(row_player, col_player);
        }
        keyEvent.consume();

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void Exit(MouseEvent event) { //exit from the game
        viewModel.Exit();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @Override
    public void saveMaze(ActionEvent actionEvent) { //saving the maze in your computer
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Maze");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("maze file", "*.maze"));
        File file = fileChooser.showSaveDialog(stage);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(mazeDisplayer.getMaze());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e){
            AlertMessage.showAlert("Can't save maze");
        }
    }

    @Override
    public void loadMaze(ActionEvent actionEvent){ //loading the maze from the computer
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Maze");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("maze file", "*.maze"));
            File file = fileChooser.showOpenDialog(stage);
            try {
                if(file == null)
                {
                    AlertMessage.showAlert("There isn't a file. Please try again");
                    return;
                }
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                Object object = inputStream.readObject();
                viewModel.setCurrentMaze((Maze)object);
                SolveMaze.setVisible(true); //not show this button
                SearchableAlgo.setVisible(true);
                save.setVisible(true);
                if(!(object instanceof Maze)) {
                    AlertMessage.showAlert("In the file there isn't a maze");
                    return;
                }
                mazeDisplayer.setMaze((Maze)object);
                mazeDisplayer.drawMaze();
            } catch (IOException | ClassNotFoundException e){
                AlertMessage.showAlert("Can't load maze");
            }
    }

    @Override
    public void update(Observable o, Object arg) { //this is the update of changing in observable
        if (o instanceof MyViewModel) {
            Maze maze = mazeDisplayer.getMaze();
            if (maze == null || !maze.equals(viewModel.getCurrentMaze())) { //new maze or loading
                maze = viewModel.getCurrentMaze();
                mazeDisplayer.setMaze(maze);
                mazeDisplayer.drawMaze();
            }
        }
    }

    public  void switchSceneHome(MouseEvent event) //switch between scenes - menu to home
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml")); //the second area
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            mazeDisplayer.setMaze(null);
            stage.show();
            Music.stopGameMusic();
            Music.playGameMusic();
        }catch(IOException e){
            AlertMessage.showAlert("Can't switch to home page");
        }
    }

    public void switchSceneAbout(MouseEvent event) {//switch between scenes - menu to about
        try {
            Parent root = FXMLLoader.load(getClass().getResource("About.fxml")); //the second area
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
            Music.stopGameMusic();
            Music.playGameMusic();
        } catch (IOException e) {
            AlertMessage.showAlert("Can't switch to about");
        }
    }

    public void switchSceneHelp(MouseEvent event) {//switch between scenes - menu to help
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Help.fxml")); //the second area
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
            Music.stopGameMusic();
            Music.playGameMusic();
        } catch (IOException e) {
            AlertMessage.showAlert("Can't switch to help");
        }
    }

    @Override
    public void Zoom() //zoom of the maze
    {
        if(mazeDisplayer.getMaze() != null)
        {
            ScrollPane scrollPane = new ScrollPane(new Group(mazeDisplayer));
            rootPane.setCenter(scrollPane);
            mazeDisplayer.setOnScroll((event) -> {double x = mazeDisplayer.getScaleX();

            double y = mazeDisplayer.getScaleY();
            if (x <= 0.8 && y <= 0.8) { x = 0.8; y = 0.8; }
            if (x >= 2 && y >= 2) { x = 2; y = 2; } mazeDisplayer.setScaleX(x + event.getDeltaY() / 800);
            mazeDisplayer.setScaleY(y + event.getDeltaY() / 800); });
        }

    }

    public void setMusic(ActionEvent event) { //set the on/off music
        if(Music.isPlay()){
            music.setText("sound-on");
        }
        else
            music.setText("sound-off");
        Music.changeSoundGame(); //booleam
    }


}

