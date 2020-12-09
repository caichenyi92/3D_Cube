package cube.generator.parameterize;

import nct.CMath;
import nct.Cgeo;
import wblut.geom.*;

import java.util.ArrayList;


public class GeoLines {

    Filter filter;

    ArrayList<Filter> mostptfiltersXY;
    ArrayList<Filter> mostptfiltersYZ;
    ArrayList<ArrayList<WB_Point>> ptsinselectfils;

    ArrayList<WB_Point> pts_on_ele;

    //projection on XY plane
    ArrayList<WB_Point> ptsprojection;
    //projection on YZ plane
    ArrayList<WB_Point> ptsprojectionX;

    // 目前不支持绕z轴旋转的点云
    WB_AABB baseAABB;

    public WB_Line mainlineXY;
    public WB_Line mainlineYZ;
    public ArrayList<WB_Line> sublinesX;
    public ArrayList<WB_Line> sublinesY;
    public double thick;

    float filter_step;

    public GeoLines(ArrayList<WB_Point> pts_on_ele) {
        this.pts_on_ele = pts_on_ele;
        ptsinselectfils = new ArrayList<>();
        sublinesX = new ArrayList<>();
        sublinesY = new ArrayList<>();
        ptsprojection = ptsProjection(pts_on_ele);
        ptsprojectionX = getPtsprojectionX(pts_on_ele);

        setMainLineXY();
        setMainLineYZ();
        setMostptFiltersXY();
        setMostptfiltersYZ();
        setSublinesX();
        setSublinesY();
        setBaseAABB();
        setThick();
    }

    public void setMainLineXY() {
        mainlineXY = leastSquareLine(ptsprojection);
    }

    public WB_Line getMainlineXY() {
        return mainlineXY;
    }

//    public void setSublinesX() {
//        for(ArrayList<WB_Point> ptarray:ptsinselectfils){
//            float a = LeastSquares.getA(ptarray);
//            float b = LeastSquares.getB(ptarray);
//            float x1 = 0;
//            float y1 = x1 * a + b;
//            float x2 = 1000;
//            float y2 = x2 * a + b;
//            WB_Line l  = new WB_Line(x1, y1, projZ(pts_on_ele), x2, y2, 0);
//            sublinesXY.add(l);
//            System.out.println("subline XY :" + sublinesXY.size());
//        }
//    }

    public void setSublinesX() {
        for (Filter f : mostptfiltersXY) {
            WB_Line l = f.line;
            sublinesX.add(l);
        }
    }

    // 外头用arrayList 接住的时候  注意引用的话 都会改变 注意外头引用时 大小都会随着改变！！！
    public ArrayList<WB_Line> getSublinesX() {
        return sublinesX;
    }

    public void setMainLineYZ() {
        WB_Transform3D T = new WB_Transform3D();
        T.addRotateAboutAxis(Math.PI / 2, WB_Point.ZERO(), WB_Point.Y());
        ArrayList<WB_Point> ptsprojX_trans = ptsTrans(ptsprojectionX, T);
        mainlineYZ = leastSquareLine(ptsprojX_trans);
        System.out.println("main Line YZ : " + mainlineYZ.getDirection());
        T.inverse();
        mainlineYZ.applySelf(T);

    }

    public WB_Line getMainlineYZ() {
        return mainlineYZ;
    }


    public void setMostptFiltersXY() {
        int startx = (int) CMath.MinX(ptsprojection) - 5;
        mostptfiltersXY = mostPtFilters(4, ptsprojection, mainlineXY, startx);
    }

    public ArrayList<Filter> getMostptfiltersXY() {
        return mostptfiltersXY;
    }

    public void setMostptfiltersYZ() {
//        WB_Transform3D T = new WB_Transform3D(WB_Point.ZERO(), WB_Point.X(), WB_Point.ZERO(), new WB_Point(0,0,1));
        WB_Transform3D T = new WB_Transform3D();
        T.addRotateAboutAxis(Math.PI / 2, WB_Point.ZERO(), WB_Point.Y());
        ArrayList<WB_Point> ptsprojX_trans = ptsTrans(ptsprojectionX, T);
        WB_AABB aabb = new WB_AABB(ptsprojX_trans);
        WB_Polygon a = new WB_Polygon(aabb.getCorners());
        System.out.println(" AABB norm vec : " + a.getNormal());
        int startx = (int) CMath.MinX(ptsprojX_trans) - 5;
        WB_Line transYZ = mainlineYZ.apply(T);
        int filnum = 4;
        ArrayList<Filter> fil_tmp;
        fil_tmp = mostPtFilters(filnum, ptsprojX_trans, transYZ, startx); // fil_tmp
        T.inverse();

        mostptfiltersYZ = new ArrayList<>();
        for (int i = 0; i < filnum; i++) {
            System.out.println("fil_tmp" + i + "scope : " + fil_tmp.get(i).scope);
            System.out.println("fil_tmp" + i + "dir: " + fil_tmp.get(i).dir);
            mostptfiltersYZ.add(new Filter(fil_tmp.get(i).origin.apply(T), fil_tmp.get(i).dir.apply(T), fil_tmp.get(i).scope, fil_tmp.get(i).step, new WB_Vector(1, 0, 0)));
        }

    }

    public ArrayList<Filter> getMostptfiltersYZ() {
        return mostptfiltersYZ;
    }

    public ArrayList<ArrayList<WB_Point>> getPtsinselectfils() {
        return ptsinselectfils;
    }

    public void setSublinesY() {
        for (Filter f : mostptfiltersYZ) {
            WB_Line l = f.line;
            sublinesY.add(l);
        }
    }

    public ArrayList<WB_Line> getSublinesY() {
        return sublinesY;
    }

    public ArrayList<WB_Point> getPtsprojection() {
        return ptsprojection;
    }

    public ArrayList<WB_Point> getPtsprojectionX() {
        return ptsprojectionX;
    }

    // filter划过后点数最多的前 num 名 , 在 2D 平面上
    private ArrayList<Filter> mostPtFilters(int num, ArrayList<WB_Point> pts, WB_Line mainl, int start) {
        filter_step = 0.5f;
        ArrayList<Filter> areas = new ArrayList<>();
        int totalfilternum = 500;

        Filter[] allfilters = new Filter[totalfilternum];
        ArrayList<ArrayList<WB_Point>> ptsinfilters = new ArrayList<>();
        int[] ptsnumcontainfil = new int[totalfilternum];

        for (int i = 0; i < totalfilternum; i++) {
            Filter f = new Filter(new WB_Point(filter_step * i + start, 0, Cgeo.projZ(pts)), (WB_Vector) mainl.getDirection(), 100, filter_step, new WB_Vector(0, 0, 1));
            allfilters[i] = f;
            ArrayList<WB_Point> ptsin = Cgeo.ptsInPolygon(f.filterbase, pts);
            ptsinfilters.add(ptsin);
            ptsnumcontainfil[i] = ptsin.size();
        }

        int[] maxinptsnum = CMath.getMaxFromArray(ptsnumcontainfil, num);
        int[] indexofmaxes = new int[num];
        for (int i = 0; i < num; i++) {
            indexofmaxes[i] = CMath.printArray(ptsnumcontainfil, maxinptsnum[i]);
        }
        for (int i = 0; i < num; i++) {
            areas.add(allfilters[indexofmaxes[i]]);
            System.out.println("num of pts in select filters : " + ptsinfilters.get(indexofmaxes[i]).size());
        }
        return areas;
    }


    public void setBaseAABB() {
        baseAABB = new WB_AABB(ptsprojection);

    }

    public WB_AABB getBaseAABB() {
        return baseAABB;
    }

    public void setThick() {
        this.thick = baseAABB.getMaxY()-baseAABB.getMinY();
        System.out.println(" thickness : " + thick);
    }

    public double getThick() {
        return thick;
    }

    public WB_Line leastSquareLine(ArrayList<WB_Point> pts) {
        WB_Line l;
        float a = LeastSquares.getA(pts);
        float b = LeastSquares.getB(pts);
        float x1 = 0;
        float y1 = x1 * a + b;
        float x2 = 1000;
        float y2 = x2 * a + b;
        l = new WB_Line(x1, y1, Cgeo.projZ(pts), x2, y2, 0);
        return l;
    }

    public ArrayList<WB_Point> ptsProjection(ArrayList<WB_Point> pts) {
        ArrayList<WB_Point> ptsproj = new ArrayList<>();
        for (WB_Point pt : pts) {
            WB_Point pproj = new WB_Point(pt.xf(), pt.yf(), Cgeo.projZ(pts));
            ptsproj.add(pproj);
        }
        return ptsproj;
    }

    public ArrayList<WB_Point> getPtsprojectionX(ArrayList<WB_Point> pts) {
        ArrayList<WB_Point> ptsProjX = new ArrayList<>();
        for (WB_Point pt : pts) {
            WB_Point pproj = new WB_Point(Cgeo.projX(pts), pt.yf(), pt.zf());
            ptsProjX.add(pproj);
        }
        return ptsProjX;
    }

    public static ArrayList<WB_Point> ptsTrans(ArrayList<WB_Point> pts, WB_Transform3D T) {
        ArrayList<WB_Point> pts_trans = new ArrayList<>();
        for (WB_Point pt : pts) {
            WB_Point tmp = pt.apply(T);
            pts_trans.add(tmp);
        }
        return pts_trans;
    }

}
