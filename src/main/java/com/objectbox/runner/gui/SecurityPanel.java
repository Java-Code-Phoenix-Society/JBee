/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.LWSeparator;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.io.Serializable;

public class SecurityPanel
        extends Panel {
    public static final int HI = 1;
    public static final int MEDIUM = 2;
    public static final int LOW = 3;
    private Checkbox ivjCheckboxHighSecurity = null;
    private Checkbox ivjCheckboxMediumSecurity = null;
    private Checkbox ivjCheckboxNoSecurity = null;
    private FocusLabel ivjLabel2 = null;
    private CheckboxGroup ivjCheckboxGroupSecurity = null;
    private Panel ivjPanelCheck = null;
    private GridLayout ivjPanelCheckGridLayout = null;
    private Panel ivjPanelHolder = null;
    private LWSeparator ivjLWSeparator1 = null;
    private LWSeparator ivjLWSeparator11 = null;

    public SecurityPanel() {
        this.initialize();
    }

    public SecurityPanel(LayoutManager layoutManager) {
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
            serializable = new SecurityPanel();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Panel");
        }
    }

    private void connPtoP1SetTarget() {
        try {
            this.getCheckboxNoSecurity().setCheckboxGroup(this.getCheckboxGroupSecurity());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connPtoP2SetTarget() {
        try {
            this.getCheckboxMediumSecurity().setCheckboxGroup(this.getCheckboxGroupSecurity());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connPtoP3SetTarget() {
        try {
            this.getCheckboxHighSecurity().setCheckboxGroup(this.getCheckboxGroupSecurity());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connPtoP4SetTarget() {
        try {
            this.getCheckboxGroupSecurity().setSelectedCheckbox(this.getCheckboxNoSecurity());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private CheckboxGroup getCheckboxGroupSecurity() {
        if (this.ivjCheckboxGroupSecurity == null) {
            try {
                this.ivjCheckboxGroupSecurity = new CheckboxGroup();
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckboxGroupSecurity;
    }

    private Checkbox getCheckboxHighSecurity() {
        if (this.ivjCheckboxHighSecurity == null) {
            try {
                this.ivjCheckboxHighSecurity = new Checkbox();
                this.ivjCheckboxHighSecurity.setName("CheckboxHighSecurity");
                this.ivjCheckboxHighSecurity.setLabel("High");
                this.ivjCheckboxHighSecurity.setState(false);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckboxHighSecurity;
    }

    private Checkbox getCheckboxMediumSecurity() {
        if (this.ivjCheckboxMediumSecurity == null) {
            try {
                this.ivjCheckboxMediumSecurity = new Checkbox();
                this.ivjCheckboxMediumSecurity.setName("CheckboxMediumSecurity");
                this.ivjCheckboxMediumSecurity.setLabel("Medium");
                this.ivjCheckboxMediumSecurity.setState(false);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckboxMediumSecurity;
    }

    private Checkbox getCheckboxNoSecurity() {
        if (this.ivjCheckboxNoSecurity == null) {
            try {
                this.ivjCheckboxNoSecurity = new Checkbox();
                this.ivjCheckboxNoSecurity.setName("CheckboxNoSecurity");
                this.ivjCheckboxNoSecurity.setLabel("Low");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckboxNoSecurity;
    }

    private FocusLabel getLabel2() {
        if (this.ivjLabel2 == null) {
            try {
                this.ivjLabel2 = new FocusLabel();
                this.ivjLabel2.setName("Label2");
                this.ivjLabel2.setAlignment(1);
                this.ivjLabel2.setText("Please set the security level");
                this.ivjLabel2.setBounds(13, 14, 219, 23);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabel2;
    }

    private LWSeparator getLWSeparator1() {
        if (this.ivjLWSeparator1 == null) {
            try {
                this.ivjLWSeparator1 = new LWSeparator();
                this.ivjLWSeparator1.setName("LWSeparator1");
                this.ivjLWSeparator1.setBounds(43, 46, 159, 15);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLWSeparator1;
    }

    private LWSeparator getLWSeparator11() {
        if (this.ivjLWSeparator11 == null) {
            try {
                this.ivjLWSeparator11 = new LWSeparator();
                this.ivjLWSeparator11.setName("LWSeparator11");
                this.ivjLWSeparator11.setBounds(43, 168, 159, 15);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLWSeparator11;
    }

    private Panel getPanelCheck() {
        if (this.ivjPanelCheck == null) {
            try {
                this.ivjPanelCheck = new Panel();
                this.ivjPanelCheck.setName("PanelCheck");
                this.ivjPanelCheck.setLayout(this.getPanelCheckGridLayout());
                this.ivjPanelCheck.setBounds(77, 68, 91, 85);
                this.getPanelCheck().add((Component) this.getCheckboxHighSecurity(), this.getCheckboxHighSecurity().getName());
                this.getPanelCheck().add((Component) this.getCheckboxMediumSecurity(), this.getCheckboxMediumSecurity().getName());
                this.ivjPanelCheck.add(this.getCheckboxNoSecurity());
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelCheck;
    }

    private GridLayout getPanelCheckGridLayout() {
        GridLayout gridLayout = null;
        try {
            gridLayout = new GridLayout();
            gridLayout.setRows(3);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
        return gridLayout;
    }

    private Panel getPanelHolder() {
        if (this.ivjPanelHolder == null) {
            try {
                this.ivjPanelHolder = new Panel();
                this.ivjPanelHolder.setName("PanelHolder");
                this.ivjPanelHolder.setLayout(null);
                this.ivjPanelHolder.setBackground(SystemColor.control);
                this.getPanelHolder().add((Component) this.getLabel2(), this.getLabel2().getName());
                this.getPanelHolder().add((Component) this.getPanelCheck(), this.getPanelCheck().getName());
                this.getPanelHolder().add((Component) this.getLWSeparator1(), this.getLWSeparator1().getName());
                this.getPanelHolder().add((Component) this.getLWSeparator11(), this.getLWSeparator11().getName());
                this.ivjPanelHolder.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelHolder;
    }

    public int getSecurityLevel() {
        if (this.getCheckboxHighSecurity().getState()) {
            return 1;
        }
        if (this.getCheckboxMediumSecurity().getState()) {
            return 2;
        }
        if (this.getCheckboxNoSecurity().getState()) {
            return 3;
        }
        return 0;
    }

    public void setSecurityLevel(int n) {
        switch (n) {
            case 3: {
                this.getCheckboxGroupSecurity().setSelectedCheckbox(this.getCheckboxNoSecurity());
                break;
            }
            case 1: {
                this.getCheckboxGroupSecurity().setSelectedCheckbox(this.getCheckboxHighSecurity());
                break;
            }
            case 2: {
                this.getCheckboxGroupSecurity().setSelectedCheckbox(this.getCheckboxMediumSecurity());
                break;
            }
            default: {
                this.getCheckboxGroupSecurity().setSelectedCheckbox(this.getCheckboxHighSecurity());
            }
        }
        this.validate();
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.connPtoP1SetTarget();
        this.connPtoP2SetTarget();
        this.connPtoP3SetTarget();
        this.connPtoP4SetTarget();
    }

    private void initialize() {
        this.setName("SecurityPanel");
        this.setLayout(new BorderLayout());
        this.setSize(240, 204);
        this.add((Component) this.getPanelHolder(), "Center");
        this.initConnections();
    }
}

