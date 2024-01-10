package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class LightenFilter implements PixelFilter {
    private int lightenAmount;

    public LightenFilter() {
        lightenAmount = 50;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] += lightenAmount;
                if (grid[r][c] > 255) {
                    grid[r][c] = 255;
                }
            }
        }

        img.setPixels(grid);
        return img;
    }
}
