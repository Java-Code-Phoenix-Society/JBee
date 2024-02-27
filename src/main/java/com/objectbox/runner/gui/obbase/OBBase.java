/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.obbase;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class OBBase
        extends Panel
        implements AdjustmentListener {
    protected Scrollbar vs;
    protected Scrollbar hs;
    protected int m_nVScrollbarWidth = 15;
    protected int m_nHScrollbarHeight = 15;
    protected transient Image offscreen = null;
    protected int m_nTopRow = 0;
    protected int m_nLeftCol = 0;
    protected boolean m_bAlwaysShowScrollbar = false;
    protected boolean m_bHideHScrollbar = false;
    protected boolean m_bHideVScrollbar = false;
    protected boolean m_bDoubleBuffering = true;
    protected transient String OSName;

    public OBBase() {
        this(false);
        this.setLayout(null);
    }

    public OBBase(boolean bl) {
        this.m_bAlwaysShowScrollbar = bl;
        this.vs = new Scrollbar(1);
        this.hs = new Scrollbar(0);
        this.vs.addAdjustmentListener(this);
        this.hs.addAdjustmentListener(this);
        this.setLayout(null);
        this.add(this.vs);
        this.add(this.hs);
        this.enableEvents(4L);
        this.enableEvents(16L);
        this.enableEvents(8L);
        this.enableEvents(256L);
        this.enableEvents(32L);
        this.OSName = System.getProperty("os.name");
    }

    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        if (adjustmentEvent.getID() == 601) {
            if (adjustmentEvent.getSource() instanceof Scrollbar && ((Scrollbar) adjustmentEvent.getSource()).getOrientation() == 1) {
                int n = this.getVScrollbar().getValue();
                if (this.onTopRowChange(n)) {
                    this.m_nTopRow = n;
                    this.onTopRowChanged(n);
                }
            } else {
                int n = this.getHScrollbar().getValue();
                if (this.onLeftColChange(n)) {
                    this.m_nLeftCol = n;
                    this.onLeftColChanged(n);
                }
            }
        }
    }

    public void doLayout() {
        Object object = this.getTreeLock();
        synchronized (object) {
            Dimension dimension = this.getSize();
            int n = dimension.width;
            int n2 = dimension.height;
            Scrollbar scrollbar = this.getVScrollbar();
            Scrollbar scrollbar2 = this.getHScrollbar();
            if (this.m_bAlwaysShowScrollbar || this.getMaxTopRow() != 0 && !this.m_bHideVScrollbar) {
                scrollbar2.setBounds(this.getInsets().left, n2 - this.m_nHScrollbarHeight - this.getInsets().bottom, n - this.m_nVScrollbarWidth - this.getInsets().left - this.getInsets().right, this.m_nHScrollbarHeight);
            } else {
                scrollbar2.setBounds(this.getInsets().left, n2 - this.m_nHScrollbarHeight - this.getInsets().bottom, n - this.getInsets().left - this.getInsets().right, this.m_nHScrollbarHeight);
            }
            if (this.m_bAlwaysShowScrollbar || this.getMaxLeftCol() != 0 && !this.m_bHideHScrollbar) {
                scrollbar.setBounds(n - this.m_nVScrollbarWidth - this.getInsets().right, this.getInsets().top, this.m_nVScrollbarWidth, n2 - this.m_nHScrollbarHeight - this.getInsets().top - this.getInsets().bottom);
            } else {
                scrollbar.setBounds(n - this.m_nVScrollbarWidth - this.getInsets().right, this.getInsets().top, this.m_nVScrollbarWidth, n2 - this.getInsets().top - this.getInsets().bottom);
            }
            if (this.m_bAlwaysShowScrollbar || this.getMaxTopRow() != 0 && !this.m_bHideVScrollbar) {
                scrollbar.setVisible(true);
            } else {
                scrollbar.setVisible(false);
            }
            if (this.m_bAlwaysShowScrollbar || this.getMaxLeftCol() != 0 && !this.m_bHideHScrollbar) {
                scrollbar2.setVisible(true);
            } else {
                scrollbar2.setVisible(false);
            }
        }
    }

    protected void draw(Graphics graphics) {
    }

    public boolean getAlwaysShowScrollbars() {
        return this.m_bAlwaysShowScrollbar;
    }

    public void setAlwaysShowScrollbars(boolean bl) {
        this.m_bAlwaysShowScrollbar = bl;
    }

    public Scrollbar getHScrollbar() {
        return this.hs;
    }

    public int getHScrollbarHeight() {
        return this.m_nHScrollbarHeight;
    }

    public void setHScrollbarHeight(int n) {
        if (n > 0) {
            this.m_nHScrollbarHeight = n;
            this.doLayout();
        }
    }

    protected int getMaxLeftCol() {
        return 0;
    }

    protected int getMaxTopRow() {
        return 0;
    }

    public Dimension getMinimumSize() {
        return new Dimension(100, 80);
    }

    public Dimension getPreferredSize() {
        Object object = this.getTreeLock();
        synchronized (object) {
            Dimension dimension = this.getSize();
            if (dimension.width > 0 && dimension.height > 0) {
                return dimension;
            }
            return this.getMinimumSize();
        }
    }

    public Scrollbar getVScrollbar() {
        return this.vs;
    }

    public int getVScrollbarWidth() {
        return this.m_nVScrollbarWidth;
    }

    public void setVScrollbarWidth(int n) {
        if (n > 0) {
            this.m_nVScrollbarWidth = n;
            this.doLayout();
        }
    }

    public void hideHScrollbar() {
        this.m_bHideHScrollbar = true;
        this.getHScrollbar().setVisible(false);
    }

    public void hideScrollbar() {
        this.m_bHideHScrollbar = true;
        this.m_bHideVScrollbar = true;
        this.hideVScrollbar();
        this.hideHScrollbar();
    }

    public void hideVScrollbar() {
        this.m_bHideVScrollbar = true;
        this.getVScrollbar().setVisible(false);
    }

    public boolean isDoubleBuffering(boolean bl) {
        return this.m_bDoubleBuffering;
    }

    protected boolean onLeftColChange(int n) {
        return true;
    }

    protected void onLeftColChanged(int n) {
        this.repaint();
    }

    protected boolean onTopRowChange(int n) {
        return true;
    }

    protected void onTopRowChanged(int n) {
        this.repaint();
    }

    public void paint(Graphics graphics) {
        if (!this.isShowing()) {
            return;
        }
        if (this.m_bDoubleBuffering && this.validateImage()) {
            Dimension dimension = this.getSize();
            Rectangle rectangle = graphics.getClipBounds();
            Graphics graphics2 = this.offscreen.getGraphics();
            if (rectangle != null) {
                graphics2.clipRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }
            graphics2.setFont(graphics.getFont());
            graphics2.setColor(this.getBackground());
            graphics2.fillRect(0, 0, dimension.width, dimension.height);
            graphics2.setColor(graphics.getColor());
            this.draw(graphics2);
            graphics.drawImage(this.offscreen, 0, 0, this);
            graphics2.dispose();
        } else if (graphics != null && graphics.getClipBounds() != null) {
            this.draw(graphics);
        }
    }

    public void repaint() {
        Dimension dimension = this.getSize();
        int n = dimension.height;
        int n2 = dimension.width;
        Graphics graphics = this.getGraphics();
        if (graphics != null) {
            if (!this.m_bDoubleBuffering) {
                graphics.setColor(this.getBackground());
                graphics.fillRect(0, 0, dimension.width, dimension.height);
            }
            this.update(graphics, 0, 0, n2, n);
            graphics.dispose();
        }
    }

    public void setBounds(int n, int n2, int n3, int n4) {
        if (n3 <= 0 || n4 <= 0) {
            return;
        }
        Object object = this.getTreeLock();
        synchronized (object) {
            super.setBounds(n, n2, n3, n4);
            this.updateScrollbar();
        }
    }

    public void setDoubleBuffering(boolean bl) {
        this.m_bDoubleBuffering = bl;
    }

    public void showHScrollbar() {
        this.m_bHideHScrollbar = false;
        this.getHScrollbar().setVisible(true);
    }

    public void showScrollbar() {
        this.m_bHideHScrollbar = false;
        this.m_bHideVScrollbar = false;
        this.showVScrollbar();
        this.showHScrollbar();
    }

    public void showVScrollbar() {
        this.m_bHideVScrollbar = false;
        this.getVScrollbar().setVisible(true);
    }

    public void update() {
        Dimension dimension = this.getSize();
        int n = dimension.height;
        int n2 = dimension.width;
        Graphics graphics = this.getGraphics();
        if (graphics != null) {
            this.update(graphics, 0, 0, n2, n);
            graphics.dispose();
        }
    }

    public void update(Graphics graphics) {
        if (this.m_bDoubleBuffering) {
            this.paint(graphics);
        } else {
            super.update(graphics);
        }
    }

    public void update(Graphics graphics, int n, int n2, int n3, int n4) {
        graphics.clipRect(n, n2, n3, n4);
        this.paint(graphics);
    }

    protected void updateScrollbar() {
        int n;
        int n2 = this.getMaxTopRow();
        int n3 = this.getMaxLeftCol();
        Scrollbar scrollbar = this.getVScrollbar();
        Scrollbar scrollbar2 = this.getHScrollbar();
        if (n2 == 0) {
            this.m_nTopRow = 0;
            if (this.m_bAlwaysShowScrollbar && (this.OSName.equals("Windows NT") || this.OSName.equals("Windows 95"))) {
                scrollbar.setEnabled(false);
            }
        } else {
            if (!scrollbar.isEnabled()) {
                scrollbar.setEnabled(true);
            }
            if ((n = n2 / 10) < 5) {
                n = 5;
            }
            scrollbar.setValues(scrollbar.getValue(), 1, 0, n2);
            scrollbar.setBlockIncrement(n);
            scrollbar.setUnitIncrement(1);
        }
        if (n3 == 0) {
            this.m_nLeftCol = 0;
            if (this.m_bAlwaysShowScrollbar && (this.OSName.equals("Windows NT") || this.OSName.equals("Windows 95"))) {
                scrollbar.setEnabled(false);
            }
        } else {
            if (!scrollbar2.isEnabled()) {
                scrollbar2.setEnabled(true);
            }
            if ((n = n3 / 10) < 5) {
                n = 5;
            }
            scrollbar2.setValues(scrollbar2.getValue(), 1, 0, n3);
            scrollbar2.setBlockIncrement(n);
            scrollbar2.setUnitIncrement(1);
        }
    }

    protected boolean validateImage() {
        try {
            Dimension dimension = this.getSize();
            if (this.offscreen == null || this.offscreen.getWidth(this) != dimension.width || this.offscreen.getHeight(this) != dimension.height) {
                if (dimension.width > 0 && dimension.height > 0) {
                    this.offscreen = this.createImage(dimension.width, dimension.height);
                } else {
                    return false;
                }
            }
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}

