package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class TileFilter implements PixelFilter {
    int n;
    int m;

    public TileFilter() {
        n = 2;
        m = 3;
    }

    @Override
    public DImage processImage(DImage img) {
        int[][] rgbs = img.getColorPixelGrid();

        int[][] newGrid = new int[rgbs.length * n][rgbs[0].length * m];

        for (int r = 0; r < newGrid.length; r += rgbs.length) {
            for (int c = 0; c < newGrid[0].length; c += rgbs[0].length) {
                copyGrid(rgbs, newGrid, r, c);
            }
        }

        img.setPixels(newGrid);
        return img;
    }

    private void copyGrid(int[][] imgToCopy, int[][] newGrid, int rStart, int cStart) {
        for (int r = rStart; r < rStart + imgToCopy.length; r++) {
            for (int c = cStart; c < cStart + imgToCopy[0].length; c++) {
                newGrid[r][c] = imgToCopy[r - rStart][c - cStart];
            }
        }
    }
}
