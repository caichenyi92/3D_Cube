package cube.generator.elements;

import nct.CFile;
import nct.Cgeo;
import processing.opengl.PGraphicsOpenGL;
import wblut.geom.*;
import wblut.hemesh.HEC_FromPolygons;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.*;

public class WallWithWindow implements Element {
    int pts_num;
    ArrayList<WB_Point> rand_pts_inside;
    ArrayList<WB_Point> rand_pts_on;
    HE_Mesh wallwithwindow;
    WB_GeometryFactory2D gf;
    WB_Polygon base;
    float minZ;
    float minX;

    public WallWithWindow(int pts_num) {
        this.pts_num = pts_num;
        gf = new WB_GeometryFactory2D();
        setWallWithWindow();
//        setRand_pts_inside();
        setRand_pts_on();
    }

    public void setWallWithWindow() {
        setBase();
        double thick = 5 + Math.random() * 25;
        ArrayList<WB_Polygon> polys = Cgeo.extrudeBaseWithOneHole(base, new WB_Vector(0, 0, 1), thick);
        HEC_FromPolygons creator = new HEC_FromPolygons(polys);
        wallwithwindow = creator.create();
        wallwithwindow.rotateAboutAxisSelf(Math.PI / 2, new WB_Point(0, 0, 0), new WB_Vector(1, 0, 0));
//        translateMesh();
//        rotateMesh();
    }

    public void setBase() {
        ArrayList<WB_Point> innerpts = new ArrayList<>();
        double w = 40 + Math.random() * 20;
        double h = 40 + Math.random() * 20;
        double buf_dis = 5 + Math.random() * 5;
        innerpts.add(new WB_Point(0, 0, 0));
        innerpts.add(new WB_Point(w, 0, 0));
        innerpts.add(new WB_Point(w, h, 0));
        innerpts.add(new WB_Point(0, h, 0));

        WB_Polygon innerpoly = new WB_Polygon(innerpts);
        List<WB_Polygon> sspoly = gf.createBufferedPolygons2D(innerpoly, buf_dis);
        System.out.println(sspoly.size());
        List<WB_Polygon> shellpoly;
        WB_Polygon shellobb = new WB_Polygon();
        double buf_dis2 = 20 + Math.random() * 30;
        for (WB_Polygon shell : sspoly) {
            shellpoly = gf.createBufferedPolygons2D(shell, buf_dis2);
            System.out.println(shellpoly.size());
            for (WB_Polygon shell2 : shellpoly)
                shellobb = Cgeo.calObb(shell2);
        }
        Cgeo.translatePolygon(shellobb, buf_dis2);
        Collections.reverse(innerpts);
        base = new WB_Polygon(shellobb.getPoints().toList(), innerpts);

        List<WB_Coord> shellpts = shellobb.getPoints().toList();
        minZ = shellpts.get(0).zf();
        minX = shellpts.get(0).xf();
        for(WB_Coord pt:shellpts){
            if(pt.zf()<minZ)
                minZ = pt.zf();
            if(pt.xf()<minX)
                minX = pt.xf();
        }

    }

    void translateMesh() {
        Cgeo.translateMesh3D(wallwithwindow, 500);
    }

    void rotateMesh() {
        Random rand = new Random();
        wallwithwindow.rotateAboutAxisSelf(Math.random() * Math.PI, 0, 0, 0, 0, 0, 1);
    }

    public void setRand_pts_inside() {
        this.rand_pts_inside = Cgeo.randomPtsInMesh(pts_num, wallwithwindow);
    }

    public void setRand_pts_on() {
        this.rand_pts_on = Cgeo.randomPtsOnTriangles(pts_num, wallwithwindow);
//        this.rand_pts_on = Cgeo.randomPtsOnMesh(pts_num, wallwithwindow);
    }

    public ArrayList<WB_Point> getRan_pts_on(){
        return this.rand_pts_on;
    }

    public float getMinZ() {
        return minZ;
    }

    public float getMinX() {
        return minX;
    }

    @Override
    public void saveToCsv(String path) throws IOException {
        CFile.savePtsToCsv("wallww", path, rand_pts_on);
    }

    @Override
    public void display(WB_Render3D render) {
        PGraphicsOpenGL app = render.getHome();
        app.pushStyle();
        app.stroke(100, 0, 255);
        render.drawEdges(wallwithwindow);
        render.drawPoint(rand_pts_on, 1);
        app.popStyle();

    }
}
