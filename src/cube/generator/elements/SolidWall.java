package cube.generator.elements;

import nct.CFile;
import nct.Cgeo;
import processing.opengl.PGraphicsOpenGL;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Transform3D;
import wblut.geom.WB_Vector;
import wblut.hemesh.HEC_FromPolygons;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SolidWall implements Element {
    int pts_num;
    ArrayList<WB_Point> rand_pts_inside;
    ArrayList<WB_Point> rand_pts_on;
    HE_Mesh solidwall;

    public SolidWall(int pts_num) {
        this.pts_num = pts_num;
        setSolidwall();
//        setRand_pts_inside();
        setRand_pts_on();
    }

    public void setSolidwall() {
        double w = 50 + Math.random() * 250;
        double h = 50 + Math.random() * 250;
        double thick = 5 + Math.random() * 25;
        WB_Point p0 = new WB_Point(0, 0, 0);
        WB_Point p1 = new WB_Point(w, 0, 0);
        WB_Point p2 = new WB_Point(w, 0, h);
        WB_Point p3 = new WB_Point(0, 0, h);
        WB_Polygon wallface = new WB_Polygon(p0, p1, p2, p3);
        ArrayList<WB_Polygon> faces = Cgeo.extrudeBase(wallface,new WB_Vector(0,1,0),thick);
        HEC_FromPolygons creator = new HEC_FromPolygons(faces);
        solidwall = creator.create();
        translateMesh();
//        rotateMesh();

    }

    void translateMesh(){
        Cgeo.translateMesh3D(solidwall,500);
    }

    void rotateMesh(){
        Random rand = new Random();
        solidwall.rotateAboutAxisSelf(Math.random() * Math.PI, 0, 0, 0, rand.nextInt(2), rand.nextInt(2), rand.nextInt(2));
    }

    public void setRand_pts_inside() {
        this.rand_pts_inside = Cgeo.randomPtsInMesh(pts_num,solidwall);
    }

    public void setRand_pts_on() {
        this.rand_pts_on = Cgeo.randomPtsOnTriangles(pts_num,solidwall);
//        this.rand_pts_on = Cgeo.randomPtsOnMesh(pts_num,solidwall);
    }

    @Override
    public void saveToCsv(String path) throws IOException {
        CFile.savePtsToCsv("solidwall",path,rand_pts_on);
    }

    @Override
    public void display(WB_Render3D render) {
        PGraphicsOpenGL app = render.getHome();
        app.pushStyle();
        app.stroke(255,100,0);
        render.drawEdges(solidwall);
        render.drawPoint(rand_pts_on, 2);
        app.popStyle();
    }
}
