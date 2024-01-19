package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class RemoveColorChannel implements PixelFilter {
    public RemoveColorChannel() {
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] reds = img.getRedChannel();
        short[][] greens = img.getGreenChannel();
        short[][] blues = img.getBlueChannel();

        for (int row = 0; row < img.getHeight(); row++) {
            for (int col = 0; col < img.getWidth(); col++) {
                reds[row][col] = 0;
            }
        }

        img.setColorChannels(reds, greens, blues);
        return img;
    }
}
