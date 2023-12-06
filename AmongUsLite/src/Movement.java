import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.Set;

public class Movement {
    private Player player;
    private Timeline movementTimeline;
    private Game game;
    private double deltaX;
    private double deltaY;
    private Set<KeyCode> currentPressedKeys;
    private Circle circle;
    private Circle clipCircle;
    static private final double SYSTEMHEIGHT = Screen.getPrimary().getBounds().getHeight();
    static private final double SYSTEMWIDTH = Screen.getPrimary().getBounds().getWidth();
    public Movement(Player player, Timeline movementTimeline, Game game, Set<KeyCode> currentPressedKeys, Circle circle) {
        this.player = player;
        this.movementTimeline = movementTimeline;
        this.game = game;
        this.currentPressedKeys = currentPressedKeys;
        this.circle = circle;
        deltaX = 0;
        deltaY = 0;
    }

    public double heightConversion(double sourceHeightValue) { return ((sourceHeightValue / 1440) * SYSTEMHEIGHT); }
    public double widthConversion(double sourceWidthValue) { return ((sourceWidthValue / 2560) * SYSTEMWIDTH); }

    public Set<KeyCode> getCurrentPressedKeys() { return currentPressedKeys; }
    public void setCurrentPressedKeys(KeyCode tmpKeyCode) { currentPressedKeys.add(tmpKeyCode); }

    public void startMovement() {
        if(movementTimeline == null) {
            movementTimeline = new Timeline(new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    player.getCharacterDisplay().setTranslateX(player.getCharacterDisplay().getTranslateX() + deltaX);
                    player.getCharacterDisplay().setTranslateY(player.getCharacterDisplay().getTranslateY() + deltaY);
                    circle.setCenterX(player.getCharacterDisplay().getTranslateX() + (SYSTEMWIDTH / 2));
                    circle.setCenterY(player.getCharacterDisplay().getTranslateY() + (SYSTEMHEIGHT / 2));

                    checkRoomBoundarySet();
                }
            }));
            movementTimeline.setCycleCount(Timeline.INDEFINITE);
            movementTimeline.play();
        }
    }

    public void stopMovement() {
        if (movementTimeline != null) {
            movementTimeline.stop();
        }
    }

    public String getXString() { return Double.toString(player.getCharacterDisplay().getTranslateX()); }
    public String getYString() { return Double.toString(player.getCharacterDisplay().getTranslateY()); }
    public double getX() { return player.getCharacterDisplay().getTranslateX(); }
    public double getY() { return player.getCharacterDisplay().getTranslateY(); }

    public Player getPlayer() {
        return player;
    }

    public int numberOfKeysPressed() {
        return currentPressedKeys.size();
    }

    public boolean arrowKey(KeyCode tmpKeyCode) {
        return (tmpKeyCode == KeyCode.UP || tmpKeyCode == KeyCode.DOWN || tmpKeyCode == KeyCode.LEFT || tmpKeyCode == KeyCode.RIGHT);
    }

    public void checkKeyDirections(Set<KeyCode> currentPressedKeys) {
        if(currentPressedKeys.contains(KeyCode.DOWN)) {
            if(currentPressedKeys.contains(KeyCode.LEFT)) {
                deltaY = 1;
                deltaX = -1;
            }
            if(currentPressedKeys.contains(KeyCode.RIGHT)) {
                deltaY = 1;
                deltaX = 1;
            }
        }
        if(currentPressedKeys.contains(KeyCode.UP)) {
            if(currentPressedKeys.contains(KeyCode.LEFT)){
                deltaY = -1;
                deltaX = -1;
            }
            if(currentPressedKeys.contains(KeyCode.RIGHT)) {
                deltaY = -1;
                deltaX = 1;
            }
        }
    }

    public void updateDelta() {
        deltaX = 0;
        deltaY = 0;

        if(numberOfKeysPressed() == 2) {
            checkKeyDirections(currentPressedKeys);
        }
        if(numberOfKeysPressed() == 1) {
            if(currentPressedKeys.contains(KeyCode.UP)){
                deltaY = -2;
            }
            else if(currentPressedKeys.contains(KeyCode.DOWN)){
                deltaY = 2;
            }
            else if(currentPressedKeys.contains(KeyCode.LEFT)){
                deltaX = -2;
            }
            else if(currentPressedKeys.contains(KeyCode.RIGHT)){
                deltaX = 2;
            }
        }
    }

    public void checkRoomBoundarySet() {
        String pageName = game.getCurrentPage().getNameOfPage();
        if(pageName.equals("Cafeteria.jpeg")) {
            wallBoundariesCafeteria();
        }
    }

    // (Original X / Source Screen Width) * Target Screen Width
    // width X height
    // 2560 X 1440
    public void wallBoundariesCafeteria() {
        if(player.getCharacterDisplay().getTranslateY() <= heightConversion(-535)) { // Top boundary
            deltaX = 0;
            deltaY = 0;
            player.getCharacterDisplay().setTranslateY(heightConversion(-535) + 1);
        }
        if(player.getCharacterDisplay().getTranslateX() <= widthConversion(-1115) && player.getCharacterDisplay().getTranslateY() <= heightConversion(-185)) { // Left boundary above door
            deltaX = 0;
            deltaY = 0;
            double tmpX = player.getCharacterDisplay().getTranslateX();
            double tmpY = player.getCharacterDisplay().getTranslateY();
            if((currentPressedKeys.contains(KeyCode.LEFT)) && (!currentPressedKeys.contains(KeyCode.UP)) && (!currentPressedKeys.contains(KeyCode.DOWN))) {
                player.getCharacterDisplay().setTranslateX(tmpX + 10);
            }
            if((currentPressedKeys.contains(KeyCode.LEFT)) && currentPressedKeys.contains(KeyCode.DOWN)) {
                player.getCharacterDisplay().setTranslateX(tmpX + 5);
                player.getCharacterDisplay().setTranslateY(tmpY + 5);
            }
            if((currentPressedKeys.contains(KeyCode.UP)) && (!currentPressedKeys.contains(KeyCode.LEFT))) {
                player.getCharacterDisplay().setTranslateY(tmpY + 10);
            }
            if(currentPressedKeys.contains(KeyCode.LEFT) && currentPressedKeys.contains(KeyCode.UP)) {
                player.getCharacterDisplay().setTranslateX(tmpX + 5);
                player.getCharacterDisplay().setTranslateY(tmpY - 5);
            }
        }

        if((player.getCharacterDisplay().getTranslateX() >= widthConversion(1115)) && (player.getCharacterDisplay().getTranslateY() <= heightConversion(-185))) { // Right boundary above door
            deltaX = 0;
            deltaY = 0;
            double tmpX = player.getCharacterDisplay().getTranslateX();
            double tmpY = player.getCharacterDisplay().getTranslateY();
            if((currentPressedKeys.contains(KeyCode.RIGHT)) && (!currentPressedKeys.contains(KeyCode.DOWN)) && (!currentPressedKeys.contains(KeyCode.UP))) {
                player.getCharacterDisplay().setTranslateX(tmpX - 10);
            }
            if((currentPressedKeys.contains(KeyCode.RIGHT)) && currentPressedKeys.contains(KeyCode.DOWN)) {
                player.getCharacterDisplay().setTranslateX(tmpX - 5);
                player.getCharacterDisplay().setTranslateY(tmpY + 5);
            }
            if((currentPressedKeys.contains(KeyCode.UP)) && (!currentPressedKeys.contains(KeyCode.RIGHT))) {
                player.getCharacterDisplay().setTranslateY(tmpY + 10);
            }
            if(currentPressedKeys.contains(KeyCode.RIGHT) && currentPressedKeys.contains(KeyCode.UP)) {
                player.getCharacterDisplay().setTranslateX(tmpX - 5);
                player.getCharacterDisplay().setTranslateY(tmpY - 5);
            }
        }

        if((player.getCharacterDisplay().getTranslateX() <= widthConversion(-1115)) && (player.getCharacterDisplay().getTranslateY() >= heightConversion(-60))) { // Left boundary below door
            deltaX = 0;
            deltaY = 0;
            double tmpX = player.getCharacterDisplay().getTranslateX();
            double tmpY = player.getCharacterDisplay().getTranslateY();
            if((currentPressedKeys.contains(KeyCode.LEFT)) && (!currentPressedKeys.contains(KeyCode.DOWN))) {
                player.getCharacterDisplay().setTranslateX(tmpX + 10);
            }
            if((currentPressedKeys.contains(KeyCode.UP)) && currentPressedKeys.contains(KeyCode.LEFT)) {
                player.getCharacterDisplay().setTranslateX(tmpX + 5);
                player.getCharacterDisplay().setTranslateY(tmpY - 5);
            }
            if((currentPressedKeys.contains(KeyCode.DOWN)) && (!currentPressedKeys.contains(KeyCode.LEFT))) {
                player.getCharacterDisplay().setTranslateY(tmpY - 10);
            }
            if(currentPressedKeys.contains(KeyCode.LEFT) && currentPressedKeys.contains(KeyCode.DOWN)) {
                player.getCharacterDisplay().setTranslateX(tmpX + 5);
                player.getCharacterDisplay().setTranslateY(tmpY);
            }
        }

        if((player.getCharacterDisplay().getTranslateX() >= widthConversion(1115)) && (player.getCharacterDisplay().getTranslateY() >= heightConversion(-60))) { // Right boundary below door
            deltaX = 0;
            deltaY = 0;
            double tmpX = player.getCharacterDisplay().getTranslateX();
            double tmpY = player.getCharacterDisplay().getTranslateY();
            if((currentPressedKeys.contains(KeyCode.RIGHT)) && (!currentPressedKeys.contains(KeyCode.DOWN))) { // Right
                player.getCharacterDisplay().setTranslateX(tmpX - 10);
            }
            if((currentPressedKeys.contains(KeyCode.UP)) && currentPressedKeys.contains(KeyCode.RIGHT)) { // Right + Up
                player.getCharacterDisplay().setTranslateX(tmpX - 5);
                player.getCharacterDisplay().setTranslateY(tmpY - 5);
            }
            if((currentPressedKeys.contains(KeyCode.DOWN)) && (!currentPressedKeys.contains(KeyCode.RIGHT))) { // Down
                player.getCharacterDisplay().setTranslateY(tmpY - 10);
            }
            if(currentPressedKeys.contains(KeyCode.RIGHT) && currentPressedKeys.contains(KeyCode.DOWN)) { // Right + Down
                player.getCharacterDisplay().setTranslateX(tmpX - 5);
                player.getCharacterDisplay().setTranslateY(tmpY + 5);
            }
        }

        if((player.getCharacterDisplay().getTranslateX() <= widthConversion(-245)) && (player.getCharacterDisplay().getTranslateY() >= heightConversion(510))) { // Left bottom boundary
            deltaX = 0;
            deltaY = 0;
            double tmpX = player.getCharacterDisplay().getTranslateX();
            double tmpY = player.getCharacterDisplay().getTranslateY();
            if((currentPressedKeys.contains(KeyCode.LEFT)) && (!currentPressedKeys.contains(KeyCode.DOWN))) {
                player.getCharacterDisplay().setTranslateX(tmpX + 10);
            }
            if((currentPressedKeys.contains(KeyCode.UP)) && currentPressedKeys.contains(KeyCode.LEFT)) {
                player.getCharacterDisplay().setTranslateX(tmpX + 5);
                player.getCharacterDisplay().setTranslateY(tmpY - 5);
            }
            if((currentPressedKeys.contains(KeyCode.DOWN)) && (!currentPressedKeys.contains(KeyCode.LEFT))) {
                player.getCharacterDisplay().setTranslateY(tmpY - 10);
            }
            if(currentPressedKeys.contains(KeyCode.LEFT) && currentPressedKeys.contains(KeyCode.DOWN)) {
                player.getCharacterDisplay().setTranslateX(tmpX);
                player.getCharacterDisplay().setTranslateY(tmpY - 5);
            }
            if(currentPressedKeys.contains(KeyCode.DOWN) && currentPressedKeys.contains(KeyCode.RIGHT)) {
                player.getCharacterDisplay().setTranslateX(tmpX + 5);
                player.getCharacterDisplay().setTranslateY(tmpY - 5);
            }
        }

        if((player.getCharacterDisplay().getTranslateX() >= widthConversion(245)) && (player.getCharacterDisplay().getTranslateY() >= heightConversion(510))) { // Right bottom boundary
            deltaX = 0;
            deltaY = 0;
            double tmpX = player.getCharacterDisplay().getTranslateX();
            double tmpY = player.getCharacterDisplay().getTranslateY();
            if((currentPressedKeys.contains(KeyCode.RIGHT)) && (!currentPressedKeys.contains(KeyCode.DOWN))) {
                player.getCharacterDisplay().setTranslateX(tmpX - 10);
            }
            if((currentPressedKeys.contains(KeyCode.UP)) && currentPressedKeys.contains(KeyCode.RIGHT)) {
                player.getCharacterDisplay().setTranslateX(tmpX - 5);
                player.getCharacterDisplay().setTranslateY(tmpY - 5);
            }
            if((currentPressedKeys.contains(KeyCode.DOWN)) && (!currentPressedKeys.contains(KeyCode.RIGHT))) {
                player.getCharacterDisplay().setTranslateY(tmpY - 10);
            }
            if(currentPressedKeys.contains(KeyCode.RIGHT) && currentPressedKeys.contains(KeyCode.DOWN)) {
                player.getCharacterDisplay().setTranslateX(tmpX);
                player.getCharacterDisplay().setTranslateY(tmpY - 5);
            }
            if(currentPressedKeys.contains(KeyCode.DOWN) && currentPressedKeys.contains(KeyCode.LEFT)) {
                player.getCharacterDisplay().setTranslateX(tmpX - 5);
                player.getCharacterDisplay().setTranslateY(tmpY - 5);
            }
        }
    }
}
