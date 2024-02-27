package com.objectbox.runner.gui.text;

import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

public class Paragraph implements Serializable {
    public static final int LEFT = 1;
    public static final int CENTER = 3;
    public static final int RIGHT = 2;
    protected static final int NONE = 0;
    protected static final int PARTIAL = 1;
    protected static final int COMPLETE = 2;
    StringBuffer buffer;
    Vector<String> lines;
    transient FontMetrics fontMetrics;
    Font m_fFont = null;
    Color m_cHighlight = SystemColor.textHighlight;
    Color m_cTextHighlight = SystemColor.textHighlightText;
    Color m_cBackground = Color.white;
    Color m_cForeground = Color.black;
    Insets m_inInsets;
    boolean m_bVisible = true;
    boolean m_bMarkVisible = true;
    int cursorPos = 0;
    int currentLine = 0;
    int m_nYSpan;
    int m_nFirstLine = NONE;
    int m_nHAlign = LEFT;
    int leftOffset = 10;
    int m_nWidth = NONE;
    int selectionStart;
    int selectionEnd;
    int selected = NONE;
    IScrollable target;
    private int x_offset = NONE;
    private int prevXOffset = NONE;
    private boolean m_bAllowScroll = false;
    private boolean m_bAutoWrap = false;

    public Paragraph() {
        this("");
    }

    public Paragraph(IScrollable iScrollable) {
        this(iScrollable, "");
    }

    public Paragraph(IScrollable iScrollable, String string) {
        this(string);
        this.target = iScrollable;
    }

    public Paragraph(String string) {
        this.lines = new Vector();
        this.buffer = new StringBuffer(string);
        this.lines.addElement(this.buffer.toString());
        this.m_inInsets = new Insets(3, 3, 3, 3);
    }

    public void addNotify() {
        this.onTextModified();
    }

    public Color getBackground() {
        return this.m_cBackground;
    }

    public void setBackground(Color color) {
        this.m_cBackground = color;
    }

    protected Point getCursorPoint() {
        if (this.fontMetrics == null) {
            if (this.m_fFont == null) {
                return null;
            }

            try {
                Component fontTarget = (Component) this.getTarget();
                this.fontMetrics = fontTarget.getFontMetrics(this.m_fFont);
            } catch (Exception e) {
                JBLogger.log(e.getMessage());
                return null;
            }
        }

        Point point = new Point(0, 0);
        int pos = 0;

        for (int i = 0; i < this.lines.size(); ++i) {
            String item = this.lines.elementAt(i);
            int length = item.length();
            if (length > 0 && i < this.lines.size() - PARTIAL && item.charAt(length - PARTIAL) == ' ') {
                --length;
            }

            length += pos;
            if (this.cursorPos >= pos && this.cursorPos <= length) {
                String substring = item.substring(0, this.cursorPos - pos);
                point.x = this.fontMetrics.stringWidth(substring) + this.m_inInsets.left;
                if (this.x_offset <= NONE && this.m_nHAlign != LEFT) {
                    if (this.m_nHAlign == CENTER) {
                        point.x = this.m_nWidth / 2 - this.fontMetrics.stringWidth(item) / 2 +
                                this.fontMetrics.stringWidth(substring) - 4;
                    } else if (this.m_nHAlign == RIGHT) {
                        if (this.fontMetrics.stringWidth(substring) > this.m_nWidth) {
                            point.x = this.m_inInsets.left + this.fontMetrics.stringWidth(substring) + 2;
                        } else {
                            point.x = this.m_nWidth - 2 - this.m_inInsets.right - this.fontMetrics.stringWidth(item) +
                                    this.fontMetrics.stringWidth(substring);
                        }
                    }
                }

                point.x -= this.x_offset;
                break;
            }

            pos += item.length();
            point.y += this.fontMetrics.getHeight();
        }

        return point;
    }

    protected int getCursorPos() {
        return this.cursorPos;
    }

    protected void setCursorPos(int n) {
        this.cursorPos = n;
        this.onScrollAction();
    }

    public int getCursorPosFromPoint(Point point) {
        if (this.fontMetrics == null) {
            if (this.m_fFont == null) {
                return NONE;
            }
            try {
                Component component = (Component) this.getTarget();
                this.fontMetrics = component.getFontMetrics(this.m_fFont);
            } catch (Exception exception) {
                return NONE;
            }
        }
        if (point.y > this.getYSpan()) {
            return this.buffer.length();
        }
        int n = -1;
        int n2 = 0;
        int n3 = 0;
        while (n3 < this.lines.size()) {
            if ((n3 + 1) * this.fontMetrics.getHeight() > point.y) {
                n = n3;
                break;
            }
            n2 += this.lines.elementAt(n3).length();
            ++n3;
        }
        if (n == -1) {
            n = this.lines.size() - 1;
        }
        String string = this.lines.elementAt(n);
        return n2 + this.getPositionAt(string, point.x);
    }

    protected int getFirstLine() {
        return this.m_nFirstLine;
    }

    public Font getFont() {
        return this.m_fFont;
    }

    public void setFont(Font font) {
        this.m_fFont = font;
    }

    public FontMetrics getFontMetrics() {
        return this.fontMetrics;
    }

    public void setFontMetrics(FontMetrics fontMetrics) {
        this.fontMetrics = fontMetrics;
    }

    public Color getForeground() {
        return this.m_cForeground;
    }

    public void setForeground(Color color) {
        this.m_cForeground = color;
    }

    protected Color getHighlight() {
        return this.m_cHighlight;
    }

    protected void setHighlight(Color color) {
        this.m_cHighlight = color;
    }

    protected Color getHighlightedText() {
        return this.m_cTextHighlight;
    }

    protected void setHighlightedText(Color color) {
        this.m_cTextHighlight = color;
    }

    public Insets getInsets() {
        return this.m_inInsets;
    }

    public void setInsets(Insets insets) {
        this.m_inInsets = insets;
    }

    protected Vector getLines() {
        return this.lines;
    }

    protected boolean getMarkVisible() {
        return this.m_bMarkVisible;
    }

    protected void setMarkVisible(boolean bl) {
        this.m_bMarkVisible = bl;
    }

    protected int getPositionAt(String string, int n) {
        if (this.fontMetrics == null) {
            if (this.m_fFont == null) {
                return NONE;
            }
            try {
                Component component = (Component) this.getTarget();
                this.fontMetrics = component.getFontMetrics(this.m_fFont);
            } catch (Exception exception) {
                return NONE;
            }
        }
        if (this.x_offset <= NONE && this.m_nHAlign != LEFT) {
            int n2 = this.m_nWidth;
            int n3 = this.fontMetrics.stringWidth(string);
            if (this.m_nHAlign == CENTER) {
                n -= (n2 - n3) / 2;
            } else if (this.m_nHAlign == RIGHT) {
                n -= n2 - n3;
            }
        }
        if ((n += this.x_offset) == NONE) {
            return n;
        }
        if (this.fontMetrics.stringWidth(string) <= n) {
            int n4 = string.length();
            if (this.lines.indexOf(string) < this.lines.size() - 1 && string.charAt(n4 - 1) == ' ') {
                --n4;
            }
            return n4;
        }
        int n5 = 0;
        while (n5 < string.length() - 1) {
            if (this.fontMetrics.stringWidth(string.substring(0, n5 + 1)) > n) break;
            ++n5;
        }
        return n5;
    }

    protected int getPrevXOffset() {
        return this.prevXOffset;
    }

    protected void setPrevXOffset(int n) {
        this.prevXOffset = n;
    }

    protected int getSelectionEnd() {
        return this.selectionEnd;
    }

    protected void setSelectionEnd(int n) {
        this.selectionEnd = n;
    }

    protected int getSelectionStart() {
        return this.selectionStart;
    }

    protected void setSelectionStart(int n) {
        this.selectionStart = n;
    }

    protected int getSelectionState() {
        return this.selected;
    }

    protected void setSelectionState(int n) {
        this.selected = n;
    }

    public IScrollable getTarget() {
        return this.target;
    }

    public void setTarget(IScrollable iScrollable) {
        this.target = iScrollable;
    }

    protected String getText() {
        return this.buffer.toString();
    }

    protected void setText(String string) {
        this.buffer = new StringBuffer(string);
        this.onTextModified();
    }

    public int getWidth() {
        return this.m_nWidth;
    }

    public void setWidth(int n) {
        this.m_nWidth = n;
        this.onTextModified();
    }

    protected int getXCoord() {
        return this.cursorPos;
    }

    protected int getXOffset() {
        return this.x_offset;
    }

    protected void setXOffset(int n) {
        this.x_offset = n;
    }

    protected int getYSpan() {
        if (this.fontMetrics == null) {
            if (this.m_fFont == null) {
                return NONE;
            }
            try {
                Component component = (Component) this.getTarget();
                this.fontMetrics = component.getFontMetrics(this.m_fFont);
            } catch (Exception exception) {
                return NONE;
            }
        }
        return this.fontMetrics.getHeight() * this.lines.size();
    }

    protected boolean insertChar(char c, boolean bl) {
        Serializable serializable;
        if (this.fontMetrics == null) {
            if (this.m_fFont == null) {
                return false;
            }
            try {
                serializable = (Component) this.getTarget();
                this.fontMetrics = ((Component) serializable).getFontMetrics(this.m_fFont);
            } catch (Exception exception) {
                return false;
            }
        }
        if (this.cursorPos > this.buffer.length()) {
            return false;
        }
        if (bl) {
            serializable = new StringBuffer(this.buffer.toString());
            ((StringBuffer) serializable).insert(this.cursorPos, c);
            if (this.fontMetrics.stringWidth(((StringBuffer) serializable).toString()) <= this.m_nWidth - 4) {
                this.buffer.insert(this.cursorPos++, c);
                this.onTextModified();
                return true;
            }
            return false;
        }
        this.buffer.insert(this.cursorPos++, c);
        this.onTextModified();
        return true;
    }

    protected boolean isAllowScroll() {
        return this.m_bAllowScroll;
    }

    protected void setAllowScroll(boolean bl) {
        this.m_bAllowScroll = bl;
    }

    public boolean isAutoWrap() {
        return this.m_bAutoWrap;
    }

    public void setAutoWrap(boolean bl) {
        if (this.m_bAutoWrap != bl) {
            this.m_bAutoWrap = bl;
            this.onTextModified();
        }
    }

    protected boolean isCursorAtEnd() {
        return this.cursorPos == this.buffer.length();
    }

    protected boolean isCursorOnFirstLine() {
        String string = this.lines.firstElement();
        int n = string.length();
        if (n > NONE && this.lines.size() > 1 && string.charAt(n - 1) == ' ') {
            --n;
        }
        return this.cursorPos <= n;
    }

    protected boolean isCursorOnLastLine() {
        if (this.lines.size() == 1) {
            return true;
        }
        int n = 0;
        int n2 = 0;
        while (n2 < this.lines.size() - 1) {
            n += this.lines.elementAt(n2).length();
            ++n2;
        }
        return this.cursorPos > n;
    }

    protected boolean isVisible() {
        return this.m_bVisible;
    }

    protected void moveSpecial(boolean bl, int n, int n2) {
        int n3;
        Object object;
        if (this.fontMetrics == null) {
            if (this.m_fFont == null) {
                return;
            }
            try {
                object = this.getTarget();
                this.fontMetrics = ((Component) object).getFontMetrics(this.m_fFont);
            } catch (Exception exception) {
                return;
            }
        }
        object = bl ? this.lines.lastElement() : this.lines.firstElement();
        int n4 = 0;
        if (bl) {
            n3 = 0;
            while (n3 < this.lines.size() - 1) {
                n4 += this.lines.elementAt(n3).length();
                ++n3;
            }
        }
        if (this.isAllowScroll()) {
            n3 = this.fontMetrics.stringWidth((String) object);
            if (bl && (n2 == 37 || n2 == 35)) {
                if (n3 + this.m_inInsets.right + this.m_inInsets.left > n && this.getTarget() != null) {
                    int n5 = Math.max(0, n3 - n + this.m_inInsets.right + this.m_inInsets.left + 6);
                    this.getTarget().setXOffset(n5);
                }
            } else if (!(bl || n2 != 39 && n2 != 36)) {
                if (this.x_offset > NONE && this.getTarget() != null) {
                    this.getTarget().setXOffset(NONE);
                }
            } else if (this.x_offset > n3 && this.getTarget() != null) {
                int n6 = Math.max(0, n3 - n + this.m_inInsets.right + this.m_inInsets.left + 6);
                this.getTarget().setXOffset(n6);
            }
        }
        this.cursorPos = n4 + this.getPositionAt((String) object, n);
    }

    protected void onBackspaceKey() {
        Object object;
        if (this.fontMetrics == null) {
            if (this.m_fFont == null) {
                return;
            }
            try {
                object = this.getTarget();
                this.fontMetrics = ((Component) object).getFontMetrics(this.m_fFont);
            } catch (Exception exception) {
                return;
            }
        }
        object = this.buffer.toString();
        String string = ((String) object).substring(0, this.cursorPos - 1);
        if (this.cursorPos < ((String) object).length()) {
            string = string + ((String) object).substring(this.cursorPos);
        }
        this.buffer = new StringBuffer(string);
        --this.cursorPos;
        this.onTextModified();
        if (this.isAllowScroll()) {
            int n = this.fontMetrics.stringWidth(string);
            if (this.getCursorPoint().x <= this.m_inInsets.left && this.getTarget() != null) {
                int n2 = Math.max(0, n - 3 * this.m_nWidth / 2 + this.m_inInsets.left + this.m_inInsets.right);
                this.setXOffset(n2);
                this.getTarget().setXOffset(n2);
            }
        }
    }

    protected void onDeleteKey() {
        String string = this.buffer.toString();
        if (this.cursorPos < string.length()) {
            String string2 = string.substring(0, this.cursorPos);
            string2 = String.valueOf(string2) + string.substring(this.cursorPos + 1);
            this.buffer = new StringBuffer(string2);
            this.onTextModified();
        }
    }

    protected void onDownKey() {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        while (n3 < this.lines.size()) {
            String string = this.lines.elementAt(n3);
            int n4 = string.length();
            if (n4 > NONE && n3 < this.lines.size() - 1 && string.charAt(n4 - 1) == ' ') {
                --n4;
            }
            if (this.cursorPos <= (n4 += (n += string.length()))) {
                n2 = n3 + 1;
                break;
            }
            ++n3;
        }
        if (n2 >= this.lines.size()) {
            return;
        }
        this.cursorPos = n + this.getPositionAt(this.lines.elementAt(n2), this.getCursorPoint().x);
    }

    protected void onEndKey() {
        if (this.isAllowScroll()) {
            this.moveSpecial(true, this.m_nWidth + this.m_inInsets.right + this.m_inInsets.left, 35);
        }
        int n = 0;
        int n2 = 0;
        while (n2 < this.lines.size()) {
            String string = this.lines.elementAt(n2);
            int n3 = string.length();
            if (n3 > NONE && n2 < this.lines.size() - 1 && string.charAt(n3 - 1) == ' ') {
                --n3;
            }
            if (this.cursorPos >= n && this.cursorPos <= (n3 += n)) {
                this.cursorPos = n3;
                break;
            }
            n += string.length();
            ++n2;
        }
    }

    protected String onEnterKey() {
        if (this.cursorPos == this.buffer.length()) {
            return null;
        }
        String string = this.buffer.toString();
        this.buffer = new StringBuffer(string.substring(0, this.cursorPos));
        this.onTextModified();
        return string.substring(this.cursorPos);
    }

    protected void onHomeKey() {
        if (this.isAllowScroll()) {
            this.moveSpecial(false, this.m_nWidth, 36);
        }
        int n = 0;
        int n2 = 0;
        while (n2 < this.lines.size()) {
            String string = this.lines.elementAt(n2);
            int n3 = string.length();
            if (n3 > NONE && n2 < this.lines.size() - 1 && string.charAt(n3 - 1) == ' ') {
                --n3;
            }
            if (this.cursorPos <= (n3 += n)) {
                this.cursorPos = n;
                break;
            }
            n += string.length();
            ++n2;
        }
    }

    protected void onScrollAction() {
        if (!this.m_bAllowScroll) {
            return;
        }
        if (this.m_nWidth > NONE) {
            IScrollable iScrollable;
            int n = this.getCursorPoint().x + 2;
            if (n > this.m_nWidth) {
                this.x_offset += n - this.m_nWidth;
            } else if (n < COMPLETE) {
                this.x_offset -= 2 - n;
            }
            if (this.isCursorAtEnd() && this.m_nHAlign == RIGHT && this.x_offset > NONE && n < this.m_nWidth - 2) {
                this.x_offset -= this.m_nWidth - 2 - n;
                if (this.x_offset < NONE) {
                    this.x_offset = NONE;
                }
            }
            if ((iScrollable = this.getTarget()) != null) {
                iScrollable.setXOffset(this.x_offset);
            }
        }
    }

    protected void onTextModified() {
        if (this.isAutoWrap() && this.m_nWidth > NONE) {
            this.lines = this.wrapText(this.buffer.toString());
        } else {
            this.lines.setElementAt(this.buffer.toString(), 0);
            this.onScrollAction();
        }
    }

    protected void onUpKey() {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        while (n4 < this.lines.size()) {
            String string = this.lines.elementAt(n4);
            int n5 = string.length();
            if (n5 > NONE && n4 < this.lines.size() - 1 && string.charAt(n5 - 1) == ' ') {
                --n5;
            }
            if (this.cursorPos >= n && this.cursorPos <= (n5 += n)) {
                n3 = n4 - 1;
                break;
            }
            n2 = n;
            n += string.length();
            ++n4;
        }
        if (n3 < NONE) {
            return;
        }
        this.cursorPos = n2 + this.getPositionAt(this.lines.elementAt(n3), this.getCursorPoint().x);
    }

    protected void render(Graphics graphics, int n, int n2, boolean bl, boolean bl2) {
        if (this.fontMetrics == null) {
            if (this.m_fFont == null) {
                return;
            }
            try {
                Component component = (Component) this.getTarget();
                this.fontMetrics = component.getFontMetrics(this.m_fFont);
            } catch (Exception exception) {
                return;
            }
        }
        int n3 = 0;
        graphics.setFont(this.m_fFont);
        graphics.setColor(this.m_cForeground);
        boolean bl3 = false;
        if (this.m_nHAlign == LEFT) {
            n += 2;
        }
        int n4 = 0;
        while (n4 < this.lines.size()) {
            int n5;
            int n6;
            String string = this.lines.elementAt(n4);
            if (this.x_offset == NONE && this.m_nHAlign != LEFT) {
                if (this.m_nHAlign == CENTER) {
                    n = this.m_nWidth / 2 - this.fontMetrics.stringWidth(string) / 2;
                } else if (this.m_nHAlign == RIGHT) {
                    n = this.m_nWidth - this.fontMetrics.stringWidth(string) - this.m_inInsets.right;
                }
            }
            if (n < this.m_inInsets.left) {
                n = this.m_inInsets.left;
            }
            if ((n6 = string.length()) > NONE && n4 < this.lines.size() - 1 && string.charAt(n6 - 1) == ' ') {
                --n6;
            }
            n6 += n3;
            int n7 = n2 + this.fontMetrics.getHeight();
            n7 -= this.fontMetrics.getHeight() / 4;
            n -= this.x_offset;
            if (!this.m_bMarkVisible || this.selected == NONE) {
                graphics.drawString(string, n, n7);
                if (bl) {
                    n5 = n2 + this.fontMetrics.getHeight();
                    graphics.drawLine(n, n5, n + this.fontMetrics.stringWidth(string), n5);
                }
                if (bl2) {
                    n5 = n2 + this.fontMetrics.getHeight() / 2;
                    graphics.drawLine(n, n5, n + this.fontMetrics.stringWidth(string), n5);
                }
            } else if (this.selected == COMPLETE) {
                graphics.setColor(this.m_cHighlight);
                graphics.fillRect(n, n2, this.fontMetrics.stringWidth(string), this.fontMetrics.getHeight());
                graphics.setColor(this.m_cTextHighlight);
                graphics.drawString(string, n, n7);
                if (bl) {
                    n5 = n2 + this.fontMetrics.getHeight();
                    graphics.drawLine(n, n5, n + this.fontMetrics.stringWidth(string), n5);
                }
                if (bl2) {
                    n5 = n2 + this.fontMetrics.getHeight() / 2;
                    graphics.drawLine(n, n5, n + this.fontMetrics.stringWidth(string), n5);
                }
            } else {
                int n8;
                int n9;
                String string2;
                String string3;
                n5 = this.selectionStart;
                int n10 = this.selectionEnd;
                if (this.selectionStart > this.selectionEnd) {
                    n10 = this.selectionStart;
                    n5 = this.selectionEnd;
                }
                boolean bl4 = false;
                boolean bl5 = false;
                boolean bl6 = false;
                if (n5 >= n3 && n5 <= n6 && n10 >= n3 && n10 <= n6) {
                    bl4 = true;
                } else if (n5 >= n3 && n5 < n3 + string.length()) {
                    bl6 = true;
                } else if (n10 >= n3 && n10 < n3 + string.length()) {
                    bl5 = true;
                }
                if (bl6) {
                    int n11 = n5 - n3;
                    string3 = string.substring(0, n11);
                    string2 = string.substring(n11);
                    graphics.drawString(string3, n, n7);
                    n9 = n + this.fontMetrics.stringWidth(string3);
                    graphics.setColor(this.m_cHighlight);
                    graphics.fillRect(n9, n2, this.fontMetrics.stringWidth(string2), this.fontMetrics.getHeight());
                    graphics.setColor(this.m_cTextHighlight);
                    graphics.drawString(string2, n9, n7);
                    if (bl) {
                        n8 = n2 + this.fontMetrics.getHeight();
                        graphics.drawLine(n, n8, n + this.fontMetrics.stringWidth(string), n8);
                    }
                    if (bl2) {
                        n8 = n2 + this.fontMetrics.getHeight() / 2;
                        graphics.drawLine(n, n8, n + this.fontMetrics.stringWidth(string), n8);
                    }
                    bl3 = true;
                } else if (bl5) {
                    int n12 = n10 - n3;
                    string3 = string.substring(0, n12);
                    string2 = string.substring(n12);
                    n9 = n + this.fontMetrics.stringWidth(string3);
                    graphics.setColor(this.m_cHighlight);
                    graphics.fillRect(n, n2, this.fontMetrics.stringWidth(string3), this.fontMetrics.getHeight());
                    graphics.setColor(this.m_cTextHighlight);
                    graphics.drawString(string3, n, n7);
                    graphics.setColor(this.m_cForeground);
                    graphics.drawString(string2, n9, n7);
                    if (bl) {
                        n8 = n2 + this.fontMetrics.getHeight();
                        graphics.drawLine(n, n8, n + this.fontMetrics.stringWidth(string), n8);
                    }
                    if (bl2) {
                        n8 = n2 + this.fontMetrics.getHeight() / 2;
                        graphics.drawLine(n, n8, n + this.fontMetrics.stringWidth(string), n8);
                    }
                    bl3 = false;
                } else if (bl3) {
                    graphics.setColor(this.m_cHighlight);
                    graphics.fillRect(n, n2, this.fontMetrics.stringWidth(string), this.fontMetrics.getHeight());
                    graphics.setColor(this.m_cTextHighlight);
                    graphics.drawString(string, n, n7);
                    if (bl) {
                        int n13 = n2 + this.fontMetrics.getHeight();
                        graphics.drawLine(n, n13, n + this.fontMetrics.stringWidth(string), n13);
                    }
                    if (bl2) {
                        int n14 = n2 + this.fontMetrics.getHeight() / 2;
                        graphics.drawLine(n, n14, n + this.fontMetrics.stringWidth(string), n14);
                    }
                } else if (bl4) {
                    int n15;
                    int n16 = n5 - n3;
                    int n17 = n10 - n3;
                    String string4 = string.substring(0, n16);
                    n9 = n + this.fontMetrics.stringWidth(string4);
                    string3 = string.substring(n16, n17);
                    n8 = n9 + this.fontMetrics.stringWidth(string3);
                    string2 = string.substring(n17);
                    graphics.drawString(string4, n, n7);
                    graphics.setColor(this.m_cHighlight);
                    graphics.fillRect(n9, n2, this.fontMetrics.stringWidth(string3), this.fontMetrics.getHeight());
                    graphics.setColor(this.m_cTextHighlight);
                    graphics.drawString(string3, n9, n7);
                    graphics.setColor(this.getForeground());
                    graphics.drawString(string2, n8, n7);
                    if (bl) {
                        n15 = n2 + this.fontMetrics.getHeight();
                        graphics.drawLine(n, n15, n + this.fontMetrics.stringWidth(string), n15);
                    }
                    if (bl2) {
                        n15 = n2 + this.fontMetrics.getHeight() / 2;
                        graphics.drawLine(n, n15, n + this.fontMetrics.stringWidth(string), n15);
                    }
                } else {
                    graphics.drawString(string, n, n7);
                    if (bl) {
                        int n18 = n2 + this.fontMetrics.getHeight();
                        graphics.drawLine(n, n18, n + this.fontMetrics.stringWidth(string), n18);
                    }
                    if (bl2) {
                        int n19 = n2 + this.fontMetrics.getHeight() / 2;
                        graphics.drawLine(n, n19, n + this.fontMetrics.stringWidth(string), n19);
                    }
                }
            }
            n2 += this.fontMetrics.getHeight();
            n3 += string.length();
            ++n4;
        }
    }

    protected void setHAlign(int n) {
        this.m_nHAlign = n;
    }

    public String toString() {
        String string = new String();
        string = "Paragraph: [text=" + this.buffer.toString() + "||selection state=";
        string = this.selected == NONE ? String.valueOf(string) + "NONE" : (this.selected == 1 ? String.valueOf(string) + "PARTIAL" : String.valueOf(string) + "COMPLETE");
        string = String.valueOf(string) + "||selectionStart=" + this.selectionStart + "||selectionEnd=" + this.selectionEnd;
        return string;
    }

    private Vector wrapText(String text) {
        if (this.fontMetrics == null) {
            if (this.m_fFont == null) {
                return null;
            }

            try {
                Component var2 = (Component) this.getTarget();
                this.fontMetrics = var2.getFontMetrics(this.m_fFont);
            } catch (Exception var7) {
                return null;
            }
        }

        Vector vector = new Vector();
        int start = 0;
        if (text.length() > 0) {
            for (int i = 0; i < text.length(); ++i) {
                String var5 = text.substring(start, i + 1);
                if (this.fontMetrics.stringWidth(var5) > this.m_nWidth - 4) {
                    int j;
                    for (j = i; j > start && text.charAt(j) != ' '; --j) {
                    }

                    if (j == start) {
                        j = i;
                    }

                    if (start != j) {
                        if (j != i || text.charAt(j) == ' ') {
                            ++j;
                        }

                        vector.addElement(text.substring(start, j));
                    }

                    start = j;
                }
            }
        }

        if (start < text.length() || text.length() == NONE) {
            vector.addElement(text.substring(start));
        }

        return vector;
    }
}

