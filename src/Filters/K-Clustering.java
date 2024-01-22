package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

public class PolychromeFilter implements PixelFilter {
    int repetitions;
    double marginOfChange;
    int clusterAmt;

    public PolychromeFilter() {
        repetitions = 5;
        marginOfChange = 0.1;
        clusterAmt = 5;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] reds = img.getRedChannel();
        short[][] greens = img.getGreenChannel();
        short[][] blues = img.getBlueChannel();

        Point[] points = convertToPoints(grid);
        ClusterCenter[] centers = initClusterCenters(clusterAmt);

        for (int i = 0; i < repetitions; i++) {
            // Clear the points from the cluster centers
            // Assign each point to a cluster center
            // Recalculate the cluster centers
        }

        img.setPixels(grid);
        return img;
    }

    public Point[] convertToPoints(short[][] reds, short[][] greens, short[][] blues) {
        // Just construct with the grid values
        return null;
    }

    public ClusterCenter[] initClusterCenters(int amt) {
        // Construct amt cluster centers
        return null;
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
        // use 3d distance formula
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

class ClusterCenter {
    int red, green, blue;

    ArrayList<Point> closestPoints;

    public ClusterCenter() {
        this.red = (int) (Math.random() * 256);
        this.green = (int) (Math.random() * 256);
        this.blue = (int) (Math.random() * 256);
    }

    public void clearPoints() {
        // Set each to null
    }

    public void recalculateCenter() {
        // Average the points
    }
}
