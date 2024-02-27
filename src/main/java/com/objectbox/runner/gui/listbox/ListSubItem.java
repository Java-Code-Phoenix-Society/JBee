/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.listbox;

import java.awt.*;
import java.io.Serializable;

public class ListSubItem
        implements Serializable {
    String pszText;
    int cchTextMax;
    Rectangle rcText;

    public ListSubItem() {
        this.pszText = " ";
        this.cchTextMax = 0;
        this.rcText = new Rectangle();
    }

    public ListSubItem(String string) {
        this.pszText = string;
        this.cchTextMax = 0;
        this.rcText = new Rectangle();
    }

    public String getText() {
        return this.pszText;
    }

    public void setText(String string) {
        this.pszText = string;
    }
}

