import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Player {
    private Position playerPosition;
    private String playerColor;
    private ArrayList<String> colorList = new ArrayList<>();
    private ImageView characterDisplay;
    private String nameOfFile;
    private SystemDimensions systemDimension = new SystemDimensions();
    private int id;
    private boolean isDead = false;

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void chooseColor(){
        Random random = new Random();
        int randomNumber = random.nextInt(10);
        playerColor = colorList.remove(randomNumber);
    }

    public void addColors(){
        colorList.add("Blue");
        colorList.add("Brown");
        colorList.add("Cyan");
        colorList.add("DarkGreen");
        colorList.add("LightGreen");
        colorList.add("Orange");
        colorList.add("Pink");
        colorList.add("Purple");
        colorList.add("Red");
        colorList.add("Yellow");
    }

    public Player(StackPane base){
        addColors();
        chooseColor();
        nameOfFile = "Characters/" + playerColor + ".png";

        Image character = new Image(nameOfFile);
        characterDisplay = new ImageView(character);

        characterDisplay.setFitWidth(systemDimension.getCharacterWidth());
        characterDisplay.setFitHeight(systemDimension.getCharacterHeight());
        playerPosition = new Position(-25,200);
        addToPane(base);
    }

    public void addToPane(StackPane base) { base.getChildren().add(characterDisplay); }

    public ImageView getCharacterDisplay() {
        return characterDisplay;
    }

    public void setCharacterDisplay(ImageView characterDisplay) {
        this.characterDisplay = characterDisplay;
        characterDisplay.setFitWidth(systemDimension.getCharacterWidth());
        characterDisplay.setFitHeight(systemDimension.getCharacterHeight());
    }

    public void requestFocus(){
        characterDisplay.requestFocus();
    }

    public void updatePlayerPosition() {
        playerPosition.updatePosition(characterDisplay.getTranslateX(), characterDisplay.getTranslateY());
    }

    public void setPlayerPosition(double x, double y){
        characterDisplay.setTranslateX(x);
        characterDisplay.setTranslateY(y);
    }

    public void getPlayerPosition(){
        System.out.println(playerPosition.getXCoordinate() + " " + playerPosition.getYCoordinate());
    }
    public double getPlayerX() { return playerPosition.getXCoordinate(); }
    public double getPlayerY() { return playerPosition.getYCoordinate(); }

    public String getNameOfFile() {
        return nameOfFile;
    }

    public void setId(String id) {
        this.id = Integer.valueOf(id);
    }

    public int getId(){
        return id;
    }

    public boolean ableToKill(double x, double y){
        double[] otherPos = {x,y};
        double[] playerPos = {playerPosition.getXCoordinate(), playerPosition.getYCoordinate()};
        Euclidean euclidean = new Euclidean(otherPos, playerPos);
        double distance = euclidean.calculateEuclidean();
        System.out.println("otherPos: " + Arrays.toString(otherPos));
        System.out.println("playerPos" + Arrays.toString(playerPos));
        System.out.println("distance = " + distance);
        if(distance < 210 && distance > 0){
            return true;
        }
        return false;
    }
}
