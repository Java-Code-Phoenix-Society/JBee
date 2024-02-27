/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.text;

import java.awt.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class OBLabel
        extends Panel
        implements IScrollable {
    public static final int STANDARD = 1;
    public static final int LEFT = 1;
    public static final int CENTER = 3;
    public static final int RIGHT = 2;
    public static final int TOP = 1;
    public static final int BOTTOM = 2;
    public static final int NONE = 0;
    public static final int INSET = 1;
    public static final int RAISED = 2;
    public static final int NORMAL = 3;
    protected Vector m_vParagraphs;
    protected Color borderColor = Color.black;
    protected Insets m_inTextInsets;
    protected boolean m_bDoubleBuffering = true;
    protected boolean m_bLockUpdate = false;
    Font defaultfont = new Font("Dialog", 0, 12);
    int m_nHAlign = 1;
    int m_nVAlign = 1;
    int m_nBorderStyle = 0;
    int y_offset = 0;
    int prevYOffset = 0;
    int m_nWidthOffset = 0;
    boolean m_bUnderline = false;
    boolean m_bStrikeOut = false;
    boolean m_bAutoWrap = true;
    transient Image offscreen;

    public OBLabel() {
        this("");
    }

    public OBLabel(String string) {
        this.m_vParagraphs = new Vector();
        super.setFont(this.defaultfont);
        super.setBackground(Color.white);
        super.setForeground(Color.black);
        this.m_inTextInsets = new Insets(3, 3, 3, 3);
        if (string.equals("")) {
            Paragraph paragraph = new Paragraph(this, string);
            paragraph.setFont(this.getFont());
            paragraph.setFontMetrics(this.getFontMetrics(this.getFont()));
            paragraph.setBackground(this.getBackground());
            paragraph.setForeground(this.getForeground());
            paragraph.setWidth(this.getSize().width);
            paragraph.setInsets(this.m_inTextInsets);
            paragraph.setAutoWrap(this.isAutoWrap());
            this.m_vParagraphs.addElement(paragraph);
        } else {
            StringTokenizer stringTokenizer = new StringTokenizer(string, "\n", false);
            while (stringTokenizer.hasMoreTokens()) {
                String string2 = stringTokenizer.nextToken();
                Paragraph paragraph = new Paragraph(this, string2);
                paragraph.setFont(this.getFont());
                paragraph.setFontMetrics(this.getFontMetrics(this.getFont()));
                paragraph.setBackground(this.getBackground());
                paragraph.setForeground(this.getForeground());
                paragraph.setWidth(this.getSize().width);
                paragraph.setInsets(this.m_inTextInsets);
                paragraph.setAutoWrap(this.isAutoWrap());
                this.m_vParagraphs.addElement(paragraph);
            }
        }
    }

    public void addNotify() {
        super.addNotify();
        OBLabel oBLabel = this;
        synchronized (oBLabel) {
            int n = 0;
            while (n < this.m_vParagraphs.size()) {
                Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n);
                paragraph.setWidth(this.getSize().width - (this.m_inTextInsets.left + this.m_inTextInsets.right) - this.m_nWidthOffset);
                paragraph.addNotify();
                ++n;
            }
        }
    }

    public void draw(Graphics graphics) {
        int n;
        int n2;
        if (graphics == null || this.isLockUpdate()) {
            return;
        }
        Dimension dimension = this.getSize();
        this.printBorder(graphics);
        int n3 = 2;
        if (this.m_nBorderStyle == 0) {
            n3 = 0;
        } else if (this.m_nBorderStyle == 3) {
            n3 = 1;
        }
        graphics.clipRect(this.m_inTextInsets.left + n3, this.m_inTextInsets.top + n3, dimension.width - this.m_inTextInsets.left - this.m_inTextInsets.right - 2 * n3 + 1, dimension.height - this.m_inTextInsets.top - this.m_inTextInsets.bottom - 2 * n3 + 1);
        int n4 = this.m_inTextInsets.top;
        if (this.y_offset == 0 && this.m_nVAlign != 1) {
            n2 = dimension.height;
            n = 0;
            int n5 = 0;
            while (n5 < this.m_vParagraphs.size()) {
                n += ((Paragraph) this.m_vParagraphs.elementAt(n5)).getYSpan();
                ++n5;
            }
            if (this.m_nVAlign == 3) {
                n4 = n2 / 2 - n / 2;
            } else if (this.m_nVAlign == 2) {
                n4 = n2 - n - this.m_inTextInsets.bottom;
            }
        }
        n4 -= this.y_offset;
        n2 = this.m_inTextInsets.left;
        graphics.setColor(this.getBackground());
        graphics.fillRect(2, 2, dimension.width - 4, dimension.height - 4);
        graphics.setColor(this.getForeground());
        n = 0;
        while (n < this.m_vParagraphs.size()) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n);
            paragraph.render(graphics, n2, n4, this.m_bUnderline, this.m_bStrikeOut);
            n4 += paragraph.getYSpan();
            ++n;
        }
    }

    public boolean getAutoWrap() {
        return this.m_bAutoWrap;
    }

    public boolean getBorder() {
        return this.m_nBorderStyle != 0;
    }

    public void setBorder(boolean bl) {
        if (!bl) {
            this.m_nBorderStyle = 0;
            this.repaint();
        }
    }

    public Color getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        this.repaint();
    }

    public int getBorderStyle() {
        return this.m_nBorderStyle;
    }

    public void setBorderStyle(int n) {
        if (n != 0 && n != 1 && n != 2 && n != 3) {
            return;
        }
        this.m_nBorderStyle = n;
        this.repaint();
    }

    public int getHAlign() {
        return this.m_nHAlign;
    }

    public void setHAlign(int n) {
        if (n != 1 && n != 3 && n != 2) {
            return;
        }
        this.m_nHAlign = n;
        int n2 = 0;
        while (n2 < this.m_vParagraphs.size()) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n2);
            paragraph.setHAlign(n);
            ++n2;
        }
        this.repaint();
    }

    public Insets getInsets() {
        return this.m_inTextInsets;
    }

    public void setInsets(Insets insets) {
        this.m_inTextInsets = insets;
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            ((Paragraph) this.m_vParagraphs.elementAt(n)).setInsets(insets);
            ++n;
        }
        this.repaint();
    }

    public Dimension getMinimumSize() {
        return new Dimension(80, 20);
    }

    public Dimension getPreferredSize() {
        Dimension dimension = new Dimension(80, 0);
        int n = 0;
        int n2 = 0;
        while (n2 < this.m_vParagraphs.size()) {
            n += ((Paragraph) this.m_vParagraphs.elementAt(n2)).getYSpan();
            ++n2;
        }
        n2 = 2;
        if (this.m_nBorderStyle == 0) {
            n2 = 0;
        } else if (this.m_nBorderStyle == 3) {
            n2 = 1;
        }
        dimension.height = this.m_inTextInsets.top + this.m_inTextInsets.bottom + n + 2 * n2;
        return dimension;
    }

    public boolean getRaised() {
        return this.m_nBorderStyle == 2;
    }

    public void setRaised(boolean bl) {
        if (bl) {
            this.m_nBorderStyle = 2;
            this.repaint();
        }
    }

    public String getText() {
        if (this.m_vParagraphs.size() == 0) {
            return null;
        }
        String string = new String();
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            string = String.valueOf(string) + ((Paragraph) this.m_vParagraphs.elementAt(n)).getText() + "\n";
            ++n;
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

    public void setText(String string) {
        this.setText(string, true);
    }

    public int getTextHIndent() {
        return this.m_inTextInsets.left;
    }

    public void setTextHIndent(int n) {
        this.m_inTextInsets.left = n;
        this.repaint();
    }

    public int getTextVIndent() {
        return this.m_inTextInsets.top;
    }

    public void setTextVIndent(int n) {
        this.m_inTextInsets.top = n;
    }

    public int getVAlign() {
        return this.m_nVAlign;
    }

    public void setVAlign(int n) {
        if (n != 1 && n != 3 && n != 2) {
            return;
        }
        this.m_nVAlign = n;
        this.repaint();
    }

    protected int getWidthOffset() {
        return this.m_nWidthOffset;
    }

    protected void setWidthOffset(int n) {
        this.m_nWidthOffset = n;
        int n2 = this.getSize().width - this.m_inTextInsets.left - this.m_inTextInsets.right;
        int n3 = 0;
        while (n3 < this.m_vParagraphs.size()) {
            ((Paragraph) this.m_vParagraphs.elementAt(n3)).setWidth(n2 - n);
            ++n3;
        }
    }

    protected boolean isAutoWrap() {
        return this.m_bAutoWrap;
    }

    public void setAutoWrap(boolean bl) {
        this.m_bAutoWrap = bl;
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            ((Paragraph) this.m_vParagraphs.elementAt(n)).setAutoWrap(bl);
            ++n;
        }
    }

    public boolean isLockUpdate() {
        return this.m_bLockUpdate;
    }

    public void setLockUpdate(boolean bl) {
        this.m_bLockUpdate = bl;
    }

    public boolean isStrikeOut() {
        return this.m_bStrikeOut;
    }

    public void setStrikeOut(boolean bl) {
        this.m_bStrikeOut = bl;
        this.repaint();
    }

    public boolean isUnderLine() {
        return this.m_bUnderline;
    }

    public void setUnderLine(boolean bl) {
        this.m_bUnderline = bl;
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

    private void paintBorder(Graphics graphics, int n, int n2, int n3, int n4, boolean bl) {
        graphics.setColor(SystemColor.controlLtHighlight);
        if (bl) {
            graphics.drawLine(n, n2, n + n3, n2);
            graphics.drawLine(n, n2, n, n2 + n4);
        } else {
            graphics.drawLine(n + 1, n2 + n4 - 1, n + n3 - 1, n2 + n4 - 1);
            graphics.drawLine(n + n3 - 1, n2 + 1, n + n3 - 1, n2 + n4 - 1);
        }
        graphics.setColor(SystemColor.controlHighlight);
        if (!bl) {
            graphics.drawLine(n + 1, n2 + n4 - 2, n + n3 - 2, n2 + n4 - 2);
            graphics.drawLine(n + n3 - 2, n2 + 1, n + n3 - 2, n2 + n4 - 2);
        }
        graphics.setColor(SystemColor.controlShadow);
        if (bl) {
            graphics.drawLine(n + 1, n2 + n4 - 1, n + n3 - 1, n2 + n4 - 1);
            graphics.drawLine(n + n3 - 1, n2 + 1, n + n3 - 1, n2 + n4 - 1);
        } else {
            graphics.drawLine(n, n2, n + n3 - 1, n2);
            graphics.drawLine(n, n2, n, n2 + n4 - 1);
        }
        graphics.setColor(SystemColor.controlDkShadow);
        if (bl) {
            graphics.drawLine(n, n2 + n4, n + n3, n2 + n4);
            graphics.drawLine(n + n3, n2, n + n3, n2 + n4);
        } else {
            graphics.drawLine(n + 1, n2 + 1, n + n3 - 2, n2 + 1);
            graphics.drawLine(n + 1, n2 + 1, n + 1, n2 + n4 - 2);
        }
    }

    protected void printBorder(Graphics graphics) {
        Dimension dimension = super.getSize();
        if (this.m_nBorderStyle == 0) {
            graphics.setColor(this.getBackground());
            graphics.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
        } else if (this.m_nBorderStyle == 3) {
            graphics.setColor(this.borderColor);
            graphics.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
        } else if (this.m_nBorderStyle == 2 || this.m_nBorderStyle == 1) {
            this.paintBorder(graphics, 0, 0, dimension.width, dimension.height, this.m_nBorderStyle == 2);
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

    public void setBackground(Color color) {
        super.setBackground(color);
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            ((Paragraph) this.m_vParagraphs.elementAt(n)).setBackground(color);
            ++n;
        }
        this.repaint();
    }

    public void setBounds(int n, int n2, int n3, int n4) {
        super.setBounds(n, n2, n3, n4);
        int n5 = 0;
        while (n5 < this.m_vParagraphs.size()) {
            ((Paragraph) this.m_vParagraphs.elementAt(n5)).setWidth(n3 - (this.m_inTextInsets.left + this.m_inTextInsets.right) - this.m_nWidthOffset);
            ++n5;
        }
        this.repaint();
    }

    public void setFont(int n, Font font) {
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n);
        if (paragraph != null) {
            paragraph.setFont(font);
            paragraph.setFontMetrics(this.getFontMetrics(font));
        }
        this.repaint();
    }

    public void setFont(Font font) {
        super.setFont(font);
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n);
            paragraph.setFont(font);
            paragraph.setFontMetrics(this.getFontMetrics(font));
            ++n;
        }
        this.repaint();
    }

    public void setForeground(Color color) {
        super.setForeground(color);
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            ((Paragraph) this.m_vParagraphs.elementAt(n)).setForeground(color);
            ++n;
        }
        this.repaint();
    }

    public void setSize(int n, int n2) {
        super.setSize(n, n2);
        int n3 = 0;
        while (n3 < this.m_vParagraphs.size()) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n3);
            paragraph.setWidth(n - (this.m_inTextInsets.left + this.m_inTextInsets.right) - this.m_nWidthOffset);
            ++n3;
        }
        this.repaint();
    }

    public void setText(String string, boolean bl) {
        if (string == null) {
            string = "";
        }
        if (this.m_vParagraphs.size() > 0) {
            this.m_vParagraphs.removeAllElements();
        }
        if (string.equals("")) {
            Paragraph paragraph = new Paragraph(this, string);
            paragraph.setFont(this.getFont());
            paragraph.setFontMetrics(this.getFontMetrics(this.getFont()));
            paragraph.setBackground(this.getBackground());
            paragraph.setForeground(this.getForeground());
            paragraph.setWidth(this.getSize().width - this.m_nWidthOffset);
            paragraph.setInsets(this.m_inTextInsets);
            paragraph.setHAlign(this.getHAlign());
            paragraph.setAutoWrap(this.isAutoWrap());
            this.m_vParagraphs.addElement(paragraph);
        } else {
            StringTokenizer stringTokenizer = new StringTokenizer(string, "\n", false);
            while (stringTokenizer.hasMoreTokens()) {
                String string2 = stringTokenizer.nextToken();
                Paragraph paragraph = new Paragraph(this, string2);
                paragraph.setFont(this.getFont());
                paragraph.setFontMetrics(this.getFontMetrics(this.getFont()));
                paragraph.setBackground(this.getBackground());
                paragraph.setForeground(this.getForeground());
                paragraph.setWidth(this.getSize().width - this.m_nWidthOffset);
                paragraph.setInsets(this.m_inTextInsets);
                paragraph.setHAlign(this.getHAlign());
                paragraph.setAutoWrap(this.isAutoWrap());
                this.m_vParagraphs.addElement(paragraph);
            }
        }
        if (bl) {
            this.repaint();
        }
    }

    public void setXOffset(int n) {
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

