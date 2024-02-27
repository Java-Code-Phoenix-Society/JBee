/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.listbox;

import java.awt.*;
import java.io.Serializable;

public class Column
        implements Serializable {
    protected int m_nCX = 0;
    protected String m_strText = "";
    protected Font m_fonFont = new Font("Dialog", 0, 12);
    protected int m_cchTextMax = 0;
    protected int m_iSubItem = -1;
    protected int m_nFmt = 0;
    protected int m_nHeaderFmt = 0;

    public int getAlignment() {
        return this.m_nFmt;
    }

    public void setAlignment(int n) {
        this.m_nFmt = n;
    }

    public Font getFont() {
        return this.m_fonFont;
    }

    public void setFont(Font font) {
        this.m_fonFont = font;
    }

    public int getHeaderAlignment() {
        return this.m_nHeaderFmt;
    }

    public void setHeaderAlignment(int n) {
        this.m_nHeaderFmt = n;
    }

    public String getText() {
        return this.m_strText;
    }

    public void setText(String string) {
        this.m_strText = string;
    }

    public int getWidth() {
        return this.m_nCX;
    }

    public void setWidth(int n) {
        this.m_nCX = n;
    }
}

