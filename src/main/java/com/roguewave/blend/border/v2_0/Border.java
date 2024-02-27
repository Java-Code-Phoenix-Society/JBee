/*
 * Decompiled with CFR 0.152.
 */
package com.roguewave.blend.border.v2_0;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.io.Serializable;

public abstract class Border
        implements Serializable {
    Color m_clrBackground = SystemColor.control;
    Color m_clrBorder = Color.black;
    int m_nThickness = 1;

    public Color getBackground() {
        return this.m_clrBackground;
    }

    public void setBackground(Color color) {
        this.m_clrBackground = color;
    }

    public int getThickness() {
        return this.m_nThickness;
    }

    public void setThickness(int n) {
        this.m_nThickness = n;
    }

    public Color getBorderColor() {
        return this.m_clrBorder;
    }

    public void setBorderColor(Color color) {
        this.m_clrBorder = color;
    }

    public abstract Insets getInsets(Rectangle var1);

    public abstract void paint(Graphics var1, Rectangle var2);

    public void copyFrom(Border border) {
        this.m_clrBackground = border.m_clrBackground;
        this.m_clrBorder = border.m_clrBorder;
        this.m_nThickness = border.m_nThickness;
    }
}

