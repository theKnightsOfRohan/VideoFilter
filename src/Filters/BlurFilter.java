package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BlurFilter implements PixelFilter {
    private int blurAmt;

    public BlurFilter() {
        blurAmt = 5;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        for (int r = blurAmt; r < grid.length - blurAmt; r++) {
            for (int c = blurAmt; c < grid[0].length - blurAmt; c++) {

                int sum = 0;
                int count = 0;

                for (int i = -blurAmt; i <= blurAmt; i++) {
                    for (int j = -blurAmt; j <= blurAmt; j++) {
                        if (r + i >= 0 && r + i < grid.length && c + j >= 0 && c + j < grid[0].length) {
                            sum += grid[r + i][c + j];
                            count++;
                        }
                    }
                }

                grid[r][c] = (short) (sum / count);
            }
        }

        img.setPixels(grid);
        return img;
    }
}
