/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class TableView
        extends Panel
        implements ICellEditor,
        ICellRenderer,
        AdjustmentListener {
    public static final int CELL_SELECT = 0;
    public static final int ROW_SELECT = 1;
    public static final int COL_SELECT = 2;
    public static final int MODIFIER_NONE = 0;
    public static final int MODIFIER_SHIFT = 1;
    public static final int MODIFIER_CTRL = 2;
    protected transient CellselectedListener aCellselectedListener = null;
    protected transient PropertyChangeSupport propertyChange;
    protected transient CellChoosenListener aCellChoosenListener = null;
    protected Scrollbar verticalScroll;
    protected Scrollbar horizontalScroll;
    protected Hashtable row_select_hash = null;
    Rectangle cliprect = null;
    private int NumberOfcols = 1;
    private int NumberOfRows = 0;
    private boolean HasHeader = true;
    private AbstrTableModel datamodel = null;
    private int x = 0;
    private int space = 4;
    private int cellwidth = 40;
    private int cellheight = 25;
    private int headerheight = 25;
    private int ymid = 0;
    private int xsize = 0;
    private int ysize = 0;
    private int currentcol = 0;
    private int currentrow = 1;
    private int translateX = 0;
    private int translateY = 0;
    private int scrollxvalue = 0;
    private int scrollyvalue = 0;
    private boolean hasfocus = false;
    private boolean[] bestfit;
    private boolean hasvericalscroll = false;
    private boolean hashorisontalscroll = false;
    private int[] colwidths;
    private boolean initialized = false;
    private Color fieldHeaderforeground = new Color(255);
    private Color fieldHeaderbackground = new Color(100);
    private boolean fieldVerticallines = true;
    private boolean fieldHorizontallines = true;
    private Image offScreenImage;
    private boolean DEBUG = true;
    private Image[] imagearray = null;
    private boolean fieldAutofit = true;
    private int editCol = 0;
    private int editRow = 0;
    private Dialog defaultEditor = null;
    private boolean dialogSvar = true;
    private boolean isEditing = false;
    private ICellRenderer cellrenderer = this;
    private ICellEditor celleditor = this;
    private Component activecelleditor = null;
    private boolean clipon = true;
    private int selectmode = 0;
    private int modifier = 0;

    public TableView() {
        this.setLayout(null);
        this.enableEvents(63L);
        this.initialize();
    }

    public static void main(String[] stringArray) {
        try {
            Frame frame;
            Serializable serializable;
            try {
                serializable = Class.forName("com.ibm.uvm.abt.edit.TestFrame");
                frame = (Frame) ((Class) serializable).newInstance();
            } catch (Throwable throwable) {
                frame = new Frame();
            }
            serializable = new TableView();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
        }
    }

    public void activateEditorComponent(Component component, int n, int n2, int n3, int n4, int n5, int n6) {
        component.setLocation(n3 - this.getTranslate().x, n4 - this.getTranslate().y);
        component.setSize(n5 - n3, n6 - n4);
        component.setVisible(true);
        this.setActiveEditor(component);
        this.addNotify();
        this.add(component);
        component.requestFocus();
    }

    public void addCellChoosenListener(CellChoosenListener cellChoosenListener) {
        this.aCellChoosenListener = CellChoosenEventMulticaster.add(this.aCellChoosenListener, cellChoosenListener);
    }

    public void addCellselectedListener(CellselectedListener cellselectedListener) {
        this.aCellselectedListener = CellselectedEventMulticaster.add(this.aCellselectedListener, cellselectedListener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().addPropertyChangeListener(propertyChangeListener);
    }

    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        if (this.activecelleditor != null) {
            this.remove(this.activecelleditor);
        }
        this.translateX = this.horizontalScroll.getValue();
        this.scrollyvalue = this.verticalScroll.getValue();
        this.translateY = this.scrollyvalue * this.cellheight;
        this.repaint();
    }

    public void autoFitAll() {
        int n = this.getSize().width / this.colwidths.length;
        int n2 = 0;
        while (n2 < this.colwidths.length) {
            this.colwidths[n2] = n;
            ++n2;
        }
    }

    public void autoFitColumn(int n) {
        if (!this.getAutofit()) {
            return;
        }
        if (this.colwidths[n] != this.getCellwidth()) {
            this.colwidths[n] = this.getCellwidth();
        } else {
            int n2 = 0;
            TableCellAttribute tableCellAttribute = null;
            String string = new String();
            int n3 = 0;
            Font font = this.getFont();
            if (font != null) {
                this.colwidths[n] = 10;
                FontMetrics fontMetrics = this.getFontMetrics(font);
                int n4 = 0;
                while (n4 < this.NumberOfRows) {
                    n2 = 0;
                    tableCellAttribute = this.getModel().getCellAttribute(n, n4);
                    int[] nArray = tableCellAttribute.getSymbolarray();
                    if (nArray != null) {
                        int n5 = 0;
                        while (n5 < nArray.length) {
                            n2 += this.imagearray[nArray[n5]].getWidth(this) + 4;
                            ++n5;
                        }
                    }
                    string = this.getModel().getCell(n, n4);
                    n3 = fontMetrics.stringWidth(string) + 10 + n2;
                    this.colwidths[n] = Math.max(this.colwidths[n], n3);
                    ++n4;
                }
            } else {
                JBLogger.log("f er null");
            }
        }
    }

    public void editCell(int n, int n2) {
    }

    protected void fireCellChoosen(CellChoosenEvent cellChoosenEvent) {
        if (this.aCellChoosenListener == null) {
            return;
        }
        this.aCellChoosenListener.cellChoosen(cellChoosenEvent);
    }

    protected void fireCellselected(CellselectedEvent cellselectedEvent) {
        if (this.aCellselectedListener == null) {
            return;
        }
        this.aCellselectedListener.cellselected(cellselectedEvent);
    }

    public void firePropertyChange(String string, Object object, Object object2) {
        this.getPropertyChange().firePropertyChange(string, object, object2);
    }

    public Component getActiveEditor() {
        return this.activecelleditor;
    }

    public void setActiveEditor(Component component) {
        this.activecelleditor = component;
    }

    public boolean getAutofit() {
        return this.fieldAutofit;
    }

    public void setAutofit(boolean bl) {
        boolean bl2 = this.fieldAutofit;
        this.fieldAutofit = bl;
        this.firePropertyChange("autofit", new Boolean(bl2), new Boolean(bl));
    }

    public ICellEditor getCelleditor() {
        return this.celleditor;
    }

    public void setCelleditor(ICellEditor iCellEditor) {
        this.celleditor = iCellEditor;
    }

    public int getCellheight() {
        return this.cellheight;
    }

    public void setCellheight(int n) {
        this.cellheight = n;
        this.repaint();
    }

    public ICellRenderer getCellrenderer() {
        return this.cellrenderer;
    }

    public void setCellrenderer(ICellRenderer iCellRenderer) {
        this.cellrenderer = iCellRenderer;
    }

    public int getCellScreenXPos(int n) {
        int n2 = 0;
        if (n == 0) {
            return 0;
        }
        int n3 = 0;
        while (n3 < n) {
            n2 += this.colwidths[n3];
            ++n3;
        }
        return n2;
    }

    public int getCellwidth() {
        return this.cellwidth;
    }

    public void setCellwidth(int n) {
        this.cellwidth = n;
        if (this.colwidths != null) {
            int n2 = 0;
            while (n2 < this.NumberOfcols) {
                this.colwidths[n2] = this.cellwidth;
                this.bestfit[n2] = true;
                ++n2;
            }
        }
        this.repaint();
    }

    public int getColumnWidth(int n) throws Exception {
        if (n < 0 || n > this.colwidths.length) {
            throw new Exception("No such column.");
        }
        return this.colwidths[n];
    }

    public Point getCurrent() {
        return new Point(this.currentcol, this.currentrow);
    }

    public int getCurrentCol() {
        return this.currentcol;
    }

    public int getCurrentRow() {
        return this.currentrow;
    }

    public AbstrTableModel getDefaultModel() {
        return this.datamodel;
    }

    public void setDefaultModel(AbstrTableModel abstrTableModel) {
        this.datamodel = abstrTableModel;
        this.repaint();
    }

    public boolean getHasHeader() {
        return this.HasHeader;
    }

    public void setHasHeader(boolean bl) {
        this.HasHeader = bl;
        this.repaint();
    }

    public Color getHeaderbackground() {
        return this.fieldHeaderbackground;
    }

    public void setHeaderbackground(Color color) {
        Color color2 = this.fieldHeaderbackground;
        this.fieldHeaderbackground = color;
        this.firePropertyChange("headerbackground", color2, color);
        this.repaint();
    }

    public Color getHeaderforeground() {
        return this.fieldHeaderforeground;
    }

    public void setHeaderforeground(Color color) {
        Color color2 = this.fieldHeaderforeground;
        this.fieldHeaderforeground = color;
        this.firePropertyChange("headerforeground", color2, color);
        this.repaint();
    }

    public boolean getHorizontallines() {
        return this.fieldHorizontallines;
    }

    public void setHorizontallines(boolean bl) {
        boolean bl2 = this.fieldHorizontallines;
        this.fieldHorizontallines = bl;
        this.firePropertyChange("horizontallines", new Boolean(bl2), new Boolean(bl));
        this.repaint();
    }

    public Dimension getMaximumSize() {
        return this.getTotalSize();
    }

    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }

    public AbstrTableModel getModel() {
        if (this.datamodel == null) {
            this.datamodel = new DefaultTableModel();
        }
        return this.datamodel;
    }

    public void setModel(AbstrTableModel abstrTableModel) {
        int n = this.currentrow;
        if (this.getActiveEditor() != null) {
            this.remove(this.getActiveEditor());
        }
        this.datamodel = abstrTableModel;
        String[] stringArray = this.datamodel.getHeaders();
        this.setNumberOfRows(abstrTableModel.getNumberOfRows());
        this.setNumberOfcols(stringArray.length);
        this.row_select_hash = new Hashtable();
        this.setCurrent(Math.min(this.currentcol, n), Math.min(this.currentrow, n));
    }

    public int getModifier() {
        return this.modifier;
    }

    public void setModifier(int n) {
        this.modifier = n;
    }

    public int getNumberOfcols() {
        this.NumberOfcols = this.datamodel.getNumberOfColumns();
        return this.NumberOfcols;
    }

    public void setNumberOfcols(int n) {
        try {
            this.NumberOfcols = n;
            this.colwidths = new int[n];
            this.bestfit = new boolean[n];
            if (this.getModel() instanceof DefaultTableModel) {
                DefaultTableModel defaultTableModel = (DefaultTableModel) this.getModel();
                String[] stringArray = new String[this.NumberOfcols];
                int n2 = 0;
                while (n2 < stringArray.length) {
                    new String();
                    stringArray[n2] = String.valueOf(n2);
                    ++n2;
                }
                defaultTableModel.setHeaders(stringArray);
            }
            int n3 = 0;
            while (n3 < n) {
                this.colwidths[n3] = this.cellwidth;
                this.bestfit[n3] = true;
                ++n3;
            }
            this.repaint();
        } catch (Exception exception) {
            JBLogger.log("Exception i TableView.setNumberofcols " + this.getName() + "\n" + exception);
        }
    }

    public int getNumberOfRows() {
        this.NumberOfRows = this.datamodel.getNumberOfRows();
        return this.NumberOfRows;
    }

    public void setNumberOfRows(int n) {
        this.NumberOfRows = n;
        if (this.getModel() instanceof DefaultTableModel) {
            DefaultTableModel defaultTableModel = (DefaultTableModel) this.getModel();
            defaultTableModel.setNumberOfRows(n);
        }
        this.repaint();
    }

    public Dimension getPreferredSize() {
        return this.getTotalSize();
    }

    protected PropertyChangeSupport getPropertyChange() {
        if (this.propertyChange == null) {
            this.propertyChange = new PropertyChangeSupport(this);
        }
        return this.propertyChange;
    }

    public Enumeration getRowSelectionEnumeration() {
        return this.row_select_hash.keys();
    }

    public int getSelectmode() {
        return this.selectmode;
    }

    public void setSelectmode(int n) {
        this.selectmode = n;
    }

    public Dimension getTotalSize() {
        try {
            int n = 0;
            int n2 = 0;
            if (this.colwidths == null) {
                this.setNumberOfcols(this.getModel().getNumberOfColumns());
            } else {
                int n3 = 0;
                while (n3 < this.colwidths.length) {
                    n += this.colwidths[n3];
                    ++n3;
                }
            }
            n2 = this.getModel().getNumberOfRows() * this.cellheight;
            return new Dimension(n, n2);
        } catch (Exception exception) {
            JBLogger.log("Exception i TableView.getTotalSize " + this.getName() + "\n" + exception);
            return this.getSize();
        }
    }

    public Point getTranslate() {
        return new Point(this.translateX, this.translateY);
    }

    public boolean getVerticallines() {
        return this.fieldVerticallines;
    }

    public void setVerticallines(boolean bl) {
        boolean bl2 = this.fieldVerticallines;
        this.fieldVerticallines = bl;
        this.firePropertyChange("verticallines", new Boolean(bl2), new Boolean(bl));
        this.repaint();
    }

    private void handleException(Throwable throwable) {
        JBLogger.log("--------- UNCAUGHT EXCEPTION ---------\n" + this.getName());
        throwable.printStackTrace(System.out);
    }

    public boolean hasEditor(TableView tableView) {
        return false;
    }

    public boolean hasFocus() {
        return this.hasfocus;
    }

    public void highligthCell(int n, int n2) {
        Graphics graphics = this.getGraphics();
        new String();
        graphics.translate(-this.translateX, -this.translateY);
        if (graphics != null) {
            if (this.selectmode == 1) {
                int n3 = 0;
                while (n3 < this.getModel().getNumberOfColumns()) {
                    int n4;
                    int n5 = n4 = this.getCellScreenXPos(n3);
                    int n6 = n2 * this.cellheight;
                    if (this.clipon) {
                        graphics.setClip(n5, n6, this.colwidths[n3], this.cellheight + 1);
                    }
                    if (n2 > 0) {
                        this.cellrenderer.renderCell(this, graphics, n3, n2, n5, n6, n5 + this.colwidths[n3], n6 + this.cellheight, true);
                    } else {
                        this.cellrenderer.renderCell(this, graphics, n3, n2, n5, n6, n5 + this.colwidths[n3], n6 + this.cellheight, true);
                    }
                    ++n3;
                }
            } else {
                int n7;
                int n8 = n7 = this.getCellScreenXPos(n);
                int n9 = n2 * this.cellheight;
                if (this.clipon) {
                    graphics.setClip(n8, n9, this.colwidths[n], this.cellheight + 1);
                }
                if (n2 > 0) {
                    this.cellrenderer.renderCell(this, graphics, n, n2, n8, n9, n8 + this.colwidths[n], n9 + this.cellheight, true);
                } else {
                    this.cellrenderer.renderCell(this, graphics, n, n2, n8, n9, n8 + this.colwidths[n], n9 + this.cellheight, true);
                }
            }
        }
    }

    private void initConnections() {
    }

    public void initGraphics() {
        LayoutManager layoutManager;
        this.initialized = true;
        this.setModel(this.getModel());
        this.addMouseListener(new TableViewMouseListener(this));
        this.addMouseMotionListener(new TableViewMouseMotionListener(this));
        this.addKeyListener(new TableViewKeyListener(this));
        Container container = this.getParent();
        if (container != null && (layoutManager = container.getLayout()) != null && layoutManager instanceof BorderLayout) {
            this.verticalScroll = new Scrollbar(1);
            this.horizontalScroll = new Scrollbar(0);
            this.verticalScroll.addAdjustmentListener(this);
            this.horizontalScroll.addAdjustmentListener(this);
            container.add((Component) this.verticalScroll, "East");
            container.add((Component) this.horizontalScroll, "South");
            this.hasvericalscroll = true;
            this.hashorisontalscroll = true;
            container.doLayout();
        }
    }

    private void initialize() {
        this.setName("TableView");
        this.setSize(150, 150);
        this.initConnections();
    }

    public void invalidate() {
        if (this.getActiveEditor() != null) {
            this.remove(this.getActiveEditor());
        }
        super.invalidate();
    }

    public void invokeEditor(TableView tableView, int n, int n2, int n3, int n4, int n5, int n6) {
    }

    public boolean isFocusTraversable() {
        return true;
    }

    public Point mapGridToScreen(Point point) {
        return null;
    }

    public Point mapScreenToGrid(Point point) {
        point.x += this.translateX;
        point.y += this.translateY;
        boolean bl = false;
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        if (this.colwidths.length > 1) {
            if (point.x < this.colwidths[0]) {
                n = 0;
                bl = true;
            }
            if (!bl) {
                int n5 = 1;
                while (n5 < this.colwidths.length) {
                    n4 = (n3 += this.colwidths[n5 - 1]) + this.colwidths[n5];
                    if (point.x >= n3 && point.x <= n4) {
                        n = n5;
                        bl = true;
                        break;
                    }
                    ++n5;
                }
            }
        }
        if (!bl) {
            n = this.NumberOfcols;
        }
        n2 = point.y / this.cellheight;
        Point point2 = new Point(this.currentcol, this.currentrow);
        if (n != this.currentcol || n2 != this.currentrow) {
            point2 = new Point(Math.min(n, this.NumberOfcols - 1), Math.min(n2, this.NumberOfRows - 1));
        }
        return point2;
    }

    public void paint(Graphics graphics) {
        int n = this.getSize().width;
        int n2 = this.getSize().height;
        if (this.offScreenImage == null) {
            this.offScreenImage = this.createImage(n, n2);
        }
        if (n != this.xsize || n2 != this.ysize) {
            this.xsize = n;
            this.ysize = n2;
            this.offScreenImage.flush();
            this.offScreenImage = this.createImage(this.xsize, this.ysize);
        }
        Graphics graphics2 = this.offScreenImage.getGraphics();
        graphics2.clearRect(0, 0, this.xsize, this.ysize);
        graphics2.setColor(graphics.getColor());
        graphics2.translate(-this.translateX, -this.translateY);
        if (this.datamodel == null) {
            this.setModel(new DefaultTableModel());
        }
        if (!this.initialized) {
            this.initGraphics();
        }
        Dimension dimension = this.getTotalSize();
        Dimension dimension2 = this.getSize();
        int n3 = dimension.height / this.cellheight;
        int n4 = dimension2.height / this.cellheight;
        int n5 = dimension.width - dimension2.width + this.cellwidth;
        if (this.hasvericalscroll) {
            if (dimension.height > dimension2.height) {
                this.verticalScroll.setMinimum(0);
                int cfr_ignored_0 = n3 - n4;
                this.verticalScroll.setMaximum(n3);
                this.verticalScroll.setVisibleAmount(n4);
                this.verticalScroll.setValue(this.translateY / this.cellheight);
                if (!this.verticalScroll.isVisible()) {
                    this.verticalScroll.setVisible(true);
                    this.setSize(n - this.verticalScroll.getSize().width, n2);
                    this.getParent().validate();
                }
            } else {
                this.verticalScroll.setVisible(false);
                this.setSize(this.getParent().getSize());
                this.translateY = 0;
            }
        }
        if (this.hashorisontalscroll) {
            if (this.translateX > 0 || dimension.width + this.translateX > dimension2.width) {
                this.horizontalScroll.setValues(this.translateX, 10, 0, n5);
                this.horizontalScroll.setVisible(true);
                this.getParent().validate();
            } else {
                this.horizontalScroll.setVisible(false);
                this.getParent().validate();
            }
        }
        if (this.fieldAutofit) {
            this.autoFitAll();
        }
        this.showTable(graphics2);
        graphics.drawImage(this.offScreenImage, 0, 0, this);
        graphics2.dispose();
    }

    public void removeCellChoosenListener(CellChoosenListener cellChoosenListener) {
        this.aCellChoosenListener = CellChoosenEventMulticaster.remove(this.aCellChoosenListener, cellChoosenListener);
    }

    public void removeCellselectedListener(CellselectedListener cellselectedListener) {
        this.aCellselectedListener = CellselectedEventMulticaster.remove(this.aCellselectedListener, cellselectedListener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().removePropertyChangeListener(propertyChangeListener);
    }

    public void renderCell(TableView tableView, Graphics graphics, int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int n7 = fontMetrics.getHeight();
        int n8 = this.cellheight / 2 - n7 / 2;
        if (n2 == 0) {
            graphics.setColor(Color.lightGray);
            graphics.fill3DRect(n3, n4, n5 - n3, n6 - n4, true);
            graphics.setColor(Color.black);
            graphics.drawString(this.getModel().getHeaders()[n], n3 + this.space, n4 + this.cellheight - n8);
        } else {
            graphics.setColor(Color.black);
            if (bl) {
                graphics.setColor(Color.gray);
                graphics.fillRect(n3, n4, n5 - n3, n6 - n4);
                graphics.setColor(Color.white);
            } else {
                graphics.setColor(Color.white);
                graphics.fillRect(n3, n4, n5 - n3, n6 - n4);
                graphics.setColor(Color.black);
                graphics.drawRect(n3, n4, n5 - n3, n6 - n4);
            }
            graphics.drawString(this.getModel().getCell(n, n2), n3 + this.space, n4 + this.cellheight - n8);
        }
    }

    public int search(String string) {
        String string2;
        string = string.toLowerCase();
        int n = this.currentrow + 1;
        while (n < this.NumberOfRows) {
            string2 = this.getModel().getCell(this.currentcol, n);
            if ((string2 = string2.toLowerCase()).startsWith(string)) {
                return n;
            }
            ++n;
        }
        n = 1;
        while (n < this.currentrow) {
            string2 = this.getModel().getCell(this.currentcol, n);
            if ((string2 = string2.toLowerCase()).startsWith(string)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    public void selectCell(int n, int n2) {
        this.unhighligthCell(this.currentcol, this.currentrow);
        this.currentcol = n;
        this.currentrow = n2;
        this.highligthCell(this.currentcol, this.currentrow);
        this.fireCellselected(new CellselectedEvent(this, this.currentcol, this.currentrow));
    }

    public void setCellRenderer(ICellRenderer iCellRenderer) {
        this.cellrenderer = iCellRenderer;
    }

    public void setClipCell(boolean bl) {
        this.clipon = bl;
    }

    public void setColumnWidth(int n, int n2) throws Exception {
        if (n < 0 || n > this.colwidths.length) {
            throw new Exception("No such column.");
        }
        this.colwidths[n] = n2;
    }

    public void setCurrent(int n, int n2) {
        if (this.activecelleditor != null) {
            this.remove(this.activecelleditor);
        }
        boolean bl = true;
        if (n < 0) {
            n = 0;
        } else if (n >= this.NumberOfcols) {
            n = this.NumberOfcols - 1;
        }
        if (n2 < 0) {
            n2 = 0;
        } else if (n2 >= this.NumberOfRows) {
            n2 = this.NumberOfRows - 1;
        }
        this.currentcol = n;
        this.currentrow = n2;
        int n3 = this.getCellScreenXPos(n) - this.translateX;
        int n4 = n3 + this.colwidths[n];
        int n5 = n2 * this.cellheight - this.translateY;
        int n6 = n5 + this.cellheight;
        Dimension dimension = this.getSize();
        if (n3 <= 0 && n4 >= dimension.width) {
            bl = false;
        }
        if (n5 < this.cellheight) {
            this.translateY += n5;
            if (this.currentrow <= 1) {
                this.currentrow = 1;
                this.translateY = 0;
            } else {
                this.translateY -= this.cellheight * 1;
            }
            this.repaint();
        } else if (n6 > dimension.height) {
            int n7 = (n6 - dimension.height) / this.cellheight + 1;
            this.translateY += n7 * this.cellheight;
            this.repaint();
        }
        if (n3 < 0 && bl) {
            this.translateX += n3;
            this.repaint();
        } else if (n4 > dimension.width && bl) {
            this.translateX += n4 - dimension.width;
            this.repaint();
        }
    }

    public void setHasFocus(boolean bl) {
        this.hasfocus = bl;
    }

    public void setImages(Image[] imageArray) {
        this.imagearray = imageArray;
    }

    public void showHeaders(Graphics graphics) {
        int n = this.getNumberOfcols();
        this.ymid = this.cellheight / 2;
        int n2 = 1;
        int n3 = 0;
        while (n3 < n) {
            graphics.setColor(this.getHeaderbackground());
            graphics.fill3DRect(n2, 0, this.colwidths[n3], this.cellheight, true);
            graphics.setColor(this.getHeaderforeground());
            graphics.drawString(this.datamodel.getHeaders()[n3], n2 + this.space, this.cellheight);
            n2 += this.colwidths[n3];
            ++n3;
        }
    }

    public void showTable(Graphics graphics) {
        new String();
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        Dimension dimension = this.getTotalSize();
        Dimension dimension2 = this.getSize();
        Point point = this.mapScreenToGrid(new Point(0, 0));
        Point point2 = this.mapScreenToGrid(new Point(dimension2.width, dimension2.height));
        int cfr_ignored_0 = dimension.width;
        int cfr_ignored_1 = dimension.height;
        n = point.x;
        while (n <= point2.x) {
            n3 = this.getCellScreenXPos(n);
            if (this.clipon) {
                graphics.setClip(n3, this.translateY, n3 + this.colwidths[n], this.cellheight);
            }
            this.cellrenderer.renderCell(this, graphics, n, 0, n3, this.translateY, n3 + this.colwidths[n], this.translateY + this.cellheight, false);
            ++n;
        }
        boolean bl = false;
        Point point3 = new Point(0, 0);
        n = point.x;
        while (n <= point2.x) {
            n3 = this.getCellScreenXPos(n);
            n2 = point.y + 1;
            while (n2 <= point2.y) {
                n4 = n2 * this.cellheight;
                point3.y = n2;
                this.datamodel.getCell(n, n2);
                if (this.selectmode == 0) {
                    bl = n2 == this.currentrow && n == this.currentcol;
                } else if (this.selectmode == 1) {
                    boolean bl2 = bl = n2 == this.currentrow || this.row_select_hash.get(point3) != null;
                }
                if (bl) {
                    if (this.clipon) {
                        graphics.setClip(n3, n4, n3 + this.colwidths[n], this.cellheight + 1);
                    }
                    this.cellrenderer.renderCell(this, graphics, n, n2, n3, n4, n3 + this.colwidths[n], n4 + this.cellheight, true);
                } else {
                    if (this.clipon) {
                        graphics.setClip(n3, n4, n3 + this.colwidths[n], this.cellheight + 1);
                    }
                    this.cellrenderer.renderCell(this, graphics, n, n2, n3, n4, n3 + this.colwidths[n], n4 + this.cellheight, false);
                }
                ++n2;
            }
            ++n;
        }
    }

    public boolean startCellEditor(int n, int n2) {
        try {
            if (this.getCelleditor() == null) {
                return false;
            }
            if (this.getActiveEditor() != null) {
                this.remove(this.getActiveEditor());
            }
            int n3 = this.getCellScreenXPos(n);
            int n4 = n2 * this.getCellheight();
            int n5 = n3 + this.getColumnWidth(n);
            int n6 = n4 + this.getCellheight();
            this.getCelleditor().invokeEditor(this, n, n2, n3, n4, n5, n6);
            return true;
        } catch (Exception exception) {
            System.out.println(exception);
            return false;
        }
    }

    public void translate(int n, int n2) {
        this.translateX = n;
        this.translateY = n2;
    }

    public void unhighligthCell(int n, int n2) {
        Graphics graphics = this.getGraphics();
        new String();
        graphics.translate(-this.translateX, -this.translateY);
        if (graphics != null) {
            if (this.selectmode == 1) {
                int n3 = 0;
                while (n3 < this.getModel().getNumberOfColumns()) {
                    int n4;
                    int n5 = n4 = this.getCellScreenXPos(n3);
                    int n6 = n2 * this.cellheight;
                    if (this.clipon) {
                        graphics.setClip(n5, n6, this.colwidths[n3], this.cellheight + 1);
                    }
                    if (n2 > 0) {
                        this.cellrenderer.renderCell(this, graphics, n3, n2, n5, n6, n5 + this.colwidths[n3], n6 + this.cellheight, false);
                    } else {
                        this.cellrenderer.renderCell(this, graphics, n3, n2, n5, n6, n5 + this.colwidths[n3], n6 + this.cellheight, false);
                    }
                    ++n3;
                }
            } else {
                int n7;
                int n8 = n7 = this.getCellScreenXPos(n);
                int n9 = n2 * this.cellheight;
                if (this.clipon) {
                    graphics.setClip(n8, n9, this.colwidths[n], this.cellheight + 1);
                }
                if (n2 > 0) {
                    this.cellrenderer.renderCell(this, graphics, n, n2, n8, n9, n8 + this.colwidths[n], n9 + this.cellheight, false);
                } else {
                    this.cellrenderer.renderCell(this, graphics, n, n2, n8, n9, n8 + this.colwidths[n], n9 + this.cellheight, false);
                }
            }
        }
    }

    public void update(Graphics graphics) {
        this.paint(graphics);
    }
}

