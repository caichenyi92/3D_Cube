package cube.generator.parameterize;

import wblut.geom.*;

public class Filter {
    WB_Point origin;
    WB_Vector dir;
    double scope;
    double step;
    WB_Vector dir_ro;
    WB_Polygon filterbase;
    public WB_Line line;

    //origin:起始边的中点;  dir:扫掠方向; scope:矩形范围的长度;step 范围的步长
    public Filter(WB_Point origin, WB_Vector direction, double scope, double step,WB_Vector dir_ro_axis) {
        this.origin = origin;
        this.dir = direction;
        this.scope = scope;
        this.step = step;
        this.dir_ro = dir_ro_axis;
        setFilterbase();
        setLine();
    }


     void setFilterbase() {
        dir.normalizeSelf();
         WB_Vector dir_verti = dir.rotateAboutAxis(Math.PI/2,origin,dir_ro);
//         = dir.rotateAboutPoint2D(Math.PI / 2, origin);
        WB_Point p1 = new WB_Point(origin.xf() + dir_verti.xf() * scope, origin.yf() + dir_verti.yf() * scope, origin.zf() + dir_verti.zf() * scope);
        WB_Point p2 = new WB_Point(p1.xf() + dir.xf() * step, p1.yf() + dir.yf() * step, p1.zf() + dir.zf() * step);
        WB_Point p3 = new WB_Point(p2.xf() - dir_verti.xf() * scope * 2, p2.yf() - dir_verti.yf() * scope * 2, p2.zf() - dir_verti.zf() * scope * 2);
        WB_Point p4 = new WB_Point(origin.xf() - dir_verti.xf() * scope, origin.yf() - dir_verti.yf() * scope, origin.zf() - dir_verti.zf() * scope);
        filterbase = new WB_Polygon(p1,p2,p3,p4);
    }

    public void setLine() {
        WB_Vector dir_verti = dir.rotateAboutAxis(Math.PI/2,origin,dir_ro);
        line = new WB_Line(origin,dir_verti);
    }


    public WB_Polygon getFilterbase() {
        return filterbase;
    }

    public WB_Point getOrigin() {
        return origin;
    }

    public WB_Vector getDir() {
        return dir;
    }

    public double getScope() {
        return scope;
    }

    public double getStep() {
        return step;
    }

    public WB_Vector getDir_ro() {
        return dir_ro;
    }

}
