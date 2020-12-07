package cube.generator.parameterize;

import wblut.geom.WB_Point;

import java.util.ArrayList;

public class LeastSquares {

    /**
     * y = a x + b ; b = sum( y ) / n - a * sum( x ) / n ; a = ( n * sum( x*y ) - sum( x
     * ) * sum( y ) ) / ( n * sum( x^2 ) - sum(x) ^ 2 )
     */

    /*
     * 功能：返回估计的y值
     */
    public static float estimate(ArrayList<WB_Point> pts, float inputX) {
        float a = getA(pts);
        float b = getB(pts);
        System.out.println("线性回归系数a值：\t" + a + "\n" + "线性回归系数b值：\t" + b);
        return (a * inputX + b);
    }

    /*
     * 功能：返回x的系数a 公式：a = ( n sum( xy ) - sum( x ) sum( y ) ) / ( n sum( x^2 )
     * - sum(x) ^ 2 )
     */
    public static float getA(ArrayList<WB_Point> pts) {
        int n = pts.size();
        return (float) ((n * pSum(pts) - sumX(pts) * sumY(pts)) / (n * sqXSum(pts) - Math
                .pow(sumX(pts), 2)));
    }

    /*
     * 功能：返回常量系数系数b 公式：b = sum( y ) / n - a sum( x ) / n
     */
    public static float getB(ArrayList<WB_Point> pts) {
        int n = pts.size();
        float a = getA(pts);
        return sumY(pts) / n - a * sumX(pts) / n;
    }

    /*
     * 功能：求和X
     */
    private static float sumX(ArrayList<WB_Point> pts) {

        float s = 0;
        for (WB_Point pt : pts) {
            s = s + pt.xf();
        }
        return s;
    }

    /*
     * 功能：求和Y
     */
    private static float sumY(ArrayList<WB_Point> pts) {

        float s = 0;
        for (WB_Point pt : pts) {
            s = s + pt.yf();
        }
        return s;
    }


    /*
     * 功能：求X平方和
     */
    private static float sqXSum(ArrayList<WB_Point> pts) {
        float s = 0;
        for (WB_Point pt : pts) {
            s = (float) (s + Math.pow(pt.xf(), 2));
        }
        return s;
    }

    /*
     * 功能：返回对应项相乘后的和
     */
    private static float pSum(ArrayList<WB_Point> pts) {
        float s = 0;
        for (int i = 0; i < pts.size(); i++) {
            s = s + pts.get(i).xf() * pts.get(i).yf();
        }
        return s;
    }

    /*
     * 功能：main()测试线性回归的最小二乘法java实现函数
     */
    public static void main(String[] args) {
        float[] x =
//		{ 540, 360, 240, 480, 420 };
                {205, 325, 445, 505, 625};
        float[] y =
//		{  520, 475, 430, 386, 500 };
                {100, 123, 148, 407, 319, 301};
//        System.out.println("经线性回归后的y值：\t" + estimate(x, y, 240));
    }


}
