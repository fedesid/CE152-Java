package Assignment;

// affine transformations from user-space coordinates to screen coordinates

import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// transformation of user coordinates to screen coordinates
public class Plot extends JComponent {
	// dimensions of plotting area with default values
	int width = 1440, height = 720; //I really struggled with the dimension of the JFrame and of the printed Earth. I couldn't make it dynamic :/
	// dimensions of user-space coordinates with default values
	double xmin=0,  xmax=1, ymin=0, ymax=1;
	// transformation of coordinates
	public  double scaleX(double x) {
		return (double) (width * (x - xmin) / (xmax - xmin));
	}
	public  double scaleY(double y) {
		return (double) (height * (ymin - y)/(ymax - ymin)+height);
	}

	public double reScaleY(double y){ //reScaleY and reScaleX are used to convert the coordinates of the mouse to the coordinates saved in the mapOfEarth.
		return (ymin - (ymax - ymin)*(y-height)/(height));
	}

	public double reScaleX(double x){
		return (x * (xmax - xmin)/(width) + xmin);
	}
	public  void setScaleX(double min, double max) {
		xmin = min;   xmax = max;   }
	public  void setScaleY(double min, double max) {
		ymin = min;   ymax = max; }

}
