package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BlindsFilter implements PixelFilter {
    private int width;

    public BlindsFilter() {
        width = 50;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        int counter = 1;

        for (int r = 0; r < grid.length; r++) {
            if (r % 50 == 0) {
                counter *= -1;
            }

            if (counter < 1) {
                for (int c = 0; c < grid[0].length; c++) {
                    grid[r][c] = 0;
                }
            }
        }

        img.setPixels(grid);
        return img;
    }
}
