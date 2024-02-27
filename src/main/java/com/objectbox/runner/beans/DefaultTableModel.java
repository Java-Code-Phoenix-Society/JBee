/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

import java.awt.*;
import java.util.Hashtable;

public class DefaultTableModel
        extends AbstrTableModel {
    private Hashtable data = new Hashtable();
    private Point cellpoint = new Point(0, 0);
    private String[] h = new String[]{"A"};
    private int nrows = 10;

    public String getCell(int n, int n2) {
        if (n == 0) {
            return String.valueOf(n2);
        }
        Point point = new Point(n, n2);
        Object v = this.data.get(point);
        if (v == null) {
            return "";
        }
        return (String) v;
    }

    public String[] getHeaders() {
        return this.h;
    }

    public void setHeaders(String[] stringArray) {
        this.h = stringArray;
    }

    public int getNumberOfColumns() {
        return this.getHeaders().length;
    }

    public int getNumberOfRows() {
        return this.nrows;
    }

    public void setNumberOfRows(int n) {
        this.nrows = n;
    }

    public void setCell(int n, int n2) {
    }

    public void setCell(int n, int n2, String string) {
        Point point = new Point(n, n2);
        this.data.put(point, string);
    }
}

