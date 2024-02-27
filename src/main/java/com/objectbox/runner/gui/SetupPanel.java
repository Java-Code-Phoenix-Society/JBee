/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.FlatButton;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Properties;

public class SetupPanel
        extends Panel
        implements ActionListener {
    private Label ivjLabel1 = null;
    private Label ivjLabel2 = null;
    private Checkbox ivjCheckbox_bytecache = null;
    private Checkbox ivjCheckbox_imagecache = null;
    private Checkbox ivjCheckbox_version = null;
    private Label ivjLabel = null;
    private Label ivjLabel4 = null;
    private Panel ivjPanelSetupMain = null;
    private Properties props = null;
    private Hashtable secmodels = null;
    private Label ivjLabel3 = null;
    private Label ivjLabel5 = null;
    private Panel ivjPanelProxy = null;
    private TextField ivjTextFieldProxyPort = null;
    private TextField ivjTextFieldProxyServerAddress = null;
    private Checkbox ivjCheckboxProxy = null;
    private FlatButton ivjFlatButtonClearCachedResources = null;

    public SetupPanel() {
        this.initialize();
    }

    public SetupPanel(LayoutManager layoutManager) {
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
            serializable = new SetupPanel();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Panel");
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.getFlatButtonClearCachedResources()) {
            this.connEtoC1();
        }
    }

    public void button_proxy_ActionPerformed(ActionEvent actionEvent) {
    }

    public void buttonproxyok_ActionPerformed(ActionEvent actionEvent) {
    }

    public void checkbox_proxy_ItemStateChanged(ItemEvent itemEvent) {
    }

    private void connEtoC1() {
        try {
            this.flatButtonClearCachedResources_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void flatButtonClearCachedResources_Action() {
        try {
            String string = String.valueOf(JBee.getPreference("javabee_home")) + File.separator + "cache";
            File file = new File(string);
            if (file.isDirectory()) {
                String[] stringArray = file.list();
                int n = 0;
                while (n < stringArray.length) {
                    File file2 = new File(string, stringArray[n]);
                    if (file2.isFile()) {
                        if (file2.delete()) {
                            JBLogger.log("Deleted file " + file2 + " from " + string);
                        } else {
                            JBLogger.log("Could not delete " + file2 + " from " + string);
                        }
                    }
                    ++n;
                }
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in SetupPanel(clearing cache): " + throwable);
        }
    }

    private Checkbox getCheckbox_bytecache() {
        if (this.ivjCheckbox_bytecache == null) {
            try {
                this.ivjCheckbox_bytecache = new Checkbox();
                this.ivjCheckbox_bytecache.setName("Checkbox_bytecache");
                this.ivjCheckbox_bytecache.setLabel("");
                this.ivjCheckbox_bytecache.setState(true);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckbox_bytecache;
    }

    private Checkbox getCheckbox_imagecache() {
        if (this.ivjCheckbox_imagecache == null) {
            try {
                this.ivjCheckbox_imagecache = new Checkbox();
                this.ivjCheckbox_imagecache.setName("Checkbox_imagecache");
                this.ivjCheckbox_imagecache.setLabel("");
                this.ivjCheckbox_imagecache.setState(true);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckbox_imagecache;
    }

    private Checkbox getCheckbox_version() {
        if (this.ivjCheckbox_version == null) {
            try {
                this.ivjCheckbox_version = new Checkbox();
                this.ivjCheckbox_version.setName("Checkbox_version");
                this.ivjCheckbox_version.setLabel("");
                this.ivjCheckbox_version.setState(true);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckbox_version;
    }

    private Checkbox getCheckboxProxy() {
        if (this.ivjCheckboxProxy == null) {
            try {
                this.ivjCheckboxProxy = new Checkbox();
                this.ivjCheckboxProxy.setName("CheckboxProxy");
                this.ivjCheckboxProxy.setEnabled(true);
                this.ivjCheckboxProxy.setLabel("");
                this.ivjCheckboxProxy.setState(false);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCheckboxProxy;
    }

    private FlatButton getFlatButtonClearCachedResources() {
        if (this.ivjFlatButtonClearCachedResources == null) {
            try {
                this.ivjFlatButtonClearCachedResources = new FlatButton();
                this.ivjFlatButtonClearCachedResources.setName("FlatButtonClearCachedResources");
                this.ivjFlatButtonClearCachedResources.setLabel("Clear cached resources");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonClearCachedResources;
    }

    private Label getLabel() {
        if (this.ivjLabel == null) {
            try {
                this.ivjLabel = new Label();
                this.ivjLabel.setName("Label");
                this.ivjLabel.setText("Use bytecode cache");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabel;
    }

    private Label getLabel1() {
        if (this.ivjLabel1 == null) {
            try {
                this.ivjLabel1 = new Label();
                this.ivjLabel1.setName("Label1");
                this.ivjLabel1.setText("Use proxy server:");
                this.ivjLabel1.setEnabled(true);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabel1;
    }

    private Label getLabel2() {
        if (this.ivjLabel2 == null) {
            try {
                this.ivjLabel2 = new Label();
                this.ivjLabel2.setName("Label2");
                this.ivjLabel2.setText("Always check for new versions (jar's)");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabel2;
    }

    private Label getLabel3() {
        if (this.ivjLabel3 == null) {
            try {
                this.ivjLabel3 = new Label();
                this.ivjLabel3.setName("Label3");
                this.ivjLabel3.setFont(new Font("dialog", 0, 10));
                this.ivjLabel3.setText("Host");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabel3;
    }

    private Label getLabel4() {
        if (this.ivjLabel4 == null) {
            try {
                this.ivjLabel4 = new Label();
                this.ivjLabel4.setName("Label4");
                this.ivjLabel4.setText("Use image cache");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabel4;
    }

    private Label getLabel5() {
        if (this.ivjLabel5 == null) {
            try {
                this.ivjLabel5 = new Label();
                this.ivjLabel5.setName("Label5");
                this.ivjLabel5.setFont(new Font("dialog", 0, 10));
                this.ivjLabel5.setText("Port");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabel5;
    }

    private Panel getPanelProxy() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        if (this.ivjPanelProxy == null) {
            try {
                this.ivjPanelProxy = new Panel();
                this.ivjPanelProxy.setName("PanelProxy");
                this.ivjPanelProxy.setLayout(new GridBagLayout());
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.fill = 2;
                gridBagConstraints.anchor = 17;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.ipadx = 50;
                gridBagConstraints.insets = new Insets(0, 0, 0, 10);
                this.getPanelProxy().add((Component) this.getTextFieldProxyServerAddress(), gridBagConstraints);
                gridBagConstraints2.gridx = 1;
                gridBagConstraints2.gridy = 1;
                gridBagConstraints2.gridwidth = 2;
                gridBagConstraints2.gridheight = 1;
                gridBagConstraints2.fill = 2;
                gridBagConstraints2.anchor = 17;
                gridBagConstraints2.weightx = 0.0;
                gridBagConstraints2.weighty = 1.0;
                gridBagConstraints2.insets = new Insets(0, 0, 0, 5);
                this.getPanelProxy().add((Component) this.getTextFieldProxyPort(), gridBagConstraints2);
                gridBagConstraints3.gridx = 0;
                gridBagConstraints3.gridy = 0;
                gridBagConstraints3.gridwidth = 1;
                gridBagConstraints3.gridheight = 1;
                gridBagConstraints3.anchor = 17;
                gridBagConstraints3.weightx = 0.0;
                gridBagConstraints3.weighty = 0.0;
                gridBagConstraints3.insets = new Insets(0, 10, 0, 0);
                this.getPanelProxy().add((Component) this.getLabel3(), gridBagConstraints3);
                gridBagConstraints4.gridx = 1;
                gridBagConstraints4.gridy = 0;
                gridBagConstraints4.gridwidth = 1;
                gridBagConstraints4.gridheight = 1;
                gridBagConstraints4.anchor = 17;
                gridBagConstraints4.weightx = 0.0;
                gridBagConstraints4.weighty = 0.0;
                this.getPanelProxy().add((Component) this.getLabel5(), gridBagConstraints4);
                this.getPanelProxy().setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelProxy;
    }

    private Panel getPanelSetupMain() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        if (this.ivjPanelSetupMain == null) {
            try {
                this.ivjPanelSetupMain = new Panel();
                this.ivjPanelSetupMain.setName("PanelSetupMain");
                this.ivjPanelSetupMain.setLayout(new GridBagLayout());
                this.ivjPanelSetupMain.setBackground(SystemColor.control);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.fill = 2;
                gridBagConstraints.anchor = 11;
                gridBagConstraints.weightx = 0.0;
                gridBagConstraints.weighty = 0.0;
                gridBagConstraints.insets = new Insets(10, 20, 0, 0);
                this.getPanelSetupMain().add((Component) this.getLabel2(), gridBagConstraints);
                gridBagConstraints2.gridx = 1;
                gridBagConstraints2.gridy = 0;
                gridBagConstraints2.gridwidth = 1;
                gridBagConstraints2.gridheight = 1;
                gridBagConstraints2.anchor = 11;
                gridBagConstraints2.weightx = 0.0;
                gridBagConstraints2.weighty = 0.0;
                gridBagConstraints2.insets = new Insets(15, 0, 0, 0);
                this.getPanelSetupMain().add((Component) this.getCheckbox_version(), gridBagConstraints2);
                gridBagConstraints3.gridx = 0;
                gridBagConstraints3.gridy = 1;
                gridBagConstraints3.gridwidth = 1;
                gridBagConstraints3.gridheight = 1;
                gridBagConstraints3.fill = 2;
                gridBagConstraints3.anchor = 11;
                gridBagConstraints3.weightx = 0.0;
                gridBagConstraints3.weighty = 0.0;
                gridBagConstraints3.insets = new Insets(5, 20, 0, 0);
                this.getPanelSetupMain().add((Component) this.getLabel(), gridBagConstraints3);
                gridBagConstraints4.gridx = 1;
                gridBagConstraints4.gridy = 1;
                gridBagConstraints4.gridwidth = 1;
                gridBagConstraints4.gridheight = 1;
                gridBagConstraints4.anchor = 11;
                gridBagConstraints4.weightx = 0.0;
                gridBagConstraints4.weighty = 0.0;
                gridBagConstraints4.insets = new Insets(10, 0, 0, 0);
                this.getPanelSetupMain().add((Component) this.getCheckbox_bytecache(), gridBagConstraints4);
                gridBagConstraints5.gridx = 0;
                gridBagConstraints5.gridy = 2;
                gridBagConstraints5.gridwidth = 1;
                gridBagConstraints5.gridheight = 1;
                gridBagConstraints5.fill = 2;
                gridBagConstraints5.anchor = 11;
                gridBagConstraints5.weightx = 0.0;
                gridBagConstraints5.weighty = 0.0;
                gridBagConstraints5.insets = new Insets(5, 20, 0, 0);
                this.getPanelSetupMain().add((Component) this.getLabel4(), gridBagConstraints5);
                gridBagConstraints6.gridx = 1;
                gridBagConstraints6.gridy = 2;
                gridBagConstraints6.gridwidth = 1;
                gridBagConstraints6.gridheight = 1;
                gridBagConstraints6.anchor = 11;
                gridBagConstraints6.weightx = 0.0;
                gridBagConstraints6.weighty = 0.0;
                gridBagConstraints6.insets = new Insets(10, 0, 0, 0);
                this.getPanelSetupMain().add((Component) this.getCheckbox_imagecache(), gridBagConstraints6);
                gridBagConstraints7.gridx = 0;
                gridBagConstraints7.gridy = 4;
                gridBagConstraints7.gridwidth = 1;
                gridBagConstraints7.gridheight = 1;
                gridBagConstraints7.fill = 2;
                gridBagConstraints7.anchor = 11;
                gridBagConstraints7.weightx = 0.0;
                gridBagConstraints7.weighty = 0.0;
                gridBagConstraints7.insets = new Insets(5, 20, 0, 0);
                this.getPanelSetupMain().add((Component) this.getLabel1(), gridBagConstraints7);
                gridBagConstraints8.gridx = 1;
                gridBagConstraints8.gridy = 4;
                gridBagConstraints8.gridwidth = 1;
                gridBagConstraints8.gridheight = 1;
                gridBagConstraints8.anchor = 11;
                gridBagConstraints8.weightx = 0.0;
                gridBagConstraints8.weighty = 0.0;
                gridBagConstraints8.insets = new Insets(10, 0, 0, 0);
                this.getPanelSetupMain().add((Component) this.getCheckboxProxy(), gridBagConstraints8);
                gridBagConstraints9.gridx = 0;
                gridBagConstraints9.gridy = 5;
                gridBagConstraints9.gridwidth = 2;
                gridBagConstraints9.gridheight = 1;
                gridBagConstraints9.fill = 2;
                gridBagConstraints9.anchor = 11;
                gridBagConstraints9.weightx = 0.0;
                gridBagConstraints9.weighty = 0.0;
                gridBagConstraints9.insets = new Insets(5, 20, 0, 0);
                this.getPanelSetupMain().add((Component) this.getPanelProxy(), gridBagConstraints9);
                gridBagConstraints10.gridx = 0;
                gridBagConstraints10.gridy = 3;
                gridBagConstraints10.gridwidth = 2;
                gridBagConstraints10.gridheight = 1;
                gridBagConstraints10.fill = 2;
                gridBagConstraints10.anchor = 10;
                gridBagConstraints10.weightx = 1.0;
                gridBagConstraints10.weighty = 0.0;
                gridBagConstraints10.ipady = -5;
                gridBagConstraints10.insets = new Insets(5, 60, 10, 60);
                this.getPanelSetupMain().add((Component) this.getFlatButtonClearCachedResources(), gridBagConstraints10);
                this.getPanelSetupMain().setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelSetupMain;
    }

    public Properties getProperties() {
        return this.props;
    }

    public void setProperties(Properties properties) {
        this.props = properties;
        this.updateProperties();
    }

    public Hashtable getSecurityModels() {
        return this.secmodels;
    }

    public void setSecurityModels(Hashtable hashtable) {
        this.secmodels = hashtable;
        this.updateSecurity();
    }

    private TextField getTextFieldProxyPort() {
        if (this.ivjTextFieldProxyPort == null) {
            try {
                this.ivjTextFieldProxyPort = new TextField();
                this.ivjTextFieldProxyPort.setName("TextFieldProxyPort");
                this.ivjTextFieldProxyPort.setColumns(3);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjTextFieldProxyPort;
    }

    private TextField getTextFieldProxyServerAddress() {
        if (this.ivjTextFieldProxyServerAddress == null) {
            try {
                this.ivjTextFieldProxyServerAddress = new TextField();
                this.ivjTextFieldProxyServerAddress.setName("TextFieldProxyServerAddress");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjTextFieldProxyServerAddress;
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.getFlatButtonClearCachedResources().addActionListener(this);
    }

    private void initialize() {
        this.setName("SetupPanel");
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(182, 182, 200));
        this.setSize(298, 246);
        this.add((Component) this.getPanelSetupMain(), "Center");
        this.initConnections();
        this.setBackground(JBee.appcolor);
    }

    public void setProperties() {
        if (this.getCheckbox_version().getState()) {
            ((Hashtable) this.props).put("checkversion", "true");
        } else {
            ((Hashtable) this.props).put("checkversion", "false");
        }
        if (this.getCheckbox_bytecache().getState()) {
            ((Hashtable) this.props).put("usebytecodecache", "true");
        } else {
            ((Hashtable) this.props).put("usebytecodecache", "false");
        }
        if (this.getCheckbox_imagecache().getState()) {
            ((Hashtable) this.props).put("useimagecache", "true");
        } else {
            ((Hashtable) this.props).put("useimagecache", "false");
        }
        if (this.getCheckboxProxy().getState()) {
            ((Hashtable) this.props).put("useproxy", "true");
        } else {
            ((Hashtable) this.props).put("useproxy", "false");
        }
        ((Hashtable) this.props).put("proxyserver", this.getTextFieldProxyServerAddress().getText());
        ((Hashtable) this.props).put("proxyport", this.getTextFieldProxyPort().getText());
    }

    public void updateProperties() {
        String string = this.props.getProperty("checkversion");
        String string2 = this.props.getProperty("usebytecodecache");
        String string3 = this.props.getProperty("useimagecache");
        String string4 = this.props.getProperty("useproxy");
        String string5 = this.props.getProperty("proxyserver");
        String string6 = this.props.getProperty("proxyport");
        if (string4 == null || string4.compareTo("false") == 0) {
            this.getCheckboxProxy().setState(false);
        } else {
            this.getCheckboxProxy().setState(true);
        }
        if (string5 != null) {
            ((TextComponent) this.getTextFieldProxyServerAddress()).setText(string5);
        }
        if (string6 != null) {
            ((TextComponent) this.getTextFieldProxyPort()).setText(string6);
        }
        if (string == null || string.compareTo("true") == 0) {
            this.getCheckbox_version().setState(true);
        } else {
            this.getCheckbox_version().setState(false);
        }
        if (string2 == null || string2.compareTo("true") == 0) {
            this.getCheckbox_bytecache().setState(true);
        } else {
            this.getCheckbox_bytecache().setState(false);
        }
        if (string3 == null || string3.compareTo("true") == 0) {
            this.getCheckbox_imagecache().setState(true);
        } else {
            this.getCheckbox_imagecache().setState(false);
        }
    }

    public void updateSecurity() {
    }
}

