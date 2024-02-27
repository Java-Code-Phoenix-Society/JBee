/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

public abstract class AbstrTableModel {
    private TableView view = null;
    private TableCellAttribute cellattrib = new TableCellAttribute();

    public abstract String getCell(int var1, int var2);

    public TableCellAttribute getCellAttribute(int n, int n2) {
        return this.cellattrib;
    }

    public abstract String[] getHeaders();

    public abstract int getNumberOfColumns();

    public abstract int getNumberOfRows();

    public TableView getView() {
        return this.view;
    }

    public void setView(TableView tableView) {
        this.view = tableView;
    }

    public void setCell(int n, int n2, String string) {
    }
}

