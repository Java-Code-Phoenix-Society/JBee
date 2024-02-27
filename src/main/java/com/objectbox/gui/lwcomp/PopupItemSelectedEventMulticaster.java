/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import java.awt.*;

public class PopupItemSelectedEventMulticaster
        extends AWTEventMulticaster
        implements PopupItemSelectedListener {
    protected PopupItemSelectedEventMulticaster(PopupItemSelectedListener popupItemSelectedListener, PopupItemSelectedListener popupItemSelectedListener2) {
        super(popupItemSelectedListener, popupItemSelectedListener2);
    }

    public static PopupItemSelectedListener add(PopupItemSelectedListener popupItemSelectedListener, PopupItemSelectedListener popupItemSelectedListener2) {
        if (popupItemSelectedListener == null) {
            return popupItemSelectedListener2;
        }
        if (popupItemSelectedListener2 == null) {
            return popupItemSelectedListener;
        }
        return new PopupItemSelectedEventMulticaster(popupItemSelectedListener, popupItemSelectedListener2);
    }

    public static PopupItemSelectedListener remove(PopupItemSelectedListener popupItemSelectedListener, PopupItemSelectedListener popupItemSelectedListener2) {
        return (PopupItemSelectedListener) AWTEventMulticaster.removeInternal(popupItemSelectedListener, popupItemSelectedListener2);
    }

    public void handlePopupSelection(PopupItemSelectedEvent popupItemSelectedEvent) {
        ((PopupItemSelectedListener) this.a).handlePopupSelection(popupItemSelectedEvent);
        ((PopupItemSelectedListener) this.b).handlePopupSelection(popupItemSelectedEvent);
    }
}

