package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class ColorHSVMasker implements PixelFilter {
    double hueTarget;
    int hueMargin;

    public ColorHSVMasker() {
        hueTarget = 100;
        hueMargin = 10;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] reds = img.getRedChannel();
        short[][] greens = img.getGreenChannel();
        short[][] blues = img.getBlueChannel();

        double[][][] hsv = calcHSVs(reds, greens, blues);
        short[][] mask = new short[hsv.length][hsv[0].length];

        applyMask(hsv, mask, hueTarget, hueMargin);

        img.setPixels(mask);
        return img;
    }

    public static void applyMask(double[][][] hsv, short[][] mask, double hueTarget, int hueMargin) {
        for (int r = 0; r < hsv.length; r++) {
            for (int c = 0; c < hsv[0].length; c++) {
                if (Math.abs(hsv[r][c][0] - hueTarget) < hueMargin) {
                    mask[r][c] = 255;
                } else {
                    mask[r][c] = 0;
                }
            }
        }
    }

    public static double[][][] calcHSVs(short[][] reds, short[][] greens, short[][] blues) {
        double[][][] hsvs = new double[reds.length][reds[0].length][3];
        for (int r = 0; r < hsvs.length; r++) {
            for (int c = 0; c < hsvs[0].length; c++) {
                hsvs[r][c] = calcHSV(reds[r][c], greens[r][c], blues[r][c]);
            }
        }

        return hsvs;
    }

    public static double[] calcHSV(short r, short g, short b) {
        double rNormalized = r / 255.0;
        double gNormalized = g / 255.0;
        double bNormalized = b / 255.0;

        double maxMag = Math.max(rNormalized, Math.max(gNormalized, bNormalized));
        double minMag = Math.min(rNormalized, Math.min(gNormalized, bNormalized));
        double delta = maxMag - minMag;

        double hue = 0;

        // Why can't I switch-case here ;-;
        if (maxMag == 0) {
            hue = 0;
        } else if (maxMag == rNormalized) {
            hue = ((gNormalized - bNormalized) / delta) % 6;
        } else if (maxMag == gNormalized) {
            hue = ((bNormalized - rNormalized) / delta) + 2;
        } else if (maxMag == bNormalized) {
            hue = ((rNormalized - gNormalized) / delta) + 4;
        } else {
            System.out.println("Error in calcHSV");
        }

        hue *= 60;

        double lightness = (maxMag + minMag) / 2;
        double saturation = 0;

        // More complicated version to maintain positive saturation
        if (delta != 0) {
            saturation = delta / (1 - Math.abs(2 * lightness - 1));
        }

        return new double[] { hue, saturation, lightness };
    }
}
