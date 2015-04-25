
public class Percolation {
    private int[] flag; //flag for all N*N sites, 3 bits(open/conn. top/ conn. bot)
    private int N; //size of board
    private boolean percolates;
    private WeightedQuickUnionUF uf;

    public Percolation(int N)  {             // create N-by-N grid, with all sites blocked
        if (N <= 0)
            throw new IllegalArgumentException("N should be positive!");
        this.N = N;
        flag = new int[N*N];
        for (int i = 0; i < N; ++i) {
            flag[i] = 0; //blocked
        }
        percolates = false;
        uf = new WeightedQuickUnionUF(N*N);
    }

    public void open(int i, int j)  {        // open site (row i, column j) if it is not open already
        if (isOpen(i, j)) return;
        int idx = index(i, j);
        flag[idx] |= 1;

        if (i == 1) flag[idx] |= 2;
        if (i == N) flag[idx] |= 4;

        int p = uf.find(idx);

        int ni, nj, nidx; //neighbor
        int np; //parent and neighbor's parent

        //left
        ni = i; 
        nj = j-1;
        if (isValid(ni, nj) && isOpen(ni, nj)) {
            nidx = index(ni, nj);
            np = uf.find(nidx);
            flag[idx] |= flag[np];
            uf.union(idx, nidx);
        }

        //right
        ni = i;
        nj = j+1;
        if (isValid(ni, nj) && isOpen(ni, nj)) {
            nidx = index(ni, nj);
            np = uf.find(nidx);
            flag[idx] |= flag[np];
            uf.union(idx, nidx);
        }

        //top
        ni = i-1;
        nj = j;
        if (isValid(ni, nj) && isOpen(ni, nj)) {
            nidx = index(ni, nj);
            np = uf.find(nidx);
            flag[idx] |= flag[np];
            uf.union(idx, nidx);
        }

        //bottom
        ni = i+1;
        nj = j;
        if (isValid(ni, nj) && isOpen(ni, nj)) {
            nidx = index(ni, nj);
            np = uf.find(nidx);
            flag[idx] |= flag[np];
            uf.union(idx, nidx);
        }

        //update status of new parent after union
        int newp = uf.find(idx);
        flag[newp] |= flag[idx];
        if (flag[newp] == 7) percolates = true;
    }

    public boolean isOpen(int i, int j)  {   // is site (row i, column j) open?
        validate(i, j);
        int idx = index(i, j);
        return ((flag[idx] & 1) != 0);
    }

    public boolean isFull(int i, int j)  {    // is site (row i, column j) full?
        if (! isOpen(i, j)) return false;
        int idx = index(i, j);
        int p = uf.find(idx); //parent
        return ((flag[p] & 2) != 0);
    }

    public boolean percolates() {            // does the system percolate?
        return percolates;
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
        while (k < N*N) {
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
