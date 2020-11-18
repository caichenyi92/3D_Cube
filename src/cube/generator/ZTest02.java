package cube.generator;

import cube.generator.elements.Capsule;
import cube.generator.elements.Cube;
import cube.generator.elements.Cylinder;
import cube.generator.elements.Element;
import gzf.gui.CameraController;
import processing.core.PApplet;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZTest02 extends PApplet {
    Element cube;
    Element capsule;
    Element cylinder;
    CameraController cam;
    WB_Render3D render;
    List<Element> elements;
    int pts_num;
    int each_element_num;

    String train_path = "D:\\eclipse-workspace\\3D_Cube\\src\\1106_rotate.csv";


    public void setup() {
        size(1200, 1000, P3D);
        pts_num = 784;
        each_element_num = 2;
        cam = new CameraController(this, 300);
//        cam.top();
        render = new WB_Render3D(this);
        getElements();
    }

    int count = 0;
    public void getElements() {
        elements = new ArrayList<>();
        cube = new Cube(pts_num);
        elements.add(cube);
        capsule = new Capsule(pts_num);
        elements.add(capsule);
        cylinder = new Cylinder(pts_num);
        elements.add(cylinder);
        count++;

    }

    public void ElementSave() throws IOException {
        for (Element element : elements) {
            element.display(render);
            if (count < each_element_num) {
                element.saveToCsv(train_path);
                count++;
            }
        }
    }


    public void draw() {
        background(255);
        for (Element e : elements) {
            e.display(render);
        }
    }

    public static void main(String[] args) {
        PApplet.main("cube.generator.ZTest02");
    }
}
