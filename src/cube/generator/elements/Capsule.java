package cube.generator.elements;

import nct.CFile;
import nct.Cgeo;
import processing.opengl.PGraphicsOpenGL;
import wblut.geom.WB_Point;
import wblut.hemesh.HEC_Capsule;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Capsule implements Element {
    int pts_num;
    ArrayList<WB_Point> rand_pts_inside;
    HE_Mesh capsule;

    public Capsule(int pts_num) {
        this.pts_num = pts_num;
        setRandPtsInside();
    }

    public void setCapsule() {
        HEC_Capsule creator = new HEC_Capsule();
        creator.setRadius(50 + Math.random() * 250);
        creator.setHeight(50 + Math.random() * 250);
        int randstep = (int) (5 + Math.random() * 15);
        creator.setFacets(randstep).setSteps(randstep);
        creator.setCap(true, true);
        creator.setCapSteps(1);
        capsule = new HE_Mesh(creator);
//        HET_Diagnosis.validate(capsule);
//        rotateMesh();
        translateMesh();
    }

    public void rotateMesh() {
        Random rand = new Random();
        capsule.rotateAboutAxisSelf(Math.random() * Math.PI, 0, 0, 0, rand.nextInt(2), rand.nextInt(2), rand.nextInt(2));
    }

    public void translateMesh() {
       Cgeo.translateMesh3D(capsule,500);
    }


    public void setRandPtsInside() {
        setCapsule();
        rand_pts_inside = Cgeo.randomPtsInMesh(pts_num, capsule);
    }

    public HE_Mesh getCapsule() {
        return capsule;
    }

    @Override
    public void saveToCsv(String path) throws IOException {
        CFile.savePtsToCsv("capsule", path, rand_pts_inside);
    }

    @Override
    public void display(WB_Render3D render) {
        PGraphicsOpenGL app = render.getHome();
        app.pushStyle();
        app.strokeWeight(1);
        app.stroke(0, 255, 0);
        render.drawEdges(capsule);
        for (WB_Point pt : rand_pts_inside) {
            render.drawPoint(pt, 2);
        }
        app.popStyle();
    }
}
