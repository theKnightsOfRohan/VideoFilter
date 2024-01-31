package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class SobelEdges implements PixelFilter {
    short[][] sobelvertiLine, sobelhoriLine, skeletizerFirst, skeletizerFirstBody, skeletizerSecond,
            skeletizerSecondBody;

    public SobelEdges() {
        sobelvertiLine = new short[][] { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
        sobelhoriLine = new short[][] { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

        skeletizerFirst = new short[][] { { 0, 0, 0 }, { -1, 255, -1 }, { 255, 255, 255 } };
        skeletizerSecond = new short[][] { { -1, 0, 0 }, { 255, 255, 0 }, { -1, 255, -1 } };
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        short[][] newGrid = new short[grid.length][grid[0].length];
        copyGrid(grid, newGrid);

        int kernelSize = sobelvertiLine.length;

        for (int r = 0; r < grid.length - (kernelSize - 1); r++) {
            for (int c = 0; c < grid[0].length - (kernelSize - 1); c++) {
                int x = getConvoOutput(grid, sobelhoriLine, r, c);
                int y = getConvoOutput(grid, sobelvertiLine, r, c);

                int sum = (int) Math.sqrt((x * x) + (y * y));

                if (sum < 255 / 2)
                    sum = 0;
                else if (sum >= 255 / 2)
                    sum = 255;

                newGrid[r + 1][c + 1] = (short) sum;
            }
        }

        printGrid(newGrid);

        img.setPixels(newGrid);
        return img;
    }

    private short getConvoOutput(short[][] grid, short[][] kernel, int r, int c) {
        int sum = 0;

        for (int kr = 0; kr < kernel.length; kr++) {
            for (int kc = 0; kc < kernel[0].length; kc++) {
                sum += grid[r + kr][c + kc] * kernel[kr][kc];
            }
        }

        short result;

        int kernelSum = getKernelSum(kernel);

        try {
            result = (short) (sum / kernelSum);
        } catch (ArithmeticException e) {
            result = (short) (sum);
        }

        if (result < 0)
            result = 0;
        else if (result > 255)
            result = 255;

        return result;
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

    private void rotateNinety(short[][] grid) {
        short[][] newGrid = new short[grid.length][grid[0].length];

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid.length; c++)
                newGrid[r][c] = grid[c][r];
        }

        grid = newGrid;
    }

    private void printGrid(short[][] grid) {
        for (short[] row : grid) {
            for (short col : row) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
    }
}
