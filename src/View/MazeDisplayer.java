package View;


import algorithms.mazeGenerators.*;
import algorithms.search.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas{

    static private Maze maze;
    static private Solution mazeSolution;
    static private int row_player;
    static private int col_player;

    //Images
    public StringProperty imageFileNameWall = new SimpleStringProperty();
    public StringProperty imageFileNamePlayer = new SimpleStringProperty();
    public StringProperty imageFileNameRoad = new SimpleStringProperty();
    public StringProperty imageFileNameTarget = new SimpleStringProperty();
    public StringProperty getImageFileMazeBackGround = new SimpleStringProperty();


    public void setGetImageFileMazeBackGround(String getImageFileMazeBackGround) {
        this.getImageFileMazeBackGround.set(getImageFileMazeBackGround);
    }

    public static void setSolution(Solution solution)
    {
        MazeDisplayer.mazeSolution = solution;
    }

    public void setImageFileNameRoad(String imageFileNameRoad) {
        this.imageFileNameRoad.set(imageFileNameRoad);
    }

    public MazeDisplayer(){
        MazeDisplayer.maze = null;
        MazeDisplayer.mazeSolution = null;
        MazeDisplayer.row_player=0;
        MazeDisplayer.col_player=0;
        setImageFileNamePlayer("./src/Resources/Images/wonderWoman.png");
        //this code will change the canvas size and then we need to enlarge the maze
        widthProperty().addListener(e-> drawMaze());
        heightProperty().addListener(e-> drawMaze());

    }

    //setters and getters

    public static int getRow_player() {
        return row_player;
    }

    public static int getCol_player() {
        return col_player;
    }

    public void set_player_position(int row, int col){
        MazeDisplayer.row_player = row;
        MazeDisplayer.col_player = col;
        drawMaze();
    }

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze){
        MazeDisplayer.maze = maze;
        if(maze != null) {
            MazeDisplayer.row_player = maze.getStartPosition().getRowIndex();
            MazeDisplayer.col_player = maze.getStartPosition().getColumnIndex();
        }
    }

    public Solution getMazeSolution(){
        return mazeSolution;
    }

    public static void setMazeSolution(Solution mazeSolution){
        MazeDisplayer.mazeSolution = mazeSolution;
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNameCharacter) {
        this.imageFileNamePlayer.set(imageFileNameCharacter);
    }

    public String getImageFileNameRoad() {
        return imageFileNameRoad.get();
    }

    public String getImageFileNameTarget() {
        return imageFileNameTarget.get();
    }

    public void setImageFileNameTarget(String imageFileNameTarget) {
        this.imageFileNameTarget.set(imageFileNameTarget);
    }


    public void drawMaze() //draw maze
    {
        if(maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = MazeDisplayer.maze.getMaze().length;
            int col = MazeDisplayer.maze.getMaze()[0].length;
            double cellHeight = canvasHeight / row;
            double cellWidth = canvasWidth / col;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.BLUE);
            double w, h;

            //Draw Maze

            Image wallImage = null; //draw wall
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            } catch (FileNotFoundException e) {
               AlertMessage.showAlert("Not found wall image....");
            }

            for (int i = 0; i < maze.getMaze().length; i++) {
                for (int j = 0; j < maze.getMaze()[0].length; j++) {
                    if (maze.getMaze()[i][j] == 1) {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null) {
                            graphicsContext.fillRect(w, h, cellWidth, cellHeight);
                        } else {
                            graphicsContext.drawImage(wallImage, w, h, cellWidth, cellHeight);
                        }
                    }
                }
            }

            //place the player image
            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            Image playerImage = null;
            try {
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            } catch (FileNotFoundException e) {
                AlertMessage.showAlert("Not found Image player....");
            }
            graphicsContext.drawImage(playerImage, w_player, h_player, cellWidth, cellHeight);

            //place the target image
            Position goalPos = maze.getGoalPosition();
            double h_target = goalPos.getRowIndex() * cellHeight;
            double w_target = goalPos.getColumnIndex() * cellWidth;
            Image targetImage = null;
            try {
                targetImage = new Image(new FileInputStream(getImageFileNameTarget()));
            } catch (FileNotFoundException e) {
                AlertMessage.showAlert("Not found Image target....");
            }
            graphicsContext.drawImage(targetImage, w_target, h_target, cellWidth, cellHeight);
            graphicsContext.drawImage(playerImage, w_player, h_player, cellWidth, cellHeight); //draw the image on the target


            //draw road
            h_player = getRow_player() * cellHeight;
            w_player = getCol_player() * cellWidth;
            if (mazeSolution != null) {
                ArrayList<AState> list = mazeSolution.getSolutionPath();
                Image roadImage = null;
                try {
                    roadImage = new Image(new FileInputStream(getImageFileNameRoad()));
                } catch (FileNotFoundException e) {
                   AlertMessage.showAlert("Not found Image road....");
                }
                for (AState state : list) {
                    MazeState mState = (MazeState) state;
                    Position pos = mState.getState();
                    int rowState = pos.getRowIndex();
                    int colState = pos.getColumnIndex();
                    h = rowState * cellHeight;
                    w = colState * cellWidth;
                    if (roadImage == null) {
                        graphicsContext.fillRect(w, h, cellWidth, cellHeight); //color
                    } else {
                        graphicsContext.drawImage(roadImage, w, h, cellWidth, cellHeight);
                    }
                }
            }


            int goalCol = maze.getGoalPosition().getColumnIndex();
            int goalRow = maze.getGoalPosition().getRowIndex();
            if (col_player == goalCol && row_player == goalRow)
                Music.playTarget();
        }
    }

    public void changeCharacter() { //change character indeed for what the player choose
        this.setImageFileNamePlayer(imageFileNamePlayer.getValue());
    }
}
