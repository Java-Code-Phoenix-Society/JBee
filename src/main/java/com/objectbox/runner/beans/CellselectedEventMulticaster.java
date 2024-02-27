/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

import java.awt.*;

public class CellselectedEventMulticaster
        extends AWTEventMulticaster
        implements CellselectedListener {
    protected CellselectedEventMulticaster(CellselectedListener cellselectedListener, CellselectedListener cellselectedListener2) {
        super(cellselectedListener, cellselectedListener2);
    }

    public static CellselectedListener add(CellselectedListener cellselectedListener, CellselectedListener cellselectedListener2) {
        if (cellselectedListener == null) {
            return cellselectedListener2;
        }
        if (cellselectedListener2 == null) {
            return cellselectedListener;
        }
        return new CellselectedEventMulticaster(cellselectedListener, cellselectedListener2);
    }

    public static CellselectedListener remove(CellselectedListener cellselectedListener, CellselectedListener cellselectedListener2) {
        return (CellselectedListener) AWTEventMulticaster.removeInternal(cellselectedListener, cellselectedListener2);
    }

    public void cellselected(CellselectedEvent cellselectedEvent) {
        ((CellselectedListener) this.a).cellselected(cellselectedEvent);
        ((CellselectedListener) this.b).cellselected(cellselectedEvent);
    }
}

