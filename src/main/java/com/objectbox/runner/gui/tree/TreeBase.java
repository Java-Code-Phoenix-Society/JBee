/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.tree;

import com.objectbox.runner.gui.listbox.Column;
import com.objectbox.runner.gui.listbox.ItemTextChangedEvent;
import com.objectbox.runner.gui.listbox.ListBox;
import com.objectbox.runner.gui.listbox.ListItem;
import com.objectbox.runner.gui.text.Text;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Vector;

public class TreeBase
        extends ListBox
        implements Serializable {
    public static final int ILLEGAL = 3000;
    public static final int OPENFOLDER = 3002;
    public static final int SELECTION_CHANGE = 3003;
    public static final int ITEM_COLLAPSED = 3004;
    public static final int ITEM_EXPANDED = 3005;
    public static final int DOUBLE_CLICK = 3006;
    public static final String commandDragDrop = "Drag_Drop";
    public static final String commandOpenFolder = "Open_Folder";
    public static final String commandSelectionChanged = "Selection_Changed";
    public static final String commandIllegalAction = "Illegal_Action";
    public static final String commandItemCollapsed = "Item_Collapsed";
    public static final String commandItemExpanded = "Item_Expanded";
    public static final int TVGN_NEXT = 0;
    public static final int TVGN_PREVIOUS = 1;
    public static final int TVGN_CHILD = 2;
    public static final int TVGN_PARENT = 3;
    public static final int TVGN_ROOT = 4;
    public static final int TVGN_CARET = 5;
    public static final int TVGN_FIRSTVISIBLE = 6;
    public static final int TVGN_NEXTVISIBLE = 7;
    public static final int TVGN_PREVIOUSVISIBLE = 8;
    public static final int TVGN_DROPHILITE = 9;
    public static final int TVGN_FIRSTSELECTED = 10;
    public static final int TVGN_NEXTSELECTED = 11;
    public static final int TVGN_PREVIOUSSELECTED = 12;
    public static final int TVE_COLLAPSE = 0;
    public static final int TVE_EXPAND = 1;
    public static final int TVE_TOGGLE = 2;
    protected Vector m_arrRoots = null;
    protected int m_nIndent = 18;
    protected boolean m_bHideDisabledItems = false;
    protected boolean m_bHierarchyLines = true;
    protected Color m_colHierarchyLines = SystemColor.windowBorder;
    protected int m_nStyleHierarchyLines = 0;
    protected boolean m_bLinesAtRoot = true;
    protected boolean m_bButton = true;
    protected int m_nWordGap = 4;
    protected Node m_NNewlyExpandedNode = null;
    protected Node m_NNewlyCollapsedNode = null;
    protected boolean hasfocus = true;
    Node m_itemEventTarget;
    Node m_itemDropTarget;
    Node m_nodeCurrentEdit = new Node();
    private int m_nButtonSize = 8;

    public TreeBase() {
        this.m_nRowSpace = 3;
    }

    public TreeBase(String string, int n, int n2, ActionListener actionListener) {
        this(false);
        this.initRootNode(string, n, n2);
        this.actionListener = actionListener;
    }

    public TreeBase(boolean bl) {
        super(bl);
        this.m_nRowSpace = 3;
    }

    protected int addChildrenToListCtrl(Node node, int n) {
        if (n == -1) {
            return n;
        }
        Node node2 = (Node) node.getFirstChild();
        while (node2 != null) {
            ListItem listItem = node2.getListItem();
            if (listItem == null) {
                listItem = this.createNewItem();
                node2.bindToListItem(listItem);
            }
            super.insertItem(++n, listItem);
            if (node2.isExpanded() && node2.hasChildren()) {
                n = this.addChildrenToListCtrl(node2, n++);
            }
            node2 = (Node) node2.getNextSibling();
        }
        return n;
    }

    protected boolean addNodeToListBox(Node node) {
        int n;
        if (this.isRoot((Node) node.getParent()) && node.getPrevSibling() == null) {
            n = 1 + this.nodeIndex((Node) node.getParent());
        } else {
            Node node2 = this.getPrevVisibleItem(node);
            if (node2 == null) {
                return false;
            }
            n = this.nodeIndex(node2);
            ++n;
        }
        ListItem listItem = super.createNewItem();
        node.bindToListItem(listItem);
        listItem.setHeight(-1);
        super.insertItem(n, listItem);
        if (node.hasChildren() && node.isExpanded()) {
            Node node3 = (Node) node.getFirstChild();
            while (node3 != null) {
                this.addNodeToListBox(node3);
                node3 = (Node) node3.getNextSibling();
            }
        }
        return true;
    }

    public int addRootItem(Node node) {
        if (this.m_arrRoots == null) {
            this.m_arrRoots = new Vector();
        }
        this.m_arrRoots.addElement(node);
        ((Node) this.m_arrRoots.elementAt(this.m_arrRoots.size() - 1)).expand();
        ListItem listItem = new ListItem();
        ((Node) this.m_arrRoots.elementAt(this.m_arrRoots.size() - 1)).bindToListItem(listItem);
        this.addItem(listItem, false);
        this.addChildrenToListCtrl((Node) this.m_arrRoots.elementAt(this.m_arrRoots.size() - 1), this.nodeIndex((Node) this.m_arrRoots.elementAt(this.m_arrRoots.size() - 1)));
        return this.m_arrRoots.size() - 1;
    }

    protected Rectangle calcButtonRect() {
        Rectangle rectangle = this.getBounds();
        Node node = this.getNodeAt(this.m_lvi_iItem);
        if (this.isRoot(node) || !node.hasChildren()) {
            return new Rectangle(0, 0, 0, 0);
        }
        ListItem listItem = super.getItemAt(this.m_lvi_iItem);
        int n = node.getDistanceFromRoot();
        this.measureText();
        int n2 = this.m_recPCRect.y;
        int n3 = this.m_recPCRect.x - this.m_nButtonSize + this.getIndent() * (n - 1) - (this.m_bLinesAtRoot ? 0 : this.getIndent());
        n3 += rectangle.x;
        n3 += this.m_nWidthGap;
        n2 += listItem.getTextBounds().y + listItem.getImageBounds().height / 2 - 2;
        int n4 = this.m_nButtonSize;
        if (this.m_bLinesAtRoot) {
            n3 += this.getIndent();
        }
        Rectangle rectangle2 = new Rectangle();
        rectangle2.x = n3 - n4 / 2 - rectangle.x - this.m_recPCRect.x - this.m_ptViewportOrg.x;
        rectangle2.y = n2 - n4 / 2 - rectangle.y - this.m_recPCRect.y;
        rectangle2.width = n4;
        rectangle2.height = n4;
        return rectangle2;
    }

    protected void changeItemText() {
        if (this.m_nColumnEdit == 0) {
            this.m_nodeCurrentEdit.setText(this.m_textEditNode.getText());
            this.m_textEditNode.setVisible(false);
            this.measureItem(this.nodeIndex(this.m_nodeCurrentEdit), this.getGraphics());
            if (this.m_textEditNode.getText().compareTo(this.m_oldName) != 0) {
                this.fireOnItemTextChanged(new ItemTextChangedEvent(this));
                this.m_oldName = this.m_textEditNode.getText();
            }
            this.updateScrollbar();
            this.validate();
        } else {
            super.changeItemText();
        }
    }

    public int createRootNode() {
        return this.createRootNode("", -1, -1);
    }

    public int createRootNode(String string, int n, int n2) {
        if (this.m_arrRoots == null) {
            this.initRootNode(string, n, n2);
            return 0;
        }
        this.m_arrRoots.addElement(new Node(string, n, n2));
        ((Node) this.m_arrRoots.elementAt(this.m_arrRoots.size() - 1)).expand();
        ListItem listItem = new ListItem();
        ((Node) this.m_arrRoots.elementAt(this.m_arrRoots.size() - 1)).bindToListItem(listItem);
        this.addItem(listItem);
        return this.m_arrRoots.size() - 1;
    }

    protected boolean deletedUnChildedItem(Node node) {
        return this.deleteUnChildedItem(node, true);
    }

    protected boolean deleteUnChildedItem(Node node, boolean bl) {
        Node node2 = node;
        if (node2.getFirstChild() != null) {
            return false;
        }
        node2.detachFromTree();
        if (bl && this.nodeInListBox(node2)) {
            this.delNodeFromListBox(node2);
        }
        return true;
    }

    protected boolean delNodeFromListBox(Node node) {
        return this.delNodeFromListBox(node, true);
    }

    protected boolean delNodeFromListBox(Node node, boolean bl) {
        int n = this.nodeIndex(node);
        super.deleteItem(n, bl);
        return true;
    }

    public boolean delNodeFromTree(Node node, boolean bl) {
        if (node.hasChildren()) {
            this.removeChildrenFromListCtrl(node, this.nodeIndex(node), false);
        }
        this.delNodeFromListBox(node, false);
        node.detachFromTree();
        if (bl) {
            this.updateScrollbar();
            this.doLayout();
            this.repaint();
        }
        return true;
    }

    protected void doubleClickEvent(int n) {
        if (n == -1) {
            return;
        }
    }

    protected void drawButton(Graphics graphics) {
        Rectangle rectangle = this.calcButtonRect();
        Node node = (Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData();
        if (!node.hasChildren()) {
            return;
        }
        int n = rectangle.width / 3;
        Point point = new Point(rectangle.width / 2 + rectangle.x, rectangle.height / 2 + rectangle.y);
        graphics.setColor(this.getBackground());
        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        graphics.setColor(this.m_colHierarchyLines);
        if (rectangle.x + rectangle.width / 2 + 1 < ((Column) this.m_arrColumns.elementAt(0)).getWidth()) {
            graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            graphics.drawLine(point.x - n + 1, point.y, point.x + n, point.y);
            if (!((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).isExpanded()) {
                graphics.drawLine(point.x, point.y - n + 1, point.x, point.y + n);
            }
        }
    }

    public void drawHierarchyLines(Graphics graphics) {
        int n;
        int n2;
        Rectangle rectangle = this.getBounds();
        graphics.setColor(this.m_colHierarchyLines);
        Point point = new Point();
        boolean bl = this.isSelected(this.m_lvi_iItem);
        int n3 = ((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getDistanceFromRoot() - 1;
        boolean bl2 = this.m_bLinesAtRoot;
        point.y = this.m_recPCRect.y;
        ListItem listItem = (ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem);
        if (this.m_arrImages != null && this.m_arrImages.size() > 0) {
            point.x = -this.m_ptViewportOrg.x + this.m_nButtonSize + this.getIndent() * n3 - (bl2 ? 0 : this.getIndent());
            point.y += listItem.getImageBounds().y + listItem.getImageBounds().height / 2;
            n2 = listItem.getImageBounds().x - point.x - 1 - this.m_nWidthGap - this.m_ptViewportOrg.x;
            point.y -= rectangle.y;
            point.x += this.m_nWidthGap;
        } else {
            point.x = -this.m_ptViewportOrg.x + this.m_nButtonSize + this.getIndent() * n3 - (bl2 ? 0 : this.getIndent());
            point.y += listItem.getTextBounds().y + listItem.getTextBounds().height / 2;
            n2 = listItem.getTextBounds().x - point.x - 1;
            point.y -= listItem.getTextBounds().y;
            point.y -= rectangle.y;
        }
        point.x += rectangle.x;
        if ((point.y - this.m_ptViewportOrg.y) % 2 != 0) {
            ++point.y;
        }
        ++point.x;
        if (!this.isRoot(this.m_lvi_iItem) && point.x < ((Column) this.m_arrColumns.elementAt(0)).getWidth()) {
            this.drawHorzHierarchyLine(graphics, bl, point.x, point.x + n2, point.y);
        }
        if (this.m_lvi_iItem > 0) {
            n = listItem.getTextBounds().y;
            if (!this.isRoot(this.m_lvi_iItem) && point.x - this.getBounds().x < ((Column) this.m_arrColumns.elementAt(0)).getWidth()) {
                this.drawVertHierarchyLine(graphics, bl, point.x, point.y, n - rectangle.y - 3);
            }
        }
        if (((Node) listItem.getItemData()).hasChildren() && ((Node) listItem.getItemData()).isExpanded()) {
            n = this.m_arrImages != null && this.m_arrImages.size() > 0 ? listItem.getImageBounds().height / 2 : listItem.getTextBounds().height / 2;
            if (point.x + this.getIndent() - this.getBounds().x < ((Column) this.m_arrColumns.elementAt(0)).getWidth()) {
                this.drawVertHierarchyLine(graphics, bl, point.x + this.getIndent(), point.y + n + 2, this.m_recPCRect.height + point.y);
            }
        }
        if (this.isRoot(this.m_lvi_iItem)) {
            return;
        }
        Node node = (Node) listItem.getItemData();
        n = n3 + 1;
        while (n >= (bl2 ? 0 : 1)) {
            if (node.getNextSibling() != null && point.x - this.getBounds().x < ((Column) this.m_arrColumns.elementAt(0)).getWidth()) {
                if (this.m_arrImages == null || this.m_arrImages.size() < 0) {
                    this.drawVertHierarchyLine(graphics, bl, point.x, point.y, listItem.getTextBounds().height + listItem.getTextBounds().y - rectangle.y + listItem.getTextBounds().height / 4);
                } else {
                    this.drawVertHierarchyLine(graphics, bl, point.x, point.y, this.m_recPCRect.height + this.m_recPCRect.y - rectangle.y + this.m_recPCRect.height / 4);
                }
            }
            node = (Node) node.getParent();
            point.x -= this.getIndent();
            point.y = this.m_recPCRect.y - 2 - rectangle.y;
            --n;
        }
    }

    public void drawHorzHierarchyLine(Graphics graphics, boolean bl, int n, int n2, int n3) {
        Rectangle rectangle = this.getBounds();
        if ((n2 -= rectangle.x) < (n -= rectangle.x)) {
            int n4 = n;
            n = n2;
            n2 = n4;
        }
        if ((n + this.m_ptViewportOrg.x) % 2 != 0) {
            ++n;
        }
        if (this.m_nStyleHierarchyLines == 0) {
            this.drawDottedLine(graphics, n, n3, n2, n3);
        } else {
            graphics.drawLine(n, n3, n2, n3);
        }
    }

    public void drawSubItem(Graphics graphics) {
        Rectangle rectangle = this.getBounds();
        if (this.m_lvi_iSubItem == 0) {
            Object object;
            Object object2;
            Serializable serializable;
            int n = this.measureText();
            this.isSelected(this.m_lvi_iItem);
            Node node = (Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData();
            Object object3 = node.getText();
            int n2 = !node.isExpanded() ? ((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getImage() : ((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getExpandedImage();
            graphics.setColor(this.getBackground());
            Rectangle rectangle2 = this.m_recPCRect;
            Rectangle rectangle3 = ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getTextBounds();
            rectangle3.y = this.m_recPCRect.y + 2;
            if (this.m_arrColumns.size() > 1) {
                rectangle3.width = Math.min(rectangle3.width, this.m_recPCRect.x + ((Column) this.m_arrColumns.elementAt(0)).getWidth() - this.m_nWidthGap);
            }
            Rectangle rectangle4 = this.m_recPCRect;
            rectangle4.width = 0;
            if (this.m_arrImages != null && this.m_arrImages.size() > 0) {
                if (this.getImageIndex(n2) >= this.m_arrImages.size()) {
                    JBLogger.log("Warning:  Image " + n2 + " does not exist!");
                    n2 = 0;
                }
                serializable = (ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem);
                object2 = new Point(this.m_recPCRect.x + ((ListItem) serializable).getImageBounds().x, this.m_recPCRect.y + ((ListItem) serializable).getImageBounds().y);
                ++((Point) object2).y;
                object = this.getImageList(n2);
                if (object != null && ((Point) object2).x + this.m_nWidthGap < ((Column) this.m_arrColumns.elementAt(0)).getWidth()) {
                    graphics.drawImage((Image) object, ((Point) object2).x, ((Point) object2).y - rectangle.y, this);
                }
                rectangle4.width = ((Point) object2).x;
            }
            rectangle2.x = rectangle3.x + rectangle3.width;
            rectangle2.width = this.m_recPCRect.width;
            graphics.fillRect(rectangle2.x, rectangle2.y - rectangle.y, rectangle2.width, rectangle2.height);
            this.m_recPCRect.x = rectangle3.x;
            if (this.m_bHierarchyLines) {
                this.drawHierarchyLines(graphics);
            }
            if (this.m_bButton) {
                this.drawButton(graphics);
            }
            rectangle2.x = rectangle3.x - this.m_nWidthGap / 2;
            rectangle2.width = rectangle3.width + this.m_nWidthGap / 2;
            rectangle2.y = this.m_recPCRect.y;
            rectangle2.height = n * 1;
            graphics.setFont(((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getFont());
            serializable = graphics.getFontMetrics();
            graphics.setColor(this.getBackground());
            if (object3 != null) {
                if (this.m_bAutoWrap) {
                    int n3;
                    ListItem listItem;
                    object2 = ((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getText();
                    object = ((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getFont();
                    if (object == null) {
                        object = this.defaultfont;
                    }
                    graphics.setFont((Font) object);
                    serializable = this.getFontMetrics((Font) object);
                    int n4 = ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getTextBounds().x + this.m_nWidthGap;
                    Vector vector = Text.wrapText((String) object3, ((Column) this.m_arrColumns.elementAt(this.m_lvi_iSubItem)).getWidth() - this.m_nWidthGap * 2 - n4, true, (FontMetrics) serializable);
                    int n5 = ((FontMetrics) serializable).stringWidth((String) object2);
                    if (vector.size() > 1) {
                        n5 = ((Column) this.m_arrColumns.elementAt(this.m_lvi_iSubItem)).getWidth() - this.m_nWidthGap * 2 - n4;
                    }
                    if (n5 > ((Column) this.m_arrColumns.elementAt(0)).getWidth() - (rectangle3.x - 2 + this.m_nWidthGap)) {
                        n5 = rectangle3.width;
                    }
                    graphics.setColor(SystemColor.textText);
                    if (!this.isSelected(this.m_lvi_iItem)) {
                        graphics.setColor(Color.black);
                    } else {
                        graphics.setColor(SystemColor.textHighlight);
                        listItem = (ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem);
                        n3 = Math.max(n, listItem.getImageBounds().height);
                        if (((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().height <= n) {
                            graphics.fillRect(rectangle3.x - 1 + this.m_nWidthGap - this.m_ptViewportOrg.x, rectangle3.y - rectangle.y - 1, n5 + 3, n - 1);
                            if (this.m_bHasFocus) {
                                this.drawDottedRect(graphics, rectangle3.x - 2 + this.m_nWidthGap - this.m_ptViewportOrg.x, rectangle3.y - rectangle.y - 2, n5 + 4, n);
                            }
                        } else {
                            graphics.fillRect(rectangle3.x + this.m_nWidthGap - this.m_ptViewportOrg.x + 1, rectangle3.y + (n3 / 2 - n / 2) - rectangle.y - 2, n5 + 3, n - 1);
                            if (this.m_bHasFocus) {
                                this.drawDottedRect(graphics, rectangle3.x + this.m_nWidthGap - this.m_ptViewportOrg.x, rectangle3.y + (n3 / 2 - n / 2) - 1 - rectangle.y - 2, n5 + 4, n);
                            }
                        }
                        graphics.setXORMode(this.getBackground());
                        graphics.setPaintMode();
                        graphics.setColor(SystemColor.textHighlightText);
                    }
                    listItem = (ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem);
                    n3 = Math.max(n, listItem.getImageBounds().height);
                    int n6 = 0;
                    int n7 = 0;
                    int n8 = ((FontMetrics) serializable).getHeight();
                    if (((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().height <= n) {
                        n6 = rectangle3.x + this.m_nWidthGap - this.m_ptViewportOrg.x;
                        n7 = rectangle3.y - 2 - rectangle.y + n8 - n8 / 4;
                    } else {
                        n6 = rectangle3.x + this.m_nWidthGap - this.m_ptViewportOrg.x;
                        n7 = rectangle3.y + n + (n3 / 2 - n / 2) - 1 - rectangle.y - 2;
                    }
                    int n9 = 0;
                    while (n9 < vector.size()) {
                        graphics.drawString((String) vector.elementAt(n9), n6, n7);
                        n7 += n8;
                        ++n9;
                    }
                } else {
                    int n10;
                    int n11;
                    int n12;
                    int n13;
                    object2 = ((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getFont();
                    if (object2 == null) {
                        object2 = this.defaultfont;
                    }
                    graphics.setFont((Font) object2);
                    serializable = this.getFontMetrics((Font) object2);
                    if (this.m_arrColumns.size() > 1) {
                        object = "";
                        n13 = 0;
                        if (this.m_lvi_iSubItem == 0) {
                            n13 = ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().width + this.m_nWidthGap;
                        }
                        if ((n12 = ((FontMetrics) serializable).stringWidth((String) object3) + n13) > (n11 = ((Column) this.m_arrColumns.elementAt(0)).getWidth()) - (n10 = rectangle3.x - this.m_ptViewportOrg.x - 3 - this.getIndent())) {
                            int n14 = ((FontMetrics) serializable).stringWidth(".");
                            if (n13 + this.m_nWidthGap + n14 > n11 - n10) {
                                n12 = n13;
                                object = " ";
                            } else if (n13 + this.m_nWidthGap + 2 * n14 > n11 - n10) {
                                n12 = n13 + this.m_nWidthGap + n14;
                                object = ".";
                            } else if (n13 + this.m_nWidthGap + 3 * n14 > n11 - n10) {
                                n12 = n13 + this.m_nWidthGap + 2 * n14;
                                object = "..";
                            } else {
                                int n15 = 0;
                                n15 = 1;
                                while (n15 < ((String) object3).length()) {
                                    n12 = n13 + 2 * this.m_nWidthGap + ((FontMetrics) serializable).stringWidth(((String) object3).substring(0, n15 - 1)) + 3 * n14;
                                    if (n12 > n11 - n10 + this.getIndent()) break;
                                    ++n15;
                                }
                                if (n15 - 2 < 0) {
                                    n15 = 2;
                                }
                                object = String.valueOf(((String) object3).substring(0, n15 - 2)) + "...";
                                n12 = n13 + this.m_nWidthGap + ((FontMetrics) serializable).stringWidth((String) object);
                            }
                        }
                        if (((FontMetrics) serializable).stringWidth((String) object3) + n13 > n11 - n10) {
                            object3 = object;
                        }
                    }
                    if (!this.isSelected(this.m_lvi_iItem)) {
                        graphics.setColor(SystemColor.textText);
                    } else {
                        graphics.setColor(SystemColor.textHighlight);
                        object = (ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem);
                        n13 = Math.max(n, ((ListItem) object).getImageBounds().height);
                        n12 = Math.min(((FontMetrics) serializable).stringWidth((String) object3), ((Column) this.m_arrColumns.elementAt(0)).getWidth() - ((ListItem) object).getTextBounds().x - this.m_nWidthGap - 4);
                        if (((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().height <= n && this.m_arrColumns.size() == 1) {
                            graphics.fillRect(rectangle3.x + this.m_nWidthGap - this.m_ptViewportOrg.x - 2, rectangle3.y - rectangle.y, n12 + 3, n - 1);
                            if (this.m_bHasFocus) {
                                this.drawDottedRect(graphics, rectangle3.x + this.m_nWidthGap - this.m_ptViewportOrg.x - 3, rectangle3.y - rectangle.y - 1, n12 + 4, n);
                            }
                        } else {
                            graphics.fillRect(rectangle3.x + this.m_nWidthGap - this.m_ptViewportOrg.x - 2, rectangle3.y + (n13 / 2 - n / 2) - rectangle.y - 1, n12 + 3, n - 1);
                            if (this.m_bHasFocus) {
                                this.drawDottedRect(graphics, rectangle3.x + this.m_nWidthGap - this.m_ptViewportOrg.x - 3, rectangle3.y + (n13 / 2 - n / 2) - rectangle.y - 2, n12 + 4, n);
                            }
                        }
                        graphics.setXORMode(this.getBackground());
                        graphics.setPaintMode();
                        graphics.setColor(SystemColor.textHighlightText);
                    }
                    object = (ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem);
                    n13 = Math.max(n, ((ListItem) object).getImageBounds().height);
                    n12 = ((FontMetrics) serializable).getHeight();
                    if (((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().height <= n) {
                        int cfr_ignored_0 = rectangle3.x + this.m_nWidthGap - this.m_ptViewportOrg.x;
                        int cfr_ignored_1 = rectangle3.y - 2 - rectangle.y + n12 - n12 / 4;
                    } else {
                        int cfr_ignored_2 = rectangle3.x + this.m_nWidthGap - this.m_ptViewportOrg.x;
                        int cfr_ignored_3 = rectangle3.y + n + (n13 / 2 - n / 2) - 1 - rectangle.y - 2;
                    }
                    n11 = this.m_recPCRect.x + this.m_nWidthGap - this.m_ptViewportOrg.x + 2;
                    n10 = this.m_recPCRect.y + ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getTextBounds().height - rectangle.y - ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getTextBounds().height / 4;
                    int cfr_ignored_4 = this.m_nWidthGap + ((FontMetrics) serializable).stringWidth(this.m_lvi_pszText);
                    Column cfr_ignored_5 = (Column) this.m_arrColumns.elementAt(this.m_lvi_iSubItem);
                    graphics.drawString((String) object3, n11, n10);
                }
            }
        } else {
            super.drawSubItem(graphics);
        }
    }

    public void drawTargetHighLight(Graphics graphics) {
        Rectangle rectangle = this.getBounds();
        graphics.setColor(SystemColor.textHighlight);
        int n = this.getTarget(this.m_nXMouse, this.m_nYMouse);
        if (n != this.m_nOldTargetIndex) {
            int n2;
            String string;
            FontMetrics fontMetrics;
            Font font;
            Node node;
            ListItem listItem;
            if (this.m_nOldTargetIndex != -1 && !this.isSelected(this.m_nOldTargetIndex)) {
                this.m_lvi_iItem = this.m_nOldTargetIndex;
                listItem = (ListItem) this.m_arrItems.elementAt(this.m_nOldTargetIndex);
                node = (Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData();
                font = node.getFont();
                if (font == null) {
                    font = this.defaultfont;
                }
                graphics.setFont(font);
                fontMetrics = this.getFontMetrics(font);
                string = node.getText();
                n2 = fontMetrics.stringWidth(string) + listItem.getTextBounds().x < ((Column) this.m_arrColumns.elementAt(0)).getWidth() - this.getIndent() ? fontMetrics.stringWidth(string) + 4 : ((Column) this.m_arrColumns.elementAt(0)).getWidth() - listItem.getTextBounds().x - this.m_nWidthGap;
                graphics.fillRect(listItem.getTextBounds().x + this.m_nWidthGap - 2, listItem.getTextBounds().y - 2 - rectangle.y, n2, listItem.getTextBounds().height);
            }
            this.m_nOldTargetIndex = n;
            if (n != -1 && !this.isSelected(n)) {
                this.m_lvi_iItem = n;
                listItem = (ListItem) this.m_arrItems.elementAt(n);
                node = (Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData();
                font = node.getFont();
                if (font == null) {
                    font = this.defaultfont;
                }
                graphics.setFont(font);
                fontMetrics = this.getFontMetrics(font);
                string = node.getText();
                n2 = fontMetrics.stringWidth(string) + listItem.getTextBounds().x < ((Column) this.m_arrColumns.elementAt(0)).getWidth() - this.getIndent() ? fontMetrics.stringWidth(string) + 4 : ((Column) this.m_arrColumns.elementAt(0)).getWidth() - listItem.getTextBounds().x - this.m_nWidthGap;
                graphics.fillRect(listItem.getTextBounds().x + this.m_nWidthGap - 2, listItem.getTextBounds().y - 2 - rectangle.y, n2, listItem.getTextBounds().height);
            }
        }
    }

    public void drawVertHierarchyLine(Graphics graphics, boolean bl, int n, int n2, int n3) {
        if (n3 < n2) {
            int n4 = n2;
            n2 = n3;
            n3 = n4;
        }
        n -= this.getBounds().x;
        if ((n2 + this.m_ptViewportOrg.y) % 2 != 0 && this.m_nStyleHierarchyLines == 0) {
            --n2;
        }
        if (this.m_nStyleHierarchyLines == 0) {
            this.drawDottedLine(graphics, n, n2, n, n3);
        } else {
            graphics.drawLine(n, n2, n, n3);
        }
    }

    public void editItem() {
        this.m_nColumnEdit = 0;
        Rectangle rectangle = this.getBounds();
        this.m_bEditModeEnabled = false;
        this.m_bDragModeEnabled = false;
        if (this.selected.length > 1) {
            int n = this.selected[this.selected.length - 1];
            this.selected = new int[0];
            this.select(n);
        }
        this.m_nodeCurrentEdit = this.getSelectedNode();
        if (this.isRoot(this.m_nodeCurrentEdit)) {
            return;
        }
        ListItem listItem = (ListItem) this.m_arrItems.elementAt(this.selected[this.selected.length - 1]);
        this.m_textEditNode.setText(this.m_nodeCurrentEdit.getText());
        this.m_textEditNode.setBorderStyle(3);
        this.m_textEditNode.setTextHIndent(1);
        this.m_textEditNode.setTextVIndent(2);
        this.m_textEditNode.selectAll();
        this.m_textEditNode.setBorderColor(Color.black);
        Font font = this.m_nodeCurrentEdit.getFont();
        if (font == null) {
            font = this.defaultfont;
        }
        this.m_textEditNode.setFont(font);
        this.m_textEditNode.selectAll();
        this.m_nodeCurrentEdit.setText("");
        this.m_textEditNode.requestFocus();
        this.m_textEditNode.setBounds(listItem.getTextBounds().x - 2 + this.m_nWidthGap - this.m_ptViewportOrg.x, listItem.getTextBounds().y - rectangle.y - 2, listItem.getTextBounds().width + 2, listItem.getTextBounds().height + listItem.getTextBounds().height / 4);
        this.m_textEditNode.setVisible(true);
        this.m_textEditNode.update();
    }

    public boolean expand(Node node, int n) {
        return this.expand(node, n, true);
    }

    public boolean expand(Node node, int n, boolean bl) {
        this.getSelectedNode();
        Node node2 = node;
        int n2 = n == 2 ? (node2.isExpanded() ? 0 : 1) : n;
        int n3 = this.nodeIndex(node2);
        switch (n2) {
            case 0: {
                if (!node2.isExpanded()) {
                    return true;
                }
                node2.collapse();
                Vector vector = new Vector();
                int n4 = 0;
                while (n4 < this.selected.length) {
                    vector.addElement(this.m_arrItems.elementAt(this.selected[n4]));
                    ++n4;
                }
                this.deselectAll(false);
                this.m_NNewlyCollapsedNode = node2;
                this.removeChildrenFromListCtrl(node2, n3, false);
                this.selected = new int[0];
                n4 = 0;
                while (n4 < vector.size()) {
                    this.select(this.getIndex((ListItem) vector.elementAt(n4)), false);
                    ++n4;
                }
                this.reMeasureAllItems();
                if (bl) {
                    this.update();
                }
                this.processActionEvent(new ActionEvent(this, 3004, commandItemCollapsed));
                break;
            }
            case 1: {
                if (node2.isExpanded()) {
                    return true;
                }
                Vector vector = new Vector();
                int n5 = 0;
                while (n5 < this.selected.length) {
                    vector.addElement(this.m_arrItems.elementAt(this.selected[n5]));
                    ++n5;
                }
                this.deselectAll(false);
                node2.expand();
                this.m_NNewlyExpandedNode = node2;
                if (node2.hasChildren()) {
                    this.addChildrenToListCtrl(node2, n3);
                    this.processActionEvent(new ActionEvent(this, 3005, commandItemExpanded));
                } else {
                    this.processActionEvent(new ActionEvent(this, 3002, commandOpenFolder));
                }
                this.selected = new int[0];
                n5 = 0;
                while (n5 < vector.size()) {
                    this.select(this.getIndex((ListItem) vector.elementAt(n5)), false);
                    ++n5;
                }
                this.reMeasureAllItems();
                if (!bl) break;
                this.update();
                break;
            }
        }
        return true;
    }

    protected Node expandSelectedItem(int n) {
        Node node = this.getSelectedNode();
        if (!this.expand(node, n)) {
            return null;
        }
        return node;
    }

    public Node getChildItem(Node node) {
        TreeNodeX treeNodeX = new TreeNodeX();
        treeNodeX = node == null ? (Node) this.m_arrRoots.elementAt(0) : node;
        return (Node) treeNodeX.getFirstChild();
    }

    public Node getFirstSelectedItem() {
        Node node = null;
        if (this.getSelectedItems().length == 0) {
            return node;
        }
        int n = this.getSelectedIndexes()[0];
        node = this.getNodeAt(n);
        return node;
    }

    public Node getFirstVisibleItem() {
        Node node = null;
        int n = this.m_nTopRow;
        if (n != -1) {
            node = this.getNodeAt(n);
        }
        return node;
    }

    public int getIndent() {
        return this.m_nIndent;
    }

    public void setIndent(int n) {
        this.m_nIndent = n;
        this.reMeasureAllItems();
        this.update();
    }

    public Node getLastVisibleItem(Node node) {
        int n = this.getItemCount();
        if (n < 0) {
            return null;
        }
        return this.getNodeAt(n - 1);
    }

    public Node getNewlyCollapsedNode() {
        return this.m_NNewlyCollapsedNode;
    }

    public Node getNewlyExpandedNode() {
        return this.m_NNewlyExpandedNode;
    }

    public Node getNextItem(Node node, int n) {
        switch (n) {
            case 0: {
                return this.getNextSiblingItem(node);
            }
            case 1: {
                return this.getPrevSiblingItem(node);
            }
            case 2: {
                return this.getChildItem(node);
            }
            case 3: {
                return this.getParentItem(node);
            }
            case 4: {
                return this.getRootItem(node);
            }
            case 6: {
                return this.getFirstVisibleItem();
            }
            case 7: {
                return this.getNextVisibleItem(node);
            }
            case 8: {
                return this.getPrevVisibleItem(node);
            }
            case 10: {
                return this.getFirstSelectedItem();
            }
            case 11: {
                return this.getNextSelectedItem(node);
            }
            case 12: {
                return this.getPrevSelectedItem(node);
            }
        }
        return null;
    }

    public Node getNextItemInDisplayOrder(Node node) {
        Node node2 = node;
        return (Node) node2.getNextInDisplayOrder();
    }

    public Node getNextSelectedItem(Node node) {
        int n = this.nodeIndex(node);
        if (n >= 0) {
            int n2 = 0;
            while (n2 < this.getSelectedIndexes().length) {
                if (n == this.getSelectedIndexes()[n2]) {
                    if (n2 == this.getSelectedIndexes().length - 1) {
                        n = -1;
                        break;
                    }
                    n = n2 + 1;
                    break;
                }
                ++n2;
            }
            if (n != -1) {
                return this.getNodeAt(n);
            }
        } else {
            Node node2 = node;
            while ((node2 = (Node) node2.getPrevInDisplayOrder()) != null) {
                if (!node2.isVisible()) continue;
                return this.getNextSelectedItem(node2);
            }
        }
        return null;
    }

    public Node getNextSiblingItem(Node node) {
        Node node2 = node == null ? (Node) this.m_arrRoots.elementAt(0) : node;
        return (Node) node2.getNextSibling();
    }

    public Node getNextVisibleItem(Node node) {
        int n = this.nodeIndex(node);
        if (n >= 0) {
            if (++n < this.getItemCount()) {
                return this.getNodeAt(n);
            }
        } else {
            Node node2 = node;
            if (node2 != null) {
                node2 = (Node) node2.getNextInDisplayOrder();
                while (node2 != null) {
                    if (node2.isVisible()) {
                        return node2;
                    }
                    node2 = (Node) node2.getNextInDisplayOrder();
                }
            }
        }
        return null;
    }

    public Node getNode(ListItem listItem) {
        if (listItem == null) {
            return null;
        }
        return (Node) listItem.getItemData();
    }

    public Node getNodeAt(int n) {
        ListItem listItem = super.getItemAt(n);
        Node node = listItem != null ? (Node) listItem.getItemData() : null;
        return node;
    }

    public Node getParentItem(Node node) {
        TreeNodeX treeNodeX = node == null ? (Node) this.m_arrRoots.elementAt(0) : node;
        if (!this.isRoot((Node) treeNodeX)) {
            while (treeNodeX != null && !this.isRoot((Node) treeNodeX.getParent())) {
                treeNodeX = (TreeNodeX) treeNodeX.getParent();
            }
            return (Node) treeNodeX.getFirstChild();
        }
        return (Node) ((Node) this.m_arrRoots.elementAt(0)).getFirstChild();
    }

    public Node getPrevSelectedItem(Node node) {
        int n = this.nodeIndex(node);
        if (n >= 0) {
            int n2 = 0;
            while (n2 < this.getSelectedIndexes().length) {
                if (n == this.getSelectedIndexes()[n2]) {
                    n = n2 - 1;
                    break;
                }
                ++n2;
            }
            if (n != -1) {
                return this.getNodeAt(n);
            }
        } else {
            Node node2 = node;
            if (node2 != null) {
                node2 = (Node) node2.getNextInDisplayOrder();
                while (node2 != null) {
                    if (node2.isVisible()) {
                        return this.getPrevSelectedItem(node2);
                    }
                    node2 = (Node) node2.getNextInDisplayOrder();
                }
            }
        }
        return null;
    }

    public Node getPrevSiblingItem(Node node) {
        Node node2 = node == null ? (Node) this.m_arrRoots.elementAt(0) : node;
        return (Node) node2.getPrevSibling();
    }

    public Node getPrevVisibleItem(Node node) {
        if (!this.isRoot(node)) {
            int n = this.nodeIndex(node);
            if (n >= 0) {
                if (--n != -1) {
                    return this.getNodeAt(n);
                }
            } else {
                Node node2 = node;
                while ((node2 = (Node) node2.getPrevInDisplayOrder()) != null) {
                    if (this.nodeIndex(node2) == -1) continue;
                    return node2;
                }
            }
        }
        return null;
    }

    public Node getRoot(int n) {
        if (this.m_arrRoots.size() > n) {
            return (Node) this.m_arrRoots.elementAt(n);
        }
        return null;
    }

    public Node getRootItem() {
        return this.getRootItem(null);
    }

    public Node getRootItem(Node node) {
        if (this.m_arrRoots == null) {
            return null;
        }
        Node node2 = node == null ? (Node) this.m_arrRoots.elementAt(0) : node;
        if (!this.isRoot(node2)) {
            while (node2 != null && !this.isRoot((Node) node2.getParent())) {
                node2 = (Node) node2.getParent();
            }
            return (Node) node2.getFirstChild();
        }
        return (Node) ((Node) this.m_arrRoots.elementAt(0)).getFirstChild();
    }

    protected int getSelected(int n, int n2) {
        int n3;
        if (this.m_textEditNode != null && this.m_textEditNode.isShowing()) {
            this.changeItemText();
        }
        if ((n3 = super.getSelected(n, n2)) > -1) {
            ListItem listItem = this.getItemAt(n3);
            Node node = (Node) listItem.getItemData();
            Font font = node.getFont();
            if (font == null) {
                font = this.defaultfont;
            }
            FontMetrics fontMetrics = this.getGraphics().getFontMetrics(font);
            int n4 = 0;
            n4 = fontMetrics.stringWidth(node.getText()) < listItem.getTextBounds().width ? fontMetrics.stringWidth(node.getText()) : listItem.getTextBounds().width;
            Rectangle rectangle = new Rectangle(listItem.getTextBounds().x - 2 + this.m_nWidthGap, n2 - 2, n4, 4);
            if (this.m_arrImages != null && this.m_arrImages.size() > 0) {
                rectangle.width += listItem.getImageBounds().width;
                rectangle.width += this.getIndent() / 2;
                rectangle.x = listItem.getImageBounds().x - this.m_ptViewportOrg.x;
            }
            if (rectangle.contains(n, n2)) {
                if (this.isSelected(n3)) {
                    rectangle.x = listItem.getTextBounds().x - 2 + this.m_nWidthGap - this.m_ptViewportOrg.x;
                    if (rectangle.contains(n, n2) && this.selected.length > 1 && this.m_bEditModeEnabled && !this.m_bDragModeEnabled) {
                        this.selected = new int[0];
                        this.select(n3, false);
                    }
                    return n3;
                }
                return n3;
            }
            this.m_lvi_iItem = n3;
            this.m_recPCRect = new Rectangle(listItem.getTextBounds().x - 2 + this.m_nWidthGap, n2 - 2, n4, 4);
            Rectangle rectangle2 = this.calcButtonRect();
            if (rectangle2.contains(n, n2)) {
                this.expand(node, 2, true);
                this.m_bEditModeEnabled = false;
                this.m_bDragModeEnabled = false;
                return -1;
            }
        }
        return -1;
    }

    public Node getSelectedNode() {
        if (this.selected.length > 0 && this.selected[this.selected.length - 1] < this.m_arrItems.size()) {
            return (Node) ((ListItem) this.m_arrItems.elementAt(this.selected[this.selected.length - 1])).getItemData();
        }
        return null;
    }

    protected int getTarget(int n, int n2) {
        return super.getSelected(n, n2);
    }

    protected void initRootNode() {
        if (this.m_arrRoots == null) {
            this.m_arrRoots = new Vector();
            this.m_arrRoots.addElement(new Node());
        }
        ListItem listItem = new ListItem();
        ((Node) this.m_arrRoots.elementAt(0)).bindToListItem(listItem);
        this.addItem(listItem);
    }

    public void initRootNode(String string, int n, int n2) {
        if (this.m_arrRoots == null) {
            this.m_arrRoots = new Vector();
            this.m_arrRoots.addElement(new Node(string, n, n2));
        }
        ListItem listItem = new ListItem();
        ((Node) this.m_arrRoots.elementAt(0)).bindToListItem(listItem);
        this.addItem(listItem);
    }

    public Node insertItem(Node node, Node node2, Node node3) {
        if (this.m_arrRoots == null) {
            this.initRootNode();
        }
        Node node4 = node2;
        if (node2 == null) {
            node4 = (Node) this.m_arrRoots.elementAt(0);
        }
        Node node5 = node3;
        Node node6 = new Node();
        node6 = node;
        if (!node4.addChild((TreeNode) node6, node5)) {
            return null;
        }
        node6.collapse();
        if (this.shouldBeInListBox(node6)) {
            this.addNodeToListBox(node6);
        }
        return node6;
    }

    public Node insertItem(Node node, Node node2, Node node3, int n, int n2) {
        node.setImage(n);
        node.setExpandedImage(n2);
        return this.insertItem(node, node2, node3);
    }

    public Node insertItem(Node node, Node node2, Node node3, int n, int n2, Font font) {
        node.setFont(font);
        return this.insertItem(node, node2, node3, n, n2);
    }

    public Node insertItem(String string, Node node, Node node2) {
        Node node3 = new Node();
        node3.setText(string);
        return this.insertItem(node3, node, node2);
    }

    public Node insertItem(String string, Node node, Node node2, int n, int n2) {
        Node node3 = new Node();
        node3.setText(string);
        return this.insertItem(node3, node, node2, n, n2);
    }

    public Node insertItem(String string, Node node, Node node2, int n, int n2, Font font) {
        Node node3 = new Node();
        node3.setText(string);
        return this.insertItem(node3, node, node2, n, n2, font);
    }

    protected boolean isChildOf(Object[] objectArray, Node node) {
        if (node == null) {
            return false;
        }
        int n = 0;
        while (n < objectArray.length) {
            Node node2 = (Node) ((ListItem) objectArray[n]).getItemData();
            if (node.isAncestor(node2)) {
                return true;
            }
            ++n;
        }
        return false;
    }

    public boolean isExpanded(Node node) {
        return node.isExpanded();
    }

    public boolean isFocusTraversable() {
        return true;
    }

    public boolean isRoot(int n) {
        int n2 = 0;
        while (n2 < this.m_arrRoots.size()) {
            if (this.nodeIndex((Node) this.m_arrRoots.elementAt(n2)) == n) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public boolean isRoot(Node node) {
        return this.m_arrRoots.contains(node);
    }

    public int measureSubItem(Graphics graphics) {
        try {
            if (this.m_lvi_iSubItem == 0) {
                int n;
                int n2;
                Object object;
                int n3 = this.getIndent() * (((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getDistanceFromRoot() - 1);
                n3 += this.getIndent();
                if (this.m_arrImages != null && this.m_arrImages.size() != 0) {
                    int n4 = ((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getImage();
                    object = this.getImageList(n4);
                    n2 = ((Image) object).getHeight(this);
                    int n5 = ((Image) object).getWidth(this);
                    ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).setImageBounds(new Rectangle(n3 + this.m_nWidthGap, 0, n5, n2));
                    Rectangle rectangle = ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getImageBounds();
                    n = rectangle.width + n3 + this.m_nWidthGap;
                } else {
                    n = n3 + this.m_nWordGap;
                }
                Font font = ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getFont();
                if (font == null) {
                    font = this.defaultfont;
                }
                object = this.getFontMetrics(font);
                if (this.m_bAutoWrap) {
                    ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).setTextBounds(n, 0, Math.min(((Column) this.m_arrColumns.elementAt(0)).getWidth(), ((FontMetrics) object).stringWidth(((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getText()) + n + this.m_nWidthGap), 0);
                } else {
                    ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).setTextBounds(n, 0, ((FontMetrics) object).stringWidth(((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getText()) + n + this.m_nWidthGap, 0);
                }
                n2 = this.measureText();
                ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).setTextHeight(n2);
                this.m_recPCRect = new Rectangle(((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getTextBounds());
                this.m_recPCRect.height = Math.max(this.m_recPCRect.height, ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().height);
                return this.m_recPCRect.height;
            }
            return super.measureSubItem(graphics);
        } catch (Exception exception) {
            return 1;
        }
    }

    protected int measureText() {
        Rectangle rectangle = new Rectangle();
        rectangle.y = 0;
        rectangle.height = 0;
        Font font = ((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getFont();
        if (font == null) {
            font = this.defaultfont;
        }
        FontMetrics fontMetrics = this.getFontMetrics(font);
        this.m_lvi_pszText = ((Node) ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getItemData()).getText();
        if (this.m_bAutoWrap && this.m_lvi_pszText != null) {
            int n = ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getTextBounds().x + this.m_nWidthGap + (this.getInsideRect().width + this.m_nVScrollbarWidth - this.getBounds().width);
            Vector vector = Text.wrapText(this.m_lvi_pszText, ((Column) this.m_arrColumns.elementAt(this.m_lvi_iSubItem)).getWidth() - this.m_nWidthGap * 2 - n, true, fontMetrics);
            rectangle.height = vector.size() * fontMetrics.getHeight();
        } else {
            rectangle.height = fontMetrics.getHeight();
        }
        return rectangle.height;
    }

    protected boolean mouseUp(MouseEvent mouseEvent, int n, int n2) {
        boolean bl;
        boolean bl2 = bl = this.m_bDragModeEnabled && this.m_bMouseDrag && this.m_nDraggingState == 0 || this.m_nDraggingState == 1;
        if (bl) {
            return super.mouseUp(mouseEvent, n, n2);
        }
        int[] nArray = this.getSelectedIndexes();
        if (nArray != null && nArray.length > 1) {
            return super.mouseUp(mouseEvent, n, n2);
        }
        if (!this.m_bEditModeAllowed || n < ((Column) this.m_arrColumns.elementAt(0)).getWidth() - this.m_ptViewportOrg.x) {
            return super.mouseUp(mouseEvent, n, n2);
        }
        int n3 = super.getSelected(n, n2);
        super.isSelected(n3);
        return true;
    }

    public void moveItems(Object[] objectArray, int n) {
        if (n == -1) {
            return;
        }
        ListItem listItem = (ListItem) this.m_arrItems.elementAt(n);
        this.moveItems(objectArray, listItem);
    }

    public void moveItems(Object[] objectArray, ListItem listItem) {
        if (listItem == null) {
            return;
        }
        Node node = (Node) listItem.getItemData();
        int n = objectArray.length - 1;
        while (n > -1) {
            Node node2 = (Node) ((ListItem) objectArray[n]).getItemData();
            if (objectArray[n] == listItem) {
                return;
            }
            if (node.isAncestor(node2)) {
                return;
            }
            if (node2.isParent(node)) {
                return;
            }
            if (this.isRoot(node2)) {
                return;
            }
            --n;
        }
        this.deselectAll(false);
        n = 0;
        int n2 = 0;
        while (n2 < objectArray.length) {
            Node node3 = (Node) ((ListItem) objectArray[n2]).getItemData();
            n = this.isChildOf(objectArray, node3) ? 1 : 0;
            if (n == 0) {
                if (node3.hasChildren()) {
                    this.removeChildrenFromListCtrl(node3, this.nodeIndex(node3), false);
                }
                this.delNodeFromListBox(node3, false);
                node3.detachFromTree();
                node.addChild(node3);
                if (this.shouldBeInListBox(node3)) {
                    this.addNodeToListBox(node3);
                }
            }
            ++n2;
        }
    }

    public int nodeIndex(Node node) {
        return this.nodeIndex(node, 0);
    }

    public int nodeIndex(Node node, int n) {
        int n2 = n;
        while (n2 < this.getItemCount()) {
            if (node == this.getNodeAt(n2)) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    protected boolean nodeInListBox(Node node) {
        return this.nodeIndex(node) != -1;
    }

    protected void processActionEvent(ActionEvent actionEvent) {
        if (this.actionListener != null) {
            this.actionListener.actionPerformed(actionEvent);
            return;
        }
        Node node = this.getSelectedNode();
        Container container = this.getParent();
        if (container != null) {
            int n;
            if (actionEvent.getActionCommand().equals(commandDragDrop)) {
                n = 3001;
            } else if (actionEvent.getActionCommand().equals(commandOpenFolder)) {
                n = 3002;
            } else if (actionEvent.getActionCommand().equals(commandSelectionChanged)) {
                n = 3003;
            } else if (actionEvent.getActionCommand().equals(commandItemCollapsed)) {
                n = 3004;
                node = this.m_itemEventTarget;
            } else if (actionEvent.getActionCommand().equals(commandItemExpanded)) {
                n = 3005;
                node = this.m_itemEventTarget;
            } else {
                n = actionEvent.getActionCommand().equals(this.commandDoubleClicked) ? 3006 : 3000;
            }
            container.postEvent(new Event(this, n, node));
        }
    }

    protected void processKeyEvent(KeyEvent keyEvent) {
        int n = keyEvent.getKeyCode();
        if (keyEvent.getID() == 401) {
            switch (n) {
                case 37: {
                    if (this.getSelectedNode() != null && this.getSelectedNode().isExpanded()) {
                        this.expand(this.getSelectedNode(), 0, true);
                        this.prev = this.nodeIndex(this.getSelectedNode());
                        break;
                    }
                    return;
                }
                case 39: {
                    if (this.getSelectedNode() != null) {
                        if (!this.getSelectedNode().isExpanded()) {
                            this.expand(this.getSelectedNode(), 1, true);
                            this.prev = this.nodeIndex(this.getSelectedNode());
                        }
                        this.reMeasureAllItems();
                        this.update();
                    }
                    return;
                }
            }
        }
        if (n != 39 && n != 37) {
            super.processKeyEvent(keyEvent);
        }
    }

    protected void removeChildrenFromListCtrl(Node node, int n) {
        this.removeChildrenFromListCtrl(node, n, true);
    }

    protected void removeChildrenFromListCtrl(Node node, int n, boolean bl) {
        Node node2;
        if (n == -1) {
            return;
        }
        int n2 = 0;
        int n3 = n + 1;
        while (n3 < this.getItemCount()) {
            node2 = this.getNodeAt(n3);
            if (!node.isDescendant(node2)) break;
            ++n3;
            ++n2;
        }
        int n4 = n + 1 + n2 - 1;
        int n5 = this.selected.length - 1;
        while (n5 >= 0) {
            int n6;
            int n7 = this.selected[n5];
            if (n7 > n && n7 < n4 && this.isSelected(n6 = this.nodeIndex(node2 = this.getNodeAt(n7)))) {
                this.deselect(n6);
            }
            --n5;
        }
        if (n2 > 0) {
            if (bl) {
                super.deleteItem(n + 1, n2);
            } else {
                super.deleteItem(n + 1, n2, false);
            }
        }
    }

    public void setHasFocus(boolean bl) {
        this.hasfocus = bl;
    }

    public void setHierarchyLineColor(Color color) {
        this.m_colHierarchyLines = color;
    }

    public void setHierarchyLineStyle(int n) {
        this.m_nStyleHierarchyLines = n;
    }

    protected boolean shouldBeInListBox(Node node) {
        return ((Node) node.getParent()).isExpanded() && this.nodeIndex((Node) node.getParent()) != -1;
    }
}

