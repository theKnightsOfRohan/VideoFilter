package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BoxBlur implements PixelFilter {
    public BoxBlur() {
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        short[][] newGrid = new short[grid.length][grid[0].length];
        copyGrid(grid, newGrid);

        short[][] kernel = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };

        int kernelSize = 3;
        int kernelSum = getKernelSum(kernel);

        for (int r = 0; r < grid.length - 2; r++) {
            for (int c = 0; c < grid[0].length - 2; c++) {
                int sum = 0;
                for (int kr = 0; kr < kernelSize; kr++) {
                    for (int kc = 0; kc < kernelSize; kc++) {
                        sum += grid[r + kr][c + kc] * kernel[kr][kc];
                    }
                }

                newGrid[r + 1][c + 1] = (short) (sum / kernelSum);
            }
        }

        img.setPixels(newGrid);
        return img;
    }

    private void copyGrid(short[][] grid, short[][] newGrid) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++)
                newGrid[r][c] = grid[r][c];
        }
    }

    private int getKernelSum(short[][] kernel) {
        int sum = 0;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                sum += kernel[i][j];
            }
        }
        return sum;
    }
}
