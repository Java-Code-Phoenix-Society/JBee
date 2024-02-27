/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.JBPopupMenu;
import com.objectbox.runner.util.JBLogger;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Constructor;

public class AppletFrame
        extends Frame
        implements IAppletWindow,
        WindowListener {
    public static int statusheight = 55;
    static Class class$java$awt$Window;
    private Panel ivjContentsPane = null;
    private Label ivjStatusLabel = null;
    private Panel ivjToolbar = null;
    private JBPopupMenu popup = null;
    private boolean done = false;
    private Applet applettorun = null;
    private String frametitle = "";

    public AppletFrame() {
        this.initialize();
    }

    public AppletFrame(String string) {
        super(string);
        this.frametitle = string;
        this.initialize();
    }

    public static void main(String[] stringArray) {
        try {
            AppletFrame appletFrame = new AppletFrame();
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
                Object[] objectArray = new Object[]{appletFrame};
                Constructor<?> constructor = clazz.getConstructor(classArray2);
                constructor.newInstance(objectArray);
            } catch (Throwable throwable) {
            }
            ((Component) appletFrame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Frame");
        }
    }

    public void addApplet(Applet applet) {
        this.applettorun = applet;
        ((Component) this).setSize(this.applettorun.getSize().width + 4, this.applettorun.getSize().height + statusheight);
        this.getContentsPane().add((Component) this.applettorun, "Center");
    }

    private void connEtoC1(WindowEvent windowEvent) {
        try {
            this.dispose();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    protected void finalize() {
        try {
            super.finalize();
        } catch (Throwable throwable) {
            JBLogger.log("Exception in finalizing AppletFrame " + throwable.toString());
        }
    }

    private Panel getContentsPane() {
        if (this.ivjContentsPane == null) {
            try {
                this.ivjContentsPane = new Panel();
                this.ivjContentsPane.setName("ContentsPane");
                this.ivjContentsPane.setLayout(new BorderLayout());
                this.getContentsPane().add((Component) this.getToolbar(), "South");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjContentsPane;
    }

    private Label getStatusLabel() {
        if (this.ivjStatusLabel == null) {
            try {
                this.ivjStatusLabel = new Label();
                this.ivjStatusLabel.setName("StatusLabel");
                this.ivjStatusLabel.setText("Status");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjStatusLabel;
    }

    private Panel getToolbar() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        if (this.ivjToolbar == null) {
            try {
                this.ivjToolbar = new Panel();
                this.ivjToolbar.setName("Toolbar");
                this.ivjToolbar.setLayout(new GridBagLayout());
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.fill = 2;
                gridBagConstraints.anchor = 17;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 0.0;
                this.getToolbar().add((Component) this.getStatusLabel(), gridBagConstraints);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjToolbar;
    }

    private void handleException(Throwable throwable) {
        JBLogger.log("--------- UNCAUGHT EXCEPTION ---------");
        throwable.printStackTrace(System.out);
    }

    private void initConnections() {
        this.addWindowListener(this);
    }

    private void initialize() {
        this.setName("AppletFrame");
        this.setLayout(new BorderLayout());
        ((Component) this).setBackground(new Color(182, 182, 200));
        ((Component) this).setSize(332, 280);
        ((Component) this).setVisible(false);
        this.setTitle("AppletFrame");
        this.add((Component) this.getContentsPane(), "Center");
        this.initConnections();
        this.setIconImage(JBee.getIcon());
        ((Component) this).setBackground(JBee.appcolor);
        this.setTitle(this.frametitle);
    }

    public boolean isDone() {
        return this.done;
    }

    public void kill() {
        try {
            if (this.applettorun != null) {
                this.applettorun.stop();
                this.applettorun.destroy();
                this.remove(this.applettorun);
                this.applettorun = null;
            }
            System.gc();
            this.done = true;
            JBLogger.log("Appletframe Stopping applet");
        } catch (Exception exception) {
            JBLogger.log("Appletframe.kill " + exception.toString());
        }
    }

    public void setStatus(String string) {
        this.getStatusLabel().setText(string);
    }

    public void windowActivated(WindowEvent windowEvent) {
    }

    public void windowClosed(WindowEvent windowEvent) {
    }

    public void windowClosing(WindowEvent windowEvent) {
        this.kill();
        this.done = true;
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

