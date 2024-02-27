/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.FlatButton;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;

public class SecurityWarningDialog
        extends Frame
        implements ActionListener {
    static Class class$java$awt$Window;
    protected transient SecurityChangedListener aSecurityChangedListener = null;
    private Panel ivjContentsPane = null;
    private FlatButton ivjFlatButtonOK = null;
    private Panel ivjPanelButtons = null;
    private Label ivjLabelMessage = null;
    private CustomTextArea ivjTextAreaMessage = null;

    public SecurityWarningDialog() {
        this.initialize();
    }

    public SecurityWarningDialog(String string) {
        this.getTextAreaMessage().setText(string);
        this.initialize();
    }

    public static void main(String[] stringArray) {
        try {
            SecurityWarningDialog securityWarningDialog = new SecurityWarningDialog();
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
                Object[] objectArray = new Object[]{securityWarningDialog};
                Constructor<?> constructor = clazz.getConstructor(classArray2);
                constructor.newInstance(objectArray);
            } catch (Throwable throwable) {
            }
            ((Component) securityWarningDialog).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Dialog");
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.getFlatButtonOK()) {
            this.connEtoC2(actionEvent);
        }
    }

    public void appendMessage(String string) {
        if (this.getTextAreaMessage().getText().length() > 30000) {
            this.getTextAreaMessage().setText("");
        }
        this.getTextAreaMessage().append("\n\n" + string);
    }

    private void connEtoC2(ActionEvent actionEvent) {
        try {
            this.flatButtonOK_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void flatButtonCancel_ActionPerformed(ActionEvent actionEvent) {
        this.dispose();
    }

    public void flatButtonOK_ActionPerformed(ActionEvent actionEvent) {
        ((Component) this).setVisible(false);
        this.setMessage("");
    }

    private Panel getContentsPane() {
        if (this.ivjContentsPane == null) {
            try {
                this.ivjContentsPane = new Panel();
                this.ivjContentsPane.setName("ContentsPane");
                this.ivjContentsPane.setLayout(new BorderLayout());
                this.getContentsPane().add((Component) this.getPanelButtons(), "South");
                this.getContentsPane().add((Component) this.getLabelMessage(), "North");
                this.getContentsPane().add((Component) this.getTextAreaMessage(), "Center");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjContentsPane;
    }

    private FlatButton getFlatButtonOK() {
        if (this.ivjFlatButtonOK == null) {
            try {
                this.ivjFlatButtonOK = new FlatButton();
                this.ivjFlatButtonOK.setName("FlatButtonOK");
                this.ivjFlatButtonOK.setFixedsize(new Dimension(75, 35));
                this.ivjFlatButtonOK.setLabel("Close");
                this.ivjFlatButtonOK.setImageResource("/images/ok.gif", 3);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonOK;
    }

    private Label getLabelMessage() {
        if (this.ivjLabelMessage == null) {
            try {
                this.ivjLabelMessage = new Label();
                this.ivjLabelMessage.setName("LabelMessage");
                this.ivjLabelMessage.setText("Security Violation(s): ");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelMessage;
    }

    private Panel getPanelButtons() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        if (this.ivjPanelButtons == null) {
            try {
                this.ivjPanelButtons = new Panel();
                this.ivjPanelButtons.setName("PanelButtons");
                this.ivjPanelButtons.setLayout(new GridBagLayout());
                this.ivjPanelButtons.setBackground(new Color(182, 182, 200));
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.anchor = 10;
                gridBagConstraints.weightx = 0.0;
                gridBagConstraints.weighty = 0.0;
                gridBagConstraints.insets = new Insets(10, 10, 10, 10);
                this.getPanelButtons().add((Component) this.getFlatButtonOK(), gridBagConstraints);
                this.ivjPanelButtons.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelButtons;
    }

    private CustomTextArea getTextAreaMessage() {
        if (this.ivjTextAreaMessage == null) {
            try {
                this.ivjTextAreaMessage = new CustomTextArea();
                this.ivjTextAreaMessage.setName("TextAreaMessage");
                this.ivjTextAreaMessage.setForeground(Color.red);
                this.ivjTextAreaMessage.setEditable(false);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjTextAreaMessage;
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.getFlatButtonOK().addActionListener(this);
    }

    private void initialize() {
        AppRegistry.getInstance().put("SecurityWarningDialog", this);
        this.setName("SecurityDialog");
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        ((Component) this).setBackground(new Color(182, 182, 200));
        ((Component) this).setSize(285, 160);
        this.setTitle("JBee security manager");
        this.add((Component) this.getContentsPane(), "Center");
        this.initConnections();
        ((Component) this).setBackground(JBee.appcolor);
        this.setIconImage(JBee.getIcon());
        int n = Toolkit.getDefaultToolkit().getScreenSize().height;
        int n2 = Toolkit.getDefaultToolkit().getScreenSize().width;
        ((Component) this).setLocation(n2 / 2 - this.getSize().width / 2, n / 2 - this.getSize().height / 2);
    }

    public void setMessage(String string) {
        this.getTextAreaMessage().setText(string);
    }
}

