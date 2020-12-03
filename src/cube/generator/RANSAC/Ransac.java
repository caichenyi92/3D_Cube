package cube.generator.RANSAC; /**
 * File: Ransac.java
 * @author Shawn Jiang
 * @author Alex Rinker
 * @author Ed Zhou
 * @author Mathias "DromeStrikeClaw" Syndrome
 * Class: CS375
 * Project: 3
 * Date: April 3 2017
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class for finding a line given a list of points
 */

public abstract class Ransac {
    /** The max distance from the line a point can be to be counted as within the fit */
    final static int MAXDIST = 1;

    /**
     * finds the intercept of the normal from a point to the estimated lines model
     * @param line the line
     * @param point the point being compared
     * @return intercept on the line
     */
    public Point findIntercept(Line2 line, Point point ) {
        double x,y;

        x = (point.getX() + line.m*point.getY() - line.m*line.b)/(1 + line.m*line.m);
        y = (line.m*point.getX()+(line.m*line.m)*point.getY()-(line.m*line.m)*line.b)/(1+line.m*line.m)+line.b;

        return new Point((int)x,(int)y);
    }


    /** the ransac algorithm
     * @param points list of points to run ransac on
     * @param iterations the number of times ransac will be performed
     * @return
     */
    public Line2 ransac(ArrayList<Point> points, int iterations) {
        // base model to be replaced by algorithm
        Line2 model = new Line2(points.get(0), points.get(points.size()-1));
        Random random = new Random();

        //Perform RANSAC iterations n times!~~~~
        for(int i = 0;i < iterations; i++) {
            // indices of points
            int r0 = 0;
            int r1 = 0;

            // find two unique random indices
            while(points.get(r0).getX() == points.get(r1).getX()) {
                r0 = random.nextInt(points.size());
                r1 = random.nextInt(points.size());
            }

            // the tested points
            ArrayList<Point> testPoints = new ArrayList<>();

            // add n points to test line fitness
            for(int j = 0; j < points.size(); j++){
                if(j != r0 && j != r1){
                    testPoints.add(points.get(j));
                }
            }

            //find a lines model for the randomly selected points
            Line2 tmpmodel = new Line2(points.get(r0), points.get(r1));

            //find orthogonal lines to the model for all other given points
            for(int j = 0; j < testPoints.size(); j++){

                Point p0 = testPoints.get(j);

                //find an intercept point of the model
                Point p1 = findIntercept(tmpmodel, p0);

                // distance from point to the model
                double dist = Math.pow(Math.pow((p1.x - p0.x),2) + Math.pow((p1.y - p0.y),2),.5);

                // check whether it's an inlier or not
                if(dist < MAXDIST) {
                    tmpmodel.fit++;
                }
            }
            // replace model with better fit line
            if(tmpmodel.fit >= model.fit){
                model = tmpmodel;
            }
        }

        return model;
    }

    /**
     * computeRansac the ransac algorithm
     */
    public abstract Line2 computeRansac(ArrayList<Point> points);
}
