/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.FlatButton;
import com.objectbox.gui.lwcomp.JBPopupMenu;
import com.objectbox.gui.lwcomp.PopupItemSelectedEvent;
import com.objectbox.gui.lwcomp.PopupItemSelectedListener;
import com.objectbox.runner.command.*;
import com.objectbox.runner.gui.listbox.ItemTextChangedEvent;
import com.objectbox.runner.gui.listbox.ItemTextChangedListener;
import com.objectbox.runner.gui.listbox.ListItem;
import com.objectbox.runner.gui.tree.Node;
import com.objectbox.runner.gui.tree.TreeBase;
import com.objectbox.runner.gui.tree.TreeNode;
import com.objectbox.runner.model.*;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class JBManagerPanel
        extends Panel
        implements PopupItemSelectedListener,
        ItemTextChangedListener,
        ActionListener,
        ItemListener,
        KeyListener,
        MouseListener {
    public Hashtable images = new Hashtable();
    protected Node rootNode = null;
    protected transient FolderChangedListener aFolderChangedListener = null;
    protected transient PropertyChangeSupport propertyChange;
    protected transient TreeChangedListener aTreeChangedListener = null;
    protected String clip_description = "";
    byte[] node_clipboard = new byte[0];
    private TreeBase ivjTreeBase = null;
    private CommandManager commandmanager = new CommandManager();
    private JBPopupMenu popup_properties = null;
    private Hashtable securityHash = new Hashtable();
    private long timesnap = 0L;
    private NameEditor name_editor = new NameEditor();
    private boolean shouldsave = false;
    private long shouldsave_reqtime = 0L;

    public JBManagerPanel() {
        this.initialize();
    }

    public JBManagerPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    public static void main(String[] stringArray) {
        try {
            CloseableFrame closeableFrame = new CloseableFrame();
            closeableFrame.setLayout(new BorderLayout());
            JBManagerPanel jBManagerPanel = new JBManagerPanel();
            closeableFrame.add(jBManagerPanel, "Center");
            ((Component) closeableFrame).setSize(400, 400);
            ((Component) closeableFrame).setLocation(100, 100);
            ((Component) closeableFrame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManager::main: " + throwable);
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.getTreeBase()) {
            this.connEtoC1(actionEvent);
        }
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().addPropertyChangeListener(propertyChangeListener);
    }

    public void addTreeChangedListener(TreeChangedListener treeChangedListener) {
        this.aTreeChangedListener = TreeChangedEventMulticaster.add(this.aTreeChangedListener, treeChangedListener);
    }

    public void buttonAdd_ActionPerformed(ActionEvent actionEvent) {
        this.newFolder("New Folder", null);
    }

    public void buttonApp_ActionPerformed(ActionEvent actionEvent) {
    }

    public void cleanupTree(Node node) {
        Enumeration enumeration = node.breadthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            Node node2 = (Node) enumeration.nextElement();
            Object object = node2.getData();
            if (object == null) continue;
            JBHolder jBHolder = (JBHolder) object;
            jBHolder.cleanup();
            node2.setData(null);
            jBHolder = null;
        }
        this.getTreeBase().delNodeFromTree(node, false);
    }

    private void connEtoC1(ActionEvent actionEvent) {
        try {
            this.treeBase1_ActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC11(ItemEvent itemEvent) {
        try {
            this.treeBase1_ItemStateChanged(itemEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC2(KeyEvent keyEvent) {
        try {
            this.treeBase_KeyReleased(keyEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC3(KeyEvent keyEvent) {
        try {
            this.treeBase_KeyPressed(keyEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    private void connEtoC5(MouseEvent mouseEvent) {
        try {
            this.treeBase1_MouseClicked(mouseEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void copy() {
        try {
            Node node;
            TreeBase treeBase = this.getTreeBase();
            Object[] objectArray = treeBase.getSelectedObjects();
            if (objectArray == null || objectArray.length < 1) {
                return;
            }
            NamedVector namedVector = new NamedVector();
            int n = 0;
            String string = "";
            n = 0;
            while (n < objectArray.length) {
                node = (Node) ((ListItem) objectArray[n]).getItemData();
                if (n == 0) {
                    string = node.getText();
                }
                if (node != this.rootNode) {
                    namedVector.addElement(node);
                }
                ++n;
            }
            node = (Node) namedVector.elementAt(0);
            if (node != null && node.getType().equals("Folder")) {
                this.node_clipboard = this.saveNodeToMem(node);
                System.out.println(this.node_clipboard.length);
            } else {
                this.node_clipboard = this.saveNodeToMem(namedVector);
            }
            this.clip_description = n == 1 ? string : n + " objects";
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManager::copy: " + throwable);
        }
    }

    public Object createNodeFromMem(byte[] byArray) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Throwable throwable) {
            JBLogger.log("Exception in loadTree: " + throwable);
            return null;
        }
    }

    public void cut() {
        this.copy();
        this.delete(false);
    }

    public void delete(boolean bl) {
        try {
            Node node;
            Hashtable hashtable = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
            TreeBase treeBase = this.getTreeBase();
            Object[] objectArray = treeBase.getSelectedObjects();
            if (objectArray == null || objectArray.length < 1) {
                return;
            }
            Vector<Node> vector = new Vector<Node>();
            int n = 0;
            while (n < objectArray.length) {
                node = (Node) ((ListItem) objectArray[n]).getItemData();
                vector.addElement(node);
                if (node != this.rootNode) {
                    Object object = node.breadthFirstEnumeration();
                    while (((Enumeration<?>) object).hasMoreElements()) {
                        hashtable.remove(((Enumeration<?>) object).nextElement());
                    }
                    object = hashtable.get(node.getParent());
                    ((JBPopupMenu) object).deleteMenuItem(node);
                    Object object2 = node.getData();
                    if (object2 != null) {
                        JBHolder jBHolder = (JBHolder) object2;
                        if (bl) {
                            Object v;
                            Properties properties = jBHolder.getProperties().getHiddenProps();
                            if (properties != null && (v = properties.get("cachenames")) != null) {
                                Vector vector2 = (Vector) v;
                                Enumeration enumeration = vector2.elements();
                                while (enumeration.hasMoreElements()) {
                                    File file;
                                    String string = enumeration.nextElement().toString();
                                    if (string.startsWith("http") || (file = new File(string)).isDirectory()) continue;
                                    if (file.delete()) {
                                        JBLogger.log("Deleted " + file + " from cache");
                                        continue;
                                    }
                                    JBLogger.log("Could not delete" + file + " from cache");
                                }
                            }
                            properties = null;
                        }
                        jBHolder.cleanup();
                        node.setData(null);
                        jBHolder = null;
                    }
                }
                ++n;
            }
            treeBase.deselectAll(false);
            Object object = vector.elements();
            while (((Enumeration<?>) object).hasMoreElements()) {
                node = (Node) ((Enumeration<?>) object).nextElement();
                if (node == this.rootNode) continue;
                treeBase.delNodeFromTree(node, false);
            }
            this.setShouldSave();
            object = AppRegistry.getInstance().lookup("LabelFolder");
            ((Label) object).setText("/");
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManager::delete: " + throwable);
        }
    }

    public Vector deleteBackup() {
        Vector<Object> vector = new Vector<Object>();
        try {
            Hashtable hashtable = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
            TreeBase treeBase = this.getTreeBase();
            Object[] objectArray = treeBase.getSelectedObjects();
            if (objectArray == null || objectArray.length < 1) {
                return vector;
            }
            Node node = (Node) ((ListItem) objectArray[0]).getItemData();
            Node node2 = (Node) node.getPrevSibling();
            JBPopupMenu jBPopupMenu = (JBPopupMenu) hashtable.get(node.getParent());
            vector.addElement(jBPopupMenu);
            Vector<Node> vector2 = new Vector<Node>();
            int n = 0;
            while (n < objectArray.length) {
                Node node3 = (Node) ((ListItem) objectArray[n]).getItemData();
                vector2.addElement(node3);
                ++n;
            }
            vector.addElement(vector2);
            vector.addElement(node.getParent());
            if (node2 == null || node.getParent() != node2.getParent()) {
                vector.addElement("FIRST");
            } else {
                vector.addElement(node2);
            }
            System.out.println("debug (backupVec). " + vector);
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManager::deleteBackup: " + throwable);
        }
        return vector;
    }

    public void fireOnFolderChanged(FolderChangedEvent folderChangedEvent) {
        if (this.aFolderChangedListener == null) {
            return;
        }
        this.aFolderChangedListener.onFolderChanged(folderChangedEvent);
    }

    public void firePropertyChange(String string, Object object, Object object2) {
        this.getPropertyChange().firePropertyChange(string, object, object2);
    }

    public void flatButtonNewApp_ActionPerformed(ActionEvent actionEvent) {
    }

    public CommandManager getCommandManager() {
        return this.commandmanager;
    }

    public NameEditor getNameEditor() {
        return this.name_editor;
    }

    public byte[] getNodeClipboard() {
        return this.node_clipboard;
    }

    protected PropertyChangeSupport getPropertyChange() {
        if (this.propertyChange == null) {
            this.propertyChange = new PropertyChangeSupport(this);
        }
        return this.propertyChange;
    }

    private TreeBase getTreeBase() {
        if (this.ivjTreeBase == null) {
            try {
                this.ivjTreeBase = new TreeBase();
                this.ivjTreeBase.setName("TreeBase");
                this.ivjTreeBase.setLineSpacing(1);
                this.ivjTreeBase.setMultipleSelections(true);
                this.ivjTreeBase.setBackground(Color.white);
                this.ivjTreeBase.setDottedLineFill(1);
                this.ivjTreeBase.setOverlapEditMode(false);
                this.ivjTreeBase.setLeftIndent(1);
                this.ivjTreeBase.setShowDotRect(false);
                this.ivjTreeBase.setFont(new Font("sansserif", 0, 12));
                this.ivjTreeBase.setDottedLineSpace(1);
                this.ivjTreeBase.setVScrollbarWidth(15);
                this.ivjTreeBase.addItemTextChangedListener(this);
                this.ivjTreeBase.setEditMode(false);
                String string = System.getProperty("user.name");
                this.rootNode = new Node(string, 2, 4);
                this.ivjTreeBase.addRootItem(this.rootNode);
                this.ivjTreeBase.expand(this.rootNode, 1, true);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjTreeBase;
    }

    public TreeBase getTreeBasePublic() {
        return this.getTreeBase();
    }

    private void handleException(Throwable throwable) {
        JBLogger.log("--------- UNCAUGHT EXCEPTION ---------");
        throwable.printStackTrace(System.out);
    }

    public void handlePopupSelection(PopupItemSelectedEvent popupItemSelectedEvent) {
    }

    private void initConnections() {
        this.getTreeBase().addActionListener(this);
        this.getTreeBase().addMouseListener(this);
        this.getTreeBase().addItemListener(this);
        this.getTreeBase().addKeyListener(this);
    }

    private void initialize() {
        AppRegistry.getInstance().put("Manager", this);
        this.setName("JBManager");
        this.setLayout(new BorderLayout());
        this.setSize(312, 309);
        this.add(this.getTreeBase(), "Center");
        this.initConnections();
        try {
            this.loadTreeImages();
            this.validate();
            this.startSaver();
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManager::initialize:" + throwable);
        }
    }

    public void installApp(String string, JBProperties jBProperties) {
        JBAppletHolder jBAppletHolder = new JBAppletHolder();
        jBAppletHolder.setProperties(jBProperties);
        this.newApp(string, jBAppletHolder, true);
        this.setShouldSave();
    }

    public void installAppList(Vector vector) {
        try {
            Enumeration enumeration = vector.elements();
            while (enumeration.hasMoreElements()) {
                Vector vector2 = (Vector) enumeration.nextElement();
                String string = (String) vector2.elementAt(0);
                JBProperties jBProperties = (JBProperties) vector2.elementAt(1);
                JBAppletHolder jBAppletHolder = new JBAppletHolder();
                jBAppletHolder.setProperties(jBProperties);
                this.newApp(string, jBAppletHolder, false);
            }
            this.updateVisual();
            this.setShouldSave();
        } catch (Throwable throwable) {
            JBLogger.log("Exception i installAppList: " + throwable);
        }
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getSource() == this.getTreeBase()) {
            this.connEtoC11(itemEvent);
        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getSource() == this.getTreeBase()) {
            this.connEtoC3(keyEvent);
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getSource() == this.getTreeBase()) {
            this.connEtoC2(keyEvent);
        }
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    public void loadTree(Node node) {
        try {
            this.getTreeBase().delNodeFromTree(this.rootNode, true);
            this.getTreeBase().addRootItem(node);
            this.rootNode = node;
            this.getTreeBase().expand(node, 1, true);
            Enumeration enumeration = node.breadthFirstEnumeration();
            while (enumeration.hasMoreElements()) {
                Node node2 = (Node) enumeration.nextElement();
                JBLogger.log(node2.getText());
                node2.getData();
            }
            this.validate();
        } catch (Throwable throwable) {
            JBLogger.log("Exception in loadTree: " + throwable);
        }
    }

    public void loadTreeFromMem(byte[] byArray) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            Node node = (Node) object;
            this.cleanupTree(this.rootNode);
            this.getTreeBase().addRootItem(node);
            this.rootNode = node;
            this.getTreeBase().validate();
        } catch (Throwable throwable) {
            JBLogger.log("Exception in loadTreeFromMem: " + throwable);
        }
    }

    public void loadTreeImages() {
        try {
            Image image = JBee.resources.getImageResource("/images/folder.gif");
            this.getTreeBase().setImageList(image);
            image = JBee.resources.getImageResource("/images/folderopen.gif");
            this.getTreeBase().setImageList(image);
            image = JBee.resources.getImageResource("/images/computer.gif");
            this.getTreeBase().setImageList(image);
            image = JBee.resources.getImageResource("/images/File.gif");
            this.getTreeBase().setImageList(image);
            image = JBee.resources.getImageResource("/images/computer.gif");
            this.getTreeBase().setImageList(image);
        } catch (Throwable throwable) {
            JBLogger.log("Exception in loadTreeImages: " + throwable);
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == this.getTreeBase()) {
            this.connEtoC5(mouseEvent);
        }
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void mousePressed(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void moveMenuItems(Object[] objectArray, Node node) {
        try {
            Hashtable hashtable = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
            JBPopupMenu jBPopupMenu = (JBPopupMenu) hashtable.get(node);
            int n = 0;
            while (n < objectArray.length) {
                Node node2 = (Node) ((ListItem) objectArray[n]).getItemData();
                if (node2 == node) {
                    JBLogger.log("Illegal drag operation");
                    return;
                }
                Node node3 = (Node) node2.getParent();
                JBPopupMenu jBPopupMenu2 = (JBPopupMenu) hashtable.get(node3);
                Object object = jBPopupMenu2.detachMenuItem(node2);
                hashtable.remove(node2);
                hashtable.put(node2, jBPopupMenu);
                if (node2.getType().equals("Folder")) {
                    jBPopupMenu.addPopupMenu(node2.getText(), (JBPopupMenu) object, node2);
                    this.setShouldSave();
                } else if (node2.getType().equals("App")) {
                    jBPopupMenu.addMenuItemWithObject(node2.getText(), node2);
                    this.setShouldSave();
                }
                ++n;
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManagerPanel::moveMenuItems" + throwable.toString());
        }
    }

    public void newApp(String string, JBObjectWithProperties jBObjectWithProperties, boolean bl) {
        try {
            Node node;
            TreeBase treeBase = this.getTreeBase();
            int n = treeBase.getSelectedIndex();
            if (n < 0) {
                treeBase.select(treeBase.getIndex(this.rootNode.getListItem()));
                n = treeBase.getSelectedIndex();
            }
            if ((node = (Node) ((ListItem) treeBase.getItem(n)).getItemData()).getType().compareTo("Folder") != 0) {
                node = (Node) node.getParent();
            }
            Node node2 = new Node(string, 3, 3);
            node2.setType("App");
            if (jBObjectWithProperties != null) {
                node2.setData(jBObjectWithProperties);
            }
            treeBase.insertItem(node2, node, null);
            Node node3 = (Node) node2.getParent();
            if (node3 == this.rootNode) {
                treeBase.expand(this.rootNode, 1, true);
            }
            while (node3 != this.rootNode) {
                treeBase.expand(node3, 1, true);
                node3 = (Node) node3.getParent();
            }
            Hashtable hashtable = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
            JBPopupMenu jBPopupMenu = (JBPopupMenu) hashtable.get(node);
            jBPopupMenu.addMenuItemWithObject(string, node2);
            hashtable.put(node2, jBPopupMenu);
            if (bl) {
                this.updateVisual();
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in newApp: " + throwable);
        }
    }

    public void newFolder(String string, JBObjectWithProperties jBObjectWithProperties) {
        try {
            TreeBase treeBase = this.getTreeBase();
            int n = treeBase.getSelectedIndex();
            if (n < 0) {
                return;
            }
            Node node = (Node) ((ListItem) treeBase.getItem(n)).getItemData();
            if (node.getType().compareTo("Folder") != 0) {
                return;
            }
            Node node2 = new Node(string, 0, 1);
            node2.setType("Folder");
            if (jBObjectWithProperties != null) {
                node2.setData(jBObjectWithProperties);
            }
            treeBase.insertItem(node2, node, null);
            Node node3 = (Node) node2.getParent();
            if (node3 == this.rootNode) {
                treeBase.expand(this.rootNode, 1, true);
            }
            while (node3 != this.rootNode) {
                treeBase.expand(node3, 1, true);
                node3 = (Node) node3.getParent();
            }
            JBee jBee = (JBee) AppRegistry.getInstance().lookup("JBee");
            jBee.insertFolderPopup(string, node, node2);
            this.updateVisual();
            this.setShouldSave();
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
        }
    }

    public void onItemTextChanged(ItemTextChangedEvent itemTextChangedEvent) {
        this.getCommandManager().invokeCommand(new RenameCommand(this.getTreeBase()));
        Node node = this.getTreeBase().getSelectedNode();
        if (node != null) {
            Object object = AppRegistry.getInstance().lookup("LabelFolder");
            if (object != null) {
                Vector<String> vector = new Vector<String>();
                while (node != this.rootNode) {
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
            this.fireOnFolderChanged(new FolderChangedEvent(node));
            this.updateVisual();
        }
    }

    public void onSecurityChanged(SecurityChangedEvent securityChangedEvent) {
        try {
            SecurityPanel securityPanel = (SecurityPanel) securityChangedEvent.getSource();
            TreeBase treeBase = this.getTreeBase();
            int n = treeBase.getSelectedIndex();
            if (n < 0) {
                return;
            }
            Node node = (Node) ((ListItem) treeBase.getItem(n)).getItemData();
            if (node.getType().compareTo("App") == 0) {
                JBHolder jBHolder = (JBHolder) node.getData();
                JBSecurityModel jBSecurityModel = JBSecurityModel.getSecurityModel(securityPanel.getSecurityLevel());
                JBProperties jBProperties = jBHolder.getProperties();
                String string = "-" + jBProperties.getProps().getProperty("codebase").hashCode() + jBProperties.getProps().getProperty("documentbase").hashCode() + jBProperties.getProps().getProperty("code").hashCode();
                Object object = AppRegistry.getInstance().lookup("JBee");
                if (object != null) {
                    ((JBee) object).setSecurity(string, jBSecurityModel, this);
                }
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManager::onSecurityChanged: " + throwable);
        }
    }

    public void paste() {
        try {
            TreeBase var1 = this.getTreeBase();
            int var2 = var1.getSelectedIndex();
            if (var2 < 0) {
                var1.select(0);
            }

            Node var3 = this.getTreeBase().getSelectedNode();
            if (var3.getType().equals("App")) {
                var3 = (Node) var3.getParent();
            }

            JBee var4 = (JBee) AppRegistry.getInstance().lookup("JBee");
            Hashtable var5 = (Hashtable) AppRegistry.getInstance().lookup("nodehash");
            if (var3 != null && this.node_clipboard != null && this.node_clipboard.length > 0) {
                boolean var10000;
                if (var3.getType().compareTo("App") == 0) {
                    var10000 = true;
                } else {
                    var10000 = false;
                }

                Object var6 = this.createNodeFromMem(this.node_clipboard);
                if (var6 instanceof NamedVector) {
                    NamedVector var13 = (NamedVector) var6;
                    Enumeration var14 = var13.elements();

                    while (var14.hasMoreElements()) {
                        Node var9 = (Node) var14.nextElement();
                        var1.insertItem((Node) var9, var3, (Node) null);
                        JBPopupMenu var10 = (JBPopupMenu) var5.get(var3);
                        var10.addMenuItemWithObject(var9.getText(), var9);
                        var5.put(var9, var10);
                    }
                } else {
                    Node var7 = (Node) var6;
                    var1.insertItem((Node) var7, var3, (Node) null);
                    var4.insertFolderPopup(var7.getText(), var3, var7);
                    JBPopupMenu var8 = (JBPopupMenu) var5.get(var7);
                    var4.addMenuItemsForNode(var7, var8);
                }
            }

            Node var12 = var3;
            if (var3 == this.rootNode) {
                var1.expand(this.rootNode, 1, true);
            }

            while (var12 != this.rootNode) {
                var1.expand(var12, 1, true);
                var12 = (Node) var12.getParent();
            }

            this.setShouldSave();
            var1.validate();
        } catch (Throwable var11) {
            JBLogger.log("Exception in JBManager::paste: " + var11);
        }

    }


    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().removePropertyChangeListener(propertyChangeListener);
    }

    public void removeTreeChangedListener(TreeChangedListener treeChangedListener) {
        this.aTreeChangedListener = TreeChangedEventMulticaster.remove(this.aTreeChangedListener, treeChangedListener);
    }

    public void restoreVec(Vector vector) {
        try {
            if (vector.size() != 4) {
                return;
            }
            JBPopupMenu jBPopupMenu = (JBPopupMenu) vector.elementAt(0);
            Vector vector2 = (Vector) vector.elementAt(1);
            Node node = (Node) vector.elementAt(2);
            Object e = vector.elementAt(3);
            Node node2 = null;
            if (e instanceof Node) {
                node2 = (Node) e;
            }
            JBee jBee = (JBee) AppRegistry.getInstance().lookup("JBee");
            Node node3 = new Node();
            int n = 0;
            Enumeration enumeration = vector2.elements();
            while (enumeration.hasMoreElements()) {
                Node node4 = (Node) enumeration.nextElement();
                if (node4.getType().equals("App")) {
                    System.out.println(String.valueOf(node4.getType()) + ": " + node4.getData().toString());
                }
                Node node5 = new Node(node4.getText(), node4.getImage(), node4.getExpandedImage());
                node5.setType(node4.getType());
                if (node4.getData() != null) {
                    node5.setData(node4.getData());
                }
                node4.setData(null);
                this.getTreeBase().insertItem(node5, node, node2);
                node2 = node4;
                ++n;
                if (node4.getType().equals("Folder")) {
                    jBee.restoreMenuItems(node4, jBPopupMenu);
                    break;
                }
                node3.addChild(node4);
            }
            if (node3.hasChildren()) {
                jBee.addMenuItemsForNode(node3, jBPopupMenu);
            }
            this.updateVisual();
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManagerPanel::restoreVec: " + throwable);
        }
    }

    public void runCommand(String string) {
        try {
            Serializable serializable;
            Serializable serializable2;
            Node node = this.getTreeBase().getSelectedNode();
            if (!string.startsWith("Undo")) {
                if (string.startsWith("Copy")) {
                    if (node == null) {
                        return;
                    }
                    this.getCommandManager().invokeCommand(new CopyCommand(this));
                } else if (string.startsWith("Brows")) {
                    if (node == null) {
                        return;
                    }
                    serializable2 = this.getTreeBase().getSelectedNode();
                    serializable = (JBHolder) ((Node) serializable2).getData();
                    JBee.displayURL(String.valueOf(((JBHolder) serializable).getProperties().getProps().get("documentbase")) + ((JBHolder) serializable).getProperties().getProps().get("webpage"));
                } else if (string.startsWith("Paste")) {
                    if (node == null) {
                        return;
                    }
                    this.getCommandManager().invokeCommand(new PasteCommand(this));
                    this.getTreeBase().repaint();
                    this.getTreeBase().validate();
                } else if (string.startsWith("Cut")) {
                    if (node == null) {
                        return;
                    }
                    this.getCommandManager().invokeCommand(new CutCommand(this));
                    this.getTreeBase().repaint();
                    this.getTreeBase().validate();
                } else if (string.startsWith("Delete")) {
                    if (node == null) {
                        return;
                    }
                    this.getCommandManager().invokeCommand(new DeleteCommand(this));
                    this.getTreeBase().repaint();
                    this.getTreeBase().validate();
                } else if (string.startsWith("NewFolder")) {
                    if (node == null) {
                        this.getTreeBase().select(0);
                    }
                    this.getCommandManager().invokeCommand(new NewFolderCommand(this, "New Folder", null));
                    this.getTreeBase().repaint();
                    this.getTreeBase().validate();
                } else if (string.startsWith("Prefe")) {
                    if (node != this.rootNode) {
                        this.showProperties();
                    } else {
                        serializable2 = new SetupFrame(JBee.getRunningInstanceFrame(), JBee.preferences);
                        ((Component) serializable2).setVisible(true);
                        ((Window) serializable2).toFront();
                    }
                } else if (string.startsWith("Run")) {
                    Node node2 = this.getTreeBase().getSelectedNode();
                    if (node2.getType().compareTo("Folder") == 0) {
                        Node node3 = (Node) node2.getFirstChild();
                        int n = 0;
                        while (node3 != null) {
                            if (++n > 10) break;
                            this.startApp(node3);
                            TreeNode treeNode = node3.getNextSibling();
                            if (treeNode == null) break;
                            node3 = (Node) treeNode;
                        }
                        if (n > 10) {
                            JBee.showMessage("Can't start all applets:", false);
                            JBee.showMessage("Maximum allowed is 10", true);
                            JBee.showMessage("", true);
                        }
                    } else {
                        this.startApp(node2);
                    }
                    return;
                }
            }
            serializable2 = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonUndo");
            serializable = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonPaste");
            FlatButton flatButton = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonCopy");
            FlatButton flatButton2 = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonDelete");
            FlatButton flatButton3 = (FlatButton) AppRegistry.getInstance().lookup("FlatButtonCut");
            if (this.getCommandManager().getHistoryLength() < 1) {
                ((FlatButton) serializable2).setEnabled(false);
                ((Component) serializable2).repaint();
            } else {
                ((FlatButton) serializable2).setEnabled(true);
                ((Component) serializable2).validate();
            }
            if (this.getNodeClipboard().length > 0 && this.getTreeBase().getSelectedIndex() > 0) {
                ((FlatButton) serializable).setEnabled(true);
                ((Component) serializable).repaint();
            } else {
                ((FlatButton) serializable).setEnabled(false);
                ((Component) serializable).repaint();
            }
            if (this.getTreeBase().getSelectedIndex() < 1) {
                flatButton.setEnabled(false);
                flatButton2.setEnabled(false);
                flatButton3.setEnabled(false);
                flatButton.repaint();
                flatButton2.repaint();
                flatButton3.repaint();
            } else {
                flatButton.setEnabled(true);
                flatButton2.setEnabled(true);
                flatButton3.setEnabled(true);
                flatButton.repaint();
                flatButton2.repaint();
                flatButton3.repaint();
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception i runCommand: " + throwable);
        }
    }

    public void save(boolean bl) {
        try {
            this.shouldsave = false;
            String string = JBee.preferences.getProperty("javabee_home");
            string = string == null ? System.getProperty("file.separator") : String.valueOf(string) + System.getProperty("file.separator");
            String string2 = "~jbee.dat";
            if (bl) {
                string2 = "jbee.dat";
            }
            File file = new File(String.valueOf(string) + string2);
            this.saveTree(file);
            Object object = AppRegistry.getInstance().lookup("SearchFrame");
            if (object != null) {
                ((JBSearchPanelFrame) object).saveState();
            }
            JBLogger.log("Tree saved OK");
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManagerPanel::save. " + throwable);
        }
    }

    public byte[] saveNodeToMem(Object object) {
        try {
            long l = System.currentTimeMillis();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(40000);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            if (object != null) {
                objectOutputStream.writeObject(object);
            } else {
                JBLogger.log("Missing node?!?");
            }
            objectOutputStream.close();
            double d = (double) (System.currentTimeMillis() - l) / 1000.0;
            JBLogger.log("saveNodeToMem: " + d + " secs");
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManager::saveNodeToMem: " + throwable);
            return null;
        }
    }

    public void saveTree(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
            if (this.rootNode != null) {
                objectOutputStream.writeObject(this.rootNode);
            } else {
                JBLogger.log("Missing rootnode?!?");
            }
            objectOutputStream.close();
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManager::saveTree: " + throwable);
        }
    }

    public byte[] saveTreeToMem() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            if (this.rootNode != null) {
                objectOutputStream.writeObject(this.rootNode);
            } else {
                JBLogger.log("Missing rootnode?!?");
            }
            objectOutputStream.close();
            byte[] byArray = byteArrayOutputStream.toByteArray();
            JBLogger.log("saveTreeToMem: " + byArray.length);
            return byArray;
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManager::saveTreeToMem: " + throwable);
            return null;
        }
    }

    public void saveTreeToMem(OutputStream outputStream) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            if (this.rootNode != null) {
                objectOutputStream.writeObject(this.rootNode);
            } else {
                JBLogger.log("Missing rootnode?!?");
            }
            objectOutputStream.close();
        } catch (Throwable throwable) {
            JBLogger.log("Exception in JBManager::saveTree: " + throwable);
        }
    }

    public void setShouldSave() {
        this.shouldsave = true;
        this.shouldsave_reqtime = System.currentTimeMillis();
    }

    public boolean shouldSave() {
        if (System.currentTimeMillis() - this.shouldsave_reqtime > 15000L) {
            this.shouldsave_reqtime = System.currentTimeMillis();
            return this.shouldsave;
        }
        return false;
    }

    public void showProperties() {
        JBObjectWithProperties jBObjectWithProperties;
        JBProperties jBProperties;
        Node node = this.getTreeBase().getSelectedNode();
        Object object = node.getData();
        if (object != null && object instanceof JBObjectWithProperties && (jBProperties = (jBObjectWithProperties = (JBObjectWithProperties) object).getProperties()) != null) {
            jBProperties.getJBeeProps().put("Name", node.getText());
            JBPropertyEditor jBPropertyEditor = jBProperties.getPropertyEditor();
            if (jBPropertyEditor != null) {
                jBPropertyEditor.edit(jBProperties);
                ((Frame) jBPropertyEditor).toFront();
            }
        }
    }

    public void startApp(Node node) {
        try {
            JBHolder jBHolder = (JBHolder) node.getData();
            Object object = AppRegistry.getInstance().lookup("JBee");
            if (object != null) {
                ThreadGroup threadGroup = ((JBee) object).getThreadGroup();
                if (threadGroup == null) {
                    threadGroup = new ThreadGroup("jbee");
                }
                BeanRunner beanRunner = jBHolder.run(threadGroup);
                JBProcessModel jBProcessModel = ((JBee) object).getProcessModel();
                JBProcessButton jBProcessButton = new JBProcessButton();
                jBProcessButton.setLabel(node.getText());
                jBProcessButton.setVisual(true);
                jBProcessButton.setState(1);
                jBProcessModel.addBeanRunner(beanRunner, jBProcessButton);
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception i JBManager startApp: " + throwable);
        }
    }

    public void startSaver() {
        class Saver
                extends Thread {
            Saver() {
            }

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000L);
                        JBManagerPanel jBManagerPanel = (JBManagerPanel) AppRegistry.getInstance().lookup("Manager");
                        if (jBManagerPanel != null && jBManagerPanel.shouldSave()) {
                            jBManagerPanel.save(false);
                        }
                    } catch (InterruptedException e) {
                        // Restore the interrupted status
                        Thread.currentThread().interrupt();
                        break; // Exit the loop if interrupted
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle other exceptions
                    }
                }
            }
        }
        Saver saver = new Saver();
        saver.setPriority(1);
        saver.start();
    }

    public void treeBase_KeyPressed(KeyEvent keyEvent) {
        int n = keyEvent.getKeyCode();
        if (n == 10) {
            Node node = this.getTreeBase().getSelectedNode();
            if (node != null && node != this.rootNode && node.getType().equals("App")) {
                this.runCommand("Prefe");
                return;
            }
            if (node != null && node.getType().equals("Folder")) {
                this.name_editor.setFolderName(node.getText());
                ((Component) this.name_editor).setVisible(true);
                this.name_editor.toFront();
            }
            return;
        }
        String string = "";
        if (keyEvent.isControlDown() && !keyEvent.isShiftDown()) {
            switch (n) {
                case 67:
                case 155: {
                    string = "Copy";
                    break;
                }
                case 69: {
                    string = "Run";
                    break;
                }
                case 85: {
                    string = "Undo";
                    break;
                }
                case 86: {
                    string = "Paste";
                    break;
                }
                case 80: {
                    string = "Prefe";
                    break;
                }
                case 88: {
                    string = "Cut";
                    break;
                }
                default: {
                    return;
                }
            }
        }
        if (!keyEvent.isControlDown() && keyEvent.isShiftDown()) {
            switch (n) {
                case 155: {
                    string = "Paste";
                }
            }
        }
        if (!keyEvent.isControlDown() && !keyEvent.isShiftDown()) {
            switch (n) {
                case 127: {
                    string = "Delete";
                }
            }
        }
        if (string.compareTo("") == 0) {
            return;
        }
        long l = System.currentTimeMillis();
        if (this.timesnap != 0L && l - this.timesnap < 500L) {
            return;
        }
        this.timesnap = l;
        this.runCommand(string);
    }

    public void treeBase_KeyReleased(KeyEvent keyEvent) {
        Node node;
        int n = keyEvent.getKeyCode();
        if ((n == 38 || n == 40 || n == 37 || n == 39) && (node = this.getTreeBase().getSelectedNode()) != null && node != this.rootNode) {
            if (node.getType().equals("Folder")) {
                this.name_editor.setFolderName(node.getText());
            } else if (node.getType().equals("App") && JBAppletProperties.editorFrame.isVisible()) {
                this.showProperties();
            }
        }
    }

    public void treeBase1_ActionPerformed(ActionEvent actionEvent) {
        try {
            Node node;
            Object object = actionEvent.getSource();
            TreeBase treeBase = this.getTreeBase();
            if (object == treeBase && actionEvent.getActionCommand().equals("Drag_Drop") && (node = treeBase.getNodeAt(treeBase.getDropTargetItem())).getType().compareTo("Folder") == 0) {
                this.moveMenuItems(treeBase.getSelectedObjects(), node);
                treeBase.moveItems(treeBase.getSelectedItems(), treeBase.getDropTargetItem());
                treeBase.select(treeBase.getDropTargetItem());
                Object object2 = AppRegistry.getInstance().lookup("LabelFolder");
                if (object2 != null) {
                    Vector<String> vector = new Vector<String>();
                    while (node != this.rootNode) {
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
                    ((Label) object2).setText(string);
                }
                this.updateVisual();
                this.shouldsave = true;
            }
        } catch (Throwable throwable) {
            JBLogger.log("JBManagerPanel::treeBase1_ActionPerformed" + throwable.toString());
        }
    }

    public void treeBase1_ItemStateChanged(ItemEvent itemEvent) {
        TreeBase treeBase = (TreeBase) itemEvent.getSource();
        Object labelFolder = AppRegistry.getInstance().lookup("LabelFolder");
        if (labelFolder != null) {
            Node selectedNode = treeBase.getSelectedNode();
            if (selectedNode != null) {
                Vector vector;
                for (vector = new Vector(); selectedNode != this.rootNode; selectedNode = (Node) selectedNode.getParent()) {
                    if (selectedNode.getType().compareTo("Folder") == 0) {
                        vector.insertElementAt(selectedNode.getText(), 0);
                    }
                }

                String s = "";

                for (Enumeration var7 = vector.elements(); var7.hasMoreElements(); s = s + "/" + var7.nextElement()) {
                }

                if (s.equals("")) {
                    s = "/";
                }

                ((Label) labelFolder).setText(s);
            }
        }

        Object[] selectedObjects = treeBase.getSelectedObjects();
        boolean b = false;
        Node node;
        if (selectedObjects.length > 1) {
            node = (Node) ((ListItem) selectedObjects[0]).getItemData();
            int var14 = node.getDistanceFromRoot();

            for (int i = 0; i < selectedObjects.length; ++i) {
                Node iNode = (Node) ((ListItem) selectedObjects[i]).getItemData();
                if (!iNode.isMultiSelectable()) {
                    b = true;
                    break;
                }

                if (((Node) iNode.getParent()).getType().compareTo("Folder") != 0) {
                    b = true;
                    break;
                }

                if (iNode.getDistanceFromRoot() != var14) {
                    b = true;
                    break;
                }
            }

            if (b) {
                JBLogger.log("Invalid selection");
                treeBase.deselectAll(true);
                return;
            }

            JBLogger.log("Selected: " + selectedObjects.length + " nodes");
        }

        node = treeBase.getSelectedNode();
        if (node != null) {
            treeBase.m_oldName = node.getText();
            if (node.getType().compareTo("Folder") != 0) {
                node = (Node) node.getParent();
            }

            this.fireOnFolderChanged(new FolderChangedEvent(node));
        }
    }

    public void treeBase1_MouseClicked(MouseEvent mouseEvent) {
        try {
            Node node = this.getTreeBase().getSelectedNode();
            if (node == this.rootNode) {
                return;
            }
            if (node != null && node.getType().equals("App") && (mouseEvent.getClickCount() == 2 || JBAppletProperties.editorFrame.isVisible())) {
                this.runCommand("Prefe");
            }
            if (node != null && node.getType().equals("Folder")) {
                this.name_editor.setFolderName(node.getText());
                if (mouseEvent.getClickCount() == 2) {
                    ((Component) this.name_editor).setVisible(true);
                    this.name_editor.toFront();
                }
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in treeBase1_MouseClicked: " + throwable);
        }
    }

    public void updateVisual() {
        this.getTreeBase().doLayout();
        this.getTreeBase().updateScrollbar(true);
        this.getTreeBase().repaint();
    }
}

