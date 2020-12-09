package cube.generator.parameterize;

import nct.Cgeo;
import wblut.geom.*;
import wblut.hemesh.HEC_FromPolygons;
import wblut.hemesh.HE_Mesh;

import java.util.ArrayList;

public class GeoAdjust {

    WB_Coord origin;
    double thick;
    double x1;
    double x2;
    double x3;
    double z1;
    double z2;
    double z3;

    WB_Polygon baseXZ;
    HE_Mesh shape;

    public GeoAdjust(WB_Coord origin, double thick, double x1, double x2, double x3, double z1, double z2, double z3) {
        this.origin = origin;
        this.thick = thick;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.z1 = z1;
        this.z2 = z2;
        this.z3 = z3;

        setBaseXZ();
        setShape();
    }

    public void setBaseXZ() {
        // shell poly  4 points;
        WB_Point p1 = new WB_Point(origin.xf(),origin.yf(),origin.zf()+z1+z2+z3);
        WB_Point p2 = new WB_Point(origin.xf()+x1+x2+x3,origin.yf(),origin.zf()+z1+z2+z3);
        WB_Point p3 = new WB_Point(origin.xf()+x1+x2+x3,origin.yf(),origin.zf());
        WB_Polygon shellpoly = new WB_Polygon(origin,p1,p2,p3);
        //inner poly
        WB_Point ip0 = new WB_Point(origin.xf()+x1,origin.yf(),origin.zf()+z1);
        WB_Point ip1 = new WB_Point(ip0.xf()+x2,ip0.yf(),ip0.zf());
        WB_Point ip2 = new WB_Point(ip1.xf(),ip1.yf(),ip1.zf()+z2);
        WB_Point ip3 = new WB_Point(ip0.xf(),ip0.yf(),ip0.zf()+z2);
        WB_Polygon innerpoly = new WB_Polygon(ip0,ip1,ip2,ip3);
        baseXZ = new WB_Polygon(shellpoly.getPoints().toList(),innerpoly.getPoints().toList());
        System.out.println(baseXZ);

    }

    public WB_Polygon getBaseXZ() {
        return baseXZ;
    }

    public void setShape() {
        ArrayList<WB_Polygon> polys = Cgeo.extrudeBaseCenterLineWithOneHole(baseXZ,baseXZ.getNormal(),thick/2);
        HEC_FromPolygons creator = new HEC_FromPolygons(polys);
        shape = creator.create();
    }

    public HE_Mesh getShape() {
        return shape;
    }
}
