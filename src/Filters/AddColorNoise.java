package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class AddColorNoise implements PixelFilter {
    double noiseProbability;
    double noiseMagnitude;

    public AddColorNoise() {
        noiseProbability = 0.5;
        noiseMagnitude = 5;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] reds = img.getRedChannel();
        short[][] greens = img.getGreenChannel();
        short[][] blues = img.getBlueChannel();

        for (int row = 0; row < img.getHeight(); row++) {
            for (int col = 0; col < img.getWidth(); col++) {
                if (Math.random() < noiseProbability) {
                    reds[row][col] = (short) (reds[row][col] * (noiseMagnitude * (Math.random() - 0.5)));
                    greens[row][col] = (short) (greens[row][col] * (noiseMagnitude * (Math.random() - 0.5)));
                    blues[row][col] = (short) (blues[row][col] * (noiseMagnitude * (Math.random() - 0.5)));
                }
            }
        }

        img.setColorChannels(reds, greens, blues);
        return img;
    }
}
