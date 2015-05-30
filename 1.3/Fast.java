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
        for (i = 0; i < N; ++i) { // pts[i] as reference point
            Arrays.sort(pts, i, N); //sort pts[i .. N) in natural order
            Arrays.sort(pts, i, N, pts[i].SLOPE_ORDER); //sort pts[i..N) in slope order w.r.t pts[i]
            //search in pts[i+1..N) four >=3 consecutively equal slopes
            j = i+1;            
            while (j < N) {
                double slope = pts[i].slopeTo(pts[j]);
                int oldj = j;
                while (j < N-1 && pts[i].slopeTo(pts[j+1]) == slope) j++;
                //pts[oldj, j] are points with equal slopes w.r.t pts[i]
                if(j - oldj >= 2 && !duplicated(pts, i, slope)) {
                    Arrays.sort(pts, oldj, j+1); //sort pts[j-count, j] for printing
                    printCollinear(pts, i, oldj, j);
                }
                oldj = j;
                j++;                
            }        
        }
        //draw points
        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (i = 0; i < N; ++i) 
            pts[i].draw();
    }

    //search points before i for a given slope
    private static boolean duplicated(Point[] pts, int i, double slope)  {
        for (int k = 0; k < i; ++k) {
            if (pts[k].slopeTo(pts[i]) == slope)
                return true;
        }
        return false;
    }

    //print and draw points i -> j1 -> j1+1 -> ... -> j2
    private static void printCollinear(Point[] pts, int i, int j1, int j2) {
        StdOut.print(pts[i] + " -> ");
        for (int k = j1; k <= j2; ++k) {
            if (k != j2) StdOut.print(pts[k] + " -> ");
            else StdOut.print(pts[k] + "\n");
        }     
        pts[i].drawTo(pts[j2]);    
    }
}
