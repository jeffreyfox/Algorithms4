
public class Percolation {
    private int[][] flag; //0 blocked, 1 open
    private int N; //size of board
    private WeightedQuickUnionUF uf;
    private int idx_top, idx_bot;
    public Percolation(int N)  {             // create N-by-N grid, with all sites blocked
        if (N <= 0)
            throw new IllegalArgumentException("N should be positive!");
        this.N = N;
        flag = new int[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                flag[i][j] = 0; //blocked
            }
        }
        uf = new WeightedQuickUnionUF(N*N+2);
        idx_top = 0;
        idx_bot = N*N+1;
    }

    public void open(int i, int j)  {        // open site (row i, column j) if it is not open already
        if (isOpen(i, j)) return;
        flag[i-1][j-1] = 1;
        int idx = index(i, j);
        if (i == 1) uf.union(idx, idx_top);
        if (i == N) uf.union(idx, idx_bot); //N could be 1

        int ni, nj; //neighbor
        //left
        ni = i; 
        nj = j-1;
        if (isValid(ni, nj) && isOpen(ni, nj)) uf.union(idx, index(ni, nj));
        //right
        ni = i;
        nj = j+1;
        if (isValid(ni, nj) && isOpen(ni, nj)) uf.union(idx, index(ni, nj));
        //top
        ni = i-1;
        nj = j;
        if (isValid(ni, nj) && isOpen(ni, nj)) uf.union(idx, index(ni, nj));
        //bottom
        ni = i+1;
        nj = j;
        if (isValid(ni, nj) && isOpen(ni, nj)) uf.union(idx, index(ni, nj));
    }

    public boolean isOpen(int i, int j)  {   // is site (row i, column j) open?
        validate(i, j);
        return (flag[i-1][j-1] == 1);
    }
    public boolean isFull(int i, int j)  {    // is site (row i, column j) full?
        return isOpen(i, j) && uf.connected(index(i, j), idx_top); 
    }

    public boolean percolates() {            // does the system percolate?
        return uf.connected(idx_top, idx_bot);
    }

    private int index(int i, int j) {
        validate(i, j);
        return (i-1)*N+j-1;
    }

    private void validate(int i, int j) {
        if (i < 1 || i > N) 
            throw new IndexOutOfBoundsException("x-index " + i + " out of range!");
        if (j < 1 || j > N) 
            throw new IndexOutOfBoundsException("y-index " + j + " out of range!");
    }

    private boolean isValid(int i, int j) {
        return (i >= 1 && i <= N && j >= 1 && j <= N);
    }

    public static void main(String[] args) {  // test client (optional)
        int N = Integer.parseInt(args[0]);
        Percolation pc = new Percolation(N);
        int k = 0;
        while(k < N*N) {
            int i = 1+StdRandom.uniform(0, N);
            int j = 1+StdRandom.uniform(0, N);
            if (!pc.isOpen(i, j)) { 
                pc.open(i, j);
                k++;
                if (pc.percolates()) {
                    StdOut.println("Percolates after openning " + k + " sites.");
                    break;
                }
            }
        }
        if (k == N*N) 
            StdOut.println("Does not percolate after openning " + k + " sites.");
    }
}
