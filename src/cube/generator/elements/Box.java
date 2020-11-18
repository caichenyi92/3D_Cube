package cube.generator.elements;

import gzf.gui.CameraController;
import nct.Cgeo;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.hemesh.HEC_Capsule;
import wblut.hemesh.HEC_Cube;
import wblut.hemesh.HEC_Ring;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Box extends PApplet {
    HE_Mesh mesh_ring;
    WB_Render3D render;
    CameraController cam;
    ArrayList<WB_Point> pts_in_ring;
    ArrayList<WB_Point> pts_in_cube;
    ArrayList<WB_Point> pts_in_capsule;
    HE_Mesh mesh_cube;
    HE_Mesh mesh_capsule;

    public void setup() {
        size(800, 600, P3D);
        cam = new CameraController(this, 300);
        cam.openLight();
        cam.top();
        pts_in_ring = this.randomPtsInRing();
        render = new WB_Render3D(this);
    }

    // label_0
    private ArrayList<WB_Point> randomPtsInCube() {
        Random rand = new Random();
        HEC_Cube creator = new HEC_Cube();
        creator.setEdge(500 + rand.nextDouble() * 2500);
        mesh_cube = new HE_Mesh(creator);
        mesh_cube = mesh_cube.rotateAboutAxis(Math.random() * PI, 0, 0, 0, rand.nextInt(2), rand.nextInt(2), rand.nextInt(2));
        pts_in_cube = Cgeo.randomPtsInMesh(576, mesh_cube);
        return pts_in_cube;
    }

    //label_1
    private ArrayList<WB_Point> randomPtsInCapsule() {
        Random rand = new Random();
        HEC_Capsule creator = new HEC_Capsule();
        creator.setRadius(250 + rand.nextDouble() * 1250);
        creator.setHeight(250 + rand.nextDouble() * 1250);
        creator.setFacets(7).setSteps(5);
        creator.setCap(true, true);
        creator.setCapSteps(1);
        mesh_capsule = new HE_Mesh(creator);
        mesh_capsule = mesh_capsule.rotateAboutAxis(Math.random() * PI, 0, 0, 0, rand.nextInt(2), rand.nextInt(2), rand.nextInt(2));
        pts_in_capsule = Cgeo.randomPtsInMesh(576, mesh_capsule);
        return pts_in_capsule;
    }

    //label_2
    private ArrayList<WB_Point> randomPtsInRing() {
        HEC_Ring creator = new HEC_Ring().setRadius(300, 600);
        mesh_ring = new HE_Mesh(creator);
        mesh_ring = mesh_ring.rotateAboutAxis(PI / 6, 0, 0, 0, 0, 0, 1);
        return Cgeo.randomPtsInMesh(1000, mesh_ring);
    }

    public void vecToCSV(String label, String path, ArrayList<WB_Point> pts) throws IOException {
        File newfile = new File(path);
        FileWriter fw = new FileWriter(newfile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(label);
        for (WB_Point pt : pts) {
            bw.write(",");
            bw.write(String.valueOf(pt.xd()));
            bw.write(",");
            bw.write(String.valueOf(pt.yd()));
            bw.write(",");
            bw.write(String.valueOf(pt.zd()));
        }
        bw.newLine();
        bw.flush();
        bw.close();
    }

    AtomicInteger count = new AtomicInteger();

    public void draw() {
        background(255);

//        noStroke();
//        fill(100,120);
//        render.drawFaces(mesh);

        while (count.get() < 10) {
            System.out.println(count.get());
//            pts_in_capsule = this.randomPtsInCapsule();
            pts_in_cube = this.randomPtsInCube();
//            try {
//                vecToCSV("1", "D:\\eclipse-workspace\\3D_Cube\\src\\1026_capsule_test.csv", pts_in_cube);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            count.getAndIncrement();
        }

        if (frameCount % 1 == 0) {
            stroke(0);
            render.drawEdges(mesh_cube);
            for (WB_Point pt : pts_in_cube) {
                render.drawPoint(pt, 10);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("cube.generator.elements.Box");
    }

}
