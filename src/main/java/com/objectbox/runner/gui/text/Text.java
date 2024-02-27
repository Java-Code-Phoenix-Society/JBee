/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.text;

import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class Text
        extends OBLabel
        implements Runnable,
        ClipboardOwner {
    public static String endEditing = "End_Editing";
    public String textModified = "textModified";
    public String cursorMovedOutOfBound = "cursorMovedOutOfBound";
    protected Point cursorPoint;
    protected ActionListener actionListener;
    protected TextListener textListener;
    protected TextEvent textEvent = new TextEvent(this, 900);
    protected TextEvent textEventEnter = new TextEvent(this, 900);
    protected ActionEvent actionEventEnter = new ActionEvent(this, 1001, endEditing);
    boolean AllowEnter = false;
    Point selectionStart;
    Point selectionEnd;
    transient Thread m_tCursor;
    Color m_cCursorColor;
    Point cursor;
    boolean m_bAllowHScroll = true;
    boolean m_bEditable = true;
    boolean m_bModified = false;
    boolean m_bBlinkCursor = true;
    transient Clipboard clipboard = new Clipboard("BlendTextComponentClipboard");
    int m_nCursorWidth = 2;

    public Text() {
        this("");
    }

    public Text(String string) {
        super(string);
        this.setBorderStyle(1);
        this.m_inTextInsets.top = 3;
        this.m_inTextInsets.left = 3;
        this.cursorPoint = new Point(0, 0);
        this.cursor = new Point(0, 0);
        this.m_cCursorColor = Color.black;
        this.selectionStart = new Point(0, 0);
        this.selectionEnd = new Point(0, 0);
        this.setAutoWrap(false);
        this.setInsets(this.m_inTextInsets);
        this.setAllowHScroll(true);
        this.enableEvents(16L);
        this.enableEvents(32L);
        this.enableEvents(4L);
        this.enableEvents(8L);
    }

    public static Vector wrapText(String string, int n, boolean bl, FontMetrics fontMetrics) {
        Vector<String> vector = new Vector<String>();
        int n2 = 0;
        int n3 = 0;
        if (bl) {
            int n4 = 0;
            while (n4 < string.length()) {
                if (string.charAt(n4) == '\n' || (n3 += fontMetrics.charWidth(string.charAt(n4))) > n) {
                    int n5 = n4;
                    while (n5 > n2 && string.charAt(n5) != '\n' && string.charAt(n5) != ' ') {
                        --n5;
                    }
                    if (n5 == n2) {
                        n5 = n4;
                    }
                    if (n5 != n2) {
                        vector.addElement(string.substring(n2, n5));
                    }
                    n2 = string.charAt(n5) == '\n' || string.charAt(n5) == ' ' ? n5 + 1 : n5;
                    n3 = n2 < n4 ? fontMetrics.stringWidth(string.substring(n2, n4 + 1)) : fontMetrics.charWidth(string.charAt(n4));
                }
                ++n4;
            }
        } else {
            int n6 = 0;
            while (n6 < string.length()) {
                if (string.charAt(n6) == '\n') {
                    vector.addElement(string.substring(n2, n6));
                    n2 = n6 + 1;
                }
                ++n6;
            }
        }
        if (n2 < string.length()) {
            vector.addElement(string.substring(n2));
        } else {
            vector.addElement(new String());
        }
        return vector;
    }

    public void addActionListener(ActionListener actionListener) {
        this.actionListener = AWTEventMulticaster.add(this.actionListener, actionListener);
    }

    public void addTextListener(TextListener textListener) {
        this.textListener = AWTEventMulticaster.add(this.textListener, textListener);
    }

    protected void copy() {
        if (!this.isSelected()) {
            return;
        }
        String string = this.getSelectedText();
        this.setClipboardText(string);
    }

    protected void cut(boolean bl) {
        Object object;
        Object object2;
        if (!this.isSelected()) {
            return;
        }
        if (bl) {
            object2 = this.getSelectedText();
            this.setClipboardText((String) object2);
        }
        object2 = this.selectionStart;
        Object object3 = this.selectionEnd;
        if (((Point) object3).y < ((Point) object2).y || ((Point) object2).y == ((Point) object3).y && ((Point) object2).x > ((Point) object3).x) {
            object = object2;
            object2 = object3;
            object3 = object;
        }
        object = new String();
        boolean bl2 = true;
        int n = ((Point) object2).y;
        while (n <= ((Point) object3).y) {
            String string;
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n);
            if (n == ((Point) object2).y) {
                string = paragraph.getText();
                if (((Point) object2).x > string.length()) {
                    bl2 = false;
                    break;
                }
                object = String.valueOf(object) + string.substring(0, ((Point) object2).x);
            }
            if (n == ((Point) object3).y) {
                string = paragraph.getText();
                if (((Point) object3).x > string.length()) {
                    bl2 = false;
                    break;
                }
                object = String.valueOf(object) + string.substring(((Point) object3).x);
            }
            ++n;
        }
        if (bl2) {
            ((Paragraph) this.m_vParagraphs.elementAt(((Point) object2).y)).setText((String) object);
            n = ((Point) object2).y + 1;
            while (n <= ((Point) object3).y) {
                this.m_vParagraphs.removeElement((Paragraph) this.m_vParagraphs.elementAt(((Point) object2).y + 1));
                ++n;
            }
        }
        this.unselectAll();
        this.start();
        this.cursorPoint.y = ((Point) object2).y;
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(((Point) object2).y);
        paragraph.moveSpecial(true, paragraph.getCursorPoint().x, 0);
        paragraph.setCursorPos(((Point) object2).x);
        this.y_offset = this.prevYOffset;
        this.repaint();
    }

    public void draw(Graphics graphics) {
        super.draw(graphics);
        if (!this.m_bBlinkCursor) {
            this.paintCursor(graphics);
        }
    }

    protected Paragraph findParagraph(int n, boolean bl) {
        int n2 = 0;
        n += this.y_offset;
        int n3 = 0;
        while (n3 < this.m_vParagraphs.size()) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n3);
            if (n < (n2 += paragraph.getYSpan())) {
                return paragraph;
            }
            ++n3;
        }
        return bl ? (Paragraph) this.m_vParagraphs.lastElement() : null;
    }

    protected int findYCoord(Paragraph paragraph) {
        int n = this.m_inTextInsets.top;
        int n2 = 0;
        while (n2 < this.m_vParagraphs.size()) {
            Paragraph paragraph2 = (Paragraph) this.m_vParagraphs.elementAt(n2);
            if (paragraph2 == paragraph) break;
            n += paragraph2.getYSpan();
            ++n2;
        }
        return n;
    }

    public boolean getAllowHScroll() {
        return this.m_bAllowHScroll && this.getHAlign() == 1 && !this.isAutoWrap();
    }

    public void setAllowHScroll(boolean bl) {
        this.m_bAllowHScroll = bl && this.getHAlign() == 1 && !this.isAutoWrap();
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n);
            paragraph.setAllowScroll(this.m_bAllowHScroll);
            ++n;
        }
    }

    public Clipboard getClipboard() {
        return this.clipboard;
    }

    public void setClipboard(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    public int getCursorPos() {
        return ((Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y)).getCursorPos();
    }

    public void setCursorPos(int n) {
        ((Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y)).setCursorPos(n);
        this.cursorPoint.x = n;
    }

    public int getCursorWidth() {
        return this.m_nCursorWidth;
    }

    public void setCursorWidth(int n) {
        if (n < 0) {
            return;
        }
        this.m_nCursorWidth = n;
    }

    public boolean getModified() {
        return this.m_bModified;
    }

    public String getSelectedText() {
        String string = new String();
        if (!this.isSelected()) {
            return string;
        }
        Point point = this.selectionStart;
        Point point2 = this.selectionEnd;
        if (point2.y < point.y || point.y == point2.y && point.x > point2.x) {
            Point point3 = point;
            point = point2;
            point2 = point3;
        }
        int n = point.y;
        while (n <= point2.y) {
            String string2;
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n);
            if (n == point.y && point.y == point2.y) {
                string2 = paragraph.getText();
                string = String.valueOf(string) + string2.substring(point.x, point2.x);
            } else {
                if (n == point.y) {
                    string2 = paragraph.getText();
                    string = String.valueOf(string) + string2.substring(point.x);
                }
                if (n == point2.y) {
                    string2 = paragraph.getText();
                    string = String.valueOf(string) + string2.substring(0, point2.x);
                }
                if (n != point.y && n != point2.y && n > point.y && n < point2.y) {
                    string = String.valueOf(string) + paragraph.getText();
                }
            }
            string = String.valueOf(string) + "\n";
            ++n;
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

    public int getSelectionEnd() {
        return Math.max(this.selectionStart.x, this.selectionEnd.x);
    }

    public int getSelectionStart() {
        return Math.min(this.selectionStart.x, this.selectionEnd.x);
    }

    protected void gotFocus() {
        if (this.m_bEditable) {
            this.start();
            int n = 0;
            while (n < this.m_vParagraphs.size()) {
                ((Paragraph) this.m_vParagraphs.elementAt(n)).setMarkVisible(true);
                ++n;
            }
            this.repaint();
        }
    }

    protected boolean insertChar(char c) {
        Paragraph paragraph;
        if (c == '\u0000') {
            return false;
        }
        if (this.m_vParagraphs == null || this.cursorPoint.y < 0 || this.cursorPoint.y > this.m_vParagraphs.size() - 1) {
            return false;
        }
        if (this.isSelected()) {
            this.cut(false);
            this.start();
        }
        if ((paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y)).insertChar(c, !this.m_bAllowHScroll && !this.isAutoWrap())) {
            this.processTextEvent(this.textEvent);
            this.update();
            return true;
        }
        return false;
    }

    public boolean isAllowEnter() {
        return false;
    }

    public void setAllowEnter(boolean bl) {
    }

    public boolean isBlinkCursor() {
        return this.m_bBlinkCursor;
    }

    public void setBlinkCursor(boolean bl) {
        this.m_bBlinkCursor = bl;
    }

    public boolean isEditable() {
        return this.m_bEditable;
    }

    public void setEditable(boolean bl) {
        this.m_bEditable = bl;
    }

    public boolean isFocusTraversable() {
        return this.m_bEditable;
    }

    public boolean isModified() {
        return this.m_bModified;
    }

    public void setModified(boolean bl) {
        this.m_bModified = bl;
    }

    private boolean isNotBreakChar(char c) {
        boolean bl = true;
        switch (c) {
            case ' ':
            case '!':
            case ',':
            case '.':
            case ':':
            case ';':
            case '?': {
                bl = false;
            }
        }
        return bl;
    }

    public boolean isSelected() {
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            if (((Paragraph) this.m_vParagraphs.elementAt(n)).getSelectionState() != 0) {
                return true;
            }
            ++n;
        }
        return false;
    }

    protected void lostFocus() {
        if (this.m_bEditable) {
            this.stop();
            int n = 0;
            while (n < this.m_vParagraphs.size()) {
                ((Paragraph) this.m_vParagraphs.elementAt(n)).setMarkVisible(false);
                ++n;
            }
            this.processActionEvent(this.actionEventEnter);
            this.repaint();
        }
    }

    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }

    protected void onBackspaceKey() {
        if (this.isSelected()) {
            this.cut(false);
            this.processTextEvent(this.textEvent);
            this.start();
        } else {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
            if (this.getAllowHScroll() && paragraph.getCursorPos() == 0) {
                if (paragraph.getXOffset() > 0) {
                    paragraph.setXOffset(0);
                    this.repaint();
                }
                return;
            }
            if (paragraph.getCursorPos() == 0) {
                return;
            }
            paragraph.onBackspaceKey();
            this.processTextEvent(this.textEvent);
        }
        this.repaint();
    }

    protected void onDeleteKey() {
        if (this.isSelected()) {
            this.cut(false);
            this.processTextEvent(this.textEvent);
            this.start();
        } else {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
            if (paragraph.isCursorAtEnd()) {
                return;
            }
            paragraph.onDeleteKey();
            this.processTextEvent(this.textEvent);
        }
        this.repaint();
    }

    protected void onDoubleClicked() {
        this.selectWord();
    }

    protected void onDownKey(int n) {
    }

    protected void onEndKey(int n) {
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        int n2 = paragraph.getCursorPos();
        this.stop();
        String string = paragraph.getText();
        paragraph.setCursorPos(string.length());
        if (paragraph.getFontMetrics().stringWidth(string) > this.getSize().width) {
            int n3 = paragraph.getFontMetrics().stringWidth(string) - this.getSize().width + 15;
            paragraph.setXOffset(n3);
        }
        if (n == 1) {
            if (paragraph.getSelectionState() == 0) {
                paragraph.setSelectionState(1);
                paragraph.setSelectionStart(n2);
                this.selectionStart.x = n2;
            }
            this.selectionEnd.x = paragraph.getText().length();
            paragraph.setSelectionEnd(paragraph.getText().length());
        } else {
            if (paragraph.getSelectionState() != 0) {
                paragraph.setSelectionState(0);
            }
            this.start();
        }
        this.repaint();
    }

    protected void onEnterKey() {
        this.processTextEvent(this.textEventEnter);
        this.processActionEvent(this.actionEventEnter);
    }

    protected void onHomeKey(int n) {
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        int n2 = paragraph.getCursorPos();
        this.stop();
        paragraph.setCursorPos(0);
        paragraph.setXOffset(0);
        if (n == 1) {
            if (paragraph.getSelectionState() == 0) {
                paragraph.setSelectionState(1);
                paragraph.setSelectionStart(n2);
                this.selectionStart.x = n2;
            }
            this.selectionEnd.x = 0;
            paragraph.setSelectionEnd(0);
        } else {
            if (paragraph.getSelectionState() != 0) {
                paragraph.setSelectionState(0);
            }
            this.start();
        }
        this.repaint();
    }

    protected void onLeftKey(int n) {
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        int n2 = paragraph.getCursorPos();
        if (n2 == 0) {
            if (paragraph.getXOffset() > 0) {
                this.setXOffset(0);
                this.repaint();
            }
            return;
        }
        paragraph.setCursorPos(--n2);
        if (n == 1) {
            if (paragraph.getSelectionState() == 0) {
                paragraph.setSelectionState(1);
                paragraph.setSelectionStart(n2 + 1);
                this.selectionStart.x = n2 + 1;
            }
            this.selectionEnd.x = n2;
            paragraph.setSelectionEnd(n2);
        } else if (this.isSelected()) {
            paragraph.setSelectionState(0);
            paragraph.setCursorPos(Math.min(this.selectionStart.x, this.selectionEnd.x));
        }
        if (this.m_bAllowHScroll && this.getHAlign() == 1 && !this.isAutoWrap() && paragraph.getCursorPoint().x < this.m_inTextInsets.left + 2) {
            int n3 = paragraph.getXOffset() - this.getSize().width / 2;
            if (n3 < 0) {
                n3 = 0;
            }
            paragraph.setXOffset(n3);
        }
        this.repaint();
    }

    protected void onMovement() {
        this.repaint();
    }

    protected void onRightKey(int n) {
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        int n2 = paragraph.getCursorPos();
        if (n2 == paragraph.getText().length()) {
            return;
        }
        this.stop();
        paragraph.setCursorPos(++n2);
        if (this.m_bAllowHScroll) {
            paragraph.onScrollAction();
        }
        if (n == 1) {
            if (paragraph.getSelectionState() == 0) {
                paragraph.setSelectionState(1);
                paragraph.setSelectionStart(n2 - 1);
                this.selectionStart.x = n2 - 1;
                if (this.m_bAllowHScroll) {
                    paragraph.setPrevXOffset(paragraph.getXOffset());
                }
            }
            this.selectionEnd.x = n2;
            paragraph.setSelectionEnd(n2);
        } else {
            if (paragraph.getSelectionState() != 0) {
                paragraph.setSelectionState(0);
                paragraph.setCursorPos(Math.max(this.selectionStart.x, this.selectionEnd.x));
            }
            this.start();
        }
        this.repaint();
    }

    protected void onTripleClicked() {
        this.selectAll();
    }

    protected void onUpKey(int n) {
    }

    protected void paintCursor(Graphics graphics) {
        int n;
        int n2;
        if (this.m_vParagraphs == null || this.cursorPoint.y < 0 || this.cursorPoint.y > this.m_vParagraphs.size() - 1) {
            return;
        }
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        Point point = paragraph.getCursorPoint();
        if (this.y_offset == 0 && this.m_nVAlign != 1) {
            n2 = this.getSize().height;
            int n3 = 0;
            n = 0;
            while (n < this.m_vParagraphs.size()) {
                n3 += ((Paragraph) this.m_vParagraphs.elementAt(n)).getYSpan();
                ++n;
            }
            if (this.m_nVAlign == 3) {
                point.y += n2 / 2 - n3 / 2;
            } else if (this.m_nVAlign == 2) {
                point.y += n2 - n3 - this.m_inTextInsets.bottom;
            }
        }
        if (this.m_nVAlign == 1) {
            point.y += this.m_inTextInsets.top;
        }
        point.y -= this.y_offset;
        this.getSize();
        n2 = 0;
        while (n2 < this.cursorPoint.y) {
            Paragraph paragraph2 = (Paragraph) this.m_vParagraphs.elementAt(n2);
            if (paragraph2.isVisible()) {
                point.y += paragraph2.getYSpan();
            }
            ++n2;
        }
        point.x += 2;
        Font font = paragraph.getFont();
        if (font == null) {
            font = this.getFont();
        }
        FontMetrics fontMetrics = this.getFontMetrics(font);
        graphics.setColor(this.m_cCursorColor);
        n = fontMetrics.getHeight();
        graphics.fillRect(point.x, point.y, this.m_nCursorWidth, n);
    }

    protected void paintCursor(boolean bl) {
        int n;
        int n2;
        if (this.m_vParagraphs == null || this.cursorPoint.y < 0 || this.cursorPoint.y > this.m_vParagraphs.size() - 1) {
            return;
        }
        Graphics graphics = this.getGraphics();
        if (graphics == null) {
            return;
        }
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        Point point = paragraph.getCursorPoint();
        if (this.y_offset == 0 && this.m_nVAlign != 1) {
            n2 = this.getSize().height;
            int n3 = 0;
            n = 0;
            while (n < this.m_vParagraphs.size()) {
                n3 += ((Paragraph) this.m_vParagraphs.elementAt(n)).getYSpan();
                ++n;
            }
            if (this.m_nVAlign == 3) {
                point.y += n2 / 2 - n3 / 2;
            } else if (this.m_nVAlign == 2) {
                point.y += n2 - n3 - this.m_inTextInsets.bottom;
            }
        }
        if (this.m_nVAlign == 1) {
            point.y += this.m_inTextInsets.top;
        }
        point.y -= this.y_offset;
        this.getSize();
        n2 = 0;
        while (n2 < this.cursorPoint.y) {
            Paragraph paragraph2 = (Paragraph) this.m_vParagraphs.elementAt(n2);
            if (paragraph2.isVisible()) {
                point.y += paragraph2.getYSpan();
            }
            ++n2;
        }
        point.x += 2;
        graphics.setXORMode(this.getBackground());
        Font font = paragraph.getFont();
        if (font == null) {
            font = this.getFont();
        }
        FontMetrics fontMetrics = this.getFontMetrics(font);
        graphics.setColor(this.m_cCursorColor);
        n = fontMetrics.getHeight();
        graphics.fillRect(point.x, point.y, this.m_nCursorWidth, n);
        graphics.setPaintMode();
    }

    protected void paste() {
        Transferable transferable = this.clipboard.getContents(this);
        if (this.isSelected()) {
            this.cut(false);
            this.unselectAll();
            this.start();
        }
        if (transferable != null) {
            try {
                String string = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                StringTokenizer stringTokenizer = new StringTokenizer(string, "\n", false);
                string = stringTokenizer.nextToken();
                Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
                int n = paragraph.getCursorPos();
                String string2 = paragraph.getText();
                String string3 = string2.substring(0, n);
                String string4 = string2.substring(n);
                paragraph.setText(String.valueOf(string3) + string);
                paragraph.setCursorPos(n + string.length());
                while (stringTokenizer.hasMoreTokens()) {
                    String string5 = stringTokenizer.nextToken();
                    paragraph = new Paragraph(this, string5);
                    paragraph.setFont(this.getFont());
                    paragraph.setFontMetrics(this.getFontMetrics(this.getFont()));
                    paragraph.setWidth(this.getSize().width - this.m_nWidthOffset);
                    paragraph.setForeground(this.getForeground());
                    paragraph.setBackground(this.getBackground());
                    paragraph.setInsets(this.m_inTextInsets);
                    this.m_vParagraphs.insertElementAt(paragraph, this.cursorPoint.y + 1);
                    ++this.cursorPoint.y;
                }
                paragraph.setText(String.valueOf(paragraph.getText()) + string4);
                paragraph.setCursorPos(paragraph.getText().length() - string4.length());
                this.processTextEvent(this.textEvent);
                this.repaint();
            } catch (Exception exception) {
                JBLogger.log("Couldn't get contents in format: " + DataFlavor.stringFlavor.getHumanPresentableName());
            }
        }
    }

    protected void processActionEvent(ActionEvent actionEvent) {
        if (this.actionListener != null) {
            this.actionListener.actionPerformed(actionEvent);
        }
    }

    protected void processFocusEvent(FocusEvent focusEvent) {
        super.processFocusEvent(focusEvent);
        if (focusEvent.getID() == 1004) {
            this.gotFocus();
        } else if (focusEvent.getID() == 1005) {
            this.lostFocus();
        }
    }

    protected void processKeyEvent(KeyEvent keyEvent) {
        int n;
        super.processKeyEvent(keyEvent);
        if (keyEvent.isConsumed() || !this.m_bEditable) {
            return;
        }
        if (keyEvent.getID() == 401 && (n = keyEvent.getKeyCode()) != 27) {
            if (n == 9) {
                this.transferFocus();
            } else if (n == 37) {
                this.onLeftKey(keyEvent.getModifiers());
            } else if (n == 39) {
                this.onRightKey(keyEvent.getModifiers());
            } else if (n == 38) {
                this.onUpKey(keyEvent.getModifiers());
            } else if (n == 40) {
                this.onDownKey(keyEvent.getModifiers());
            } else if (n == 36) {
                this.onHomeKey(keyEvent.getModifiers());
            } else if (n == 35) {
                this.onEndKey(keyEvent.getModifiers());
            } else if (n == 8) {
                this.onBackspaceKey();
            } else if (n == 127) {
                this.onDeleteKey();
            } else if (n == 10) {
                this.onEnterKey();
            } else if (n != 33 && n != 34) {
                if (keyEvent.getModifiers() == 2 && n == 67) {
                    this.copy();
                } else if (keyEvent.getModifiers() == 2 && n == 88) {
                    this.cut(true);
                } else if (keyEvent.getModifiers() == 2 && n == 86) {
                    this.paste();
                } else {
                    this.insertChar(keyEvent.getKeyChar());
                }
            }
        }
    }

    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        if (!this.m_bEditable) {
            return;
        }
        Point point = mouseEvent.getPoint();
        if (mouseEvent.getID() == 501) {
            this.requestFocus();
            this.setCursorPosition(point.x, point.y);
            this.unselectAll();
            this.repaint();
            try {
                this.start();
            } catch (Exception exception) {
            }
            if (mouseEvent.getClickCount() == 2) {
                this.onDoubleClicked();
            } else if (mouseEvent.getClickCount() == 3) {
                this.onTripleClicked();
            }
        }
    }

    protected void processMouseMotionEvent(MouseEvent mouseEvent) {
        super.processMouseMotionEvent(mouseEvent);
        if (!this.m_bEditable) {
            return;
        }
        Point point = mouseEvent.getPoint();
        if (mouseEvent.getID() == 506) {
            this.stop();
            Paragraph paragraph = this.findParagraph(point.y, true);
            point.x -= this.m_inTextInsets.left;
            this.selectionEnd.y = this.m_vParagraphs.indexOf(paragraph);
            point.y += this.y_offset;
            int n = 0;
            while (n < this.selectionEnd.y) {
                point.y -= ((Paragraph) this.m_vParagraphs.elementAt(n)).getYSpan();
                ++n;
            }
            if (this.y_offset == 0 && this.m_nVAlign != 1) {
                n = this.getSize().height;
                int n2 = 0;
                int n3 = 0;
                while (n3 < this.m_vParagraphs.size()) {
                    n2 += ((Paragraph) this.m_vParagraphs.elementAt(n3)).getYSpan();
                    ++n3;
                }
                if (this.m_nVAlign == 3) {
                    point.y -= (n - n2) / 2;
                } else if (this.m_nVAlign == 2) {
                    point.y -= n - n2;
                }
            }
            this.selectionEnd.x = paragraph.getCursorPosFromPoint(point);
            paragraph.setCursorPos(this.selectionEnd.x);
            this.cursorPoint.y = this.selectionEnd.y;
            this.cursorPoint.x = this.selectionEnd.x;
            if (this.m_bAllowHScroll) {
                paragraph.onScrollAction();
            }
            Point point2 = new Point(this.selectionStart);
            Point point3 = new Point(this.selectionEnd);
            if (point3.y < point2.y || point3.y == point2.y && point3.x < point2.x) {
                Point point4 = new Point(point2);
                point2 = point3;
                point3 = point4;
            }
            int n4 = 0;
            while (n4 < this.m_vParagraphs.size()) {
                Paragraph paragraph2 = (Paragraph) this.m_vParagraphs.elementAt(n4);
                if (n4 < point2.y || n4 > point3.y) {
                    paragraph2.setSelectionState(0);
                } else if (point2.y == point3.y) {
                    paragraph2.setSelectionState(1);
                    paragraph2.setSelectionStart(point2.x);
                    paragraph2.setSelectionEnd(point3.x);
                } else {
                    int n5 = point2.y;
                    while (n5 <= point3.y) {
                        Paragraph paragraph3 = (Paragraph) this.m_vParagraphs.elementAt(n5);
                        if (n5 == point2.y) {
                            paragraph3.setSelectionState(1);
                            paragraph3.setSelectionStart(point2.x);
                            paragraph3.setSelectionEnd(paragraph3.getText().length());
                        } else if (n5 == point3.y) {
                            paragraph3.setSelectionState(1);
                            paragraph3.setSelectionStart(0);
                            paragraph3.setSelectionEnd(point3.x);
                        } else {
                            paragraph3.setSelectionState(2);
                        }
                        ++n5;
                    }
                    n4 = point3.y;
                }
                ++n4;
            }
            this.repaint();
        }
    }

    protected void processTextEvent(TextEvent textEvent) {
        if (textEvent.equals(this.textEvent)) {
            this.setModified(true);
        }
        if (this.textListener != null) {
            this.textListener.textValueChanged(textEvent);
        }
    }

    public void removeActionListener(ActionListener actionListener) {
        this.actionListener = AWTEventMulticaster.remove(this.actionListener, actionListener);
    }

    public void removeTextListener(TextListener textListener) {
        this.textListener = AWTEventMulticaster.remove(this.textListener, textListener);
    }

    public void run() {
        boolean bl = true;
        while (this.m_bBlinkCursor) {
            this.paintCursor(bl);
            bl = !bl;
            try {
                Thread.sleep(500L);
            } catch (InterruptedException interruptedException) {
                this.stop();
            }
        }
    }

    public void select(int n, int n2) {
        if (this.m_vParagraphs == null || this.m_vParagraphs.size() == 0) {
            return;
        }
        this.stop();
        this.selectionStart = new Point(n, 0);
        this.selectionEnd = new Point(n2, 0);
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.firstElement();
        paragraph.setSelectionStart(n);
        paragraph.setSelectionEnd(n2);
        paragraph.setCursorPos(n2);
        if (n == n2) {
            paragraph.setSelectionState(0);
            this.start();
        } else {
            paragraph.setSelectionState(1);
        }
        this.repaint();
    }

    public void selectAll() {
        this.stop();
        this.selectionStart = new Point(0, 0);
        this.selectionEnd = new Point(((Paragraph) this.m_vParagraphs.lastElement()).getText().length(), this.m_vParagraphs.size() - 1);
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n);
            paragraph.setSelectionState(2);
            paragraph.setMarkVisible(true);
            ++n;
        }
        this.repaint();
    }

    public void selectWord() {
        Paragraph paragraph = (Paragraph) super.m_vParagraphs.elementAt(this.cursorPoint.y);
        String text = paragraph.getText();
        if (text.length() != 0) {
            this.stop();
            int x = this.cursorPoint.x;
            int x1 = this.cursorPoint.x;
            if (x1 == 0) {
                x1 = 1;
            }

            if (x <= text.length()) {
                while (x > 0 && this.isNotBreakChar(text.charAt(x - 1))) {
                    --x;
                }

                while (x1 < text.length() && this.isNotBreakChar(text.charAt(x1))) {
                    ++x1;
                }

                this.selectionStart = new Point(x, this.cursorPoint.y);
                this.selectionEnd = new Point(x1, this.cursorPoint.y);
                paragraph.setSelectionState(1);
                paragraph.setSelectionStart(x);
                paragraph.setSelectionEnd(x1);
                this.repaint();
            }
        }
    }

    protected void setClipboardText(String string) {
        if (string != null) {
            StringSelection stringSelection = new StringSelection(string);
            this.clipboard.setContents(stringSelection, this);
        }
    }

    protected void setCursorPosition(int n, int n2) {
        int n3;
        n2 -= this.m_inTextInsets.top;
        if (this.y_offset == 0 && this.m_nVAlign != 1) {
            int n4 = this.getSize().height;
            int n5 = 0;
            n3 = 0;
            while (n3 < this.m_vParagraphs.size()) {
                n5 += ((Paragraph) this.m_vParagraphs.elementAt(n3)).getYSpan();
                ++n3;
            }
            if (this.m_nVAlign == 3) {
                n2 -= (n4 - n5) / 2;
            } else if (this.m_nVAlign == 2) {
                n2 -= n4 - n5;
            }
        }
        Point point = new Point(n, n2);
        Paragraph paragraph = this.findParagraph(point.y, true);
        this.cursorPoint.y = this.m_vParagraphs.indexOf(paragraph);
        point.y -= this.m_inTextInsets.top;
        point.y += this.y_offset;
        n3 = 0;
        while (n3 < this.cursorPoint.y) {
            point.y -= ((Paragraph) this.m_vParagraphs.elementAt(n3)).getYSpan();
            ++n3;
        }
        this.cursorPoint.x = paragraph.getCursorPosFromPoint(point);
        paragraph.setCursorPos(this.cursorPoint.x);
        this.selectionStart.x = this.cursorPoint.x;
        this.selectionStart.y = this.cursorPoint.y;
        this.selectionEnd.x = this.cursorPoint.x;
        this.selectionEnd.y = this.cursorPoint.y;
    }

    public void setHAlign(int n) {
        if (n != 1) {
            this.setAllowHScroll(false);
        }
        super.setHAlign(n);
    }

    public void setText(String string, boolean bl) {
        if (string == null) {
            string = "";
        }
        if (this.m_vParagraphs.size() > 0) {
            this.m_vParagraphs.removeAllElements();
        }
        Paragraph paragraph = new Paragraph(this, string);
        paragraph.setFont(this.getFont());
        paragraph.setFontMetrics(this.getFontMetrics(this.getFont()));
        paragraph.setBackground(this.getBackground());
        paragraph.setForeground(this.getForeground());
        paragraph.setWidth(this.getSize().width - this.m_nWidthOffset);
        paragraph.setInsets(this.m_inTextInsets);
        paragraph.setHAlign(this.getHAlign());
        paragraph.setAutoWrap(this.isAutoWrap());
        paragraph.setAllowScroll(this.getAllowHScroll());
        this.m_vParagraphs.addElement(paragraph);
        if (bl) {
            this.repaint();
        }
    }

    public void setXOffset(int n) {
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        paragraph.setXOffset(n);
    }

    public void start() {
        if (this == null) {
            return;
        }
        if (this.m_tCursor == null) {
            this.m_tCursor = new Thread(this);
            this.m_tCursor.start();
        }
    }

    public void stop() {
        if (this.m_tCursor != null) {
            this.m_tCursor.stop();
            this.m_tCursor = null;
        }
    }

    protected void unselectAll() {
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            ((Paragraph) this.m_vParagraphs.elementAt(n)).setSelectionState(0);
            ++n;
        }
    }

    public void update() {
        this.repaint();
    }
}

