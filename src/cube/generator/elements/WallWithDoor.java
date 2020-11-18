package cube.generator.elements;

import wblut.geom.WB_Point;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;

public class WallWithDoor implements Element{

    int pts_num;
    ArrayList<WB_Point> rand_pts_on;
    ArrayList<WB_Point> rand_pts_inside;

    public WallWithDoor(int pts_num) {
        this.pts_num = pts_num;
    }

    @Override
    public void saveToCsv(String path) throws IOException {

    }

    @Override
    public void display(WB_Render3D render) {

    }
}
