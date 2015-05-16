import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pts;
    private int N; //count
    // construct an empty set of points
    public         PointSET()      {
        pts = new TreeSet<Point2D>();
        N = 0;
    }
    // is the set empty?
    public boolean isEmpty() { return N == 0;   }
    // number of points in the set
    public int size() {  return N;    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)  {
        if (p == null) throw new NullPointerException("Argument is null!");
        if (!pts.contains(p)) { 
            pts.add(p);
            N++;
        }
    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("Argument is null!");
        return pts.contains(p); 
    }
    //    draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D p : pts)
            p.draw();
    }
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("Argument is null!");
        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D p : pts) {
            if (rect.contains(p)) q.enqueue(p);
        }
        return q;
    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)  { 
        if (p == null) throw new java.lang.NullPointerException("Argument is null!");
        if (pts.isEmpty()) return null;
        double mind = Double.MAX_VALUE;
        Point2D nbr = new Point2D(0.0, 0.0);
        for (Point2D pt : pts) {
            double d = p.distanceTo(pt);
            if (d < mind) {
                mind = d;
                nbr = pt;
            }
        }

        return nbr;
    }
    // unit testing of the methods (optional)
    public static void main(String[] args)   {
        StdDraw.setXscale();
        StdDraw.setYscale();
        PointSET pset = new PointSET();
        int N = 100;
        for (int i = 0; i < N; ++i) {
            double x = StdRandom.uniform();
            double y = StdRandom.uniform();
            Point2D p = new Point2D(x, y);
            pset.insert(p);
        }
        pset.draw();
    }
}
