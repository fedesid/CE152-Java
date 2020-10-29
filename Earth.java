package Assignment;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class Earth extends Plot {

    public static void main(String[] args) throws FileNotFoundException { //MAIN where I call the methods

        String file = new String ("C:\\Users\\sidon\\Documents\\University\\CE152\\Java\\src\\Assignment\\earth.xyz");
        Earth earth = new Earth();
        earth.readDataMap(file);
        earth.readDataArray(file);
    }

    private double[][] arrayOfEarth;        //A few global variables called throughout the whole program
    HashMap<Double[], Double> mapOfEarth;
    double seaLevel = 0;
    int lineLength = 0;

//EXERCISE 1
    //PART 1A
    public void readDataArray(String filename){ //Method that reads the file and saves all the coordinates and altitudes in an array of two dimensions
        try{
            Scanner file = new Scanner(new File(filename));
            Scanner file1 = new Scanner(new File(filename));

            int fileLenght = 0;

            while (file1.hasNextDouble()){ //While loop used to count the number of lines in the file

                String line = file1.nextLine();
                fileLenght++;
            }
            this.arrayOfEarth = new double[fileLenght][3]; //initializing the array with the correct size

            int i = 0;
            while (file.hasNextDouble()){ //In this while loop I read one line at a time, split it, save the content in a single dimension array, and then add it to the two dimensional array
                String firstLine = file.nextLine();
                String[] line = firstLine.split("\\t");

                int j = 0;
                for (String coo: line){
                    double coordinate = Double.parseDouble(coo);
                    arrayOfEarth[i][j] = coordinate;
                    j++;
                    }
                i++;
            }

            //PART 1C
            String answer = "";
            while(!(answer.equals("quit"))){ //Here I make the "menus" giving the user several options

                Scanner userAnswer = new Scanner(System.in);
                System.out.println("");
                System.out.println("(Command) Action");
                System.out.println("(1) Percentage coordinates above");
                System.out.println("(2) Percentage coordinates below");
                System.out.println("(3) Find altitude with coordinates");
                System.out.println("(4) Generate a random map");
                System.out.println("(5) Print the Earth");
                System.out.println("(quit) Exit the program");
                System.out.println("");
                answer = userAnswer.nextLine();

                try{ //Check if the user input is correct (a number)

                    Double userInput = Double.valueOf(answer);

                    if (userInput == 1) { //Check for altitudes above
                        userAnswer = new Scanner(System.in);
                        System.out.println("Please enter an altitude");
                        answer = userAnswer.nextLine();

                        try {
                            userInput = Double.valueOf(answer);
                            percentageAbove(userInput);
                        } catch (Exception e) {
                            System.out.println("Not a valid altitude. Please enter an altitude or quit to end program.");
                        }
                    }else if (userInput == 2){ //Check for altitudes below
                        userAnswer = new Scanner(System.in);
                        System.out.println("Please enter an altitude");
                        answer = userAnswer.nextLine();

                        try {
                            userInput = Double.valueOf(answer);
                            percentageBelow(userInput);
                        } catch (Exception e) {
                            System.out.println("Not a valid altitude. Please enter an altitude or quit to end program.");
                        }

                    }else if (userInput == 3){ //Find altitude given coordiantes
                        userAnswer = new Scanner(System.in);
                        System.out.println("Please enter a longitude (0-360) and latitude (-90-90):");
                        answer = userAnswer.nextLine();

                        try{
                            String coos[] = answer.split("\\s+");
                            double longitude = Double.valueOf(coos[0]);
                            double latitude = Double.valueOf(coos[1]);
                            double fLongitude = longitude;


                            if ( (longitude <= 180) && (longitude >= 0) ){ //This is to align the input of the user with how I save the coordinates in the array. It is a bit messy but it works
                                longitude = longitude + 180;
                            }else if ( (longitude > 180) && (longitude <= 360) ){
                                longitude -= 180;
                            }

                            //System.out.println(longitude + " " + latitude);

                            double altitude = getAltitude(longitude, latitude);

                            if (altitude%(9*8*7*6*5*4*3*2) == 0){ //More on this in getAltitude()
                                System.out.println("Please enter valid longitude/latitude or quit to end program.");
                            }else {
                                System.out.println("The altitude at longitude " + fLongitude + " and latitude " + latitude + " is " + altitude + " meters.");
                            }

                        } catch (Exception e){
                            System.out.println("Please enter valid longitude/latitude or quit to end program.");
                        }

                    }else if (userInput == 4){ //Generate a random map

                        JFrame f = new JFrame();
                        double resolution = 0.25;
                        int wid = (int) (360/resolution);
                        int hei = (int) (180/resolution);
                        f.getContentPane().setPreferredSize(new Dimension(wid/2, hei));
                        PlotRandomMap prm = new PlotRandomMap(0.5);
                        f.add(prm);
                        f.pack();
                        f.setLocationRelativeTo(null);
                        String title = "Random Map";
                        f.setTitle(title);
                        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        f.setVisible(true);

                    } else if (userInput == 5){ //Prints the Earth
                        String file2 = new String ("C:\\Users\\sidon\\Documents\\University\\CE152\\Java\\src\\Assignment\\earth.xyz");
                        JFrame f = new JFrame();
                        PlotMap pm = new PlotMap(file2);

                        double resolution = 0.25;
                        int wid = (int) (360/resolution);
                        int hei = (int) (180/resolution);
                        f.getContentPane().setPreferredSize(new Dimension(wid, hei));

                        f.add(pm);
                        f.pack();




                        // make frame a bit larger than plotting area
                        //f.getContentPane().setPreferredSize(new Dimension(720,360));

                        f.setLocationRelativeTo(null);
                        String title = "Earth";
                        f.setTitle(title);

                        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        f.setVisible(true);
                    }

                } catch (Exception e){ //The catch is entered if the user hasn't typed a number
                    if (answer.equals("quit")){ //If the user input is "quit" then end the program
                        System.out.println("Bye!");
                        break;
                    }
                    //System.out.println(e);
                    System.out.println("Not a valid input. Enter quit to end program.");
                }
            }

        }catch(FileNotFoundException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    //PART 1B
    public List<Double> coordinatesAbove(double altitude){ //Method that saves all coordinates above a given altitude in a list. This method is implemented in percentageAbove
        List<Double> coordinatesAbove = new ArrayList<>();
        for(int i=0; i<arrayOfEarth.length; i++){
            double[] coordinates = arrayOfEarth[i];
            if (coordinates[2] > altitude){
                coordinatesAbove.add(coordinates[2]);
            }
        }
        return coordinatesAbove;
    }

    //PART 1B
    public List<Double> coordinatesBelow(double altitude){ //Method that saves all coordinates above a given altitude in a list. This method is implemented in  percentageBelow
        List<Double> coordinatesBelow = new ArrayList<>();
        for(int i=0; i<arrayOfEarth.length; i++){
            double[] coordinates = arrayOfEarth[i];
            if (coordinates[2] < altitude){
                coordinatesBelow.add(coordinates[2]);
            }
        }
        return coordinatesBelow;
    }

    //PART 1B
    public void percentageBelow(double altitude){ //This method calculates the percentage of coordinates below a given altitude
        List<Double> coordinatesBelow = coordinatesBelow(altitude);
        double percentage = ((double)(coordinatesBelow.size())/(arrayOfEarth.length))*100; //How many coordinates are below a given one divided by the total umber of coordinates
        DecimalFormat df = new DecimalFormat("#.#");
        percentage = Double.valueOf(df.format(percentage));

        if (percentage == 0){
            System.out.println("There are no coordinates below " + altitude);
        }else{
            System.out.println("Percentage of coordinates below " + altitude + " meters: "+ percentage+"%");
        }

    }

    //PART 1B
    public void percentageAbove(double altitude){ //This method calculates the percentage of coordinates above a given altitude
        List<Double> coordinatesAbove = coordinatesAbove(altitude);
        double percentage = ((double)(coordinatesAbove.size())/(arrayOfEarth.length))*100; //How many coordinates are above a given one divided by the total umber of coordinates
        DecimalFormat df = new DecimalFormat("#.#");
        percentage = Double.valueOf(df.format(percentage));

        if (percentage == 0){
            System.out.println("There are no coordinates above " + altitude);
        }else{
            System.out.println("Percentage of coordinates above " + altitude + " meters: "+ percentage+"%");
        }
    }

//EXERCISE 2
    //PART 2A
    public void readDataMap(String filename) { //This method is used to save the coordinates from the file in a HashMap. The longitude and latitude are used as key, the altitude as the value.
        try {

            Scanner file = new Scanner(new File(filename));
            this.mapOfEarth = new HashMap<Double[], Double>();

            double xmin = 0;
            double ymin = -90;
            double xmax = 360;
            double ymax = 90;
            this.height = (int) (this.width * (ymax-ymin) / (xmax - xmin));
            this.setScaleX(xmin, xmax);
            this.setScaleY(ymin, ymax);

            while(file.hasNextDouble()){
                String line = file.nextLine();
                String coosA[] = line.split("\\t");
                double longitude = Double.valueOf(coosA[0]);
                if (longitude < 180){ //Here I shift all the coordinates below 180 by 360. I do so because I then translate the map to the left in the paintComponent. Again, it could have been much simplier but it works.
                    longitude = longitude + 360;
                }

                longitude = this.scaleX(longitude);
                double latitude = this.scaleY(Double.valueOf(coosA[1]));
                double altitude = Double.valueOf(coosA[2]);

                Double[] coos = new Double[]{longitude, latitude};
                mapOfEarth.put(coos, altitude);
            }




        } catch (FileNotFoundException e) {
            System.out.println("An error occurred");
        }
    }

    //PART 2B
    public void generateMap(double resolution){ //Here I generate a random map with random altitudes
        mapOfEarth = new HashMap<Double[], Double>();
        int xaxis = (int) (360/resolution);
        int yaxis = (int) (180/resolution);

        double xmin = 0;
        double ymin = 0;
        double xmax = xaxis;
        double ymax = yaxis;
        this.height = (int) (this.width * (ymax-ymin) / (xmax - xmin));
        this.setScaleX(xmin, xmax);
        this.setScaleY(ymin, ymax);

        for(int i=0; i<yaxis; i++){
            for(int j=0; j<xaxis; j++){
                Random r = new Random();
                Double[] coos = new Double[]{(double) j, (double) i};
                double longitude = Double.valueOf(coos[0]);
                if (longitude < 180){
                    longitude = longitude + 360;
                }

                longitude = this.scaleX(longitude);
                coos[0] = longitude;
                double latitude = this.scaleY(Double.valueOf(coos[1]));
                coos[1] = latitude;
                double altitude = r.nextInt(6000 + 6000) - 6000;
                //System.out.println(Arrays.toString(coos) + " " + altitude);
                mapOfEarth.put(coos, altitude);
            }
        }
    }

    //PART 2C
    public double getAltitude(double longitude, double latitude){ //This method finds the altitude of a given pair of coordinates
        double altitude = 1;
        longitude = longitude + 180;
        //System.out.println("INSIDE: " + longitude);
        longitude = this.scaleX(longitude);
        latitude = this.scaleY(Double.valueOf(latitude));

            for (Double[] coos : mapOfEarth.keySet()) { //this for is used to iterate through the whole map trying to find the matching coordinates
                if (coos[0] == longitude && coos[1] == latitude) {
                    altitude = mapOfEarth.get(coos);
                    //System.out.println("Inside get: " + longitude + ", " + latitude);
                    //System.out.println("GOT RESULT " + Arrays.toString(coos));
                    //System.out.println("Altitude: " + altitude);
                    return altitude - seaLevel; //Here I return the altitude minus the seaLevel which by default is 0
                }
            }

        altitude = altitude*9*8*7*6*5*4*3*2; //If no coordinate is found in the map, I multiply the altitude (default value of 1) by 9! which I then check above if the altitude%9! == 0, if it is true I know that no altitude was found.
        return altitude;
    }
}
