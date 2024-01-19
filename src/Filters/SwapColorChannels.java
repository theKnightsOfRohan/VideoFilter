package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class SwapColorChannels implements PixelFilter {
    public SwapColorChannels() {
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] reds = img.getRedChannel();
        short[][] greens = img.getGreenChannel();
        short[][] blues = img.getBlueChannel();

        img.setColorChannels(blues, greens, reds);
        return img;
    }
}
