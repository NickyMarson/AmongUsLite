import java.util.HashSet;
import java.util.Set;

public class DeadBody {
    private double x;
    private double y;
    private Set<Integer> visiblePlayerIDs;

    public DeadBody(double x, double y) {
        this.x = x;
        this.y = y;
        this.visiblePlayerIDs = new HashSet<>();
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public Set<Integer> getVisiblePlayerIDs() {
        return visiblePlayerIDs;
    }
    public void addVisiblePlayerIDs(int playerID) {
        visiblePlayerIDs.add(playerID);
    }
}
