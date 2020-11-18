package cube.generator.elements;

import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

import java.io.IOException;
import java.util.ArrayList;

public class Column implements Element{
    ArrayList<WB_Point> rand_pts_inside;
    ArrayList<WB_Point> rand_pts_on;
    int pts_num;
    HE_Mesh column;

    public Column(int pts_num) {
        this.pts_num = pts_num;
    }

    public void setColumn() {
        double w = 5+Math.random()*25;
        double h = 5+Math.random()*25;
        double height = 50+Math.random()*250;
        WB_Point p0 = new WB_Point(0,0,0);
        WB_Point p1 = new WB_Point(w,0,0);
        WB_Point p2 = new WB_Point(w,h,0);
        WB_Point p3 = new WB_Point(0,h,0);
        WB_Polygon base = new WB_Polygon(p0,p1,p2,p3);

    }

    public void setRand_pts_inside(ArrayList<WB_Point> rand_pts_inside) {
        this.rand_pts_inside = rand_pts_inside;
    }

    public void setRand_pts_on(ArrayList<WB_Point> rand_pts_on) {
        this.rand_pts_on = rand_pts_on;
    }

    @Override
    public void saveToCsv(String path) throws IOException {


    }

    @Override
    public void display(WB_Render3D render) {


    }
}
