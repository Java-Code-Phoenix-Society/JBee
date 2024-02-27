/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.JBPopupMenu;
import com.objectbox.runner.beans.ICellEditor;
import com.objectbox.runner.beans.TableView;
import com.objectbox.runner.gui.tree.Node;
import com.objectbox.runner.model.JBProperties;
import com.objectbox.runner.model.JBSecurityModel;
import com.objectbox.runner.model.PropTableModel;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

public class PropertyEditor
        implements ICellEditor,
        ItemListener,
        ActionListener,
        ComponentListener {
    Choice security_choice = new Choice();
    Choice windowtype_choice = new Choice();
    TextField default_editor = new TextField();
    TextField name_editor = new TextField();
    PropTableModel propertymodel = null;
    TableView tableview = null;

    public PropertyEditor() {
        this.default_editor.addActionListener(this);
        this.default_editor.addComponentListener(this);
        this.name_editor.addActionListener(this);
        this.name_editor.addComponentListener(this);
        this.security_choice.addItemListener(this);
        this.windowtype_choice.addItemListener(this);
        this.security_choice.addItem("High");
        this.security_choice.addItem("Medium");
        this.security_choice.addItem("Low");
        this.windowtype_choice.addItem("Small");
        this.windowtype_choice.addItem("Standard");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        try {
            if (this.tableview != null && this.propertymodel != null) {
                if (actionEvent.getSource() == this.default_editor) {
                    this.propertymodel.setCell(this.tableview.getCurrentCol(), this.tableview.getCurrentRow(), this.default_editor.getText());
                } else if (actionEvent.getSource() == this.name_editor) {
                    this.propertymodel.setCell(this.tableview.getCurrentCol(), this.tableview.getCurrentRow(), this.name_editor.getText());
                    JBManagerPanel jBManagerPanel = (JBManagerPanel) AppRegistry.getInstance().lookup("Manager");
                    Node node = jBManagerPanel.getTreeBasePublic().getSelectedNode();
                    if (node.getType().equals("App")) {
                        jBManagerPanel.getTreeBasePublic().getSelectedNode().setText(this.name_editor.getText());
                        jBManagerPanel.updateVisual();
                        Hashtable hashtable = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
                        JBPopupMenu jBPopupMenu = (JBPopupMenu) hashtable.get(node);
                        jBPopupMenu.setItemText(this.name_editor.getText(), node);
                    }
                }
            }
            this.tableview.remove((Component) actionEvent.getSource());
        } catch (Throwable throwable) {
            JBLogger.log("Exception i PropertyEditor::actionPerformed: " + throwable);
        }
    }

    public void componentHidden(ComponentEvent componentEvent) {
        if (this.tableview != null && this.propertymodel != null) {
            if (componentEvent.getSource() == this.default_editor) {
                this.propertymodel.setCell(this.tableview.getCurrentCol(), this.tableview.getCurrentRow(), this.default_editor.getText());
            } else if (componentEvent.getSource() == this.name_editor) {
                this.propertymodel.setCell(this.tableview.getCurrentCol(), this.tableview.getCurrentRow(), this.name_editor.getText());
                JBManagerPanel jBManagerPanel = (JBManagerPanel) AppRegistry.getInstance().lookup("Manager");
                if (jBManagerPanel.getTreeBasePublic().getSelectedNode().getType().equals("App")) {
                    jBManagerPanel.getTreeBasePublic().getSelectedNode().setText(this.name_editor.getText());
                    jBManagerPanel.updateVisual();
                }
            }
        }
        this.tableview.remove((Component) componentEvent.getSource());
    }

    public void componentMoved(ComponentEvent componentEvent) {
    }

    public void componentResized(ComponentEvent componentEvent) {
    }

    public void componentShown(ComponentEvent componentEvent) {
    }

    public boolean hasEditor(TableView tableView) {
        return tableView.getCurrentCol() != 0;
    }

    public void invokeEditor(TableView tableView, int n, int n2, int n3, int n4, int n5, int n6) {
        if (n == 0) {
            return;
        }
        this.tableview = tableView;
        this.propertymodel = (PropTableModel) tableView.getModel();
        String string = this.propertymodel.getCell(n, n2);
        if (this.propertymodel.getCell(2, n2).compareTo("jbprops") == 0) {
            String string2 = this.propertymodel.getCell(0, n2);
            if (string2.equals("Security")) {
                this.security_choice.setLocation(n3 - tableView.getTranslate().x, n4 - tableView.getTranslate().y);
                this.security_choice.setSize(new Dimension(n5 - n3, n6 - n4));
                tableView.add(this.security_choice);
                tableView.setActiveEditor(this.security_choice);
                this.security_choice.setVisible(true);
                this.security_choice.select(string);
                this.security_choice.requestFocus();
                return;
            }
            if (string2.equals("Window type")) {
                this.windowtype_choice.setLocation(n3 - tableView.getTranslate().x, n4 - tableView.getTranslate().y);
                this.windowtype_choice.setSize(new Dimension(n5 - n3, n6 - n4));
                tableView.add(this.windowtype_choice);
                tableView.setActiveEditor(this.windowtype_choice);
                this.windowtype_choice.setVisible(true);
                this.windowtype_choice.select(string);
                this.windowtype_choice.requestFocus();
                return;
            }
            if (string2.equals("Name")) {
                ((TextComponent) this.name_editor).setText(tableView.getModel().getCell(n, n2));
                this.name_editor.setLocation(n3 - tableView.getTranslate().x, n4 - tableView.getTranslate().y);
                this.name_editor.setSize(new Dimension(n5 - n3, n6 - n4));
                tableView.add(this.name_editor);
                tableView.setActiveEditor(this.name_editor);
                this.name_editor.setVisible(true);
                this.name_editor.selectAll();
                this.name_editor.requestFocus();
                this.name_editor.validate();
            } else {
                ((TextComponent) this.default_editor).setText(tableView.getModel().getCell(n, n2));
                this.default_editor.setLocation(n3 - tableView.getTranslate().x, n4 - tableView.getTranslate().y);
                this.default_editor.setSize(new Dimension(n5 - n3, n6 - n4));
                tableView.add(this.default_editor);
                tableView.setActiveEditor(this.default_editor);
                this.default_editor.setVisible(true);
                this.default_editor.selectAll();
                this.default_editor.requestFocus();
                this.default_editor.validate();
            }
        } else {
            ((TextComponent) this.default_editor).setText(tableView.getModel().getCell(n, n2));
            this.default_editor.setLocation(n3 - tableView.getTranslate().x, n4 - tableView.getTranslate().y);
            this.default_editor.setSize(new Dimension(n5 - n3, n6 - n4));
            tableView.add(this.default_editor);
            tableView.setActiveEditor(this.default_editor);
            this.default_editor.setVisible(true);
            this.default_editor.selectAll();
            this.default_editor.requestFocus();
            this.default_editor.validate();
        }
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (this.tableview != null && this.propertymodel != null) {
            Object object = itemEvent.getSource();
            if (object == this.security_choice) {
                this.propertymodel.setCell(this.tableview.getCurrentCol(), this.tableview.getCurrentRow(), this.security_choice.getSelectedItem());
                int n = this.security_choice.getSelectedIndex();
                PropTableModel propTableModel = (PropTableModel) this.tableview.getModel();
                JBProperties jBProperties = propTableModel.getData();
                String string = "-" + jBProperties.getProps().getProperty("codebase").hashCode() + jBProperties.getProps().getProperty("documentbase").hashCode() + jBProperties.getProps().getProperty("code").hashCode();
                JBee jBee = (JBee) AppRegistry.getInstance().lookup("JBee");
                JBManagerPanel jBManagerPanel = (JBManagerPanel) AppRegistry.getInstance().lookup("Manager");
                JBSecurityModel jBSecurityModel = JBSecurityModel.getSecurityModel(n + 1);
                jBee.setSecurity(string, jBSecurityModel, jBManagerPanel);
            } else if (object == this.windowtype_choice) {
                this.propertymodel.setCell(this.tableview.getCurrentCol(), this.tableview.getCurrentRow(), this.windowtype_choice.getSelectedItem());
            }
            this.tableview.remove((Component) object);
        }
    }
}

