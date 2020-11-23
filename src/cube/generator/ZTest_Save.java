package cube.generator;

import cube.generator.elements.*;
import gzf.gui.CameraController;
import processing.core.PApplet;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZTest_Save extends PApplet {
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
        size(1000, 1200, P3D);

        render = new WB_Render3D(this);
        cam = new CameraController(this,400);

        pts_num = 784;
        each_element_num = 200;
        train_path = "D:\\eclipse-workspace\\3D_Cube\\src\\datasets\\1116_translate_onmesh_train.csv";
        test_path = "D:\\eclipse-workspace\\3D_Cube\\src\\datasets\\1116_translate_onmesh_test.csv";

        getElements();
        System.out.println("saving points=========");
        for(Element e:elements){
            try {
                e.saveToCsv(train_path);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        System.out.println("drawing begin =========");

    }

    public void getElements() {
        elements = new ArrayList<>();
        int count = 0;
        while (count < each_element_num) {
//            cube = new Cube(pts_num);
//            capsule = new Capsule(pts_num);
//            cylinder = new Cylinder(pts_num);
//            solidwall = new SolidWall(pts_num);
//            slab = new Slab(pts_num);
            wallww = new WallWithWindow(pts_num);
//            elements.add(cube);
//            elements.add(capsule);
//            elements.add(cylinder);
//            elements.add(solidwall);
//            elements.add(slab);
            elements.add(wallww);
            count++;
            System.out.println(elements.size());
        }
    }

    public void draw() {
        background(0);

        for(Element e:elements){
            e.display(render);
        }

    }

    public static void main(String[] args) {
        PApplet.main("cube.generator.ZTest_Save");
    }
}
