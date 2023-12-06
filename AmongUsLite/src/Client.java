import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.Map;

public class Client extends Application {
    static private final double SYSTEMHEIGHT = Screen.getPrimary().getBounds().getHeight();
    static private final double SYSTEMWIDTH = Screen.getPrimary().getBounds().getWidth();
    private double xPosition = 0.0;
    private double yPosition = 0.0;

    private Socket socket;
    private OutputStream outputStream;
    private ImageView darkness = new ImageView(new Image("Utility/Darkness.jpg"));

    private Map<Integer, ImageView> clientImageViews = new HashMap<>();
    private Map<Integer, Position> otherClientPositions = new HashMap<>();
    private ImageView room = new ImageView(new Image("RoomImages/Cafeteria.jpeg"));
    private ImageView fakeDead = new ImageView(new Image("Characters/DeadBody.png"));
    private String playerType;
    private Player player;
    private String playerID;
    SystemDimensions systemDimension = new SystemDimensions();
    private Circle circle;
    private Circle clipCircle;
    private boolean killInRange = false;
    private boolean isDead;
    private boolean requestKill = false;
    private Movement movement;
    private Timeline movementTimeline;
    private Game game = new Game();
    private Set<KeyCode> currentPressedKeys = new HashSet<>();
    private double lastStoredX = 0;
    private double lastStoredY = 0;
    private Map<Integer, DeadBody> deadBodies = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        SystemDimensions systemDimension = new SystemDimensions();
        room.setFitHeight(systemDimension.getSystemHeight());
        room.setFitWidth(systemDimension.getSystemWidth());
        fakeDead.setFitWidth(systemDimension.getCharacterWidth());
        fakeDead.setFitHeight(systemDimension.getCharacterHeight());
        darkness.setFitHeight(systemDimension.getSystemHeight());
        darkness.setFitWidth(systemDimension.getSystemWidth());

        circle = new Circle(250);
        circle.setFill(Color.RED);
        circle.setCenterX(systemDimension.getSystemWidth() / 2);
        circle.setCenterY(systemDimension.getSystemHeight() / 2);
        room.setClip(circle);
        clipCircle = new Circle(250);
        clipCircle.setFill(Color.RED);
        clipCircle.setCenterX(systemDimension.getSystemWidth() / 2);
        clipCircle.setCenterY(systemDimension.getSystemHeight() / 2);

        player = new Player(root);
        root.getChildren().add(fakeDead);
        root.getChildren().add(darkness);
        root.getChildren().add(room);
        Scene scene = new Scene(root, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("AmongUsLite");
        primaryStage.show();
        // Connect to the server on the specified host and port
        connectToServer();
        movement = new Movement(player, movementTimeline, game, currentPressedKeys, circle);
        movement.startMovement();

        scene.setOnKeyPressed(event -> handleKeyPress(event.getCode(), player.getCharacterDisplay(), player.getNameOfFile()));
        scene.setOnKeyReleased(event -> handleKeyRelease(event.getCode(), player.getCharacterDisplay(), player.getNameOfFile()));

        // Start a thread to continuously receive updates from the server
        new Thread(() -> receiveUpdatesFromServer(player.getCharacterDisplay(), root)).start();
    }

    private void handleKeyPress(KeyCode keyCode, ImageView imageView, String nameOfFile) {
        if(movement.arrowKey(keyCode)) {
            movement.setCurrentPressedKeys(keyCode);
            movement.updateDelta();
            imageView.setTranslateX(movement.getX());
            imageView.setTranslateY(movement.getY());
            player.updatePlayerPosition();
        }

        // Update image position
        if (!isDead) {
            lastStoredX = movement.getX();
            lastStoredY = movement.getY();
            sendPositionToServer(movement.getX(), movement.getY(), nameOfFile, isDead);
        }
        if (isDead) {
            sendPositionToServer(lastStoredX, lastStoredY, "Characters/DeadBody.png", isDead);
        }
    }

    public void handleKeyRelease(KeyCode keyCode, ImageView imageView, String nameOfFile) {
        if(movement.arrowKey(keyCode)) {
            currentPressedKeys.remove(keyCode);
        }
        movement.updateDelta();

        if (!isDead) {
            lastStoredX = movement.getX();
            lastStoredY = movement.getY();
            sendPositionToServer(movement.getX(), movement.getY(), nameOfFile, isDead);

        }
        if (isDead) {
            sendPositionToServer(lastStoredX, lastStoredY, "Characters/DeadBody.png", isDead);
        }
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 23705);
            outputStream = socket.getOutputStream();
            byte[] buffer1 = new byte[1024];
            byte[] buffer2 = new byte[1024];
            playerType = new String(buffer1, 0, socket.getInputStream().read(buffer1));
            System.out.println(playerType);
            player.setId(new String(buffer2, 0, socket.getInputStream().read(buffer2)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPositionToServer(double x, double y, String nameOfFile, boolean isDead) {
        try {
            // Send x and y positions as parameters
            String parameters = nameOfFile + "&x=" + x + "&y=" + y + "&dead=" + isDead + "&kill=" + requestKill;
            byte[] messageBytes = parameters.getBytes();
            System.out.println("messageBytes: " + messageBytes);
            outputStream.write(messageBytes);
            outputStream.flush();
            requestKill = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveUpdatesFromServer(ImageView imageView, Pane root) {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                int bytesRead = socket.getInputStream().read(buffer);
                if (bytesRead == -1) {
                    break; // server disconnected
                }

                String receivedMessage = new String(buffer, 0, bytesRead);
                System.out.println("Received update from server: " + receivedMessage);
                Platform.runLater(() -> updateAllClientsPosition(imageView, root, receivedMessage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDeathToServer(String fileName, int clientID, double deadBodyX, double deadBodyY) {
        try {
            // Send x and y positions as parameters
            String parameters = fileName + "&i=" + clientID + "&deadBodyX=" + deadBodyX + "&deadBodyY=" + deadBodyY;
            byte[] messageBytes = parameters.getBytes();
            outputStream.write(messageBytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAllClientsPosition(ImageView imageView, Pane root, String positions) {
        // Clear existing positions
        root.getChildren().removeIf(node -> node != room && node != darkness && node != fakeDead);

        //root.getChildren().add(background);

        // Update all clients' positions
        String[] clients = positions.split(";");
        //System.out.println(Arrays.toString(clients));
        for (String client : clients) {
            String[] parts = client.split("&");
            int clientID = Integer.parseInt(parts[0]);
            String fileName = parts[1];
            double otherX = Double.parseDouble(parts[2].substring(2));
            double otherY = Double.parseDouble(parts[3].substring(2));
            boolean otherIsDead = false;
            double deadBodyX = 0;
            double deadBodyY = 0;

            if (parts.length > 4 && parts[4].startsWith("dead=")) {
                otherIsDead = Boolean.parseBoolean(parts[4].substring(5));
//                if (otherIsDead) {
//                    deadBodyX = Double.parseDouble(parts[5].substring(11));
//                    deadBodyY = Double.parseDouble(parts[6].substring(11));
//                }
            }

            // Checks if another user is dead, if so it checks if there is a dead body for the given user, if not creates one
            if (otherIsDead) {
                if (!deadBodies.containsKey(clientID)) {
                    DeadBody deadBody = new DeadBody(otherX, otherY);
                    deadBodies.put(clientID, deadBody);
                }

                if (isPlayerInRange(imageView.getTranslateX(), imageView.getTranslateY(), deadBodies.get(clientID).getX(), deadBodies.get(clientID).getY())) {
                    ImageView deadBodyView = new ImageView(new Image("Characters/DeadBody.png"));
                    deadBodyView.setTranslateX(otherX);
                    deadBodyView.setTranslateY(otherY);
                    deadBodyView.setFitHeight(systemDimension.getCharacterHeight());
                    deadBodyView.setFitWidth(systemDimension.getCharacterHeight());
                    root.getChildren().add(deadBodyView);
                    clientImageViews.put(clientID, deadBodyView);
                }
                else {
                    deadBodies.remove(clientID);
                }
//                room.toFront();
//                deadBodyView.toFront();
            }
            // If another user is not dead, updates the position of the other clients
            else {
                ImageView otherImageView = new ImageView(new Image(fileName)); // Replace with other clients' image path
                otherImageView.setTranslateX(otherX);
                otherImageView.setTranslateY(otherY);
                otherImageView.setFitHeight(systemDimension.getCharacterHeight());
                otherImageView.setFitWidth(systemDimension.getCharacterHeight());
                clipCircle.setCenterX(otherImageView.getTranslateX());
                clipCircle.setCenterY(otherImageView.getTranslateY());
                root.getChildren().add(otherImageView);
                clientImageViews.put(clientID, otherImageView);
            }

            imageView.setTranslateX(movement.getX());
            imageView.setTranslateY(movement.getY());
            player.updatePlayerPosition();

            if(playerType.equals("CREWMATE") && !isDead){
                System.out.println("HereUpdate");
                System.out.println();
                if(player.ableToKill(xPosition, yPosition)) {
                    fileName = "Characters/DeadBody.png";
                    isDead = true;
                    System.out.println("DEATH HERE (" + otherX + ", " + otherY + ")");
                    sendDeathToServer(fileName, clientID, otherX, otherY);
                }
            }
        }
    }

    public boolean isPlayerInRange(double playerX, double playerY, double deadBodyX, double deadBodyY) {
        double distance = Math.sqrt(Math.pow(playerX - deadBodyX, 2) + Math.pow(playerY - deadBodyY, 2));
        System.out.println("DISTANCE + " + distance);
        return distance <= 210;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

