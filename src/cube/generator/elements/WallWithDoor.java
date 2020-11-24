package cube.generator.elements;

import javafx.beans.binding.DoubleExpression;
import nct.CFile;
import nct.Cgeo;
import processing.opengl.PGraphicsOpenGL;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class WallWithDoor implements Element {

    int pts_num;
    ArrayList<WB_Point> rand_pts_on;
    ArrayList<WB_Point> rand_pts_inside;
    HE_Mesh wallwithdoor;

    public WallWithDoor(int pts_num) {
        this.pts_num = pts_num;
        setWallwithdoor();
        setRand_pts_on();
//        setRand_pts_inside();
    }

    public void setWallwithdoor() {
        double w = 50 + Math.random() * 250;
        double h = 50 + Math.random() * 250;
        double thick = 5 + Math.random() * 25;
        WB_Point p0 = new WB_Point(0, 0, 0);
        WB_Point p5 = new WB_Point(w, 0, 0);
        WB_Point p6 = new WB_Point(w, 0, h);
        WB_Point p7 = new WB_Point(0, 0, h);

        double d_hei = h / 2 + Math.random() * h / 6;
        double d_wid = d_hei / 2;

        if (d_wid > w) {
            d_wid = 0.8 * w;
            System.out.println("d_wid_update : " + d_wid);
        }

        double p1x = 5 + Math.random() * (w - d_wid - 10);
        double p2x = p1x + d_wid;
        WB_Point p1 = new WB_Point(p1x, 0, 0);
        WB_Point p2 = new WB_Point(p1x, 0, d_hei);
        WB_Point p3 = new WB_Point(p2x, 0, d_hei);
        WB_Point p4 = new WB_Point(p2x, 0, 0);
        WB_Polygon base = new WB_Polygon(p0, p1, p2, p3, p4, p5, p6, p7);
        ArrayList<WB_Polygon> allfaces = Cgeo.extrudeBase(base, new WB_Vector(0, 1, 0), thick);
        HEC_FromPolygons creator = new HEC_FromPolygons(allfaces);
        wallwithdoor = creator.create();
        translateMesh();

    }

    void rotateMesh() {
        Cgeo.rotateMesh(wallwithdoor);
    }

    void translateMesh() {
        Cgeo.translateMesh3D(wallwithdoor, 500);
    }

    public void setRand_pts_on() {
        rand_pts_on = Cgeo.randomPtsOnTriangles(pts_num,wallwithdoor);
//        rand_pts_on = Cgeo.randomPtsOnMesh(pts_num, wallwithdoor);
    }

    void setRand_pts_inside(){
        rand_pts_inside = Cgeo.randomPtsInMesh(pts_num,wallwithdoor);
    }

    @Override
    public void saveToCsv(String path) throws IOException {
        CFile.savePtsToCsv("wallwd", path, rand_pts_on);

    }

    @Override
    public void display(WB_Render3D render) {
        PGraphicsOpenGL app = render.getHome();
        app.pushStyle();
        app.stroke(0, 100, 100);
        render.drawEdges(wallwithdoor);
        render.drawPoint(rand_pts_on, 2);
        app.popStyle();
    }


}
