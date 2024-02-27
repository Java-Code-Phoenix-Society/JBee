package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.DoubleBufferPanel;
import com.objectbox.gui.lwcomp.FlatButton;
import com.objectbox.gui.lwcomp.LWSeparator;
import com.objectbox.runner.beans.TableView;
import com.objectbox.runner.gui.tree.Node;
import com.objectbox.runner.model.*;
import com.objectbox.runner.util.AppletTagParser;
import com.objectbox.runner.util.Hyperlink;
import com.objectbox.runner.util.JBLogger;
import com.roguewave.blend.progress.v2_0.ProgressBar;
import com.sun.java.swing.BoxLayout;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class JBSearchPanel
        extends Panel
        implements ActionListener,
        KeyListener {
    protected transient SavePropertiesListener aSavePropertiesListener = null;
    protected ThreadGroup threadgroup = new ThreadGroup("testrun");
    private NewAppTableModel ivjNewAppTableModel = null;
    private TableView ivjNewAppTableView = null;
    private FlatButton ivjFlatButtonClear = null;
    private FlatButton ivjFlatButtonSaveApp = null;
    private FlatButton ivjFlatButtonStart = null;
    private FlatButton ivjFlatButtonStop = null;
    private TextField ivjTextFieldURL = null;
    private AppletTagParser atp = null;
    private FlatButton ivjFlatButtonTest = null;
    private Panel ivjPanelAddress = null;
    private Choice ivjChoiceSearchURL = null;
    private Hashtable siteHash = null;
    private BorderLayout ivjSearchPanelBorderLayout = null;
    private Panel ivjPanelStatus = null;
    private ProgressBar ivjStatusbar = null;
    private Label ivjLabelStatus = null;
    private int locallevel = 10;
    private int remotelevel = 10;
    private FlatButton ivjFlatButtonOpenBrowser = null;
    private FlatButton ivjFlatButtonSaveAll = null;
    private Vector historyModel = null;
    private int maxHistoryLength = 10;
    private int currentHistoryPosition = 0;
    private int searchnr = 0;
    private Panel ivjPanelSouth = null;
    private Panel ivjPanelCenter = null;
    private Panel ivjPanelMenuCarddeck = null;
    private Panel ivjPanelScroll = null;
    private Label ivjLabelFolder = null;
    private Label ivjLabelURL = null;
    private CardLayout ivjcardlayout = null;
    private FlatButton ivjFlatButtonManage = null;
    private Label ivjLabelFolderTx = null;
    private FlatButton ivjFlatButtonCopy = null;
    private FlatButton ivjFlatButtonCut = null;
    private FlatButton ivjFlatButtonDelete = null;
    private FlatButton ivjFlatButtonNewfolder = null;
    private FlatButton ivjFlatButtonPaste = null;
    private FlatButton ivjFlatButtonProperties = null;
    private FlatButton ivjFlatButtonRun = null;
    private FlatButton ivjFlatButtonSwitchToSearch = null;
    private FlatButton ivjFlatButtonUndo = null;
    private JBManagerPanel ivjJBManagerPanel = null;
    private CardLayout ivjCardLayoutCenter = null;
    private DoubleBufferPanel ivjDoubleBufferPanelMenuManage = null;
    private DoubleBufferPanel ivjDoubleBufferPanelMenuSearch = null;
    private DoubleBufferPanel ivjPanelLogo = null;
    private JBImageSpinner ivjJBImageSpinner1 = null;
    private LWSeparator ivjLWSeparator1 = null;
    private LWSeparator ivjLWSeparator2 = null;
    private LWSeparator ivjLWSeparator3 = null;
    private Object adspot_applet = null;
    private FlatButton ivjFlatButtonBrowser = null;
    private LWSeparator ivjLWSeparator = null;

    public JBSearchPanel() {
        this.initialize();
    }

    public JBSearchPanel(LayoutManager layoutManager) {
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
            serializable = new JBSearchPanel();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Panel");
        }
    }

    static TextField access$getTextFieldURL(JBSearchPanel jBSearchPanel) {
        return jBSearchPanel.getTextFieldURL();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.getFlatButtonStart()) {
            this.connEtoC1(actionEvent);
        }
        if (actionEvent.getSource() == this.getTextFieldURL()) {
            this.connEtoC2(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonClear()) {
            this.connEtoC3();
        }
        if (actionEvent.getSource() == this.getFlatButtonStop()) {
            this.connEtoC4();
        }
        if (actionEvent.getSource() == this.getFlatButtonSaveApp()) {
            this.connEtoC6(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonTest()) {
            this.connEtoC8(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonOpenBrowser()) {
            this.connEtoC10(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonSaveAll()) {
            this.connEtoC11(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonManage()) {
            this.connEtoM1(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonSwitchToSearch()) {
            this.connEtoM2(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonManage()) {
            this.connEtoM3(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonSwitchToSearch()) {
            this.connEtoM4(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonNewfolder()) {
            this.connEtoC5(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonNewfolder()) {
            this.connEtoC7(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonProperties()) {
            this.connEtoC9(actionEvent);
        }
        if (actionEvent.getSource() == this.getFlatButtonRun()) {
            this.connEtoC12();
        }
        if (actionEvent.getSource() == this.getFlatButtonCut()) {
            this.connEtoC13();
        }
        if (actionEvent.getSource() == this.getFlatButtonCopy()) {
            this.connEtoC15();
        }
        if (actionEvent.getSource() == this.getFlatButtonPaste()) {
            this.connEtoC16();
        }
        if (actionEvent.getSource() == this.getFlatButtonDelete()) {
            this.connEtoC17();
        }
        if (actionEvent.getSource() == this.getFlatButtonUndo()) {
            this.connEtoC18();
        }
        if (actionEvent.getSource() == this.getFlatButtonBrowser()) {
            this.connEtoC19();
        }
    }

    public void addSavePropertiesListener(SavePropertiesListener savePropertiesListener) {
        this.aSavePropertiesListener = SavePropertiesEventMulticaster.add(this.aSavePropertiesListener, savePropertiesListener);
    }

    private void connEtoC1(ActionEvent actionEvent) {
        try {
            this.flatButtonStart_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC10(ActionEvent actionEvent) {
        try {
            this.flatButtonOpenBrowser_ActionPerformed();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC11(ActionEvent actionEvent) {
        try {
            this.flatButtonSaveAll_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC12() {
        try {
            this.flatButtonRun_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC13() {
        try {
            this.flatButtonCut_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC14(KeyEvent keyEvent) {
        try {
            this.textFieldURL_KeyPressed(keyEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC15() {
        try {
            this.flatButtonCopy_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC16() {
        try {
            this.flatButtonPaste_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC17() {
        try {
            this.flatButtonDelete_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC18() {
        try {
            this.flatButtonUndo_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC19() {
        try {
            this.flatButtonBrowser_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC2(ActionEvent actionEvent) {
        try {
            this.flatButtonStart_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC3() {
        try {
            this.flatButtonClear_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC4() {
        try {
            this.flatButtonStop_Action();
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC5(ActionEvent actionEvent) {
        try {
            this.flatButtonNewfolder_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC6(ActionEvent actionEvent) {
        try {
            this.flatButtonSaveApp_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC7(ActionEvent actionEvent) {
        try {
            this.flatButtonNewfolder_ActionPerformed1(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC8(ActionEvent actionEvent) {
        try {
            this.flatButtonTest_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC9(ActionEvent actionEvent) {
        try {
            this.flatButtonProperties_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoM1(ActionEvent actionEvent) {
        try {
            this.getcardlayout().next(this.getPanelMenuCarddeck());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoM2(ActionEvent actionEvent) {
        try {
            this.getcardlayout().next(this.getPanelMenuCarddeck());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoM3(ActionEvent actionEvent) {
        try {
            this.getCardLayoutCenter().next(this.getPanelCenter());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoM4(ActionEvent actionEvent) {
        try {
            this.getCardLayoutCenter().next(this.getPanelCenter());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connPtoP1SetTarget() {
        try {
            this.getNewAppTableView().setModel(this.getNewAppTableModel());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connPtoP2SetTarget() {
        try {
            this.getPanelMenuCarddeck().setLayout(this.getcardlayout());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connPtoP3SetTarget() {
        try {
            this.getPanelCenter().setLayout(this.getCardLayoutCenter());
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    protected void fireOnSaveProperties(SavePropertiesEvent savePropertiesEvent) {
        if (this.aSavePropertiesListener == null) {
            return;
        }
        this.aSavePropertiesListener.onSaveProperties(savePropertiesEvent);
    }

    public void flatButtonBrowser_Action() {
        Object object = AppRegistry.getInstance().lookup("Manager");
        if (object != null) {
            JBManagerPanel jBManagerPanel = (JBManagerPanel) object;
            jBManagerPanel.runCommand("Browser");
        }
    }

    public void flatButtonClear_Action() {
        this.getNewAppTableModel().setData(new Vector());
        this.getNewAppTableView().setModel(this.getNewAppTableModel());
        this.getNewAppTableView().setCurrent(0, this.getNewAppTableView().getNumberOfRows());
        this.getNewAppTableView().repaint();
    }

    public void flatButtonClose_ActionPerformed(ActionEvent actionEvent) {
        Container container = this.getParent();
        while (container != null && !(container instanceof Frame)) {
            container = container.getParent();
        }
        ((Component) ((Frame) container)).setVisible(false);
    }

    public void flatButtonCopy_Action() {
        Object object = AppRegistry.getInstance().lookup("Manager");
        if (object != null) {
            JBManagerPanel jBManagerPanel = (JBManagerPanel) object;
            jBManagerPanel.runCommand("Copy");
        }
    }

    public void flatButtonCut_Action() {
        Object object = AppRegistry.getInstance().lookup("Manager");
        if (object != null) {
            JBManagerPanel jBManagerPanel = (JBManagerPanel) object;
            jBManagerPanel.runCommand("Cut");
        }
    }

    public void flatButtonDelete_Action() {
        Object object = AppRegistry.getInstance().lookup("Manager");
        if (object != null) {
            JBManagerPanel jBManagerPanel = (JBManagerPanel) object;
            jBManagerPanel.runCommand("Delete");
        }
    }

    public void flatButtonNewfolder_ActionPerformed(ActionEvent actionEvent) {
        Object object = AppRegistry.getInstance().lookup("Manager");
        if (object != null) {
            JBManagerPanel jBManagerPanel = (JBManagerPanel) object;
            jBManagerPanel.runCommand("NewFolder");
        }
    }

    public void flatButtonNewfolder_ActionPerformed1(ActionEvent actionEvent) {
        try {
            JBManagerPanel jBManagerPanel = (JBManagerPanel) AppRegistry.getInstance().lookup("Manager");
            jBManagerPanel.updateVisual();
        } catch (Throwable throwable) {
            JBLogger.log("flatButtonNewfolder_ActionPerformed: " + throwable);
        }
    }

    public void flatButtonOpenBrowser_ActionPerformed() {
        try {
            int n = this.getNewAppTableView().getCurrent().y;
            JBProperties jBProperties = this.getNewAppTableModel().getProperty(n);
            JBee.displayURL(String.valueOf((String) ((Hashtable) jBProperties.getProps()).get("documentbase")) + (String) ((Hashtable) jBProperties.getProps()).get("webpage"));
            return;
        } catch (Exception exception) {
            JBLogger.log("ex i flatButtonOpenBrowser " + exception);
            return;
        }
    }

    public void flatButtonPaste_Action() {
        Object object = AppRegistry.getInstance().lookup("Manager");
        if (object != null) {
            JBManagerPanel jBManagerPanel = (JBManagerPanel) object;
            jBManagerPanel.runCommand("Paste");
        }
    }

    public void flatButtonPause_Action() {
    }

    public void flatButtonProperties_ActionPerformed(ActionEvent actionEvent) {
        Object object = AppRegistry.getInstance().lookup("Manager");
        if (object != null) {
            JBManagerPanel jBManagerPanel = (JBManagerPanel) object;
            jBManagerPanel.runCommand("Prefere");
        }
    }

    public void flatButtonRun_Action() {
        Object object = AppRegistry.getInstance().lookup("Manager");
        if (object != null) {
            JBManagerPanel jBManagerPanel = (JBManagerPanel) object;
            jBManagerPanel.runCommand("Run");
        }
    }

    public void flatButtonSaveAll_ActionPerformed(ActionEvent actionEvent) {
        NewAppTableModel newAppTableModel = this.getNewAppTableModel();
        synchronized (newAppTableModel) {
            try {
                Object object = AppRegistry.getInstance().lookup("Manager");
                if (object == null) {
                    return;
                }
                JBManagerPanel jBManagerPanel = (JBManagerPanel) object;
                int n = this.getNewAppTableModel().getNumberOfRows();
                Vector vector = new Vector();
                int n2 = 1;
                while (n2 < n) {
                    int n3 = n2;
                    JBProperties jBProperties = this.getNewAppTableModel().getProperty(n3);
                    String string = (String) ((Hashtable) jBProperties.getProps()).get("code");
                    if (string.endsWith(".class")) {
                        string = string.substring(0, string.lastIndexOf("."));
                    }
                    if (string.indexOf(".") > 0) {
                        string = string.substring(string.lastIndexOf(".") + 1);
                    }
                    Vector<Object> vector2 = new Vector<Object>();
                    vector2.addElement(string);
                    vector2.addElement(new JBAppletProperties(jBProperties));
                    vector.addElement(vector2);
                    ++n2;
                }
                jBManagerPanel.installAppList(vector);
            } catch (Throwable throwable) {
                JBLogger.log("Exception i saveAllAction..." + throwable);
            }
        }
    }

    public void flatButtonSaveApp_ActionPerformed(ActionEvent var1) {
        try {
            synchronized (this.getNewAppTableModel()) {
                try {
                    Object var3 = AppRegistry.getInstance().lookup("Manager");
                    if (var3 == null) {
                        return;
                    }

                    JBManagerPanel var4 = (JBManagerPanel) var3;
                    this.getNewAppTableModel().getNumberOfRows();
                    Vector var5 = new Vector();
                    Enumeration var6 = this.getNewAppTableView().getRowSelectionEnumeration();

                    while (var6.hasMoreElements()) {
                        Point var7 = (Point) var6.nextElement();
                        JBProperties var8 = this.getNewAppTableModel().getProperty(var7.y);
                        String var9 = (String) var8.getProps().get("code");
                        if (var9.endsWith(".class")) {
                            var9 = var9.substring(0, var9.lastIndexOf("."));
                        }

                        if (var9.indexOf(".") > 0) {
                            var9 = var9.substring(var9.lastIndexOf(".") + 1);
                        }

                        Vector var10 = new Vector();
                        var10.addElement(var9);
                        var10.addElement(new JBAppletProperties(var8));
                        var5.addElement(var10);
                    }

                    if (var5.size() > 0) {
                        var4.installAppList(var5);
                        return;
                    }
                } catch (Throwable var11) {
                    JBLogger.log("Exception i saveAllAction..." + var11);
                }
            }

            int var2 = this.getNewAppTableView().getCurrent().y;
            JBProperties var14 = this.getNewAppTableModel().getProperty(var2);
            String var15 = (String) var14.getProps().get("code");
            if (var15.endsWith(".class")) {
                var15 = var15.substring(0, var15.lastIndexOf("."));
            }

            if (var15.indexOf(".") > 0) {
                var15 = var15.substring(var15.lastIndexOf(".") + 1);
            }

            Object var16 = AppRegistry.getInstance().lookup("Manager");
            if (var16 != null) {
                JBManagerPanel var17 = (JBManagerPanel) var16;
                var17.installApp(var15, new JBAppletProperties(var14));
            }
        } catch (Throwable var13) {
            JBLogger.log("Exception i saveAppAction..." + var13);
        }

    }

    public void flatButtonStart_ActionPerformed(ActionEvent actionEvent) {
        this.flatButtonStop_Action();
//        if (!this.getJAdPanel().isAppletLoaded()) {
//            this.getJAdPanel().loadApplet();
//        }
        this.getJBImageSpinner1().toggle(true);
        boolean bl = true;
        String string = this.getTextFieldURL().getText();
        int n = this.historyModel.size();
        int n2 = 0;
        while (n2 < n) {
            if (this.historyModel.elementAt(n2).toString().equals(string)) {
                bl = false;
                break;
            }
            ++n2;
        }
        if (bl) {
            this.historyModel.insertElementAt(string, 0);
        }
        if (this.historyModel.size() > this.maxHistoryLength) {
            this.historyModel.removeElementAt(this.historyModel.size() - 1);
        }
        try {
            Object object;
            n2 = 0;
            URL uRL = null;
            this.updateStatus(0, 0, 0, "");
            if (string.toLowerCase().startsWith("http://") || string.toLowerCase().startsWith("file:")) {
                uRL = new URL(string);
            } else if (string.indexOf(".") > 0) {
                uRL = new URL("http://" + string);
            } else {
                object = "http://www.altavista.com/cgi-bin/query?text=yes&q=applet:";
                string = string.replace(' ', '+');
                uRL = new URL(String.valueOf(object) + string);
                n2 = 1;
            }
            ++this.searchnr;
            object = new ThreadGroup("parsegroup" + this.searchnr);
            ((ThreadGroup) object).setMaxPriority(1);
            this.atp = new AppletTagParser((ThreadGroup) object, "start");
            if (n2 != 0) {
                this.atp.setPrefs(true, true, 5, 5, uRL);
            } else {
                if (this.getChoiceSearchURL().getSelectedItem() == "this page") {
                    this.atp.setPrefs(false, false, 1, 0, uRL);
                }
                if (this.getChoiceSearchURL().getSelectedItem() == "this site") {
                    this.atp.setPrefs(true, false, this.locallevel, 0, uRL);
                }
                if (this.getChoiceSearchURL().getSelectedItem() == "all links") {
                    this.atp.setPrefs(true, true, this.locallevel, this.remotelevel, uRL);
                }
            }
            Hyperlink hyperlink = new Hyperlink();
            hyperlink.url = uRL;
            this.atp.setStartURL(hyperlink);
            this.atp.setHandle(this);
            this.atp.start();
        } catch (Exception exception) {
            JBLogger.log("Ex:  " + exception);
        }
    }

    public void flatButtonStop_Action() {
        this.atp = new AppletTagParser();
        this.atp.kill();
        this.getJBImageSpinner1().toggle(false);
    }

    public void flatButtonTest_ActionPerformed(ActionEvent actionEvent) {
        int n = this.getNewAppTableView().getCurrent().y;
        JBProperties jBProperties = this.getNewAppTableModel().getProperty(n);
        JBAppletHolder jBAppletHolder = new JBAppletHolder();
        jBAppletHolder.setProperties(jBProperties);
        jBAppletHolder.setBeanRunner(new BeanRunner());
        ((JBHolder) jBAppletHolder).run(this.threadgroup);
    }

    public void flatButtonUndo_Action() {
        Object object = AppRegistry.getInstance().lookup("Manager");
        if (object != null) {
            JBManagerPanel jBManagerPanel = (JBManagerPanel) object;
            jBManagerPanel.runCommand("Undo");
        }
    }

    private CardLayout getcardlayout() {
        if (this.ivjcardlayout == null) {
            try {
                this.ivjcardlayout = new CardLayout();
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjcardlayout;
    }

    private CardLayout getCardLayoutCenter() {
        if (this.ivjCardLayoutCenter == null) {
            try {
                this.ivjCardLayoutCenter = new CardLayout();
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjCardLayoutCenter;
    }

    private Choice getChoiceSearchURL() {
        if (this.ivjChoiceSearchURL == null) {
            try {
                this.ivjChoiceSearchURL = new Choice();
                this.ivjChoiceSearchURL.setName("ChoiceSearchURL");
                this.ivjChoiceSearchURL.add("this page");
                this.ivjChoiceSearchURL.add("this site");
                this.ivjChoiceSearchURL.add("all links");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjChoiceSearchURL;
    }

    private DoubleBufferPanel getDoubleBufferPanelMenuManage() {
        if (this.ivjDoubleBufferPanelMenuManage == null) {
            try {
                this.ivjDoubleBufferPanelMenuManage = new DoubleBufferPanel();
                this.ivjDoubleBufferPanelMenuManage.setName("DoubleBufferPanelMenuManage");
                this.ivjDoubleBufferPanelMenuManage.setLayout(new BoxLayout(this.getDoubleBufferPanelMenuManage(), 0));
                this.ivjDoubleBufferPanelMenuManage.setBackground(SystemColor.control);
                this.ivjDoubleBufferPanelMenuManage.setHasframe(false);
                this.getDoubleBufferPanelMenuManage().add((Component) this.getFlatButtonSwitchToSearch(), this.getFlatButtonSwitchToSearch().getName());
                this.getDoubleBufferPanelMenuManage().add((Component) this.getFlatButtonNewfolder(), this.getFlatButtonNewfolder().getName());
                this.getDoubleBufferPanelMenuManage().add((Component) this.getFlatButtonProperties(), this.getFlatButtonProperties().getName());
                this.getDoubleBufferPanelMenuManage().add((Component) this.getFlatButtonRun(), this.getFlatButtonRun().getName());
                this.getDoubleBufferPanelMenuManage().add((Component) this.getFlatButtonBrowser(), this.getFlatButtonBrowser().getName());
                this.ivjDoubleBufferPanelMenuManage.add(this.getLWSeparator1());
                this.getDoubleBufferPanelMenuManage().add((Component) this.getFlatButtonCut(), this.getFlatButtonCut().getName());
                this.getDoubleBufferPanelMenuManage().add((Component) this.getFlatButtonCopy(), this.getFlatButtonCopy().getName());
                this.getDoubleBufferPanelMenuManage().add((Component) this.getFlatButtonPaste(), this.getFlatButtonPaste().getName());
                this.getDoubleBufferPanelMenuManage().add((Component) this.getFlatButtonDelete(), this.getFlatButtonDelete().getName());
                this.getDoubleBufferPanelMenuManage().add((Component) this.getFlatButtonUndo(), this.getFlatButtonUndo().getName());
                this.ivjDoubleBufferPanelMenuManage.setBackground(JBee.appcolor);
                this.ivjDoubleBufferPanelMenuManage.setHasframe(false);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjDoubleBufferPanelMenuManage;
    }

    private DoubleBufferPanel getDoubleBufferPanelMenuSearch() {
        if (this.ivjDoubleBufferPanelMenuSearch == null) {
            try {
                this.ivjDoubleBufferPanelMenuSearch = new DoubleBufferPanel();
                this.ivjDoubleBufferPanelMenuSearch.setName("DoubleBufferPanelMenuSearch");
                this.ivjDoubleBufferPanelMenuSearch.setLayout(new BoxLayout(this.getDoubleBufferPanelMenuSearch(), 0));
                this.ivjDoubleBufferPanelMenuSearch.setBackground(SystemColor.control);
                this.ivjDoubleBufferPanelMenuSearch.setHasframe(false);
                this.getDoubleBufferPanelMenuSearch().add((Component) this.getFlatButtonManage(), this.getFlatButtonManage().getName());
                this.getDoubleBufferPanelMenuSearch().add((Component) this.getFlatButtonStop(), this.getFlatButtonStop().getName());
                this.getDoubleBufferPanelMenuSearch().add((Component) this.getFlatButtonStart(), this.getFlatButtonStart().getName());
                this.getDoubleBufferPanelMenuSearch().add((Component) this.getFlatButtonTest(), this.getFlatButtonTest().getName());
                this.getDoubleBufferPanelMenuSearch().add((Component) this.getFlatButtonSaveApp(), this.getFlatButtonSaveApp().getName());
                this.getDoubleBufferPanelMenuSearch().add((Component) this.getFlatButtonSaveAll(), this.getFlatButtonSaveAll().getName());
                this.getDoubleBufferPanelMenuSearch().add((Component) this.getFlatButtonClear(), this.getFlatButtonClear().getName());
                this.getDoubleBufferPanelMenuSearch().add((Component) this.getFlatButtonOpenBrowser(), this.getFlatButtonOpenBrowser().getName());
                this.ivjDoubleBufferPanelMenuSearch.setHasframe(false);
                this.ivjDoubleBufferPanelMenuSearch.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjDoubleBufferPanelMenuSearch;
    }

    private FlatButton getFlatButtonBrowser() {
        if (this.ivjFlatButtonBrowser == null) {
            try {
                this.ivjFlatButtonBrowser = new FlatButton();
                this.ivjFlatButtonBrowser.setName("FlatButtonBrowser");
                this.ivjFlatButtonBrowser.setHasborder(false);
                this.ivjFlatButtonBrowser.setLabel("Browser");
                this.ivjFlatButtonBrowser.setFont(new Font("dialog", 0, 10));
                this.ivjFlatButtonBrowser.setFixedsize(new Dimension(60, 50));
                this.ivjFlatButtonBrowser.setEnabled(true);
                AppRegistry.getInstance().put("FlatButtonBrowser", this.ivjFlatButtonBrowser);
                this.ivjFlatButtonBrowser.setImageResource("/images/world.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonBrowser;
    }

    private FlatButton getFlatButtonClear() {
        if (this.ivjFlatButtonClear == null) {
            try {
                this.ivjFlatButtonClear = new FlatButton();
                this.ivjFlatButtonClear.setName("FlatButtonClear");
                this.ivjFlatButtonClear.setFixedsize(new Dimension(62, 50));
                this.ivjFlatButtonClear.setLabel("Clear list");
                this.ivjFlatButtonClear.setImageResource("/images/clear.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonClear;
    }

    private FlatButton getFlatButtonCopy() {
        if (this.ivjFlatButtonCopy == null) {
            try {
                this.ivjFlatButtonCopy = new FlatButton();
                this.ivjFlatButtonCopy.setName("FlatButtonCopy");
                this.ivjFlatButtonCopy.setFont(new Font("dialog", 0, 10));
                this.ivjFlatButtonCopy.setFixedsize(new Dimension(43, 50));
                this.ivjFlatButtonCopy.setEnabled(true);
                this.ivjFlatButtonCopy.setLabel("Copy");
                AppRegistry.getInstance().put("FlatButtonCopy", this.ivjFlatButtonCopy);
                this.ivjFlatButtonCopy.setImageResource("/images/copy.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonCopy;
    }

    private FlatButton getFlatButtonCut() {
        if (this.ivjFlatButtonCut == null) {
            try {
                this.ivjFlatButtonCut = new FlatButton();
                this.ivjFlatButtonCut.setName("FlatButtonCut");
                this.ivjFlatButtonCut.setFont(new Font("dialog", 0, 10));
                this.ivjFlatButtonCut.setFixedsize(new Dimension(43, 50));
                this.ivjFlatButtonCut.setEnabled(true);
                this.ivjFlatButtonCut.setLabel("Cut");
                AppRegistry.getInstance().put("FlatButtonCut", this.ivjFlatButtonCut);
                this.ivjFlatButtonCut.setImageResource("/images/cut.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonCut;
    }

    private FlatButton getFlatButtonDelete() {
        if (this.ivjFlatButtonDelete == null) {
            try {
                this.ivjFlatButtonDelete = new FlatButton();
                this.ivjFlatButtonDelete.setName("FlatButtonDelete");
                this.ivjFlatButtonDelete.setFont(new Font("dialog", 0, 10));
                this.ivjFlatButtonDelete.setFixedsize(new Dimension(43, 50));
                this.ivjFlatButtonDelete.setEnabled(true);
                this.ivjFlatButtonDelete.setLabel("Delete");
                AppRegistry.getInstance().put("FlatButtonDelete", this.ivjFlatButtonDelete);
                this.ivjFlatButtonDelete.setImageResource("/images/delete.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonDelete;
    }

    private FlatButton getFlatButtonManage() {
        if (this.ivjFlatButtonManage == null) {
            try {
                this.ivjFlatButtonManage = new FlatButton();
                this.ivjFlatButtonManage.setName("FlatButtonManage");
                this.ivjFlatButtonManage.setFixedsize(new Dimension(62, 50));
                this.ivjFlatButtonManage.setEnabled(true);
                this.ivjFlatButtonManage.setLabel("Admin");
                this.ivjFlatButtonManage.setHasborder(false);
                this.ivjFlatButtonManage.setImageResource("/images/admin.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonManage;
    }

    private FlatButton getFlatButtonNewfolder() {
        if (this.ivjFlatButtonNewfolder == null) {
            try {
                this.ivjFlatButtonNewfolder = new FlatButton();
                this.ivjFlatButtonNewfolder.setName("FlatButtonNewfolder");
                this.ivjFlatButtonNewfolder.setFont(new Font("dialog", 0, 10));
                this.ivjFlatButtonNewfolder.setFixedsize(new Dimension(70, 50));
                this.ivjFlatButtonNewfolder.setEnabled(true);
                this.ivjFlatButtonNewfolder.setLabel("New Folder");
                AppRegistry.getInstance().put("FlatButtonNewFolder", this.ivjFlatButtonNewfolder);
                this.ivjFlatButtonNewfolder.setImageResource("/images/foldernew.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonNewfolder;
    }

    private FlatButton getFlatButtonOpenBrowser() {
        if (this.ivjFlatButtonOpenBrowser == null) {
            try {
                this.ivjFlatButtonOpenBrowser = new FlatButton();
                this.ivjFlatButtonOpenBrowser.setName("FlatButtonOpenBrowser");
                this.ivjFlatButtonOpenBrowser.setFixedsize(new Dimension(62, 50));
                this.ivjFlatButtonOpenBrowser.setEnabled(true);
                this.ivjFlatButtonOpenBrowser.setLabel("Browser");
                this.ivjFlatButtonOpenBrowser.setImageResource("/images/world.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonOpenBrowser;
    }

    private FlatButton getFlatButtonPaste() {
        if (this.ivjFlatButtonPaste == null) {
            try {
                this.ivjFlatButtonPaste = new FlatButton();
                this.ivjFlatButtonPaste.setName("FlatButtonPaste");
                this.ivjFlatButtonPaste.setFont(new Font("dialog", 0, 10));
                this.ivjFlatButtonPaste.setFixedsize(new Dimension(43, 50));
                this.ivjFlatButtonPaste.setEnabled(false);
                this.ivjFlatButtonPaste.setLabel("Paste");
                AppRegistry.getInstance().put("FlatButtonPaste", this.ivjFlatButtonPaste);
                this.ivjFlatButtonPaste.setImageResource("/images/paste.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonPaste;
    }

    private FlatButton getFlatButtonProperties() {
        if (this.ivjFlatButtonProperties == null) {
            try {
                this.ivjFlatButtonProperties = new FlatButton();
                this.ivjFlatButtonProperties.setName("FlatButtonProperties");
                this.ivjFlatButtonProperties.setFont(new Font("dialog", 0, 10));
                this.ivjFlatButtonProperties.setFixedsize(new Dimension(70, 50));
                this.ivjFlatButtonProperties.setEnabled(true);
                this.ivjFlatButtonProperties.setLabel("Properties");
                AppRegistry.getInstance().put("FlatButtonProperties", this.ivjFlatButtonProperties);
                this.ivjFlatButtonProperties.setImageResource("/images/properties.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonProperties;
    }

    private FlatButton getFlatButtonRun() {
        if (this.ivjFlatButtonRun == null) {
            try {
                this.ivjFlatButtonRun = new FlatButton();
                this.ivjFlatButtonRun.setName("FlatButtonRun");
                this.ivjFlatButtonRun.setHasborder(false);
                this.ivjFlatButtonRun.setLabel("Run");
                this.ivjFlatButtonRun.setFont(new Font("dialog", 0, 10));
                this.ivjFlatButtonRun.setFixedsize(new Dimension(60, 50));
                this.ivjFlatButtonRun.setEnabled(true);
                AppRegistry.getInstance().put("FlatButtonRun", this.ivjFlatButtonRun);
                this.ivjFlatButtonRun.setImageResource("/images/run.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonRun;
    }

    private FlatButton getFlatButtonSaveAll() {
        if (this.ivjFlatButtonSaveAll == null) {
            try {
                this.ivjFlatButtonSaveAll = new FlatButton();
                this.ivjFlatButtonSaveAll.setName("FlatButtonSaveAll");
                this.ivjFlatButtonSaveAll.setFixedsize(new Dimension(62, 50));
                this.ivjFlatButtonSaveAll.setEnabled(true);
                this.ivjFlatButtonSaveAll.setLabel("Save all");
                this.ivjFlatButtonSaveAll.setImageResource("/images/saveall.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonSaveAll;
    }

    private FlatButton getFlatButtonSaveApp() {
        if (this.ivjFlatButtonSaveApp == null) {
            try {
                this.ivjFlatButtonSaveApp = new FlatButton();
                this.ivjFlatButtonSaveApp.setName("FlatButtonSaveApp");
                this.ivjFlatButtonSaveApp.setFixedsize(new Dimension(62, 50));
                this.ivjFlatButtonSaveApp.setEnabled(true);
                this.ivjFlatButtonSaveApp.setLabel("Save");
                this.ivjFlatButtonSaveApp.setImageResource("/images/save.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonSaveApp;
    }

    private FlatButton getFlatButtonStart() {
        if (this.ivjFlatButtonStart == null) {
            try {
                this.ivjFlatButtonStart = new FlatButton();
                this.ivjFlatButtonStart.setName("FlatButtonStart");
                this.ivjFlatButtonStart.setFixedsize(new Dimension(62, 50));
                this.ivjFlatButtonStart.setLabel("Search");
                this.ivjFlatButtonStart.setImageResource("/images/search.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonStart;
    }

    private FlatButton getFlatButtonStop() {
        if (this.ivjFlatButtonStop == null) {
            try {
                this.ivjFlatButtonStop = new FlatButton();
                this.ivjFlatButtonStop.setName("FlatButtonStop");
                this.ivjFlatButtonStop.setFont(new Font("dialog", 0, 12));
                this.ivjFlatButtonStop.setFixedsize(new Dimension(62, 50));
                this.ivjFlatButtonStop.setLabel("Stop");
                this.ivjFlatButtonStop.setImageResource("/images/stop.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonStop;
    }

    private FlatButton getFlatButtonSwitchToSearch() {
        if (this.ivjFlatButtonSwitchToSearch == null) {
            try {
                this.ivjFlatButtonSwitchToSearch = new FlatButton();
                this.ivjFlatButtonSwitchToSearch.setName("FlatButtonSwitchToSearch");
                this.ivjFlatButtonSwitchToSearch.setHasborder(false);
                this.ivjFlatButtonSwitchToSearch.setLabel("Back");
                this.ivjFlatButtonSwitchToSearch.setFont(new Font("dialog", 0, 10));
                this.ivjFlatButtonSwitchToSearch.setFixedsize(new Dimension(70, 50));
                this.ivjFlatButtonSwitchToSearch.setEnabled(true);
                this.ivjFlatButtonSwitchToSearch.setImageResource("/images/search.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonSwitchToSearch;
    }

    private FlatButton getFlatButtonTest() {
        if (this.ivjFlatButtonTest == null) {
            try {
                this.ivjFlatButtonTest = new FlatButton();
                this.ivjFlatButtonTest.setName("FlatButtonTest");
                this.ivjFlatButtonTest.setFixedsize(new Dimension(62, 50));
                this.ivjFlatButtonTest.setEnabled(true);
                this.ivjFlatButtonTest.setLabel("Test");
                this.ivjFlatButtonTest.setImageResource("/images/run.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonTest;
    }

    private FlatButton getFlatButtonUndo() {
        if (this.ivjFlatButtonUndo == null) {
            try {
                this.ivjFlatButtonUndo = new FlatButton();
                this.ivjFlatButtonUndo.setName("FlatButtonUndo");
                this.ivjFlatButtonUndo.setVisible(false);
                this.ivjFlatButtonUndo.setLabel("Undo");
                this.ivjFlatButtonUndo.setFont(new Font("dialog", 0, 10));
                this.ivjFlatButtonUndo.setFixedsize(new Dimension(43, 50));
                this.ivjFlatButtonUndo.setEnabled(false);
                AppRegistry.getInstance().put("FlatButtonUndo", this.ivjFlatButtonUndo);
                this.ivjFlatButtonUndo.setImageResource("/images/undo.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjFlatButtonUndo;
    }

    private JBImageSpinner getJBImageSpinner1() {
        if (this.ivjJBImageSpinner1 == null) {
            try {
                Image image;
                this.ivjJBImageSpinner1 = new JBImageSpinner();
                this.ivjJBImageSpinner1.setName("JBImageSpinner1");
                Image image2 = JBee.resources.getImageResource("/images/jblogo_still.gif");
                if (image2 != null) {
                    image = image2;
                    this.ivjJBImageSpinner1.setStillImage(image);
                }
                if ((image = JBee.resources.getImageResource("/images/jblogo_anim.gif")) != null) {
                    Image image3 = image;
                    this.ivjJBImageSpinner1.setAnimatedImage(image3);
                }
                this.ivjJBImageSpinner1.toggle(false);
                AppRegistry.getInstance().put("Spinner", this.ivjJBImageSpinner1);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjJBImageSpinner1;
    }

    private JBManagerPanel getJBManagerPanel() {
        if (this.ivjJBManagerPanel == null) {
            try {
                this.ivjJBManagerPanel = new JBManagerPanel();
                this.ivjJBManagerPanel.setName("JBManagerPanel");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjJBManagerPanel;
    }

    private Label getLabelFolder() {
        if (this.ivjLabelFolder == null) {
            try {
                this.ivjLabelFolder = new Label();
                this.ivjLabelFolder.setName("LabelFolder");
                this.ivjLabelFolder.setText("");
                this.ivjLabelFolder.setBackground(SystemColor.control);
                this.ivjLabelFolder.setForeground(Color.darkGray);
                this.ivjLabelFolder.setAlignment(0);
                this.ivjLabelFolder.setFont(new Font("dialog", 1, 14));
                AppRegistry.getInstance().put("LabelFolder", this.ivjLabelFolder);
                this.ivjLabelFolder.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelFolder;
    }

    private Label getLabelFolderTx() {
        if (this.ivjLabelFolderTx == null) {
            try {
                this.ivjLabelFolderTx = new Label();
                this.ivjLabelFolderTx.setName("LabelFolderTx");
                this.ivjLabelFolderTx.setText("Current folder");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelFolderTx;
    }

    private Label getLabelStatus() {
        if (this.ivjLabelStatus == null) {
            try {
                this.ivjLabelStatus = new Label();
                this.ivjLabelStatus.setName("LabelStatus");
                this.ivjLabelStatus.setText("Status");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelStatus;
    }

    private Label getLabelURL() {
        if (this.ivjLabelURL == null) {
            try {
                this.ivjLabelURL = new Label();
                this.ivjLabelURL.setName("LabelURL");
                this.ivjLabelURL.setAlignment(0);
                this.ivjLabelURL.setText("URL or keyword");
                this.ivjLabelURL.setBackground(SystemColor.control);
                this.ivjLabelURL.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLabelURL;
    }

    private LWSeparator getLWSeparator() {
        if (this.ivjLWSeparator == null) {
            try {
                this.ivjLWSeparator = new LWSeparator();
                this.ivjLWSeparator.setName("LWSeparator");
                this.ivjLWSeparator.setDirection(true);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLWSeparator;
    }

    private LWSeparator getLWSeparator1() {
        if (this.ivjLWSeparator1 == null) {
            try {
                this.ivjLWSeparator1 = new LWSeparator();
                this.ivjLWSeparator1.setName("LWSeparator1");
                this.ivjLWSeparator1.setPrefSize(new Dimension(10, 45));
                this.ivjLWSeparator1.setDirection(false);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLWSeparator1;
    }

    private LWSeparator getLWSeparator2() {
        if (this.ivjLWSeparator2 == null) {
            try {
                this.ivjLWSeparator2 = new LWSeparator();
                this.ivjLWSeparator2.setName("LWSeparator2");
                this.ivjLWSeparator2.setDirection(true);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLWSeparator2;
    }

    private LWSeparator getLWSeparator3() {
        if (this.ivjLWSeparator3 == null) {
            try {
                this.ivjLWSeparator3 = new LWSeparator();
                this.ivjLWSeparator3.setName("LWSeparator3");
                this.ivjLWSeparator3.setDirection(true);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLWSeparator3;
    }

    private NewAppTableModel getNewAppTableModel() {
        if (this.ivjNewAppTableModel == null) {
            try {
                this.ivjNewAppTableModel = new NewAppTableModel();
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjNewAppTableModel;
    }

    private TableView getNewAppTableView() {
        if (this.ivjNewAppTableView == null) {
            try {
                this.ivjNewAppTableView = new TableView();
                this.ivjNewAppTableView.setName("NewAppTableView");
                this.ivjNewAppTableView.setCellheight(18);
                this.ivjNewAppTableView.setBackground(Color.white);
                this.ivjNewAppTableView.setCellwidth(195);
                this.ivjNewAppTableView.setHeaderbackground(Color.orange);
                this.ivjNewAppTableView.setNumberOfRows(2);
                this.ivjNewAppTableView.setHeaderforeground(SystemColor.activeCaption);
                this.ivjNewAppTableView.setNumberOfcols(2);
                this.ivjNewAppTableView.setSelectmode(1);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjNewAppTableView;
    }

    private Panel getPanelAddress() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        if (this.ivjPanelAddress == null) {
            try {
                this.ivjPanelAddress = new Panel();
                this.ivjPanelAddress.setName("PanelAddress");
                this.ivjPanelAddress.setLayout(new GridBagLayout());
                this.ivjPanelAddress.setBackground(SystemColor.control);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.fill = 2;
                gridBagConstraints.anchor = 17;
                gridBagConstraints.weightx = 0.1;
                gridBagConstraints.weighty = 0.0;
                gridBagConstraints.insets = new Insets(0, 5, 0, 0);
                this.getPanelAddress().add((Component) this.getLabelURL(), gridBagConstraints);
                gridBagConstraints2.gridx = 1;
                gridBagConstraints2.gridy = 2;
                gridBagConstraints2.gridwidth = 1;
                gridBagConstraints2.gridheight = 1;
                gridBagConstraints2.fill = 2;
                gridBagConstraints2.anchor = 17;
                gridBagConstraints2.weightx = 1.0;
                gridBagConstraints2.weighty = 0.0;
                this.getPanelAddress().add((Component) this.getTextFieldURL(), gridBagConstraints2);
                gridBagConstraints3.gridx = 3;
                gridBagConstraints3.gridy = 2;
                gridBagConstraints3.gridwidth = 1;
                gridBagConstraints3.gridheight = 1;
                gridBagConstraints3.fill = 2;
                gridBagConstraints3.anchor = 17;
                gridBagConstraints3.weightx = 0.15;
                gridBagConstraints3.weighty = 0.0;
                gridBagConstraints3.insets = new Insets(0, 5, 0, 5);
                this.getPanelAddress().add((Component) this.getChoiceSearchURL(), gridBagConstraints3);
                gridBagConstraints4.gridx = 1;
                gridBagConstraints4.gridy = 4;
                gridBagConstraints4.gridwidth = 3;
                gridBagConstraints4.gridheight = 1;
                gridBagConstraints4.fill = 2;
                gridBagConstraints4.anchor = 10;
                gridBagConstraints4.weightx = 0.0;
                gridBagConstraints4.weighty = 0.0;
                gridBagConstraints4.insets = new Insets(5, 0, 5, 10);
                this.getPanelAddress().add((Component) this.getLabelFolder(), gridBagConstraints4);
                gridBagConstraints5.gridx = 0;
                gridBagConstraints5.gridy = 4;
                gridBagConstraints5.gridwidth = 1;
                gridBagConstraints5.gridheight = 1;
                gridBagConstraints5.anchor = 17;
                gridBagConstraints5.weightx = 0.0;
                gridBagConstraints5.weighty = 0.0;
                gridBagConstraints5.insets = new Insets(5, 5, 5, 0);
                this.getPanelAddress().add((Component) this.getLabelFolderTx(), gridBagConstraints5);
                gridBagConstraints6.gridx = 0;
                gridBagConstraints6.gridy = 0;
                gridBagConstraints6.gridwidth = 3;
                gridBagConstraints6.gridheight = 1;
                gridBagConstraints6.fill = 2;
                gridBagConstraints6.anchor = 10;
                gridBagConstraints6.weightx = 1.0;
                gridBagConstraints6.weighty = 1.0;
                this.getPanelAddress().add((Component) this.getPanelMenuCarddeck(), gridBagConstraints6);
                gridBagConstraints7.gridx = 3;
                gridBagConstraints7.gridy = 0;
                gridBagConstraints7.gridwidth = 1;
                gridBagConstraints7.gridheight = 1;
                gridBagConstraints7.anchor = 13;
                gridBagConstraints7.weightx = 1.0;
                gridBagConstraints7.weighty = 1.0;
                gridBagConstraints7.insets = new Insets(5, 5, 5, 5);
                this.getPanelAddress().add((Component) this.getPanelLogo(), gridBagConstraints7);
                gridBagConstraints8.gridx = 0;
                gridBagConstraints8.gridy = 1;
                gridBagConstraints8.gridwidth = 4;
                gridBagConstraints8.gridheight = 1;
                gridBagConstraints8.fill = 2;
                gridBagConstraints8.anchor = 10;
                gridBagConstraints8.weightx = 0.0;
                gridBagConstraints8.weighty = 0.0;
                this.getPanelAddress().add((Component) this.getLWSeparator2(), gridBagConstraints8);
                gridBagConstraints9.gridx = 0;
                gridBagConstraints9.gridy = 3;
                gridBagConstraints9.gridwidth = 4;
                gridBagConstraints9.gridheight = 1;
                gridBagConstraints9.fill = 2;
                gridBagConstraints9.anchor = 10;
                gridBagConstraints9.weightx = 0.0;
                gridBagConstraints9.weighty = 0.0;
                this.getPanelAddress().add((Component) this.getLWSeparator3(), gridBagConstraints9);
                this.getPanelAddress().setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelAddress;
    }

    private Panel getPanelCenter() {
        if (this.ivjPanelCenter == null) {
            try {
                this.ivjPanelCenter = new Panel();
                this.ivjPanelCenter.setName("PanelCenter");
                this.ivjPanelCenter.setLayout(new CardLayout());
                this.getPanelCenter().add((Component) this.getPanelScroll(), this.getPanelScroll().getName());
                this.getPanelCenter().add((Component) this.getJBManagerPanel(), this.getJBManagerPanel().getName());
                this.ivjPanelCenter.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelCenter;
    }

    private DoubleBufferPanel getPanelLogo() {
        if (this.ivjPanelLogo == null) {
            try {
                this.ivjPanelLogo = new DoubleBufferPanel();
                this.ivjPanelLogo.setName("PanelLogo");
                this.ivjPanelLogo.setLayout(new BorderLayout());
                this.getPanelLogo().add((Component) this.getJBImageSpinner1(), "Center");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelLogo;
    }

    private Panel getPanelMenuCarddeck() {
        if (this.ivjPanelMenuCarddeck == null) {
            try {
                this.ivjPanelMenuCarddeck = new Panel();
                this.ivjPanelMenuCarddeck.setName("PanelMenuCarddeck");
                this.ivjPanelMenuCarddeck.setLayout(new CardLayout());
                this.getPanelMenuCarddeck().add((Component) this.getDoubleBufferPanelMenuSearch(), this.getDoubleBufferPanelMenuSearch().getName());
                this.getPanelMenuCarddeck().add((Component) this.getDoubleBufferPanelMenuManage(), this.getDoubleBufferPanelMenuManage().getName());
                this.ivjPanelMenuCarddeck.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelMenuCarddeck;
    }

    private Panel getPanelScroll() {
        if (this.ivjPanelScroll == null) {
            try {
                this.ivjPanelScroll = new Panel();
                this.ivjPanelScroll.setName("PanelScroll");
                this.ivjPanelScroll.setLayout(new BorderLayout());
                this.getPanelScroll().add((Component) this.getNewAppTableView(), "Center");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelScroll;
    }

    private Panel getPanelSouth() {
        if (this.ivjPanelSouth == null) {
            try {
                this.ivjPanelSouth = new Panel();
                this.ivjPanelSouth.setName("PanelSouth");
                this.ivjPanelSouth.setLayout(new BorderLayout());
                this.ivjPanelSouth.setBackground(SystemColor.control);
                this.getPanelSouth().add((Component) this.getPanelStatus(), "North");
                //this.getPanelSouth().add((Component) this.getJAdPanel(), "South");
                this.ivjPanelSouth.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelSouth;
    }

    private Panel getPanelStatus() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        if (this.ivjPanelStatus == null) {
            try {
                this.ivjPanelStatus = new Panel();
                this.ivjPanelStatus.setName("PanelStatus");
                this.ivjPanelStatus.setLayout(new GridBagLayout());
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.fill = 2;
                gridBagConstraints.anchor = 17;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.insets = new Insets(0, 10, 0, 10);
                this.getPanelStatus().add((Component) this.getLabelStatus(), gridBagConstraints);
                gridBagConstraints2.gridx = 1;
                gridBagConstraints2.gridy = 0;
                gridBagConstraints2.gridwidth = 1;
                gridBagConstraints2.gridheight = 1;
                gridBagConstraints2.fill = 1;
                gridBagConstraints2.anchor = 10;
                gridBagConstraints2.weightx = 0.3;
                gridBagConstraints2.weighty = 1.0;
                gridBagConstraints2.ipady = -8;
                gridBagConstraints2.insets = new Insets(5, 0, 5, 15);
                this.getPanelStatus().add((Component) this.getStatusbar(), gridBagConstraints2);
                gridBagConstraints3.gridx = 0;
                gridBagConstraints3.gridy = 1;
                gridBagConstraints3.gridwidth = 4;
                gridBagConstraints3.gridheight = 1;
                gridBagConstraints3.fill = 2;
                gridBagConstraints3.anchor = 10;
                gridBagConstraints3.weightx = 0.0;
                gridBagConstraints3.weighty = 0.0;
                this.getPanelStatus().add((Component) this.getLWSeparator(), gridBagConstraints3);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelStatus;
    }

    public int getParsegroupNr() {
        return this.searchnr;
    }

    private BorderLayout getSearchPanelBorderLayout() {
        BorderLayout borderLayout = null;
        try {
            borderLayout = new BorderLayout();
            borderLayout.setVgap(0);
            borderLayout.setHgap(0);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
        return borderLayout;
    }

    private ProgressBar getStatusbar() {
        if (this.ivjStatusbar == null) {
            try {
                this.ivjStatusbar = new ProgressBar();
                this.ivjStatusbar.setName("Statusbar");
                this.ivjStatusbar.setBorderThickness(1);
                this.ivjStatusbar.setBarColor(Color.orange);
                this.ivjStatusbar.setForeground(SystemColor.activeCaption);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjStatusbar;
    }

    private TextField getTextFieldURL() {
        if (this.ivjTextFieldURL == null) {
            try {
                this.ivjTextFieldURL = new TextField();
                this.ivjTextFieldURL.setName("TextFieldURL");
                ((TextComponent) this.ivjTextFieldURL).setText("http://");
                ((Component) this.ivjTextFieldURL).setBackground(Color.white);
                this.ivjTextFieldURL.setColumns(10);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjTextFieldURL;
    }

    private void handleException(Throwable throwable) {
    }

    public void init() {
        String string = "";
        try {
            this.historyModel = new Vector(10);
            String string2 = JBee.getPreference("javabee_home");
            if (!string2.endsWith(System.getProperty("file.separator"))) {
                string2 = String.valueOf(string2) + System.getProperty("file.separator");
            }
            String string3 = String.valueOf(string2) + "history.log";
            try {
                FileInputStream fileInputStream = new FileInputStream(string3);
                DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                while (string != null) {
                    string = dataInputStream.readLine();
                    if (string == null) continue;
                    this.historyModel.addElement(string);
                    JBLogger.log(string);
                }
            } catch (Exception exception) {
                JBLogger.log("Ex. i JBSearchPanel.init " + exception.toString());
                if (this.historyModel.size() == 0) {
                    this.historyModel.addElement("http://www.objectbox.com/javabee/javalinks.htm");
                }
            }
        } catch (Exception exception) {
            JBLogger.log("Ex in JBSearch init: " + exception);
        }
    }

    private void initConnections() {
        this.getFlatButtonStart().addActionListener(this);
        this.getTextFieldURL().addActionListener(this);
        this.getFlatButtonClear().addActionListener(this);
        this.getFlatButtonStop().addActionListener(this);
        this.getFlatButtonSaveApp().addActionListener(this);
        this.getFlatButtonTest().addActionListener(this);
        this.getFlatButtonOpenBrowser().addActionListener(this);
        this.getFlatButtonSaveAll().addActionListener(this);
        this.getTextFieldURL().addKeyListener(this);
        this.getFlatButtonManage().addActionListener(this);
        this.getFlatButtonSwitchToSearch().addActionListener(this);
        this.getFlatButtonNewfolder().addActionListener(this);
        this.getFlatButtonProperties().addActionListener(this);
        this.getFlatButtonRun().addActionListener(this);
        this.getFlatButtonCut().addActionListener(this);
        this.getFlatButtonCopy().addActionListener(this);
        this.getFlatButtonPaste().addActionListener(this);
        this.getFlatButtonDelete().addActionListener(this);
        this.getFlatButtonUndo().addActionListener(this);
        this.getFlatButtonBrowser().addActionListener(this);
        this.connPtoP1SetTarget();
        this.connPtoP2SetTarget();
        this.connPtoP3SetTarget();
    }

    private void initialize() {
        this.init();
        this.setName("SearchPanel");
        this.setLayout(this.getSearchPanelBorderLayout());
        this.setBackground(new Color(162, 162, 180));
        this.setSize(648, 482);
        this.add((Component) this.getPanelSouth(), "South");
        this.add((Component) this.getPanelAddress(), "North");
        this.add((Component) this.getPanelCenter(), "Center");
        this.initConnections();
        this.setBackground(JBee.appcolor);
        this.getNewAppTableView().autoFitAll();
        this.startSniffer();
        this.atp = new AppletTagParser();
        this.atp.setNoGoList();
        this.atp = null;
        AppRegistry.getInstance().put("SearchManagePanel", this);
    }

    public void keyPressed(KeyEvent keyEvent) {
    }

    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getSource() == this.getTextFieldURL()) {
            this.connEtoC14(keyEvent);
        }
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    public void list1_FocusLost(FocusEvent focusEvent) {
    }

    public void list1_ItemStateChanged(ItemEvent itemEvent) {
    }

    public void removeSavePropertiesListener(SavePropertiesListener savePropertiesListener) {
        this.aSavePropertiesListener = SavePropertiesEventMulticaster.remove(this.aSavePropertiesListener, savePropertiesListener);
    }

    public void saveHistory() {
        String string = "";
        int n = this.historyModel.size();
        try {
            String string2 = JBee.getPreference("javabee_home");
            if (!string2.endsWith(System.getProperty("file.separator"))) {
                string2 = String.valueOf(string2) + System.getProperty("file.separator");
            }
            String string3 = String.valueOf(string2) + "history.log";
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(string3);
                DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
                int n2 = 0;
                while (n2 < n) {
                    string = (String) this.historyModel.elementAt(n2);
                    if ((string = string.trim()).length() > 0) {
                        dataOutputStream.writeBytes(String.valueOf(string) + "\n");
                        JBLogger.log(string);
                    }
                    ++n2;
                }
                dataOutputStream.close();
            } catch (Exception exception) {
                JBLogger.log("Ex in saving history: " + exception);
            }
        } catch (Exception exception) {
            JBLogger.log("Ex in saving history: " + exception);
        }
    }

    public void setFolder(String string) {
        this.getLabelFolder().setText(string);
    }

    private void startSniffer() {
        class Sniffer extends Thread {
            /* synthetic */ JBSearchPanel this$0;
            String oldClipTxt;
            String clipTxt;

            Sniffer(JBSearchPanel jBSearchPanel) {
                this.this$0 = jBSearchPanel;
                this.oldClipTxt = "";
                this.clipTxt = "";
            }

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000L);
                        JBManagerPanel manager = (JBManagerPanel) AppRegistry.getInstance().lookup("Manager");
                        if (manager.isShowing()) {
                            FlatButton flatButtonCopy = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonCopy");
                            FlatButton flatButtonDelete = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonDelete");
                            FlatButton flatButtonCut = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonCut");
                            FlatButton flatButtonPaste = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonPaste");
                            FlatButton flatButtonNewFolder = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonNewFolder");
                            FlatButton flatButtonRun = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonRun");
                            FlatButton flatButtonBrowser = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonBrowser");
                            FlatButton flatButtonProperties = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonProperties");
                            int selectedIndex = manager.getTreeBasePublic().getSelectedIndex();
                            int length = manager.getTreeBasePublic().getSelectedIndexes().length;
                            Node node = manager.getTreeBasePublic().getSelectedNode();
                            if (selectedIndex < 1) {
                                if (selectedIndex != 0) {
                                    flatButtonProperties.setEnabled(false);
                                    flatButtonNewFolder.setEnabled(false);
                                } else {
                                    flatButtonProperties.setEnabled(true);
                                    flatButtonNewFolder.setEnabled(true);
                                    if (manager.getNodeClipboard().length > 0) {
                                        flatButtonPaste.setEnabled(true);
                                    } else {
                                        flatButtonPaste.setEnabled(false);
                                    }
                                }

                                flatButtonBrowser.setEnabled(false);
                                flatButtonRun.setEnabled(false);
                                if (length < 2) {
                                    flatButtonCopy.setEnabled(false);
                                    flatButtonDelete.setEnabled(false);
                                    flatButtonCut.setEnabled(false);
                                }
                            } else {
                                if (node != null && node.getType().equals("Folder")) {
                                    flatButtonProperties.setEnabled(false);
                                    flatButtonBrowser.setEnabled(false);
                                    flatButtonRun.setLabel("Run All");
                                } else {
                                    flatButtonBrowser.setEnabled(true);
                                    flatButtonProperties.setEnabled(true);
                                    flatButtonRun.setLabel("Run");
                                }

                                if (manager.getNodeClipboard().length > 0) {
                                    flatButtonPaste.setEnabled(true);
                                }

                                flatButtonCopy.setEnabled(true);
                                flatButtonDelete.setEnabled(true);
                                flatButtonCut.setEnabled(true);
                                flatButtonNewFolder.setEnabled(true);
                                flatButtonRun.setEnabled(true);
                            }
                        }

                        if (JBee.getUrlToShow().length() > 0) {
                            JBee.displayURL(JBee.getUrlToShow());
                            JBee.setUrlToShow("");
                        }

                        if (JBSearchPanel.access$getTextFieldURL(JBSearchPanel.this) != null) {
                            Clipboard var14 = Toolkit.getDefaultToolkit().getSystemClipboard();
                            Transferable var15 = var14.getContents(this);
                            this.clipTxt = (String) var15.getTransferData(new DataFlavor(this.clipTxt.getClass(), "text/plain"));
                            if (this.clipTxt != null && this.clipTxt.startsWith("http://") && this.oldClipTxt.compareTo(this.clipTxt) != 0) {
                                JBSearchPanel.access$getTextFieldURL(JBSearchPanel.this).setText(this.clipTxt);
                                this.oldClipTxt = this.clipTxt;
                                Frame var16 = (Frame) JBSearchPanel.this.getParent().getParent();
                                if (var16.isVisible()) {
                                    var16.toFront();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Sniffer sniffer = new Sniffer(this);
        sniffer.start();
    }

    public void textFieldURL_KeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 40 | keyEvent.getKeyCode() == 38) {
            this.currentHistoryPosition = keyEvent.getKeyCode() == 40 ? ++this.currentHistoryPosition : --this.currentHistoryPosition;
            if (this.currentHistoryPosition > this.historyModel.size() | this.currentHistoryPosition < 0) {
                this.currentHistoryPosition = 0;
            }
            ((TextComponent) this.getTextFieldURL()).setText((String) this.historyModel.elementAt(this.currentHistoryPosition));
            return;
        }
        if (!(keyEvent.isActionKey() | keyEvent.getKeyCode() == 127 | keyEvent.getKeyCode() == 8 | keyEvent.getKeyCode() == 10 | keyEvent.getKeyCode() == 16 | keyEvent.getKeyCode() == 17 | keyEvent.getKeyCode() == 18)) {
            String string = this.getTextFieldURL().getText();
            int n = this.historyModel.size();
            int n2 = 0;
            while (n2 < n) {
                if (this.historyModel.elementAt(n2).toString().startsWith(string)) {
                    int n3 = this.getTextFieldURL().getCaretPosition();
                    ((TextComponent) this.getTextFieldURL()).setText(this.historyModel.elementAt(n2).toString());
                    this.getTextFieldURL().setSelectionStart(n3);
                    this.getTextFieldURL().setSelectionEnd(this.getTextFieldURL().getText().length());
                    keyEvent.consume();
                    break;
                }
                ++n2;
            }
        }
    }

    public void thumbNailComponent1_MousePressed(MouseEvent mouseEvent) {
        JBee.displayURL("http://www.objectbox.com");
    }

    public void updateAppletList(Vector vector) {
        if (vector != null) {
            this.getNewAppTableModel().addData(vector);
            this.getNewAppTableView().setModel(this.getNewAppTableModel());
            this.getNewAppTableView().autoFitAll();
        }
    }

    public void updateStatus(int n, int n2, int n3, String string) {
        this.getLabelStatus().setText("Status:  Checked: " + new Integer(n).toString() + " pages. Found " + n3 + " applets and " + new Integer(n2).toString() + " links.");
        int n4 = 0;
        if (n2 > 0) {
            n4 = (int) ((double) n / (double) n2 * 100.0);
        }
        if (n4 >= 0 && n4 <= 100) {
            this.getStatusbar().setProgress(n4);
        }
        if (n4 == 100) {
            this.getJBImageSpinner1().toggle(false);
        } else {
            this.getJBImageSpinner1().toggle(true);
        }
        this.getStatusbar().repaint();
    }
}

