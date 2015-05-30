/*************************************************************************
 *  Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();
    
    private class SlopeOrder implements Comparator<Point>
    {
        public int compare(Point v, Point w) 
        {
            double slope1 = Point.this.slopeTo(v);
            double slope2 = Point.this.slopeTo(w);
            if      (slope1 < slope2) return -1;
            else if (slope1 > slope2) return  1;
            else return 0;
        }
    }

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if (this.x == that.x) {
            if (that.y != this.y) return Double.POSITIVE_INFINITY;
            else return Double.NEGATIVE_INFINITY;
        } 
        if (this.y == that.y) return 0.0;
        return ((double) (that.y - this.y)) / (that.x - this.x); 
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return  1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return  1;
        return 0;
    }

    // return string representation of this point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(4, 5);
        StdOut.println("P1 = " + p1 + " P2 = " + p2);
        StdDraw.setXscale(0, 10);
        StdDraw.setYscale(0, 10);
        StdDraw.setPenRadius(.01);
        p1.draw();
        p2.draw();
        //p1.drawTo(p2);
    }
}
