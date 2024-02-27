/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.FlatButton;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Properties;

public class SetupFrame
        extends Frame
        implements ActionListener,
        WindowListener {
    static Class class$java$awt$Window;
    private Panel ivjContentsPane = null;
    private SetupPanel ivjSetupPanel1 = null;
    private FlatButton ivjFlatButtonCancel = null;
    private FlatButton ivjFlatButtonSave = null;
    private Panel ivjPanelButtons = null;
    private Frame owner = null;
    private Properties props = null;

    public SetupFrame() {
        this.initialize();
    }

    public SetupFrame(Frame frame, Properties properties) {
        this.owner = frame;
        this.props = properties;
        this.initialize();
    }

    public static void main(String[] args) {
        try {
            SetupFrame setupFrame = new SetupFrame(new Frame(), null);
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
                Object[] objectArray = new Object[]{setupFrame};
                Constructor<?> constructor = clazz.getConstructor(classArray2);
                constructor.newInstance(objectArray);
            } catch (Throwable throwable) {
            }
            ((Component) setupFrame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Frame");
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.getFlatButtonCancel()) {
            this.connEtoM1(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonSave()) {
            this.connEtoC2(actionEvent);
        }
    }

    private void connEtoC1(WindowEvent windowEvent) {
        try {
            this.dispose();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC2(ActionEvent actionEvent) {
        try {
            this.flatButtonSave_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoM1(ActionEvent actionEvent) {
        try {
            this.dispose();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void flatButtonSave_ActionPerformed(ActionEvent actionEvent) {
        this.getPanel().setProperties();
        JBee cfr_ignored_0 = (JBee) this.owner;
        JBee.preferences = this.getPanel().getProperties();
        JBee cfr_ignored_1 = (JBee) this.owner;
        if (JBee.preferences.getProperty("useproxy").equals("true")) {
            ((Hashtable) System.getProperties()).put("proxySet", "true");
            JBee cfr_ignored_2 = (JBee) this.owner;
            ((Hashtable) System.getProperties()).put("proxyHost", JBee.preferences.getProperty("proxyserver"));
            JBee cfr_ignored_3 = (JBee) this.owner;
            ((Hashtable) System.getProperties()).put("proxyPort", JBee.preferences.getProperty("proxyport"));
        } else {
            ((Hashtable) System.getProperties()).put("proxySet", "false");
        }
        ((Component) this).setVisible(false);
        this.dispose();
    }

    private Panel getContentsPane() {
        if (this.ivjContentsPane == null) {
            try {
                this.ivjContentsPane = new Panel();
                this.ivjContentsPane.setName("ContentsPane");
                this.ivjContentsPane.setLayout(new BorderLayout());
                this.getContentsPane().add((Component) this.getSetupPanel1(), "Center");
                this.getContentsPane().add((Component) this.getPanelButtons(), "South");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjContentsPane;
    }

    private FlatButton getFlatButtonCancel() {
        if (this.ivjFlatButtonCancel == null) {
            try {
                this.ivjFlatButtonCancel = new FlatButton();
                this.ivjFlatButtonCancel.setName("FlatButtonCancel");
                this.ivjFlatButtonCancel.setFixedsize(new Dimension(75, 30));
                this.ivjFlatButtonCancel.setLabel("Cancel");
                this.ivjFlatButtonCancel.setImageResource("/images/cancel.gif", 3);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonCancel;
    }

    private FlatButton getFlatButtonSave() {
        if (this.ivjFlatButtonSave == null) {
            try {
                this.ivjFlatButtonSave = new FlatButton();
                this.ivjFlatButtonSave.setName("FlatButtonSave");
                this.ivjFlatButtonSave.setFixedsize(new Dimension(75, 30));
                this.ivjFlatButtonSave.setLabel("OK");
                this.ivjFlatButtonSave.setImageResource("/images/ok.gif", 3);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonSave;
    }

    public SetupPanel getPanel() {
        return this.getSetupPanel1();
    }

    private Panel getPanelButtons() {
        if (this.ivjPanelButtons == null) {
            try {
                this.ivjPanelButtons = new Panel();
                this.ivjPanelButtons.setName("PanelButtons");
                this.ivjPanelButtons.setLayout(new FlowLayout());
                this.ivjPanelButtons.setBackground(SystemColor.control);
                this.getPanelButtons().add((Component) this.getFlatButtonSave(), this.getFlatButtonSave().getName());
                this.getPanelButtons().add((Component) this.getFlatButtonCancel(), this.getFlatButtonCancel().getName());
                this.getPanelButtons().setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelButtons;
    }

    private SetupPanel getSetupPanel1() {
        if (this.ivjSetupPanel1 == null) {
            try {
                this.ivjSetupPanel1 = new SetupPanel();
                this.ivjSetupPanel1.setName("SetupPanel1");
                this.ivjSetupPanel1.setBackground(new Color(177, 156, 204));
                this.ivjSetupPanel1.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjSetupPanel1;
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.addWindowListener(this);
        this.getFlatButtonCancel().addActionListener(this);
        this.getFlatButtonSave().addActionListener(this);
    }

    private void initialize() {
        this.setIconImage(JBee.getIcon());
        this.setName("SetupFrame");
        this.setTitle("JBee setup");
        this.setLayout(new BorderLayout());
        ((Component) this).setSize(308, 301);
        this.setResizable(false);
        this.add((Component) this.getContentsPane(), "Center");
        this.initConnections();
        this.getPanel().setProperties(this.props);
        ((Component) this).setBackground(JBee.appcolor);
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

