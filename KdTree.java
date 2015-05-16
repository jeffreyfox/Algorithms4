
public class KdTree {
    private Node root;
    private int N; //count
    private Point2D nbr; //neareest neighbor

    private class Node {
        private Point2D p; //point
        private RectHV box; //bounding box
        private Node left, right;
        public Node(Point2D p, RectHV box) {
            this.p = p;
            this.box = box;
            this.left = null;
            this.right = null;
        }
    }
    // construct an empty set of points
    public KdTree()      {
        root = null;
        N = 0;      
    }
    // is the set empty?
    public boolean isEmpty() { return N == 0;   }
    // number of points in the set
    public int size() {  return N;    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)  {
        if (p == null) throw new NullPointerException("Argument is null!");
        if (root == null) { //insert to an empty tree
            root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
            N++;
        } else insert(root, p, 0);        
    }
    //insert p to subtree t whose root is at level l (t is not null)
    private void insert(Node t, Point2D p, int l) {
        if (p.equals(t.p)) return; //equal, do nothing
        RectHV box = t.box;
        if (l % 2 == 0) { // even levels, compare with x
            if (p.x() < t.p.x()) { //insert to left
                if (t.left == null) {
                    t.left = new Node(p, new RectHV(box.xmin(), box.ymin(), t.p.x(), box.ymax()));
                    N++;
                } else insert(t.left, p, l+1);                
            } else { //insert to right
                if (t.right == null) {
                    t.right = new Node(p, new RectHV(t.p.x(), box.ymin(), box.xmax(), box.ymax()));
                    N++;
                } else insert(t.right, p, l+1);
            }
        } else { // odd levels, compare with y       
            if (p.y() < t.p.y()) { //insert to bottom
                if (t.left == null) {
                    t.left = new Node(p, new RectHV(box.xmin(), box.ymin(), box.xmax(), t.p.y()));
                    N++;
                } else insert(t.left, p, l+1);
            } else { //insert to top
                if (t.right == null) {
                    t.right = new Node(p, new RectHV(box.xmin(), t.p.y(), box.xmax(), box.ymax()));
                    N++;
                } else insert(t.right, p, l+1);
            }
        }
    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("Argument is null!");
        return contains(root, p, 0);
    }
    // does subtree t whose root is at level l contain p?
    private boolean contains(Node t, Point2D p, int l) {
        if (t == null) return false;
        if (p.equals(t.p)) return true;

        // even levels, compare with x; odd levels with y
        if (l % 2 == 0) {
            if (p.x() < t.p.x()) return contains(t.left, p, l+1);
            else return contains(t.right, p, l+1);            
        } else {
            if (p.y() < t.p.y()) return contains(t.left, p, l+1);
            else return contains(t.right, p, l+1);
        }
    }

    //    draw all points to standard draw
    public void draw() {
        if (root == null) return;
        draw(root, 0);
    }

    // draw line/point for node t at level l 
    private void draw(Node t, int l) {
        if (t == null) return;
        Point2D p = t.p; //current point

        //first draw point
        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(p.x(), p.y());

        RectHV box = t.box;
        if (l % 2 == 0) { //even levels, draw vertical line
            StdDraw.setPenRadius(.005);
            StdDraw.setPenColor(StdDraw.RED);       
            StdDraw.line(p.x(), box.ymin(), p.x(), box.ymax());      
        } else { //odd levels, draw horizontal line
            StdDraw.setPenRadius(.005);
            StdDraw.setPenColor(StdDraw.BLUE);            
            StdDraw.line(box.xmin(), p.y(), box.xmax(), p.y());
        } 
        draw(t.left,  l+1);
        draw(t.right, l+1);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("Argument is null!");
        Queue<Point2D> q = new Queue<Point2D>();
        range(rect, q, root, 0);
        return q;
    }
    //check ranges for query box rect at subtree t at level l
    private void range(RectHV rect, Queue<Point2D> q, Node t, int l) {
        if (t == null || !rect.intersects(t.box)) return;
        Point2D p = t.p;
        if (rect.contains(p)) q.enqueue(p);

        range(rect, q, t.left,  l+1);
        range(rect, q, t.right, l+1);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)  { 
        if (p == null) throw new NullPointerException("Argument is null!");
        if (root == null) return null;
        nbr = root.p;
        nearest(p, root, 0);
        return nbr;
    }

    //pi input point, pass reference to nearest distance
    private void nearest(Point2D pi, Node t, int l) { 
        if (t == null) return;

        Point2D p = t.p;
        double mind = pi.distanceSquaredTo(nbr);   
        double d = pi.distanceSquaredTo(p);     
        if (d < mind)  nbr = p;

        if (t.box.distanceSquaredTo(pi) >= mind) return;

        if ( (l % 2 == 0 && pi.x() < p.x()) || (l % 2 == 1 && pi.y() < p.y())) {
            //left first
            nearest(pi, t.left,  l+1);
            nearest(pi, t.right, l+1);
        } else {
            //right first
            nearest(pi, t.right, l+1);
            nearest(pi, t.left,  l+1);            
        }      
    }

    // unit testing of the methods (optional)
    public static void main(String[] args)   {
        StdDraw.setXscale();
        StdDraw.setYscale();
        KdTree kdtr = new KdTree();
        PointSET pset = new PointSET(); 
        int N = 5;
        for (int i = 0; i < N; ++i) {
            //double x = StdRandom.uniform();
            //double y = StdRandom.uniform();
            double x = 0.1*i+0.05;
            double y = 0.1*i+0.05;
            Point2D p = new Point2D(x, y);
            kdtr.insert(p);
            pset.insert(p);
        }
        kdtr.draw();
        Point2D p = new Point2D(0.21, 0.28);
        StdOut.println("Nearest point of " + p + " is " + pset.nearest(p) + " dist = " + p.distanceSquaredTo(pset.nearest(p)));
        StdOut.println("Nearest point of " + p + " is " + kdtr.nearest(p) + " dist = " + p.distanceSquaredTo(kdtr.nearest(p)));
    }
}
