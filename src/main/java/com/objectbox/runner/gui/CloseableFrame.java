/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class CloseableFrame
        extends Frame
        implements WindowListener {
    private boolean done = true;

    public CloseableFrame() {
        this.addWindowListener(this);
        this.done = false;
    }

    public CloseableFrame(String string) {
        super(string);
        this.addWindowListener(this);
        this.done = false;
    }

    public boolean isDone() {
        return this.done;
    }

    public void windowActivated(WindowEvent windowEvent) {
    }

    public void windowClosed(WindowEvent windowEvent) {
        this.done = true;
    }

    public void windowClosing(WindowEvent windowEvent) {
        int n = 0;
        while (n < this.getComponents().length) {
            if (this.getComponent(n) instanceof Applet) {
                ((Applet) this.getComponent(n)).stop();
                ((Applet) this.getComponent(n)).destroy();
            }
            this.remove(n);
            ++n;
        }
        this.dispose();
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

