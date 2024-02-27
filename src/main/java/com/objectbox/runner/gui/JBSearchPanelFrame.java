/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.runner.model.JBAppletProperties;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Constructor;

public class JBSearchPanelFrame
        extends Frame
        implements WindowListener {
    static Class class$java$awt$Window;
    protected JBManagerPanel jbmanager = null;
    private Panel ivjContentsPane = null;
    private JBSearchPanel ivJBSearchPanel = null;
    private JBSearchPanel ivjJBSearchPanel1 = null;

    public JBSearchPanelFrame() {
        this.initialize();
    }

    public JBSearchPanelFrame(String string) {
        super(string);
        this.initialize();
    }

    public static void main(String[] stringArray) {
        try {
            JBSearchPanelFrame jBSearchPanelFrame = new JBSearchPanelFrame();
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
                Object[] objectArray = new Object[]{jBSearchPanelFrame};
                Constructor<?> constructor = clazz.getConstructor(classArray2);
                constructor.newInstance(objectArray);
            } catch (Throwable throwable) {
            }
            ((Component) jBSearchPanelFrame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Frame");
        }
    }

    private void connEtoC1(WindowEvent windowEvent) {
        try {
            this.dispose();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private Panel getContentsPane() {
        if (this.ivjContentsPane == null) {
            try {
                this.ivjContentsPane = new Panel();
                this.ivjContentsPane.setName("ContentsPane");
                this.ivjContentsPane.setLayout(new BorderLayout());
                this.getContentsPane().add((Component) this.getJBSearchPanel1(), "Center");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjContentsPane;
    }

    private JBSearchPanel getJBSearchPanel1() {
        if (this.ivjJBSearchPanel1 == null) {
            try {
                this.ivjJBSearchPanel1 = new JBSearchPanel();
                this.ivjJBSearchPanel1.setName("JBSearchPanel1");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjJBSearchPanel1;
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.addWindowListener(this);
    }

    private void initialize() {
        AppRegistry.getInstance().put("SearchFrame", this);
        this.setName("JBSearchPanelFrame");
        this.setLayout(new BorderLayout());
        ((Component) this).setSize(660, 503);
        this.add((Component) this.getContentsPane(), "Center");
        this.initConnections();
        ((Component) this).setSize(640, 470);
        this.setIconImage(JBee.getIcon());
        this.setTitle("JBee Search and Admin");
        int n = Toolkit.getDefaultToolkit().getScreenSize().height;
        int n2 = Toolkit.getDefaultToolkit().getScreenSize().width;
        ((Component) this).setLocation(n2 / 2 - this.getSize().width / 2, n / 2 - this.getSize().height / 2);
        AppRegistry.getInstance().put("JBSearchPanelFrame", this);
    }

    public void saveState() {
        this.getJBSearchPanel1().saveHistory();
    }

    public void windowActivated(WindowEvent windowEvent) {
    }

    public void windowClosed(WindowEvent windowEvent) {
    }

    public void windowClosing(WindowEvent windowEvent) {
        JBManagerPanel jBManagerPanel = (JBManagerPanel) AppRegistry.getInstance().lookup("Manager");
        ((Component) jBManagerPanel.getNameEditor()).setVisible(false);
        ((Component) JBAppletProperties.editorFrame).setVisible(false);
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

