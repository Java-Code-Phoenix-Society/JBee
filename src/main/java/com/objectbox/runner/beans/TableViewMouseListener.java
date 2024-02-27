/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TableViewMouseListener
        extends MouseAdapter {
    TableView tableview = null;

    public TableViewMouseListener(TableView tableView) {
        this.tableview = tableView;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        try {
            this.tableview.requestFocus();
            Point point = this.tableview.getCurrent();
            mouseEvent.getModifiers();
            Point point2 = this.tableview.mapScreenToGrid(new Point(mouseEvent.getX(), mouseEvent.getY()));
            int n = point.x;
            int n2 = point.y;
            if (mouseEvent.getClickCount() >= 1) {
                if (this.tableview.getModifier() == 0) {
                    if (this.tableview.row_select_hash.size() > 0) {
                        this.tableview.repaint();
                    }
                    this.tableview.row_select_hash.clear();
                    this.tableview.unhighligthCell(n, n2);
                    this.tableview.setCurrent(point2.x, point2.y);
                    this.tableview.highligthCell(point2.x, point2.y);
                } else if (mouseEvent.isShiftDown()) {
                    int n3 = point2.y;
                    int n4 = n2;
                    if (n2 <= point2.y) {
                        n3 = n2;
                        n4 = point2.y;
                    }
                    if (this.tableview.row_select_hash.size() == 0) {
                        this.tableview.row_select_hash.put(new Point(0, n2), "");
                    }
                    int n5 = n3;
                    while (n5 < n4) {
                        this.tableview.row_select_hash.put(new Point(0, n5), "");
                        this.tableview.highligthCell(point2.x, n5);
                        ++n5;
                    }
                    this.tableview.setCurrent(point2.x, n4);
                    this.tableview.highligthCell(point2.x, n4);
                } else if (mouseEvent.isControlDown()) {
                    if (this.tableview.row_select_hash.size() == 0) {
                        this.tableview.row_select_hash.put(new Point(0, n2), "");
                    }
                    if (this.tableview.row_select_hash.get(new Point(0, point2.y)) != null) {
                        this.tableview.row_select_hash.remove(new Point(0, point2.y));
                        this.tableview.unhighligthCell(point2.x, point2.y);
                    } else {
                        this.tableview.row_select_hash.put(new Point(0, point2.y), "");
                        this.tableview.setCurrent(point2.x, point2.y);
                        this.tableview.highligthCell(point2.x, point2.y);
                    }
                }
                this.tableview.fireCellChoosen(new CellChoosenEvent(mouseEvent.getSource(), point2.x, point2.y));
                if (n2 > 0) {
                    this.tableview.startCellEditor(point2.x, point2.y);
                }
                if (mouseEvent.getY() < this.tableview.getCellheight() && mouseEvent.getModifiers() == 4) {
                    this.tableview.autoFitColumn(point2.x);
                    this.tableview.repaint();
                    return;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

