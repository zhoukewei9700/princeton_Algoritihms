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

public class FastCollinearPoints {
    private final List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        // Exception modify
        if (points == null) throw new IllegalArgumentException();
        // copy
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("point null");
            copy[i] = points[i];
        }
        Arrays.sort(copy);
        for (int i = 0; i < copy.length - 1; i++) {
            if (copy[i].compareTo(copy[i + 1]) == 0) throw new IllegalArgumentException();
        }
        segments = new ArrayList<>();

        // compare
        Point currentBase;
        int length = copy.length;
        Point[] otherPoints = new Point[length - 1];

        if (length < 4) {
            return;
        }

        for (int p = 0; p < length; p++) {
            currentBase = copy[p];
            // copy otherPoints
            for (int i = 0; i < length; i++) {
                if (i < p) otherPoints[i] = copy[i];
                if (i > p) otherPoints[i - 1] = copy[i];
            }

            // start to compare
            // first sort otherPoints by slope order
            Arrays.sort(otherPoints, currentBase.slopeOrder());
            // count the collinear points
            int count = 1;
            double lastSlope = otherPoints[0].slopeTo(currentBase);
            double currentSlope;
            for (int j = 1; j < length - 1; j++) {
                currentSlope = otherPoints[j].slopeTo(currentBase);
                // only record when currentBase was the start point
                if (currentSlope == lastSlope) {
                    count++;
                    // come to last point
                    if (j == length - 2) {
                        // segments >=3 and the smallest point in otherPoints is bigger than currentBase
                        if (count >= 3 && currentBase.compareTo(otherPoints[j - count + 1]) < 0) {
                            // start point is currentBase, end point is the last point of otherPoints
                            LineSegment lineSegment = new LineSegment(currentBase, otherPoints[j]);
                            segments.add(lineSegment);
                        }
                    }
                }
                else {
                    if (count >= 3 && currentBase.compareTo(otherPoints[j - count]) < 0) {
                        LineSegment lineSegment = new LineSegment(currentBase, otherPoints[j - 1]);
                        segments.add(lineSegment);
                    }
                    count = 1; // count restart from 1
                }
                lastSlope = currentSlope;
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
        In in = new In("input6.txt");
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
