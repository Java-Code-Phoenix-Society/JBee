/*
 * Decompiled with CFR 0.152.
 */
package com.sun.java.swing;

import java.awt.*;
import java.io.PrintStream;
import java.io.Serializable;

public class BoxLayout
        implements LayoutManager2,
        Serializable {
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    private int axis;
    private Container target;
    private transient SizeRequirements[] xChildren;
    private transient SizeRequirements[] yChildren;
    private transient SizeRequirements xTotal;
    private transient SizeRequirements yTotal;
    private transient PrintStream dbg;

    public BoxLayout(Container target, int axis) {
        if (axis != 0 && axis != 1) {
            throw new AWTError("Invalid axis");
        }
        this.axis = axis;
        this.target = target;
    }

    BoxLayout(Container target, int axis, PrintStream dbg) {
        this(target, axis);
        this.dbg = dbg;
    }

    public void addLayoutComponent(Component comp, Object constraints) {
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    void checkContainer(Container target) {
        if (this.target != target) {
            throw new AWTError("BoxLayout can't be shared");
        }
    }

    void checkRequests() {
        if (this.xChildren == null || this.yChildren == null) {
            int n = this.target.getComponentCount();
            this.xChildren = new SizeRequirements[n];
            this.yChildren = new SizeRequirements[n];
            int i = 0;
            while (i < n) {
                Component c = this.target.getComponent(i);
                Dimension min = c.getMinimumSize();
                Dimension typ = c.getPreferredSize();
                Dimension max = c.getMaximumSize();
                this.xChildren[i] = new SizeRequirements(min.width, typ.width, max.width, c.getAlignmentX());
                this.yChildren[i] = new SizeRequirements(min.height, typ.height, max.height, c.getAlignmentY());
                ++i;
            }
            if (this.axis == 0) {
                this.xTotal = SizeRequirements.getTiledSizeRequirements(this.xChildren);
                this.yTotal = SizeRequirements.getAlignedSizeRequirements(this.yChildren);
            } else {
                this.xTotal = SizeRequirements.getAlignedSizeRequirements(this.xChildren);
                this.yTotal = SizeRequirements.getTiledSizeRequirements(this.yChildren);
            }
        }
    }

    public float getLayoutAlignmentX(Container target) {
        this.checkContainer(target);
        this.checkRequests();
        return this.xTotal.alignment;
    }

    public float getLayoutAlignmentY(Container target) {
        this.checkContainer(target);
        this.checkRequests();
        return this.yTotal.alignment;
    }

    public void invalidateLayout(Container target) {
        this.checkContainer(target);
        this.xChildren = null;
        this.yChildren = null;
        this.xTotal = null;
        this.yTotal = null;
    }

    public void layoutContainer(Container target) {
        Component c;
        this.checkContainer(target);
        this.checkRequests();
        int nChildren = target.getComponentCount();
        int[] xOffsets = new int[nChildren];
        int[] xSpans = new int[nChildren];
        int[] yOffsets = new int[nChildren];
        int[] ySpans = new int[nChildren];
        Dimension alloc = target.getSize();
        Insets in = target.getInsets();
        alloc.width -= in.left + in.right;
        alloc.height -= in.top + in.bottom;
        if (this.axis == 0) {
            SizeRequirements.calculateTiledPositions(alloc.width, this.xTotal, this.xChildren, xOffsets, xSpans);
            SizeRequirements.calculateAlignedPositions(alloc.height, this.yTotal, this.yChildren, yOffsets, ySpans);
        } else {
            SizeRequirements.calculateAlignedPositions(alloc.width, this.xTotal, this.xChildren, xOffsets, xSpans);
            SizeRequirements.calculateTiledPositions(alloc.height, this.yTotal, this.yChildren, yOffsets, ySpans);
        }
        int i = 0;
        while (i < nChildren) {
            c = target.getComponent(i);
            c.setBounds((int) Math.min((long) in.left + (long) xOffsets[i], Integer.MAX_VALUE), (int) Math.min((long) in.top + (long) yOffsets[i], Integer.MAX_VALUE), xSpans[i], ySpans[i]);
            ++i;
        }
        if (this.dbg != null) {
            i = 0;
            while (i < nChildren) {
                c = target.getComponent(i);
                this.dbg.println(c.toString());
                this.dbg.println("X: " + this.xChildren[i]);
                this.dbg.println("Y: " + this.yChildren[i]);
                ++i;
            }
        }
    }

    public Dimension maximumLayoutSize(Container target) {
        this.checkContainer(target);
        this.checkRequests();
        Dimension size = new Dimension(this.xTotal.maximum, this.yTotal.maximum);
        Insets insets = target.getInsets();
        size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
        size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
        return size;
    }

    public Dimension minimumLayoutSize(Container target) {
        this.checkContainer(target);
        this.checkRequests();
        Dimension size = new Dimension(this.xTotal.minimum, this.yTotal.minimum);
        Insets insets = target.getInsets();
        size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
        size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
        return size;
    }

    public Dimension preferredLayoutSize(Container target) {
        this.checkContainer(target);
        this.checkRequests();
        Dimension size = new Dimension(this.xTotal.preferred, this.yTotal.preferred);
        Insets insets = target.getInsets();
        size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
        size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
        return size;
    }

    public void removeLayoutComponent(Component comp) {
    }
}

