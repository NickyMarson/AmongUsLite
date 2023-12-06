import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
// class for normalizing dimension based on system size
public class SystemDimensions {

    private double systemWidth;
    private double systemHeight;

    private double characterWidth;
    private double characterHeight;
    public SystemDimensions(){
        Rectangle2D systemBounds = Screen.getPrimary().getBounds();
        systemWidth = systemBounds.getWidth();
        systemHeight = systemBounds.getHeight();

        characterWidth = systemWidth/30;
        characterHeight = systemHeight/15;
    }

    public double getSystemWidth() {
        return systemWidth;
    }

    public double getSystemHeight() {
        return systemHeight;
    }

    public double getCharacterWidth() {
        return characterWidth;
    }

    public double getCharacterHeight() {
        return characterHeight;
    }
}
