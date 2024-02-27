/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.listbox;

import com.objectbox.runner.gui.obbase.OBBase;
import com.objectbox.runner.gui.text.ExpandableText;
import com.objectbox.runner.gui.text.Text;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class ListBox
        extends OBBase
        implements ItemSelectable,
        ActionListener {
    public static final int LVXC_UNINITIALIZED = -1;
    public static final int FMT_LEFT = 0;
    public static final int FMT_CENTER = 1;
    public static final int FMT_RIGHT = 2;
    public static final int NONE = 0;
    public static final int RESIZE = 1;
    public static final int DEFAULT_COL_WIDTH = 100;
    public static final int DOTTED = 0;
    public static final int SOLID = 1;
    public static final int DRAGDROP = 3001;
    public static String commandDragDrop = "Drag_Drop";
    public Font defaultfont = new Font("Dialog", 0, 12);
    public String m_oldName = "";
    protected int m_nWidthGap = 11;
    protected int m_nRowSpace = 0;
    protected int DottedLineSpace = 1;
    protected int DottedLineFill = 1;
    protected Rectangle m_recPCRect = new Rectangle();
    protected Column m_colPCol = new Column();
    protected int m_lvi_iSubItem = 0;
    protected int m_lvi_iItem = 0;
    protected int m_PCheight = -1;
    protected String m_lvi_pszText = null;
    protected Color m_colGridLines = SystemColor.windowBorder;
    protected Color m_colText = SystemColor.textText;
    protected Color m_colHighlightText = SystemColor.textHighlightText;
    protected Color m_colHighlightTextBackground = SystemColor.textHighlight;
    protected int m_nStyleGridLines = 1;
    protected String commandTopRowChanged = "Top_Row_Changed";
    protected String commandDoubleClicked = "Double_Clicked";
    protected String commandLeftColChanged = "Left_Col_Changed";
    protected String commandItemDeleted = "Item_Deleted";
    protected ItemListener itemListener;
    protected ActionListener actionListener;
    protected int[] selected = new int[0];
    protected int prev = -1;
    protected boolean m_bMouseDrag = false;
    protected boolean m_bHilightSubItems = true;
    protected Vector m_arrColumns = new Vector();
    protected Vector m_arrItems = new Vector();
    protected int m_nSubItemCount = 0;
    protected int m_nTopRow = 0;
    protected Point m_ptViewportOrg = new Point(0, 0);
    protected int m_nItemsThisPage = 0;
    protected boolean m_bMultipleSelections = true;
    protected boolean m_bColumnHeader = false;
    protected int m_cyHeader = 0;
    protected boolean m_bColumnLines = false;
    protected boolean m_bItemLines = false;
    protected boolean m_bEditModeAllowed = false;
    protected boolean m_bEditModeEnabled = false;
    protected boolean m_bOverlapEdit = false;
    protected int m_nColumnEdit = 0;
    protected ListItem m_itemCurrentEdit = new ListItem();
    protected ExpandableText m_textEditNode = null;
    protected Vector m_arrImages = null;
    protected Vector m_arrImageIDs = null;
    protected int m_nOldTargetIndex = -1;
    protected int m_nDraggingCurrent;
    protected int m_nDraggingBegin;
    protected int m_nDraggingState = 0;
    protected Rectangle m_rectDragging = new Rectangle();
    protected boolean m_bAutoWrap = true;
    protected boolean m_bDragModeEnabled = false;
    protected boolean m_bDragModeAllowed = true;
    protected boolean m_bDrawDragImage = true;
    protected int m_nXMouse = -100;
    protected int m_nYMouse = -100;
    protected Image m_imgDragPic = null;
    protected int m_dropTargetItem = -1;
    protected boolean m_bDragDropDrawn = false;
    protected boolean m_bAllowDelete = false;
    protected boolean m_bShowDotRect = true;
    protected boolean m_bHasFocus = false;
    protected transient ItemTextChangedListener aItemTextChangedListener = null;
    boolean m_bCaptured = false;
    transient boolean m_bSelectedItemChanged = false;
    private String OSName;

    public ListBox() {
        this(false);
        this.setLayout(null);
    }

    public ListBox(boolean bl) {
        super(false);
        this.m_bMultipleSelections = bl;
        this.m_bAlwaysShowScrollbar = false;
        this.setBackground(Color.white);
        this.insertColumn(0, "", 0, -1, 0, false);
        this.OSName = System.getProperty("os.name");
        this.m_textEditNode = new ExpandableText("");
        this.m_textEditNode.addActionListener(this);
        this.add(this.m_textEditNode);
        this.m_textEditNode.setVisible(false);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.m_textEditNode && actionEvent.getActionCommand().equals(Text.endEditing)) {
            this.changeItemText();
        }
    }

    public void addActionListener(ActionListener actionListener) {
        this.actionListener = AWTEventMulticaster.add(this.actionListener, actionListener);
    }

    public synchronized void addItem(ListItem listItem) {
        this.addItem(listItem, -1, true);
    }

    public synchronized void addItem(ListItem listItem, int n, boolean bl) {
        listItem.setImage(n);
        this.m_arrItems.insertElementAt(listItem, this.m_arrItems.size());
        if (this.m_nSubItemCount > listItem.getSubItemCount() + 1) {
            int n2 = listItem.getSubItemCount();
            while (n2 < this.m_nSubItemCount - 1) {
                ((ListItem) this.m_arrItems.elementAt(this.m_arrItems.size() - 1)).addSubItem();
                ++n2;
            }
        }
        if (bl) {
            this.update();
        }
    }

    public synchronized void addItem(ListItem listItem, boolean bl) {
        this.addItem(listItem, -1, bl);
    }

    public synchronized void addItem(Object object) {
        this.addItem(object, -1, true);
    }

    public synchronized void addItem(Object object, int n, boolean bl) {
        if (object != null && object instanceof String) {
            this.addItem((String) object, n, bl);
        } else if (object != null && object instanceof ListItem) {
            this.addItem((ListItem) object, n, bl);
        }
    }

    public synchronized void addItem(Object object, boolean bl) {
        this.addItem(object, -1, bl);
    }

    public synchronized void addItem(String string) {
        this.addItem(string, -1, true);
    }

    public synchronized void addItem(String string, int n, boolean bl) {
        ListItem listItem = new ListItem();
        listItem.setText(string);
        listItem.setImage(n);
        this.m_arrItems.addElement(listItem);
        if (this.m_nSubItemCount > listItem.getSubItemCount() + 1) {
            int n2 = listItem.getSubItemCount();
            while (n2 < this.m_nSubItemCount - 1) {
                ((ListItem) this.m_arrItems.elementAt(this.m_arrItems.size() - 1)).addSubItem();
                ++n2;
            }
        }
        if (bl) {
            this.updateScrollbar();
            this.doLayout();
            this.repaint();
        }
    }

    public synchronized void addItem(String string, Font font) {
        this.addItem(string, font, -1);
    }

    public synchronized void addItem(String string, Font font, int n) {
        ListItem listItem = new ListItem();
        listItem.setText(string);
        listItem.setFont(font);
        listItem.setImage(n);
        this.m_arrItems.addElement(listItem);
        if (this.m_nSubItemCount > listItem.getSubItemCount() + 1) {
            int n2 = listItem.getSubItemCount();
            while (n2 < this.m_nSubItemCount - 1) {
                ((ListItem) this.m_arrItems.elementAt(this.m_arrItems.size() - 1)).addSubItem();
                ++n2;
            }
        }
    }

    public synchronized void addItem(String string, boolean bl) {
        this.addItem(string, -1, bl);
    }

    public void addItemListener(ItemListener itemListener) {
        this.itemListener = AWTEventMulticaster.add(this.itemListener, itemListener);
    }

    public void addItemTextChangedListener(ItemTextChangedListener itemTextChangedListener) {
        this.aItemTextChangedListener = ItemTextChangedEventMulticaster.add(this.aItemTextChangedListener, itemTextChangedListener);
    }

    public void addNotify() {
        FontMetrics fontMetrics;
        super.addNotify();
        Font font = this.getFont();
        if (font != null) {
            fontMetrics = this.getFontMetrics(font);
            this.defaultfont = font;
        } else {
            fontMetrics = this.getFontMetrics(this.defaultfont);
        }
        this.m_PCheight = fontMetrics.getHeight();
        if (this.m_arrColumns.size() == 1) {
            if (this.m_bAutoWrap) {
                ((Column) this.m_arrColumns.elementAt((int) 0)).m_nCX = this.getInsideRect().width;
                this.reMeasureAllItems();
            } else {
                ((Column) this.m_arrColumns.elementAt((int) 0)).m_nCX = this.getLogicalSize().x;
                if (this.getLogicalSize().x < this.getInsideRect().width) {
                    ((Column) this.m_arrColumns.elementAt((int) 0)).m_nCX = this.getInsideRect().width;
                }
                this.reMeasureAllItems();
            }
        }
    }

    public synchronized void addSubItem(int n, String string) {
        this.addSubItem(n, string, true);
    }

    public synchronized void addSubItem(int n, String string, boolean bl) {
        if (n <= this.m_arrItems.size() - 1) {
            ((ListItem) this.m_arrItems.elementAt(n)).addSubItem(string);
            if (bl && this.m_arrColumns.size() - 1 < ((ListItem) this.m_arrItems.elementAt(n)).getSubItemCount()) {
                int n2 = ((ListItem) this.m_arrItems.elementAt(n)).getSubItemCount();
                int n3 = this.m_arrColumns.size() - 1;
                while (n3 < n2) {
                    this.insertColumn(n3, "", 0, -1, -1, false);
                    ++n3;
                }
            }
        }
    }

    public int calcItemsInRange(int n, int n2, boolean bl) {
        ListItem listItem = new ListItem();
        int n3 = 0;
        int n4 = 0;
        if (bl) {
            int n5 = n2;
            while (n5 < this.m_arrItems.size()) {
                listItem = (ListItem) this.m_arrItems.elementAt(n5);
                ++n4;
                if ((n3 += listItem.m_nCY) < n) {
                    ++n5;
                    continue;
                }
                break;
            }
        } else {
            int n6 = n2;
            while (n6 >= 0) {
                listItem = (ListItem) this.m_arrItems.elementAt(n6);
                ++n4;
                if ((n3 += listItem.m_nCY) < n) {
                    --n6;
                    continue;
                }
                break;
            }
        }
        return n4;
    }

    public int calcRangeHeight(int n, int n2, boolean bl) {
        ListItem listItem = new ListItem();
        boolean bl2 = false;
        if (n > n2) {
            int n3 = n;
            n = n2;
            n2 = n3;
            bl2 = true;
        }
        int n4 = 0;
        int n5 = n;
        while (n5 < n2 || bl && n5 <= n2) {
            listItem = (ListItem) this.m_arrItems.elementAt(n5);
            if (listItem.m_nCY == -1) {
                this.measureItem(n5, this.getGraphics());
            }
            n4 += listItem.m_nCY;
            ++n5;
        }
        if (bl2) {
            n4 *= -1;
        }
        return n4;
    }

    protected void changeItemText() {
        if (this.m_nColumnEdit == 0) {
            this.m_itemCurrentEdit.setText(this.m_textEditNode.getText());
        } else {
            this.m_itemCurrentEdit.getSubItem(this.m_nColumnEdit).setText(this.m_textEditNode.getText());
        }
        this.m_textEditNode.stop();
        this.m_textEditNode.setVisible(false);
        this.reMeasureAllItems();
        this.update();
    }

    public synchronized void clear() {
        this.m_arrItems = new Vector();
        this.selected = new int[0];
        this.prev = -1;
        this.m_nTopRow = 0;
        this.updateScrollbar();
        this.update();
    }

    public int countItems() {
        return this.getItemCount();
    }

    public ListItem createNewItem() {
        ListItem listItem = new ListItem();
        return listItem;
    }

    public synchronized boolean deleteColumn(int n, boolean bl) {
        int n2 = this.m_arrColumns.size();
        if (n < 1 || n >= n2) {
            return false;
        }
        int n3 = n + 1;
        while (n3 < n2) {
            --((Column) this.m_arrColumns.elementAt((int) n3)).m_iSubItem;
            ++n3;
        }
        this.m_arrColumns.removeElementAt(n);
        n3 = 0;
        while (n3 < this.m_arrItems.size()) {
            ListItem listItem = (ListItem) this.m_arrItems.elementAt(n3);
            listItem.deleteSubItem(n);
            ++n3;
        }
        --this.m_nSubItemCount;
        if (bl) {
            this.updateScrollbar();
            this.doLayout();
            this.repaint();
        }
        return true;
    }

    public synchronized boolean deleteItem(int n) {
        return this.deleteItem(n, 1);
    }

    public synchronized boolean deleteItem(int n, int n2) {
        return this.deleteItem(n, n2, true);
    }

    public synchronized boolean deleteItem(int n, int n2, boolean bl) {
        int n3 = n + n2 - 1;
        if (n3 > this.m_arrItems.size() - 1) {
            n3 = this.m_arrItems.size() - 1;
        }
        Vector vector = new Vector();
        try {
            int n4 = 0;
            while (n4 < this.selected.length) {
                vector.addElement(this.m_arrItems.elementAt(this.selected[n4]));
                ++n4;
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in Listbox::deleteItem: " + throwable);
        }
        int n5 = n;
        while (n5 <= n3) {
            ListItem listItem = (ListItem) this.m_arrItems.elementAt(n5);
            if (this.isSelected(n5)) {
                this.deselect(n5);
            }
            this.m_arrItems.removeElementAt(n5);
            vector.removeElement(listItem);
            --n3;
            --n5;
            ++n5;
        }
        int n6 = 0;
        while (n6 < vector.size()) {
            this.select(this.getIndex((ListItem) vector.elementAt(n6)), false);
            ++n6;
        }
        this.processActionEvent(new ActionEvent(this, 1001, this.commandItemDeleted));
        if (bl) {
            this.updateScrollbar();
            this.doLayout();
            this.update();
        }
        return true;
    }

    public synchronized boolean deleteItem(int n, boolean bl) {
        return this.deleteItem(n, 1, bl);
    }

    public synchronized void deleteItems(int n, int n2) {
        if (n < 0 || n >= this.countItems() || n2 < 0 || n2 >= this.countItems()) {
            return;
        }
        int n3 = n2 - n + 1;
        this.deleteItem(n, n3, true);
    }

    public synchronized void delItem(int n) {
        this.deleteItem(n);
    }

    public synchronized void delItems(int n, int n2) {
        this.deleteItems(n, n2);
    }

    public synchronized void deselect(int n) {
        int n2 = 0;
        while (n2 < this.selected.length) {
            if (this.selected[n2] == n) {
                int[] nArray = new int[this.selected.length - 1];
                System.arraycopy(this.selected, 0, nArray, 0, n2);
                System.arraycopy(this.selected, n2 + 1, nArray, n2, this.selected.length - (n2 + 1));
                this.selected = nArray;
                this.processItemEvent(new ItemEvent(this, 701, new Integer(n), 2));
                return;
            }
            ++n2;
        }
    }

    public synchronized void deselectAll() {
        this.deselectAll(true);
    }

    public synchronized void deselectAll(boolean bl) {
        this.selected = new int[0];
        this.prev = -1;
        if (bl) {
            this.update();
        }
        this.processItemEvent(new ItemEvent(this, 701, null, 2));
    }

    public void doLayout() {
        Object object = this.getTreeLock();
        synchronized (object) {
            Scrollbar scrollbar = this.getVScrollbar();
            Scrollbar scrollbar2 = this.getHScrollbar();
            this.getInsideRect();
            Rectangle rectangle = this.getBounds();
            int n = rectangle.width;
            int n2 = rectangle.height;
            if (this.m_bAlwaysShowScrollbar || this.getMaxTopRow() != 0) {
                if (this.getMaxLeftCol() != 0 || this.m_bAlwaysShowScrollbar) {
                    scrollbar2.setBounds(0, n2 - this.m_nHScrollbarHeight, n - this.m_nVScrollbarWidth, this.m_nHScrollbarHeight);
                } else {
                    scrollbar2 = null;
                }
            } else if (this.getMaxLeftCol() != 0 || this.m_bAlwaysShowScrollbar) {
                scrollbar2.setBounds(0, n2 - this.m_nHScrollbarHeight, n, this.m_nHScrollbarHeight);
            } else {
                scrollbar2 = null;
            }
            if (this.m_bAlwaysShowScrollbar || this.getMaxLeftCol() != 0) {
                if (this.getMaxTopRow() > 0 || this.m_bAlwaysShowScrollbar) {
                    scrollbar.setBounds(n - this.m_nVScrollbarWidth, 0, this.m_nVScrollbarWidth, n2 - this.m_nHScrollbarHeight);
                } else {
                    scrollbar = null;
                }
            } else if (this.getMaxTopRow() > 0 || this.m_bAlwaysShowScrollbar) {
                scrollbar.setBounds(n - this.m_nVScrollbarWidth, 0, this.m_nVScrollbarWidth, n2);
            } else {
                scrollbar = null;
            }
        }
    }

    protected void doubleClickEvent(int n) {
    }

    public void draw(Graphics graphics) {
        Rectangle rectangle = this.getBounds();
        if (this.m_nItemsThisPage == 0) {
            this.updateScrollbar();
        }
        Rectangle rectangle2 = this.getInsideRect();
        if (this.isHeaderCtrlEnabled()) {
            this.drawHeader(graphics);
        }
        if (this.m_arrItems.size() > 0) {
            if (this.m_nDraggingState == 0) {
                this.drawInvalidItems(graphics);
            }
            if (this.vs != null && this.vs.isShowing() && this.hs != null && this.hs.isShowing()) {
                rectangle2 = this.getInsideRect();
                Rectangle rectangle3 = rectangle;
                Rectangle rectangle4 = new Rectangle();
                rectangle4.x = rectangle3.width - this.vs.getBounds().width;
                rectangle4.width = rectangle3.width - rectangle2.width;
                rectangle4.y = 0 + rectangle3.height - this.hs.getBounds().height;
                rectangle4.height = rectangle3.height - rectangle2.height;
                graphics.clearRect(rectangle4.x, rectangle4.y, rectangle4.width, rectangle4.height);
            }
        }
        this.paintBorder(graphics, 0, 0, rectangle.width, rectangle.height, false);
    }

    public void drawColumnLines(Graphics graphics) {
        Rectangle rectangle = this.getBounds();
        graphics.setColor(this.m_colGridLines);
        int n = -this.m_ptViewportOrg.x;
        int n2 = 0;
        while (n2 < this.m_arrColumns.size()) {
            n += ((Column) this.m_arrColumns.elementAt(n2)).getWidth();
            if (this.m_nStyleGridLines == 0) {
                this.drawDottedLine(graphics, n, this.m_cyHeader, n, rectangle.y + rectangle.height);
            } else {
                graphics.drawLine(n, this.m_cyHeader, n, rectangle.y + rectangle.height);
            }
            ++n2;
        }
    }

    protected void drawDottedLine(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5 = Math.abs(n - n3);
        boolean bl = false;
        if (n5 == 0) {
            n5 = Math.abs(n2 - n4);
            bl = true;
        }
        int n6 = 0;
        while (n6 <= n5) {
            if (bl) {
                if (n2 < n4) {
                    graphics.drawLine(n, n2 + n6, n, n2 + n6 + this.DottedLineFill - 1);
                } else {
                    graphics.drawLine(n, n4 + n6, n, n4 + n6 + this.DottedLineFill - 1);
                }
            } else if (n < n3) {
                graphics.drawLine(n + n6, n2, n + n6 + this.DottedLineFill - 1, n2);
            } else {
                graphics.drawLine(n3 + n6, n2, n3 + n6 + this.DottedLineFill - 1, n2);
            }
            n6 += this.DottedLineSpace + this.DottedLineFill;
        }
    }

    protected void drawDottedRect(Graphics graphics, int n, int n2, int n3, int n4) {
        Color color = graphics.getColor();
        graphics.setColor(SystemColor.control);
        graphics.drawRect(n, n2, n3, n4);
        graphics.setColor(color);
        this.drawDottedLine(graphics, n + 1, n2, n + n3, n2);
        this.drawDottedLine(graphics, n, n2, n, n2 + n4);
        this.drawDottedLine(graphics, n + n3, n2, n + n3, n2 + n4);
        this.drawDottedLine(graphics, n, n2 + n4, n + n3, n2 + n4);
    }

    public void drawFocusRect(Graphics graphics, int n, int n2, int n3, int n4) {
        if (this.m_bHasFocus && this.getSelectedListItem() == null) {
            ListItem listItem = this.getItemAt(this.m_nTopRow);
            Rectangle rectangle = listItem.getTextBounds();
            if (this.isColumnHeader()) {
                n2 += this.getColumnHeaderHeight();
            }
            this.drawDottedRect(graphics, n + 3, n2 + 3, n3 - 6, rectangle.height);
        }
    }

    private void drawHeader(Graphics graphics) {
        Column column;
        int n;
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = this.getBounds();
        Point point = this.getLogicalSize();
        rectangle.y = 0;
        rectangle.height = this.m_cyHeader - 1;
        rectangle.x = -this.m_ptViewportOrg.x;
        rectangle.width = Math.max(point.x, rectangle2.width + 1);
        graphics.setColor(this.getBackground());
        graphics.fillRect(rectangle.x - 2, rectangle.y, rectangle.width, rectangle.height);
        graphics.setColor(SystemColor.control);
        graphics.fill3DRect(rectangle.x - 2, rectangle.y, rectangle.width, rectangle.height, true);
        this.paintBorder(graphics, rectangle.x + 2, rectangle.y + 2, rectangle.width - 2, rectangle.height - 2, true);
        if (this.m_arrColumns.size() > 1) {
            int n2 = this.m_arrColumns.size();
            n = 0;
            while (n < n2) {
                column = (Column) this.m_arrColumns.elementAt(n);
                rectangle.width = column.m_nCX;
                graphics.drawLine(rectangle.x + rectangle.width, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height);
                rectangle.x += rectangle.width;
                ++n;
            }
        }
        rectangle.x = -this.m_ptViewportOrg.x;
        n = 0;
        while (n < this.m_arrColumns.size()) {
            int n3;
            int n4;
            column = (Column) this.m_arrColumns.elementAt(n);
            rectangle.width = column.m_nCX - 1;
            graphics.setFont(column.m_fonFont);
            String string = column.m_strText;
            FontMetrics fontMetrics = graphics.getFontMetrics(graphics.getFont());
            int n5 = fontMetrics.stringWidth(column.m_strText);
            String string2 = "";
            int n6 = 0;
            if (n5 > column.m_nCX - this.m_nWidthGap) {
                n4 = fontMetrics.stringWidth(".");
                if (n6 + this.m_nWidthGap + n4 > column.m_nCX) {
                    n5 = n6;
                    string2 = " ";
                } else if (n6 + this.m_nWidthGap + 2 * n4 > column.m_nCX) {
                    n5 = n6 + this.m_nWidthGap + n4;
                    string2 = ".";
                } else if (n6 + this.m_nWidthGap + 3 * n4 > column.m_nCX) {
                    n5 = n6 + this.m_nWidthGap + n4;
                    string2 = "..";
                } else {
                    n3 = 0;
                    n3 = 1;
                    while (n3 < string.length()) {
                        n5 = n6 + this.m_nWidthGap * 2 + fontMetrics.stringWidth(string.substring(0, n3 - 1)) + 3 * n4;
                        if (n5 > column.m_nCX) break;
                        ++n3;
                    }
                    string2 = String.valueOf(string.substring(0, n3 - 1)) + "...";
                    n5 = n6 + this.m_nWidthGap + fontMetrics.stringWidth(string2);
                }
            }
            if (fontMetrics.stringWidth(column.m_strText) > column.m_nCX - this.m_nWidthGap) {
                string = string2;
            }
            n4 = rectangle.x + this.m_nWidthGap;
            n3 = rectangle.y + rectangle.height - rectangle.height / 4;
            int n7 = this.m_nWidthGap + fontMetrics.stringWidth(string);
            if (column.m_nHeaderFmt == 1) {
                if (n7 < column.m_nCX) {
                    n4 = column.m_nCX / 2 - (n7 - this.m_nWidthGap) / 2 + n4 - this.m_nWidthGap;
                }
            } else if (column.m_nHeaderFmt == 2 && n7 < column.m_nCX) {
                n4 = column.m_nCX - (n7 - this.m_nWidthGap) + n4 - 2 * this.m_nWidthGap;
            }
            graphics.drawString(string, n4, n3);
            rectangle.x = rectangle.x + rectangle.width + 1;
            ++n;
        }
    }

    public void drawInvalidItems(Graphics graphics) {
        Rectangle rectangle = new Rectangle();
        int n = this.m_arrItems.size();
        Rectangle rectangle2 = this.getInsideRect();
        Point point = this.getLogicalSize();
        rectangle.y = rectangle2.y;
        rectangle.y += 2;
        rectangle.x = -this.m_ptViewportOrg.x;
        rectangle.width = point.x;
        rectangle.height = 0;
        int n2 = 0;
        this.m_lvi_iItem = this.m_nTopRow;
        while (this.m_lvi_iItem < n) {
            if (((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_nCY == -1) {
                ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_nCY = this.measureItem(graphics);
            }
            rectangle.height += ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_nCY;
            Rectangle rectangle3 = this.getBounds();
            rectangle3.x = 0;
            rectangle3.width *= 2;
            boolean bl = rectangle.intersects(rectangle3);
            if (!bl) break;
            this.m_recPCRect = rectangle;
            ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_rcText.y = rectangle.y;
            this.drawItem(graphics);
            rectangle.y += rectangle.height;
            rectangle.y += this.m_nRowSpace;
            rectangle.height = 0;
            if (rectangle.y > rectangle2.height + rectangle2.y) break;
            ++this.m_lvi_iItem;
            ++n2;
        }
        this.vs.setVisibleAmount(this.itemsThisPage(graphics));
        this.vs.setMaximum(this.m_arrItems.size());
        this.vs.setValue(this.m_nTopRow);
        this.vs.setBlockIncrement(Math.max(n2, 1));
        if (this.m_bColumnLines && this.m_arrColumns.size() > 1) {
            this.drawColumnLines(graphics);
        }
        --this.m_lvi_iItem;
    }

    public void drawItem(Graphics graphics) {
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = this.m_recPCRect;
        rectangle.y = rectangle2.y;
        rectangle.height = rectangle2.height;
        rectangle.x = rectangle2.x;
        rectangle.width = 0;
        int n = this.m_arrColumns.size();
        int n2 = 0;
        while (n2 < n) {
            this.m_colPCol = (Column) this.m_arrColumns.elementAt(n2);
            rectangle.width = n > 1 ? this.m_colPCol.m_nCX : Math.max(this.m_colPCol.m_nCX, ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_rcText.width);
            if (rectangle.intersects(rectangle2)) {
                this.m_lvi_iSubItem = this.m_colPCol.m_iSubItem;
                this.m_lvi_pszText = this.m_lvi_iSubItem != 0 ? ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getSubItem((int) this.m_lvi_iSubItem).pszText : ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_pszText;
                this.m_recPCRect = new Rectangle(rectangle);
                this.drawSubItem(graphics);
            }
            rectangle.x += rectangle.width;
            this.m_recPCRect.x = rectangle.x;
            ++n2;
        }
        if (this.m_bItemLines) {
            this.drawItemLines(graphics, rectangle2);
        }
    }

    public void drawItemLines(Graphics graphics, Rectangle rectangle) {
        Rectangle rectangle2 = this.getBounds();
        graphics.setColor(this.m_colGridLines);
        if (this.m_nStyleGridLines == 0) {
            this.drawDottedLine(graphics, rectangle.x, rectangle.y + rectangle.height + this.m_nRowSpace / 2 - rectangle2.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height + this.m_nRowSpace / 2 - rectangle2.y);
        } else {
            graphics.drawLine(rectangle.x, rectangle.y + rectangle.height + this.m_nRowSpace / 2 - rectangle2.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height + this.m_nRowSpace / 2 - rectangle2.y);
        }
    }

    public void drawSubItem(Graphics graphics) {
        Rectangle rectangle = this.getBounds();
        if (this.m_lvi_pszText != null) {
            if (((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getFont() != null) {
                graphics.setFont(((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getFont());
            } else {
                graphics.setFont(this.defaultfont);
            }
            int cfr_ignored_0 = this.m_recPCRect.x;
            FontMetrics fontMetrics = graphics.getFontMetrics();
            int n = fontMetrics.getHeight();
            if (!this.m_bAutoWrap) {
                int n2;
                int n3;
                int n4;
                String string = "";
                int n5 = 0;
                if (this.m_lvi_iSubItem == 0) {
                    n5 = ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().width + this.m_nWidthGap;
                }
                if ((n4 = fontMetrics.stringWidth(this.m_lvi_pszText) + n5) > this.m_recPCRect.width - 2 * this.m_nWidthGap - (rectangle.width - this.getInsideRect().width)) {
                    n3 = fontMetrics.stringWidth(".");
                    if (n5 + this.m_nWidthGap + n3 > this.m_recPCRect.width - (rectangle.width - this.getInsideRect().width)) {
                        n4 = n5;
                        string = " ";
                    } else if (n5 + this.m_nWidthGap + 2 * n3 > this.m_recPCRect.width - (rectangle.width - this.getInsideRect().width)) {
                        n4 = n5 + this.m_nWidthGap + n3;
                        string = ".";
                    } else if (n5 + this.m_nWidthGap + 3 * n3 > this.m_recPCRect.width - (rectangle.width - this.getInsideRect().width)) {
                        n4 = n5 + this.m_nWidthGap + 2 * n3;
                        string = "..";
                    } else {
                        n2 = 0;
                        n2 = 1;
                        while (n2 < this.m_lvi_pszText.length()) {
                            n4 = n5 + 2 * this.m_nWidthGap + fontMetrics.stringWidth(this.m_lvi_pszText.substring(0, n2 - 1)) + 3 * n3;
                            if (n4 > this.m_recPCRect.width) break;
                            ++n2;
                        }
                        if (n2 - 2 < 0) {
                            n2 = 2;
                        }
                        string = String.valueOf(this.m_lvi_pszText.substring(0, n2 - 2)) + "...";
                        n4 = n5 + this.m_nWidthGap + fontMetrics.stringWidth(string);
                    }
                }
                if (fontMetrics.stringWidth(this.m_lvi_pszText) + n5 > this.m_recPCRect.width - 2 * this.m_nWidthGap) {
                    this.m_lvi_pszText = string;
                }
                if (!this.isSelected(this.m_lvi_iItem) || this.m_lvi_iSubItem != 0 && !this.m_bHilightSubItems) {
                    graphics.setColor(this.m_colText);
                } else {
                    graphics.setColor(this.m_colHighlightTextBackground);
                    graphics.fillRect(this.m_recPCRect.x + 1, this.m_recPCRect.y - rectangle.y + 1, this.m_recPCRect.width - 3, this.m_recPCRect.height - 1);
                    if (this.m_bShowDotRect && this.m_bHasFocus) {
                        this.drawDottedRect(graphics, this.m_recPCRect.x, this.m_recPCRect.y - rectangle.y, this.m_recPCRect.width - 1, this.m_recPCRect.height);
                    }
                    graphics.setColor(this.m_colHighlightText);
                }
                n3 = this.m_recPCRect.x;
                n3 = this.m_lvi_iSubItem == 0 ? (n3 += this.m_nWidthGap + ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().width) : (n3 += this.m_nWidthGap);
                n2 = this.m_recPCRect.y + ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_rcText.height - rectangle.y - ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_rcText.height / 4;
                int n6 = this.m_nWidthGap + fontMetrics.stringWidth(this.m_lvi_pszText);
                Column column = (Column) this.m_arrColumns.elementAt(this.m_lvi_iSubItem);
                if (column.m_nFmt == 1) {
                    if (n6 < column.m_nCX) {
                        n3 = column.m_nCX / 2 - (n6 - this.m_nWidthGap) / 2 + n3 - this.m_nWidthGap;
                    }
                } else if (column.m_nFmt == 2 && n6 < column.m_nCX) {
                    n3 = column.m_nCX - (n6 - this.m_nWidthGap) + n3 - 2 * this.m_nWidthGap;
                }
                int n7 = ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getImage();
                int n8 = this.getImageIndex(n7);
                if (this.m_lvi_iSubItem == 0 && n8 != -1 && this.m_arrImages != null && this.m_arrImages.size() > 0 && n8 < this.m_arrImages.size()) {
                    Image image;
                    int n9 = this.m_recPCRect.x + this.m_nWidthGap;
                    int n10 = this.m_recPCRect.y - rectangle.y;
                    int n11 = ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getTextBounds().height;
                    int n12 = ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().height;
                    if (n11 > n12) {
                        n10 += (n11 - n12) / 2;
                    }
                    if ((image = (Image) this.m_arrImages.elementAt(n8)) != null) {
                        graphics.drawImage(image, n9, n10, this);
                    }
                    n3 += this.m_nWidthGap;
                }
                graphics.drawString(this.m_lvi_pszText, n3, n2);
            } else {
                int n13;
                int n14;
                Vector vector = new Vector();
                vector = this.m_lvi_iSubItem == 0 ? Text.wrapText(this.m_lvi_pszText, ((Column) this.m_arrColumns.elementAt((int) this.m_lvi_iSubItem)).m_nCX - this.m_nWidthGap * 2 - ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().width, true, fontMetrics) : Text.wrapText(this.m_lvi_pszText, ((Column) this.m_arrColumns.elementAt((int) this.m_lvi_iSubItem)).m_nCX - this.m_nWidthGap * 2, true, fontMetrics);
                if (!this.isSelected(this.m_lvi_iItem) || this.m_lvi_iSubItem != 0 && !this.m_bHilightSubItems) {
                    graphics.setColor(this.m_colText);
                } else {
                    graphics.setColor(this.m_colHighlightTextBackground);
                    graphics.fillRect(this.m_recPCRect.x + 1, this.m_recPCRect.y - rectangle.y + 1, this.m_recPCRect.width - 3, this.m_recPCRect.height - 1);
                    if (this.m_bShowDotRect && this.m_bHasFocus) {
                        this.drawDottedRect(graphics, this.m_recPCRect.x, this.m_recPCRect.y - rectangle.y, this.m_recPCRect.width - 1, this.m_recPCRect.height);
                    }
                    graphics.setColor(this.m_colHighlightText);
                }
                int n15 = this.m_recPCRect.y;
                int n16 = this.m_recPCRect.x + this.m_nWidthGap;
                int n17 = n - rectangle.y - n / 4;
                int n18 = 0;
                Column column = (Column) this.m_arrColumns.elementAt(this.m_lvi_iSubItem);
                int n19 = ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getImage();
                int n20 = this.getImageIndex(n19);
                if (this.m_lvi_iSubItem == 0 && n20 != -1 && this.m_arrImages != null && this.m_arrImages.size() > 0 && n20 < this.m_arrImages.size()) {
                    int n21;
                    Image image;
                    int n22;
                    n14 = this.m_recPCRect.x + this.m_nWidthGap;
                    n13 = this.m_recPCRect.y - rectangle.y;
                    int n23 = fontMetrics.getHeight();
                    if (n23 > (n22 = ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().height)) {
                        n13 += (n23 - n22) / 2;
                    }
                    if ((image = (Image) this.m_arrImages.elementAt(n20)) != null && (n21 = image.getWidth(this)) < this.m_recPCRect.width - this.m_nWidthGap) {
                        graphics.drawImage(image, n14, n13, this);
                        n16 += this.m_nWidthGap;
                    }
                }
                n14 = 0;
                while (n14 < vector.size()) {
                    n16 = this.m_recPCRect.x + this.m_nWidthGap;
                    if (this.m_lvi_iSubItem == 0) {
                        n16 += ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().width;
                    }
                    n13 = ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getImage();
                    if (this.m_lvi_iSubItem == 0 && n13 != -1 && this.m_arrImages != null && this.m_arrImages.size() > 0 && n13 < this.m_arrImages.size()) {
                        n16 += this.m_nWidthGap;
                    }
                    n18 = this.m_nWidthGap + fontMetrics.stringWidth((String) vector.elementAt(n14));
                    if (column.m_nFmt == 1) {
                        if (n18 < column.m_nCX) {
                            n16 = column.m_nCX / 2 - (n18 - this.m_nWidthGap) / 2 + n16 - this.m_nWidthGap;
                        }
                    } else if (column.m_nFmt == 2 && n18 < column.m_nCX) {
                        n16 = column.m_nCX - (n18 - this.m_nWidthGap) + n16 - 2 * this.m_nWidthGap;
                    }
                    if (n18 + n16 - this.m_nWidthGap - 2 < this.m_recPCRect.x + this.m_recPCRect.width) {
                        graphics.drawString((String) vector.elementAt(n14), n16, n15 + n17);
                    }
                    n15 = n15 + n + this.m_nRowSpace;
                    ++n14;
                }
            }
        }
    }

    protected void drawTargetHighLight(Graphics graphics) {
        ListItem listItem;
        Rectangle rectangle = this.getBounds();
        int n = this.getTarget(this.m_nXMouse, this.m_nYMouse);
        if (n == this.m_nOldTargetIndex) {
            return;
        }
        graphics.setColor(SystemColor.textHighlight);
        if (n >= 0 && !this.isSelected(n)) {
            graphics.setColor(Color.red);
            listItem = (ListItem) this.m_arrItems.elementAt(n);
            graphics.drawLine(0, listItem.getTextBounds().y - rectangle.y, rectangle.x + this.getInsideRect().width, listItem.getTextBounds().y - rectangle.y);
        }
        if (this.m_nOldTargetIndex != -1 && !this.isSelected(this.m_nOldTargetIndex)) {
            graphics.setColor(Color.red);
            listItem = (ListItem) this.m_arrItems.elementAt(this.m_nOldTargetIndex);
            graphics.drawLine(0, listItem.getTextBounds().y - rectangle.y, this.getInsideRect().width + rectangle.x, listItem.getTextBounds().y - rectangle.y);
        }
        this.m_nOldTargetIndex = n;
    }

    protected void editItem() {
        this.m_bEditModeEnabled = false;
        this.m_bDragModeEnabled = false;
        Rectangle rectangle = this.getBounds();
        this.m_itemCurrentEdit = this.getSelectedListItem();
        ListItem listItem = (ListItem) this.m_arrItems.elementAt(this.selected[this.selected.length - 1]);
        this.m_nColumnEdit = 0;
        int n = 0;
        if (this.m_nXMouse < ((Column) this.m_arrColumns.elementAt(0)).getWidth() - this.m_ptViewportOrg.x) {
            this.m_textEditNode.setText(this.m_itemCurrentEdit.getText());
        } else {
            n = ((Column) this.m_arrColumns.elementAt(0)).getWidth() - this.m_ptViewportOrg.x;
            int n2 = 1;
            while (n2 < this.m_arrColumns.size()) {
                if (this.m_nXMouse < (n += ((Column) this.m_arrColumns.elementAt(n2)).getWidth())) break;
                ++n2;
            }
            this.m_nColumnEdit = n2;
            this.m_textEditNode.setText(this.m_itemCurrentEdit.getSubItem(n2).getText());
            n -= ((Column) this.m_arrColumns.elementAt(n2)).getWidth();
        }
        this.m_textEditNode.setBorderStyle(3);
        this.m_textEditNode.setTextHIndent(1);
        this.m_textEditNode.setTextVIndent(2);
        this.m_textEditNode.selectAll();
        this.m_textEditNode.setBorderColor(Color.black);
        Font font = this.m_itemCurrentEdit.getFont();
        if (font == null) {
            font = this.defaultfont;
        }
        this.m_textEditNode.setFont(font);
        if (this.m_nColumnEdit == 0) {
            this.m_itemCurrentEdit.setText("");
        } else {
            this.m_itemCurrentEdit.getSubItem(this.m_nColumnEdit).setText("");
        }
        this.deselectAll();
        this.m_textEditNode.selectAll();
        this.m_textEditNode.setVisible(true);
        this.m_textEditNode.requestFocus();
        int n3 = listItem.getImageBounds().width;
        if (n3 != 0) {
            n3 += this.m_nWidthGap;
        }
        if (this.m_bOverlapEdit) {
            this.m_textEditNode.setFixedWidth(0);
        } else {
            int n4 = 0;
            n4 = this.m_nColumnEdit == 0 ? ((Column) this.m_arrColumns.elementAt((int) this.m_nColumnEdit)).m_nCX - this.m_nWidthGap * 2 - listItem.getImageBounds().width : ((Column) this.m_arrColumns.elementAt((int) this.m_nColumnEdit)).m_nCX - this.m_nWidthGap * 2;
            this.m_textEditNode.setFixedWidth(n4);
        }
        if (this.m_nColumnEdit == 0) {
            this.m_textEditNode.setBounds(listItem.getTextBounds().x + this.m_nWidthGap - this.m_ptViewportOrg.x + n3, listItem.getTextBounds().y - rectangle.y, listItem.getTextBounds().width, listItem.getTextBounds().height + this.m_nRowSpace + 1);
        } else {
            this.m_textEditNode.setBounds(this.m_nWidthGap + n, listItem.getTextBounds().y - rectangle.y, listItem.getTextBounds().width, listItem.getTextBounds().height + this.m_nRowSpace + 1);
        }
        this.m_textEditNode.update();
    }

    protected void fireOnItemTextChanged(ItemTextChangedEvent itemTextChangedEvent) {
        if (this.aItemTextChangedListener == null) {
            return;
        }
        this.aItemTextChangedListener.onItemTextChanged(itemTextChangedEvent);
    }

    public boolean getAutoWrap() {
        return this.m_bAutoWrap;
    }

    public void setAutoWrap(boolean bl) {
        this.m_bAutoWrap = bl;
    }

    public String getChangedItemName() {
        return this.m_oldName;
    }

    public int getColumnAlignment(int n) {
        if (n < this.m_arrColumns.size()) {
            return ((Column) this.m_arrColumns.elementAt(n)).getAlignment();
        }
        return -1;
    }

    public Font getColumnFont(int n) {
        if (n < this.m_arrColumns.size()) {
            return ((Column) this.m_arrColumns.elementAt(n)).getFont();
        }
        return null;
    }

    public int getColumnHeaderAlignment(int n) {
        if (n < this.m_arrColumns.size()) {
            return ((Column) this.m_arrColumns.elementAt(n)).getHeaderAlignment();
        }
        return -1;
    }

    public int getColumnHeaderHeight() {
        return this.m_cyHeader;
    }

    public void setColumnHeaderHeight(int n) {
        this.m_cyHeader = n;
        this.update();
    }

    protected int getColumnHit(int n) {
        int n2 = 0;
        int n3 = 0;
        n += this.m_ptViewportOrg.x;
        while (n3 <= n && n2 < this.m_arrColumns.size()) {
            n3 += ((Column) this.m_arrColumns.elementAt((int) n2++)).m_nCX;
        }
        return n2 < this.m_arrColumns.size() ? n2 - 1 : this.m_arrColumns.size() - 1;
    }

    public String getColumnText(int n) {
        if (n < this.m_arrColumns.size()) {
            return ((Column) this.m_arrColumns.elementAt(n)).getText();
        }
        return null;
    }

    public int getColumnWidth(int n) {
        if (n < this.m_arrColumns.size()) {
            return ((Column) this.m_arrColumns.elementAt(n)).getWidth();
        }
        return 0;
    }

    public Font getDefaultFont() {
        return this.defaultfont;
    }

    public void setDefaultFont(Font font) {
        this.defaultfont = font;
    }

    public int getDottedLineFill() {
        return this.DottedLineFill;
    }

    public void setDottedLineFill(int n) {
        this.DottedLineFill = n;
    }

    public int getDottedLineSpace() {
        return this.DottedLineSpace;
    }

    public void setDottedLineSpace(int n) {
        this.DottedLineSpace = n;
    }

    public int getDropTargetItem() {
        return this.m_dropTargetItem;
    }

    public Color getHighlightTextBgColor() {
        return this.m_colHighlightTextBackground;
    }

    public void setHighlightTextBgColor(Color color) {
        this.m_colHighlightTextBackground = color;
    }

    public Color getHighlightTextColor() {
        return this.m_colHighlightText;
    }

    public void setHighlightTextColor(Color color) {
        this.m_colHighlightText = color;
    }

    public boolean getHilightSubItems() {
        return this.m_bHilightSubItems;
    }

    public void setHilightSubItems(boolean bl) {
        this.m_bHilightSubItems = bl;
    }

    protected int getImageIndex(int n) {
        if (this.m_arrImageIDs == null) {
            return -1;
        }
        return this.m_arrImageIDs.indexOf(new Integer(n));
    }

    public Image getImageList(int n) {
        int n2 = this.m_arrImageIDs.indexOf(new Integer(n));
        if (this.m_arrImages == null || n2 < 0 || n2 > this.m_arrImages.size() - 1) {
            return null;
        }
        Image image = (Image) this.m_arrImages.elementAt(n2);
        return image;
    }

    public int getIndex(ListItem listItem) {
        return this.m_arrItems.indexOf(listItem);
    }

    public Rectangle getInsideRect() {
        Rectangle rectangle = this.getBounds();
        if (this.isHeaderCtrlEnabled()) {
            rectangle.y += this.m_cyHeader;
            rectangle.height -= this.m_cyHeader;
        }
        if (this.vs != null) {
            rectangle.width -= this.m_nVScrollbarWidth - 1;
        }
        if (this.hs != null) {
            rectangle.height -= this.m_nHScrollbarHeight - 1;
        }
        return rectangle;
    }

    public Object getItem(int n) {
        return this.m_arrItems.elementAt(n);
    }

    public ListItem getItemAt(int n) {
        return (ListItem) this.m_arrItems.elementAt(n);
    }

    public int getItemCount() {
        return this.m_arrItems.size();
    }

    public int getItemImage(ListItem listItem) {
        return listItem.getImage();
    }

    public String[] getItems() {
        int n = this.m_arrItems.size();
        String[] stringArray = new String[n];
        int n2 = 0;
        while (n2 < n) {
            stringArray[n2] = ((ListItem) this.m_arrItems.elementAt(n2)).getText();
            ++n2;
        }
        return stringArray;
    }

    public synchronized void setItems(String[] stringArray) {
        int n = 0;
        while (n < stringArray.length) {
            this.addItem(stringArray[n]);
            ++n;
        }
    }

    public int getLastFullyVisibleItem() {
        ListItem listItem = new ListItem();
        Rectangle rectangle = this.getInsideRect();
        int n = 0;
        int n2 = this.m_nTopRow;
        while (n2 < this.m_arrItems.size()) {
            listItem = (ListItem) this.m_arrItems.elementAt(n2);
            if (listItem.m_nCY == -1) {
                this.measureItem(n2, this.getGraphics());
            }
            if ((n += listItem.m_nCY) > rectangle.height) break;
            ++n2;
        }
        return n2 - 1;
    }

    public int getLastVisibleRow() {
        if (this.itemsThisPage(this.getGraphics()) != 0) {
            return this.m_nTopRow + this.itemsThisPage(this.getGraphics()) - 1;
        }
        return this.m_nTopRow;
    }

    public int getLeftIndent() {
        return 0;
    }

    public void setLeftIndent(int n) {
    }

    public int getLineSpacing() {
        return this.m_nRowSpace;
    }

    public void setLineSpacing(int n) {
        this.m_nRowSpace = n;
        this.reMeasureAllItems();
    }

    protected Point getLogicalSize() {
        Point point = new Point(0, 0);
        point.y = this.m_arrItems.size();
        if (this.m_arrColumns.size() > 1) {
            int n = this.m_arrColumns.size();
            int n2 = 0;
            while (n2 < n) {
                point.x += ((Column) this.m_arrColumns.elementAt((int) n2)).m_nCX;
                ++n2;
            }
            point.x = Math.max(point.x, this.getInsideRect().width);
        } else {
            Rectangle rectangle = new Rectangle();
            ListItem listItem = new ListItem();
            int n = this.m_arrItems.size();
            int n3 = 0;
            int n4 = 0;
            rectangle = this.getInsideRect();
            int n5 = this.m_nTopRow;
            while (n5 < n && n4 < rectangle.height) {
                listItem = (ListItem) this.m_arrItems.elementAt(n5);
                if (listItem.m_nCY == -1) {
                    listItem.m_nCY = ((ListItem) this.m_arrItems.elementAt((int) n5)).m_nCY = this.measureItem(n5, this.getGraphics());
                }
                n3 = !this.m_bAutoWrap ? Math.max(n3, listItem.m_rcText.x - this.getBounds().x + listItem.m_rcText.width + this.m_nWidthGap) : Math.max(this.getInsideRect().width, ((Column) this.m_arrColumns.elementAt((int) 0)).m_nCX);
                n4 += listItem.m_nCY;
                ++n5;
            }
            point.x = Math.max(n3, ((Column) this.m_arrColumns.elementAt(0)).getWidth());
        }
        return point;
    }

    protected int getMaxLeftCol() {
        Rectangle rectangle = this.getInsideRect();
        Point point = this.getLogicalSize();
        return point.x - rectangle.width;
    }

    protected int getMaxTopRow() {
        Rectangle rectangle = this.getInsideRect();
        int n = this.m_arrItems.size() - 1;
        new ListItem();
        int n2 = 0;
        int n3 = n;
        while (n3 >= 0) {
            ListItem cfr_ignored_0 = (ListItem) this.m_arrItems.elementAt(n3);
            this.m_lvi_iItem = n3;
            if ((n2 += this.measureItem(n3, this.getGraphics())) > rectangle.height) break;
            --n3;
        }
        return Math.max(0, n3 + 1);
    }

    public boolean getMultipleSelections() {
        return this.m_bMultipleSelections;
    }

    public void setMultipleSelections(boolean bl) {
        if (bl != this.m_bMultipleSelections) {
            this.m_bMultipleSelections = bl;
        }
    }

    protected int getPosFromCol(int n) {
        int n2 = 0 - this.m_ptViewportOrg.x;
        int n3 = 0;
        while (n3 < n + 1) {
            n2 += ((Column) this.m_arrColumns.elementAt((int) n3)).m_nCX;
            ++n3;
        }
        return n2 - 1;
    }

    public ListItem getSelected() {
        return this.getItemAt(this.selected[this.selected.length - 1]);
    }

    protected int getSelected(int n, int n2) {
        int n3;
        int n4;
        ListItem listItem = new ListItem();
        Column column = new Column();
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = new Rectangle();
        Rectangle rectangle3 = new Rectangle();
        int n5 = -1;
        rectangle3 = this.getBounds();
        rectangle3.x = 0;
        rectangle3.y = 0;
        rectangle2 = new Rectangle();
        rectangle2 = this.getInsideRect();
        rectangle2.x = 0;
        rectangle2.y = this.isHeaderCtrlEnabled() ? this.m_cyHeader : 0;
        int n6 = this.m_nTopRow;
        int n7 = n4 = -this.m_ptViewportOrg.x;
        if (this.m_arrColumns.size() > 1) {
            n3 = 0;
            while (n3 < this.m_arrColumns.size()) {
                column = (Column) this.m_arrColumns.elementAt(n3);
                if (n >= n4 && n < (n7 += column.m_nCX)) {
                    break;
                }
                ++n3;
            }
        } else {
            column = (Column) this.m_arrColumns.elementAt(0);
        }
        if (n2 <= rectangle2.y) {
            if (this.m_nDraggingState == 1) {
                int n8 = this.getColumnHit(n);
                if (this.onStartTracking(n8)) {
                    Graphics graphics = this.getGraphics();
                    graphics.setColor(Color.red);
                    graphics.setXORMode(this.getBackground());
                    this.m_rectDragging = new Rectangle(n, this.m_cyHeader, 1, this.getInsideRect().height);
                    graphics.fillRect(this.m_rectDragging.x, this.m_rectDragging.y, this.m_rectDragging.width, this.m_rectDragging.height);
                    return -1;
                }
            } else {
                this.onHitColumnHeader(this.getColumnHit(n));
            }
            Rectangle rectangle4 = new Rectangle();
            rectangle4.y = rectangle3.y;
            rectangle4.x = -this.m_ptViewportOrg.x;
            rectangle4.height = 0;
            rectangle4.width = 0;
            int n9 = this.m_arrColumns.size();
            n3 = 0;
            while (n3 < n9) {
                column = (Column) this.m_arrColumns.elementAt(n3);
                rectangle4.width += column.m_nCX;
                if (this.m_arrColumns.size() > 1) {
                    n5 = n3 - 1;
                    break;
                }
                if (rectangle4.contains(new Point(n, n2))) {
                    n5 = n3 - 1;
                    break;
                }
                rectangle4.x += rectangle4.width;
                ++n3;
            }
        } else {
            rectangle.y = rectangle2.y - 0 * this.m_nRowSpace;
            rectangle.x = -this.m_ptViewportOrg.x;
            rectangle.width = rectangle2.width;
            rectangle.width = this.getLogicalSize().x;
            if (this.m_arrColumns.size() == 1) {
                rectangle.width = this.getLogicalSize().x;
            }
            rectangle.height = 0;
            n3 = n6;
            while (rectangle.y < rectangle2.height + rectangle2.y && n3 < this.m_arrItems.size()) {
                listItem = (ListItem) this.m_arrItems.elementAt(n3);
                if (listItem.m_nCY == -1) {
                    listItem.m_nCY = this.measureItem(n3, this.getGraphics());
                }
                rectangle.height = listItem.m_nCY + this.m_nRowSpace;
                if (rectangle.contains(new Point(n, n2))) {
                    n5 = n3;
                    int n10 = column.m_iSubItem;
                    if (n10 != 0) break;
                    Rectangle rectangle5 = new Rectangle();
                    Rectangle rectangle6 = new Rectangle();
                    rectangle5.y = rectangle.y;
                    rectangle5.height = rectangle.height + this.m_nRowSpace;
                    rectangle5.x = n4 + listItem.m_rcText.x;
                    rectangle5.width = listItem.m_rcText.width;
                    rectangle6.y = rectangle.y + rectangle6.y;
                    rectangle6.height = listItem.m_rcIcon.height;
                    rectangle6.x = n4 + listItem.m_rcIcon.x;
                    rectangle6.width = listItem.m_rcIcon.width;
                    break;
                }
                rectangle.y += rectangle.height;
                ++n3;
            }
        }
        return n5;
    }

    public synchronized int getSelectedIndex() {
        int[] nArray = this.getSelectedIndexes();
        return nArray.length == 1 ? nArray[0] : -1;
    }

    public synchronized int[] getSelectedIndexes() {
        int[] nArray = new int[this.selected.length];
        if (this.selected.length > 0) {
            nArray[0] = this.selected[0];
            int n = 1;
            while (n < this.selected.length) {
                boolean bl = false;
                int n2 = 0;
                while (n2 < n) {
                    if (nArray[n2] > this.selected[n]) {
                        int n3 = n;
                        while (n3 > n2) {
                            nArray[n3] = nArray[n3 - 1];
                            --n3;
                        }
                        nArray[n2] = this.selected[n];
                        bl = true;
                        break;
                    }
                    ++n2;
                }
                if (!bl) {
                    nArray[n] = this.selected[n];
                }
                ++n;
            }
        }
        return nArray;
    }

    public String getSelectedItem() {
        if (this.selected.length > 0 && this.selected[this.selected.length - 1] < this.m_arrItems.size()) {
            ListItem listItem = (ListItem) this.m_arrItems.elementAt(this.selected[this.selected.length - 1]);
            return listItem.getText();
        }
        return null;
    }

    public synchronized Object[] getSelectedItems() {
        int[] nArray = this.getSelectedIndexes();
        Object[] objectArray = new Object[nArray.length];
        int n = 0;
        while (n < nArray.length) {
            objectArray[n] = this.getItem(nArray[n]);
            ++n;
        }
        return objectArray;
    }

    public ListItem getSelectedListItem() {
        if (this.selected.length > 0 && this.selected[this.selected.length - 1] < this.m_arrItems.size()) {
            return (ListItem) this.m_arrItems.elementAt(this.selected[this.selected.length - 1]);
        }
        return null;
    }

    public Object[] getSelectedObjects() {
        return this.getSelectedItems();
    }

    protected int getTarget(int n, int n2) {
        return this.getSelected(n, n2);
    }

    public Color getTextColor() {
        return this.m_colText;
    }

    public void setTextColor(Color color) {
        this.m_colText = color;
        this.repaint();
    }

    public int getTextIndent() {
        return this.m_nWidthGap;
    }

    public void setTextIndent(int n) {
        this.m_nWidthGap = n;
    }

    public int getTopIndent() {
        return 0;
    }

    public void setTopIndent(int n) {
    }

    public synchronized boolean insertColumn(int n, Column column, boolean bl) {
        ListItem listItem = new ListItem();
        int n2 = this.m_arrColumns.size();
        if (n < 0) {
            n = n2;
        }
        if (n >= n2) {
            this.m_arrColumns.addElement(column);
        } else {
            this.m_arrColumns.insertElementAt(column, n);
        }
        int n3 = this.m_nSubItemCount;
        column.m_iSubItem = n >= n3 ? n3 : n;
        int n4 = column.m_iSubItem + 1;
        while (n4 <= n2) {
            ++((Column) this.m_arrColumns.elementAt((int) n4)).m_iSubItem;
            ++n4;
        }
        int n5 = 0;
        while (n5 < this.m_arrItems.size()) {
            listItem = (ListItem) this.m_arrItems.elementAt(n5);
            listItem.insertSubItem(column.m_iSubItem - 1);
            ++n5;
        }
        if (column.m_nCX == -1) {
            n4 = Math.min(n, this.m_arrColumns.size() - 1);
            ((Column) this.m_arrColumns.elementAt((int) n4)).m_nCX = 100;
        }
        this.reMeasureAllItems();
        ++this.m_nSubItemCount;
        if (bl) {
            this.updateScrollbar();
            this.doLayout();
            this.repaint();
        }
        return true;
    }

    public boolean insertColumn(int n, String string, int n2, int n3, int n4, boolean bl) {
        Column column = new Column();
        column.m_nCX = n3;
        column.m_strText = string;
        column.m_nFmt = n2;
        column.m_iSubItem = n4;
        return this.insertColumn(n, column, bl);
    }

    public synchronized void insertItem(int n, ListItem listItem) {
        block3:
        {
            block2:
            {
                this.m_arrItems.insertElementAt(listItem, n);
                if (this.m_nSubItemCount <= listItem.getSubItemCount() + 1) break block2;
                int n2 = listItem.getSubItemCount();
                while (n2 < this.m_nSubItemCount - 1) {
                    ((ListItem) this.m_arrItems.elementAt(n)).addSubItem();
                    ++n2;
                }
                break block3;
            }
            if (this.m_nSubItemCount >= listItem.getSubItemCount() + 1) break block3;
            int n3 = this.m_nSubItemCount;
            while (n3 < listItem.getSubItemCount() + 1) {
                this.insertColumn(n3, "", 0, -1, -1, true);
                ++n3;
            }
        }
    }

    public boolean isColumnHeader() {
        return this.m_bColumnHeader;
    }

    public void setColumnHeader(boolean bl) {
        this.m_bColumnHeader = bl;
        if (!bl) {
            this.m_cyHeader = 0;
        } else {
            this.update();
        }
    }

    public boolean isFocusTraversable() {
        return true;
    }

    public boolean isHeaderCtrlEnabled() {
        return this.m_bColumnHeader;
    }

    public boolean isOverlapEditMode() {
        return this.m_bOverlapEdit;
    }

    public void setOverlapEditMode(boolean bl) {
        this.m_bOverlapEdit = bl;
    }

    protected boolean isRoot(int n) {
        return false;
    }

    public synchronized boolean isSelected(int n) {
        int[] nArray = this.getSelectedIndexes();
        int n2 = 0;
        while (n2 < nArray.length) {
            if (nArray[n2] == n) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public boolean isShowDotRect() {
        return this.m_bShowDotRect;
    }

    public void setShowDotRect(boolean bl) {
        this.m_bShowDotRect = bl;
    }

    protected int itemsThisPage(Graphics graphics) {
        ListItem listItem = new ListItem();
        Rectangle rectangle = this.getInsideRect();
        rectangle.y -= this.getBounds().y;
        int n = 0;
        int n2 = 0;
        if (this.m_nTopRow > this.m_arrItems.size()) {
            this.m_nTopRow = 0;
        }
        if (this.m_arrItems.size() == 0) {
            return 0;
        }
        int n3 = this.m_nTopRow;
        while (n3 < this.m_arrItems.size()) {
            listItem = (ListItem) this.m_arrItems.elementAt(n3);
            if (listItem.m_nCY == -1) {
                listItem.m_nCY = ((ListItem) this.m_arrItems.elementAt((int) n3)).m_nCY = this.measureItem(n3, graphics);
            }
            n += listItem.m_nCY;
            this.getFontMetrics(this.defaultfont);
            if (listItem.getFont() != null) {
                this.getFontMetrics(listItem.getFont());
            }
            if ((n += this.m_nRowSpace) > rectangle.height) break;
            ++n3;
            ++n2;
        }
        if (n < rectangle.height && (n3 = this.m_nTopRow - 1) >= 0) {
            while (n3 >= 0) {
                listItem = (ListItem) this.m_arrItems.elementAt(n3);
                if (listItem.m_nCY == -1) {
                    listItem.m_nCY = ((ListItem) this.m_arrItems.elementAt((int) n3)).m_nCY = this.measureItem(n3, graphics);
                }
                if ((n += listItem.m_nCY) > rectangle.height) break;
                --n3;
                ++n2;
            }
        }
        this.m_nItemsThisPage = n2;
        return n2;
    }

    public int measureItem(int n, Graphics graphics) {
        this.m_lvi_iItem = n;
        return this.measureItem(graphics);
    }

    protected int measureItem(Graphics graphics) {
        ListItem listItem = new ListItem();
        listItem = (ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem);
        listItem.m_nCY = 0;
        listItem.m_rcText.height = 0;
        int n = 0;
        int n2 = this.m_arrColumns.size();
        Image image = null;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        while (n5 < n2) {
            this.m_colPCol = (Column) this.m_arrColumns.elementAt(n5);
            this.m_lvi_iSubItem = this.m_colPCol.m_iSubItem;
            if (this.m_lvi_iSubItem == 0) {
                int n6 = ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getImage();
                int n7 = this.getImageIndex(n6);
                if (this.m_lvi_iSubItem == 0 && this.m_arrImages != null && this.m_arrImages.size() > 0 && n7 != -1 && n7 < this.m_arrImages.size()) {
                    image = (Image) this.m_arrImages.elementAt(n7);
                    n3 = image.getHeight(this);
                    n4 = image.getWidth(this);
                }
                ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).setImageBounds(new Rectangle(0, 0, n4, n3));
            }
            this.m_lvi_pszText = this.m_colPCol.m_iSubItem > 0 ? ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getSubItem((int) this.m_lvi_iSubItem).pszText : ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_pszText;
            if (n3 > listItem.m_nCY) {
                listItem.m_nCY = n3;
            }
            n = this.measureSubItem(graphics);
            listItem.m_rcText.height = listItem.m_nCY = Math.max(listItem.m_nCY, n);
            ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_nCY = listItem.m_nCY;
            ++n5;
        }
        this.m_PCheight = listItem.m_nCY;
        this.m_recPCRect.height = listItem.m_nCY;
        return this.m_PCheight;
    }

    protected int measureSubItem(Graphics graphics) {
        Rectangle rectangle = new Rectangle(0, 0, this.m_colPCol.m_nCX, 0);
        if (this.m_lvi_pszText != null) {
            Font font = ((ListItem) this.m_arrItems.elementAt(this.m_lvi_iItem)).getFont();
            FontMetrics fontMetrics = font != null ? this.getFontMetrics(font) : this.getFontMetrics(this.defaultfont);
            if (this.m_bAutoWrap && this.m_lvi_pszText != null) {
                Vector vector = new Vector();
                vector = this.m_lvi_iSubItem == 0 ? Text.wrapText(this.m_lvi_pszText, ((Column) this.m_arrColumns.elementAt((int) this.m_lvi_iSubItem)).m_nCX - this.m_nWidthGap * 2 - ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getImageBounds().width, true, fontMetrics) : Text.wrapText(this.m_lvi_pszText, ((Column) this.m_arrColumns.elementAt((int) this.m_lvi_iSubItem)).m_nCX - this.m_nWidthGap * 2, true, fontMetrics);
                rectangle.height = vector.size() * fontMetrics.getHeight();
            } else {
                rectangle.height = fontMetrics.getHeight();
            }
            this.m_PCheight = rectangle.height;
            this.m_recPCRect.height = rectangle.height;
            ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_rcText.height = rectangle.height;
            if (this.m_arrColumns.size() > 1) {
                rectangle.width = ((Column) this.m_arrColumns.elementAt((int) this.m_lvi_iSubItem)).m_nCX;
                ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_rcText.width = ((Column) this.m_arrColumns.elementAt((int) this.m_lvi_iSubItem)).m_nCX;
            } else {
                ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_rcText.width = fontMetrics.stringWidth(this.m_lvi_pszText) + 2 * this.m_nWidthGap;
                rectangle.width = fontMetrics.stringWidth(this.m_lvi_pszText) + 2 * this.m_nWidthGap;
                if (rectangle.width > ((Column) this.m_arrColumns.elementAt((int) 0)).m_nCX && !this.m_bAutoWrap) {
                    ((Column) this.m_arrColumns.elementAt((int) 0)).m_nCX = Math.max(rectangle.width, this.getInsideRect().width);
                }
            }
        }
        if (this.m_lvi_iSubItem == 0) {
            ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).m_rcText = new Rectangle(rectangle);
            this.m_recPCRect = new Rectangle(rectangle);
        } else {
            ((ListItem) this.m_arrItems.elementAt((int) this.m_lvi_iItem)).getSubItem((int) this.m_lvi_iSubItem).rcText = new Rectangle(rectangle);
        }
        return this.m_PCheight;
    }

    public void modifySubItem(int n, int n2, String string) {
        if (n <= this.m_arrItems.size() - 1 && n2 > 0 && n2 <= ((ListItem) this.m_arrItems.elementAt(n)).getSubItemCount()) {
            ((ListItem) this.m_arrItems.elementAt((int) n)).getSubItem((int) n2).pszText = string;
        }
    }

    protected boolean mouseDown(MouseEvent mouseEvent, int n, int n2) {
        this.m_bSelectedItemChanged = false;
        if (this.m_bDragModeAllowed) {
            this.m_bDragModeEnabled = true;
        }
        this.m_bEditModeEnabled = false;
        this.m_nXMouse = n;
        this.m_nYMouse = n2;
        if (this.m_nDraggingState == 0) {
            this.requestFocus();
        }
        if (mouseEvent.isShiftDown()) {
            this.m_bDragModeEnabled = false;
            this.m_bEditModeEnabled = false;
            this.selected = new int[0];
            int n3 = this.getSelected(n, n2);
            if (!this.m_bMultipleSelections) {
                if (n3 != -1) {
                    this.prev = n3;
                    this.select(n3, true);
                    this.m_bSelectedItemChanged = true;
                }
                return true;
            }
            if (n3 != -1) {
                int n4;
                int n5 = n3 > this.prev ? this.prev : n3;
                int n6 = n4 = n3 > this.prev ? n3 : this.prev;
                if (n5 == -1) {
                    n5 = n3;
                }
                this.prev = n3;
                int n7 = n5;
                while (n7 <= n4) {
                    this.select(n7, false);
                    ++n7;
                }
                this.update();
                this.m_bSelectedItemChanged = true;
            }
            return true;
        }
        if (mouseEvent.isControlDown()) {
            this.m_bEditModeEnabled = false;
            this.m_bDragModeEnabled = false;
            int n8 = this.getSelected(n, n2);
            if (!this.m_bMultipleSelections) {
                if (n8 != -1) {
                    this.prev = n8;
                    this.select(n8, true);
                    this.m_bSelectedItemChanged = true;
                }
                return true;
            }
            if (this.isSelected(n8)) {
                this.deselect(n8);
                this.update();
                this.m_bSelectedItemChanged = true;
                this.m_bEditModeEnabled = false;
                return true;
            }
            if (n8 != -1) {
                this.prev = n8;
                this.select(n8, true);
                this.m_bSelectedItemChanged = true;
            }
            return true;
        }
        this.m_bEditModeEnabled = false;
        int n9 = this.getSelected(n, n2);
        if (mouseEvent.getClickCount() == 2) {
            this.doubleClickEvent(n9);
            this.processActionEvent(new ActionEvent(this, 1001, this.commandDoubleClicked));
            return true;
        }
        boolean bl = this.isSelected(n9);
        if (bl) {
            if (this.m_bDragModeAllowed) {
                this.m_bDragModeEnabled = true;
            }
            if (this.m_bEditModeAllowed) {
                this.m_bEditModeEnabled = true;
            }
            if (!this.m_bDragModeAllowed && !this.m_bEditModeAllowed) {
                this.deselectAll();
                this.select(n9);
            }
            return true;
        }
        if (this.m_nDraggingState == 0) {
            if (n9 == -1) {
                this.m_bDragModeEnabled = false;
            }
            if (n9 != -1) {
                this.selected = new int[0];
                this.prev = n9;
                this.select(n9, true);
                if (mouseEvent.getClickCount() != 2) {
                    this.m_bSelectedItemChanged = true;
                }
            }
        }
        return true;
    }

    protected boolean mouseDrag(MouseEvent mouseEvent, int n, int n2) {
        this.m_bMouseDrag = true;
        this.m_bEditModeEnabled = false;
        if (this.m_nDraggingState == 1 && n - this.m_nDraggingBegin >= -3) {
            int n3 = this.getColumnHit(n);
            this.onMoveTracking(n3, n);
            return true;
        }
        if (this.m_nDraggingState == 1 && n - this.m_nDraggingBegin < -3) {
            return true;
        }
        if (this.m_bDragModeEnabled) {
            this.m_bEditModeEnabled = false;
            Graphics graphics = this.getGraphics();
            if (this.m_bDragDropDrawn) {
                this.printDragItem(graphics);
            } else {
                this.m_bDragDropDrawn = true;
            }
            this.m_nXMouse = n;
            this.m_nYMouse = n2;
            this.printDragItem(graphics);
            graphics.dispose();
        }
        return true;
    }

    protected boolean mouseExit(MouseEvent mouseEvent, int n, int n2) {
        this.setCursor(Cursor.getPredefinedCursor(0));
        return true;
    }

    protected boolean mouseMove(MouseEvent mouseEvent, int n, int n2) {
        int n3 = this.getColumnHit(n);
        int n4 = this.getPosFromCol(n3);
        if (n2 > 0 && n2 < this.m_cyHeader && Math.abs(n - n4) <= 3 && this.m_arrColumns.size() > 1) {
            this.m_nDraggingState = 1;
            this.m_nDraggingCurrent = n3;
            this.m_nDraggingBegin = n4 - ((Column) this.m_arrColumns.elementAt((int) this.m_nDraggingCurrent)).m_nCX;
            if (!this.m_bCaptured) {
                if (this.OSName.equals("Windows NT") || this.OSName.equals("Windows 95")) {
                    this.setCursor(Cursor.getPredefinedCursor(10));
                } else {
                    this.setCursor(Cursor.getPredefinedCursor(13));
                }
            }
            this.m_bCaptured = true;
        } else if (n2 > 0 && n2 < this.m_cyHeader && Math.abs(n4 + ((Column) this.m_arrColumns.elementAt((int) n3)).m_nCX - n) <= 3 && this.m_arrColumns.size() > 1) {
            this.m_nDraggingState = 1;
            this.m_nDraggingCurrent = n3;
            this.m_nDraggingBegin = n4;
            if (!this.m_bCaptured) {
                if (this.OSName.equals("Windows NT") || this.OSName.equals("Windows 95")) {
                    this.setCursor(Cursor.getPredefinedCursor(10));
                } else {
                    this.setCursor(Cursor.getPredefinedCursor(13));
                }
            }
            this.m_bCaptured = true;
        } else {
            this.m_nDraggingState = 0;
            if (this.m_bCaptured) {
                this.setCursor(Cursor.getPredefinedCursor(0));
            }
            this.m_bCaptured = false;
            this.onMoveOverItem(n, n2);
        }
        return true;
    }

    protected boolean mouseUp(MouseEvent mouseEvent, int n, int n2) {
        if (this.m_bDragModeEnabled && this.m_bMouseDrag && this.m_nDraggingState == 0) {
            this.m_bEditModeEnabled = false;
            this.m_bDragModeEnabled = false;
            this.m_bMouseDrag = false;
            Graphics graphics = this.getGraphics();
            if (this.m_bDragDropDrawn) {
                this.printDragItem(graphics);
                this.m_bDragDropDrawn = false;
                this.m_dropTargetItem = this.getTarget(n, n2);
                this.onDropAction();
                this.processActionEvent(new ActionEvent(this, 3001, commandDragDrop));
                this.repaint();
            }
            return true;
        }
        if (!this.m_bHasFocus) {
            this.requestFocus();
        }
        if (this.m_nDraggingState == 1) {
            this.onEndTracking(this.m_nDraggingCurrent, n);
            this.m_bDragModeEnabled = false;
            this.m_bEditModeEnabled = false;
            this.m_bDragDropDrawn = false;
            this.m_bMouseDrag = false;
            return true;
        }
        if (this.m_bEditModeEnabled || this.m_bDragModeEnabled) {
            if (this.selected != null && this.selected.length > 1) {
                this.deselectAll();
                int n3 = this.getSelected(n, n2);
                if (n3 > 0) {
                    this.select(n3);
                }
            } else if (this.m_bEditModeEnabled) {
                this.editItem();
            }
            this.m_bDragModeEnabled = false;
            this.m_bMouseDrag = false;
        }
        this.m_bDragModeEnabled = false;
        this.m_bEditModeEnabled = false;
        this.m_nOldTargetIndex = -1;
        this.m_bMouseDrag = false;
        return true;
    }

    public void moveItems(Object[] objectArray, int n) {
        if (n == -1) {
            return;
        }
        int n2 = objectArray.length - 1;
        while (n2 > -1) {
            if (objectArray[n2] == this.m_arrItems.elementAt(n)) {
                return;
            }
            --n2;
        }
        Vector vector = new Vector();
        int n3 = 0;
        while (n3 < this.selected.length) {
            vector.addElement(this.m_arrItems.elementAt(this.selected[n3]));
            ++n3;
        }
        this.deselectAll(false);
        n3 = objectArray.length - 1;
        while (n3 > -1) {
            int n4 = this.getIndex((ListItem) objectArray[n3]);
            if (n4 > n) {
                ++n4;
            }
            this.m_arrItems.insertElementAt(objectArray[n3], n);
            this.m_arrItems.removeElementAt(n4);
            n = this.getIndex((ListItem) objectArray[n3]);
            --n3;
        }
        n3 = 0;
        while (n3 < vector.size()) {
            this.select(this.getIndex((ListItem) vector.elementAt(n3)), false);
            ++n3;
        }
        this.update();
    }

    protected void onDropAction() {
    }

    protected void onEndTracking(int n, int n2) {
        this.m_nDraggingState = 0;
        this.setCursor(Cursor.getPredefinedCursor(0));
        ((Column) this.m_arrColumns.elementAt((int) n)).m_nCX = Math.max(n2 - this.m_nDraggingBegin, 20);
        if (this.getMaxTopRow() == 0) {
            this.m_nTopRow = 0;
        }
        if (this.getMaxLeftCol() == 0) {
            this.m_nLeftCol = 0;
        }
        this.reMeasureAllItems();
        this.update();
    }

    protected void onHitColumnHeader(int n) {
    }

    protected void onLeftColChanged(int n) {
        if (this.m_textEditNode != null && this.m_textEditNode.isShowing()) {
            this.changeItemText();
        }
        if (this.m_ptViewportOrg.x != n) {
            this.m_ptViewportOrg.x = n;
            this.updateScrollbar(false);
            this.repaint();
            this.processActionEvent(new ActionEvent(this, 1001, this.commandLeftColChanged));
        }
    }

    protected void onMoveOverItem(int n, int n2) {
    }

    protected void onMoveTracking(int n, int n2) {
        Rectangle rectangle = this.getBounds();
        int n3 = rectangle.width;
        int n4 = rectangle.height;
        Graphics graphics = this.getGraphics();
        graphics.setColor(Color.red);
        graphics.setXORMode(this.getBackground());
        graphics.fillRect(this.m_rectDragging.x, this.m_rectDragging.y, this.m_rectDragging.width, this.m_rectDragging.height);
        this.m_rectDragging = new Rectangle(n2, this.m_cyHeader, 1, n4);
        graphics.fillRect(this.m_rectDragging.x, this.m_rectDragging.y, this.m_rectDragging.width, this.m_rectDragging.height);
        ((Column) this.m_arrColumns.elementAt((int) this.m_nDraggingCurrent)).m_nCX = Math.max(n2 - this.m_nDraggingBegin, 20);
        graphics = this.getGraphics();
        Rectangle rectangle2 = new Rectangle(this.m_nDraggingBegin, 0, n3 - this.m_nDraggingBegin, this.m_cyHeader);
        graphics.clipRect(rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height);
        this.update(graphics);
    }

    protected boolean onStartTracking(int n) {
        return true;
    }

    protected void onTopRowChanged(int n) {
        if (this.m_textEditNode != null && this.m_textEditNode.isShowing()) {
            this.changeItemText();
        }
        if (this.m_nTopRow != n) {
            this.m_nTopRow = n;
            this.updateScrollbar(false);
            this.repaint();
            this.processActionEvent(new ActionEvent(this, 1001, this.commandTopRowChanged));
        }
    }

    private void paintBorder(Graphics graphics, int n, int n2, int n3, int n4, boolean bl) {
        graphics.setColor(SystemColor.controlLtHighlight);
        if (bl) {
            graphics.drawLine(n, n2, n + n3, n2);
            graphics.drawLine(n, n2, n, n2 + n4);
        } else {
            graphics.drawLine(n + 1, n2 + n4 - 1, n + n3 - 1, n2 + n4 - 1);
            graphics.drawLine(n + n3 - 1, n2 + 1, n + n3 - 1, n2 + n4 - 1);
        }
        graphics.setColor(SystemColor.controlHighlight);
        if (!bl) {
            graphics.drawLine(n + 1, n2 + n4 - 2, n + n3 - 2, n2 + n4 - 2);
            graphics.drawLine(n + n3 - 2, n2 + 1, n + n3 - 2, n2 + n4 - 2);
        }
        graphics.setColor(SystemColor.controlShadow);
        if (bl) {
            graphics.drawLine(n + 1, n2 + n4 - 1, n + n3 - 1, n2 + n4 - 1);
            graphics.drawLine(n + n3 - 1, n2 + 1, n + n3 - 1, n2 + n4 - 1);
        } else {
            graphics.drawLine(n, n2, n + n3 - 1, n2);
            graphics.drawLine(n, n2, n, n2 + n4 - 1);
        }
        graphics.setColor(SystemColor.controlDkShadow);
        if (bl) {
            graphics.drawLine(n, n2 + n4, n + n3, n2 + n4);
            graphics.drawLine(n + n3, n2, n + n3, n2 + n4);
        } else {
            graphics.drawLine(n + 1, n2 + 1, n + n3 - 2, n2 + 1);
            graphics.drawLine(n + 1, n2 + 1, n + 1, n2 + n4 - 2);
        }
    }

    public void printDragItem(Graphics graphics) {
        graphics.setXORMode(this.getBackground());
        if (!this.m_bColumnHeader || this.m_nYMouse + 10 > this.m_cyHeader) {
            this.drawTargetHighLight(graphics);
            if (!this.m_bDrawDragImage) {
                return;
            }
            if (this.m_imgDragPic != null) {
                graphics.drawImage(this.m_imgDragPic, this.m_nXMouse + 10, this.m_nYMouse + 10, this);
            } else {
                graphics.setColor(Color.white);
                graphics.fillRect(this.m_nXMouse + 10, this.m_nYMouse + 10, 10, 10);
                graphics.setColor(Color.black);
                graphics.drawRect(this.m_nXMouse + 10, this.m_nYMouse + 10, 10, 10);
            }
        }
    }

    protected void processActionEvent(ActionEvent actionEvent) {
        if (this.actionListener != null) {
            this.actionListener.actionPerformed(actionEvent);
        }
    }

    protected void processEvent(AWTEvent aWTEvent) {
        switch (aWTEvent.getID()) {
            case 501: {
                if ((((MouseEvent) aWTEvent).getModifiers() & 4) != 0) {
                    return;
                }
                Point point = ((MouseEvent) aWTEvent).getPoint();
                this.mouseDown((MouseEvent) aWTEvent, point.x, point.y);
                break;
            }
            case 503: {
                Point point = ((MouseEvent) aWTEvent).getPoint();
                this.mouseMove((MouseEvent) aWTEvent, point.x, point.y);
                break;
            }
            case 506: {
                Point point = ((MouseEvent) aWTEvent).getPoint();
                this.mouseDrag((MouseEvent) aWTEvent, point.x, point.y);
                break;
            }
            case 502: {
                Point point = ((MouseEvent) aWTEvent).getPoint();
                this.mouseUp((MouseEvent) aWTEvent, point.x, point.y);
                break;
            }
        }
        super.processEvent(aWTEvent);
    }

    protected void processFocusEvent(FocusEvent focusEvent) {
        switch (focusEvent.getID()) {
            case 1004: {
                this.m_bHasFocus = true;
                this.update();
                break;
            }
            case 1005: {
                this.m_bHasFocus = false;
                this.update();
            }
        }
        super.processFocusEvent(focusEvent);
    }

    protected void processItemEvent(ItemEvent itemEvent) {
        if (this.itemListener != null) {
            this.itemListener.itemStateChanged(itemEvent);
        }
    }

    protected void processKeyEvent(KeyEvent keyEvent) {
        super.processKeyEvent(keyEvent);
        int n = keyEvent.getKeyCode();
        if (keyEvent.getID() == 401) {
            switch (n) {
                case 40: {
                    if (this.prev >= this.m_arrItems.size() - 1) break;
                    if (!this.m_bMultipleSelections || !keyEvent.isShiftDown()) {
                        this.selected = new int[0];
                    }
                    this.select(++this.prev, false, false);
                    this.scrollToView(this.prev, 1005);
                    this.update();
                    break;
                }
                case 38: {
                    if (this.prev <= 0) break;
                    if (!this.m_bMultipleSelections || !keyEvent.isShiftDown()) {
                        this.selected = new int[0];
                    }
                    this.select(--this.prev, false, false);
                    this.scrollToView(this.prev, 1004);
                    this.update();
                    break;
                }
                case 36:
                case 37: {
                    this.selected = new int[0];
                    this.prev = 0;
                    this.select(this.prev, false, false);
                    this.scrollToView(this.prev, 1006);
                    this.hs.setValue(this.prev);
                    this.onLeftColChanged(this.prev);
                    this.update();
                    break;
                }
                case 35:
                case 39: {
                    this.selected = new int[0];
                    this.prev = this.m_arrItems.size() - 1;
                    this.select(this.prev, false, false);
                    this.scrollToView(this.prev, 1007);
                    this.hs.setValue(this.prev);
                    this.onLeftColChanged(this.prev);
                    this.update();
                    break;
                }
                case 34: {
                    this.prev = this.m_nTopRow + this.itemsThisPage(this.getGraphics()) - 1;
                    if (this.isSelected(this.prev)) {
                        this.m_nTopRow = this.prev;
                        this.vs.setValue(this.prev);
                        this.onTopRowChanged(this.prev);
                        this.prev = this.m_nTopRow + this.itemsThisPage(this.getGraphics()) - 1;
                    }
                    this.selected = new int[0];
                    this.prev = Math.min(this.prev, this.m_arrItems.size() - 1);
                    this.select(this.prev, false, false);
                    this.update();
                    break;
                }
                case 33: {
                    if (this.isSelected(this.m_nTopRow)) {
                        this.m_nTopRow = Math.max(this.m_nTopRow - this.itemsThisPage(this.getGraphics()), 0);
                        this.vs.setValue(this.m_nTopRow);
                        this.onTopRowChanged(this.m_nTopRow);
                    }
                    this.selected = new int[0];
                    this.prev = this.m_nTopRow;
                    this.select(this.prev, false, false);
                    this.update();
                    break;
                }
                case 127: {
                    if (this.m_bAllowDelete && this.selected.length > 0) {
                        int[] nArray = this.getSelectedIndexes();
                        int n2 = nArray.length - 1;
                        while (n2 >= 0) {
                            if (n2 != 0) {
                                this.deleteItem(nArray[n2], false);
                            } else {
                                this.deleteItem(nArray[n2], true);
                            }
                            --n2;
                        }
                    }
                    this.deselectAll();
                    break;
                }
                case 10: {
                    this.processItemEvent(new ItemEvent(this, 701, this.getSelectedListItem(), 1));
                    break;
                }
            }
        }
    }

    public void reMeasureAllItems() {
        new ListItem();
        int n = this.m_arrItems.size();
        int n2 = 0;
        while (n2 < n) {
            ((ListItem) this.m_arrItems.elementAt((int) n2)).m_nCY = -1;
            ((ListItem) this.m_arrItems.elementAt((int) n2)).m_rcText.height = -1;
            ++n2;
        }
        this.updateScrollbar();
    }

    public void removeActionListener(ActionListener actionListener) {
        this.actionListener = AWTEventMulticaster.remove(this.actionListener, actionListener);
    }

    public void removeItemListener(ItemListener itemListener) {
        this.itemListener = AWTEventMulticaster.remove(this.itemListener, itemListener);
    }

    public void removeItemTextChangedListener(ItemTextChangedListener itemTextChangedListener) {
        this.aItemTextChangedListener = ItemTextChangedEventMulticaster.remove(this.aItemTextChangedListener, itemTextChangedListener);
    }

    public synchronized void replaceItem(Object object, int n) {
        this.deleteItem(n);
        this.addItem(object, n, false);
        this.update();
    }

    public void scrollHorz(int n) {
        Rectangle rectangle = this.getInsideRect();
        Point point = this.getLogicalSize();
        int n2 = this.m_ptViewportOrg.x;
        this.m_ptViewportOrg.x -= n;
        this.m_ptViewportOrg.x = Math.max(0, this.m_ptViewportOrg.x);
        this.m_ptViewportOrg.x = Math.min(point.x - rectangle.width, this.m_ptViewportOrg.x);
        this.onLeftColChanged(this.m_ptViewportOrg.x);
        n = n2 - this.m_ptViewportOrg.x;
        this.update();
        if (this.isHeaderCtrlEnabled()) {
            Rectangle rectangle2 = this.getBounds();
            Rectangle rectangle3 = new Rectangle();
            rectangle3.y = rectangle2.y;
            rectangle3.x = rectangle2.x;
            rectangle3.width = rectangle2.width;
            rectangle3.height = 0;
            this.update();
        }
        this.hs.setUnitIncrement(10);
        this.hs.setBlockIncrement(this.getInsideRect().width - 10);
        this.hs.setValue(this.m_ptViewportOrg.x);
    }

    public void scrollToView(int n) {
        if (!this.getVScrollbar().isShowing()) {
            return;
        }
        this.m_arrItems.size();
        int n2 = this.getLastVisibleRow();
        Scrollbar scrollbar = this.getVScrollbar();
        if (n < this.m_nTopRow) {
            int n3 = scrollbar.getValue() - this.m_nTopRow + n + 2;
            if (n3 < scrollbar.getMinimum()) {
                n3 = scrollbar.getMinimum();
            }
            scrollbar.setValue(n3);
            this.onTopRowChanged(n3);
        } else if (n > n2 - 2) {
            int n4 = scrollbar.getValue() + n - n2 + 1;
            if (n4 > scrollbar.getMaximum() - 1) {
                n4 = scrollbar.getMaximum() - 1;
            }
            scrollbar.setValue(n4);
            this.onTopRowChanged(n4);
        }
        if (n == 0) {
            scrollbar.setValue(0);
            this.onTopRowChanged(0);
        }
    }

    public void scrollToView(int n, int n2) {
        int n3;
        this.m_arrItems.size();
        int n4 = this.getLastVisibleRow();
        Scrollbar scrollbar = this.getVScrollbar();
        if (n < this.m_nTopRow && n2 == 1004) {
            n3 = n;
            if (n3 < scrollbar.getMinimum()) {
                n3 = scrollbar.getMinimum();
            }
            scrollbar.setValue(n3);
            this.onTopRowChanged(n3);
        } else if (n2 == 1004) {
            this.update();
        }
        if (n > n4 && n2 == 1005) {
            n3 = n;
            int n5 = this.itemsThisPage(this.getGraphics());
            while (n > this.getLastVisibleRow()) {
                if ((n3 = n3 - this.itemsThisPage(this.getGraphics()) + 1) >= scrollbar.getMaximum() - 1) {
                    n3 = scrollbar.getMaximum() - 1;
                    scrollbar.setValue(n3);
                    this.onTopRowChanged(n3);
                    break;
                }
                n5 = this.itemsThisPage(this.getGraphics());
                scrollbar.setValue(n3);
                this.m_nTopRow = n3;
                n3 += n5;
                n4 = this.getLastVisibleRow();
            }
            this.onTopRowChanged(n3 - n5);
        } else if (n2 == 1005) {
            this.update();
        }
        if (n2 == 1006) {
            n3 = scrollbar.getMinimum();
            scrollbar.setValue(n3);
            this.onTopRowChanged(n3);
        }
        if (n2 == 1007) {
            n3 = this.getMaxTopRow() + 3;
            if (n3 >= scrollbar.getMaximum() - 1) {
                n3 = scrollbar.getMaximum() - 1;
            }
            scrollbar.setValue(++n3);
            this.onTopRowChanged(n3);
        }
    }

    public void scrollVert(int n) {
        this.scrollVert(n, false);
    }

    public void scrollVert(int n, boolean bl) {
        if (!bl) {
            int n2 = this.m_nTopRow;
            int n3 = n2 + n;
            n3 = Math.max(n3, 0);
            n = (n3 = Math.min(n3, this.m_arrItems.size() - 1)) - n2;
            if (n != 0) {
                this.m_nTopRow += n;
                this.update();
            }
        } else {
            int n4 = this.getLastFullyVisibleItem();
            int n5 = n4 + n;
            int n6 = this.calcRangeHeight(n4, n5, true);
            n = this.calcItemsInRange(n6, this.m_nTopRow, true);
            this.scrollVert(n, false);
        }
    }

    public synchronized void select(int n) {
        this.select(n, true, true);
    }

    public synchronized void select(int n, boolean bl) {
        this.select(n, bl, false);
    }

    protected synchronized void select(int n, boolean bl, boolean bl2) {
        if (n < 0 || n >= this.getItemCount()) {
            return;
        }
        int n2 = 0;
        while (n2 < this.selected.length) {
            if (this.selected[n2] == n) {
                return;
            }
            ++n2;
        }
        if (!this.m_bMultipleSelections) {
            this.selected = new int[1];
            this.selected[0] = n;
            this.prev = n;
        } else {
            int[] nArray = new int[this.selected.length + 1];
            System.arraycopy(this.selected, 0, nArray, 0, this.selected.length);
            nArray[this.selected.length] = n;
            this.selected = nArray;
            this.prev = n;
        }
        this.processItemEvent(new ItemEvent(this, 701, this.getSelectedListItem(), 1));
        if (bl2) {
            this.scrollToView(n);
        }
        if (bl) {
            this.update();
        }
    }

    public void setAllowDelete(boolean bl) {
        this.m_bAllowDelete = bl;
    }

    public void setBounds(int n, int n2, int n3, int n4) {
        super.setBounds(n, n2, n3, n4);
        if (this.m_arrColumns.size() == 1) {
            if (this.m_bAutoWrap) {
                ((Column) this.m_arrColumns.elementAt((int) 0)).m_nCX = this.getInsideRect().width;
                this.reMeasureAllItems();
            } else {
                ((Column) this.m_arrColumns.elementAt((int) 0)).m_nCX = this.getLogicalSize().x;
                if (this.getLogicalSize().x < this.getInsideRect().width) {
                    ((Column) this.m_arrColumns.elementAt((int) 0)).m_nCX = this.getInsideRect().width;
                }
                this.reMeasureAllItems();
            }
        }
    }

    public void setColumnAlignment(int n, int n2) {
        if (n < this.m_arrColumns.size()) {
            ((Column) this.m_arrColumns.elementAt(n)).setAlignment(n2);
            this.update();
        }
    }

    public void setColumnFont(int n, Font font) {
        if (n < this.m_arrColumns.size()) {
            ((Column) this.m_arrColumns.elementAt(n)).setFont(font);
            this.reMeasureAllItems();
            this.update();
        }
    }

    public void setColumnHeaderAlignment(int n, int n2) {
        if (n < this.m_arrColumns.size()) {
            ((Column) this.m_arrColumns.elementAt(n)).setHeaderAlignment(n2);
            this.update();
        }
    }

    public void setColumnLines(boolean bl) {
        this.m_bColumnLines = bl;
    }

    public void setColumnText(int n, String string) {
        if (n < this.m_arrColumns.size()) {
            ((Column) this.m_arrColumns.elementAt(n)).setText(string);
            this.reMeasureAllItems();
            this.update();
        }
    }

    public void setColumnWidth(int n, int n2) {
        if (n < this.m_arrColumns.size()) {
            ((Column) this.m_arrColumns.elementAt(n)).setWidth(n2);
            this.reMeasureAllItems();
            this.update();
        }
    }

    public void setDragDrop(boolean bl) {
        this.m_bDragModeAllowed = bl;
    }

    public void setDragDropImage(Image image) {
        this.m_imgDragPic = image;
    }

    public void setDrawDragImage(boolean bl) {
        this.m_bDrawDragImage = bl;
    }

    public void setEditMode(boolean bl) {
        this.m_bEditModeAllowed = bl;
    }

    public void setFont(Font font) {
        if (font == null) {
            return;
        }
        super.setFont(font);
        this.defaultfont = font;
        FontMetrics fontMetrics = this.getFontMetrics(font);
        this.m_PCheight = fontMetrics.getHeight();
    }

    public void setGridLineColor(Color color) {
        this.m_colGridLines = color;
    }

    public void setGridLines(boolean bl) {
        this.m_bColumnLines = bl;
        this.m_bItemLines = bl;
    }

    public void setGridLineStyle(int n) {
        this.m_nStyleGridLines = n == 0 ? 0 : 1;
    }

    public int setImageList(Image image) {
        if (this.m_arrImages == null) {
            this.m_arrImages = new Vector();
            this.m_arrImageIDs = new Vector();
        }
        int n = this.m_arrImages.size();
        this.setImageList(image, n);
        return n;
    }

    public void setImageList(Image image, int n) {
        if (this.m_arrImages == null) {
            this.m_arrImages = new Vector();
            this.m_arrImageIDs = new Vector();
        }
        this.m_arrImages.addElement(image);
        this.m_arrImageIDs.addElement(new Integer(n));
    }

    public void setItemLines(boolean bl) {
        this.m_bItemLines = bl;
    }

    public void updateScrollbar() {
        this.updateScrollbar(true);
    }

    public void updateScrollbar(boolean bl) {
        Rectangle rectangle = new Rectangle();
        Point point = this.getLogicalSize();
        int n = this.itemsThisPage(this.getGraphics());
        rectangle = this.getInsideRect();
        if (point.y > n) {
            if (this.m_nTopRow > point.y - n) {
                this.scrollVert(point.y - n - this.m_nTopRow);
            }
            this.vs.setValues(this.m_nTopRow, n, 0, this.m_arrItems.size());
            this.vs.setBlockIncrement(Math.max(n, 1));
            this.showVScrollbar();
        } else {
            if (this.m_nTopRow > 0) {
                this.scrollVert(-this.m_nTopRow);
            }
            this.hideVScrollbar();
        }
        if (point.x > rectangle.width) {
            if (this.m_ptViewportOrg.x > point.x - rectangle.width) {
                this.scrollHorz(point.x - rectangle.width - this.m_ptViewportOrg.x);
            }
            this.hs.setValues(this.m_ptViewportOrg.x, rectangle.width, 0, point.x);
            this.hs.setBlockIncrement(Math.max(this.getInsideRect().width - 10, 10));
            this.hs.setUnitIncrement(10);
            this.showHScrollbar();
        } else {
            this.m_ptViewportOrg.x = 0;
            this.onLeftColChanged(this.m_ptViewportOrg.x);
            this.hideHScrollbar();
        }
        if (bl) {
            this.doLayout();
        }
    }
}

