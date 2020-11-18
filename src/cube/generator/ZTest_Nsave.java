package cube.generator;

import cube.generator.elements.*;
import gzf.gui.CameraController;
import processing.core.PApplet;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZTest_Nsave extends PApplet {
    Element cube;
    Element capsule;
    Element cylinder;
    Element solidwall;
    Element slab;
    Element wallww;
    List<Element> elements;

    String train_path;
    String test_path;
    int pts_num;
    int each_element_num;

    WB_Render3D render;
    CameraController cam;

    public void setup() {
        size(1500, 1000, P3D);

        render = new WB_Render3D(this);
        cam = new CameraController(this, 400);

        pts_num = 784;
        each_element_num = 4;
        getElements();
        System.out.println("drawing=========");
    }

    public void getElements() {
        elements = new ArrayList<>();
        int count = 0;
        while (count < each_element_num) {
//            cube = new Cube(pts_num);
            solidwall = new SolidWall(pts_num);
            slab = new Slab(pts_num);
            wallww = new WallWithWindow(pts_num);
//            elements.add(cube);
            elements.add(solidwall);
            elements.add(slab);
            elements.add(wallww);
            // capsule = new Capsule(pts_num);
//            cylinder = new Cylinder(pts_num);
//            elements.add(capsule);
//            elements.add(cylinder);
            count++;
            System.out.println(elements.size());
        }
    }

    public void draw() {
        background(0);
        for (Element e : elements) {
            e.display(render);
        }

    }

    public static void main(String[] args) {
        PApplet.main("cube.generator.ZTest_Nsave");
    }
}
