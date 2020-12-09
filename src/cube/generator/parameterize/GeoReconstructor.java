package cube.generator.parameterize;

import nct.CMath;
import nct.Cgeo;
import wblut.geom.*;

import java.util.ArrayList;
import java.util.List;

// 重构带窗的墙的方法； origin 表示底边中线原点； x 系列表示x轴上对应的长度；y表示y轴上对应的长度。
public class GeoReconstructor {
    GeoLines geols;
    WB_Point origin;
    List<WB_Point> ptsX;
    List<WB_Point> ptsZ;
    double thick;
    double x1;
    double x2;
    double x3;
    double z1;
    double z2;
    double z3;

    WB_Polygon baseXZ;

    public GeoReconstructor(GeoLines geo_lines) {
        this.geols = geo_lines;
        setPtsX();
        setPtsZ();
        setOrigin();
        setXDist();
        setZDist();
        setThick();
    }

    public void setOrigin() {
        WB_Point tmp_p = ptsX.get(0);
        for (WB_Point p : ptsX) {
            if (p.xf() < tmp_p.xf()) {
                tmp_p = p;
            }
        }
        origin = tmp_p;
    }

    public WB_Coord getOrigin() {
        return origin;
    }

    public void setXDist() {
        x1 = ptsX.get(0).getDistance3D(ptsX.get(1));
        x2 = ptsX.get(1).getDistance3D(ptsX.get(2));
        x3 = ptsX.get(2).getDistance3D(ptsX.get(3));
        System.out.println(" x1 , x2 , x3, : " + x1 + " , " + x2 + " , " + x3  );
    }

    public void setZDist() {
        z1 = ptsZ.get(0).getDistance3D(ptsZ.get(1));
        z2 = ptsZ.get(1).getDistance3D(ptsZ.get(2));
        z3 = ptsZ.get(2).getDistance3D(ptsZ.get(3));
        System.out.println(" z1 , z2 , z3, : " + z1 + " , " + z2 + " , " + z3  );
    }

    public void setPtsX() {
        // 将主线的 origin 在X轴上向左平移一定的距离，避免相交不到算补点
        WB_Transform3D T1 = new WB_Transform3D();
        WB_Vector v = Cgeo.oppositeVector((WB_Vector) geols.getMainlineXY().getDirection());
        T1.addTranslate(500, v);
        WB_Segment mainl = new WB_Segment(geols.getMainlineXY().getOrigin(), geols.getMainlineXY().getDirection(), 1000);
        WB_Segment mainl_new = mainl.apply(T1);
        ArrayList<WB_Segment> subls = new ArrayList<>();

        for (int i = 0; i < geols.getSublinesX().size(); i++) {
            WB_Transform3D T2 = new WB_Transform3D();
            WB_Vector v2 = Cgeo.oppositeVector((WB_Vector) geols.getSublinesX().get(i).getDirection());
            T2.addTranslate(500, v2);
            WB_Segment subl = new WB_Segment(geols.getSublinesX().get(i).getOrigin(), geols.getSublinesX().get(i).getDirection(), 1000);
            WB_Segment subl_new = subl.apply(T2);
            subls.add(subl_new);
        }
        ptsX = new ArrayList<>();
        for (WB_Segment l : subls) {
            WB_Point p = (WB_Point) WB_GeometryOp.getIntersection3D(l, mainl_new).object;
            ptsX.add(p);
        }

        ptsX = CMath.setOrderAccrodingToX(ptsX);
        System.out.println("pts X size : " + ptsX.size());
    }


    public void setPtsZ() {
        WB_Transform3D T1 = new WB_Transform3D();
        WB_Vector v = Cgeo.oppositeVector((WB_Vector) geols.getMainlineYZ().getDirection());
        T1.addTranslate(500, v);
        WB_Segment mainl = new WB_Segment(geols.getMainlineYZ().getOrigin(), geols.getMainlineYZ().getDirection(), 1000);
        WB_Segment mainl_new = mainl.apply(T1);

        ArrayList<WB_Segment> subls = new ArrayList<>();
        for (int i = 0; i < geols.getSublinesY().size(); i++) {
            WB_Transform3D T2 = new WB_Transform3D();
            WB_Vector v2 = Cgeo.oppositeVector((WB_Vector) geols.getSublinesY().get(i).getDirection());
            T2.addTranslate(500, v2);
            WB_Segment subl = new WB_Segment(geols.getSublinesY().get(i).getOrigin(), geols.getSublinesY().get(i).getDirection(), 1000);
            WB_Segment subl_new = subl.apply(T2);
            subls.add(subl_new);
        }
        ptsZ = new ArrayList<>();
        for (WB_Segment l : subls) {
            WB_Point p = (WB_Point) WB_GeometryOp.getIntersection3D(l, mainl_new).object;
            ptsZ.add(p);
        }
        ptsZ = CMath.setOrderAccrodingToZ(ptsZ);
        System.out.println("pts Z size : " + ptsZ.size());
    }

    public GeoLines getGeols() {
        return geols;
    }

    public void setThick() {
        this.thick = geols.getThick();
    }

    public double getThick() {
        return thick;
    }

    public List<WB_Point> getPtsX() {
        return ptsX;
    }

    public List<WB_Point> getPtsZ() {
        return ptsZ;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getX3() {
        return x3;
    }

    public double getZ1() {
        return z1;
    }

    public double getZ2() {
        return z2;
    }

    public double getZ3() {
        return z3;
    }
}
