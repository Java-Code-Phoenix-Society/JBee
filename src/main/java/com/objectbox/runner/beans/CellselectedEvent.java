/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

import java.util.EventObject;

public class CellselectedEvent
        extends EventObject {
    private int rowselected;
    private int colselected;

    public CellselectedEvent(Object object) {
        super(object);
    }

    public CellselectedEvent(Object object, int n, int n2) {
        super(object);
        this.rowselected = n2;
        this.colselected = n;
    }

    public int getColselected() {
        return this.colselected;
    }

    public int getRowselected() {
        return this.rowselected;
    }
}

