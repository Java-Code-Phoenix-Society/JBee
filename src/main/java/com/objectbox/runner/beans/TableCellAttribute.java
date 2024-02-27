/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TableCellAttribute {
    protected transient PropertyChangeSupport propertyChange;
    private Color textcolor = Color.black;
    private int[] symbolarray = null;
    private boolean isselectable = true;
    private Color backgroundcolor = Color.white;
    private boolean fieldIsEditable = false;

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().addPropertyChangeListener(propertyChangeListener);
    }

    public void firePropertyChange(String string, Object object, Object object2) {
        this.getPropertyChange().firePropertyChange(string, object, object2);
    }

    public Color getBackgroundcolor() {
        return this.backgroundcolor;
    }

    public void setBackgroundcolor(Color color) {
        this.backgroundcolor = color;
    }

    public boolean getIsEditable() {
        return this.fieldIsEditable;
    }

    public void setIsEditable(boolean bl) {
        boolean bl2 = this.fieldIsEditable;
        this.fieldIsEditable = bl;
        this.firePropertyChange("isEditable", new Boolean(bl2), new Boolean(bl));
    }

    public boolean getIsselectable() {
        return this.isselectable;
    }

    public void setIsselectable(boolean bl) {
        this.isselectable = bl;
    }

    protected PropertyChangeSupport getPropertyChange() {
        if (this.propertyChange == null) {
            this.propertyChange = new PropertyChangeSupport(this);
        }
        return this.propertyChange;
    }

    public int[] getSymbolarray() {
        return this.symbolarray;
    }

    public void setSymbolarray(int[] nArray) {
        this.symbolarray = nArray;
    }

    public Color getTextcolor() {
        return this.textcolor;
    }

    public void setTextcolor(Color color) {
        this.textcolor = color;
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().removePropertyChangeListener(propertyChangeListener);
    }
}

