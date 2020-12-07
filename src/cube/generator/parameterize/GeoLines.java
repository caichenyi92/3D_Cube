package cube.generator.parameterize;

import nct.CMath;
import nct.Cgeo;
import wblut.geom.*;

import java.util.ArrayList;


public class GeoLines {

    Filter filter;
    Filter[] mostptfiltersXY;
    Filter[] mostptfiltersYZ;
    ArrayList<ArrayList<WB_Point>> ptsinselectfils;

    ArrayList<WB_Point> pts_on_ele;

    ArrayList<WB_Point> ptsprojection;
    ArrayList<WB_Point> ptsprojectionX;

    public WB_Line mainlineXY;
    public WB_Line mainlineYZ;
    public ArrayList<WB_Line> sublinesX;
    public ArrayList<WB_Line> sublinesY;

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
        setFilter();
        setMostptFiltersXY();
        setMostptfiltersYZ();
        setSublinesX();
        setSublinesY();
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

    public ArrayList<WB_Line> getSublinesX() {
        return sublinesX;
    }


    public void setMainLineYZ() {
        WB_Transform3D T = new WB_Transform3D();
        T.addRotateAboutAxis(Math.PI/2,WB_Point.ZERO(),WB_Point.Y());
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
        int startx = (int) MinX(ptsprojection) - 5;
        mostptfiltersXY = mostPtFilters(4, ptsprojection, mainlineXY,startx);
    }

    public Filter[] getMostptfiltersXY() {
        return mostptfiltersXY;
    }

    public void setMostptfiltersYZ() {
//        WB_Transform3D T = new WB_Transform3D(WB_Point.ZERO(), WB_Point.X(), WB_Point.ZERO(), new WB_Point(0,0,1));
        WB_Transform3D T = new WB_Transform3D();
        T.addRotateAboutAxis(Math.PI/2,WB_Point.ZERO(),WB_Point.Y());
        ArrayList<WB_Point> ptsprojX_trans = ptsTrans(ptsprojectionX, T);
        WB_AABB aabb = new WB_AABB(ptsprojX_trans);
        WB_Polygon a = new WB_Polygon(aabb.getCorners());
        System.out.println(" AABB norm vec : " + a.getNormal());
        int startx = (int) MinX(ptsprojX_trans) - 5;
        WB_Line transYZ = mainlineYZ.apply(T);
        int filnum = 4;
        Filter[] fil_tmp ;
        fil_tmp = mostPtFilters(filnum, ptsprojX_trans, transYZ,startx); // fil_tmp
        T.inverse();

        mostptfiltersYZ = new Filter[filnum];
        for(int i = 0;i<filnum;i++){
            System.out.println("fil_tmp" + i + "scope : " + fil_tmp[i].scope);
            System.out.println("fil_tmp" + i + "dir: "+ fil_tmp[i].dir );
            mostptfiltersYZ[i] = new Filter(fil_tmp[i].origin.apply(T),fil_tmp[i].dir.apply(T),fil_tmp[i].scope,fil_tmp[i].step,new WB_Vector(1,0,0));
        }

    }


    public Filter[] getMostptfiltersYZ() {
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

    public void setFilter() {
//        filter = new Filter(new WB_Point(0, 0, projZ(pts_on_ele)), (WB_Vector) mainlineXY.getDirection(), 100, 20);
    }

    public Filter getFilter() {
        return filter;
    }

    // filter划过后点数最多的前 num 名 , 在 2D 平面上
    private Filter[] mostPtFilters(int num, ArrayList<WB_Point> pts, WB_Line mainl, int start) {
        filter_step = 0.5f;
        Filter[] areas = new Filter[num];
        int totalfilternum = 500;

        Filter[] allfilters = new Filter[totalfilternum];
        ArrayList<ArrayList<WB_Point>> ptsinfilters = new ArrayList<>();
        int[] ptsnumcontainfil = new int[totalfilternum];

        for (int i = 0; i < totalfilternum; i++) {
            Filter f = new Filter(new WB_Point(filter_step * i + start, 0, projZ(pts)), (WB_Vector) mainl.getDirection(), 100, filter_step,new WB_Vector(0,0,1));
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
            areas[i] = allfilters[indexofmaxes[i]];
            System.out.println("num of pts in select filters : "+ ptsinfilters.get(indexofmaxes[i]).size());
        }
        return areas;
    }

    public WB_Line leastSquareLine(ArrayList<WB_Point> pts) {
        WB_Line l;
        float a = LeastSquares.getA(pts);
        float b = LeastSquares.getB(pts);
        float x1 = 0;
        float y1 = x1 * a + b;
        float x2 = 1000;
        float y2 = x2 * a + b;
        l = new WB_Line(x1, y1, projZ(pts), x2, y2, 0);
        return l;

    }

    public float MinX(ArrayList<WB_Point> pts) {
        float minx = pts.get(0).xf();
        for (WB_Point pt : pts) {
            if (pt.xf() < minx)
                minx = pt.xf();
        }
        return minx;
    }

    public float MinY(ArrayList<WB_Point> pts) {
        float miny = pts.get(0).yf();
        for (WB_Point pt : pts) {
            if (pt.yf() < miny)
                miny = pt.yf();
        }
        return miny;
    }


    public float projZ(ArrayList<WB_Point> pts) {
        float z = pts.get(0).zf();
        for (WB_Point pt : pts) {
            if (pt.zf() < z)
                z = pt.zf();
        }
        return z;
    }

    public float projX(ArrayList<WB_Point> pts) {
        float x = pts.get(0).xf();
        for (WB_Point pt : pts) {
            if (pt.xf() < x)
                x = pt.xf();
        }
        return x;
    }

    public ArrayList<WB_Point> ptsProjection(ArrayList<WB_Point> pts) {
        ArrayList<WB_Point> ptsproj = new ArrayList<>();
        for (WB_Point pt : pts) {
            WB_Point pproj = new WB_Point(pt.xf(), pt.yf(), projZ(pts));
            ptsproj.add(pproj);
        }
        return ptsproj;
    }

    public ArrayList<WB_Point> getPtsprojectionX(ArrayList<WB_Point> pts) {
        ArrayList<WB_Point> ptsProjX = new ArrayList<>();
        for (WB_Point pt : pts) {
            WB_Point pproj = new WB_Point(projX(pts), pt.yf(), pt.zf());
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
