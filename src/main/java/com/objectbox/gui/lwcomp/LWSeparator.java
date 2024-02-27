/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class LWSeparator
        extends Component {
    protected transient PropertyChangeSupport propertyChange;
    private boolean fieldDirection = true;
    private Dimension fieldPrefSize = null;

    public LWSeparator() {
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
            serializable = new LWSeparator();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            System.err.println("Exception occurred in main() of com.objectbox.gui.lwcomp.LWSeparator");
            throwable.printStackTrace(System.out);
        }
    }

    public void addNotify() {
        super.addNotify();
        Dimension dimension = this.getSize();
        if (dimension.width == 0 || dimension.height == 0) {
            this.setSize(this.getPreferredSize());
        }
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().addPropertyChangeListener(propertyChangeListener);
    }

    public void firePropertyChange(String string, Object object, Object object2) {
        this.getPropertyChange().firePropertyChange(string, object, object2);
    }

    public boolean getDirection() {
        return this.fieldDirection;
    }

    public void setDirection(boolean bl) {
        boolean bl2 = this.fieldDirection;
        this.fieldDirection = bl;
        this.firePropertyChange("direction", new Boolean(bl2), new Boolean(bl));
        this.repaint();
    }

    public Dimension getMaximumSize() {
        if (this.getPrefSize() != null) {
            return this.getPrefSize();
        }
        if (this.fieldDirection) {
            return new Dimension(this.getSize().width, 10);
        }
        return new Dimension(10, this.getSize().height);
    }

    public Dimension getMinimumSize() {
        if (this.getPrefSize() != null) {
            return this.getPrefSize();
        }
        if (this.fieldDirection) {
            return new Dimension(this.getSize().width, 10);
        }
        return new Dimension(10, this.getSize().height);
    }

    public Dimension getPreferredSize() {
        if (this.getPrefSize() != null) {
            return this.getPrefSize();
        }
        if (this.fieldDirection) {
            return new Dimension(this.getSize().width, 10);
        }
        return new Dimension(10, this.getSize().height);
    }

    public Dimension getPrefSize() {
        return this.fieldPrefSize;
    }

    public void setPrefSize(Dimension dimension) {
        Dimension dimension2 = this.fieldPrefSize;
        this.fieldPrefSize = dimension;
        this.firePropertyChange("prefSize", dimension2, dimension);
    }

    protected PropertyChangeSupport getPropertyChange() {
        if (this.propertyChange == null) {
            this.propertyChange = new PropertyChangeSupport(this);
        }
        return this.propertyChange;
    }

    private void handleException(Throwable throwable) {
    }

    private void initialize() {
        this.setName("LWSeparator");
        this.setSize(150, 143);
        this.setDirection(false);
    }

    public void paint(Graphics graphics) {
        int n = this.getSize().width;
        int n2 = this.getSize().height;
        graphics.setColor(this.getBackground().darker());
        if (this.fieldDirection) {
            graphics.drawLine(0, n2 / 2, n, n2 / 2);
            graphics.setColor(this.getBackground().brighter());
            graphics.drawLine(0, n2 / 2 + 1, n, n2 / 2 + 1);
        } else {
            graphics.drawLine(n / 2, 0, n / 2, n2);
            graphics.setColor(this.getBackground().brighter());
            graphics.drawLine(n / 2 + 1, 0, n / 2 + 1, n2);
        }
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().removePropertyChangeListener(propertyChangeListener);
    }
}

