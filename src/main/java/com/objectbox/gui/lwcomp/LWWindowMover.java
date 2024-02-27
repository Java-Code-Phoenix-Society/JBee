/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import com.objectbox.runner.gui.JBee;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;

public class LWWindowMover
        extends Container {
    static final Color BGCOL = new Color(220, 220, 255);
    static final Color TXCOL = BGCOL.brighter().brighter();
    static final Color BUTCOL = new Color(250, 100, 200);
    Window windowtomove = null;
    private int offsetx = 0;
    private int offsety = 0;
    private int startx_mouse = 0;
    private int starty_mouse = 0;
    private int startx_window = 0;
    private int starty_window = 0;
    private String title = "";
    private Font titlefont = null;
    private int headerheight = 5;
    private Dimension pref_size = new Dimension(10, 2);
    private Image gradient = null;
    private Image left = null;
    private Image right = null;
    private Rectangle quitdrawrect = null;
    private Cursor crosshair = new Cursor(1);
    private Cursor defaultcursor = new Cursor(0);

    public LWWindowMover(Window window, String string) {
        this.enableEvents(48L);
        this.windowtomove = window;
        this.title = string;
        LayoutManager layoutManager = null;
        this.setLayout(layoutManager);
        this.titlefont = new Font("Sansserif", 0, 8);
    }

    public void addNotify() {
        super.addNotify();
        Dimension dimension = this.getSize();
        if (dimension.width == 0 || dimension.height == 0) {
            this.setSize(this.getPreferredSize());
        }
    }

    public int getHeaderheight() {
        return this.headerheight;
    }

    public void setHeaderheight(int n) {
        this.headerheight = n;
        this.pref_size = new Dimension(this.getSize().width, this.headerheight);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Image getImageResource(String string) {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(string);
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            if (inputStream == null) return null;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                while (true) {
                    byteArrayOutputStream.write(dataInputStream.readByte());
                }
            } catch (IOException iOException) {
                byte[] byArray = byteArrayOutputStream.toByteArray();
                ((OutputStream) byteArrayOutputStream).close();
                dataInputStream.close();
                Image image = Toolkit.getDefaultToolkit().createImage(byArray);
                MediaTracker mediaTracker = new MediaTracker(this);
                mediaTracker.addImage(image, 0);
                mediaTracker.waitForID(0);
                return image;
            }
        } catch (Exception exception) {
            return null;
        }
    }

    public Dimension getMaximumSize() {
        return this.pref_size;
    }

    public Dimension getMinimumSize() {
        return this.pref_size;
    }

    public Dimension getPreferredSize() {
        return this.pref_size;
    }

    public void paint(Graphics graphics) {
        int n = this.getSize().width;
        int n2 = this.getSize().height;
        if (this.quitdrawrect == null) {
            this.quitdrawrect = new Rectangle(n - 12, 2, 10, n2 - 3);
        }
        graphics.setColor(BGCOL);
        graphics.fillRect(0, 0, n, n2);
        graphics.setColor(Color.black);
        graphics.drawRect(0, 0, n - 1, n2 - 1);
        graphics.setColor(BGCOL);
        graphics.fill3DRect(this.quitdrawrect.x, this.quitdrawrect.y, this.quitdrawrect.width, this.quitdrawrect.height, false);
        super.paint(graphics);
    }

    public void processMouseEvent(MouseEvent mouseEvent) {
        switch (mouseEvent.getID()) {
            case 501: {
                this.setCursor(this.crosshair);
                this.offsetx = mouseEvent.getX();
                this.offsety = mouseEvent.getY();
                this.startx_mouse = mouseEvent.getX();
                this.starty_mouse = mouseEvent.getY();
                this.startx_window = this.windowtomove.getLocation().x;
                this.starty_window = this.windowtomove.getLocation().y;
                if (this.quitdrawrect != null && this.quitdrawrect.contains(this.offsetx, this.offsety) && this.windowtomove instanceof JBSmallWindow) {
                    ((JBSmallWindow) this.windowtomove).kill();
                }
                this.windowtomove.toFront();
                this.windowtomove.requestFocus();
                break;
            }
            case 502: {
                if (JBee.OS_type == 1) {
                    int n = this.windowtomove.getLocationOnScreen().x;
                    int n2 = this.windowtomove.getLocationOnScreen().y;
                    ((Component) this.windowtomove).setLocation(n += mouseEvent.getX() - this.offsetx, n2 += mouseEvent.getY() - this.offsety);
                } else {
                    ((Component) this.windowtomove).setLocation(mouseEvent.getX() - this.startx_mouse + this.startx_window, mouseEvent.getY() - this.starty_mouse + this.starty_window);
                }
                this.setCursor(this.defaultcursor);
                break;
            }
            case 504: {
                break;
            }
        }
        super.processMouseEvent(mouseEvent);
    }

    public void processMouseMotionEvent(MouseEvent mouseEvent) {
        switch (mouseEvent.getID()) {
            case 502: {
                break;
            }
            case 504: {
                break;
            }
            case 505: {
                break;
            }
            case 506: {
                if (JBee.OS_type != 1) break;
                int n = this.windowtomove.getLocationOnScreen().x;
                int n2 = this.windowtomove.getLocationOnScreen().y;
                ((Component) this.windowtomove).setLocation(n += mouseEvent.getX() - this.offsetx, n2 += mouseEvent.getY() - this.offsety);
            }
        }
        super.processMouseEvent(mouseEvent);
    }

    public void update(Graphics graphics) {
        this.paint(graphics);
    }
}

