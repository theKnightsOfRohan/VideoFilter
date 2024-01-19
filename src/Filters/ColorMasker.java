package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class ColorMasker implements PixelFilter {
    short redTarget, greenTarget, blueTarget;
    short margin;

    public ColorMasker() {
        redTarget = 100;
        greenTarget = 100;
        blueTarget = 100;
        margin = 50;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] reds = img.getRedChannel();
        short[][] greens = img.getGreenChannel();
        short[][] blues = img.getBlueChannel();

        for (int row = 0; row < img.getHeight(); row++) {
            for (int col = 0; col < img.getWidth(); col++) {
                if (getDistance(reds[row][col], greens[row][col], blues[row][col], redTarget, greenTarget,
                        blueTarget) > margin) {
                    reds[row][col] = 255;
                    greens[row][col] = 255;
                    blues[row][col] = 255;
                } else {
                    reds[row][col] = 0;
                    greens[row][col] = 0;
                    blues[row][col] = 0;
                }
            }
        }

        img.setColorChannels(reds, greens, blues);
        return img;
    }

    private double getDistance(short r1, short g1, short b1, short r2, short g2, short b2) {
        return Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
    }
}
