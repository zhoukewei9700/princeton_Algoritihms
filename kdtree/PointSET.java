/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private SET<Point2D> points;

    public PointSET() {      // construct an empty set of points
        points = new SET<Point2D>();
    }

    public boolean isEmpty() {       // is the set empty?
        return points.isEmpty();
    }

    public int size() {      // number of points in the set
        return points.size();
    }

    public void insert(
            Point2D p) {      // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!points.contains(p)) {
            points.add(p);
        }
    }

    public boolean contains(Point2D p) {     // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    public void draw() {     // draw all points to standard draw
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect) {        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException("rect is null");
        }
        List<Point2D> insidePoints = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                insidePoints.add(p);
            }
        }
        return insidePoints;
    }

    public Point2D nearest(
            Point2D p) {      // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        if (this.isEmpty()) return null;
        Point2D nearstPoint = null;
        double nearstDistance = Double.MAX_VALUE;
        for (Point2D point : points) {
            double currentDistance = point.distanceTo(p);
            if (currentDistance < nearstDistance) {
                nearstDistance = currentDistance;
                nearstPoint = point;
            }
        }
        return nearstPoint;
    }

    public static void main(
            String[] args) {                // unit testing of the methods (optional)
        // PointSET pointSET = new PointSET();
    }
}
