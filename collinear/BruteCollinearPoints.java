/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("arguments null");

        Point[] copy = new Point[points.length];
        segments = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("point null");
            copy[i] = points[i];
        }
        Arrays.sort(copy);
        for (int i = 0; i < copy.length - 1; i++) {
            if (copy[i].compareTo(copy[i + 1]) == 0) {
                throw new IllegalArgumentException("repeat point");
            }
        }

        // find segments
        for (int p1 = 0; p1 < copy.length - 3; p1++) {
            for (int p2 = p1 + 1; p2 < copy.length - 2; p2++) {
                double s1 = copy[p1].slopeTo(copy[p2]);
                for (int p3 = p2 + 1; p3 < copy.length - 1; p3++) {
                    double s2 = copy[p2].slopeTo(copy[p3]);
                    if (s1 == s2) {
                        for (int p4 = p3 + 1; p4 < copy.length; p4++) {
                            double s3 = copy[p3].slopeTo(copy[p4]);
                            if (s2 == s3) segments.add(new LineSegment(copy[p1], copy[p4]));
                        }
                    }
                }
            }
        }


    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[segments.size()];
        int i = 0;
        for (LineSegment seg : segments) {
            res[i++] = seg;
        }
        return res;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In("input8.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
