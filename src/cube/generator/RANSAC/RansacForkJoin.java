package cube.generator.RANSAC; /**
 * File: RansacForkJoin.java
 * @author Shawn Jiang
 * @author Alex "hyukhyuk" Rinker
 * @author Edokunzilla Zhou
 * @author Mathias "DromeStrikeClaw" Syndrome
 * Class: CS375
 * Project: 3
 * Date: April 3 2017
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * A class to run ransac with fork join threads
 */
public class RansacForkJoin extends Ransac {
    public static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    @Override
    public Line2 computeRansac(ArrayList<Point> points) {
        Line2[] results = new Line2[NUM_THREADS];
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new RansacAction(points, results, 0, NUM_THREADS-1));

        FanInThread[] fanInThreads = new FanInThread[NUM_THREADS/2];
        //initial collect and find best fits
        int i = NUM_THREADS/2;
        for(int j = 0; j<i; j++) {
            fanInThreads[j] = new FanInThread(results[j], results[j+i]);
            fanInThreads[j].start();
        }

        //wait for initial collect
        for (FanInThread t : fanInThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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

    /** Recursive class to contain recursive method */
    class RansacAction extends RecursiveAction {
        private ArrayList<Point> points;
        private Line2[] results;
        /** The low bound index */
        private int low;
        /** The high bound index*/
        private int high;

        public RansacAction(ArrayList<Point> points, Line2[] r, int low, int high) {
            this.points = points;
            results = r;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (this.low == this.high) {
                results[this.low] = ransac(points, points.size()/NUM_THREADS);

            } else { // fork the work into two tasks for other threads
                int h = (int)(this.high-(Math.ceil(((double)(this.high-this.low)/2))));
                RansacAction left = new RansacAction(points, results, this.low, h);
                RansacAction right = new RansacAction(points, results, h+1, this.high);
                invokeAll(left, right);
            }
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
     * test dat code
     * @param args
     */
    public static void main(String[] args) {
        for(int j=0; j< 1; j++) {
            ArrayList<Point> points = new ArrayList<>();
            Random random = new Random();
            int size = 1000;

            for (int i = 0; i < 200; i++) {
                points.add(new Point(random.nextInt(size), random.nextInt(size)));
            }
            for (int i = 0; i < 1000; i++) {
                points.add(new Point(random.nextInt(size), 500));
            }
            Line2 line = new RansacForkJoin().computeRansac(points);

            Drawer drawer = new Drawer(size, size);
            drawer.addLine(line.p1, line.p2);
            drawer.setPoints(points);
            drawer.setVisible(true);
        }
    }
}
