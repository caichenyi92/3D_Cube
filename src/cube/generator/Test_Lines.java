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
    ArrayList<WB_Line> subls;

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
        subls = new ArrayList<>();
        subls= ls.getSublinesX();
        subls.addAll(ls.getSublinesY());
        System.out.println("subls size: " + subls.size());

        filtersX = new ArrayList<>();
        for(int i = 0;i<4;i++){
            filtersX.add(ls.getMostptfiltersXY()[i].getFilterbase());
        }
        filtersY = new ArrayList<>();
        for(int i = 0; i<4;i++){
            filtersY.add(ls.getMostptfiltersYZ()[i].getFilterbase());
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
        stroke(100,255,0);
//        render.drawPolygonEdges(filtersX);
//        render.drawPolygonEdges(filtersY);
        for(WB_Line l:subls){
            render.drawLine(l,200);
        }
        popStyle();

        pushStyle();
        stroke(255,0,100);
        render.drawPoint(gr.getOrigin(),10);
        popStyle();
    }

    public static void main(String[] args) {
        PApplet.main("cube.generator.Test_Lines");
    }


}
