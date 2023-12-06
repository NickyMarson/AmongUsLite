import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Map {
    private ImageView roomDisplay;

    public Map(StackPane base) {
        SystemDimensions systemDimension = new SystemDimensions();
        Image cafeteria = new Image("RoomImages/Cafeteria.jpeg");
        roomDisplay = new ImageView(cafeteria);

        base.getChildren().add(roomDisplay);
        roomDisplay.setFitHeight(systemDimension.getSystemHeight());
        roomDisplay.setFitWidth(systemDimension.getSystemWidth());
    }

    public ImageView getRoomDisplay() {
        return roomDisplay;
    }

    public void setRoomDisplay(ImageView roomDisplay) {
        this.roomDisplay = roomDisplay;
    }
}
