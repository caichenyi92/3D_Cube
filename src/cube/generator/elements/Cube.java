package cube.generator.elements;

import nct.CFile;
import nct.Cgeo;
import processing.opengl.PGraphicsOpenGL;
import wblut.geom.WB_Point;
import wblut.hemesh.HEC_Cube;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Cube implements Element {
    ArrayList<WB_Point> rand_pts_inside;
    ArrayList<WB_Point> rand_pts_on;
    HEC_Cube creator;
    HE_Mesh cube;
    int pts_num;

    public Cube(int pts_num) {
        this.pts_num = pts_num;
        setCube();
//        setRand_pts_inside();
        setRand_pts_on();
    }

    public void setCube() {
        creator = new HEC_Cube();
        creator.setEdge(50 + Math.random() * 250);
        cube = new HE_Mesh(creator);
//        rotateMesh();
        translateMesh();
    }

    public void rotateMesh(){
        Random rand = new Random();
        cube.rotateAboutAxisSelf(Math.random() * Math.PI, 0, 0, 0, rand.nextInt(2), rand.nextInt(2), rand.nextInt(2));
    }

    public void translateMesh() {
        Cgeo.translateMesh3D(cube,500);
    }

    public void setRand_pts_inside() {
        rand_pts_inside = Cgeo.randomPtsInMesh(pts_num, cube);
    }

    public void setRand_pts_on() {
        rand_pts_on = Cgeo.randomPtsOnMesh(pts_num,cube);
    }

    public HE_Mesh getCube() {
        return cube;
    }

    @Override
    public void saveToCsv(String path) throws IOException {
        CFile.savePtsToCsv("cube", path, rand_pts_on);
    }

    @Override
    public void display(WB_Render3D render) {
        PGraphicsOpenGL app = render.getHome();
        app.pushStyle();
        app.stroke(255,0,0);
        app.strokeWeight(1);
        render.drawEdges(cube);
        render.drawPoint(rand_pts_on);
        app.popStyle();
    }


}