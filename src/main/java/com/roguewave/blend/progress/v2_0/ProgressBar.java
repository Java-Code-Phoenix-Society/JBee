/*
 * Decompiled with CFR 0.152.
 */
package com.roguewave.blend.progress.v2_0;

import com.roguewave.blend.border.v2_0.Border;
import com.roguewave.blend.border.v2_0.FlatBorder;
import com.roguewave.blend.border.v2_0.RoundedBorder;
import com.roguewave.blend.border.v2_0.ThreeDBorder;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class ProgressBar
        extends Canvas {
    public static final int ALIGNMENT_LEFT = 1;
    public static final int ALIGNMENT_RIGHT = 2;
    public static final int ALIGNMENT_CENTER = 3;
    public static final int TEXT_FORMAT_PERCENTAGE = 1;
    public static final int TEXT_FORMAT_DECIMAL = 2;
    public static final int TEXT_FORMAT_NUMERIC = 3;
    public static final int TEXT_FORMAT_NONE = 4;
    public static final Color DEFAULT_BAR_COLOR = Color.red;
    public static final Color DEFAULT_FOREGROUND = Color.black;
    public static final int DEFAULT_ALIGNMENT = 3;
    public static final Border DEFAULT_BORDER = new ThreeDBorder();
    public static final int DEFAULT_MINIMUM = 0;
    public static final int DEFAULT_MAXIMUM = 100;
    public static final int DEFAULT_PROGRESS = 0;
    public static final String DEFAULT_PRE_TEXT = "";
    public static final int DEFAULT_TEXT_FORMAT = 1;
    public static final String DEFAULT_POST_TEXT = "";
    public static final Font DEFAULT_FONT = new Font("Helvetica", 0, 12);
    static final Color FLAT_SHADOW_COLOR = Color.black;
    static final Color FLAT_REFLECTION_COLOR = Color.black;
    static final long serialVersionUID = -8687333054606268895L;
    private int m_nProgress;
    private String m_strPreText;
    private String m_strPostText;
    private int m_nAlignment;
    private Border m_bdrBorder;
    private int m_nMinimum;
    private int m_nMaximum;
    private transient int m_nIncrements;
    private Color m_colBarColor;
    private String m_strText = "";
    private Rectangle m_rectTextRectangle = new Rectangle();
    private boolean m_bInitialized = false;
    private int m_nTextFormat;
    private PropertyChangeSupport m_pcsChanges = new PropertyChangeSupport(this);

    public ProgressBar() {
        this(3);
    }

    public ProgressBar(int n) {
        this(n, DEFAULT_BORDER);
    }

    public ProgressBar(int n, int n2) {
        this(n, DEFAULT_BORDER, n2);
    }

    public ProgressBar(int n, Border border) {
        this(n, border, 0);
    }

    public ProgressBar(int n, Border border, int n2) {
        this(n, border, n2, DEFAULT_BAR_COLOR);
    }

    public ProgressBar(int n, Border border, int n2, Color color) {
        this(n, border, n2, color, DEFAULT_FOREGROUND);
    }

    public ProgressBar(int n, Border border, int n2, Color color, Color color2) {
        this.setAlignment(n);
        this.setBorder(border);
        this.setBarColor(color);
        this.setForeground(color2);
        this.setProgress(n2);
        this.setMinimum(0);
        this.setMaximum(100);
        this.setPreText("");
        this.setTextFormat(1);
        this.setPostText("");
        this.setFont(DEFAULT_FONT);
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.m_pcsChanges.addPropertyChangeListener(propertyChangeListener);
    }

    private void calculateText() {
        String string = "";
        if (this.m_nTextFormat == 1) {
            double d = new Integer(100 * (this.m_nProgress - this.m_nMinimum)).doubleValue();
            double d2 = new Integer(this.m_nMaximum - this.m_nMinimum).doubleValue();
            int n = new Double(Math.round(d / d2)).intValue();
            string = String.valueOf(String.valueOf(n)) + "%";
        } else if (this.m_nTextFormat == 2) {
            double d = new Integer(this.m_nProgress - this.m_nMinimum).doubleValue();
            double d3 = new Integer(this.m_nMaximum - this.m_nMinimum).doubleValue();
            double d4 = (double) Math.round(100.0 * (d / d3)) / 100.0;
            string = String.valueOf(d4);
        } else if (this.m_nTextFormat == 3) {
            string = String.valueOf(this.m_nProgress - this.m_nMinimum);
        }
        this.m_strText = String.valueOf(this.m_strPreText) + string + this.m_strPostText;
    }

    public void decrement() {
        this.decrementBy(1);
    }

    public void decrementBy(int n) {
        this.incrementBy(-1 * n);
    }

    private void displayText(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5;
        Serializable serializable;
        if (this.m_bInitialized) {
            graphics.setColor(this.getBackground());
            graphics.fillRect(this.m_rectTextRectangle.x, this.m_rectTextRectangle.y, this.m_rectTextRectangle.width, this.m_rectTextRectangle.height);
            serializable = graphics.getColor();
            graphics.setColor(this.m_colBarColor);
            n5 = (this.m_nProgress - this.m_nMinimum) * n3 / this.m_nIncrements + n;
            if (n5 > this.m_rectTextRectangle.x + this.m_rectTextRectangle.width) {
                graphics.fillRect(this.m_rectTextRectangle.x, this.m_rectTextRectangle.y, this.m_rectTextRectangle.width, this.m_rectTextRectangle.height);
            } else if (n5 > this.m_rectTextRectangle.x) {
                graphics.fillRect(this.m_rectTextRectangle.x, this.m_rectTextRectangle.y, n5 - this.m_rectTextRectangle.x, this.m_rectTextRectangle.height);
            }
            graphics.setColor((Color) serializable);
        }
        this.calculateText();
        serializable = this.getFontMetrics(this.getFont());
        n5 = ((FontMetrics) serializable).stringWidth(this.m_strText);
        int n6 = ((FontMetrics) serializable).getMaxAscent();
        int n7 = 2;
        new Dimension(n5, n6);
        Point point = new Point();
        point.y = n2 + n4 / 2 + n6 / 2;
        switch (this.m_nAlignment) {
            case 1: {
                point.x = n + n7;
                break;
            }
            case 2: {
                point.x = n + n3 - n5 - n7;
                break;
            }
            case 3: {
                point.x = n + (n3 - n5 - n7) / 2;
            }
        }
        this.m_rectTextRectangle = new Rectangle(point.x, point.y - n6, n5 + n7, n6);
        Color color = graphics.getColor();
        graphics.setColor(this.getForeground());
        graphics.drawString(this.m_strText, point.x, point.y);
        graphics.setColor(color);
    }

    public int getAlignment() {
        return this.m_nAlignment;
    }

    public void setAlignment(int n) {
        Integer n2 = new Integer(this.getAlignment());
        this.m_nAlignment = n;
        this.m_pcsChanges.firePropertyChange("alignment", n2, new Integer(this.getAlignment()));
    }

    public Color getBackground() {
        return this.m_bdrBorder.getBackground();
    }

    public void setBackground(Color color) {
        Color color2 = this.getBackground();
        this.m_bdrBorder.setBackground(color);
        this.m_pcsChanges.firePropertyChange("background", color2, this.getBackground());
    }

    public Color getBarColor() {
        return this.m_colBarColor;
    }

    public void setBarColor(Color color) {
        Color color2 = this.getBarColor();
        this.m_colBarColor = color;
        this.m_pcsChanges.firePropertyChange("barColor", color2, this.getBarColor());
    }

    public Border getBorder() {
        return this.m_bdrBorder;
    }

    public void setBorder(Border border) {
        Border border2 = this.getBorder();
        this.m_bdrBorder = border;
        this.m_pcsChanges.firePropertyChange("border", border2, this.getBorder());
        if (border instanceof RoundedBorder) {
            ((RoundedBorder) this.m_bdrBorder).setPercentRounded(0);
        }
    }

    public Color getBorderColor() {
        if (this.m_bdrBorder instanceof FlatBorder) {
            return ((FlatBorder) this.m_bdrBorder).getBorderColor();
        }
        return Color.black;
    }

    public void setBorderColor(Color color) {
        if (this.m_bdrBorder instanceof FlatBorder) {
            Color color2 = this.getBorderColor();
            ((FlatBorder) this.m_bdrBorder).setBorderColor(color);
            this.m_pcsChanges.firePropertyChange("borderColor", color2, this.getBorderColor());
        }
    }

    public int getBorderThickness() {
        return this.getBorder().getThickness();
    }

    public void setBorderThickness(int n) {
        Integer n2 = new Integer(this.getBorderThickness());
        this.getBorder().setThickness(n);
        this.m_pcsChanges.firePropertyChange("borderThickness", n2, new Integer(this.getBorderThickness()));
    }

    public int getMaximum() {
        return this.m_nMaximum;
    }

    public void setMaximum(int n) {
        this.sanityCheck(this.getMinimum(), this.getProgress(), n);
        Integer n2 = new Integer(this.getMaximum());
        this.m_nMaximum = n;
        this.m_pcsChanges.firePropertyChange("maximum", n2, new Integer(this.getMaximum()));
    }

    public int getMinimum() {
        return this.m_nMinimum;
    }

    public void setMinimum(int n) {
        this.sanityCheck(n, this.getProgress(), this.getMaximum());
        Integer n2 = new Integer(this.getMinimum());
        this.m_nMinimum = n;
        this.m_pcsChanges.firePropertyChange("minimum", n2, new Integer(this.getMinimum()));
    }

    public Dimension getMinimumSize() {
        int n = 10;
        int n2 = 4;
        FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
        int n3 = Math.max(n, fontMetrics.stringWidth(this.m_strText));
        int n4 = Math.max(n2, fontMetrics.getMaxAscent());
        return new Dimension(n3, n4);
    }

    public String getPostText() {
        return this.m_strPostText;
    }

    public void setPostText(String string) {
        String string2 = this.getPostText();
        this.m_strPostText = string;
        this.m_pcsChanges.firePropertyChange("postText", string2, this.getPostText());
    }

    public Dimension getPreferredSize() {
        Dimension dimension = this.getMinimumSize();
        return new Dimension(dimension.width * 5, dimension.height * 3);
    }

    public String getPreText() {
        return this.m_strPreText;
    }

    public void setPreText(String string) {
        String string2 = this.getPreText();
        this.m_strPreText = string;
        this.m_pcsChanges.firePropertyChange("preText", string2, this.getPreText());
    }

    public int getProgress() {
        return this.m_nProgress;
    }

    public void setProgress(int n) {
        this.sanityCheck(this.getMinimum(), n, this.getMaximum());
        Integer n2 = new Integer(this.getProgress());
        this.m_nProgress = n;
        this.m_pcsChanges.firePropertyChange("progress", n2, new Integer(this.getProgress()));
    }

    public Color getReflectionColor() {
        if (this.m_bdrBorder instanceof ThreeDBorder) {
            return ((ThreeDBorder) this.m_bdrBorder).getReflectionColor();
        }
        return FLAT_REFLECTION_COLOR;
    }

    public void setReflectionColor(Color color) {
        if (this.m_bdrBorder instanceof ThreeDBorder) {
            Color color2 = this.getReflectionColor();
            ((ThreeDBorder) this.m_bdrBorder).setReflectionColor(color);
            this.m_pcsChanges.firePropertyChange("reflectionColor", color2, this.getReflectionColor());
        }
    }

    public Color getShadowColor() {
        if (this.m_bdrBorder instanceof ThreeDBorder) {
            return ((ThreeDBorder) this.m_bdrBorder).getShadowColor();
        }
        return FLAT_SHADOW_COLOR;
    }

    public void setShadowColor(Color color) {
        if (this.m_bdrBorder instanceof ThreeDBorder) {
            Color color2 = this.getShadowColor();
            ((ThreeDBorder) this.m_bdrBorder).setShadowColor(color);
            this.m_pcsChanges.firePropertyChange("shadowColor", color2, this.getShadowColor());
        }
    }

    public int getTextFormat() {
        return this.m_nTextFormat;
    }

    public void setTextFormat(int n) {
        Integer n2 = new Integer(this.getTextFormat());
        this.m_nTextFormat = n;
        this.m_pcsChanges.firePropertyChange("textFormat", n2, new Integer(this.getTextFormat()));
    }

    public void increment() {
        this.incrementBy(1);
    }

    public void incrementBy(int n) {
        this.setProgress(this.m_nProgress + n);
        this.repaint();
    }

    public void paint(Graphics graphics) {
        this.m_bInitialized = true;
        Dimension dimension = this.getSize();
        Image image = this.createImage(dimension.width, dimension.height);
        Graphics graphics2 = image.getGraphics();
        Rectangle rectangle = new Rectangle(0, 0, dimension.width, dimension.height);
        this.m_bdrBorder.paint(graphics2, rectangle);
        Insets insets = this.m_bdrBorder.getInsets(rectangle);
        this.m_nIncrements = this.m_nMaximum - this.m_nMinimum;
        int n = dimension.width - insets.left - insets.right;
        int n2 = dimension.height - insets.top - insets.bottom;
        int n3 = insets.left;
        int n4 = insets.top;
        graphics2.setColor(this.m_colBarColor);
        int n5 = this.m_nProgress;
        if (n5 > this.m_nMaximum) {
            n5 = this.m_nMaximum;
        }
        int n6 = (n5 - this.m_nMinimum) * n / this.m_nIncrements;
        graphics2.fillRect(n3, n4, n6, n2);
        graphics2.setClip(n3, n4, n, n2);
        this.displayText(graphics2, n3, n4, n, n2);
        graphics.drawImage(image, 0, 0, null);
        graphics2.dispose();
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.m_pcsChanges.removePropertyChangeListener(propertyChangeListener);
    }

    private void sanityCheck(int n, int n2, int n3) {
        String string = new String();
        string = String.valueOf(string) + "\n\n";
        string = String.valueOf(string) + "Minimum: ";
        string = String.valueOf(string) + n;
        string = String.valueOf(string) + "\n";
        string = String.valueOf(string) + "Progress: ";
        string = String.valueOf(string) + n2;
        string = String.valueOf(string) + "\n";
        string = String.valueOf(string) + "Maximum: ";
        string = String.valueOf(string) + n3;
        string = String.valueOf(string) + "\n\n";
        string = String.valueOf(string) + "Values must obey the rule: minimum <= progress <= maximum";
        string = String.valueOf(string) + "\n";
        if (n > n2 || n2 > n3) {
            throw new IllegalArgumentException(string);
        }
    }

    public void setEnabled(boolean bl) {
        Boolean bl2 = new Boolean(this.isEnabled());
        super.setEnabled(bl);
        this.m_pcsChanges.firePropertyChange("enabled", bl2, new Boolean(this.isEnabled()));
    }

    public void setFont(Font font) {
        Font font2 = this.getFont();
        super.setFont(font);
        this.m_pcsChanges.firePropertyChange("font", font2, this.getFont());
    }

    public void setForeground(Color color) {
        Color color2 = this.getForeground();
        super.setForeground(color);
        this.m_pcsChanges.firePropertyChange("foreground", color2, this.getForeground());
    }

    public void setVisible(boolean bl) {
        Boolean bl2 = new Boolean(this.isVisible());
        super.setVisible(bl);
        this.m_pcsChanges.firePropertyChange("visible", bl2, new Boolean(this.isVisible()));
    }

    public void update(Graphics graphics) {
        this.paint(graphics);
    }
}

