package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BetterDownsampler implements PixelFilter {
    private int relativeSize;

    public BetterDownsampler() {
        relativeSize = 2;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] originalGrid = img.getBWPixelGrid();
        short[][] result = new short[originalGrid.length / relativeSize][originalGrid[0].length / relativeSize];

        for (int r = 0; r < result.length; r++) {
            for (int c = 0; c < result[r].length; c++) {
                result[r][c] = getAverage(originalGrid, r * relativeSize, c * relativeSize);
            }
        }

        img.setPixels(result);
        return img;
    }

    private short getAverage(short[][] grid, int startRow, int startCol) {
        int sum = 0;
        int count = 0;

        for (int r = startRow; r < startRow + relativeSize; r++) {
            for (int c = startCol; c < startCol + relativeSize; c++) {
                sum += grid[r][c];
                count++;
            }
        }

        return (short) (sum / count);
    }
}
