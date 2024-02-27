/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.runner.image.ImageComponent;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class JBImageSpinner
        extends Panel {
    protected transient PropertyChangeSupport propertyChange;
    private ImageComponent ivjImageComponentAnimation = null;
    private ImageComponent ivjImageComponentStillLogo = null;
    private Image fieldAnimatedImage = null;
    private Image fieldStillImage = null;
    private CardLayout ivjCardLayout = null;

    public JBImageSpinner() {
        this.initialize();
    }

    public JBImageSpinner(LayoutManager layoutManager) {
        super(layoutManager);
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
            serializable = new JBImageSpinner();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            System.err.println("Exception occurred in main() of java.awt.Panel");
            throwable.printStackTrace(System.out);
        }
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().addPropertyChangeListener(propertyChangeListener);
    }

    private void connPtoP1SetTarget() {
        try {
            this.setLayout(this.getCardLayout());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void firePropertyChange(String string, Object object, Object object2) {
        this.getPropertyChange().firePropertyChange(string, object, object2);
    }

    private CardLayout getCardLayout() {
        if (this.ivjCardLayout == null) {
            try {
                this.ivjCardLayout = new CardLayout();
                this.ivjCardLayout.addLayoutComponent(this.getImageComponentAnimation(), "ANI");
                this.ivjCardLayout.addLayoutComponent(this.getImageComponentStillLogo(), "STILL");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCardLayout;
    }

    private ImageComponent getImageComponentAnimation() {
        if (this.ivjImageComponentAnimation == null) {
            try {
                this.ivjImageComponentAnimation = new ImageComponent();
                this.ivjImageComponentAnimation.setName("ImageComponentAnimation");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjImageComponentAnimation;
    }

    private ImageComponent getImageComponentStillLogo() {
        if (this.ivjImageComponentStillLogo == null) {
            try {
                this.ivjImageComponentStillLogo = new ImageComponent();
                this.ivjImageComponentStillLogo.setName("ImageComponentStillLogo");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjImageComponentStillLogo;
    }

    protected PropertyChangeSupport getPropertyChange() {
        if (this.propertyChange == null) {
            this.propertyChange = new PropertyChangeSupport(this);
        }
        return this.propertyChange;
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.connPtoP1SetTarget();
    }

    private void initialize() {
        this.setName("JBImageSpinner");
        this.setLayout(new CardLayout());
        this.setSize(70, 49);
        this.add((Component) this.getImageComponentStillLogo(), this.getImageComponentStillLogo().getName());
        this.add((Component) this.getImageComponentAnimation(), this.getImageComponentAnimation().getName());
        this.initConnections();
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().removePropertyChangeListener(propertyChangeListener);
    }

    public void setAnimatedImage(Image image) {
        Image image2 = this.fieldAnimatedImage;
        this.fieldAnimatedImage = image;
        this.ivjImageComponentAnimation.setImage(image);
        this.firePropertyChange("animatedImage", image2, image);
    }

    public void setStillImage(Image image) {
        Image image2 = this.fieldStillImage;
        this.fieldStillImage = image;
        this.ivjImageComponentStillLogo.setImage(image);
        this.firePropertyChange("stillImage", image2, image);
    }

    public void toggle(boolean bl) {
        if (bl) {
            this.getCardLayout().show(this, "ANI");
        } else {
            this.getCardLayout().show(this, "STILL");
        }
    }
}

