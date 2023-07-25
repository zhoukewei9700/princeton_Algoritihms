/* *****************************************************************************
 *  Name:              zhoukewei
 *  Last modified:     7/24/2023
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int dimension;
    private boolean[] grid;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF locationIsFull;
    private int vitrulTop;
    private int virtualBottom;
    private int openSites;

    /***
     * creates n-by-n grid, with all sites initially blocked
     * @param n dimension
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n < 0!");
        dimension = n;
        grid = new boolean[n * n];
        uf = new WeightedQuickUnionUF(n * n + 2);  // union find with virtual top and virtual bottom
        locationIsFull = new WeightedQuickUnionUF(
                n * n + 1);  // union find without virtual top and virtual bottom
        vitrulTop = n * n;
        virtualBottom = n * n + 1;
        openSites = 0;
    }

    /***
     * opens the site (row, col) if it is not open already
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        checkValidation(row, col);
        if (isOpen(row, col)) return;
        grid[flat(row, col)] = true;
        openSites++;
        if (row == 1) {
            uf.union(flat(row, col), vitrulTop);
            locationIsFull.union(flat(row, col), vitrulTop);
        }
        if (row == dimension) {
            uf.union(flat(row, col), virtualBottom);
        }
        // up
        if (row - 1 > 0) {
            if (isOpen(row - 1, col)) {
                uf.union(flat(row, col), flat(row - 1, col));
                locationIsFull.union(flat(row, col), flat(row - 1, col));
            }
        }
        // down
        if (row + 1 <= dimension) {
            if (isOpen(row + 1, col)) {
                uf.union(flat(row, col), flat(row + 1, col));
                locationIsFull.union(flat(row, col), flat(row + 1, col));
            }
        }
        // left
        if (col - 1 > 0) {
            if (isOpen(row, col - 1)) {
                uf.union(flat(row, col), flat(row, col - 1));
                locationIsFull.union(flat(row, col), flat(row, col - 1));
            }
        }
        // right
        if (col + 1 <= dimension) {
            if (isOpen(row, col + 1)) {
                uf.union(flat(row, col), flat(row, col + 1));
                locationIsFull.union(flat(row, col), flat(row, col + 1));
            }
        }

    }

    /***
     * is the site (row, col) open?
     * @param row
     * @param col
     * @return true if (row,col) is open, else return false
     */
    public boolean isOpen(int row, int col) {
        checkValidation(row, col);
        return grid[flat(row, col)];
    }

    /***
     * is the site (row, col) full?
     * @param row
     * @param col
     * @return true if (row,col) can be filled with liquid, else false
     */
    public boolean isFull(int row, int col) {
        checkValidation(row, col);
        return locationIsFull.find(flat(row, col)) == locationIsFull.find(vitrulTop);
    }

    /***
     * returns the number of open sites
     * @return the number of open sites
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /***
     * does the system percolate?
     * @return true if system percolate
     */
    public boolean percolates() {
        return uf.find(vitrulTop) == uf.find(virtualBottom);
    }

    /**
     * check if the location is valid
     *
     * @param row
     * @param col
     * @return true if (row,col) is not out of bound
     */
    private boolean checkValidation(int row, int col) {
        if (row > dimension || row <= 0) {
            throw new IllegalArgumentException("row out of range!");
        }
        if (col > dimension || col <= 0) {
            throw new IllegalArgumentException("col out of range!");
        }
        return true;
    }

    /**
     * turn 2D array to 1D
     *
     * @param row
     * @param col
     * @return the (row,col) index of 1D array
     */
    private int flat(int row, int col) {
        return dimension * (row - 1) + col - 1;
    }

    public static void main(String[] args) {

    }
}
