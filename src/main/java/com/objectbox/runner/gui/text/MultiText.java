/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.text;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

public class MultiText
        extends Text {
    Hashtable m_htUpDownPositions = new Hashtable();
    boolean m_bAllowEnter = true;
    boolean m_bAllowVScroll = true;
    int m_nLastXPosition = -1;

    public MultiText() {
        this("");
    }

    public MultiText(String string) {
        super(string);
    }

    public boolean getAllowSpace() {
        return false;
    }

    public boolean getAllowVScroll() {
        return this.m_bAllowVScroll;
    }

    public void setAllowVScroll(boolean bl) {
        this.m_bAllowVScroll = bl;
    }

    public int getCursorPos() {
        int n = 0;
        int n2 = 0;
        while (n2 < this.cursorPoint.y) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n2);
            n += paragraph.getText().length() + 1;
            ++n2;
        }
        return n + ((Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y)).getCursorPos();
    }

    protected boolean insertChar(char c) {
        Paragraph paragraph;
        if (c == '\u0000') {
            return false;
        }
        if (this.m_vParagraphs == null || this.cursorPoint.y < 0 || this.cursorPoint.y > this.m_vParagraphs.size() - 1) {
            return false;
        }
        this.m_htUpDownPositions.clear();
        this.m_nLastXPosition = -1;
        if (this.isSelected()) {
            this.cut(false);
            if (this.textListener != null) {
                this.textListener.textValueChanged(this.textEvent);
            }
            if (this.m_bAllowVScroll) {
                this.onVerticalScroll();
            }
            this.start();
        }
        if ((paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y)).insertChar(c, !this.isAutoWrap() && !this.m_bAllowHScroll)) {
            if (this.textListener != null) {
                this.textListener.textValueChanged(this.textEvent);
            }
            if (this.m_bAllowVScroll) {
                this.onVerticalScroll();
            }
            this.update();
            return true;
        }
        return false;
    }

    public boolean isAllowEnter() {
        return this.m_bAllowEnter && this.getVAlign() == 1;
    }

    public void setAllowEnter(boolean bl) {
        this.m_bAllowEnter = bl;
    }

    public boolean isCursorOnEndPos() {
        if (this.m_vParagraphs.size() <= this.cursorPoint.y) {
            return true;
        }
        if (this.m_vParagraphs.size() - 1 == this.cursorPoint.y) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
            if (super.getCursorPos() >= paragraph.getText().length()) {
                return true;
            }
        }
        return false;
    }

    protected int nextCursorPos(int n, int n2) {
        if (this.m_htUpDownPositions.size() == 0) {
            return -1;
        }
        if (this.m_htUpDownPositions.containsKey("" + n)) {
            int n3 = -1;
            Vector vector = (Vector) this.m_htUpDownPositions.get("" + n);
            int n4 = 0;
            while (n4 < vector.size()) {
                Integer n5 = (Integer) vector.elementAt(n4);
                int n6 = n5;
                if (n6 > n2) {
                    n3 = n3 == -1 ? n6 : Math.min(n3, n6);
                }
                ++n4;
            }
            return n3;
        }
        return -1;
    }

    protected void onBackspaceKey() {
        this.m_htUpDownPositions.clear();
        if (this.isSelected()) {
            this.cut(false);
            if (this.textListener != null) {
                this.textListener.textValueChanged(this.textEvent);
            }
            this.start();
        } else {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
            if (!this.isAutoWrap() && paragraph.getCursorPos() == 0) {
                if (paragraph.getXOffset() > 0) {
                    this.setXOffset(0);
                    this.repaint();
                    return;
                }
                if (this.cursorPoint.y == 0) {
                    return;
                }
            } else if (this.cursorPoint.y == 0 && paragraph.getCursorPos() == 0) {
                return;
            }
            this.stop();
            if (paragraph.getCursorPos() == 0) {
                String string = paragraph.getText();
                Paragraph paragraph2 = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y - 1);
                paragraph2.setCursorPos(paragraph2.getText().length());
                paragraph2.setText(String.valueOf(paragraph2.getText()) + string);
                this.m_vParagraphs.removeElement(paragraph);
                --this.cursorPoint.y;
            } else {
                paragraph.onBackspaceKey();
            }
            if (this.textListener != null) {
                this.textListener.textValueChanged(this.textEvent);
            }
            this.start();
        }
        if (this.m_bAllowVScroll) {
            this.onVerticalScroll();
        }
        this.repaint();
    }

    protected void onDeleteKey() {
        this.m_htUpDownPositions.clear();
        if (this.isSelected()) {
            this.cut(false);
            if (this.textListener != null) {
                this.textListener.textValueChanged(this.textEvent);
            }
            this.start();
        } else {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
            if (paragraph.isCursorAtEnd() && this.cursorPoint.y >= this.m_vParagraphs.size() - 1) {
                return;
            }
            if (paragraph.isCursorAtEnd()) {
                String string = paragraph.getText();
                Paragraph paragraph2 = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y + 1);
                string = String.valueOf(string) + paragraph2.getText();
                this.m_vParagraphs.removeElement(paragraph2);
                paragraph.setText(string);
            } else {
                paragraph.onDeleteKey();
            }
            if (this.textListener != null) {
                this.textListener.textValueChanged(this.textEvent);
            }
        }
        if (this.m_bAllowVScroll) {
            this.onVerticalScroll();
        }
        this.repaint();
    }

    protected void onDownKey(int n) {
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        if (paragraph.isCursorOnLastLine() && this.cursorPoint.y == this.m_vParagraphs.size() - 1) {
            this.onEndKey(n);
            return;
        }
        this.stop();
        if (paragraph.isCursorOnLastLine()) {
            this.storePosition(this.cursorPoint.y, paragraph.getCursorPos());
            int n2 = 0;
            if (this.m_nLastXPosition == -1) {
                this.m_nLastXPosition = n2 = paragraph.getCursorPoint().x;
            } else {
                n2 = this.m_nLastXPosition;
            }
            ++this.cursorPoint.y;
            Paragraph paragraph2 = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
            int n3 = this.nextCursorPos(this.cursorPoint.y, 0);
            if (n3 == -1) {
                paragraph2.moveSpecial(false, n2, 40);
            } else {
                paragraph2.setCursorPos(n3);
            }
            if (n == 1) {
                if (paragraph.getSelectionState() == 0) {
                    paragraph.setSelectionState(1);
                    paragraph.setSelectionStart(paragraph.getCursorPos());
                    this.selectionStart.x = paragraph.getCursorPos();
                    this.selectionStart.y = this.cursorPoint.y - 1;
                    if (this.m_bAllowVScroll) {
                        this.prevYOffset = this.y_offset;
                    }
                }
                paragraph.setSelectionEnd(paragraph.getText().length());
                if (paragraph2.getSelectionState() == 0) {
                    paragraph2.setSelectionState(1);
                    paragraph2.setSelectionStart(0);
                }
                this.selectionEnd.x = paragraph2.getCursorPos();
                this.selectionEnd.y = this.cursorPoint.y;
                paragraph2.setSelectionEnd(paragraph2.getCursorPos());
            } else {
                if (paragraph.getSelectionState() != 0) {
                    this.unselectAll();
                }
                this.start();
            }
        } else {
            int n4 = paragraph.getCursorPos();
            this.storePosition(this.cursorPoint.y, n4);
            int n5 = this.nextCursorPos(this.cursorPoint.y, n4);
            if (n5 == -1) {
                paragraph.onDownKey();
            } else {
                paragraph.setCursorPos(n5);
            }
            int n6 = paragraph.getCursorPos();
            if (n == 1) {
                if (paragraph.getSelectionState() == 0) {
                    paragraph.setSelectionState(1);
                    paragraph.setSelectionStart(n4);
                    this.selectionStart.x = n4;
                    this.selectionStart.y = this.cursorPoint.y;
                    if (this.m_bAllowVScroll) {
                        this.prevYOffset = this.y_offset;
                    }
                }
                this.selectionEnd.x = n6;
                this.selectionEnd.y = this.cursorPoint.y;
                paragraph.setSelectionEnd(n6);
            } else {
                if (paragraph.getSelectionState() != 0) {
                    this.unselectAll();
                }
                this.start();
            }
        }
        if (this.m_bAllowVScroll) {
            this.onVerticalScroll();
        }
        this.repaint();
    }

    protected void onEndKey(int n) {
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        this.m_htUpDownPositions.clear();
        this.m_nLastXPosition = -1;
        this.stop();
        int n2 = paragraph.getCursorPos();
        paragraph.onEndKey();
        int n3 = paragraph.getCursorPos();
        if (n == 1) {
            if (paragraph.getSelectionState() == 0) {
                paragraph.setSelectionState(1);
                paragraph.setSelectionStart(n2);
                this.selectionStart.x = n2;
                this.selectionStart.y = this.cursorPoint.y;
            }
            this.selectionEnd.x = n3;
            this.selectionEnd.y = this.cursorPoint.y;
            paragraph.setSelectionEnd(n3);
        } else {
            if (paragraph.getSelectionState() != 0) {
                this.unselectAll();
            }
            this.start();
        }
        this.repaint();
    }

    protected void onEnterKey() {
        if (!this.isAllowEnter()) {
            super.onEnterKey();
            return;
        }
        if (this.isSelected()) {
            this.cut(false);
            if (this.textListener != null) {
                this.textListener.textValueChanged(this.textEvent);
            }
            this.start();
        }
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        String string = paragraph.onEnterKey();
        this.m_htUpDownPositions.clear();
        Paragraph paragraph2 = string == null || string.length() < 1 ? new Paragraph(this, "") : new Paragraph(this, string);
        paragraph2.setFont(this.getFont());
        paragraph2.setFontMetrics(this.getFontMetrics(this.getFont()));
        paragraph2.setWidth(this.getSize().width - this.m_inTextInsets.left - this.m_inTextInsets.right);
        paragraph2.setForeground(this.getForeground());
        paragraph2.setBackground(this.getBackground());
        paragraph2.setInsets(this.m_inTextInsets);
        paragraph2.setHAlign(this.m_nHAlign);
        paragraph2.setAllowScroll(this.getAllowHScroll());
        paragraph2.setAutoWrap(this.isAutoWrap());
        this.m_vParagraphs.insertElementAt(paragraph2, this.cursorPoint.y + 1);
        ++this.cursorPoint.y;
        paragraph2.setCursorPos(0);
        if (this.m_bAllowVScroll) {
            this.onVerticalScroll();
        }
        this.setXOffset(0);
        if (this.textListener != null) {
            this.textListener.textValueChanged(this.textEvent);
        }
        this.repaint();
    }

    protected void onHomeKey(int n) {
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        this.m_htUpDownPositions.clear();
        this.m_nLastXPosition = -1;
        this.stop();
        int n2 = paragraph.getCursorPos();
        paragraph.onHomeKey();
        int n3 = paragraph.getCursorPos();
        if (n == 1) {
            if (paragraph.getSelectionState() == 0) {
                paragraph.setSelectionState(1);
                paragraph.setSelectionStart(n2);
                this.selectionStart.x = n2;
                this.selectionStart.y = this.cursorPoint.y;
            }
            this.selectionEnd.x = n3;
            this.selectionEnd.y = this.cursorPoint.y;
            paragraph.setSelectionEnd(n3);
        } else {
            if (paragraph.getSelectionState() != 0) {
                this.unselectAll();
            }
            this.start();
        }
        this.repaint();
    }

    protected void onLeftKey(int key) {
        Paragraph paragraph = (Paragraph) super.m_vParagraphs.elementAt(super.cursorPoint.y);
        int cursorPos = paragraph.getCursorPos();
        if (cursorPos == 0) {
            if (paragraph.getXOffset() > 0) {
                this.setXOffset(0);
                this.repaint();
                return;
            }

            if (super.cursorPoint.y == 0) {
                return;
            }
        }

        this.m_htUpDownPositions.clear();
        this.m_nLastXPosition = -1;
        this.stop();
        Point selectionStart1 = super.selectionStart;
        Point selectionEnd1 = super.selectionEnd;
        if (selectionEnd1.y < selectionStart1.y || selectionEnd1.y == selectionStart1.y && selectionEnd1.x < selectionStart1.x) {
            selectionStart1 = selectionEnd1;
        }

        if (cursorPos == 0) {
            --super.cursorPoint.y;
            Paragraph paragraph1 = (Paragraph) super.m_vParagraphs.elementAt(super.cursorPoint.y);
            paragraph1.moveSpecial(true, this.getSize().width, 37);
            if (key == 1) {
                if (paragraph.getSelectionState() == 0) {
                    paragraph.setSelectionState(1);
                    paragraph.setSelectionStart(0);
                    super.selectionStart.y = super.cursorPoint.y + 1;
                    super.selectionStart.x = 0;
                }

                if (paragraph1.getSelectionState() == 0) {
                    paragraph1.setSelectionState(1);
                    paragraph1.setSelectionStart(paragraph1.getCursorPos());
                }

                int selLen = paragraph1.getText().length() > 0 ? paragraph1.getCursorPos() - 1 : paragraph1.getCursorPos();
                paragraph1.setCursorPos(selLen);
                super.selectionEnd.x = selLen;
                super.selectionEnd.y = super.cursorPoint.y;
                paragraph1.setSelectionEnd(selLen);
            } else {
                if (this.isSelected()) {
                    super.cursorPoint.y = selectionStart1.y;
                    super.cursorPoint.x = selectionStart1.x;
                    ((Paragraph) super.m_vParagraphs.elementAt(super.cursorPoint.y)).setCursorPos(selectionStart1.x);
                    this.unselectAll();
                }

                this.start();
            }
        } else {
            --cursorPos;
            paragraph.setCursorPos(cursorPos);
            if (key == 1) {
                if (paragraph.getSelectionState() == 0) {
                    paragraph.setSelectionState(1);
                    paragraph.setSelectionStart(cursorPos + 1);
                    super.selectionStart.y = super.cursorPoint.y;
                    super.selectionStart.x = cursorPos + 1;
                }

                super.selectionEnd.x = cursorPos;
                super.selectionEnd.y = super.cursorPoint.y;
                paragraph.setSelectionEnd(cursorPos);
            } else {
                if (this.isSelected()) {
                    super.cursorPoint.y = selectionStart1.y;
                    super.cursorPoint.x = selectionStart1.x;
                    ((Paragraph) super.m_vParagraphs.elementAt(super.cursorPoint.y)).setCursorPos(selectionStart1.x);
                    this.unselectAll();
                }

                this.start();
            }

            if (!this.isAutoWrap() && paragraph.getCursorPoint().x < super.m_inTextInsets.left + 2) {
                int xOffset = paragraph.getXOffset() - this.getSize().width / 2;
                if (xOffset < 0) {
                    xOffset = 0;
                }

                this.setXOffset(xOffset);
            }
        }

        if (this.m_bAllowVScroll) {
            this.onVerticalScroll();
        }

        this.repaint();
    }

    protected void onRightKey(int key) {
        Paragraph paragraph = (Paragraph) super.m_vParagraphs.elementAt(super.cursorPoint.y);
        int cursorPos = paragraph.getCursorPos();
        if (super.cursorPoint.y != super.m_vParagraphs.size() - 1 || cursorPos != paragraph.getText().length()) {
            this.m_htUpDownPositions.clear();
            this.m_nLastXPosition = -1;
            this.stop();
            Point start = super.selectionStart;
            Point end = super.selectionEnd;
            if (end.y < start.y || end.y == start.y && end.x < start.x) {
                end = start;
            }

            if (cursorPos == paragraph.getText().length()) {
                ++super.cursorPoint.y;
                Paragraph paragraph1 = (Paragraph) super.m_vParagraphs.elementAt(super.cursorPoint.y);
                paragraph1.moveSpecial(false, 0, 39);
                if (key == 1) {
                    if (paragraph.getSelectionState() == 0) {
                        paragraph.setSelectionState(1);
                        paragraph.setSelectionStart(cursorPos);
                        super.selectionStart.x = cursorPos;
                        super.selectionStart.y = super.cursorPoint.y - 1;
                    }

                    if (paragraph1.getSelectionState() == 0) {
                        paragraph1.setSelectionState(1);
                        paragraph1.setSelectionStart(0);
                    }

                    int selLen = paragraph1.getText().length() > 0 ? 1 : 0;
                    super.selectionEnd.x = selLen;
                    super.selectionEnd.y = super.cursorPoint.y;
                    paragraph1.setCursorPos(selLen);
                    paragraph1.setSelectionEnd(selLen);
                } else {
                    if (this.isSelected()) {
                        super.cursorPoint.y = end.y;
                        super.cursorPoint.x = end.x;
                        ((Paragraph) super.m_vParagraphs.elementAt(super.cursorPoint.y)).setCursorPos(end.x);
                        this.unselectAll();
                    }

                    this.start();
                }
            } else {
                ++cursorPos;
                paragraph.setCursorPos(cursorPos);
                if (key == 1) {
                    if (paragraph.getSelectionState() == 0) {
                        paragraph.setSelectionState(1);
                        paragraph.setSelectionStart(cursorPos - 1);
                        super.selectionStart.x = cursorPos - 1;
                        super.selectionStart.y = super.cursorPoint.y;
                    }

                    super.selectionEnd.x = cursorPos;
                    super.selectionEnd.y = super.cursorPoint.y;
                    paragraph.setSelectionEnd(cursorPos);
                } else {
                    if (this.isSelected()) {
                        super.cursorPoint.y = end.y;
                        super.cursorPoint.x = end.x;
                        ((Paragraph) super.m_vParagraphs.elementAt(super.cursorPoint.y)).setCursorPos(end.x);
                        this.unselectAll();
                    }

                    this.start();
                }
            }

            if (this.m_bAllowVScroll) {
                this.onVerticalScroll();
            }

            this.repaint();
        }
    }

    protected void onUpKey(int n) {
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        if (paragraph.isCursorOnFirstLine() && this.cursorPoint.y == 0) {
            this.onHomeKey(n);
            return;
        }
        this.stop();
        if (paragraph.isCursorOnFirstLine()) {
            int n2 = 0;
            if (this.m_nLastXPosition == -1) {
                this.m_nLastXPosition = n2 = paragraph.getCursorPoint().x;
            } else {
                n2 = this.m_nLastXPosition;
            }
            this.storePosition(this.cursorPoint.y, paragraph.getCursorPos());
            --this.cursorPoint.y;
            Paragraph paragraph2 = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
            int n3 = this.prevCursorPos(this.cursorPoint.y, paragraph2.getText().length());
            if (n3 == -1) {
                paragraph2.moveSpecial(true, n2, 38);
            } else {
                paragraph2.setCursorPos(n3);
            }
            if (n == 1) {
                if (paragraph.getSelectionState() == 0) {
                    paragraph.setSelectionState(1);
                    paragraph.setSelectionStart(paragraph.getCursorPos());
                    this.selectionStart.x = paragraph.getCursorPos();
                    this.selectionStart.y = this.cursorPoint.y + 1;
                }
                paragraph.setSelectionEnd(0);
                if (paragraph2.getSelectionState() == 0) {
                    paragraph2.setSelectionState(1);
                    paragraph2.setSelectionStart(paragraph2.getText().length());
                }
                this.selectionEnd.x = paragraph2.getCursorPos();
                this.selectionEnd.y = this.cursorPoint.y;
                paragraph2.setSelectionEnd(paragraph2.getCursorPos());
            } else {
                if (paragraph.getSelectionState() != 0) {
                    this.unselectAll();
                }
                this.start();
            }
        } else {
            int n4 = paragraph.getCursorPos();
            this.storePosition(this.cursorPoint.y, n4);
            int n5 = this.prevCursorPos(this.cursorPoint.y, n4);
            if (n5 == -1) {
                paragraph.onUpKey();
            } else {
                paragraph.setCursorPos(n5);
            }
            int n6 = paragraph.getCursorPos();
            if (n == 1) {
                if (paragraph.getSelectionState() == 0) {
                    paragraph.setSelectionState(1);
                    paragraph.setSelectionStart(n4);
                    this.selectionStart.x = n4;
                    this.selectionStart.y = this.cursorPoint.y;
                }
                this.selectionEnd.x = n6;
                this.selectionEnd.y = this.cursorPoint.y;
                paragraph.setSelectionEnd(n6);
            } else {
                if (paragraph.getSelectionState() != 0) {
                    this.unselectAll();
                }
                this.start();
            }
        }
        if (this.m_bAllowVScroll) {
            this.onVerticalScroll();
            this.prevYOffset = this.y_offset;
        }
        this.repaint();
    }

    protected void onVerticalScroll() {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        while (n3 <= this.cursorPoint.y) {
            if ((n += ((Paragraph) this.m_vParagraphs.elementAt(n3)).getYSpan()) > this.y_offset) {
                n2 = n - this.y_offset;
            }
            ++n3;
        }
        Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(this.cursorPoint.y);
        n2 = n2 - paragraph.getYSpan() + paragraph.getCursorPoint().y;
        int n4 = 2;
        if (this.m_nBorderStyle == 0) {
            n4 = 0;
        } else if (this.m_nBorderStyle == 3) {
            n4 = 1;
        }
        if (n2 + paragraph.getFontMetrics().getHeight() + this.m_inTextInsets.top + this.m_inTextInsets.bottom + 2 * n4 > this.getSize().height) {
            this.y_offset += paragraph.getFontMetrics().getHeight();
        } else if (n2 < 0 && this.y_offset > 0) {
            this.y_offset -= paragraph.getFontMetrics().getHeight();
        }
    }

    protected int prevCursorPos(int n, int n2) {
        if (this.m_htUpDownPositions.size() == 0) {
            return -1;
        }
        if (this.m_htUpDownPositions.containsKey("" + n)) {
            int n3 = -1;
            Vector vector = (Vector) this.m_htUpDownPositions.get("" + n);
            int n4 = 0;
            while (n4 < vector.size()) {
                Integer n5 = (Integer) vector.elementAt(n4);
                int n6 = n5;
                if (n6 < n2) {
                    n3 = Math.max(n3, n6);
                }
                ++n4;
            }
            return n3;
        }
        return -1;
    }

    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
        if (mouseEvent.getID() == 501) {
            this.m_htUpDownPositions.clear();
            this.m_nLastXPosition = -1;
        }
    }

    protected void processMouseMotionEvent(MouseEvent mouseEvent) {
        mouseEvent.getPoint();
        if (mouseEvent.getID() == 506) {
            super.processMouseMotionEvent(mouseEvent);
            this.onVerticalScroll();
        } else {
            super.processMouseMotionEvent(mouseEvent);
        }
    }

    public void setAllowHScroll(boolean bl) {
        this.m_bAllowHScroll = bl && this.getHAlign() == 1 && this.getVAlign() == 1 && !this.isAutoWrap();
        int n = 0;
        while (n < this.m_vParagraphs.size()) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n);
            paragraph.setAllowScroll(this.m_bAllowHScroll);
            ++n;
        }
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
            paragraph.setAllowScroll(this.getAllowHScroll());
            paragraph.setAutoWrap(this.isAutoWrap());
            this.m_vParagraphs.addElement(paragraph);
            this.cursorPoint = new Point(0, 0);
            this.selectionStart = new Point(0, 0);
            this.selectionEnd = new Point(0, 0);
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
                paragraph.setAllowScroll(this.getAllowHScroll());
                paragraph.setAutoWrap(this.isAutoWrap());
                this.m_vParagraphs.addElement(paragraph);
            }
        }
        this.y_offset = 0;
        this.cursorPoint = new Point(0, 0);
        this.selectionStart = new Point(0, 0);
        this.selectionEnd = new Point(0, 0);
        if (bl) {
            this.repaint();
        }
    }

    public void setVAlign(int n) {
        if (n != 1) {
            this.setAllowHScroll(false);
        }
        super.setVAlign(n);
    }

    public void setXOffset(int n) {
        int n2 = this.m_vParagraphs.size();
        int n3 = 0;
        while (n3 < n2) {
            Paragraph paragraph = (Paragraph) this.m_vParagraphs.elementAt(n3);
            paragraph.setXOffset(n);
            ++n3;
        }
    }

    protected void storePosition(int n, int n2) {
        Vector vector = this.m_htUpDownPositions.containsKey("" + n) ? (Vector) this.m_htUpDownPositions.get("" + n) : new Vector();
        vector.addElement(new Integer(n2));
        this.m_htUpDownPositions.put("" + n, vector);
    }
}

