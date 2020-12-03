package cube.generator.RANSAC; /**
 * File: RansacThreads.java
 * @author Shawn Jiang
 * @author Alex Rinker
 * @author Edokun Zhou
 * @author Mathias "DromeStrikeClaw" Syndrome
 * Class: CS375
 * Project: 3
 * Date: April 3 2017
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class to run ransac with multiple threads
 */
public class RansacThreads extends Ransac {
    public static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    @Override
    public Line2 computeRansac(ArrayList<Point> points) {
        //create an array of threads and start them running
        RansacThread[] ransacThreads = new RansacThread[NUM_THREADS];
        FanInThread[] fanInThreads = new FanInThread[NUM_THREADS/2];

        for (int i = 0; i < ransacThreads.length; i++) {
            ransacThreads[i] = new RansacThread(points, points.size()/NUM_THREADS);
            ransacThreads[i].start();
        }

        //wait for all threads to finish
        for (RansacThread t : ransacThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //initial collect and find best fits
        int i = NUM_THREADS/2;
        for(int j = 0; j<i; j++) {
            fanInThreads[j] = new FanInThread(ransacThreads[j].model, ransacThreads[j+i].model);
            fanInThreads[j].start();
        }

        // wait for initial collect from ransac to fan in threads
        for (FanInThread t : fanInThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // finish fan in
        i=i/2;
        while(i > 1) {
            for(int j = 0; j<i; j++) {
                fanInThreads[j] = new FanInThread(fanInThreads[j].result, fanInThreads[j+i].result);
                fanInThreads[j].start();
            }

            for (int j = 0; j<i; j++) {
                try {
                    fanInThreads[j].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            i=i/2;
        }

        return fanInThreads[0].result;
    }

    /**
     * Thread to perform ransac with a certain amount of iterations
     */
    private class RansacThread extends Thread {
        /** model result from ransac */
        private Line2 model;
        /** Points to perform ransac upon */
        private ArrayList<Point> points;
        /** number of lines to ransac */
        private int iterations;

        private RansacThread(ArrayList<Point> points, int iterations) {
            this.points = points;
            this.iterations = iterations;
        }

        @Override
        public void run() {
             this.model = ransac(this.points, iterations);
        }

    }

    /** Thread to assist in fanning in the best fit*/
    private class FanInThread extends Thread {
        /** First line */
        private Line2 l1;
        /** Second line */
        private Line2 l2;
        /** The line with more inliers */
        private Line2 result;

        private FanInThread(Line2 l1, Line2 l2) {
            this.l1 = l1;
            this.l2 = l2;
        }

        @Override
        public void run() {
            if(l1.fit > l2.fit) {
                result = l1;
            } else {
                result = l2;
            }
        }
    }

    /**
     * test code
     * @param args
     */
    public static void main(String[] args) {
        for(int j=0; j< 50; j++) {
            ArrayList<Point> points = new ArrayList<>();
            Random random = new Random();
            int size = 1000;

            for (int i = 0; i < 200; i++) {
                points.add(new Point(random.nextInt(size), random.nextInt(size)));
            }
            for (int i = 0; i < 1000; i++) {
                points.add(new Point(random.nextInt(size), 500));
            }
            Line2 line = new RansacThreads().computeRansac(points);

            Drawer drawer = new Drawer(size, size);
            drawer.addLine(line.p1, line.p2);
            drawer.setPoints(points);
            drawer.setVisible(true);
        }
    }
}
