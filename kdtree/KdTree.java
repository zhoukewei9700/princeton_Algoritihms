/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private class Node {
        private boolean isVertical; // is segment vertical?
        private Point2D point;
        private RectHV rect;
        private Node left, right;
        private int size;

        public Node(boolean isVertical, Point2D point, RectHV rect, int size) {
            this.isVertical = isVertical;
            this.point = point;
            this.rect = rect;
            this.size = size;
        }

        public boolean isVertical() {
            return isVertical;
        }

        public RectHV leftRect() {
            if (isVertical) {
                return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            }
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
        }

        public RectHV rightRect() {
            if (isVertical) {
                return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
            return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
        }
    }

    private Node root;
    private Point2D nearest_point;
    private double distance;

    public KdTree() {
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        else {
            return node.size;
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void insert(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("point is null");
        }
        root = insert(point, root, null, true);
    }

    private Node insert(Point2D point, Node node, Node parent, boolean isLeft) {
        if (node == null) {
            if (parent == null) {
                return new Node(true, point, new RectHV(0, 0, 1, 1), 1);
            }
            else {
                if (isLeft) {
                    return new Node(!parent.isVertical(), point, parent.leftRect(), 1);
                }
                else {
                    return new Node(!parent.isVertical(), point, parent.rightRect(), 1);
                }
            }
        }
        else if (node.point.equals(point)) {
            return node;
        }
        else if (node.isVertical()) {
            if (point.x() < node.point.x()) {
                node.left = insert(point, node.left, node, true);
            }
            else {
                node.right = insert(point, node.right, node, false);
            }
        }
        else {
            if (point.y() < node.point.y()) {
                node.left = insert(point, node.left, node, true);
            }
            else {
                node.right = insert(point, node.right, node, false);
            }
        }

        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("point is null");
        }
        return contains(root, point);
    }

    private boolean contains(Node node, Point2D point) {
        if (node.point.equals(point)) return true;
        else if (node.isVertical()) {
            if (point.x() < node.point.x()) return contains(node.left, point);
            else return contains(node.right, point);
        }
        else {
            if (point.y() < node.point.y()) return contains(node.left, point);
            else return contains(node.right, point);
        }
    }

    public void draw() {
        draw(root, null);
    }

    private void draw(Node node, Node parent) {
        if (node == null) return;
        if (parent == null) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.point.x(), 0, node.point.x(), 1);
        }
        else if (node.isVertical()) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.point.x(), node.point.y());
        draw(node.left, node);
        draw(node.right, node);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rect null");
        }
        List<Point2D> points = new ArrayList<>();
        range(rect, points, root);
        return points;
    }

    private void range(RectHV rect, List<Point2D> points, Node node) {
        if (node == null) return;
        if (rect.contains(node.point)) points.add(node.point);
        if (node.isVertical()) {
            if (node.point.x() > rect.xmax()) {
                range(rect, points, node.left);
            }
            else if (node.point.x() < rect.xmin()) {
                range(rect, points, node.right);
            }
            else {
                range(rect, points, node.left);
                range(rect, points, node.right);
            }
        }
        else {
            if (node.point.y() > rect.ymax()) {
                range(rect, points, node.left);
            }
            else if (node.point.y() < rect.ymin()) {
                range(rect, points, node.right);
            }
            else {
                range(rect, points, node.left);
                range(rect, points, node.right);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("point is null");
        }
        distance = Double.MAX_VALUE;
        nearest_point = null;
        nearest(p, root);
        return nearest_point;
    }

    private void nearest(Point2D p, Node node) {
        if (node == null) return;
        if (node.point.distanceSquaredTo(p) < distance) {
            distance = node.point.distanceSquaredTo(p);
            nearest_point = node.point;
        }
        boolean searchLeft = false;
        boolean searchRight = false;
        if (node.left != null && node.leftRect().distanceSquaredTo(p) < distance) searchLeft = true;
        if (node.right != null && node.rightRect().distanceSquaredTo(p) < distance)
            searchRight = true;
        if (searchLeft && searchRight) {
            if (node.isVertical()) {
                if (node.point.x() < p.x()) {
                    // search right subtree
                    nearest(p, node.right);
                    // then search left subtree if needed
                    if (distance > node.leftRect().distanceSquaredTo(p))
                        nearest(p, node.left);
                }
                else {
                    // search left subtree
                    nearest(p, node.left);
                    if (distance > node.rightRect().distanceSquaredTo(p))
                        nearest(p, node.right);
                }
            }
            else {
                if (node.point.y() < p.y()) {
                    // search right subtree
                    nearest(p, node.right);
                    if (distance > node.leftRect().distanceSquaredTo(p))
                        nearest(p, node.left);
                }
                else {
                    // search left subtree
                    nearest(p, node.left);
                    if (distance > node.rightRect().distanceSquaredTo(p))
                        nearest(p, node.right);
                }
            }
        }
        else if (searchLeft) {
            nearest(p, node.left);
        }
        else if (searchRight) {
            nearest(p, node.right);
        }
    }

    public static void main(String[] args) {
        KdTree mytree = new KdTree();
        mytree.insert(new Point2D(0.7, 0.2));
        mytree.insert(new Point2D(0.5, 0.4));
        mytree.insert(new Point2D(0.2, 0.3));
        mytree.insert(new Point2D(0.4, 0.7));
        mytree.insert(new Point2D(0.9, 0.6));

        mytree.draw();
        RectHV searchRect = new RectHV(0.1, 0.1, 0.45, 0.45);
        StdDraw.setPenColor(StdDraw.GREEN);
        searchRect.draw();
        for (Point2D p : mytree.range(searchRect)) {
            StdOut.printf("x: %.2f, y: %.2f \n", p.x(), p.y());
        }
        Point2D searchPoint = new Point2D(0.7, 0.5);
        StdDraw.setPenColor(StdDraw.GREEN);
        searchPoint.draw();
        Point2D resPoint = mytree.nearest(searchPoint);
        StdOut.printf("x: %.2f, y: %.2f", resPoint.x(), resPoint.y());
    }
}
