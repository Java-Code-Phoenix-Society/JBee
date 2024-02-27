/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.image;

import com.objectbox.runner.image.ImageEvent;
import com.objectbox.runner.image.ImageListener;
import com.objectbox.runner.image.SerializableImage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageFilter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ImageComponent
        extends Canvas
        implements ImageListener {
    private boolean scalable;
    private Image image;
    private transient ImageFilter filter;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public ImageComponent() {
        this.setScalable(false);
    }

    public ImageComponent(SerializableImage serializableImage) {
        this(serializableImage, false);
    }

    public ImageComponent(SerializableImage serializableImage, boolean bl) {
        this.setImage(serializableImage);
        this.setScalable(bl);
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.changes.addPropertyChangeListener(propertyChangeListener);
    }

    public ImageFilter getFilter() {
        return this.filter;
    }

    public void setFilter(ImageFilter imageFilter) {
        this.filter = imageFilter;
    }

    public Image getImage() {
        return this.image;
    }

    public synchronized void setImage(Image image) {
        Image image2 = this.getImage();
        this.image = image;
        this.changes.firePropertyChange("image", image2, this.getImage());
    }

    public Dimension getMinimumSize() {
        if (this.image != null) {
            return new Dimension(this.image.getWidth(this), this.image.getHeight(this));
        }
        return new Dimension(75, 35);
    }

    public Dimension getPreferredSize() {
        return this.getMinimumSize();
    }

    public void imageUpdated(ImageEvent imageEvent) {
        if (imageEvent.getImage() != null) {
            this.setImage(imageEvent.getImage());
            this.repaint();
        }
    }

    public boolean isScalable() {
        return this.scalable;
    }

    public synchronized void setScalable(boolean bl) {
        Boolean bl2 = new Boolean(this.isScalable());
        this.scalable = bl;
        this.changes.firePropertyChange("scalable", bl2, new Boolean(this.isScalable()));
    }

    public void paint(Graphics graphics) {
        int n = this.getSize().width;
        int n2 = this.getSize().height;
        if (this.image != null) {
            if (this.scalable) {
                graphics.drawImage(this.image, 0, 0, n, n2, this);
            } else {
                graphics.drawImage(this.image, 0, 0, this);
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            this.filter = (ImageFilter) objectInputStream.readObject();
        } catch (Exception exception) {
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.changes.removePropertyChangeListener(propertyChangeListener);
    }

    public void setBackground(Color color) {
        Color color2 = this.getBackground();
        super.setBackground(color);
        this.changes.firePropertyChange("background", color2, this.getBackground());
    }

    public void setEnabled(boolean bl) {
        Boolean bl2 = new Boolean(this.isEnabled());
        super.setEnabled(bl);
        this.changes.firePropertyChange("enabled", bl2, new Boolean(this.isEnabled()));
    }

    public void setVisible(boolean bl) {
        Boolean bl2 = new Boolean(this.isVisible());
        super.setVisible(bl);
        this.changes.firePropertyChange("visible", bl2, new Boolean(this.isVisible()));
    }

    public void update(Graphics graphics) {
        this.paint(graphics);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (this.filter instanceof Serializable) {
            objectOutputStream.writeObject(this.filter);
        }
    }
}

