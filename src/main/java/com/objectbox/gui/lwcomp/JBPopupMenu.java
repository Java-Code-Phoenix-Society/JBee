/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import com.objectbox.runner.gui.JBee;
import com.objectbox.runner.gui.tree.Node;
import com.objectbox.runner.util.JBLogger;
import com.objectbox.runner.util.MenuGridLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;

public class JBPopupMenu
        extends Window
        implements OnActiveListener,
        ActionListener,
        WindowListener {
    public static String platform;
    static Class class$java$awt$Window;
    private static MenuCloser closerThread;
    final int textspace = 8;
    protected JBPopupMenu opensubmenu = null;
    protected JBPopupMenu parent = null;
    protected boolean dir = true;
    protected boolean focus = false;
    protected int maxwidth = 50;
    protected transient PopupItemSelectedListener aPopupItemSelectedListener = null;
    protected boolean floating = false;
    protected boolean master = false;
    private Panel menupanel = new DoubleBufferPanel();
    private int nitems = 0;
    private int itemheight = 28;
    private MenuGridLayout grid = new MenuGridLayout(0, 1, 0, 0);
    private Hashtable submenuhash = new Hashtable();
    private Component comp = null;
    private boolean istoplevel = false;

    public JBPopupMenu(Frame frame) {
        super(frame);
        this.initialize();
        ((Component) this).setSize(0, 0);
    }

    public JBPopupMenu(Frame frame, Component component, String string) {
        this(frame, component, string, false);
    }

    public JBPopupMenu(Frame frame, Component component, String string, boolean bl) {
        super(frame);
        this.enableEvents(1007L);
        this.istoplevel = bl;
        this.comp = component;
        this.setName(string);
        this.setLayout(new BorderLayout(0, 0));
        this.add((Component) this.menupanel, "Center");
        this.menupanel.setLayout(this.grid);
        ((Component) this).setBackground(JBee.appcolor);
    }

    public JBPopupMenu(Frame frame, Component component, String string, boolean bl, boolean bl2) {
        super(frame);
        this.enableEvents(1007L);
        this.istoplevel = bl;
        this.comp = component;
        this.floating = bl2;
        this.setName(string);
        this.setLayout(new BorderLayout(0, 0));
        this.add((Component) this.menupanel, "Center");
        this.menupanel.setLayout(this.grid);
        ((Component) this).setBackground(JBee.appcolor);
    }

    public static void main(String[] args) {
        try {
            JBPopupMenu jBPopupMenu = new JBPopupMenu(new Frame());
            try {
                Class<?> clazz = Class.forName("com.ibm.uvm.abt.edit.WindowCloser");
                Class[] classArray = new Class[1];
                Class<?> clazz2 = class$java$awt$Window;
                if (clazz2 == null) {
                    try {
                        clazz2 = class$java$awt$Window = Class.forName("java.awt.Window");
                    } catch (ClassNotFoundException classNotFoundException) {
                        throw new NoClassDefFoundError(classNotFoundException.getMessage());
                    }
                }
                classArray[0] = clazz2;
                Class[] classArray2 = classArray;
                Object[] objectArray = new Object[]{jBPopupMenu};
                Constructor<?> constructor = clazz.getConstructor(classArray2);
                constructor.newInstance(objectArray);
            } catch (Throwable throwable) {
            }
            jBPopupMenu.setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Window");
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        this.setVisible(false);
        JBPopupMenu jBPopupMenu = this.parent;
        while (jBPopupMenu != null) {
            jBPopupMenu.setVisible(false);
            jBPopupMenu = jBPopupMenu.parent;
        }
        try {
            PopupItemSelectedEvent popupItemSelectedEvent = new PopupItemSelectedEvent(((FlatButton) actionEvent.getSource()).getUserObject());
            popupItemSelectedEvent.setComponent((Component) actionEvent.getSource());
            this.fireHandlePopupSelection(popupItemSelectedEvent);
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBPopupMenu::actionPerformed: " + throwable);
        }
    }

    public void addLogo(String string) {
        FlatButton flatButton = new FlatButton("");
        flatButton.setInset(new Dimension(0, 0));
        flatButton.setBackground(Color.black);
        flatButton.setImageResource("/images/beemenulogo.gif", 0);
        this.add((Component) flatButton, "West");
        this.addMenuItem("Help", "/images/Question.gif");
        this.addMenuItem("Preferences", "/images/ListView.gif");
        this.addMenuItem("Feedback", "/images/mail.gif");
        this.addMenuItem("Kill all", "/images/die.gif");
        this.addMenuItem("Search and Admin", "/images/earth_small.gif");
        this.addSeparator();
    }

    public void addMenuItem(String string) {
        FlatButton flatButton = new FlatButton(string);
        flatButton.addActionListener(this);
        flatButton.setImageResource("/images/beesmile.gif", 3);
        flatButton.addOnActiveListener(this);
        this.maxwidth = Math.max(this.maxwidth, string.length() * 8);
        this.menupanel.add(flatButton);
        this.addone(0);
    }

    public void addMenuItem(String string, String string2) {
        FlatButton flatButton = new FlatButton(string);
        flatButton.setImageResource(string2, 3);
        flatButton.addActionListener(this);
        flatButton.addOnActiveListener(this);
        this.maxwidth = Math.max(this.maxwidth, string.length() * 8);
        this.menupanel.add(flatButton);
        this.addone(0);
    }

    public void addMenuItem(String string, boolean bl) {
        FlatButton flatButton = new FlatButton(string);
        flatButton.setEnabled(bl);
        flatButton.addActionListener(this);
        flatButton.setImageResource("/images/beesmile.gif", 3);
        flatButton.addOnActiveListener(this);
        this.maxwidth = Math.max(this.maxwidth, string.length() * 8);
        this.menupanel.add(flatButton);
        this.addone(0);
    }

    public void addMenuItemWithObject(String string, Object object) {
        FlatButton flatButton = new FlatButton(string);
        flatButton.setUserObject(object);
        flatButton.setImageResource("/images/beesmile.gif", 3);
        flatButton.addActionListener(this);
        flatButton.addOnActiveListener(this);
        this.maxwidth = Math.max(this.maxwidth, string.length() * 8);
        this.menupanel.add(flatButton);
        this.addone(0);
    }

    public void addMenuItemWithObject(String string, Object object, String string2) {
        FlatButton flatButton = new FlatButton(string);
        flatButton.setUserObject(object);
        flatButton.setImageResource(string2, 3);
        flatButton.addActionListener(this);
        flatButton.addOnActiveListener(this);
        this.maxwidth = Math.max(this.maxwidth, string.length() * 8);
        this.menupanel.add(flatButton);
        this.addone(0);
    }

    private void addone(int n) {
        ++this.nitems;
        this.maxwidth = 170;
        int n2 = Toolkit.getDefaultToolkit().getScreenSize().height;
        int n3 = n2 / this.itemheight;
        if (this.nitems > n3) {
            int n4 = this.nitems / n3 + 1;
            this.grid.setColumns(n4);
            ((Component) this).setSize(new Dimension(n4 * this.maxwidth, this.nitems * this.itemheight / n4));
            this.doLayout();
        } else {
            ((Component) this).setSize(new Dimension(this.maxwidth, this.nitems * this.itemheight));
        }
    }

    public void addPopupItemSelectedListener(PopupItemSelectedListener popupItemSelectedListener) {
        this.aPopupItemSelectedListener = PopupItemSelectedEventMulticaster.add(this.aPopupItemSelectedListener, popupItemSelectedListener);
    }

    public void addPopupMenu(String string, JBPopupMenu jBPopupMenu) throws Exception {
        if (jBPopupMenu == this) {
            throw new Exception("Cannot add this sub to itself");
        }
        jBPopupMenu.setParentMenu(this);
        FlatButton flatButton = new FlatButton(string);
        flatButton.setIsSubmenu(true);
        flatButton.setImageResource("/images/smallfolder.gif", 3);
        flatButton.addOnActiveListener(this);
        jBPopupMenu.setLocationComponent(flatButton);
        this.maxwidth = Math.max(this.maxwidth, string.length() * 8);
        this.menupanel.add(flatButton);
        this.submenuhash.put(flatButton, jBPopupMenu);
        this.addone(0);
    }

    public void addPopupMenu(String string, JBPopupMenu jBPopupMenu, Object object) throws Exception {
        if (jBPopupMenu == this) {
            throw new Exception("Cannot add this sub to itself");
        }
        jBPopupMenu.setParentMenu(this);
        FlatButton flatButton = new FlatButton(string);
        flatButton.setUserObject(object);
        flatButton.setIsSubmenu(true);
        flatButton.setImageResource("/images/smallfolder.gif", 3);
        flatButton.addActionListener(this);
        flatButton.addOnActiveListener(this);
        jBPopupMenu.setLocationComponent(flatButton);
        this.maxwidth = Math.max(this.maxwidth, string.length() * 8);
        this.menupanel.add(flatButton);
        this.submenuhash.put(flatButton, jBPopupMenu);
        this.addone(0);
    }

    public void addSeparator() {
        this.addone(0);
        LWSeparator lWSeparator = new LWSeparator();
        lWSeparator.setDirection(true);
        this.menupanel.add(lWSeparator);
    }

    public void cleanup() {
        ((Container) this).removeNotify();
        Enumeration enumeration = this.submenuhash.elements();
        while (enumeration.hasMoreElements()) {
            Object v = enumeration.nextElement();
            JBPopupMenu jBPopupMenu = (JBPopupMenu) v;
            jBPopupMenu.cleanup();
            this.submenuhash.remove(jBPopupMenu);
            jBPopupMenu = null;
        }
        this.submenuhash.clear();
        this.submenuhash = null;
        this.dispose();
    }

    private void connEtoC1(WindowEvent windowEvent) {
        try {
            this.dispose();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void deleteMenuItem(Object object) {
        try {
            int n = 0;
            while (n < this.menupanel.getComponentCount()) {
                FlatButton flatButton;
                Object object2;
                Component component = this.menupanel.getComponent(n);
                if (component instanceof FlatButton && (object2 = (flatButton = (FlatButton) component).getUserObject()) != null && object2 == object) {
                    this.menupanel.remove(flatButton);
                    JBPopupMenu jBPopupMenu = (JBPopupMenu) this.submenuhash.get(flatButton);
                    if (jBPopupMenu != null) {
                        jBPopupMenu.cleanup();
                        this.submenuhash.remove(flatButton);
                        jBPopupMenu = null;
                    }
                    this.removeone(0);
                    break;
                }
                ++n;
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBPopupMenu::deleteMenuItem: " + throwable.toString());
        }
    }

    public Object detachMenuItem(Object object) {
        Component component = null;
        try {
            Node cfr_ignored_0 = (Node) object;
            int n = 0;
            while (n < this.menupanel.getComponentCount()) {
                FlatButton flatButton;
                Object object2;
                Component component2 = this.menupanel.getComponent(n);
                if (component2 instanceof FlatButton && (object2 = (flatButton = (FlatButton) component2).getUserObject()) != null && object2 == object) {
                    this.menupanel.remove(flatButton);
                    component = flatButton;
                    JBPopupMenu jBPopupMenu = (JBPopupMenu) this.submenuhash.get(flatButton);
                    if (jBPopupMenu != null) {
                        component = jBPopupMenu;
                        this.submenuhash.remove(flatButton);
                    }
                    this.removeone(0);
                    break;
                }
                ++n;
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBPopupMenu::detachMenuItem: " + throwable.toString());
        }
        return component;
    }

    public void finalize() {
    }

    protected void fireHandlePopupSelection(PopupItemSelectedEvent popupItemSelectedEvent) {
        if (this.aPopupItemSelectedListener == null) {
            return;
        }
        this.aPopupItemSelectedListener.handlePopupSelection(popupItemSelectedEvent);
    }

    public int getItemheight() {
        return this.itemheight;
    }

    public void setItemheight(int n) {
        this.itemheight = n;
    }

    public JBPopupMenu getParentMenu() {
        return this.parent;
    }

    public void setParentMenu(JBPopupMenu jBPopupMenu) {
        this.parent = jBPopupMenu;
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.addWindowListener(this);
    }

    private void initialize() {
        this.setName("JBPopupMenu");
        this.setLayout(null);
        ((Component) this).setSize(426, 240);
        this.initConnections();
    }

    public void onActive(OnActiveEvent onActiveEvent) {
        FlatButton flatButton = (FlatButton) onActiveEvent.getSource();
        JBPopupMenu jBPopupMenu = (JBPopupMenu) this.submenuhash.get(flatButton);
        if (jBPopupMenu == null) {
            if (this.opensubmenu != null) {
                this.opensubmenu.setVisible(false);
                this.requestFocus();
            }
        } else {
            if (this.opensubmenu != null && this.opensubmenu != jBPopupMenu) {
                this.opensubmenu.setVisible(false);
                this.requestFocus();
            }
            this.opensubmenu = jBPopupMenu;
            jBPopupMenu.setVisible(true);
        }
    }

    public void processEvent(AWTEvent aWTEvent) {
        block10:
        {
            block9:
            {
                if (JBee.OS_type != 1) break block9;
                switch (aWTEvent.getID()) {
                    case 1004: {
                        JBLogger.log("got focus" + System.getProperty("os.name"));
                        this.focus = true;
                        if (closerThread != null) {
                            JBPopupMenu.closerThread.isFresh = false;
                            break;
                        }
                        break block10;
                    }
                    case 1005: {
                        JBLogger.log("lost focus");
                        this.focus = false;
                        if (closerThread == null) break;
                        JBPopupMenu.closerThread.isFresh = false;
                    }
                }
                break block10;
            }
            switch (aWTEvent.getID()) {
                case 205: {
                    JBLogger.log("win activated");
                    this.focus = true;
                    if (closerThread == null) break;
                    JBPopupMenu.closerThread.isFresh = false;
                    break;
                }
                case 206: {
                    JBLogger.log("win deactivated");
                    this.focus = false;
                    if (closerThread == null) break;
                    JBPopupMenu.closerThread.isFresh = false;
                }
            }
        }
        super.processEvent(aWTEvent);
    }

    private void removeone(int n) {
        --this.nitems;
        this.maxwidth = 170;
        int n2 = Toolkit.getDefaultToolkit().getScreenSize().height;
        int n3 = n2 / this.itemheight;
        int n4 = this.nitems / n3 + 1;
        this.grid.setColumns(n4);
        ((Component) this).setSize(new Dimension(n4 * this.maxwidth, this.nitems * this.itemheight / n4));
        this.doLayout();
    }

    public void removePopupItemSelectedListener(PopupItemSelectedListener popupItemSelectedListener) {
        this.aPopupItemSelectedListener = PopupItemSelectedEventMulticaster.remove(this.aPopupItemSelectedListener, popupItemSelectedListener);
    }

    public void setFocus(boolean bl) {
        this.focus = bl;
    }

    public void setItemText(String string, Object object) {
        try {
            int n = 0;
            while (n < this.menupanel.getComponentCount()) {
                FlatButton flatButton;
                Object object2;
                Component component = this.menupanel.getComponent(n);
                if (component instanceof FlatButton && (object2 = (flatButton = (FlatButton) component).getUserObject()) != null && object2 == object) {
                    flatButton.setLabel(string);
                }
                ++n;
            }
        } catch (Throwable throwable) {
            JBLogger.log("JBPopupMenu::setItemText: " + throwable.toString());
        }
    }

    public void setLocationComponent(Component component) {
        this.comp = component;
    }

    public void setRelativeLocation(Point point) {
        if (this.comp != null) {
            Point point2 = this.comp.getLocationOnScreen();
            ((Component) this).setLocation(new Point(point2.x + point.x, point2.y + point.y));
        } else {
            ((Component) this).setLocation(point);
        }
    }

    public void setText(String string) {
        if (this.comp instanceof FlatButton) {
            ((FlatButton) this.comp).setLabel(string);
        }
    }

    public void setVisible(boolean bl) {
        if (!bl) {
            if (this.opensubmenu != null) {
                this.opensubmenu.setVisible(false);
            }
            super.setVisible(bl);
        } else {
            if (this.parent != null) {
                this.dir = this.parent.dir;
            }
            if (!this.floating) {
                int n = Toolkit.getDefaultToolkit().getScreenSize().width;
                int n2 = Toolkit.getDefaultToolkit().getScreenSize().height;
                int n3 = this.getSize().height;
                int n4 = this.getSize().width;
                int n5 = this.dir ? this.comp.getLocationOnScreen().x + this.comp.getSize().width : this.comp.getLocationOnScreen().x - n4;
                int n6 = this.comp.getLocationOnScreen().y;
                if (n6 + n3 > n2) {
                    n6 -= n6 + n3 - n2;
                }
                if (n5 + n4 > n) {
                    n5 = this.comp.getLocationOnScreen().x - n4;
                    if (this.parent == null) {
                        this.dir = false;
                    }
                } else if (this.parent == null) {
                    this.dir = true;
                }
                ((Component) this).setLocation(n5, n6);
            }
            this.requestFocus();
            super.setVisible(bl);
            if (this.istoplevel) {
                this.startCloser();
            }
        }
    }

    public void startCloser() {
        closerThread = null;
        JBLogger.log("starter");
        closerThread = new MenuCloser(this);
        closerThread.start();
    }

    public void stopCloser() {
        JBLogger.log("stopper");
        if (closerThread != null) {
            JBPopupMenu.closerThread.isRunning = false;
            closerThread = null;
        }
    }

    public void windowActivated(WindowEvent windowEvent) {
    }

    public void windowClosed(WindowEvent windowEvent) {
    }

    public void windowClosing(WindowEvent windowEvent) {
        if (windowEvent.getSource() == this) {
            this.connEtoC1(windowEvent);
        }
    }

    public void windowDeactivated(WindowEvent windowEvent) {
    }

    public void windowDeiconified(WindowEvent windowEvent) {
    }

    public void windowIconified(WindowEvent windowEvent) {
    }

    public void windowOpened(WindowEvent windowEvent) {
    }
}

