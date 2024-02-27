/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.runner.beans.ICellRenderer;
import com.objectbox.runner.beans.TableView;

import java.awt.*;

public class CellRenderer
        implements ICellRenderer {
    Color high = new Color(222, 222, 222);
    Color medium = new Color(192, 192, 192);
    Color low = new Color(152, 152, 152);

    public void renderCell(TableView tableView, Graphics graphics, int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        int n7 = 5;
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int n8 = tableView.getCellheight();
        int n9 = fontMetrics.getHeight();
        int n10 = n8 / 2 - n9 / 2;
        if (n2 == 0) {
            graphics.setColor(Color.lightGray);
            graphics.fill3DRect(n3, n4, n5 - n3, n6 - n4, true);
            graphics.setColor(Color.black);
            graphics.drawString(tableView.getModel().getHeaders()[n], n3 + n7, n4 + n8 - n10);
        } else {
            graphics.setColor(Color.black);
            if (bl) {
                graphics.setColor(Color.gray);
                graphics.drawRect(n3 + 2, n4 + 2, n5 - n3 - 4, n6 - n4 - 4);
            } else {
                String string = tableView.getModel().getCell(2, n2);
                if (string.equals("jbprops")) {
                    graphics.setColor(this.high);
                } else if (string.equals("props")) {
                    graphics.setColor(this.low);
                } else if (string.equals("param")) {
                    graphics.setColor(this.medium);
                }
                graphics.fillRect(n3, n4, n5 - n3, n6 - n4);
            }
            if (bl) {
                graphics.setColor(Color.blue.darker());
            } else {
                graphics.setColor(Color.black);
            }
            graphics.drawRect(n3, n4, n5 - n3, n6 - n4);
            graphics.drawString(tableView.getModel().getCell(n, n2), n3 + n7, n4 + n8 - n10);
        }
    }
}

