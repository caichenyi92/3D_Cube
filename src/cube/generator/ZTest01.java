package cube.generator;

import cube.generator.elements.*;
import cube.generator.parameterize.Filter;
import gzf.gui.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Segment;
import wblut.processing.WB_Render3D;

import java.util.ArrayList;
import java.util.List;

public class ZTest01 extends PApplet {
    Element cube;
    Element capsule;
    Element cylinder;
    Element solidwall;
    Element slab;
    Element www;
    CameraController cam;
    WB_Render3D render;
    List<Element> elements;
    int pts_num;
    int each_element_num;

    public void setup() {
        size(500, 1000, P3D);
        pts_num = 784;
        each_element_num = 1;
        cam = new CameraController(this, 300);
//        cam.top();
        render = new WB_Render3D(this);
        getElements();

    }

    public void getElements() {
        elements = new ArrayList<>();
        cube = new Cube(pts_num);
        elements.add(cube);
        solidwall = new SolidWall(pts_num);
        elements.add(solidwall);
        slab = new Slab(pts_num);
        elements.add(slab);
        www = new WallWithWindow(pts_num);
        elements.add(www);
//        capsule = new Capsule(pts_num);
//        elements.add(capsule);
//        cylinder = new Cylinder(pts_num);
//        elements.add(cylinder);
    }

    public void draw() {
        background(0);


        for (Element e : elements) {
            e.display(render);
        }
    }

    public static void main(String[] args) {
        PApplet.main("cube.generator.ZTest01");
    }
}
