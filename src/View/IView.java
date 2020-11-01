package View;


import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public interface IView  {

    void saveMaze(ActionEvent actionEvent);
    void generateMaze();
    void solveMaze();
    void keyPressed(KeyEvent keyEvent);
    void mouseClicked(MouseEvent mouseEvent);
    void Exit(MouseEvent event);
    void loadMaze(ActionEvent actionEvent);
    void Zoom();
}
