/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import java.awt.*;

public class SplashWindow
        extends Window {
    Image splashIm;
    int w;
    int h;

    public SplashWindow(Frame frame, Image image, int n, int n2) {
        super(frame);
        this.splashIm = image;
        this.w = n;
        this.h = n2;
        ((Component) this).setSize(this.w, this.h);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rectangle = this.getBounds();
        ((Component) this).setLocation((dimension.width - rectangle.width) / 2, (dimension.height - rectangle.height) / 2);
        ((Component) this).setVisible(true);
    }

    public void paint(Graphics graphics) {
        if (this.splashIm != null) {
            graphics.drawImage(this.splashIm, 0, 0, this.w, this.h, this);
        }
    }
}

