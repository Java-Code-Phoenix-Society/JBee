/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.tree;

import com.objectbox.runner.gui.listbox.ListItem;
import com.objectbox.runner.gui.listbox.ListSubItem;

import java.awt.*;
import java.util.Vector;

public class Node
        extends TreeNodeX {
    static final long serialVersionUID = -123456789L;
    boolean m_bVisible = false;
    String m_pszText = null;
    int m_nImage = -1;
    int m_nExpandedImage = -1;
    int m_nChildren = 0;
    boolean m_bExpanded = false;
    Font m_fonFont = null;
    Object Data = null;
    ListItem m_itemParent;
    String m_type = "Folder";

    public Node() {
        this("", -1, -1);
    }

    public Node(String string, int n, int n2) {
        this.m_pszText = string;
        this.m_nImage = n;
        this.m_nExpandedImage = n2;
    }

    public void addSubItem(ListSubItem listSubItem) {
        ListItem listItem = this.getListItem();
        if (listItem == null) {
            listItem = new ListItem();
            this.bindToListItem(listItem);
        }
        listItem.addSubItem(listSubItem);
    }

    public void addSubItem(String string) {
        this.addSubItem(new ListSubItem(string));
    }

    public void bindToListItem(ListItem listItem) {
        this.m_itemParent = listItem;
        listItem.setItemData(this);
    }

    public void expand(boolean bl) {
        this.m_bExpanded = bl;
    }

    public Object getData() {
        return this.Data;
    }

    public void setData(Object object) {
        this.Data = object;
    }

    public int getExpandedImage() {
        return this.m_nExpandedImage;
    }

    public void setExpandedImage(int n) {
        this.m_nExpandedImage = n;
    }

    public Font getFont() {
        return this.m_fonFont;
    }

    public void setFont(Font font) {
        this.m_fonFont = font;
    }

    public int getImage() {
        if (this.isExpanded()) {
            return this.m_nExpandedImage;
        }
        return this.m_nImage;
    }

    public void setImage(int n) {
        this.m_nImage = n;
    }

    public ListItem getListItem() {
        return this.m_itemParent;
    }

    public Vector getSubItems() {
        ListItem listItem = this.getListItem();
        if (listItem != null) {
            return listItem.getSubItems();
        }
        return null;
    }

    public String getText() {
        return this.m_pszText;
    }

    public void setText(String string) {
        this.m_pszText = string;
    }

    public String getType() {
        return this.m_type;
    }

    public void setType(String string) {
        this.m_type = string;
    }

    public boolean isExpanded() {
        return this.m_bExpanded;
    }

    public boolean isMultiSelectable() {
        return this.getType().compareTo("Folder") != 0;
    }

    boolean isVisible() {
        Node node = this;
        if (!this.m_bVisible) {
            return false;
        } else {
            do {
                if (node.getParent() == null) {
                    return true;
                }

                node = (Node) node.getParent();
            } while (node.isExpanded() && node.m_bVisible);

            return false;
        }
    }

    public void setVisible(boolean bl) {
        this.m_bVisible = bl;
    }

    public String toString() {
        return "GnATT: " + super.toString();
    }
}

