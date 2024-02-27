/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.DoubleBufferPanel;
import com.objectbox.gui.lwcomp.FlatButton;
import com.objectbox.runner.gui.tree.Node;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Vector;

public class NameEditor
        extends Frame
        implements ActionListener,
        WindowListener {
    static Class class$java$awt$Window;
    private Panel ivjContentsPane = null;
    private FlatButton ivjFlatButtonCancel = null;
    private FlatButton ivjFlatButtonOK = null;
    private Label ivjLabelRename = null;
    private TextField ivjTextFieldFolderName = null;
    private DoubleBufferPanel ivjDoubleBufferPanelButtons = null;

    public NameEditor() {
        this.initialize();
    }

    public NameEditor(String string) {
        super(string);
    }

    public static void main(String[] stringArray) {
        try {
            NameEditor nameEditor = new NameEditor();
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
                Object[] objectArray = new Object[]{nameEditor};
                Constructor<?> constructor = clazz.getConstructor(classArray2);
                constructor.newInstance(objectArray);
            } catch (Throwable throwable) {
            }
            ((Component) nameEditor).setVisible(true);
        } catch (Throwable throwable) {
            System.err.println("Exception occurred in main() of java.awt.Frame");
            throwable.printStackTrace(System.out);
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.getFlatButtonCancel()) {
            this.connEtoM1();
        }
        if (actionEvent.getSource() == this.getFlatButtonOK()) {
            this.connEtoC2();
        }
        if (actionEvent.getSource() == this.getTextFieldFolderName()) {
            this.connEtoC3(actionEvent);
        }
    }

    private void connEtoC1(WindowEvent windowEvent) {
        try {
            this.dispose();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC2() {
        try {
            this.flatButtonOK_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC3(ActionEvent actionEvent) {
        try {
            this.flatButtonOK_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoM1() {
        try {
            this.dispose();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void flatButtonOK_Action() {
        JBManagerPanel jBManagerPanel = (JBManagerPanel) AppRegistry.getInstance().lookup("Manager");
        Node node = jBManagerPanel.getTreeBasePublic().getSelectedNode();
        if (node != null && node.getType().equals("Folder")) {
            node.setText(this.getTextFieldFolderName().getText());
            JBee jBee = (JBee) AppRegistry.getInstance().lookup("JBee");
            jBee.updateName(node, this.getTextFieldFolderName().getText());
            Object object = AppRegistry.getInstance().lookup("LabelFolder");
            if (object != null && node != null) {
                Vector<String> vector = new Vector<String>();
                while (node != jBManagerPanel.rootNode) {
                    if (node.getType().compareTo("Folder") == 0) {
                        vector.insertElementAt(node.getText(), 0);
                    }
                    node = (Node) node.getParent();
                }
                String string = "";
                Enumeration enumeration = vector.elements();
                while (enumeration.hasMoreElements()) {
                    string = String.valueOf(string) + "/" + enumeration.nextElement();
                }
                if (string.equals("")) {
                    string = "/";
                }
                ((Label) object).setText(string);
            }
            jBManagerPanel.updateVisual();
        }
        this.dispose();
    }

    private Panel getContentsPane() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        if (this.ivjContentsPane == null) {
            try {
                this.ivjContentsPane = new Panel();
                this.ivjContentsPane.setName("ContentsPane");
                this.ivjContentsPane.setLayout(new GridBagLayout());
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.fill = 2;
                gridBagConstraints.anchor = 10;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 0.0;
                gridBagConstraints.ipadx = 106;
                gridBagConstraints.insets = new Insets(10, 25, 10, 25);
                this.getContentsPane().add((Component) this.getTextFieldFolderName(), gridBagConstraints);
                gridBagConstraints2.gridx = 0;
                gridBagConstraints2.gridy = 0;
                gridBagConstraints2.gridwidth = 1;
                gridBagConstraints2.gridheight = 1;
                gridBagConstraints2.fill = 2;
                gridBagConstraints2.anchor = 10;
                gridBagConstraints2.weightx = 1.0;
                gridBagConstraints2.weighty = 0.0;
                gridBagConstraints2.ipadx = 15;
                gridBagConstraints2.insets = new Insets(10, 25, 0, 25);
                this.getContentsPane().add((Component) this.getLabelRename(), gridBagConstraints2);
                gridBagConstraints3.gridx = 0;
                gridBagConstraints3.gridy = 2;
                gridBagConstraints3.gridwidth = 1;
                gridBagConstraints3.gridheight = 1;
                gridBagConstraints3.fill = 2;
                gridBagConstraints3.anchor = 10;
                gridBagConstraints3.weightx = 0.0;
                gridBagConstraints3.weighty = 0.0;
                gridBagConstraints3.insets = new Insets(5, 10, 0, 10);
                this.getContentsPane().add((Component) this.getDoubleBufferPanelButtons(), gridBagConstraints3);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjContentsPane;
    }

    private DoubleBufferPanel getDoubleBufferPanelButtons() {
        if (this.ivjDoubleBufferPanelButtons == null) {
            try {
                this.ivjDoubleBufferPanelButtons = new DoubleBufferPanel();
                this.ivjDoubleBufferPanelButtons.setName("DoubleBufferPanelButtons");
                this.ivjDoubleBufferPanelButtons.setLayout(new FlowLayout());
                this.ivjDoubleBufferPanelButtons.setHasframe(false);
                this.getDoubleBufferPanelButtons().add((Component) this.getFlatButtonOK(), this.getFlatButtonOK().getName());
                this.ivjDoubleBufferPanelButtons.add(this.getFlatButtonCancel());
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjDoubleBufferPanelButtons;
    }

    private FlatButton getFlatButtonCancel() {
        if (this.ivjFlatButtonCancel == null) {
            try {
                this.ivjFlatButtonCancel = new FlatButton();
                this.ivjFlatButtonCancel.setName("FlatButtonCancel");
                this.ivjFlatButtonCancel.setFixedsize(new Dimension(73, 25));
                this.ivjFlatButtonCancel.setLabel("Cancel");
                this.ivjFlatButtonCancel.setImageResource("/images/cancel.gif", 3);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonCancel;
    }

    private FlatButton getFlatButtonOK() {
        if (this.ivjFlatButtonOK == null) {
            try {
                this.ivjFlatButtonOK = new FlatButton();
                this.ivjFlatButtonOK.setName("FlatButtonOK");
                this.ivjFlatButtonOK.setFixedsize(new Dimension(73, 25));
                this.ivjFlatButtonOK.setLabel("OK");
                this.ivjFlatButtonOK.setImageResource("/images/ok.gif", 3);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonOK;
    }

    private Label getLabelRename() {
        if (this.ivjLabelRename == null) {
            try {
                this.ivjLabelRename = new Label();
                this.ivjLabelRename.setName("LabelRename");
                this.ivjLabelRename.setText("New folder name:");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelRename;
    }

    private TextField getTextFieldFolderName() {
        if (this.ivjTextFieldFolderName == null) {
            try {
                this.ivjTextFieldFolderName = new TextField();
                this.ivjTextFieldFolderName.setName("TextFieldFolderName");
                ((Component) this.ivjTextFieldFolderName).setBackground(Color.white);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjTextFieldFolderName;
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.addWindowListener(this);
        this.getFlatButtonCancel().addActionListener(this);
        this.getFlatButtonOK().addActionListener(this);
        this.getTextFieldFolderName().addActionListener(this);
    }

    private void initialize() {
        this.setIconImage(JBee.getIcon());
        this.setName("NameEditor");
        this.setTitle("JBee - rename folder");
        this.setLayout(new BorderLayout());
        ((Component) this).setSize(282, 149);
        this.setResizable(false);
        this.add((Component) this.getContentsPane(), "Center");
        this.initConnections();
        ((Component) this).setBackground(JBee.appcolor);
        int n = Toolkit.getDefaultToolkit().getScreenSize().height;
        int n2 = Toolkit.getDefaultToolkit().getScreenSize().width;
        ((Component) this).setLocation(n2 / 2 - this.getSize().width / 2, n / 2 - this.getSize().height / 2);
    }

    public void setFolderName(String string) {
        ((TextComponent) this.getTextFieldFolderName()).setText(string);
        this.getTextFieldFolderName().setSelectionStart(0);
        this.getTextFieldFolderName().setSelectionEnd(string.length());
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

