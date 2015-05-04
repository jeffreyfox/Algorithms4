import java.util.Arrays;

public class Fast {
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

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(.005);
        StdDraw.setPenColor(StdDraw.RED);

        int i, j;
        for (i = 0; i < N; ++i) { //pts[i] as p
            Arrays.sort(pts, i, N); //first sort natural order
            Arrays.sort(pts, i, N, pts[i].SLOPE_ORDER); //sort pts[i..N)
            int count = 1;
            for (j = i+1; j < N; ++j) { //find 3 points in a row with same slope
                if (pts[i].slopeTo(pts[j]) == pts[i].slopeTo(pts[j-1])) count++;
                else {
                    if (count >= 3) {
                        Arrays.sort(pts, j-count, j);
                        StdOut.print(pts[i] + " -> ");
                        for (int k = j-count; k < j; ++k) {
                            if (k != j-1) StdOut.print(pts[k] + " -> ");
                            else StdOut.print(pts[k] + "\n");
                        }   
                        pts[i].drawTo(pts[j-1]);
                        //   StdOut.println("draw (" + pts[i] + " " + pts[j-1] + ")");
                    }
                    count = 1;
                }
            }
            if (count >= 3) {
                Arrays.sort(pts, j-count, j);
                StdOut.print(pts[i] + " -> ");
                for (int k = j-count; k < j; ++k) {
                    if (k != j-1) StdOut.print(pts[k] + " -> ");
                    else StdOut.print(pts[k] + "\n");
                }     
                pts[i].drawTo(pts[j-1]);    
                //   StdOut.println("draw (" + pts[i] + " " + pts[j-1] + ")");
            }
        }
        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (i = 0; i < N; ++i) 
            pts[i].draw();
    }
}
