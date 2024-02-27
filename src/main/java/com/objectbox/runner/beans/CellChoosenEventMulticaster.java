/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

import java.awt.*;

public class CellChoosenEventMulticaster
        extends AWTEventMulticaster
        implements CellChoosenListener {
    protected CellChoosenEventMulticaster(CellChoosenListener cellChoosenListener, CellChoosenListener cellChoosenListener2) {
        super(cellChoosenListener, cellChoosenListener2);
    }

    public static CellChoosenListener add(CellChoosenListener cellChoosenListener, CellChoosenListener cellChoosenListener2) {
        if (cellChoosenListener == null) {
            return cellChoosenListener2;
        }
        if (cellChoosenListener2 == null) {
            return cellChoosenListener;
        }
        return new CellChoosenEventMulticaster(cellChoosenListener, cellChoosenListener2);
    }

    public static CellChoosenListener remove(CellChoosenListener cellChoosenListener, CellChoosenListener cellChoosenListener2) {
        return (CellChoosenListener) AWTEventMulticaster.removeInternal(cellChoosenListener, cellChoosenListener2);
    }

    public void cellChoosen(CellChoosenEvent cellChoosenEvent) {
        ((CellChoosenListener) this.a).cellChoosen(cellChoosenEvent);
        ((CellChoosenListener) this.b).cellChoosen(cellChoosenEvent);
    }
}

