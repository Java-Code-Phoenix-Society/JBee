/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.runner.model.JBProperties;
import com.objectbox.runner.model.JBPropertyEditor;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AppletPropertiesEditorFrame
        extends Frame
        implements WindowListener,
        JBPropertyEditor {
    protected AppletPropertiesEditor ape = null;
    private Panel ivjContentsPane = null;

    public AppletPropertiesEditorFrame() {
        this.initialize();
    }

    public AppletPropertiesEditorFrame(String string) {
        super(string);
    }

    public static void main(String[] stringArray) {
    }

    private void connEtoC1(WindowEvent windowEvent) {
        try {
            this.dispose();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void edit(JBProperties jBProperties) {
        this.ape.edit(jBProperties);
        if (!this.isVisible()) {
            ((Component) this).setVisible(true);
        }
        this.repaint();
    }

    private Panel getContentsPane() {
        if (this.ivjContentsPane == null) {
            try {
                this.ivjContentsPane = new Panel();
                this.ivjContentsPane.setName("ContentsPane");
                this.ivjContentsPane.setLayout(new BorderLayout());
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjContentsPane;
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.addWindowListener(this);
    }

    private void initialize() {
        this.setIconImage(JBee.getIcon());
        this.setName("TestEditorFrame");
        this.setLayout(new BorderLayout());
        ((Component) this).setSize(350, 400);
        this.setResizable(true);
        this.add((Component) this.getContentsPane(), "Center");
        this.initConnections();
        this.setIconImage(JBee.getIcon());
        this.setTitle("Properties");
        ((Component) this).setSize(500, 400);
        this.ape = new AppletPropertiesEditor();
        this.getContentsPane().add((Component) this.ape, "Center");
        int n = Toolkit.getDefaultToolkit().getScreenSize().height;
        int n2 = Toolkit.getDefaultToolkit().getScreenSize().width;
        ((Component) this).setLocation(n2 / 2 - this.getSize().width / 2, n / 2 - this.getSize().height / 2);
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

