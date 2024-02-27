/*
 * Decompiled with CFR 0.152.
 */
package com.sun.java.swing;

import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

public class GrayFilter
        extends RGBImageFilter {
    private boolean brighter;
    private int percent;

    public GrayFilter(boolean b, int p) {
        this.brighter = b;
        this.percent = p;
        this.canFilterIndexColorModel = true;
    }

    public static Image createDisabledImage(Image i) {
        GrayFilter filter = new GrayFilter(true, 50);
        FilteredImageSource prod = new FilteredImageSource(i.getSource(), filter);
        Image grayImage = Toolkit.getDefaultToolkit().createImage(prod);
        return grayImage;
    }

    public int filterRGB(int x, int y, int rgb) {
        int gray = (int) ((0.3 * (double) (rgb >> 16 & 0xFF) + 0.59 * (double) (rgb >> 8 & 0xFF) + 0.11 * (double) (rgb & 0xFF)) / 3.0);
        gray = this.brighter ? 255 - (255 - gray) * (100 - this.percent) / 100 : gray * (100 - this.percent) / 100;
        if (gray < 0) {
            gray = 0;
        }
        if (gray > 255) {
            gray = 255;
        }
        return rgb & 0xFF000000 | gray << 16 | gray << 8 | gray << 0;
    }
}

