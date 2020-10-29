package Assignment;

import java.text.DecimalFormat;
import java.util.Map;

public class MapCoordinate implements Comparable { //I only really use this class to calculate the distance between two coordinates and to order the MapCoordinate List

    public final double LONGITUDE;
    public final double LATITUDE ;
    public final double ALTITUDE;

    MapCoordinate(double longitude, double latitude, double altitude){ //Constructor
        this.LONGITUDE = longitude;
        this.LATITUDE = latitude;
        this.ALTITUDE = altitude;
    }

    public double distanceTo(MapCoordinate coosA){ //In this method I use haversine formula to calculate the distance between two points on a sphere
        double distance;
        double r = 6372.8;

        double x1 = (this.LONGITUDE);
        double y1 = (this.LATITUDE);
        double x2 = (coosA.LONGITUDE);
        double y2 = (coosA.LATITUDE);

        double dLat = Math.toRadians(y2 - y1);
        double dLon = Math.toRadians(x2 - x1);
        y1 = Math.toRadians(y1);
        y2 = Math.toRadians(y2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(y1) * Math.cos(y2);
        double c = 2 * Math.asin(Math.sqrt(a));

        distance = r*c;
        //System.out.println(distance + " km");
        Math.round(distance);
        DecimalFormat df = new DecimalFormat("#.###");
        distance = Double.parseDouble(df.format(distance));
        return distance;
    }

    public int compareTo(MapCoordinate coosA){ //Compare two coordinates at a time. Comparing first altitude, then latitude and finally longitude
        double altitude1 = this.ALTITUDE;
        double latitude1 = this.LATITUDE;
        double longitude1 = this.LONGITUDE;
        double altitude2 = coosA.ALTITUDE;
        double latitude2 = coosA.LATITUDE;
        double longitude2 = coosA.LONGITUDE;

        //System.out.println(altitude1 + "        " + altitude2);

        /*
        int result = (int) (this.ALTITUDE - coosA.ALTITUDE);
        if (result == 0){
            result = this.compareTo(coosA);
        }
        return result;
         */

        if (altitude1 > altitude2){ //return 1 will swap the two coordinates in PlotMap.java, return 0 will do nothing.
            return 1;
        }else if (altitude1 == altitude2) {
            if (latitude1 > latitude2){
                return 1;
            }else if (latitude1 == latitude2){
                if (longitude1 > longitude2){
                    return 1;
                }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }


    public boolean equals(MapCoordinate coosA){ //I really could find a way to implement equals in compareTo. I was thinking of adding it to the conditions but I couldn't get it to work. However the compareTo() works just fine.
        if (this == coosA) return true;
        if (coosA == null) return false;
        if (getClass() != coosA.getClass()) return false;

        MapCoordinate other = (MapCoordinate) coosA;
        return this.compareTo(other) == 0;
    }
/*
    @Override
    String toString(){ //Same here, I believe that my program works just fine without implementing this method.

    }

 */
}
