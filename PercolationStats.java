public class PercolationStats {
    private int T; //T experiments
    private double[] frac; //threshold fractions in each experiment
    public PercolationStats(int N, int T)   {  // perform T independent experiments on an N-by-N grid
        if (N <= 0)
            throw new IllegalArgumentException("N should be positive!");
        if (T <= 0)
            throw new IllegalArgumentException("T should be positive!");
        this.T = T;
        frac = new double[T];

        for (int t = 0; t < T; ++t) {
            frac[t] = singleExpt(N);
        }
    }
    public double mean()    {                  // sample mean of percolation threshold
        double sum = 0.0;
        for (int t = 0; t < T; ++t) {
            sum += frac[t];
        }
        return sum / T;
    }
    public double stddev()   {                 // sample standard deviation of percolation threshold
        if (T == 1) return Double.NaN;
        double sum = 0.0;
        double avg = mean();
        for (int t = 0; t < T; ++t) {
            sum += (frac[t] - avg)*(frac[t] -avg);
        }
        return Math.sqrt(sum / (T-1));
    }
    public double confidenceLo()  {            // low  endpoint of 95% confidence interval
        return mean() - 1.96*stddev()/Math.sqrt(T);
    }
    public double confidenceHi() {             // high endpoint of 95% confidence interval
        return mean() + 1.96*stddev()/Math.sqrt(T);
    }

    private double singleExpt(int N) {
        Percolation pc = new Percolation(N);
        int k = 0;
        while (k < N*N) {
            int i = 1+StdRandom.uniform(0, N);
            int j = 1+StdRandom.uniform(0, N);
            if (!pc.isOpen(i, j)) { 
                pc.open(i, j);
                k++;
                if (pc.percolates()) break;
            }
        }
        return k / (double) (N*N);
    }

    public static void main(String[] args) {   // test client (described below)
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats pcstat = new PercolationStats(N, T);
        System.out.printf("mean                    = %.10f\n", pcstat.mean());
        System.out.printf("stddev                  = %.10f\n", pcstat.stddev());
        System.out.printf("95%% confidence interval = %.10f, %.10f\n", pcstat.confidenceLo(), pcstat.confidenceHi());
    }
}
