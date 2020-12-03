package cube.generator.RANSAC; /**
 * File: Line2.java
 * @author Shawn Jiang
 * @author Alex Rinker
 * @author Ed Zhou
 * @author Mathias "DromeStrikeClaw" Syndrome
 * Class: CS375
 * Project: 3
 * Date: April 3 2017
 */

import java.awt.*;

/**
 * Simple storage class to hold data of a line for ransac
 */
public class Line2 {
    /** Slope of the line */
    public double m;
    /** intercept of the line */
    public double b;
    /** Point 1 of the line */
    public Point p1;
    /** Point 2 of the line */
    public Point p2;
    /** The number of points within a threshold radius of the line (defined by ransac)*/
    public int fit;

    /**
     * Default Constructor
     */
    public Line2(){
        this.m = 0;
        this.b = 0;
    }

    /**
     * Establish Line2 based on two points
     * @param p1 point 1
     * @param p2 point 2
     */
    public Line2(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.fit = 0;
        this.m = ((p2.getY() - p1.getY()) / (p2.getX() - p1.getX()));
        // slope (gradient) of the lines
        this.b = p2.getY() - this.m * p2.getX();
    }
}
