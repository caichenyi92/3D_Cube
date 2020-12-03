package cube.generator.RANSAC; /**
 * File: Driver.java
 * @author Dale Skrien
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
 * A driver for testing subclasses of Ransac.
 */

public class Driver {
    /** The runtime of the sequential run of the algorithm */
    static long sequentialRuntime = 0;

    /** Number of available processors */
    final static int NUM_PROCESSORS = Runtime.getRuntime().availableProcessors();

    /** Size of the window for display results */
    final static int windowSize = 700;

    /** Points to perform the algorithm upon*/
    private static ArrayList<Point> points;

    public static void main(String[] args) throws Exception {
        // initialize a window
        Drawer drawer = new Drawer(windowSize, windowSize);

        // generate points
        points = new ArrayList<>();
        Random random = new Random();
        int size = 700;

        for (int i = 0; i < size/5; i++) { // generate random points
            points.add(new Point(random.nextInt(size), random.nextInt(size)));
        }
        for (int i = 0; i < size; i++) { // generate line from points
            points.add(new Point(random.nextInt(size), windowSize/2));
        }
        drawer.setPoints(points);

        // tests
        System.out.println("Number of processors: " + NUM_PROCESSORS);
        test("Sequential version", new RansacSequential(), drawer);
        test("Threads version", new RansacThreads(), drawer);
        test("Fork-join version", new RansacForkJoin(), drawer);
        test("Parallel streams version", new RansacStreams(), drawer);
        drawer.setVisible(true);
    }

    private static void test(String version, Ransac p, Drawer drawer) throws Exception {
        // warm-up
        for(int i = 0; i<NUM_PROCESSORS/2;i++) {
            p.computeRansac(points);
        }
        // computeRansac results
        Timer.start();
        Line2 result = p.computeRansac(points);
        Timer.stop();

        // output the results
        System.out.println("--------" + version + "----------");
        System.out.println("Line: " + result.p1 +" to "+ result.p2);
        // output the time needed to get an estimated line!
        System.out.println("Time: " + Timer.getRuntime() + "ms");

        // output the speedup
        if (sequentialRuntime == 0) {
            sequentialRuntime = Timer.getRuntime(); //sequential time
        }
        else {
            System.out.printf("Speed-up: %.2f\n", sequentialRuntime / 1.0 / Timer
                    .getRuntime());
        }

        //add the line to the graphics window
        drawer.addLine(result.p1, result.p2);
        System.out.println();
    }
}
