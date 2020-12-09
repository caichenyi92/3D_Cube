package cube.generator;

import cube.generator.elements.WallWithWindow;
import cube.generator.parameterize.GeoLines;
import cube.generator.parameterize.GeoReconstructor;
import gzf.gui.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Line;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Transform3D;
import wblut.processing.WB_Render3D;

import java.util.ArrayList;

public class Test_Lines extends PApplet {

    WallWithWindow www;
    ArrayList<WB_Point> pts_on_www;
    ArrayList<WB_Point> ptsprojX_trans;

    GeoLines ls;
    WB_Line l;
    WB_Line l1;
    ArrayList<WB_Line> sublsX;
    ArrayList<WB_Line> sublsY;

    GeoReconstructor gr;

    ArrayList<WB_Polygon> filtersX;
    ArrayList<WB_Polygon> filtersY;


    WB_Render3D render;
    CameraController cam;

    public void setup() {
        size(1000, 1000, P3D);
        render = new WB_Render3D(this);
        cam = new CameraController(this, 400);
//        cam.top();

        www = new WallWithWindow(6000);
        pts_on_www = new ArrayList<>();
        pts_on_www = www.getRan_pts_on();
        ls = new GeoLines(pts_on_www);
        l = ls.getMainlineXY();
        l1 = ls.getMainlineYZ();
        sublsX = new ArrayList<>();
        sublsX= ls.getSublinesX();
        System.out.println("subls X size : " + sublsX.size());

        sublsY = new ArrayList<>();
        sublsY = ls.getSublinesY();
        System.out.println("sublsY size: " + sublsY.size());

        filtersX = new ArrayList<>();
        for(int i = 0;i<4;i++){
            filtersX.add(ls.getMostptfiltersXY().get(i).getFilterbase());
        }
        filtersY = new ArrayList<>();
        for(int i = 0; i<4;i++){
            filtersY.add(ls.getMostptfiltersYZ().get(i).getFilterbase());
        }
        WB_Transform3D T = new WB_Transform3D(WB_Point.ZERO(), WB_Point.X(), WB_Point.ZERO(), new WB_Point(0,0,1));
        //WB_Transform3D T = new WB_Transform3D();
       // T.addRotateAboutAxis(Math.PI/2,WB_Point.ZERO(),WB_Point.Y());
        ptsprojX_trans = GeoLines.ptsTrans(ls.getPtsprojectionX(),T);
        gr = new GeoReconstructor(ls);
    }

    public void draw() {
        background(255);
//        www.display(render);
        render.drawPoint(pts_on_www);
        render.drawPoint(ls.getPtsprojection());
        render.drawPoint(ls.getPtsprojectionX());
//        render.drawPoint(ptsprojX_trans);
        render.drawLine(new WB_Line(0, 0, 0, 1, 0, 0), 2);
        render.drawLine(new WB_Line(0, 0, 0, 0, 1, 0), 2);
        render.drawLine(new WB_Line(0, 0, 0, 0, 0, 1), 2);
        pushStyle();
        stroke(255, 0, 0);
        render.drawLine(l, 500);
        render.drawLine(l1,1000);
        popStyle();

        pushStyle();
        stroke(0,100,255);
//        render.drawPolygonEdges(filtersX);
//        render.drawPolygonEdges(filtersY);
        for(WB_Line l:sublsX)
            render.drawLine(l,200);
        for(WB_Line l:sublsY)
            render.drawLine(l,200);
//        render.drawPolygonEdges(new WB_Polygon(ls.getBaseAABB().getCorners()));
        popStyle();

        pushStyle();
        stroke(100,0,255);
        render.drawPoint(gr.getPtsX(),5);
        render.drawPoint(gr.getPtsZ(),5);
        render.drawPoint(gr.getOrigin(),2);
//        render.drawLine(ls.getSublinesX().get(0),100);
        popStyle();

        pushStyle();
        stroke(255,0,100);
        render.drawLine(ls.getSublinesY().get(0),100);
        popStyle();
    }

    public static void main(String[] args) {
        PApplet.main("cube.generator.Test_Lines");
    }

}
