package Assignment;

import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class PlotMap extends Plot { //This class is used to print the Earth and to deal with the mouseAdaptor
    Earth e;
    int count = 0;
    Double[][] points = new Double[2][3];
    Double[] pPoints = new Double[3];
    Double[] pCoos;

    ArrayList<Double[]> MapCoordinate = new ArrayList<Double[]>(); //List used to save the clicked coordinates

    public PlotMap(String filename) throws FileNotFoundException{

        e = new Earth();

        try { //Ask for a sea level
            Scanner userAnswer = new Scanner(System.in);
            System.out.println();
            System.out.println("Please enter new sea level in meters. (Default value: 0)");
            String answer = userAnswer.nextLine();
            e.seaLevel = Double.valueOf(answer);
        }catch (Exception error){
            System.out.println("Not a valid input");
            System.out.println("Please enter new sea level in meters. (Default value: 0)");
        }

        e.readDataMap(filename);
        System.out.println("");
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent event) {

                if (SwingUtilities.isRightMouseButton(event) && event.getClickCount() == 1) { //This condition checks if the right button of the mouse was pressed
                    try {
                    //System.out.println("CLICK DESTRO");

                    int position = MapCoordinate.indexOf(pPoints); //pPoints is a single dimension array with size 1 which every time you left click saves the coordinates. Meaning that it is always equal to the last clicked coordinate. Here I find the index the last saved coordinates and delete them from the main List
                    //System.out.println(Arrays.toString(pPoints) + " index of " + position);
                    MapCoordinate.remove(position);
                    count = 0;

                    System.out.println("The last coordinate, " + " longitude: " + pPoints[0] + " latitude:  " + pPoints[1] + " altitude: " + pPoints[2] + " was deleted from the list");
                    System.out.println("");
                } catch (IndexOutOfBoundsException e){
                        System.out.println("No coordinate was found");
                        System.out.println("");
                    }

                } else {

                    double x = (int) reScaleX(event.getX()); //get the x and y coordinates, more on reScaleX() and reScaleY() inside Plot.java
                    double y = (int) reScaleY(event.getY());


                    //System.out.println(Arrays.toString(coos));

                    double altitude = e.getAltitude(x, y); //Again here it gets a bit messy since I saved the coordinates in a weired way. I need to make sure that the coordinate clicked on the map matches with the right saved coordinate
                    if (x > 180) {
                        x -= 180;
                    } else if (x < 180) {
                        x += 180;
                    }
                    Double[] coos = {x, y, altitude};
                    Double[] pCoos = {x, y, altitude};
                    pPoints = pCoos;

                    //System.out.println("pPoints" +  Arrays.toString(pPoints));

                    System.out.println("longitude: " + x + " latitude:  " + y + " at " + altitude + " meters when the sea has risen by " + e.seaLevel + " meters");


                    if (count < 2) { //if the user has clicked more than 2 times this if is not entered
                        points[count] = coos;

                        count++;

                        if (count == 2) { //if two coordinates have been clicked then I calculate the distance between the two.
                            MapCoordinate coosA = new MapCoordinate(points[0][0], points[0][1], points[0][2]);
                            MapCoordinate coosA2 = new MapCoordinate(points[1][0], points[1][1], points[1][2]);
                            double distance = coosA.distanceTo(coosA2); //this method can be found in MapCoordinate.java
                            System.out.println("The distance between the last two clicked coordinates is " + distance + " km");
                            System.out.println("");
                        }

                    } else { //this else is entered only if the user has pressed more than 2 times, resetting the loop
                        count = 1;
                        for (int i = 0; i < 2; i++) {
                            for (int j = 0; j < 2; j++) {
                                points[i][j] = null;
                            }
                        }
                        points[0] = coos;
                    }

                    MapCoordinate.add(pCoos);
                    if (MapCoordinate.size() > 1) { //This if condition is entered if there are more than one saved coordinate
                        for (int j = 0; j < MapCoordinate.size(); j++) { //these two for loops are used to order the map, first by altitude then by latitude and finally by longitude. This is the order that the assignment says in 4C I believe.
                            for (int i = 0; i < MapCoordinate.size() - 1; i++) {
                                Double[] data = MapCoordinate.get(i);
                                Double[] data2 = MapCoordinate.get(i + 1);

                                //System.out.println("data1" + Arrays.toString(data));
                                //System.out.println("data2" + Arrays.toString(data2));

                                MapCoordinate coosA = new MapCoordinate(data[0], data[1], data[2]); //Here I create two objects that are then compared in MapCoordinate.java
                                MapCoordinate coosA2 = new MapCoordinate(data2[0], data2[1], data2[2]);
                                int result = coosA.compareTo(coosA2);

                                if (result == 1) { //if the result of the method compareTo() is 1 then swap the two coordinates in the List. It will make more sense if you check compareTo() inside MapCoordinate.java
                                    MapCoordinate.set(i, data2);
                                    MapCoordinate.set(i + 1, data);
                                }
                            }
                        }
                    }
                }
                String fileName = "C:\\Users\\sidon\\Documents\\University\\CE152\\Java\\src\\Assignment\\write.txt";
                try{ //every time the mouse is clicked the coordinates saved in MapCoordinate are written on the file write.txt
                    writeCoordinates(fileName, MapCoordinate);
                }catch (IOException e){
                    System.out.println("An error occurred.");
                }
            }
        });

        double xmin = 0;
        double ymin = -90;
        double xmax = 360;
        double ymax = 90;
        //this.height = (int) (this.width * (ymax-ymin) / (xmax - xmin));
        this.setScaleX(xmin, xmax);
        this.setScaleY(ymin, ymax);
    }

    public static void writeCoordinates(String fileName, ArrayList<Double[]> MapCoordinate) throws FileNotFoundException { //This method is used to write the coordinates to write.txt
        PrintWriter output = new PrintWriter(fileName);
        output.println(String.format("|%12s|", "LONGITUDE") + String.format("%12s|","LATITUDE") + String.format("%12s|", "ALTITUDE"));

        //output.println("PROVA");

        //output.println(MapCoordinate.size());


        for (int i = 0; i < MapCoordinate.size(); i++) { //this loop iterates over all the saved coordinates
            //output.println("PROVA");
            Double[] coosA = MapCoordinate.get(i);
            String longitude = Double.toString(coosA[0]);
            String latitude = Double.toString(coosA[1]);
            String altitude = Double.toString(coosA[2]);

            output.println(String.format("|%12s|", longitude) + String.format("%12s|", latitude) + String.format("%12s|", altitude)); //adding some format to the text
        }
        output.close();
    }

    @Override
    public void paintComponent(Graphics g){ //paintComponent which paints the Earth
        g.translate(-width/2, 0); //This is why my coordinates are messy. I translate the map to the left forcing me to then move all the coordinates below 180 to the right. When I realized that I could have done it more efficiently, I was already too deep in the assignment to change it.

        for (Double[] coos: e.mapOfEarth.keySet()) { //iterating through the mapOfEarth (all of the coordinates)
            double altitude = e.mapOfEarth.get(coos);
            altitude -= e.seaLevel; //Changing the altitude according to the sea level. Again, the user is asked to provide a new sea level which he can decide to keep default by giving in input 0

            if (altitude > 3400){
                g.setColor(new Color(0xD4D3E8));
            }else if (altitude > 3200){
                g.setColor(new Color(0x341900));
            }else if (altitude > 2800){
                g.setColor(new Color(0x46260C));
            }else if (altitude > 2500){
                g.setColor(new Color(0x573316));
            }else if (altitude > 1500){
                g.setColor(new Color(0x532F0B));
            }else if (altitude > 1000){
                g.setColor(new Color(0x693B12));
            }else if (altitude > 700){
                g.setColor(new Color(0xAEA225));
            }else if (altitude > 400){
                g.setColor(new Color(0xEFDE1A));
            }else if (altitude > 200){
                g.setColor(new Color(0xF0EB1F));
            }else if (altitude > 0){
                g.setColor(new Color(0x2A5800));
            }else if (altitude > -100) {
                g.setColor(new Color(0xD328A7D6));
            }else if (altitude > -400){
                g.setColor(new Color(0x3D7AB5));
            }else if (altitude > -900){
                g.setColor(new Color(0x23589F));
            }else if (altitude > -1300){
                g.setColor(new Color(0x223FAA));
            }else if (altitude > -1800){
                g.setColor(new Color(0x1631A8));
            }else if (altitude > -2000){
                g.setColor(new Color(0x173162));
            }else if (altitude > -2500){
                g.setColor(new Color(0x132158));
            }else if (altitude > -4000){
                g.setColor(new Color(0x151E58));
            }else if (altitude <= -4000){
                g.setColor(new Color(0xF90A0058));
            }

            if ( ( (coos[1] > scaleY(-60) ) && (altitude > 1000) ) ){ //The next if, else if and if are used to change the color of Antarctica and Greenland to white. I know that the colors change according to altitude and not geographical position, but I still thought that it would look better, besides the assignment only really require for two colors so I hope that this won't affect the grade
                g.setColor(new Color(0xFFFBF9));
            }else if ((coos[1] > scaleY(-60)) && (altitude > 0)) {
                g.setColor(new Color(0xCFD3CF));
            }

            if ( (scaleY(60) > coos[1]) && (coos[1] > scaleY(90) ) && ( coos[0] > scaleX(270) ) && ( coos[0] < scaleX(360) ) && (altitude > 600) ){
                g.setColor(new Color(0xFFFBF9));
            }

            /*
            if ((0 < altitude) && (altitude < e.seaLevel)){
                g.setColor(new Color(0xD328A7D6));
            }else if( (altitude < 0) && (altitude > e.seaLevel)){
                g.setColor(new Color(0xF0EB1F));
            }

             */

            g.fillRect((int) Math.round(coos[0]), (int) Math.round(coos[1]), 1, 1); //print rectangles (mimicking pixels) with the smallest possible size I believe. (This method only accepts integers right?)
        }
    }
}
