package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class PolychromeFilter implements PixelFilter {
    private short chromacity;
    private int chromacitySegment;

    public PolychromeFilter() {
        chromacity = 4;
        chromacitySegment = 256 / (chromacity - 1);
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] = getNewValue(grid[r][c]);
            }
        }

        img.setPixels(grid);
        return img;
    }

    public short getNewValue(short color) {
        short lower = (short) (color - (color % chromacitySegment));
        short upper = (short) (lower + chromacitySegment);

        if (color - lower < upper - color) {
            return lower;
        } else {
            return upper;
        }
    }
}
