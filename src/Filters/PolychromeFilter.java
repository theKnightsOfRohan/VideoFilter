package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class PolychromeFilter implements PixelFilter {
    private short chromacity;
    private int chromacitySegment;

    public PolychromeFilter() {
        chromacity = 4;
        chromacitySegment = 256 / chromacity;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] = (short) (grid[r][c] - (grid[r][c] % chromacitySegment));
            }
        }

        img.setPixels(grid);
        return img;
    }
}
