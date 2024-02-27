/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class TableViewMouseMotionListener
        extends MouseMotionAdapter {
    TableView tableview = null;
    boolean cursorstate = false;

    public TableViewMouseMotionListener(TableView tableView) {
        this.tableview = tableView;
    }

    public void mouseMoved(MouseEvent mouseEvent) {
    }
}

