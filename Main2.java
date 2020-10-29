package Assignment;

public class Main2 {

    public static void main(String[] args) {
        MapCoordinate coosA = new MapCoordinate(90, 0 , 1000);
        MapCoordinate coosA2 = new MapCoordinate(180, -90, 1000);

        coosA.distanceTo(coosA2);
        int result = coosA.compareTo(coosA2);
        System.out.println(result);
    }

}
