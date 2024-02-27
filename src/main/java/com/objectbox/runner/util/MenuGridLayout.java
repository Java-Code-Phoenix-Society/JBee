/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.util;

import java.awt.*;
import java.io.Serializable;

public class MenuGridLayout
        implements LayoutManager,
        Serializable {
    int hgap;
    int vgap;
    int rows;
    int cols;

    public MenuGridLayout() {
        this(1, 0, 0, 0);
    }

    public MenuGridLayout(int n, int n2) {
        this(n, n2, 0, 0);
    }

    public MenuGridLayout(int n, int n2, int n3, int n4) {
        if (n == 0 && n2 == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.rows = n;
        this.cols = n2;
        this.hgap = n3;
        this.vgap = n4;
    }

    public void addLayoutComponent(String string, Component component) {
    }

    public int getColumns() {
        return this.cols;
    }

    public void setColumns(int n) {
        if (n == 0 && this.rows == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.cols = n;
    }

    public int getHgap() {
        return this.hgap;
    }

    public void setHgap(int n) {
        this.hgap = n;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int n) {
        if (n == 0 && this.cols == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.rows = n;
    }

    public int getVgap() {
        return this.vgap;
    }

    public void setVgap(int n) {
        this.vgap = n;
    }

    public void layoutContainer(Container container) {
        Object object = container.getTreeLock();
        synchronized (object) {
            Insets insets = container.getInsets();
            int n = container.getComponentCount();
            int n2 = this.rows;
            int n3 = this.cols;
            if (n == 0) {
                return;
            }
            if (n2 > 0) {
                n3 = (n + n2 - 1) / n2;
            } else {
                n2 = (n + n3 - 1) / n3;
            }
            int n4 = container.getSize().width - (insets.left + insets.right);
            int n5 = container.getSize().height - (insets.top + insets.bottom);
            n4 = (n4 - (n3 - 1) * this.hgap) / n3;
            n5 = (n5 - (n2 - 1) * this.vgap) / n2;
            int n6 = 0;
            int n7 = insets.top;
            while (n6 < n2) {
                int n8 = 0;
                int n9 = insets.left;
                while (n8 < n3) {
                    int n10 = n8 * n2 + n6;
                    if (n10 < n) {
                        container.getComponent(n10).setBounds(n9, n7, n4, n5);
                    }
                    ++n8;
                    n9 += n4 + this.hgap;
                }
                ++n6;
                n7 += n5 + this.vgap;
            }
        }
    }

    public Dimension minimumLayoutSize(Container container) {
        Object object = container.getTreeLock();
        synchronized (object) {
            Insets insets = container.getInsets();
            int n = container.getComponentCount();
            int n2 = this.rows;
            int n3 = this.cols;
            if (n2 > 0) {
                n3 = (n + n2 - 1) / n2;
            } else {
                n2 = (n + n3 - 1) / n3;
            }
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            while (n6 < n) {
                Component component = container.getComponent(n6);
                Dimension dimension = component.getMinimumSize();
                if (n4 < dimension.width) {
                    n4 = dimension.width;
                }
                if (n5 < dimension.height) {
                    n5 = dimension.height;
                }
                ++n6;
            }
            return new Dimension(insets.left + insets.right + n3 * n4 + (n3 - 1) * this.hgap, insets.top + insets.bottom + n2 * n5 + (n2 - 1) * this.vgap);
        }
    }

    public Dimension preferredLayoutSize(Container container) {
        Object object = container.getTreeLock();
        synchronized (object) {
            Insets insets = container.getInsets();
            int n = container.getComponentCount();
            int n2 = this.rows;
            int n3 = this.cols;
            if (n2 > 0) {
                n3 = (n + n2 - 1) / n2;
            } else {
                n2 = (n + n3 - 1) / n3;
            }
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            while (n6 < n) {
                Component component = container.getComponent(n6);
                Dimension dimension = component.getPreferredSize();
                if (n4 < dimension.width) {
                    n4 = dimension.width;
                }
                if (n5 < dimension.height) {
                    n5 = dimension.height;
                }
                ++n6;
            }
            return new Dimension(insets.left + insets.right + n3 * n4 + (n3 - 1) * this.hgap, insets.top + insets.bottom + n2 * n5 + (n2 - 1) * this.vgap);
        }
    }

    public void removeLayoutComponent(Component component) {
    }

    public String toString() {
        return String.valueOf(this.getClass().getName()) + "[hgap=" + this.hgap + ",vgap=" + this.vgap + ",rows=" + this.rows + ",cols=" + this.cols + "]";
    }
}

