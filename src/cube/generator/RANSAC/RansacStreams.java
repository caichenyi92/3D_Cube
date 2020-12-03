package cube.generator.RANSAC; /**
 * File: RansacStreams.java
 * @author Shawn Jiang
 * @author Alex "hyukeweaewfahyuk" Rinker
 * @author Edokunzilla Zhousus
 * @author Mathias "DromeStrikeClaw" Syndrome "DaClawOfTheFenix"
 * Class: CS375
 * Project: 3
 * Date: April 3 2017
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * A class to run ransac with streams
 */
public class RansacStreams extends Ransac {
    @Override
    public Line2 computeRansac(ArrayList<Point> points) {
        Line2[] lines = IntStream.range(0, points.size()).parallel().mapToObj(e -> ransac(points, 1)).toArray(s -> new Line2[points.size()]);

        FanInThread[] fanInThreads = new FanInThread[lines.length/2];
        //initial collect and find best fits
        int i = lines.length/2;
        for(int j = 0; j<i; j++) {
            fanInThreads[j] = new FanInThread(lines[j], lines[j+i]);
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
            Line2 line = new RansacStreams().computeRansac(points);

            Drawer drawer = new Drawer(size, size);
            drawer.addLine(line.p1, line.p2);
            drawer.setPoints(points);
            drawer.setVisible(true);
        }
    }
}
