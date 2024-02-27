/*
 * Decompiled with CFR 0.152.
 */
package com.roguewave.blend.border.v2_0;

import com.roguewave.blend.border.v2_0.Border;
import com.roguewave.blend.border.v2_0.RoundedBorder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;

public class ThreeDBorder
        extends RoundedBorder {
    private Color m_clrShadow = SystemColor.controlShadow;
    private Color m_clrReflect = SystemColor.controlLtHighlight;
    private boolean m_bRaised = true;

    public ThreeDBorder() {
    }

    public ThreeDBorder(boolean bl) {
        this.setRaised(bl);
    }

    public Color getShadowColor() {
        return this.m_clrShadow;
    }

    public void setShadowColor(Color color) {
        this.m_clrShadow = color;
    }

    public Color getReflectionColor() {
        return this.m_clrReflect;
    }

    public void setReflectionColor(Color color) {
        this.m_clrReflect = color;
    }

    public boolean isRaised() {
        return this.m_bRaised;
    }

    public void setRaised(boolean bl) {
        this.m_bRaised = bl;
    }

    public Insets getInsets(Rectangle rectangle) {
        int n = this.getArcLength(rectangle.width - this.getThickness() - rectangle.x) / 2;
        int n2 = this.getArcLength(rectangle.height - this.getThickness() - rectangle.y) / 2;
        int n3 = Math.min(n, n2);
        int n4 = (int) ((double) n3 / Math.sqrt(2.0));
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        if (this.m_bRaised) {
            n5 = n2 - n4 + 1;
            n6 = n5 - 1 + this.getThickness();
            n7 = n - n4 + 1;
            n8 = n7 - 1 + this.getThickness();
        } else {
            n6 = n2 - n4 + 1;
            n5 = n6 - 1 + this.getThickness();
            n8 = n - n4 + 1;
            n7 = n8 - 1 + this.getThickness();
        }
        return new Insets(n5, n7, n6, n8);
    }

    public void paint(Graphics graphics, Rectangle rectangle) {
        Color color = graphics.getColor();
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        while (n7 < this.getThickness()) {
            n6 = rectangle.height - (this.getThickness() - n7);
            n5 = rectangle.width - (this.getThickness() - n7);
            n = rectangle.x + n7;
            n2 = rectangle.y + n7;
            n3 = rectangle.x + n5;
            n4 = rectangle.y + n6;
            if (this.m_bRaised) {
                if (n7 == this.getThickness() - 1) {
                    graphics.setColor(this.m_clrReflect);
                    int n8 = rectangle.x;
                    int n9 = rectangle.y;
                    int n10 = n + rectangle.width - this.getThickness() * 2;
                    int n11 = n2 + rectangle.height - this.getThickness() * 2;
                    this.drawFromLightSource(graphics, n8, n9, n10, n11);
                }
            } else {
                graphics.setColor(this.m_clrShadow);
                this.drawFromLightSource(graphics, n, n2, rectangle.x + rectangle.width - 2, rectangle.y + rectangle.height - 2);
            }
            if (this.m_bRaised) {
                graphics.setColor(this.m_clrShadow);
                this.drawShadowedEdges(graphics, n, n2, n3, n4);
            } else if (n7 == this.getThickness() - 1) {
                graphics.setColor(this.m_clrReflect);
                this.drawShadowedEdges(graphics, rectangle.x, rectangle.y, n3, n4);
            }
            ++n7;
        }
        if (this.m_bRaised) {
            n = rectangle.x + 1;
            n2 = rectangle.y + 1;
            n5 = rectangle.width - this.getThickness() - 1;
            n6 = rectangle.height - this.getThickness() - 1;
        } else {
            n = rectangle.x + this.getThickness();
            n2 = rectangle.y + this.getThickness();
            n5 = rectangle.width - this.getThickness() - 1;
            n6 = rectangle.height - this.getThickness() - 1;
        }
        this.fillInterior(graphics, n, n2, n5, n6);
        graphics.setColor(color);
    }

    protected void drawFromLightSource(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5 = this.getArcLength(n3 - n);
        int n6 = this.getArcLength(n4 - n2);
        int n7 = n5 / 2;
        int n8 = n6 / 2;
        graphics.drawArc(n, n4 - n6, n5, n6, -180, 45);
        graphics.drawLine(n, n4 - n8, n, n2 + n8);
        graphics.drawArc(n, n2, n5, n6, 180, -90);
        graphics.drawLine(n + n7, n2, n3 - n7, n2);
        graphics.drawArc(n3 - n5, n2, n5, n6, 90, -45);
    }

    protected void drawShadowedEdges(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5 = this.getArcLength(n3 - n);
        int n6 = this.getArcLength(n4 - n2);
        int n7 = n5 / 2;
        int n8 = n6 / 2;
        graphics.drawArc(n3 - n5, n2, n5, n6, 45, -45);
        graphics.drawLine(n3, n2 + n8, n3, n4 - n8);
        graphics.drawArc(n3 - n5, n4 - n6, n5, n6, 0, -90);
        graphics.drawLine(n3 - n7, n4, n + n7, n4);
        graphics.drawArc(n, n4 - n6, n5, n6, -90, -45);
    }

    private void fillInterior(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5 = this.getArcLength(n3);
        int n6 = this.getArcLength(n4);
        graphics.setColor(this.getBackground());
        graphics.fillRoundRect(n, n2, n3, n4, n5, n6);
    }

    public void copyFrom(Border border) {
        if (border instanceof ThreeDBorder) {
            this.m_clrShadow = ((ThreeDBorder) border).m_clrShadow;
            this.m_clrReflect = ((ThreeDBorder) border).m_clrReflect;
            this.m_bRaised = ((ThreeDBorder) border).m_bRaised;
        }
        super.copyFrom(border);
    }
}

