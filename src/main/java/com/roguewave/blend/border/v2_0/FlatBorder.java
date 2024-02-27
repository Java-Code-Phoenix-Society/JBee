/*
 * Decompiled with CFR 0.152.
 */
package com.roguewave.blend.border.v2_0;

import com.roguewave.blend.border.v2_0.RoundedBorder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

public class FlatBorder
        extends RoundedBorder {
    public Insets getInsets(Rectangle rectangle) {
        int n;
        int n2;
        int n3 = this.getArcLength(rectangle.width - this.getThickness() - rectangle.x) / 2;
        int n4 = this.getArcLength(rectangle.height - this.getThickness() - rectangle.y) / 2;
        int n5 = Math.min(n3, n4);
        int n6 = (int) ((double) n5 / Math.sqrt(2.0));
        int n7 = n2 = n4 - n6 + this.getThickness();
        int n8 = n = n3 - n6 + this.getThickness();
        return new Insets(n2, n, n7, n8);
    }

    public void paint(Graphics graphics, Rectangle rectangle) {
        Color color = graphics.getColor();
        graphics.setColor(this.getBorderColor());
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        while (n5 < this.getThickness()) {
            n = rectangle.width - 2 * n5 - 1;
            n2 = rectangle.height - 2 * n5 - 1;
            n4 = rectangle.x + n5;
            n3 = rectangle.y + n5;
            graphics.drawRoundRect(n4, n3, n, n2, this.getArcLength(n), this.getArcLength(n2));
            ++n5;
        }
        graphics.setColor(this.getBackground());
        graphics.fillRoundRect(n4 + 1, n3 + 1, n - 1, n2 - 1, this.getArcLength(n - 2), this.getArcLength(n2 - 2));
        graphics.setColor(color);
    }
}

