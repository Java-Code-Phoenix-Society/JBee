/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import com.objectbox.runner.beans.AbstrTableModel;
import com.objectbox.runner.model.JBAppletProperties;
import com.objectbox.runner.model.JBProperties;

import java.awt.Point;
import java.util.Hashtable;
import java.util.Vector;

public class NewAppTableModel
        extends AbstrTableModel {
    private Hashtable data = new Hashtable();
    private Point cellpoint = new Point(0, 0);
    private String[] h = new String[]{"Name", "URL"};
    private int nrows = 0;
    private Hashtable newProps = null;
    private Hashtable newParam = null;

    public void addData(Vector vector) {
        int n = this.data.size() + 1;
        int n2 = 0;
        while (n2 < vector.size()) {
            JBAppletProperties jBAppletProperties = (JBAppletProperties) vector.elementAt(n2);
            this.data.put(new Point(0, n2 + n), jBAppletProperties);
            ++n2;
        }
        this.nrows = this.data.size() + 1;
    }

    public String getCell(int n, int n2) {
        Point point = new Point(0, n2);
        Object v = this.data.get(point);
        if (v == null) {
            return "";
        }
        JBAppletProperties jBAppletProperties = (JBAppletProperties) v;
        if (n == 0) {
            return (String) ((Hashtable) jBAppletProperties.getProps()).get("code");
        }
        return (String) ((Hashtable) jBAppletProperties.getProps()).get("documentbase");
    }

    public Hashtable getData() {
        return this.data;
    }

    public void setData(Vector vector) {
        this.data.clear();
        int n = 1;
        int n2 = 0;
        while (n2 < vector.size()) {
            JBAppletProperties jBAppletProperties = (JBAppletProperties) vector.elementAt(n2);
            this.data.put(new Point(0, n2 + n), jBAppletProperties);
            ++n2;
        }
        this.nrows = this.data.size() + 1;
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

    public JBProperties getProperty(int n) {
        return (JBProperties) this.data.get(new Point(0, n));
    }

    public void init(String string) {
        int n = 0;
        while (n < 10) {
            this.data.put(new Point(1, n), "abc" + n);
            ++n;
        }
    }

    public void setCell(int n, int n2) {
    }

    public void setCell(int n, int n2, String string) {
        Point point = new Point(n, n2);
        this.data.put(point, string);
        Point point2 = new Point(0, n2);
        if (point.y < this.newProps.size()) {
            this.newProps.put(this.data.get(point2), string);
        } else {
            this.newParam.put(this.data.get(point2), string);
        }
    }
}

