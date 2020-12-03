package cube.generator.RANSAC; /**
 * File: RansacSequential.java
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
 * Class to run ransac sequentially
 */
public class RansacSequential extends Ransac {
    @Override
    public Line2 computeRansac(ArrayList<Point> points) {
        return ransac(points, points.size());
    }

    /**
     * test code
     * @param args
     */
    public static void main(String[] args) {
        ArrayList<Point> points = new ArrayList<>();
        Random random = new Random();
        int size = 1000;

        for (int i = 0; i < 200; i++) {
            points.add(new Point(random.nextInt(size), random.nextInt(size)));
        }
        for (int i = 0; i < 1000; i++) {
            points.add(new Point(random.nextInt(size), 500));
        }
        Line2 line = new RansacSequential().computeRansac(points);

        Drawer drawer = new Drawer(size, size);
        drawer.addLine(line.p1, line.p2);
        drawer.setPoints(points);
        drawer.setVisible(true);
    }
}
