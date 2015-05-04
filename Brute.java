import java.util.Arrays;

public class Brute {
    private static void print(Point p1, Point p2, Point p3, Point p4) {
        StdOut.println(p1 + " -> " + p2 + " -> " + p3 + " -> " + p4);
    }

    public static void main(String[] args) {
        int[] numbers = In.readInts(args[0]);
        int c = 0;
        int N = numbers[c++];
        Point[] pts = new Point[N];
        for (int i = 0; i < N; ++i) {
            int x = numbers[c++];
            int y = numbers[c++];
            pts[i] = new Point(x, y);
        } 

        Arrays.sort(pts);        
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(.005);
        StdDraw.setPenColor(StdDraw.RED);
        for (int i = 0; i < N; ++i) {
            for (int j = i+1; j < N; ++j) {
                for (int k = j+1; k < N; ++k) {
                    for (int l = k+1; l < N; ++l) {
                        double sl1 = pts[i].slopeTo(pts[j]);
                        double sl2 = pts[i].slopeTo(pts[k]);
                        double sl3 = pts[i].slopeTo(pts[l]);
                        if (sl1 == sl2 && sl1 == sl3) {
                            Arrays.sort(pts, j, l+1);
                            print(pts[i], pts[j], pts[k], pts[l]);
                            pts[i].drawTo(pts[l]);                            
                        }
                    }
                }
            }
        }
        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; ++i) 
            pts[i].draw();
    }
}
