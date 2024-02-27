/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import com.objectbox.runner.gui.JBee;
import com.objectbox.runner.util.JBLogger;
import com.sun.java.swing.GrayFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;

public class FlatButton
        extends Component
        implements DelayedInvocationCallBack {
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;
    public static final int CENTER = 4;
    private static final Font font = new Font("SansSerif", 1, 12);
    private static final Color skin = new Color(255, 226, 198);
    public boolean rightButtonPush = false;
    protected transient ActionListener aActionListener = null;
    protected transient OnActiveListener aOnActiveListener = null;
    protected transient PropertyChangeSupport propertyChange;
    protected boolean usefixedsize = false;
    boolean toggle = false;
    boolean pressed = false;
    boolean hasfocus = false;
    private String label = "Button";
    private Image img = null;
    private Image disabledimg = null;
    private Image originalimg = null;
    private Image submenuicon = null;
    private int imagepos = 0;
    private Object fieldUserObject = new Object();
    private Dimension fieldFixedsize = new Dimension();
    private long now = -1L;
    private boolean shallfire = true;
    private Dimension inset = new Dimension(2, 6);
    private boolean fieldHasborder = false;
    private boolean submenu = false;

    public FlatButton() {
        this("");
    }

    public FlatButton(String string) {
        this.enableEvents(52L);
        this.label = string;
    }

    public void addActionListener(ActionListener actionListener) {
        this.aActionListener = AWTEventMulticaster.add(this.aActionListener, actionListener);
    }

    public void addNotify() {
        super.addNotify();
        Dimension dimension = this.getSize();
        if (dimension.width == 0 || dimension.height == 0) {
            this.setSize(this.getPreferredSize());
        }
    }

    public void addOnActiveListener(OnActiveListener onActiveListener) {
        this.aOnActiveListener = OnActiveEventMulticaster.add(this.aOnActiveListener, onActiveListener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().addPropertyChangeListener(propertyChangeListener);
    }

    public boolean contains(int n, int n2) {
        Rectangle rectangle = new Rectangle(this.getSize().width, this.getSize().height);
        return rectangle.contains(n, n2);
    }

    public void delayedInvoke() {
        if (this.isVisible()) {
            this.fireOnActive(new OnActiveEvent(this));
        }
    }

    public void finalize() {
        if (this.img != null) {
            this.img.flush();
        }
        if (this.disabledimg != null) {
            this.disabledimg.flush();
        }
        if (this.originalimg != null) {
            this.originalimg.flush();
        }
        if (this.submenuicon != null) {
            this.submenuicon.flush();
        }
    }

    protected void fireActionPerformed(ActionEvent actionEvent) {
        if (this.aActionListener == null) {
            return;
        }
        this.aActionListener.actionPerformed(actionEvent);
    }

    protected void fireOnActive(OnActiveEvent onActiveEvent) {
        if (this.aOnActiveListener == null) {
            return;
        }
        this.aOnActiveListener.onActive(onActiveEvent);
    }

    public void firePropertyChange(String string, Object object, Object object2) {
        this.getPropertyChange().firePropertyChange(string, object, object2);
    }

    public Dimension getFixedsize() {
        return this.fieldFixedsize;
    }

    public void setFixedsize(Dimension dimension) {
        Dimension dimension2 = this.fieldFixedsize;
        this.fieldFixedsize = dimension;
        this.usefixedsize = true;
        this.firePropertyChange("fixedsize", dimension2, dimension);
        this.repaint();
    }

    public boolean getHasborder() {
        return this.fieldHasborder;
    }

    public void setHasborder(boolean bl) {
        boolean bl2 = this.fieldHasborder;
        this.fieldHasborder = bl;
        this.firePropertyChange("hasborder", new Boolean(bl2), new Boolean(bl));
        this.repaint();
    }

    public Image getImage() {
        return this.img;
    }

    public void setImage(Image image) {
        this.originalimg = image;
        this.disabledimg = GrayFilter.createDisabledImage(image);
        this.img = this.isEnabled() ? this.originalimg : this.disabledimg;
        this.imagepos = 0;
    }

    public Dimension getInset() {
        return this.inset;
    }

    public void setInset(Dimension dimension) {
        this.inset = dimension;
    }

    public boolean getIsSubmenu() {
        return this.submenu;
    }

    public void setIsSubmenu(boolean bl) {
        this.submenu = bl;
        this.submenuicon = this.loadImageFromResource("/images/TreeCollapsed.gif");
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String string) {
        this.label = string;
        this.repaint();
    }

    public Dimension getMaximumSize() {
        return this.getTheSize();
    }

    public Dimension getMinimumSize() {
        return this.getTheSize();
    }

    public Dimension getPreferredSize() {
        return this.getTheSize();
    }

    protected PropertyChangeSupport getPropertyChange() {
        if (this.propertyChange == null) {
            this.propertyChange = new PropertyChangeSupport(this);
        }
        return this.propertyChange;
    }

    public Dimension getTheSize() {
        Font font = this.getFont();
        if (this.usefixedsize) {
            return this.fieldFixedsize;
        }
        if (font != null) {
            FontMetrics fontMetrics = this.getFontMetrics(font);
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            if (this.img != null) {
                n3 = this.img.getHeight(this);
                n4 = this.img.getWidth(this);
                switch (this.imagepos) {
                    case 0: {
                        n = n3 + fontMetrics.getHeight();
                        n2 = Math.max(n4, fontMetrics.stringWidth(this.label));
                        break;
                    }
                    case 1: {
                        n = n3 + fontMetrics.getHeight();
                        n2 = Math.max(n4, fontMetrics.stringWidth(this.label));
                        break;
                    }
                    case 2: {
                        n = Math.max(n3, fontMetrics.getHeight());
                        n2 = n4 + fontMetrics.stringWidth(this.label);
                        break;
                    }
                    case 3: {
                        n = Math.max(n3, fontMetrics.getHeight());
                        n2 = n4 + fontMetrics.stringWidth(this.label);
                        break;
                    }
                    case 4: {
                        n = Math.max(n3, fontMetrics.getHeight());
                        n2 = n4 + fontMetrics.stringWidth(this.label);
                    }
                }
            } else {
                return new Dimension(fontMetrics.stringWidth(this.label) + this.inset.width + this.inset.width, fontMetrics.getHeight() + this.inset.height + this.inset.height);
            }
            return new Dimension(n2 + this.inset.width + this.inset.width, n + this.inset.height + this.inset.height);
        }
        return this.getMinimumSize();
    }

    public Object getUserObject() {
        return this.fieldUserObject;
    }

    public void setUserObject(Object object) {
        Object object2 = this.fieldUserObject;
        this.fieldUserObject = object;
        this.firePropertyChange("userObject", object2, object);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Image loadImageFromResource(String string) {
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

    public void paint(Graphics graphics) {
        Color color;
        graphics.setFont(font);
        if (this.getSize().width <= 1 || this.getSize().height <= 1) {
            this.setSize(this.getPreferredSize());
        }
        int n = 0;
        int n2 = this.getSize().width - 0;
        int n3 = this.getSize().height - 0;
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int n4 = fontMetrics.stringWidth(this.label);
        int n5 = fontMetrics.getHeight();
        fontMetrics.getAscent();
        int n6 = fontMetrics.getDescent();
        Color color2 = color = this.toggle && this.isEnabled() ? skin : this.getBackground().darker().darker();
        if (this.pressed && this.isEnabled()) {
            graphics.setColor(this.getBackground());
            graphics.fill3DRect(0, 0, n2, n3, false);
            n = 1;
        } else if (this.toggle && this.isEnabled()) {
            graphics.setColor(this.getBackground());
            graphics.fill3DRect(0, 0, n2, n3, true);
            n = -1;
        } else {
            graphics.setColor(this.getBackground());
            graphics.fillRect(0, 0, n2, n3);
        }
        if (this.img != null) {
            int n7 = this.img.getHeight(this);
            int n8 = this.img.getWidth(this);
            graphics.setColor(color);
            switch (this.imagepos) {
                case 0: {
                    graphics.drawImage(this.img, n2 / 2 - n8 / 2 + n, this.inset.height + n, this);
                    if (!this.isEnabled()) {
                        Color color3 = graphics.getColor();
                        graphics.setColor(Color.white);
                        graphics.drawString(this.label, n2 / 2 - n4 / 2 + n + 1, n3 - this.inset.height - n6 + n + 1);
                        graphics.setColor(color3);
                    }
                    graphics.drawString(this.label, n2 / 2 - n4 / 2 + n, n3 - this.inset.height - n6 + n);
                    break;
                }
                case 1: {
                    graphics.drawImage(this.img, n2 / 2 - n8 / 2, n3 - this.inset.height - n7 - 4, this);
                    if (!this.isEnabled()) {
                        Color color4 = graphics.getColor();
                        graphics.setColor(Color.white);
                        graphics.drawString(this.label, n2 / 2 - n4 / 2 + n + 1, n3 - this.inset.height - n6 + n + 1);
                        graphics.setColor(color4);
                    }
                    graphics.drawString(this.label, n2 / 2 - n4 / 2 + n, n5 + n6 + n);
                    break;
                }
                case 2: {
                    graphics.drawImage(this.img, n2 - this.inset.width - n8 - 4 + 2 + n, n3 / 2 - n7 / 2 + n, this);
                    if (!this.isEnabled()) {
                        Color color5 = graphics.getColor();
                        graphics.setColor(Color.white);
                        graphics.drawString(this.label, n2 / 2 - n4 / 2 + n + 1, n3 - this.inset.height - n6 + n + 1);
                        graphics.setColor(color5);
                    }
                    graphics.drawString(this.label, this.inset.width + n, n3 / 2 + n5 / 2 - n6 + n);
                    break;
                }
                case 3: {
                    graphics.drawImage(this.img, this.inset.width + n, n3 / 2 - n7 / 2 + n, this);
                    if (!this.isEnabled()) {
                        Color color6 = graphics.getColor();
                        graphics.setColor(Color.white);
                        graphics.drawString(this.label, n2 / 2 - n4 / 2 + n + 1, n3 - this.inset.height - n6 + n + 1);
                        graphics.setColor(color6);
                    }
                    graphics.drawString(this.label, n8 + this.inset.width + 4 + n, n3 / 2 + n5 / 2 - n6 + n);
                    break;
                }
                case 4: {
                    graphics.drawImage(this.img, n2 / 2 - n8 / 2 + n, n3 / 2 - n7 / 2 + n, this);
                    if (!this.isEnabled()) {
                        Color color7 = graphics.getColor();
                        graphics.setColor(Color.white);
                        graphics.drawString(this.label, n2 / 2 - n4 / 2 + n + 1, n3 - this.inset.height - n6 + n + 1);
                        graphics.setColor(color7);
                    }
                    graphics.drawString(this.label, n2 - this.inset.width - n4 - 4 + n, n3 / 2 + n5 / 2 - n6 + n);
                }
            }
        } else {
            if (!this.isEnabled()) {
                graphics.setColor(Color.white);
                graphics.drawString(this.label, n2 / 2 - n4 / 2 + n + 1, n3 / 2 + n5 / 2 - n6 + n + 1);
            }
            graphics.setColor(color);
            graphics.drawString(this.label, n2 / 2 - n4 / 2 + n, n3 / 2 + n5 / 2 - n6 + n);
        }
        if (this.fieldHasborder) {
            graphics.setColor(this.getBackground().darker());
            graphics.drawRect(0, 0, n2 - 2, n3 - 2);
            graphics.setColor(this.getBackground().brighter());
            graphics.drawRect(1, 1, n2 - 1, n3 - 1);
        }
        if (this.submenu && this.submenuicon != null) {
            graphics.setColor(this.getBackground().darker());
            graphics.drawImage(this.submenuicon, n2 - this.submenuicon.getWidth(this) - this.inset.width, n3 / 2 - this.submenuicon.getHeight(this) / 2, this);
        }
    }

    public void processFocusEvent(FocusEvent focusEvent) {
        try {
            block11:
            {
                block10:
                {
                    if (JBee.OS_type != 1) break block10;
                    switch (focusEvent.getID()) {
                        case 1004: {
                            Container container = this.getParent().getParent();
                            if (container instanceof JBPopupMenu) {
                                ((JBPopupMenu) container).processEvent(focusEvent);
                                break;
                            }
                            break block11;
                        }
                        case 1005: {
                            Container container = this.getParent().getParent();
                            if (!(container instanceof JBPopupMenu)) break;
                            ((JBPopupMenu) container).processEvent(focusEvent);
                        }
                    }
                    break block11;
                }
                switch (focusEvent.getID()) {
                    case 1005: {
                        Container container = this.getParent().getParent();
                        if (!(container instanceof JBee)) break;
                        ((JBee) container).closeMenu();
                    }
                }
            }
            super.processFocusEvent(focusEvent);
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
        }
    }

    public void processMouseEvent(MouseEvent mouseEvent) {
        if (!this.isEnabled()) {
            return;
        }
        switch (mouseEvent.getID()) {
            case 501: {
                this.rightButtonPush = (mouseEvent.getModifiers() & 4) != 0;
                this.pressed = true;
                this.toggle = false;
                this.requestFocus();
                this.repaint();
                break;
            }
            case 502: {
                if (this.contains(mouseEvent.getX(), mouseEvent.getY())) {
                    this.fireActionPerformed(new ActionEvent(this, 1001, this.label));
                }
                if (!this.pressed) break;
                this.pressed = false;
                this.toggle = true;
                this.repaint();
                break;
            }
            case 504: {
                this.shallfire = true;
                this.now = System.currentTimeMillis();
                this.toggle = true;
                this.repaint();
                DelayedInvocation delayedInvocation = DelayedInvocation.getInstance();
                delayedInvocation.setCallback(this, 500L);
                break;
            }
            case 505: {
                this.now = -1L;
                if (this.pressed) {
                    this.pressed = false;
                    this.repaint();
                    break;
                }
                this.toggle = false;
                this.repaint();
                DelayedInvocation delayedInvocation = DelayedInvocation.getInstance();
                delayedInvocation.setCallback(null, 10000L);
            }
        }
        super.processMouseEvent(mouseEvent);
    }

    public void processMouseMotionEvent(MouseEvent mouseEvent) {
        switch (mouseEvent.getID()) {
            default:
        }
        super.processMouseEvent(mouseEvent);
    }

    public void removeActionListener(ActionListener actionListener) {
        this.aActionListener = AWTEventMulticaster.remove(this.aActionListener, actionListener);
    }

    public void removeOnActiveListener(OnActiveListener onActiveListener) {
        this.aOnActiveListener = OnActiveEventMulticaster.remove(this.aOnActiveListener, onActiveListener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().removePropertyChangeListener(propertyChangeListener);
    }

    public void setEnabled(boolean bl) {
        this.img = !bl ? this.disabledimg : this.originalimg;
        if (bl != this.isEnabled()) {
            this.toggle = false;
            super.setEnabled(bl);
            this.repaint();
        }
    }

    public void setImage(Image image, int n) {
        this.originalimg = image;
        this.disabledimg = GrayFilter.createDisabledImage(image);
        this.img = this.isEnabled() ? this.originalimg : this.disabledimg;
        this.imagepos = n;
    }

    public void setImagePos(int n) {
        this.imagepos = n;
    }

    public boolean setImageResource(String string, int n) {
        this.setImage(this.loadImageFromResource(string), n);
        return true;
    }

    public void update(Graphics graphics) {
        this.paint(graphics);
    }
}

