/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;

public class Board {

    private int[][] myTiles;
    // 0 represent blank square
    private int n;
    private int hamming;
    private int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        myTiles = new int[n][n];
        hamming = 0;
        manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                myTiles[i][j] = tiles[i][j];
                if (myTiles[i][j] != 0) {
                    int shouldAt = myTiles[i][j] - 1;
                    int nowAt = i * n + j;
                    hamming += (shouldAt != nowAt ? 1 : 0);
                    int vertical = Math.abs(i - shouldAt / n);
                    int horizontal = Math.abs(j - shouldAt % n);
                    manhattan += vertical + horizontal;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        String res = String.format("%d\n", n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res += String.format(" %d", myTiles[i][j]);
            }
            res += "\n";
        }

        return res;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        // for (int i=0;i<n;i++){
        //     for (int j=0;j<n;j++){
        //         int shouldAt
        //     }
        // }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || !(y instanceof Board)) {
            return false;
        }
        if (y == this) return true;
        Board b = (Board) y;
        return this.toString().equals(b.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborsBoard = new ArrayList<Board>();
        int blankI = -1;
        int blankJ = -1;
        boolean findBlank = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.myTiles[i][j] == 0) {
                    blankI = i;
                    blankJ = j;
                    findBlank = true;
                    break;
                }
                if (findBlank) break;
            }
        }
        // if corner or side
        if (blankI == 0) {
            // left top
            if (blankJ == 0) {
                neighborsBoard.add(new Board(swap(this.myTiles, 0, 0, 0, 1)));
                neighborsBoard.add(new Board(swap(this.myTiles, 0, 0, 1, 0)));
            }
            // right top
            else if (blankJ == n - 1) {
                neighborsBoard.add(new Board(swap(this.myTiles, 0, n - 1, 0, n - 2)));
                neighborsBoard.add(new Board(swap(this.myTiles, 0, n - 1, 1, n - 1)));
            }
            // top side
            else {
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI, blankJ - 1)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI + 1, blankJ)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI, blankJ + 1)));
            }
        }
        else if (blankI == n - 1) {
            // left bottom
            if (blankJ == 0) {
                neighborsBoard.add(new Board(swap(this.myTiles, n - 1, 0, n - 1, 1)));
                neighborsBoard.add(new Board(swap(this.myTiles, n - 1, 0, n - 2, 0)));
            }
            // right bottom
            else if (blankJ == n - 1) {
                neighborsBoard.add(new Board(swap(this.myTiles, n - 1, n - 1, n - 2, n - 1)));
                neighborsBoard.add(new Board(swap(this.myTiles, n - 1, n - 1, n - 1, n - 2)));
            }
            // bottom side
            else {
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI, blankJ + 1)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI, blankJ - 1)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI - 1, blankJ)));
            }
        }
        // left and right side
        else {
            // left side
            if (blankJ == 0) {
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI, blankJ + 1)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI + 1, blankJ)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI - 1, blankJ)));
            }
            // right side
            else if (blankJ == n - 1) {
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI, blankJ - 1)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI + 1, blankJ)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI - 1, blankJ)));
            }
            // middle
            else {
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI, blankJ + 1)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI, blankJ - 1)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI + 1, blankJ)));
                neighborsBoard.add(
                        new Board(swap(this.myTiles, blankI, blankJ, blankI - 1, blankJ)));
            }
        }
        return neighborsBoard;
    }

    private int[][] swap(int[][] tiles, int i1, int j1, int i2, int j2) {
        // int[][] res = tiles.clone();//2d array clone is shallow clone, need to clone every 1d array
        int[][] res = new int[this.n][this.n];
        for (int i = 0; i < this.n; i++) {
            res[i] = tiles[i].clone();
        }
        int temp = tiles[i1][j1];
        res[i1][j1] = tiles[i2][j2];
        res[i2][j2] = temp;
        return res;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (this.myTiles[0][0] == 0) {
            return new Board(swap(this.myTiles, 0, 1, 0, 2));
        }
        else {
            if (this.myTiles[0][1] == 0) {
                return new Board(swap(this.myTiles, 0, 0, 0, 2));
            }
            return new Board(swap(this.myTiles, 0, 0, 0, 1));
        }
    }

    public static void main(String[] args) {
        int[][] tiles = new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board b = new Board(tiles);
        System.out.println(b.toString());
        System.out.println(b.dimension());
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        int[][] tiles2 = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board c = new Board(tiles2);
        System.out.println(c.isGoal());
        System.out.println(b.neighbors());
    }
}
