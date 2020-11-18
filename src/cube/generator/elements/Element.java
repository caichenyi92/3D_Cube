package cube.generator.elements;

import wblut.geom.WB_Point;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;

public interface Element {

    public abstract void saveToCsv(String path) throws IOException;
    public abstract void display(WB_Render3D render);


}
