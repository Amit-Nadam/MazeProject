package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {


    private IModel myModel;
    private Maze currentMaze;
    private StringProperty rowProperty;
    private StringProperty colProperty;
    private StringProperty choiceBoxProperty;
    private StringProperty searchAlgoProperty;
    private StringProperty typeMaze;
    private int currentRowPlayerIndex;
    private int currentColPlayerIndex;
    public StringProperty characterRowPlayerPosition;
    public StringProperty characterColPlayerPosition;

    public MyViewModel(IModel model)
    {
        this.myModel = model;
        this.myModel.assignObserver(this);
        this.rowProperty = new SimpleStringProperty();
        this.colProperty = new SimpleStringProperty();
        this.choiceBoxProperty = new SimpleStringProperty();
        this.searchAlgoProperty = new SimpleStringProperty();
        this.typeMaze = new SimpleStringProperty();
        this.characterRowPlayerPosition = new SimpleStringProperty();
        this.characterColPlayerPosition = new SimpleStringProperty();
    }

    public Maze getCurrentMaze() {
        return currentMaze;
    }

    public String setTypeMaze(String typeMaze) {
        this.typeMaze.set(typeMaze);
        return this.typeMaze.getValue();
    }

    public String setSearchAlgoProperty(String searchAlgoProperty) {
        this.searchAlgoProperty.set(searchAlgoProperty);
        return this.searchAlgoProperty.getValue();
    }

    public StringProperty setRowProperty(String mazeRow){
        this.rowProperty.setValue(mazeRow);
        return this.rowProperty;
    }

    public StringProperty setColProperty(String mazeCol){
        this.colProperty.setValue(mazeCol);
        return this.colProperty;
    }

    public int setRowPropertyIndex(String rowPlayer)
    {
        this.currentRowPlayerIndex = Integer.parseInt(rowPlayer);
        return this.currentRowPlayerIndex;
    }

    public int setColPropertyIndex(String colPlayer)
    {
        this.currentColPlayerIndex = Integer.parseInt(colPlayer);
        return this.currentColPlayerIndex;
    }


    public String getRowProperty() {
        return rowProperty.get();
    }

    public StringProperty rowPropertyProperty() {
        return rowProperty;
    }

    public int getCurrentRowPlayerIndex() {
        return currentRowPlayerIndex;
    }

    public int getCurrentColPlayerIndex() {
        return currentColPlayerIndex;
    }

    public IModel getMyModel() {
        return myModel;
    }

    public String getCharacterRowPlayerPosition() {
        return characterRowPlayerPosition.get();
    }


    public String getCharacterColPlayerPosition() {
        return characterColPlayerPosition.get();
    }

    public StringProperty setChoiceBoxProperty(String choiceboxProperty) {
        this.choiceBoxProperty.set(choiceboxProperty);
        return this.choiceBoxProperty;
    }

    public void Exit()
    {
        myModel.Exit();
    }

    public Maze generateMaze(int rows, int cols, String typeMaze) {
        boolean flag = checkProperties();
        if(!flag)
            showAlert("Invalid value - Try Again!");
        else //maze generate from model
        {
          //  myModel.generateMaze(Integer.parseInt(rowProperty.getValue()), Integer.parseInt(colProperty.getValue()), choiceBoxProperty.getValue());
            myModel.generateMaze(rows, cols, typeMaze);
            setChanged();
            notifyObservers();
            return myModel.getCurrentMaze();
        }
        return null;
    }

    public boolean checkProperties()
    {
        try {
            int row = Integer.parseInt(rowProperty.getValue());
            int col = Integer.parseInt(colProperty.getValue());
            if (row < 2 || col < 2)
                return false;
            return true;
        }
        catch(NumberFormatException e){
            showAlert("Invalid value");
        }
        return false;
    }

    public void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);;
        alert.show();
    }

    public Solution solveMaze(String algorithmSolve)
    {
        myModel.solveMaze(algorithmSolve);
        setChanged();
        notifyObservers();
        return myModel.getSolution();
    }

    public boolean canMove(int row, int col) //if this valid move
    {
        return myModel.canMove(row, col);
    }
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel)
        {
            this.currentMaze = myModel.getCurrentMaze();
            this.currentRowPlayerIndex = myModel.getCharacterPositionRow();
            this.characterRowPlayerPosition.set(currentRowPlayerIndex + "");
            this.currentColPlayerIndex = myModel.getCharacterPositionColumn();
            this.characterColPlayerPosition.set(currentColPlayerIndex + "");
            setChanged();
            notifyObservers();
        }
    }

    public void setCurrentMaze(Maze maze){ //set from the load to the model
        this.currentMaze = maze;
        myModel.setCurrentMaze(maze);
    }
}
