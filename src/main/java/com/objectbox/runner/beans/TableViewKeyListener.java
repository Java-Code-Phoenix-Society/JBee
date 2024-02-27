/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TableViewKeyListener
        extends KeyAdapter {
    TableView tableview = null;

    public TableViewKeyListener(TableView tableView) {
        this.tableview = tableView;
    }

    public void keyPressed(KeyEvent keyEvent) {
        int n;
        Point point = this.tableview.getCurrent();
        int n2 = point.x;
        int n3 = n2--;
        int n4 = point.y;
        int n5 = n4--;
        int n6 = keyEvent.getKeyCode();
        switch (n6) {
            case 16: {
                this.tableview.setModifier(1);
                return;
            }
            case 17: {
                this.tableview.setModifier(2);
                return;
            }
            case 37: {
                break;
            }
            case 39: {
                ++n2;
                break;
            }
            case 38: {
                break;
            }
            case 40: {
                ++n4;
                break;
            }
            case 34: {
                n4 = (n4 += this.tableview.getSize().height / this.tableview.getCellheight() - 2) >= this.tableview.getNumberOfRows() ? this.tableview.getNumberOfRows() - 1 : n4;
                break;
            }
            case 33: {
                n4 = (n4 -= this.tableview.getSize().height / this.tableview.getCellheight() - 2) < 1 ? 1 : n4;
                break;
            }
            case 36: {
                n4 = 1;
                break;
            }
            case 35: {
                n4 = this.tableview.getNumberOfRows() - 1;
                break;
            }
            case 10: {
                this.tableview.fireCellselected(new CellselectedEvent(keyEvent.getSource(), n2, n4));
                this.tableview.startCellEditor(n2, n4);
                return;
            }
            default: {
                n = this.tableview.search(String.valueOf((char) n6));
                if (n == -1) break;
                n4 = n;
            }
        }
        n = this.tableview.getNumberOfcols();
        int n7 = this.tableview.getNumberOfRows();
        if (n4 >= 0 && n2 >= 0 && n2 < n && n4 < n7 && this.tableview.getModel().getCellAttribute(n2, n4).getIsselectable()) {
            if (this.tableview.getModifier() == 0) {
                if (this.tableview.row_select_hash.size() > 0) {
                    this.tableview.repaint();
                }
                this.tableview.row_select_hash.clear();
                this.tableview.unhighligthCell(n3, n5);
                this.tableview.setCurrent(n2, n4);
                this.tableview.highligthCell(n2, n4);
            } else if (this.tableview.getModifier() == 2) {
                if (this.tableview.row_select_hash.get(new Point(0, n4)) != null) {
                    this.tableview.row_select_hash.remove(new Point(0, n4));
                    this.tableview.unhighligthCell(n2, n4);
                } else {
                    this.tableview.row_select_hash.put(new Point(0, n4), "");
                    this.tableview.setCurrent(n2, n4);
                    this.tableview.highligthCell(n2, n4);
                }
            } else if (this.tableview.getModifier() == 1) {
                this.tableview.row_select_hash.put(new Point(0, n4), "");
                this.tableview.setCurrent(n2, n4);
                this.tableview.highligthCell(n2, n4);
            }
            this.tableview.fireCellselected(new CellselectedEvent(keyEvent.getSource(), n2, n4));
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
        int n = keyEvent.getKeyCode();
        switch (n) {
            case 16: {
                this.tableview.setModifier(0);
                break;
            }
            case 17: {
                this.tableview.setModifier(0);
            }
        }
    }
}

