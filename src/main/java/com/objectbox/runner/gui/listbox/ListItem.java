/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.listbox;

import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

public class ListItem
        implements Serializable {
    protected Vector m_arrSubItems = new Vector();
    protected String m_pszText = "";
    protected int m_nImage = -1;
    protected int m_nCY = -1;
    protected Rectangle m_rcText = new Rectangle();
    protected Rectangle m_rcIcon = new Rectangle();
    protected Font m_fonFont = null;
    protected Object m_objData;
    protected boolean m_bIsSelected = false;
    protected boolean m_bIsDropHiLited = false;
    protected boolean m_bIsFocused = false;
    protected boolean m_bIsMarked = false;
    protected boolean m_bIsDisabled = false;

    public void addSubItem() {
        ListSubItem listSubItem = new ListSubItem();
        this.addSubItem(listSubItem);
    }

    public void addSubItem(ListSubItem listSubItem) {
        this.m_arrSubItems.addElement(listSubItem);
    }

    public void addSubItem(String string) {
        ListSubItem listSubItem = new ListSubItem(string);
        this.addSubItem(listSubItem);
    }

    public void copy(ListItem listItem) {
        this.m_arrSubItems = (Vector) listItem.m_arrSubItems.clone();
        this.m_pszText = new String(listItem.m_pszText);
        this.m_nImage = listItem.m_nImage;
        this.m_nCY = listItem.m_nCY;
        this.m_rcText = listItem.m_rcText;
        this.m_rcIcon = listItem.m_rcIcon;
        this.m_fonFont = listItem.m_fonFont;
        this.m_bIsSelected = listItem.m_bIsSelected;
        this.m_bIsDropHiLited = listItem.m_bIsDropHiLited;
        this.m_bIsFocused = listItem.m_bIsFocused;
        this.m_bIsMarked = listItem.m_bIsMarked;
        this.m_bIsDisabled = listItem.m_bIsDisabled;
    }

    public boolean deleteSubItem(int n) {
        if (n > 0 && n - 1 < this.m_arrSubItems.size()) {
            this.m_arrSubItems.removeElementAt(n - 1);
            return true;
        }
        return false;
    }

    public Font getFont() {
        return this.m_fonFont;
    }

    public void setFont(Font font) {
        this.m_fonFont = font;
    }

    public int getHeight() {
        return this.m_nCY;
    }

    public void setHeight(int n) {
        this.m_nCY = n;
    }

    public int getImage() {
        return this.m_nImage;
    }

    public void setImage(int n) {
        this.m_nImage = n;
    }

    public Rectangle getImageBounds() {
        return this.m_rcIcon;
    }

    public void setImageBounds(Rectangle rectangle) {
        this.m_rcIcon = rectangle;
    }

    public Object getItemData() {
        return this.m_objData;
    }

    public void setItemData(Object object) {
        this.m_objData = object;
    }

    public ListSubItem getSubItem(int n) {
        if (n > 0 && n - 1 < this.m_arrSubItems.size()) {
            return (ListSubItem) this.m_arrSubItems.elementAt(n - 1);
        }
        return null;
    }

    public int getSubItemCount() {
        return this.m_arrSubItems.size();
    }

    public Vector getSubItems() {
        return this.m_arrSubItems;
    }

    public void setSubItems(Vector vector) {
        this.m_arrSubItems = vector;
    }

    public String getText() {
        return this.m_pszText;
    }

    public void setText(String string) {
        this.m_pszText = string;
    }

    public Rectangle getTextBounds() {
        return this.m_rcText;
    }

    public void setTextBounds(Rectangle rectangle) {
        this.m_rcText = rectangle;
    }

    public void insertSubItem(int n) {
        int n2 = this.getSubItemCount();
        if (n < 0) {
            n = n2;
        }
        if (n > n2) {
            this.addSubItem();
        } else {
            this.m_arrSubItems.insertElementAt(new ListSubItem(), n);
        }
    }

    public void setImageYPos(int n) {
        this.m_rcIcon.y = n;
    }

    public void setTextBounds(int n, int n2, int n3, int n4) {
        this.m_rcText.setBounds(n, n2, n3, n4);
    }

    public void setTextHeight(int n) {
        this.m_rcText.height = n;
    }
}

