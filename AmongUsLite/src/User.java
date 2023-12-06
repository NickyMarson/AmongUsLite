import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// User class with not much functionality
public class User extends Application {
    private static final String SERVER_IP = "localhost";
    private static final int PORT_NUMBER = 23708;
    private ObjectOutputStream serverWriter;
    private static JTextArea chatArea;
    private JTextField userInputField;
    private int id;
    private String username;
    private String password;
    private int games_played;
    private int wins;
    private int losses;

    // constructor sets user information
    public User(String username, String password, int games_played, int wins, int losses) {
        this.username = username;
        this.password =  password;
        this.games_played = games_played;
        this.wins = wins;
        this.losses = losses;
    }

    public String getUsername() {
        return username;
    }

    public void initialize(Stage primaryStage) {
        /*connectToServer();

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        userInputField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = userInputField.getText();
                //sendMessage(userInput);
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(userInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        Image cafeteria = new Image("RoomImages/Cafeteria.jpeg");
        roomDisplay = new ImageView(cafeteria);

        Image map = new Image("Utility/FullMap.jpg");
        mapDisplay = new ImageView(map);
        mapOn = false;
        ePressed = false;

        Image darkness = new Image("Utility/Darkness.jpg");
        darknessDisplay = new ImageView(darkness);

        systemDimension = new SystemDimensions();

        roomDisplay.setFitHeight(systemDimension.getSystemHeight());
        roomDisplay.setFitWidth(systemDimension.getSystemWidth());
        mapDisplay.setFitHeight(systemDimension.getSystemHeight());
        mapDisplay.setFitWidth(systemDimension.getSystemWidth());
        darknessDisplay.setFitHeight(systemDimension.getSystemHeight());
        darknessDisplay.setFitWidth(systemDimension.getSystemWidth());

        Circle circle = new Circle(50);
        circle.setFill(Color.RED);
        roomDisplay.setClip(circle);

        base = new StackPane();
        base.getChildren().add(mapDisplay);
        base.getChildren().add(roomDisplay);
        base.getChildren().add(darknessDisplay);
        player = new Player(base);

        movement = new Movement(player, movementTimeline, game, currentPressedKeys, circle);
        movement.startMovement();
        movement.getPlayer();

        primaryStage.setScene(new Scene(base, systemDimension.getSystemWidth(), systemDimension.getSystemHeight()));
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private boolean connectToServer() {
        try {
            Socket socket = new Socket(SERVER_IP, PORT_NUMBER);

            // Output stream for writing to the server
            serverWriter = new ObjectOutputStream(socket.getOutputStream());

            // Executor service for managing the server listener thread
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new ServerListener(socket));
            executorService.shutdown();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void sendMessage(Player player) {
        try {
            serverWriter.writeObject(player);
            serverWriter.flush();
            //userInputField.setText(""); // Clear the input field
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static class ServerListener implements Runnable {
        private Socket socket;

        public ServerListener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            /*try {
                ObjectInputStream serverReader = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    serverMessage = (String) serverReader.readObject();
                    //receiveMessage(serverMessage);
                    System.out.println("Client: " + serverMessage);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Error");
            }*/
        }
    }

    public static void receiveMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //chatArea.append(message + "\n");
            }
        });
    }

    public void start(Stage primaryStage) {
        /*scene = new Scene(base, systemDimension.getSystemWidth(), systemDimension.getSystemHeight());
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode tmpKeyCode = keyEvent.getCode();
                if(movement.arrowKey(tmpKeyCode)) {
                    movement.setCurrentPressedKeys(tmpKeyCode);
                    movement.updateDelta();
                }

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

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(400, 300);
                setLocationRelativeTo(null);
                setVisible(true);
            }
        });*/
    }

    /*public static void main(String[] args, String username)
    {
        launch(args);
    }*/

//    public void startMovement() {
//        if(movementTimeline == null) {
//            movementTimeline = new Timeline(new KeyFrame(Duration.millis(16), new EventHandler<javafx.event.ActionEvent>() {
//                @Override
//                public void handle(javafx.event.ActionEvent actionEvent) {
//                    player.getCharacterDisplay().setTranslateX(player.getCharacterDisplay().getTranslateX() + deltaX);
//                    player.getCharacterDisplay().setTranslateY(player.getCharacterDisplay().getTranslateY() + deltaY);
//                    checkRoomBoundarySet();
//                }
//            }));
//            movementTimeline.setCycleCount(Timeline.INDEFINITE);
//            movementTimeline.play();
//        }
//    }
} // end class Client
