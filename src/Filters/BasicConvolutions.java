package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BasicConvolutions implements PixelFilter {
    short[][] basic, gaussian, largeGaussian, sharpen, horiLine, vertiLine, upDiagLine, downDiagLine,
            prewittEdgeDetection;

    public BasicConvolutions() {
        basic = new short[][] { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };

        gaussian = new short[][] { { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 } };

        largeGaussian = new short[][] { { 0, 0, 0, 5, 0, 0, 0 },
                { 0, 5, 18, 32, 18, 5, 0 },
                { 0, 18, 64, 100, 64, 18, 0 },
                { 5, 32, 100, 100, 100, 32, 5 },
                { 0, 18, 64, 100, 64, 18, 0 },
                { 0, 5, 18, 32, 18, 5, 0 },
                { 0, 0, 0, 5, 0, 0, 0 } };

        sharpen = new short[][] { { 0, -1, 0 }, { -1, 5, -1 }, { 0, -1, 0 } };

        horiLine = new short[][] { { -1, -1, -1 }, { 2, 2, 2 }, { -1, -1, -1 } };

        vertiLine = new short[][] { { -1, 2, -1 }, { -1, 2, -1 }, { -1, 2, -1 } };

        upDiagLine = new short[][] { { -1, -1, 2 }, { -1, 2, -1 }, { 2, -1, -1 } };

        downDiagLine = new short[][] { { 2, -1, -1 }, { -1, 2, -1 }, { -1, -1, 2 } };

        prewittEdgeDetection = new short[][] { { -1, -1, -1 }, { -1, 8, -1 }, { -1, -1, -1 } };
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        short[][] newGrid = new short[grid.length][grid[0].length];
        copyGrid(grid, newGrid);

        short[][] kernel = prewittEdgeDetection;

        int kernelSize = kernel.length;
        int kernelSum = getKernelSum(kernel);

        for (int r = 0; r < grid.length - (kernelSize - 1); r++) {
            for (int c = 0; c < grid[0].length - (kernelSize - 1); c++) {
                int sum = 0;
                for (int kr = 0; kr < kernelSize; kr++) {
                    for (int kc = 0; kc < kernelSize; kc++) {
                        sum += grid[r + kr][c + kc] * kernel[kr][kc];
                    }
                }

                int centerRow = r + (kernelSize / 2);
                int centerCol = c + (kernelSize / 2);

                try {
                    newGrid[centerRow][centerCol] = (short) (sum / kernelSum);
                } catch (ArithmeticException e) {
                    newGrid[centerRow][centerCol] = (short) (sum);
                }

                if (newGrid[centerRow][centerCol] < 0)
                    newGrid[centerRow][centerCol] = 0;
                else if (newGrid[centerRow][centerCol] > 255)
                    newGrid[centerRow][centerCol] = 255;
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
