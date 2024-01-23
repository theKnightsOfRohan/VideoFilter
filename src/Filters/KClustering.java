package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

public class KClustering implements PixelFilter {
    int cycles;
    double marginOfChange;
    int clusterAmt;

    public KClustering() {
        cycles = 50;
        marginOfChange = 0.1;
        clusterAmt = 100;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] reds = img.getRedChannel();
        short[][] greens = img.getGreenChannel();
        short[][] blues = img.getBlueChannel();

        Point[] points = convertToPoints(reds, greens, blues);

        ClusterCenter[] centers = initClusterCenters(clusterAmt);

        for (int i = 0; i < cycles; i++) {
            for (ClusterCenter c : centers) {
                c.clearPoints();
            }

            for (Point p : points) {
                p.assignToCenter(centers);
            }

            for (ClusterCenter c : centers) {
                c.recalculateCenter();
            }
        }

        roundPoints(reds, greens, blues, centers);

        img.setColorChannels(reds, greens, blues);
        return img;
    }

    public Point[] convertToPoints(short[][] reds, short[][] greens, short[][] blues) {
        ArrayList<Point> points = new ArrayList<>();

        for (int r = 0; r < reds.length; r++) {
            for (int c = 0; c < reds[0].length; c++) {
                points.add(new Point(reds[r][c], greens[r][c], blues[r][c], r, c));
            }
        }

        return points.toArray(new Point[0]);
    }

    public ClusterCenter[] initClusterCenters(int amt) {
        ClusterCenter[] centers = new ClusterCenter[amt];
        for (int i = 0; i < amt; i++) {
            centers[i] = new ClusterCenter();
        }
        return centers;
    }

    public void roundPoints(short[][] reds, short[][] greens, short[][] blues, ClusterCenter[] centers) {
        for (ClusterCenter c : centers) {
            for (Point p : c.closestPoints) {
                reds[p.row][p.col] = (short) Math.round(c.red);
                greens[p.row][p.col] = (short) Math.round(c.green);
                blues[p.row][p.col] = (short) Math.round(c.blue);
            }
        }
    }
}

class Point {
    int red, green, blue;
    int row, col;

    public Point(int red, int green, int blue, int row, int col) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.row = row;
        this.col = col;
    }

    public void assignToCenter(ClusterCenter[] centers) {
        ClusterCenter closestCenter = centers[0];
        double closestDistance = distanceToCenter(closestCenter);

        for (int i = 1; i < centers.length; i++) {
            double distance = distanceToCenter(centers[i]);
            if (distance < closestDistance) {
                closestCenter = centers[i];
                closestDistance = distance;
            }
        }

        closestCenter.closestPoints.add(this);
    }

    public double distanceToCenter(ClusterCenter center) {
        double rDistSq = Math.pow(red - center.red, 2);
        double gDistSq = Math.pow(green - center.green, 2);
        double bDistSq = Math.pow(blue - center.blue, 2);

        return Math.sqrt(rDistSq + gDistSq + bDistSq);
    }
}

class ClusterCenter {
    double red, green, blue;

    ArrayList<Point> closestPoints;

    public ClusterCenter() {
        this.red = Math.random() * 256;
        this.green = Math.random() * 256;
        this.blue = Math.random() * 256;
        this.closestPoints = new ArrayList<>();
    }

    public void clearPoints() {
        closestPoints.clear();
    }

    public void recalculateCenter() {
        red = 0;
        green = 0;
        blue = 0;
        for (Point p : closestPoints) {
            red += p.red;
            green += p.green;
            blue += p.blue;
        }

        // Expected is that the only arithmetic exception is dividing by 0
        try {
            red /= closestPoints.size();
            green /= closestPoints.size();
            blue /= closestPoints.size();
        } catch (ArithmeticException e) {
            red = Math.random() * 256;
            green = Math.random() * 256;
            blue = Math.random() * 256;
        }
    }

    public String toString() {
        return "Points: " + closestPoints.size() + " Red: " + red + " Green: " + green + " Blue: " + blue;
    }
}
