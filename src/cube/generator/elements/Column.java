package cube.generator.elements;

import nct.CFile;
import nct.Cgeo;
import processing.opengl.PGraphicsOpenGL;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.hemesh.HEC_FromPolygons;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Column implements Element {
    ArrayList<WB_Point> rand_pts_inside;
    ArrayList<WB_Point> rand_pts_on;
    int pts_num;
    HE_Mesh column;

    public Column(int pts_num) {
        this.pts_num = pts_num;
        setColumn();
        setRand_pts_on();
    }

    public void setColumn() {
        double w = 5 + Math.random() * 25;
        double h = 5 + Math.random() * 25;
        double height = 50 + Math.random() * 250;
        WB_Point p0 = new WB_Point(0, 0, 0);
        WB_Point p1 = new WB_Point(w, 0, 0);
        WB_Point p2 = new WB_Point(w, h, 0);
        WB_Point p3 = new WB_Point(0, h, 0);
        WB_Polygon base = new WB_Polygon(p0, p1, p2, p3);
        ArrayList<WB_Polygon> faces = Cgeo.extrudeBase(base, new WB_Vector(0, 0, 1), height);
        HEC_FromPolygons creator = new HEC_FromPolygons(faces);
        column = creator.create();
        translateMesh();
    }

    void rotateMesh() {
        Random rand = new Random();
        column.rotateAboutAxisSelf(Math.random() * Math.PI, 0, 0, 0, rand.nextInt(2), rand.nextInt(2), rand.nextInt(2));
    }

    void translateMesh() {
        Cgeo.translateMesh3D(column, 500);
    }

    public void setRand_pts_inside() {
        rand_pts_inside = Cgeo.randomPtsInMesh(pts_num, column);
    }

    public void setRand_pts_on() {
        rand_pts_on = Cgeo.randomPtsOnMesh(pts_num, column);
    }

    @Override
    public void saveToCsv(String path) throws IOException {
        CFile.savePtsToCsv("column",path,rand_pts_on);
    }

    @Override
    public void display(WB_Render3D render) {
        PGraphicsOpenGL app = render.getHome();
        app.pushStyle();
        app.stroke(0,255,100);
        render.drawEdges(column);
        render.drawPoint(rand_pts_on);
        app.popStyle();
    }
}
