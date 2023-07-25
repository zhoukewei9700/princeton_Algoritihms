/* *****************************************************************************
 *  Name:              zhoukewei
 *  Last modified:     7/24/2023
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private Percolation percolation;
    private int myN;
    private int myTrials;
    private double[] thresholdArray;
    private double resMean;
    private double resStddev;
    private double resConfidenceLo;
    private double resConfidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        checkValidation(n, trials);
        myN = n;
        myTrials = trials;
        thresholdArray = new double[trials];
        resMean = 0;
        resStddev = Double.NaN;
        resConfidenceLo = Double.NaN;
        resConfidenceHi = Double.NaN;
        // simulation
        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                if (percolation.isOpen(row, col)) continue;
                percolation.open(row, col);
            }
            thresholdArray[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        resMean = StdStats.mean(thresholdArray);
        return resMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        resStddev = StdStats.stddev(thresholdArray);
        return resStddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        resConfidenceLo = resMean - (1.96 * resStddev) / Math.sqrt(myTrials);
        return resConfidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        resConfidenceHi = resMean + (1.96 * resStddev) / Math.sqrt(myTrials);
        return resConfidenceHi;
    }

    // check input validation
    private void checkValidation(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("argument n is uncorrect!");
        if (trials <= 0) throw new IllegalArgumentException("argument trials is uncorrect!");
    }

    // test client (see below)
    public static void main(String[] args) {
        // int n = 200;
        // int t = 100;
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(n, t);
        StdOut.println("mean                            = " + p.mean());
        StdOut.println("stddev                          = " + p.stddev());
        StdOut.printf(
                "95%%confidence interval          = [" + p.confidenceLo() + ", " + p.confidenceHi()
                        + "]");

    }
}
