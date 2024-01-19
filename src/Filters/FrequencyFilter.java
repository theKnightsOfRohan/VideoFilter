package Filters;

import Interfaces.PixelFilter;
import core.DImage;
import java.util.Arrays;

public class FrequencyFilter implements PixelFilter {
    private int segmentAmt, segmentSize;

    public FrequencyFilter() {
        segmentAmt = 4;
        segmentSize = 255 / (segmentAmt - 1);
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        short[] freqs = getFrequencies(grid);

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] = applyFreqs(grid[r][c], freqs);
            }
        }

        img.setPixels(grid);
        return img;
    }

    public short applyFreqs(short color, short[] freqs) {
        int segment = color / segmentSize;

        return freqs[segment];
    }

    public short[] getFrequencies(short[][] grid) {
        int[] freqs = new int[segmentAmt];
        int[] freqCounts = new int[segmentAmt];

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                freqs[grid[r][c] / segmentSize] += grid[r][c];
                freqCounts[grid[r][c] / segmentSize]++;
            }
        }

        for (int i = 0; i < freqs.length; i++) {
            freqs[i] /= freqCounts[i];
        }

        return castToShort(freqs);
    }

    private short[] castToShort(int[] arr) {
        short[] shortArr = new short[arr.length];

        for (int i = 0; i < arr.length; i++) {
            shortArr[i] = (short) arr[i];
        }

        return shortArr;
    }
}
