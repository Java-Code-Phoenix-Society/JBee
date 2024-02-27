/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import java.awt.*;

public class DoubleBufferPanel
        extends Panel {
    Image offscreen;
    private boolean hasframe = true;

    public boolean getHasframe() {
        return this.hasframe;
    }

    public void setHasframe(boolean bl) {
        this.hasframe = bl;
    }

    public void invalidate() {
        super.invalidate();
        if (this.offscreen != null) {
            this.offscreen.flush();
        }
        this.offscreen = null;
    }

    public void paint(Graphics graphics) {
        Graphics graphics2;
        if (this.offscreen == null && (this.getSize().width == 0 || this.getSize().height == 0)) {
            return;
        }
        if (this.offscreen == null) {
            this.offscreen = this.createImage(this.getSize().width, this.getSize().height);
            if (this.offscreen == null) {
                return;
            }
        }
        if ((graphics2 = this.offscreen.getGraphics()) == null) {
            return;
        }
        graphics2.setClip(0, 0, this.getSize().width, this.getSize().height);
        graphics2.setColor(this.getBackground());
        graphics2.fillRect(0, 0, this.getSize().width, this.getSize().height);
        super.paint(graphics2);
        if (this.hasframe) {
            graphics2.setColor(this.getBackground());
            graphics2.draw3DRect(0, 0, this.getSize().width - 1, this.getSize().height - 1, true);
        }
        graphics.drawImage(this.offscreen, 0, 0, null);
        graphics2.dispose();
    }

    public void update(Graphics graphics) {
        this.paint(graphics);
    }
}

