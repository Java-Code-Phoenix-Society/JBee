/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.DoubleBufferPanel;
import com.objectbox.gui.lwcomp.FlatButton;
import com.objectbox.gui.lwcomp.LWSeparator;
import com.objectbox.runner.beans.TableView;
import com.objectbox.runner.model.JBProperties;
import com.objectbox.runner.model.JBPropertyEditor;
import com.objectbox.runner.model.PropTableModel;
import com.objectbox.runner.util.JBLogger;
import com.sun.java.swing.BoxLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class AppletPropertiesEditor
        extends Panel
        implements JBPropertyEditor,
        ActionListener {
    private TableView ivjTableViewProps = null;
    private PropTableModel ivjPropTableModel1 = null;
    private int yPos = 0;
    private int xPos = 1;
    private DoubleBufferPanel ivjDoubleBufferPanelMenu = null;
    private FlatButton ivjFlatButtonDelete = null;
    private FlatButton ivjFlatButtonNew = null;
    private CheckboxGroup ivjCheckboxGroup1 = null;
    private Checkbox ivjCheckboxParameter = null;
    private Checkbox ivjCheckboxProperty = null;
    private DoubleBufferPanel ivjDoubleBufferPanelMenu1 = null;
    private Panel ivjEditorContentsPane = null;
    private FlatButton ivjFlatButtonCancel = null;
    private FlatButton ivjFlatButtonOk = null;
    private Dialog dialog = null;
    private boolean dialogSvar = false;
    private Label ivjLabel1 = null;
    private Label ivjLabelName = null;
    private Label ivjLabelValue = null;
    private TextField ivjTextFieldName = null;
    private TextField ivjTextFieldValue = null;
    private Panel ivjTableViewPanel = null;
    private BoxLayout ivjDoubleBufferPanelMenuBoxLayout = null;
    private Label ivjLabelCache = null;
    private Label ivjLabelCacheSize = null;
    private Label ivjLabelCacheUnits = null;
    private Panel ivjPanelAppletDetails = null;
    private DoubleBufferPanel ivjDoubleBufferPanelMenu3 = null;
    private LWSeparator ivjLWSeparator1 = null;
    private FlatButton ivjFlatButtonClearCache = null;

    public AppletPropertiesEditor() {
        this.initialize();
    }

    public AppletPropertiesEditor(LayoutManager layoutManager) {
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
            serializable = new AppletPropertiesEditor();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Panel");
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.getFlatButtonNew()) {
            this.connEtoC1(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonDelete()) {
            this.connEtoC2(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonCancel()) {
            this.connEtoC3(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonOk()) {
            this.connEtoC4(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonClearCache()) {
            this.connEtoC5();
        }
    }

    private void connEtoC1(ActionEvent actionEvent) {
        try {
            this.flatButtonNew_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC2(ActionEvent actionEvent) {
        try {
            this.flatButtonDelete_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC3(ActionEvent actionEvent) {
        try {
            this.flatButtonCancel_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC4(ActionEvent actionEvent) {
        try {
            this.flatButtonOk_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC5() {
        try {
            this.flatButtonClearCache_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connPtoP1SetTarget() {
        try {
            this.getTableViewProps().setModel(this.getPropTableModel1());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connPtoP2SetTarget() {
        try {
            this.getCheckboxParameter().setCheckboxGroup(this.getCheckboxGroup1());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connPtoP3SetTarget() {
        try {
            this.getCheckboxProperty().setCheckboxGroup(this.getCheckboxGroup1());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connPtoP4SetTarget() {
        try {
            this.getCheckboxGroup1().setSelectedCheckbox(this.getCheckboxParameter());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void edit(JBProperties jBProperties) {
        this.getPropTableModel1().setData(jBProperties);
        this.getTableViewProps().setModel(this.getPropTableModel1());
        this.getLabelCacheSize().setText(this.getPropTableModel1().computeCacheSize());
    }

    public void flatButtonCancel_ActionPerformed(ActionEvent actionEvent) {
        this.dialogSvar = false;
        ((Component) this.getDialog()).setVisible(false);
    }

    public void flatButtonClearCache_Action() {
        JBLogger.log("Deleting cache...");
        this.getPropTableModel1().deleteCache();
        this.getLabelCacheSize().setText(this.getPropTableModel1().computeCacheSize());
        JBLogger.log("Done deleting cache!");
    }

    public void flatButtonClose_ActionPerformed(ActionEvent actionEvent) {
        Container container = this.getParent();
        while (container != null && !(container instanceof Frame)) {
            container = container.getParent();
        }
        ((Frame) container).dispose();
    }

    public void flatButtonDelete_ActionPerformed(ActionEvent actionEvent) {
        this.getPropTableModel1().delete(this.getTableViewProps().getCurrent().y);
        this.getTableViewProps().setModel(this.getPropTableModel1());
    }

    public void flatButtonNew_ActionPerformed(ActionEvent actionEvent) {
        ((Component) this.getDialog()).setSize(300, 200);
        ((Component) this.getDialog()).setLocation(this.getLocationOnScreen().x + 20, this.getLocationOnScreen().y + 100);
        ((TextComponent) this.getTextFieldName()).setText("");
        ((TextComponent) this.getTextFieldValue()).setText("");
        this.getCheckboxGroup1().setSelectedCheckbox(this.getCheckboxParameter());
        ((Component) this.getDialog()).setVisible(true);
        ((Component) this.getDialog()).setVisible(false);
        if (this.dialogSvar) {
            String string = this.getTextFieldName().getText();
            String string2 = this.getTextFieldValue().getText();
            if (this.getCheckboxParameter().getState()) {
                this.getPropTableModel1().newParameter(string, string2);
            } else {
                this.getPropTableModel1().newProperty(string, string2);
            }
            this.getTableViewProps().setModel(this.getPropTableModel1());
            this.getTableViewProps().autoFitAll();
            this.getTableViewProps().repaint();
        }
    }

    public void flatButtonOk_ActionPerformed(ActionEvent actionEvent) {
        this.dialogSvar = true;
        ((Component) this.getDialog()).setVisible(false);
    }

    private CheckboxGroup getCheckboxGroup1() {
        if (this.ivjCheckboxGroup1 == null) {
            try {
                this.ivjCheckboxGroup1 = new CheckboxGroup();
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckboxGroup1;
    }

    private Checkbox getCheckboxParameter() {
        if (this.ivjCheckboxParameter == null) {
            try {
                this.ivjCheckboxParameter = new Checkbox();
                this.ivjCheckboxParameter.setName("CheckboxParameter");
                this.ivjCheckboxParameter.setLabel("Parameter");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckboxParameter;
    }

    private Checkbox getCheckboxProperty() {
        if (this.ivjCheckboxProperty == null) {
            try {
                this.ivjCheckboxProperty = new Checkbox();
                this.ivjCheckboxProperty.setName("CheckboxProperty");
                this.ivjCheckboxProperty.setLabel("Property");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckboxProperty;
    }

    private Dialog getDialog() {
        if (this.dialog == null) {
            try {
                Container container = this.getParent();
                while (container != null && !(container instanceof Frame)) {
                    container = container.getParent();
                }
                Frame frame = new Frame();
                frame = (Frame) container;
                this.dialog = new Dialog(frame, true);
                this.dialog.setName("Editor");
                this.dialog.setLayout(new BorderLayout());
                this.dialog.setModal(true);
                this.getDialog().add((Component) this.getEditorContentsPane(), "Center");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.dialog;
    }

    private DoubleBufferPanel getDoubleBufferPanelMenu() {
        if (this.ivjDoubleBufferPanelMenu == null) {
            try {
                this.ivjDoubleBufferPanelMenu = new DoubleBufferPanel();
                this.ivjDoubleBufferPanelMenu.setName("DoubleBufferPanelMenu");
                this.ivjDoubleBufferPanelMenu.setLayout(this.getDoubleBufferPanelMenuBoxLayout());
                this.ivjDoubleBufferPanelMenu.setBackground(new Color(193, 195, 239));
                this.ivjDoubleBufferPanelMenu.setHasframe(false);
                this.getDoubleBufferPanelMenu().add((Component) this.getFlatButtonNew(), this.getFlatButtonNew().getName());
                this.getDoubleBufferPanelMenu().add((Component) this.getFlatButtonDelete(), this.getFlatButtonDelete().getName());
                this.ivjDoubleBufferPanelMenu.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjDoubleBufferPanelMenu;
    }

    private DoubleBufferPanel getDoubleBufferPanelMenu1() {
        if (this.ivjDoubleBufferPanelMenu1 == null) {
            try {
                this.ivjDoubleBufferPanelMenu1 = new DoubleBufferPanel();
                this.ivjDoubleBufferPanelMenu1.setName("DoubleBufferPanelMenu1");
                this.ivjDoubleBufferPanelMenu1.setLayout(new FlowLayout());
                this.ivjDoubleBufferPanelMenu1.setBackground(new Color(193, 195, 239));
                this.ivjDoubleBufferPanelMenu1.setHasframe(false);
                this.getDoubleBufferPanelMenu1().add((Component) this.getFlatButtonOk(), this.getFlatButtonOk().getName());
                this.getDoubleBufferPanelMenu1().add((Component) this.getFlatButtonCancel(), this.getFlatButtonCancel().getName());
                this.ivjDoubleBufferPanelMenu1.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjDoubleBufferPanelMenu1;
    }

    private DoubleBufferPanel getDoubleBufferPanelMenu3() {
        if (this.ivjDoubleBufferPanelMenu3 == null) {
            try {
                this.ivjDoubleBufferPanelMenu3 = new DoubleBufferPanel();
                this.ivjDoubleBufferPanelMenu3.setName("DoubleBufferPanelMenu3");
                this.ivjDoubleBufferPanelMenu3.setLayout(new BorderLayout());
                this.ivjDoubleBufferPanelMenu3.setBackground(new Color(193, 195, 239));
                this.ivjDoubleBufferPanelMenu3.setHasframe(false);
                this.getDoubleBufferPanelMenu3().add((Component) this.getPanelAppletDetails(), "Center");
                this.getDoubleBufferPanelMenu3().add((Component) this.getLWSeparator1(), "North");
                this.getDoubleBufferPanelMenu3().setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjDoubleBufferPanelMenu3;
    }

    private BoxLayout getDoubleBufferPanelMenuBoxLayout() {
        BoxLayout boxLayout = null;
        try {
            boxLayout = new BoxLayout(this.getDoubleBufferPanelMenu(), 1);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
        return boxLayout;
    }

    private Panel getEditorContentsPane() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        if (this.ivjEditorContentsPane == null) {
            try {
                this.ivjEditorContentsPane = new Panel();
                this.ivjEditorContentsPane.setName("EditorContentsPane");
                this.ivjEditorContentsPane.setLayout(new GridBagLayout());
                this.ivjEditorContentsPane.setBackground(new Color(193, 195, 239));
                this.ivjEditorContentsPane.setBounds(377, 50, 309, 199);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 3;
                gridBagConstraints.gridwidth = 4;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.fill = 2;
                gridBagConstraints.anchor = 10;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.insets = new Insets(10, 10, 10, 10);
                this.getEditorContentsPane().add((Component) this.getDoubleBufferPanelMenu1(), gridBagConstraints);
                gridBagConstraints2.gridx = 0;
                gridBagConstraints2.gridy = 1;
                gridBagConstraints2.gridwidth = 1;
                gridBagConstraints2.gridheight = 1;
                gridBagConstraints2.anchor = 17;
                gridBagConstraints2.weightx = 0.0;
                gridBagConstraints2.weighty = 0.0;
                gridBagConstraints2.insets = new Insets(10, 10, 0, 0);
                this.getEditorContentsPane().add((Component) this.getLabelName(), gridBagConstraints2);
                gridBagConstraints3.gridx = 0;
                gridBagConstraints3.gridy = 2;
                gridBagConstraints3.gridwidth = 1;
                gridBagConstraints3.gridheight = 1;
                gridBagConstraints3.anchor = 16;
                gridBagConstraints3.weightx = 0.0;
                gridBagConstraints3.weighty = 0.0;
                gridBagConstraints3.insets = new Insets(0, 10, 0, 0);
                this.getEditorContentsPane().add((Component) this.getLabelValue(), gridBagConstraints3);
                gridBagConstraints4.gridx = 2;
                gridBagConstraints4.gridy = 1;
                gridBagConstraints4.gridwidth = 2;
                gridBagConstraints4.gridheight = 1;
                gridBagConstraints4.fill = 1;
                gridBagConstraints4.anchor = 10;
                gridBagConstraints4.weightx = 0.0;
                gridBagConstraints4.weighty = 0.0;
                gridBagConstraints4.insets = new Insets(10, 0, 0, 10);
                this.getEditorContentsPane().add((Component) this.getTextFieldName(), gridBagConstraints4);
                gridBagConstraints5.gridx = 2;
                gridBagConstraints5.gridy = 2;
                gridBagConstraints5.gridwidth = 2;
                gridBagConstraints5.gridheight = 1;
                gridBagConstraints5.fill = 2;
                gridBagConstraints5.anchor = 10;
                gridBagConstraints5.weightx = 0.0;
                gridBagConstraints5.weighty = 0.0;
                gridBagConstraints5.insets = new Insets(0, 0, 0, 10);
                this.getEditorContentsPane().add((Component) this.getTextFieldValue(), gridBagConstraints5);
                gridBagConstraints6.gridx = 0;
                gridBagConstraints6.gridy = 0;
                gridBagConstraints6.gridwidth = 1;
                gridBagConstraints6.gridheight = 1;
                gridBagConstraints6.anchor = 17;
                gridBagConstraints6.weightx = 0.0;
                gridBagConstraints6.weighty = 0.0;
                gridBagConstraints6.insets = new Insets(0, 10, 0, 0);
                this.getEditorContentsPane().add((Component) this.getLabel1(), gridBagConstraints6);
                gridBagConstraints7.gridx = 2;
                gridBagConstraints7.gridy = 0;
                gridBagConstraints7.gridwidth = 1;
                gridBagConstraints7.gridheight = 1;
                gridBagConstraints7.anchor = 10;
                gridBagConstraints7.weightx = 0.0;
                gridBagConstraints7.weighty = 0.0;
                this.getEditorContentsPane().add((Component) this.getCheckboxParameter(), gridBagConstraints7);
                gridBagConstraints8.gridx = 3;
                gridBagConstraints8.gridy = 0;
                gridBagConstraints8.gridwidth = 1;
                gridBagConstraints8.gridheight = 1;
                gridBagConstraints8.anchor = 10;
                gridBagConstraints8.weightx = 0.0;
                gridBagConstraints8.weighty = 0.0;
                this.getEditorContentsPane().add((Component) this.getCheckboxProperty(), gridBagConstraints8);
                this.getEditorContentsPane().setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjEditorContentsPane;
    }

    private FlatButton getFlatButtonCancel() {
        if (this.ivjFlatButtonCancel == null) {
            try {
                this.ivjFlatButtonCancel = new FlatButton();
                this.ivjFlatButtonCancel.setName("FlatButtonCancel");
                this.ivjFlatButtonCancel.setFixedsize(new Dimension(70, 35));
                this.ivjFlatButtonCancel.setLabel("Cancel");
                this.ivjFlatButtonCancel.setImageResource("/images/cancel.gif", 3);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonCancel;
    }

    private FlatButton getFlatButtonClearCache() {
        if (this.ivjFlatButtonClearCache == null) {
            try {
                this.ivjFlatButtonClearCache = new FlatButton();
                this.ivjFlatButtonClearCache.setName("FlatButtonClearCache");
                this.ivjFlatButtonClearCache.setFixedsize(new Dimension(75, 14));
                this.ivjFlatButtonClearCache.setLabel("Clear cache");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonClearCache;
    }

    private FlatButton getFlatButtonDelete() {
        if (this.ivjFlatButtonDelete == null) {
            try {
                this.ivjFlatButtonDelete = new FlatButton();
                this.ivjFlatButtonDelete.setName("FlatButtonDelete");
                this.ivjFlatButtonDelete.setFixedsize(new Dimension(70, 50));
                this.ivjFlatButtonDelete.setLabel("Delete");
                this.ivjFlatButtonDelete.setImageResource("/images/delete.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonDelete;
    }

    private FlatButton getFlatButtonNew() {
        if (this.ivjFlatButtonNew == null) {
            try {
                this.ivjFlatButtonNew = new FlatButton();
                this.ivjFlatButtonNew.setName("FlatButtonNew");
                this.ivjFlatButtonNew.setFixedsize(new Dimension(70, 50));
                this.ivjFlatButtonNew.setLabel("New");
                this.ivjFlatButtonNew.setImageResource("/images/File.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonNew;
    }

    private FlatButton getFlatButtonOk() {
        if (this.ivjFlatButtonOk == null) {
            try {
                this.ivjFlatButtonOk = new FlatButton();
                this.ivjFlatButtonOk.setName("FlatButtonOk");
                this.ivjFlatButtonOk.setFixedsize(new Dimension(70, 35));
                this.ivjFlatButtonOk.setLabel("Ok");
                this.ivjFlatButtonOk.setImageResource("/images/ok.gif", 3);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonOk;
    }

    private Label getLabel1() {
        if (this.ivjLabel1 == null) {
            try {
                this.ivjLabel1 = new Label();
                this.ivjLabel1.setName("Label1");
                this.ivjLabel1.setText("New:");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabel1;
    }

    private Label getLabelCache() {
        if (this.ivjLabelCache == null) {
            try {
                this.ivjLabelCache = new Label();
                this.ivjLabelCache.setName("LabelCache");
                this.ivjLabelCache.setAlignment(2);
                this.ivjLabelCache.setText("Cache size:");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelCache;
    }

    private Label getLabelCacheSize() {
        if (this.ivjLabelCacheSize == null) {
            try {
                this.ivjLabelCacheSize = new Label();
                this.ivjLabelCacheSize.setName("LabelCacheSize");
                this.ivjLabelCacheSize.setAlignment(2);
                this.ivjLabelCacheSize.setText("0");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelCacheSize;
    }

    private Label getLabelCacheUnits() {
        if (this.ivjLabelCacheUnits == null) {
            try {
                this.ivjLabelCacheUnits = new Label();
                this.ivjLabelCacheUnits.setName("LabelCacheUnits");
                this.ivjLabelCacheUnits.setAlignment(0);
                this.ivjLabelCacheUnits.setText("bytes");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelCacheUnits;
    }

    private Label getLabelName() {
        if (this.ivjLabelName == null) {
            try {
                this.ivjLabelName = new Label();
                this.ivjLabelName.setName("LabelName");
                this.ivjLabelName.setText("Name:");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelName;
    }

    private Label getLabelValue() {
        if (this.ivjLabelValue == null) {
            try {
                this.ivjLabelValue = new Label();
                this.ivjLabelValue.setName("LabelValue");
                this.ivjLabelValue.setText("Value:");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelValue;
    }

    private LWSeparator getLWSeparator1() {
        if (this.ivjLWSeparator1 == null) {
            try {
                this.ivjLWSeparator1 = new LWSeparator();
                this.ivjLWSeparator1.setName("LWSeparator1");
                this.ivjLWSeparator1.setDirection(true);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLWSeparator1;
    }

    private Panel getPanelAppletDetails() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        if (this.ivjPanelAppletDetails == null) {
            try {
                this.ivjPanelAppletDetails = new Panel();
                this.ivjPanelAppletDetails.setName("PanelAppletDetails");
                this.ivjPanelAppletDetails.setLayout(new GridBagLayout());
                this.ivjPanelAppletDetails.setBackground(new Color(193, 195, 239));
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.fill = 2;
                gridBagConstraints.anchor = 10;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 0.0;
                this.getPanelAppletDetails().add((Component) this.getLabelCache(), gridBagConstraints);
                gridBagConstraints2.gridx = 2;
                gridBagConstraints2.gridy = 1;
                gridBagConstraints2.gridwidth = 1;
                gridBagConstraints2.gridheight = 1;
                gridBagConstraints2.fill = 2;
                gridBagConstraints2.anchor = 10;
                gridBagConstraints2.weightx = 0.5;
                gridBagConstraints2.weighty = 0.0;
                gridBagConstraints2.ipadx = -30;
                gridBagConstraints2.insets = new Insets(0, 10, 0, 0);
                this.getPanelAppletDetails().add((Component) this.getLabelCacheSize(), gridBagConstraints2);
                gridBagConstraints3.gridx = 3;
                gridBagConstraints3.gridy = 1;
                gridBagConstraints3.gridwidth = 1;
                gridBagConstraints3.gridheight = 1;
                gridBagConstraints3.fill = 2;
                gridBagConstraints3.anchor = 17;
                gridBagConstraints3.weightx = 1.0;
                gridBagConstraints3.weighty = 0.0;
                gridBagConstraints3.insets = new Insets(0, 10, 0, 0);
                this.getPanelAppletDetails().add((Component) this.getLabelCacheUnits(), gridBagConstraints3);
                gridBagConstraints4.gridx = 4;
                gridBagConstraints4.gridy = 1;
                gridBagConstraints4.gridwidth = 1;
                gridBagConstraints4.gridheight = 1;
                gridBagConstraints4.fill = 2;
                gridBagConstraints4.anchor = 13;
                gridBagConstraints4.weightx = 0.0;
                gridBagConstraints4.weighty = 0.0;
                gridBagConstraints4.insets = new Insets(0, 0, 0, 10);
                this.getPanelAppletDetails().add((Component) this.getFlatButtonClearCache(), gridBagConstraints4);
                this.getPanelAppletDetails().setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelAppletDetails;
    }

    private PropTableModel getPropTableModel1() {
        if (this.ivjPropTableModel1 == null) {
            try {
                this.ivjPropTableModel1 = new PropTableModel();
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPropTableModel1;
    }

    private Panel getTableViewPanel() {
        if (this.ivjTableViewPanel == null) {
            try {
                this.ivjTableViewPanel = new Panel();
                this.ivjTableViewPanel.setName("TableViewPanel");
                this.ivjTableViewPanel.setLayout(new BorderLayout());
                this.getTableViewPanel().add((Component) this.getTableViewProps(), "Center");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjTableViewPanel;
    }

    private TableView getTableViewProps() {
        if (this.ivjTableViewProps == null) {
            try {
                this.ivjTableViewProps = new TableView();
                this.ivjTableViewProps.setName("TableViewProps");
                this.ivjTableViewProps.setCellheight(28);
                this.ivjTableViewProps.setBackground(Color.white);
                this.ivjTableViewProps.setCellwidth(160);
                this.ivjTableViewProps.setHeaderforeground(Color.white);
                this.ivjTableViewProps.setAutofit(true);
                this.ivjTableViewProps.setHeaderbackground(new Color(46, 55, 216));
                this.ivjTableViewProps.setNumberOfcols(2);
                if (JBee.OS_type == 1) {
                    this.ivjTableViewProps.setCellheight(18);
                }
                this.ivjTableViewProps.setCelleditor(new PropertyEditor());
                this.ivjTableViewProps.setCellrenderer(new CellRenderer());
                this.ivjTableViewProps.autoFitAll();
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjTableViewProps;
    }

    private TextField getTextFieldName() {
        if (this.ivjTextFieldName == null) {
            try {
                this.ivjTextFieldName = new TextField();
                this.ivjTextFieldName.setName("TextFieldName");
                ((Component) this.ivjTextFieldName).setBackground(Color.white);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjTextFieldName;
    }

    private TextField getTextFieldValue() {
        if (this.ivjTextFieldValue == null) {
            try {
                this.ivjTextFieldValue = new TextField();
                this.ivjTextFieldValue.setName("TextFieldValue");
                ((Component) this.ivjTextFieldValue).setBackground(Color.white);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjTextFieldValue;
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.getFlatButtonNew().addActionListener(this);
        this.getFlatButtonDelete().addActionListener(this);
        this.getFlatButtonCancel().addActionListener(this);
        this.getFlatButtonOk().addActionListener(this);
        this.getFlatButtonClearCache().addActionListener(this);
        this.connPtoP1SetTarget();
        this.connPtoP2SetTarget();
        this.connPtoP3SetTarget();
        this.connPtoP4SetTarget();
    }

    private void initialize() {
        this.setName("AppletPropertiesEditor");
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        this.setSize(324, 400);
        this.add((Component) this.getDoubleBufferPanelMenu(), "West");
        this.add((Component) this.getTableViewPanel(), "Center");
        this.add((Component) this.getDoubleBufferPanelMenu3(), "South");
        this.initConnections();
    }

    public void updateSize(Dimension dimension) {
        JBLogger.log("NewSize: " + dimension);
        JBLogger.log("Parent: " + this.getParent().getSize() + this.getParent().getName());
        JBLogger.log("This: " + this.getSize() + this.getName());
        JBLogger.log("tableview: " + this.getTableViewProps().getSize());
    }
}

