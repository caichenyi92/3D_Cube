package cube.generator.RANSAC; /**
 * File: Drawer.java
 * @author Shawn Jiang
 * @author Alex Rinker
 * @author Ed Zhou
 * @author Mathias "DromeStrikeClaw" Syndrome
 * Class: CS375
 * Project: 3
 * Date: April 3 2017
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Simple canvas class for displaying added points and lines */
class Drawer extends JFrame{
    /** The points to draw on the canvas */
    List<Point> points;
    /** The lines to draw on the canvas */
    List<Line2D> lines;

    /**
     * Initialize the class with canvas size x, y
     * @param x width of the canvas
     * @param y height of the canvas
     */
    public Drawer(int x,int y){
        JPanel panel=new JPanel();
        getContentPane().add(panel);
        setSize(x,y);
        lines = new ArrayList<>();
    }

    /**
     * Sets the points to be drawn on the canvas
     * @param points A list of points
     */
    public void setPoints(List<Point> points) {
        this.points = points;
    }

    /**
     * add a line to be drawn on the canvas
     * @param p1 point 1
     * @param p2 point 2
     */
    public void addLine(Point p1, Point p2) {
        this.lines.add(new Line2D.Double(p1.getX(),p1.getY(),p2.getX(),p2.getY()));
    }

    /**
     * Draw points and lines on the canvas
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        Random random = new Random();
        points.forEach(e -> g2.drawOval((int)e.getX(),(int)e.getY(),2,2));
        g2.setStroke(new BasicStroke(20));
        g2.setColor(Color.RED);
        lines.forEach(e -> {
            int seed = lines.indexOf(e);
            g2.setStroke(new BasicStroke(20 + 5 * seed));
            g2.setColor(
                    new Color(random.nextInt(255),
                              random.nextInt(255),
                              random.nextInt(255),
                              150));
            g2.draw(e);
        });
    }
}