/*
 * Decompiled with CFR 0.152.
 */
package com.roguewave.blend.border.v2_0;

import com.roguewave.blend.border.v2_0.Border;

public abstract class RoundedBorder
        extends Border {
    int m_nPercentRounded;

    public int getPercentRounded() {
        return this.m_nPercentRounded;
    }

    public void setPercentRounded(int n) {
        this.m_nPercentRounded = n;
    }

    public int getArcLength(int n) {
        return n * this.m_nPercentRounded / 100;
    }

    public void copyFrom(Border border) {
        if (border instanceof RoundedBorder) {
            this.m_nPercentRounded = ((RoundedBorder) border).m_nPercentRounded;
        }
        super.copyFrom(border);
    }
}

