/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import java.awt.*;

public class FocusLabel
        extends Label {
    public FocusLabel() {
    }

    public FocusLabel(String string) {
        super(string);
    }

    public FocusLabel(String string, int n) {
        super(string, n);
    }

    public boolean isFocusTraversable() {
        return true;
    }
}

