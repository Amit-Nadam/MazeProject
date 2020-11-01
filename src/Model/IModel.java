package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.util.Observer;

public interface IModel
{
    void generateMaze(int row, int col, String typeMaze);
    Maze getCurrentMaze();
    Solution getSolution();
    void Exit();
    void assignObserver(Observer o);
    void solveMaze(String searcingAlgo);

    int getCharacterPositionColumn();

    int getCharacterPositionRow();

    boolean canMove(int row, int col);

    void saveMaze(String nameFile);

    boolean loadMaze(String nameFile);

    void setCurrentMaze(Maze maze);


}
