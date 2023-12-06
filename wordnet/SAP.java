/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("graph can not be null");
        }
        graph = G;
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(graph, w);
        int minDist = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                int currentDist = bfdp1.distTo(i) + bfdp2.distTo(i);
                minDist = Math.min(currentDist, minDist);
            }
        }
        return minDist == Integer.MAX_VALUE ? -1 : minDist;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(graph, w);
        int ancestor = -1;
        int minDist = graph.E();
        for (int i = 0; i < graph.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                int currentDist = bfdp1.distTo(i) + bfdp2.distTo(i);
                if (currentDist < minDist) {
                    minDist = currentDist;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("variable can not be null");
        }
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(graph, w);
        int minDist = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); i++) {
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                int currentDist = bfdpV.distTo(i) + bfdpW.distTo(i);
                minDist = Math.min(currentDist, minDist);
            }
        }
        return minDist == Integer.MAX_VALUE ? -1 : minDist;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("variable can not be null");
        }
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(graph, w);
        int minDist = graph.E();
        int ancestor = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                int currentDist = bfdpV.distTo(i) + bfdpW.distTo(i);
                if (currentDist < minDist) {
                    minDist = currentDist;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    public static void main(String[] args) {

    }
}
