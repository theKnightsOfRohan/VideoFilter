package Filters;

import Interfaces.PixelFilter;
import core.DImage;
import java.util.Arrays;
import java.util.Objects;
import java.util.ArrayList;

public class BetterKClustering implements PixelFilter {
    int maxCycles;
    double marginOfChange;
    int clusterAmt;

    public BetterKClustering() {
        maxCycles = 500;
        marginOfChange = 0.001;
        clusterAmt = 256;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] reds = img.getRedChannel();
        short[][] greens = img.getGreenChannel();
        short[][] blues = img.getBlueChannel();

        Point[] points = grabPoints(reds, greens, blues);
        points = filterUniquePoints(points);

        ClusterCenter[] centers = initClusterCenters(clusterAmt);

        int cycles = 0;
        while (true) {
            for (ClusterCenter c : centers) {
                c.clearPoints();
            }

            for (Point p : points) {
                p.assignToCenter(centers);
            }

            double change = 0;
            for (ClusterCenter c : centers) {
                change += c.recalculateCenter();
            }

            cycles++;
            System.out.println("Cycle: " + cycles + " Change: " + change);

            if (change < marginOfChange) {
                break;
            }
        }

        roundPoints(reds, greens, blues, centers);

        System.out.println("===> Done");
        img.setColorChannels(reds, greens, blues);
        return img;
    }

    public Point[] grabPoints(short[][] reds, short[][] greens, short[][] blues) {
        System.out.println("===> Grabbing points");
        ArrayList<Point> points = new ArrayList<>();

        for (int r = 0; r < reds.length; r += 2) {
            for (int c = 0; c < reds[0].length; c += 2) {
                points.add(new Point(reds[r][c], greens[r][c], blues[r][c], r, c));
            }
        }

        return points.toArray(new Point[0]);
    }

    public Point[] filterUniquePoints(Point[] points) {
        System.out.println("===> Filtering unique points");
        return Arrays.stream(points).distinct().toArray(Point[]::new);
    }

    public ClusterCenter[] initClusterCenters(int amt) {
        System.out.println("===> Initializing cluster centers");

        ClusterCenter[] centers = new ClusterCenter[amt];
        for (int i = 0; i < amt; i++) {
            centers[i] = new ClusterCenter();
        }
        return centers;
    }

    public void roundPoints(short[][] reds, short[][] greens, short[][] blues, ClusterCenter[] centers) {
        System.out.println("===> Rounding points");
        Point p;
        ClusterCenter center;
        for (int r = 0; r < reds.length; r++) {
            for (int c = 0; c < reds[0].length; c++) {
                p = new Point(reds[r][c], greens[r][c], blues[r][c], r, c);
                center = p.assignToCenter(centers);
                reds[r][c] = (short) center.red;
                greens[r][c] = (short) center.green;
                blues[r][c] = (short) center.blue;
            }
        }
    }

    static class Point {
        short red, green, blue;
        int row, col;

        public Point(short red, short green, short blue, int row, int col) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.row = row;
            this.col = col;
        }

        public ClusterCenter assignToCenter(ClusterCenter[] centers) {
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
            return closestCenter;
        }

        public double distanceToCenter(ClusterCenter center) {
            double rDistSq = Math.pow(red - center.red, 2);
            double gDistSq = Math.pow(green - center.green, 2);
            double bDistSq = Math.pow(blue - center.blue, 2);

            return Math.sqrt(rDistSq + gDistSq + bDistSq);
        }

        @Override
        public boolean equals(Object o) {
            Point point;
            if (!(o instanceof Point))
                return false;
            else
                point = (Point) o;

            boolean eq = this == o || (this.red == point.red && this.green == point.green && this.blue == point.blue);

            return eq;
        }

        @Override
        public int hashCode() {
            return Objects.hash(red, green, blue);
        }
    }

    static class ClusterCenter {
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

        public double recalculateCenter() {
            double oldRed = red;
            double oldGreen = green;
            double oldBlue = blue;

            red = 0;
            green = 0;
            blue = 0;
            for (Point p : closestPoints) {
                red += p.red;
                green += p.green;
                blue += p.blue;
            }

            try {
                red /= closestPoints.size();
                green /= closestPoints.size();
                blue /= closestPoints.size();
                if (Double.isNaN(red) || Double.isNaN(green) || Double.isNaN(blue)) {
                    throw new ArithmeticException();
                }
            } catch (ArithmeticException e) {
                red = Math.random() * 256;
                green = Math.random() * 256;
                blue = Math.random() * 256;
            }

            // Return the distance between the old center and the new center
            return Math.sqrt(Math.pow(red - oldRed, 2) + Math.pow(green - oldGreen, 2) + Math.pow(blue - oldBlue, 2));
        }

        public String toString() {
            return "Points: " + closestPoints.size() + " Red: " + red + " Green: " + green + " Blue: " + blue;
        }
    }
}
