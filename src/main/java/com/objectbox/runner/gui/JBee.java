/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.*;
import com.objectbox.jadvertise.Launcher;
import com.objectbox.loader.JBSecurityManager;
import com.objectbox.loader.SecurityViolationEvent;
import com.objectbox.loader.SecurityViolationListener;
import com.objectbox.runner.applet.AudioApp;
import com.objectbox.runner.beans.DownloadView;
import com.objectbox.runner.beans.MessageDialog;
import com.objectbox.runner.gui.tree.Node;
import com.objectbox.runner.gui.tree.TreeNode;
import com.objectbox.runner.model.JBProcessModel;
import com.objectbox.runner.model.JBSecurityModel;
import com.objectbox.runner.model.SecurityManagerIF;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.nio.file.FileSystems;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class JBee
        extends Frame
        implements PopupItemSelectedListener,
        SecurityViolationListener,
        ActionListener,
        WindowListener {
    public static final int OS_WIN = 1;
    public static final int OS_OTHER = 2;
    public static final int DESKTOP_MAX_APPLETS = 10;
    protected static final int POPUPITEM_HEIGHT = 25;
    private static final String WIN_PATH = "rundll32";
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
    private static final String UNIX_PATH = "netscape";
    private static final String UNIX_FLAG = "-remote 'openURL(";
    public static int OS_type = 1;
    public static JBResources resources = new JBResources();
    public static Color appcolor = SystemColor.control;
    public static boolean killall = false;
    public static Node loadedroot = null;
    protected static Properties preferences = new Properties();
    private static Hashtable<String,SecurityManagerIF> securitysettings = null;
    private static Image appicon = null;
    private static Frame runningInstanceFrame = null;
    private static String urlToShow = "";
    private static MessageDialog messagedialog = null;
    protected JBPopupMenu popupRun = null;
    protected Hashtable threadhash = new Hashtable<>();
    protected Hashtable zoombiehash = new Hashtable<>();
    protected ThreadGroup threadgroup = new ThreadGroup("javabee");
    private FlatButton ivjButtonRun = null;
    private JBProcessViewPanel ivjJBProcessViewPanel1 = null;
    private final Frame menuParentFrame = this;
    private Panel ivjGPanel = null;
    private File propertyFile = new File("javabee.properties");
    private SetupFrame setupFrame = null;
    private DownloadView ivjdownload = null;
    private Label ivjLabel1 = null;
    private Label ivjLabel2 = null;
    private final BorderLayout ivjJavaBeeFrameBorderLayout = null;
    private JBSearchPanelFrame searchframe = null;
    private SecurityWarningDialog warning_dialog = new SecurityWarningDialog();
    private LWSeparator ivjLWSeparator1 = null;
    private boolean splashWait = true;

    public JBee() {
        this.initialize();
    }

    public JBee(String string) {
        super(string);
    }

    public static void displayURL(String string) {
        try {
            Launcher.openURL(string);
        } catch (IOException iOException) {
            System.err.println("Could not invoke browser, url=" + string);
        }
    }

    public static Image getIcon() {
        if (appicon == null) {
            appicon = resources.getImageResource("/images/beehead.gif");
        }
        return appicon;
    }

    public static String getPreference(String string) {
        return preferences.getProperty(string);
    }

    public static Frame getRunningInstanceFrame() {
        return runningInstanceFrame;
    }

    public static SecurityManagerIF getSecurityHandler(Object object) {
        try {
            if (!(System.getSecurityManager() instanceof JBSecurityManager)) {
                return null;
            }
            SecurityManagerIF securityManagerIF = securitysettings.get(object);
            if (securityManagerIF == null) {
                securityManagerIF = securitysettings.get("default");
            }
            return securityManagerIF;
        } catch (Throwable throwable) {
            JBLogger.log("getSecHandler: " + throwable.toString());
            return securitysettings.get("default");
        }
    }

    public static String getUrlToShow() {
        return urlToShow;
    }

    public static void setUrlToShow(String string) {
        urlToShow = string;
    }

    public static void main(String[] stringArray) {
        boolean bl = false;
        if (stringArray.length == 0) {
            System.setOut(new PrintStream(new NullOutputstream()));
            System.setErr(new PrintStream(new NullOutputstream()));
        } else if (stringArray[0].equals("-nosecurity")) {
            bl = true;
        }
        try {
            JBee jBee = new JBee();
            if (!bl) {
                JBSecurityManager jBSecurityManager = new JBSecurityManager();
                jBSecurityManager.addSecurityViolationListener(jBee);
                System.setSecurityManager(jBSecurityManager);
                jBee.loadSecuritySettings();
            }
            ((Component) jBee).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Frame:" + throwable);
        }
    }

    public static void setPreference(String string, String string2) throws PropertyNotFoundException {
        if (preferences.getProperty(string) == null) {
            throw new PropertyNotFoundException();
        }
        preferences.put(string, string2);
    }

    public static void showMessage(String string, boolean bl) {
        if (messagedialog == null) {
            messagedialog = new MessageDialog();
        }
        if (bl) {
            messagedialog.appendMessage(string);
        } else {
            messagedialog.setMessage(string);
        }
        if (!messagedialog.isVisible()) {
            ((Component) messagedialog).setVisible(true);
            messagedialog.toFront();
        }
    }

    static boolean access$splashWait(JBee jBee) {
        return jBee.splashWait;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.getButtonRun()) {
            this.connEtoC2();
        }
    }

    public void addMenuItemsForNode(Node node, JBPopupMenu jBPopupMenu) {
        try {
            Hashtable hashtable = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
            Enumeration enumeration = node.children();
            while (enumeration.hasMoreElements()) {
                Node node2 = (Node) enumeration.nextElement();
                if (node2.getType().compareTo("Folder") == 0) {
                    JBPopupMenu jBPopupMenu2 = new JBPopupMenu(this.menuParentFrame, this.getButtonRun(), "sub");
                    jBPopupMenu2.setItemheight(25);
                    hashtable.put(node2, jBPopupMenu2);
                    jBPopupMenu.addPopupMenu(node2.getText(), jBPopupMenu2, node2);
                    this.addMenuItemsForNode(node2, jBPopupMenu2);
                    jBPopupMenu2.addPopupItemSelectedListener(this);
                    continue;
                }
                jBPopupMenu.addMenuItemWithObject(node2.getText(), node2);
                hashtable.put(node2, jBPopupMenu);
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in addMenuItemsForNode: " + throwable);
        }
    }

    public void addPreference(String string, String string2) {
        preferences.put(string, string2);
    }

    public void buttonRun_ActionPerformed(ActionEvent actionEvent) {
        try {
            if (this.popupRun.isVisible()) {
                this.popupRun.setVisible(false);
                this.popupRun.setFocus(false);
            } else {
//                if (!this.getJAdPanel().isAppletLoaded()) {
//                    this.getJAdPanel().loadApplet();
//                }
                this.popupRun.setVisible(true);
                this.popupRun.setFocus(true);
                this.popupRun.requestFocus();
            }
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
        }
    }

    public void buttonRun_OnActive(OnActiveEvent onActiveEvent) {
    }

    public void closeMenu() {
        this.popupRun.setFocus(false);
    }

    private void connEtoC1(WindowEvent windowEvent) {
        try {
            Object object = AppRegistry.getInstance().lookup("Manager");
            if (object != null) {
                ((JBManagerPanel) object).save(true);
            }
            this.dispose();
            this.savePreferences();
            this.saveSecuritySettings();
            System.exit(0);
        } catch (Throwable throwable) {
            System.exit(0);
            this.handleException(throwable);
        }
    }

    private void connEtoC2() {
        try {
            this.buttonRun_ActionPerformed(null);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void doSplash() {
        class SplashThread extends Thread {
            /* synthetic */ JBee this$0;
            Frame f;

            public SplashThread(JBee jBee, Frame frame) {
                this.this$0 = jBee;
                this.f = frame;
            }

            public void run() {
                try {
                    MediaTracker mediaTracker = new MediaTracker(this.f);
                    InputStream inputStream = this.getClass().getResourceAsStream("/images/splashbee.jpg");
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    if (inputStream != null) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                        while (true) {
                            try {
                                outputStream.write(dataInputStream.readByte());
                            } catch (IOException e) {
                                byte[] streamByteArray = outputStream.toByteArray();
                                outputStream.close();
                                dataInputStream.close();
                                Image image = Toolkit.getDefaultToolkit().createImage(streamByteArray);
                                mediaTracker.addImage(image, 0);

                                try {
                                    mediaTracker.waitForID(0);
                                } catch (InterruptedException e1) {
                                }

                                SplashWindow splashWindow = new SplashWindow(this.f, image, 275, 250);

                                try {
                                    while (JBee.access$splashWait(JBee.this)) {
                                        Thread.sleep(1000L);
                                    }
                                } catch (InterruptedException e1) {
                                }

                                splashWindow.dispose();
                                splashWindow.removeNotify();
                                image.flush();
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    JBLogger.log(e.toString());
                }

            }
        }
        SplashThread splashThread = new SplashThread(this, this);
        splashThread.start();
    }

    private FlatButton getButtonRun() {
        if (this.ivjButtonRun == null) {
            try {
                this.ivjButtonRun = new FlatButton();
                this.ivjButtonRun.setName("ButtonRun");
                this.ivjButtonRun.setBackground(SystemColor.control);
                this.ivjButtonRun.setFixedsize(new Dimension(80, 100));
                this.ivjButtonRun.setLabel("Start");
                this.ivjButtonRun.setBackground(appcolor);
                this.ivjButtonRun.setImageResource("/images/beebutton.gif", 0);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjButtonRun;
    }

    private DownloadView getdownload() {
        if (this.ivjdownload == null) {
            try {
                this.ivjdownload = new DownloadView();
                this.ivjdownload.setName("download");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjdownload;
    }

    private Panel getGPanel() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        if (this.ivjGPanel == null) {
            try {
                this.ivjGPanel = new Panel();
                this.ivjGPanel.setName("GPanel");
                this.ivjGPanel.setLayout(new GridBagLayout());
                this.ivjGPanel.setBackground(SystemColor.control);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.gridheight = 1;
                gridBagConstraints.anchor = 10;
                gridBagConstraints.weightx = 0.0;
                gridBagConstraints.weighty = 0.0;
                gridBagConstraints.insets = new Insets(5, 0, 0, 0);
                this.getGPanel().add(this.getButtonRun(), gridBagConstraints);
                gridBagConstraints2.gridx = 0;
                gridBagConstraints2.gridy = 1;
                gridBagConstraints2.gridwidth = 1;
                gridBagConstraints2.gridheight = 1;
                gridBagConstraints2.anchor = 17;
                gridBagConstraints2.weightx = 0.0;
                gridBagConstraints2.weighty = 0.0;
                gridBagConstraints2.insets = new Insets(0, 5, 0, 0);
                this.getGPanel().add(this.getLabel1(), gridBagConstraints2);
                gridBagConstraints3.gridx = 0;
                gridBagConstraints3.gridy = 3;
                gridBagConstraints3.gridwidth = 1;
                gridBagConstraints3.gridheight = 1;
                gridBagConstraints3.anchor = 17;
                gridBagConstraints3.weightx = 0.0;
                gridBagConstraints3.weighty = 0.0;
                gridBagConstraints3.insets = new Insets(0, 5, 0, 0);
                this.getGPanel().add(this.getLabel2(), gridBagConstraints3);
                gridBagConstraints4.gridx = 0;
                gridBagConstraints4.gridy = 4;
                gridBagConstraints4.gridwidth = 1;
                gridBagConstraints4.gridheight = 1;
                gridBagConstraints4.anchor = 10;
                gridBagConstraints4.weightx = 0.0;
                gridBagConstraints4.weighty = 0.0;
                gridBagConstraints4.ipady = 5;
                gridBagConstraints4.insets = new Insets(0, 5, 5, 5);
                this.getGPanel().add(this.getdownload(), gridBagConstraints4);
                gridBagConstraints5.gridx = 0;
                gridBagConstraints5.gridy = 2;
                gridBagConstraints5.gridwidth = 1;
                gridBagConstraints5.gridheight = 1;
                gridBagConstraints5.fill = 3;
                gridBagConstraints5.anchor = 10;
                gridBagConstraints5.weightx = 1.0;
                gridBagConstraints5.weighty = 1.0;
                gridBagConstraints5.ipadx = 10;
                this.getGPanel().add(this.getJBProcessViewPanel1(), gridBagConstraints5);
                gridBagConstraints6.gridx = 0;
                gridBagConstraints6.gridy = 6;
                gridBagConstraints6.gridwidth = 1;
                gridBagConstraints6.gridheight = 1;
                gridBagConstraints6.anchor = 10;
                gridBagConstraints6.weightx = 0.0;
                gridBagConstraints6.weighty = 0.0;
                this.getGPanel().add(new Panel(), gridBagConstraints6);
                gridBagConstraints7.gridx = 0;
                gridBagConstraints7.gridy = 5;
                gridBagConstraints7.gridwidth = 1;
                gridBagConstraints7.gridheight = 1;
                gridBagConstraints7.fill = 2;
                gridBagConstraints7.anchor = 10;
                gridBagConstraints7.weightx = 0.0;
                gridBagConstraints7.weighty = 0.0;
                gridBagConstraints7.insets = new Insets(0, 30, 5, 30);
                this.getGPanel().add(this.getLWSeparator1(), gridBagConstraints7);
                this.getGPanel().setBackground(appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjGPanel;
    }

    private BorderLayout getJavaBeeFrameBorderLayout() {
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

    private JBProcessViewPanel getJBProcessViewPanel1() {
        if (this.ivjJBProcessViewPanel1 == null) {
            try {
                this.ivjJBProcessViewPanel1 = new JBProcessViewPanel();
                this.ivjJBProcessViewPanel1.setName("JBProcessViewPanel1");
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjJBProcessViewPanel1;
    }

    private Label getLabel1() {
        if (this.ivjLabel1 == null) {
            try {
                this.ivjLabel1 = new Label();
                this.ivjLabel1.setName("Label1");
                this.ivjLabel1.setFont(new Font("sansserif", 1, 10));
                this.ivjLabel1.setText("Programs");
                this.ivjLabel1.setForeground(SystemColor.textInactiveText);
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
                this.ivjLabel2.setFont(new Font("sansserif", 1, 10));
                this.ivjLabel2.setText("Network traffic");
                this.ivjLabel2.setForeground(SystemColor.textInactiveText);
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
                this.ivjLWSeparator1.setDirection(true);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjLWSeparator1;
    }

    public JBProcessModel getProcessModel() {
        return this.getJBProcessViewPanel1().getModel();
    }

    public SetupFrame getSetupFrame() {
        return this.setupFrame;
    }

    public ThreadGroup getThreadGroup() {
        return this.threadgroup;
    }

    private void handleException(Throwable throwable) {
        JBLogger.log("--------- UNCAUGHT EXCEPTION ---------");
    }

    public void handlePopupSelection(PopupItemSelectedEvent popupItemSelectedEvent) {
        if (popupItemSelectedEvent.getSource() != null && popupItemSelectedEvent.getSource() instanceof Node) {
            Node node = (Node) popupItemSelectedEvent.getSource();
            Object object = AppRegistry.getInstance().lookup("Manager");
            if (node.getType().compareTo("Folder") == 0) {
                Node node2 = (Node) node.getFirstChild();
                int n = 0;
                while (node2 != null) {
                    if (object == null) continue;
                    if (++n > 10) break;
                    ((JBManagerPanel) object).startApp(node2);
                    TreeNode treeNode = node2.getNextSibling();
                    if (treeNode == null) break;
                    node2 = (Node) treeNode;
                }
                if (n > 10) {
                    JBee.showMessage("Can't start all applets:", false);
                    JBee.showMessage("Maximum allowed is 10", true);
                    JBee.showMessage("", true);
                }
            } else if (object != null) {
                ((JBManagerPanel) object).startApp(node);
            }
        } else {
            String string = ((FlatButton) popupItemSelectedEvent.getComponent()).getLabel();
            if (string.compareTo("Preferences") == 0) {
                SetupFrame setupFrame = new SetupFrame(this, preferences);
                ((Component) setupFrame).setVisible(true);
            } else if (string.compareTo("Help") == 0) {
                JBee.displayURL("http://www.objectbox.com/javabee/help/index.html");
            } else if (string.compareTo("Feedback") == 0) {
                JBee.displayURL("http://www.objectbox.com/javabee/feedback.html");
            } else if (string.compareTo("Kill all") == 0) {
                this.killall();
            } else if (string.startsWith("Search")) {
                Object object = AppRegistry.getInstance().lookup("SearchFrame");
                if (object != null) {
                    ((Component) object).setVisible(true);
                } else {
                    JBSearchPanelFrame jBSearchPanelFrame = new JBSearchPanelFrame();
                    ((Component) jBSearchPanelFrame).setVisible(true);
                }
            }
        }
    }

    public void handleSecurityViolation(SecurityViolationEvent securityViolationEvent) {
        JBLogger.log("WARNING! Security violation.");
        JBLogger.log("Message: " + securityViolationEvent.getSource().toString());
        this.warning_dialog.appendMessage(securityViolationEvent.getSource().toString());
        if (!this.warning_dialog.isVisible()) {
            ((Component) this.warning_dialog).setVisible(true);
            this.warning_dialog.toFront();
            try {
                Thread.currentThread();
                Thread.sleep(5000L);
            } catch (InterruptedException interruptedException) {
            }
        }
    }

    private void initConnections() {
        this.addWindowListener(this);
        this.getButtonRun().addActionListener(this);
    }

    private void initialize() {
        this.getButtonRun().setEnabled(false);
        AppRegistry.getInstance().put("JBee", this);
        String string = System.getProperty("os.name");
        string = string.toLowerCase();
        if (string.indexOf("win") >= 0) {
            OS_type = 1;
            this.doSplash();
        } else {
            OS_type = 2;
        }
        this.loadPreferences();
        this.setName("JavaBeeFrame");
        this.setTitle("JBee");
        this.setLayout(this.getJavaBeeFrameBorderLayout());
        ((Component) this).setBackground(SystemColor.control);
        ((Component) this).setVisible(false);
        ((Component) this).setSize(200, 450);
        this.setResizable(true);
        this.add(this.getGPanel(), "Center");
        this.initConnections();
        ((Component) this).setVisible(true);
        this.setIconImage(JBee.getIcon());
        ((Component) this).setBackground(appcolor);
        this.setupFrame = null;
        this.searchframe = new JBSearchPanelFrame();
        Object object = AppRegistry.getInstance().lookup("Manager");
        this.loadPopupResources();
        ((JBManagerPanel) object).loadTree(loadedroot);
        runningInstanceFrame = this;
        this.splashWait = false;
        this.getButtonRun().setEnabled(true);
        try {
            Math.sin(5.343);
            AudioApp audioApp = new AudioApp();
            audioApp.loadAudioFromResource("/images/intro.au");
            audioApp.play();
            audioApp = null;
        } catch (Exception exception) {
            JBLogger.log("Ex i audio init" + exception);
        }
    }

    public void insertFolderPopup(String string, Node node, Node node2) {
        try {
            Hashtable hashtable = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
            JBPopupMenu jBPopupMenu = (JBPopupMenu) hashtable.get(node);
            JBPopupMenu jBPopupMenu2 = new JBPopupMenu(this.menuParentFrame, this.getButtonRun(), "sub");
            jBPopupMenu2.setItemheight(25);
            jBPopupMenu.addPopupMenu(string, jBPopupMenu2, node2);
            hashtable.put(node2, jBPopupMenu2);
            jBPopupMenu2.addPopupItemSelectedListener(this);
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
        }
    }

    public void killall() {
        class Killhelper
                extends Thread {
            Killhelper() {
            }

            public void run() {
                try {
                    JBee.killall = true;
                    Thread.sleep(5000L);
                    JBee.killall = false;
                } catch (Exception exception) {
                }
            }
        }
        Killhelper killhelper2 = new Killhelper();
        killhelper2.start();
    }

    protected JBPopupMenu loadPopup(String string, Component component) {
        Object object;
        Object object2;
        Object object3 = null;
        try {
            object2 = new FileInputStream(string);
            object = new BufferedInputStream((InputStream) object2);
            ObjectInputStream objectInputStream = new ObjectInputStream((InputStream) object);
            object3 = objectInputStream.readObject();
            loadedroot = (Node) object3;
            objectInputStream.close();
        } catch (Throwable throwable) {
            JBLogger.log("No data file found");
        }
        AppRegistry.getInstance().lookup("Manager");
        try {
            if (object3 == null) {
                loadedroot = new Node("JBee", 2, 4);
            }
            object2 = new JBPopupMenu(this.menuParentFrame, component, "Startmenu", true);
            ((JBPopupMenu) object2).addLogo("/images/beemenulogo.gif");
            ((JBPopupMenu) object2).addPopupItemSelectedListener(this);
            ((JBPopupMenu) object2).setItemheight(25);
            object = AppRegistry.getInstance().lookup("nodehash");
            ((Hashtable) object).clear();
            ((Hashtable) object).put(loadedroot, object2);
            this.addMenuItemsForNode((Node) object3, (JBPopupMenu) object2);
            return (JBPopupMenu) object2;
        } catch (Throwable throwable) {
            JBLogger.log("Exception in getPopup: " + throwable);
            return null;
        }
    }

    protected void loadPopupResources() {
        try {
            String string = JBee.getPreference("javabee_home");
            string = string == null ? System.getProperty("file.separator") : String.valueOf(string) + System.getProperty("file.separator");
            JBLogger.log("Loading resources from: " + string);
            this.popupRun = this.loadPopup(String.valueOf(string) + "jbee.dat", this.getButtonRun());
        } catch (Throwable throwable) {
            JBLogger.log("Exception i JBee::loadPopupResources: " + throwable);
        }
    }

    public void loadPreferences() {
        String string = "javabee.properties";
        this.propertyFile = new File(string);
        String string2 = this.propertyFile.getAbsolutePath().substring(0, this.propertyFile.getAbsolutePath().indexOf(string));
        if (!string2.endsWith(System.getProperty("file.separator"))) {
            string2 = String.valueOf(string2) + System.getProperty("file.separator");
        }
        JBLogger.log("javabee_home: " + string2);
        File file = new File(String.valueOf(string2) + "cache");
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Throwable throwable) {
            JBLogger.log("Failed to create folder " + file);
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(this.propertyFile);
            preferences = new Properties();
            preferences.put("javabee_home", string2);
            preferences.put("usebytecodecache", "true");
            preferences.put("useimagecache", "true");
            preferences.put("checkversion", "true");
            preferences.put("useproxy", "false");
            preferences.load(fileInputStream);
            fileInputStream.close();
            if (preferences.getProperty("useproxy").equals("true")) {
                System.getProperties().put("proxySet", "true");
                System.getProperties().put("proxyHost", preferences.getProperty("proxyserver"));
                System.getProperties().put("proxyPort", preferences.getProperty("proxyport"));
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in loadPreferences: " + throwable);
            preferences = new Properties();
            preferences.put("javabee_home", string2);
        }
    }

    public void loadSecuritySettings() {
        block7:
        {
            try {
                try {
                    String string = JBee.getPreference("javabee_home");
                    if (!string.endsWith(System.getProperty("file.separator"))) {
                        string = String.valueOf(string) + System.getProperty("file.separator");
                    }
                    File file = new File(String.valueOf(string) + "javabee.security");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
                    Object object = objectInputStream.readObject();
                    objectInputStream.close();
                    securitysettings = (Hashtable) object;
                    securitysettings.put("default", JBSecurityModel.getSecurityModel(1));
                } catch (FileNotFoundException fileNotFoundException) {
                    JBLogger.log("Cannot find javabee security file");
                } catch (Throwable throwable) {
                    JBLogger.log("Exception in loadPreferences: " + throwable);
                }
            } catch (Throwable throwable) {
                Object var1_9 = null;
                if (securitysettings == null) {
                    securitysettings = new Hashtable<>();
                    securitysettings.put("default", JBSecurityModel.getSecurityModel(1));
                }
                throw throwable;
            }
            Object var1_10 = null;
            if (securitysettings != null) break block7;
            securitysettings = new Hashtable<>();
            securitysettings.put("default", JBSecurityModel.getSecurityModel(1));
        }
    }

    public void onTreeChanged(TreeChangedEvent treeChangedEvent) {
        try {
            Node node = (Node) treeChangedEvent.getSource();
            if (this.popupRun != null) {
                try {
                    this.popupRun.setVisible(false);
                    this.popupRun.cleanup();
                    this.popupRun.dispose();
                } catch (Throwable throwable) {
                    JBLogger.log("Exception in JBee::onTreeChanged: " + throwable);
                }
            }
            this.popupRun = new JBPopupMenu(this.menuParentFrame, this.getButtonRun(), "Startmenu", true);
            this.popupRun.addPopupItemSelectedListener(this);
            this.popupRun.setItemheight(25);
            this.popupRun.addLogo("/images/beemenulogo.gif");
            Hashtable hashtable = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
            hashtable.clear();
            hashtable.put(node, this.popupRun);
            this.addMenuItemsForNode(node, this.popupRun);
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBee::onTreeChanged: " + throwable);
        }
    }

    public void restoreMenuItems(Node node, JBPopupMenu jBPopupMenu) {
        AppRegistry.getInstance().lookup("Manager");
        try {
            JBPopupMenu jBPopupMenu2 = new JBPopupMenu(this.menuParentFrame, this.getButtonRun(), "sub");
            jBPopupMenu2.setItemheight(25);
            jBPopupMenu2.addPopupItemSelectedListener(this);
            jBPopupMenu.addPopupMenu(node.getText(), jBPopupMenu2, node);
            Hashtable hashtable = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
            hashtable.put(node, jBPopupMenu2);
            this.addMenuItemsForNode(node, jBPopupMenu2);
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBee::restoreMenuItems: " + throwable);
        }
    }

    public void savePreferences() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.propertyFile);
            preferences.save(fileOutputStream, this.propertyFile.getPath());
            fileOutputStream.close();
        } catch (Throwable throwable) {
            JBLogger.log("Cannot write properties (" + throwable + ")");
        }
    }

    public void saveSecuritySettings() {
        try {
            String string = JBee.getPreference("javabee_home");
            if (!string.endsWith(FileSystems.getDefault().getSeparator())) {
                string = String.valueOf(string) + FileSystems.getDefault().getSeparator();
            }
            File file = new File(string + "javabee.security");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
            objectOutputStream.writeObject(securitysettings);
            objectOutputStream.close();
        } catch (Throwable throwable) {
            JBLogger.log("Cannot write security settings (" + throwable + ")");
        }
    }

    public void setSecurity(String object, SecurityManagerIF securityManagerIF, JBManagerPanel jBManagerPanel) {
        if (AppRegistry.getInstance().lookup("Manager") != jBManagerPanel) {
            throw new SecurityException("Hacker attack....");
        }
        securitysettings.put(object, securityManagerIF);
    }

    public void updateName(Node node, String string) {
        try {
            Hashtable hashtable = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
            JBPopupMenu jBPopupMenu = (JBPopupMenu) hashtable.get(node);
            jBPopupMenu.setText(string);
        } catch (Throwable throwable) {
            JBLogger.log("Exception in updateFolderName: " + throwable);
        }
    }

    public void windowActivated(WindowEvent windowEvent) {
        JBLogger.log("JBEE activated");
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

