/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.text;

import java.awt.*;

public class ExpandableText
        extends MultiText {
    public int m_nLookAhead = 15;
    protected int m_nFixedWidth = 100;

    public ExpandableText() {
        this("");
    }

    public ExpandableText(String string) {
        super(string);
        this.setAutoWrap(true);
        this.setAllowEnter(false);
        this.setInsets(new Insets(0, 0, 0, 0));
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
        if (n2 + this.m_inTextInsets.top + this.m_inTextInsets.bottom + 2 * n4 > this.getSize().height) {
            this.y_offset += paragraph.getFontMetrics().getHeight();
        } else if (n2 < 0 && this.y_offset > 0) {
            this.y_offset -= paragraph.getFontMetrics().getHeight();
        }
    }

    public void setFixedWidth(int n) {
        this.m_nFixedWidth = n;
    }

    public void sizeToFit() {
        String string = this.getText();
        Font font = this.getFont() != null ? this.getFont() : this.defaultfont;
        FontMetrics fontMetrics = this.getFontMetrics(font);
        int n = fontMetrics.stringWidth(string);
        if (this.m_nFixedWidth > 0 && n + this.m_nLookAhead > this.m_nFixedWidth) {
            n = this.m_nFixedWidth;
            this.setAutoWrap(true);
        } else {
            this.setAutoWrap(false);
            n += this.m_nLookAhead;
        }
        if (this.getBounds().width != n) {
            this.setBounds(this.getBounds().x, this.getBounds().y, n, this.getBounds().height);
        }
    }

    public void update() {
        this.sizeToFit();
        super.update();
    }
}

