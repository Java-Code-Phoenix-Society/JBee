package com.objectbox.jadvertise;

import com.objectbox.loader.JBURLClassloader;
import com.objectbox.runner.applet.OBContext;
import com.objectbox.runner.applet.OBStub;
import com.objectbox.runner.gui.AppRegistry;
import com.objectbox.runner.gui.JBManagerPanel;
import com.objectbox.runner.gui.JBee;
import com.objectbox.runner.model.JBSecurityModel;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Properties;

public class JAdPanel extends Container implements MouseListener {
    protected transient PropertyChangeSupport propertyChange;
    private Dimension thesize = new Dimension(10, 10);
    private Dimension adsize = new Dimension(10, 10);
    private String codebase = "";
    private String classname = "";
    private String jararchives = "";
    private boolean useimagecache = false;
    private boolean usebytecodecache = true;
    private URL adurl;
    private boolean useindirectadurl = true;
    private String cachedir = "";
    private String[] fieldParameters = new String[1];
    private boolean appletloaded = false;
    private String adspotid;
    private Font f = new Font("SansSerif", 1, 16);
    private boolean isAppletLoaded = false;

    public JAdPanel() {
        this.initialize();
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
            serializable = new JAdPanel();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            System.err.println("Exception occurred in main() of java.awt.Container");
            throwable.printStackTrace(System.out);
        }
    }

    static void access$loadApplet_impl(JAdPanel jAdPanel) {
        jAdPanel.loadApplet_impl();
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().addPropertyChangeListener(propertyChangeListener);
    }

    private void connEtoC1(MouseEvent mouseEvent) {
        try {
            this.jadPanel_MouseClicked(mouseEvent);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
    }

    public void firePropertyChange(String string, Object object, Object object2) {
        this.getPropertyChange().firePropertyChange(string, object, object2);
    }

    public Dimension getAdsize() {
        return this.adsize;
    }

    public void setAdsize(Dimension dimension) {
        this.adsize = dimension;
        this.validate();
    }

    public String getAdspotid() {
        return this.adspotid;
    }

    public void setAdspotid(String string) {
        this.adspotid = string;
    }

    public URL getAdurl() {
        return this.adurl;
    }

    public void setAdurl(URL uRL) {
        this.adurl = uRL;
    }

    public String getCachedir() {
        return this.cachedir;
    }

    public void setCachedir(String string) {
        this.cachedir = string;
    }

    public String getClassname() {
        return this.classname;
    }

    public void setClassname(String string) {
        this.classname = string;
    }

    public String getCodebase() {
        return this.codebase;
    }

    public void setCodebase(String string) {
        this.codebase = string;
    }

    public String getJararchives() {
        return this.jararchives;
    }

    public void setJararchives(String string) {
        this.jararchives = string;
    }

    public Dimension getMaximumSize() {
        return this.adsize;
    }

    public Dimension getMinimumSize() {
        return this.adsize;
    }

    public String[] getParameters() {
        return this.fieldParameters;
    }

    public void setParameters(String[] stringArray) {
        String[] stringArray2 = this.fieldParameters;
        this.fieldParameters = stringArray;
        this.firePropertyChange("parameters", stringArray2, stringArray);
    }

    public String getParameters(int n) {
        return this.getParameters()[n];
    }

    public Dimension getPreferredSize() {
        return this.adsize;
    }

    protected PropertyChangeSupport getPropertyChange() {
        if (this.propertyChange == null) {
            this.propertyChange = new PropertyChangeSupport(this);
        }
        return this.propertyChange;
    }

    public boolean getUsebytecodecache() {
        return this.usebytecodecache;
    }

    public void setUsebytecodecache(boolean bl) {
        this.usebytecodecache = bl;
    }

    public boolean getUseimagecache() {
        return this.useimagecache;
    }

    public void setUseimagecache(boolean bl) {
        this.useimagecache = bl;
    }

    public boolean getUseindirectadurl() {
        return this.useindirectadurl;
    }

    public void setUseindirectadurl(boolean bl) {
        this.useindirectadurl = bl;
    }

    private void handleException(Throwable throwable) {
    }

    private void initConnections() {
        this.addMouseListener(this);
    }

    private void initialize() {
        this.setName("com.objectbox.jadvertise.JAdPanel");
        this.setLayout(new BorderLayout());
        this.setSize(160, 120);
        this.initConnections();
    }

    public boolean isAppletLoaded() {
        return this.isAppletLoaded;
    }

    public void jadPanel_MouseClicked(MouseEvent mouseEvent) {
        if (!this.appletloaded) {
            try {
                Launcher.openURL("http://www.objectbox.com/jad");
            } catch (Exception exception) {
            }
        }
    }

    public void loadApplet() {
        class Trun
                extends Thread {
            /* synthetic */ JAdPanel this$0;

            Trun(JAdPanel jAdPanel) {
                this.this$0 = jAdPanel;
            }

            public void run() {
                JAdPanel.access$loadApplet_impl(this.this$0);
            }
        }
        Trun trun2 = new Trun(this);
        trun2.start();
    }

    private void loadApplet_impl() {
        try {
            Object object;
            String string;
            Object object2;
            Object object3;
            Object object4;
            Object object5;
            Object object6;
            Serializable serializable;
            if (this.adurl != null) {
                serializable = new Properties();
                object6 = this.adurl.openConnection();
                object5 = ((URLConnection) object6).getInputStream();
                ((Properties) serializable).load((InputStream) object5);
                ((InputStream) object5).close();
                object4 = ((Properties) serializable).getProperty("forward");
                if (object4 != null) {
                    object6 = new URL((String) object4).openConnection();
                    object5 = ((URLConnection) object6).getInputStream();
                    ((Hashtable) serializable).clear();
                    ((Properties) serializable).load((InputStream) object5);
                    ((InputStream) object5).close();
                }
                object3 = new String[((Hashtable) serializable).size()];
                int n = 0;

                this.setParameters((String[]) object3);
                this.setClassname(((Properties) serializable).getProperty("classname"));
                this.setCodebase(((Properties) serializable).getProperty("codebase"));
                this.setJararchives(((Properties) serializable).getProperty("archives"));
            }
            this.isAppletLoaded = true;
            serializable = new JBURLClassloader(this.cachedir);
            ((JBURLClassloader) serializable).setCacheOn(this.usebytecodecache);
            object6 = "-" + this.codebase.hashCode() + this.classname.hashCode();
            ((JBURLClassloader) serializable).setSecurityID((String) object6);
            ((JBURLClassloader) serializable).setCodebase(this.codebase);
            object5 = new URL(this.codebase);
            ((JBURLClassloader) serializable).setServer(((URL) object5).getHost());
            ((JBURLClassloader) serializable).setRootclass(this.classname);
            ((JBURLClassloader) serializable).setJarfile(this.jararchives);
            ((JBURLClassloader) serializable).readCacheFromFile();
            ((JBURLClassloader) serializable).setOwner(this);
            object4 = AppRegistry.getInstance().lookup("JBee");
            object3 = AppRegistry.getInstance().lookup("Manager");
            JBSecurityModel jBSecurityModel = JBSecurityModel.getSecurityModel(2);
            System.out.println("Sec man: " + jBSecurityModel);
            ((JBee) object4).setSecurity((String) object6, jBSecurityModel, (JBManagerPanel) object3);
            object2 = ((JBURLClassloader) serializable).resolveClass(this.classname);
            string = (String) ((Class) object2).newInstance();
            object = string;
            OBStub oBStub = new OBStub((Applet) object, this, new URL(this.codebase), new URL(this.codebase));
            ((Applet) object).setStub(oBStub);
            ((OBContext) oBStub.getAppletContext()).setUseImageCache(this.useimagecache);
            if (this.fieldParameters != null) {
                int n = 0;
                while (n < this.fieldParameters.length) {
                    int n2 = this.fieldParameters[n].indexOf("=");
                    String string2 = this.fieldParameters[n].substring(0, n2);
                    String string3 = this.fieldParameters[n].substring(n2 + 1);
                    oBStub.addParameter(string2, string3);
                    ++n;
                }
            }
            if (oBStub.getParameter("height") != null) {
                this.adsize.height = Integer.parseInt(oBStub.getParameter("height"));
            }
            ((Component) object).setSize(this.adsize);
            this.add((Component) object, "Center");
            ((Applet) object).init();
            ((Applet) object).start();
            this.appletloaded = true;
            Container container = this.getParent();
            while (container.getParent() != null) {
                container = container.getParent();
            }
            ((Component) container).validate();
            ((JBURLClassloader) serializable).saveme();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == this) {
            this.connEtoC1(mouseEvent);
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

    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (!this.appletloaded) {
            graphics.setColor(Color.orange);
            graphics.fillRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
            graphics.setColor(Color.black);
            graphics.setFont(this.f);
            FontMetrics fontMetrics = graphics.getFontMetrics(this.f);
            graphics.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
            graphics.drawString("JAdvertise (c)", this.getSize().width / 2 - fontMetrics.stringWidth("JAdvertise (c)") / 2, this.getSize().height / 2 - fontMetrics.getHeight() / 2);
            graphics.drawString("objectBOX 1999", this.getSize().width / 2 - fontMetrics.stringWidth("objectBOX 1999") / 2, this.getSize().height / 2 + fontMetrics.getHeight() / 2);
        }
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().removePropertyChangeListener(propertyChangeListener);
    }

    public void setParameters(int n, String string) {
        String[] stringArray = this.fieldParameters;
        this.fieldParameters[n] = string;
        this.firePropertyChange("parameters", stringArray, this.fieldParameters);
    }
}

