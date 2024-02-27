/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import java.awt.*;

public class TreeChangedEventMulticaster
        extends AWTEventMulticaster
        implements TreeChangedListener {
    protected TreeChangedEventMulticaster(TreeChangedListener treeChangedListener, TreeChangedListener treeChangedListener2) {
        super(treeChangedListener, treeChangedListener2);
    }

    public static TreeChangedListener add(TreeChangedListener treeChangedListener, TreeChangedListener treeChangedListener2) {
        if (treeChangedListener == null) {
            return treeChangedListener2;
        }
        if (treeChangedListener2 == null) {
            return treeChangedListener;
        }
        return new TreeChangedEventMulticaster(treeChangedListener, treeChangedListener2);
    }

    public static TreeChangedListener remove(TreeChangedListener treeChangedListener, TreeChangedListener treeChangedListener2) {
        return (TreeChangedListener) AWTEventMulticaster.removeInternal(treeChangedListener, treeChangedListener2);
    }

    public void onTreeChanged(TreeChangedEvent treeChangedEvent) {
        ((TreeChangedListener) this.a).onTreeChanged(treeChangedEvent);
        ((TreeChangedListener) this.b).onTreeChanged(treeChangedEvent);
    }
}

