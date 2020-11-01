package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import Server.*;
import algorithms.search.ISearchable;
import algorithms.search.ISearchingAlgorithm;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;


public class MyModel extends Observable implements IModel {
    private Maze currentMaze;
    private Server mazeGeneratingServer;
    private Server solveSearchProblemServer;
    private String[] configuration;
    private Solution solution;
    private int rowPlayer;
    private int colPlayer;

    public MyModel() {
        rowPlayer = 0;
        colPlayer = 0;
        Configurations.setConfigurations("5", "BestF", "My Maze"); //initialize the number of threads
        mazeGeneratingServer = new Server(5400, 100000, new ServerStrategyGenerateMaze()); //change time
        mazeGeneratingServer.start();
        Configurations.setConfigurations("5", "BestF", "My Maze");
        solveSearchProblemServer = new Server(5401, 100000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();
        configuration = new String[2]; //in order to save the configuration from the user 0- searching algorithm 1- type maze
        configuration[0] ="BestF";
        configuration[1]="My Maze";
    }

    public Maze getCurrentMaze() {
        return currentMaze;
    }

    public Solution getSolution() {
        return solution;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    public void generateMaze(int row, int col, String typeMaze) { //this function get maze from the server
        try {
            //write the inputs into the configuration file
            if(typeMaze == null)
                typeMaze = "My Maze";
            configuration[1] = typeMaze;
            Configurations.setConfigurations("5", "BestFS", typeMaze);
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col}; //50,50
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[mazeDimensions[0] * mazeDimensions[1] + 48 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        currentMaze = new Maze(decompressedMaze);
                        setChanged();
                        notifyObservers();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentMaze(Maze maze)
    {
        this.currentMaze =maze;
    }

    public void Exit() //delete all files and close all threads indeed to what we have done already
    {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

    public void solveMaze(String searcingAlgo) {
        //if(this.currentMaze == null)
        //   generateMaze(50,50, "My Maze"); //this is the default maze when user doesn't type generate maze
        if(searcingAlgo == null)
            configuration[0] = "BestFS";
        else
            configuration[0] = searcingAlgo;

        Configurations.setConfigurations("5", configuration[0], configuration[1]); ////////////////////ask about how many threads need to config
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(currentMaze); //send maze to server
                        toServer.flush();
                        solution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        setChanged();
                        notifyObservers();
                 /*   //Print Maze Solution retrieved from the server
                    System.out.println(String.format("Solution steps: %s", solution));
                    ArrayList<AState> mazeSolutionSteps = solution.getSolutionPath();
                    for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                        System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                    } */
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //  return solution;
    }

    @Override
    public int getCharacterPositionColumn() {
        return this.colPlayer;
    }

    @Override
    public int getCharacterPositionRow() {
        return this.rowPlayer;
    }

    public boolean canMove(int row, int col) {
        int rowLength = currentMaze.getMaze().length;
        int colLength = currentMaze.getMaze()[0].length;
        if (row > rowLength - 1 || col > colLength - 1) //big than the length - out of bound
            return false;
        if (row < 0 || col < 0) //small than 0
            return false;
        int[][] maze = currentMaze.getMaze();
        if (maze[row][col] == 1) //if this place is wall
            return false;
        return true;
    }

    @Override
    public void saveMaze(String nameFile) {
        String DirectoryPath = "C:\\Maze";
        File tempFile = new File(DirectoryPath);
        if(!tempFile.exists()) { //if not exist, we need to create one
            new File(DirectoryPath).mkdir(); //create a new folder!!!
        }
        String absolutePath = DirectoryPath +"\\"+ (nameFile) + ".maze";
        File objectFile = new File(absolutePath); //create the file
        // write the content in file
        try (FileOutputStream fileOutputStream = new FileOutputStream(objectFile)) {
            //write OBJECT maze
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOutputStream);
            objectOut.writeObject(currentMaze); //compressMaze
            objectOut.flush();
            objectOut.close();
        } catch (IOException e) {    }
    }

    @Override
    public boolean loadMaze(String nameFile) {
        String DirectoryPath = "C:\\Maze";
        File tempFile = new File(DirectoryPath);
        if (!tempFile.exists())
            return false;
        try {
            //read OBJECT maze
            String absolutePath = DirectoryPath + "\\" + (nameFile) + ".maze";
            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(absolutePath));
            Maze m = (Maze) objectIn.readObject();
            objectIn.close();
            this.currentMaze = m;
            setChanged();
            notifyObservers();
            return true;
        } catch (IOException | ClassNotFoundException e) {
        }
        return false;
    }
}
