package cube.generator.parameterize;

import org.locationtech.jts.geom.GeometryFactory;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_Segment;

// 重构带窗的墙的方法； origin 表示底边中线原点； x 系列表示x轴上对应的长度；y表示y轴上对应的长度。
public class GeoReconstructor {
    GeoLines geols;
    WB_Point origin;
    double thick;
    double x1;
    double x2;
    double x3;
    double y1;
    double y2;
    double y3;

    public GeoReconstructor(GeoLines geo_lines) {
        this.geols = geo_lines;
        setOrigin();
    }

    public void setOrigin() {
        WB_Segment mainl = new WB_Segment(geols.getMainlineXY().getOrigin(),geols.getMainlineXY().getDirection(),1000);
        WB_Segment subl = new WB_Segment(geols.getSublinesX().get(0).getOrigin(),geols.getSublinesX().get(0).getDirection(),1000);
        System.out.println("main l x :" + mainl.getOrigin().xf());
        System.out.println("sub l x : " + subl.getOrigin().xf());
        System.out.println("x轴上相交 ： " + WB_GeometryOp.getIntersection3D(mainl,subl).isIntersection());
        this.origin = (WB_Point) WB_GeometryOp.getIntersection3D(mainl,subl).object;
    }

    public WB_Coord getOrigin() {
        return origin;
    }
}
