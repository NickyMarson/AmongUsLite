import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application implements Runnable{
    private static Socket serverSocket;
    private static ObjectOutputStream serverWriter;
    private static ObjectInputStream serverReader;
    private Set<KeyCode> currentPressedKeys = new HashSet<>();
    private Timeline movementTimeline;
    private ImageView roomDisplay;
    private ImageView mapDisplay;
    private ImageView darknessDisplay;
    private boolean mapOn;
    private boolean ePressed;
    private String currentPage = "RoomImages/Cafeteria.jpeg";
    private Game game = new Game();
    private Player player;
    private Movement movement;
    private Circle circle;
    private static String serverMessage;
    public static void main(String[] args) {
        try {
            serverSocket = new Socket("localhost", 23708);
            System.out.println("Chat Server (Main) is running on port " + 23708);
            serverWriter = new ObjectOutputStream(serverSocket.getOutputStream());
            serverReader = new ObjectInputStream(serverSocket.getInputStream());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new ServerListener(serverSocket));
            executorService.shutdown();
            launch(args);
        }
        catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
    @Override
    public void run() {
        try {
            while (true) {
                /*if(serverReader.available() > 0) {
                    serverMessage = (String) serverReader.readObject();
                    System.out.println("Server sent (Main): " + serverMessage);
                }*/
                serverMessage = (String) serverReader.readObject();
                System.out.println("Server sent (Main): " + serverMessage);
                receiveMessage(serverMessage);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void receiveMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Server sent (Main0)" + message);
                //chatArea.append(message + "\n");
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //Create the image
        Image cafeteria = new Image("RoomImages/Cafeteria.jpeg");
        roomDisplay = new ImageView(cafeteria);

        Image map = new Image("Utility/FullMap.jpg");
        mapDisplay = new ImageView(map);
        mapOn = false;
        ePressed = false;

        Image darkness = new Image("Utility/Darkness.jpg");
        darknessDisplay = new ImageView(darkness);

        SystemDimensions systemDimension = new SystemDimensions();

        roomDisplay.setFitHeight(systemDimension.getSystemHeight());
        roomDisplay.setFitWidth(systemDimension.getSystemWidth());
        mapDisplay.setFitHeight(systemDimension.getSystemHeight());
        mapDisplay.setFitWidth(systemDimension.getSystemWidth());
        darknessDisplay.setFitHeight(systemDimension.getSystemHeight());
        darknessDisplay.setFitWidth(systemDimension.getSystemWidth());

        circle = new Circle(250);
        circle.setFill(Color.RED);
        circle.setCenterX(systemDimension.getSystemWidth() / 2);
        circle.setCenterY(systemDimension.getSystemHeight() / 2);
        roomDisplay.setClip(circle);

        StackPane base = new StackPane();
        //base.getChildren().add(mapDisplay);
        base.getChildren().add(darknessDisplay);
        base.getChildren().add(roomDisplay);
        //player = new Player(base);

        Scene scene = new Scene(base, systemDimension.getSystemWidth(), systemDimension.getSystemHeight());
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        movement = new Movement(player, movementTimeline, game, currentPressedKeys, circle);
        movement.startMovement();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode tmpKeyCode = keyEvent.getCode();
                if(movement.arrowKey(tmpKeyCode)) {
                    movement.setCurrentPressedKeys(tmpKeyCode);
                    movement.updateDelta();
                }
                //circle.setCenterX(movement.getX() + (systemDimension.getSystemWidth() / 2));
                //circle.setCenterY(movement.getY() + (systemDimension.getSystemHeight() / 2));

//                ImageView previous = roomDisplay;
//                boolean switchPage = game.runPage(player.getCharacterDisplay(), roomDisplay);
//                if(switchPage){
//                    roomDisplay = new ImageView(new Image("RoomImages/" + game.getCurrentPage().getNameOfPage()));
//                    roomDisplay.setFitWidth(systemDimension.getSystemWidth());
//                    roomDisplay.setFitHeight(systemDimension.getSystemHeight());
//                    base.getChildren().add(0, roomDisplay);
//                    base.getChildren().remove(previous);
//                    ePressed = false;
//                    mapDisplay.toBack();
//                    mapOn = false;
//                }

                if(keyEvent.getCode() == KeyCode.E && !ePressed && !mapOn) {
                    mapDisplay.toFront();
                    mapOn = true;
                    ePressed = true;
                }
                player.updatePlayerPosition();
//                player.getPosition();
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode tmpKeyCode = keyEvent.getCode();
                if(movement.arrowKey(tmpKeyCode)) {
                    currentPressedKeys.remove(keyEvent.getCode());
                }
                movement.updateDelta();
                try {
                    //File file = new File("Position.txt");
                    serverWriter.writeObject("(" + movement.getXString() + ", " + movement.getYString() + ")");
                    serverWriter.flush();
                    //System.out.println("serverWriter (Main): " + movement.getXString());
                    /*FileWriter myWriter = new FileWriter(file);
                    myWriter.write(movement.getXString());
                    myWriter.flush();*/
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                System.out.println("X-value = " + player.getCharacterDisplay().getTranslateX());
//                System.out.println("Y-value = " + player.getCharacterDisplay().getTranslateY());

                if((keyEvent.getCode() == KeyCode.E && ePressed && mapOn)) {
                    ePressed = false;
                    mapDisplay.toBack();
                    mapOn = false;
                    //System.out.println("mapDisplay: " + base.getChildren().contains(mapDisplay));
                }
            }
        });
    }
}
