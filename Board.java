
public class Board {
    private int[][] b;
    private int N;

    public Board(int[][] blocks)  // construct a b from an N-by-N array of blocks
    {
        N = blocks.length;
        b = new int[N][N];
        for (int i = 0; i < N; ++i) 
            for (int j = 0; j < N; ++j)
                b[i][j] = blocks[i][j];
    }
    // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // b dimension N
    {   return N;  }

    public int hamming()  // number of blocks out of place
    {
        int cnt = 0;
        for (int i = 0; i < N; ++i) 
            for (int j = 0; j < N; ++j) {                
                int v = b[i][j];
                if (v == 0) continue;
                int ig = (v-1) / N, jg = (v-1) % N; 
                if (ig != i || jg != j) cnt++;
            }

        return cnt;
    }
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int cnt = 0;
        for (int i = 0; i < N; ++i) 
            for (int j = 0; j < N; ++j) {                
                int v = b[i][j];
                if (v == 0) continue;
                int ig = (v-1) / N, jg = (v-1) % N; 
                cnt += Math.abs(ig-i) + Math.abs(jg-j);
            }

        return cnt;
    }
    public boolean isGoal()                // is this b the goal b?
    {
        for (int i = 0; i < N; ++i) 
            for (int j = 0; j < N; ++j) {                
                int v = b[i][j];
                if (v == 0) continue;
                int ig = (v-1) / N, jg = (v-1) % N; 
                if (ig != i || jg != j) return false;
            }

        return true;
    }
    public Board twin()                    // a b that is obtained by exchanging two adjacent blocks in the same row
    {
        Board bd = new Board(b);
        bd.exch();
        return bd;
    }

    private void exch() //exchange two adjacent elements in a row 
    {
        if   (b[0][0] == 0 || b[0][1] == 0) exch(1, 0, 1, 1); //exchange second row
        else                                exch(0, 0, 0, 1); //exchange first row
    }
    //exchange two objects (i1, j1), (i2, j2)
    private void exch(int i1, int j1, int i2, int j2) {        
        int t = b[i1][j1];
        b[i1][j1] = b[i2][j2];
        b[i2][j2] = t;
    }
    public boolean equals(Object y)        // does this b equal y?
    {
        if (y == this) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;

        Board that = (Board) y;
        if (this.N != that.N) return false;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (this.b[i][j] != that.b[i][j]) return false;
            }
        }
        return true;
    }
    public Iterable<Board> neighbors()     // all neighboring bs
    {
        int i = 0, j = 0;
        outerloop:
            for (i = 0; i < N; ++i) 
                for (j = 0; j < N; ++j)             
                    if (b[i][j] == 0) break outerloop;

        Queue<Board> q = new Queue<Board>();
        if (i+1 < N) {
            Board nb = new Board(b);
            nb.exch(i, j, i+1, j); //right
            q.enqueue(nb);
        }
        if (j > 0) {
            Board nb = new Board(b);
            nb.exch(i, j, i, j-1); //top
            q.enqueue(nb);
        }        
        if (i > 0) {
            Board nb = new Board(b);
            nb.exch(i, j, i-1, j); //left
            q.enqueue(nb);
        }
        if (j+1 < N) {
            Board nb = new Board(b);
            nb.exch(i, j, i, j+1); //bottom
            q.enqueue(nb);
        }
        return q;                  
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", b[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial);
        for (Board board : initial.neighbors())
            StdOut.println(board);
    }

}
