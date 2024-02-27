/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import java.awt.*;

public class BorderPanel
        extends Panel {
    private boolean hasframe = true;

    public BorderPanel() {
    }

    public BorderPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    public boolean getHasframe() {
        return this.hasframe;
    }

    public void setHasframe(boolean bl) {
        this.hasframe = bl;
    }

    public void invalidate() {
        super.invalidate();
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (this.hasframe) {
            graphics.setColor(Color.black);
            graphics.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
        }
    }

    public void update(Graphics graphics) {
        this.paint(graphics);
    }
}

