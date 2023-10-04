/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;

import java.util.Arrays;

public class Solver {
    private Board[] resultPath;
    private boolean isSovable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }
        isSovable = false;
        GameTreeNode root = new GameTreeNode(initial, null);
        MinPQ<GameTreeNode> minPQ = new MinPQ<>();
        minPQ.insert(root);
        GameTreeNode rootTwin = new GameTreeNode(initial.twin(), null);
        minPQ.insert(rootTwin);


        while (!minPQ.isEmpty()) {
            GameTreeNode currentNode = minPQ.delMin();
            for (Board nbrBoard : currentNode.getBoard().neighbors()) {
                if (currentNode.getFather() == null || !(nbrBoard.equals(
                        currentNode.getFather().getBoard()))) {
                    minPQ.insert(new GameTreeNode(nbrBoard, currentNode));
                }
            }
            if (currentNode.getBoard().isGoal()) {
                GameTreeNode node = currentNode;
                resultPath = new Board[node.getMoves() + 1];
                for (int i = node.getMoves(); i >= 0; i--) {
                    resultPath[i] = node.getBoard();
                    node = node.getFather();
                }
                if (resultPath[0].equals(initial)) {
                    isSovable = true;
                }
                break;
            }
        }

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSovable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSovable) return resultPath.length - 1;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSovable) return Arrays.asList(resultPath);
        else return null;
    }

    // test client
    public static void main(String[] args) {
        int[][] tiles = new int[][] { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        Board b = new Board(tiles);
        Solver solver = new Solver(b);
        System.out.println(solver.isSovable);
        System.out.println(solver.moves());
        for (Board board : solver.resultPath) {
            System.out.println(board.toString());
        }
    }
}

class GameTreeNode implements Comparable<GameTreeNode> {
    private Board board;
    private final int moves;
    private final int priority;
    private final int manhattan;

    private final GameTreeNode father;

    public GameTreeNode(Board b, GameTreeNode father) {
        this.board = b;
        this.father = father;
        this.manhattan = this.board.manhattan();
        if (father != null) {
            this.moves = father.moves + 1;
            this.priority = manhattan + this.moves;
        }
        else {
            this.moves = 0;
            this.priority = manhattan + this.moves;
        }
    }

    public Board getBoard() {
        return board;
    }

    public int getMoves() {
        return moves;
    }

    public int getPriority() {
        return priority;
    }

    public GameTreeNode getFather() {
        return father;
    }

    public int compareTo(GameTreeNode o) {
        if (this.priority == o.priority) {
            return manhattan - o.manhattan;
        }
        return this.priority - o.priority;
    }
}
