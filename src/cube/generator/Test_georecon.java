package cube.generator;

import cube.generator.elements.WallWithWindow;
import cube.generator.parameterize.GeoAdjust;
import cube.generator.parameterize.GeoLines;
import cube.generator.parameterize.GeoReconstructor;
import gzf.gui.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.processing.WB_Render3D;

import java.util.ArrayList;

public class Test_georecon extends PApplet {

   WallWithWindow www;
    ArrayList<WB_Point> pts_on_www;

    GeoLines ls;

    GeoReconstructor gr;

    GeoAdjust ga;

    WB_Render3D render;
    CameraController cam;

    public void setup(){
        size(1000,1000,P3D);
        render = new WB_Render3D(this);
        cam = new CameraController(this,400);
        cam.openLight();

        www = new WallWithWindow(3000);
        pts_on_www = www.getRan_pts_on();

        ls = new GeoLines(pts_on_www);
        gr = new GeoReconstructor(ls);
        ga = new GeoAdjust(gr.getOrigin(),gr.getThick(),gr.getX1(),gr.getX2(),gr.getX3(),gr.getZ1(),gr.getZ2(),gr.getZ3());

    }

    public void draw(){
        background(255);
        render.drawPoint(pts_on_www);
//        render.drawPolygonEdges(ga.getBaseXZ());
//        render.drawEdges(ga.getShape());
        pushStyle();
        noStroke();
        fill(255,100,0,100);
        render.drawFaces(ga.getShape());
        popStyle();

    }

    public static void main(String[] args) {
        PApplet.main("cube.generator.Test_georecon");
    }

}
