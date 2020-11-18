package cube.generator.elements;

import nct.CFile;
import nct.Cgeo;
import processing.opengl.PGraphicsOpenGL;
import wblut.geom.WB_Point;
import wblut.hemesh.HEC_Cylinder;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Cylinder implements Element {

    int pts_num;
    ArrayList<WB_Point> rand_pts_inside;
    HE_Mesh cylinder;

    public Cylinder(int pts_num) {
        this.pts_num = pts_num;
        setRand_pts_inside();
    }

    public void setCylinder() {
        HEC_Cylinder creator = new HEC_Cylinder();
        double r = 50 + Math.random() * 250;
        double R = 80 + Math.random() * 250;
        double h = 150 + Math.random() * 250;
        creator.setRadius(R, r);
        creator.setHeight(h);
        creator.setFacets(32).setSteps(5);
        creator.setCap(true, true);
        cylinder = new HE_Mesh(creator);
//        HET_Diagnosis.validate(cylinder);
//        rotateMesh();
        translateMesh();
    }


    void rotateMesh() {
        Random rand = new Random();
        cylinder.rotateAboutAxisSelf(Math.random() * Math.PI, 0, 0, 0, rand.nextInt(2), rand.nextInt(2), rand.nextInt(2));
    }

    void translateMesh() {
        Cgeo.translateMesh3D(cylinder, 500);
    }

    public void setRand_pts_inside() {
        setCylinder();
        rand_pts_inside = Cgeo.randomPtsInMesh(pts_num, cylinder);
    }

    @Override
    public void saveToCsv(String path) throws IOException {
        CFile.savePtsToCsv("cylinder", path, rand_pts_inside);
    }

    @Override
    public void display(WB_Render3D render) {
        PGraphicsOpenGL app = render.getHome();
        app.pushStyle();
        app.stroke(0, 0, 255);
        app.strokeWeight(1);
        render.drawEdges(cylinder);
        render.drawPoint(rand_pts_inside, 2);
        app.popStyle();
    }
}
