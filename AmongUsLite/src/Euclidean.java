public class Euclidean {
    private double[] point1;
    private double[] point2;

    private int length;

    public Euclidean(double[] point1, double[] point2){
        if(point1.length == point2.length) {
            this.point1 = point1;
            this.point2 = point2;
            length = point1.length;
        }
    }

    public double calculateEuclidean(){
        double sum = 0;
        for(int i = 0; i < length; i++){
            sum += ((point1[i] - point2[i]) * (point1[i] - point2[i]));
        }
        if (sum == 0) {
            return 0;
        }
        else {
            return Math.sqrt(sum);
        }
    }
}
