public class Position {
    private double xCoordinate;
    private double yCoordinate;

    public Position(double x, double y){
        xCoordinate = x;
        yCoordinate = y;
    }

    public void updateXCoordinate(double xCoordinate){
        this.xCoordinate = xCoordinate;
    }

    public void updateYCoordinate(double yCoordinate){
        this.yCoordinate = yCoordinate;
    }

    public void updatePosition(double xCoordinate, double yCoordinate){
        updateXCoordinate(xCoordinate);
        updateYCoordinate(yCoordinate);
    }

    public double getXCoordinate() {
        return xCoordinate;
    }

    public double getYCoordinate() {
        return yCoordinate;
    }
}
