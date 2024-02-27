/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.JBSmallWindow;
import com.objectbox.loader.JBURLClassloader;
import com.objectbox.runner.applet.OBContext;
import com.objectbox.runner.applet.OBStub;
import com.objectbox.runner.model.JBProperties;
import com.objectbox.runner.util.JBLogger;

import java.applet.Applet;
import java.awt.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class BeanRunner
        extends Thread {
    String classname = "";
    String server = "";
    String codebase = "";
    String jar = "";
    Hashtable parameters = new Hashtable();
    Properties properties = new Properties();
    Properties jbee_props = new Properties();
    Properties hidden_props = new Properties();
    String documentbase = "";
    IAppletWindow frame = null;
    Thread beanthread = null;
    boolean isDone = false;
    private JBURLClassloader loader = null;
    private long delay = 250L;
    private Applet applettorun;
    private OBContext appletcontext;
    private OBStub mystub;

    public BeanRunner() {
        this.init();
    }

    public BeanRunner(Runnable runnable) {
        super(runnable);
        this.init();
    }

    public BeanRunner(Runnable runnable, String string) {
        super(runnable, string);
        this.init();
    }

    public BeanRunner(String string) {
        super(string);
        this.init();
    }

    public BeanRunner(ThreadGroup threadGroup, Runnable runnable) {
        super(threadGroup, runnable);
        this.init();
    }

    public BeanRunner(ThreadGroup threadGroup, Runnable runnable, String string) {
        super(threadGroup, runnable, string);
        this.init();
    }

    public BeanRunner(ThreadGroup threadGroup, String string) {
        super(threadGroup, string);
        this.init();
    }

    public void destroy() {
        ThreadGroup threadGroup = this.getThreadGroup();
        int n = threadGroup.activeCount();
        Thread[] threadArray = new Thread[n + 5];
        n = threadGroup.enumerate(threadArray, true);
        int n2 = 0;
        while (n2 < n) {
            JBLogger.log("Stopping: " + threadArray[n2].getName());
            ++n2;
        }
        super.destroy();
    }

    public void finalize() {
    }

    public OBContext getAppletContext() {
        return this.appletcontext;
    }

    public long getDelay() {
        return this.delay;
    }

    public void setDelay(long l) {
        this.delay = l;
    }

    public Container getFrame() {
        return (Container) ((Object) this.frame);
    }

    public synchronized boolean getIsDone() {
        if (JBee.killall) {
            this.isDone = true;
        }
        return this.isDone;
    }

    public void setIsDone(boolean bl) {
        this.isDone = bl;
    }

    public JBURLClassloader getLoader() throws NullPointerException {
        if (this.loader == null) {
            throw new NullPointerException("Loader is null");
        }
        return this.loader;
    }

    private void init() {
        this.setDaemon(true);
    }

    public void invokeApplet(JBURLClassloader loader, String classname, String server, String codebase,
                             String jar, Hashtable parameters, String documentbase) {
        Object objInst = null;
        String classNames = "";
        String serverString = "";
        String url = "";

        String hashCodes;
        try {
            hashCodes = "-" + codebase.hashCode() + documentbase.hashCode() + classname.hashCode();
            loader.setSecurityID(hashCodes);
            classNames = classname.endsWith(".class") ? classname.substring(0, classname.lastIndexOf(".")) : classname;
            url = !documentbase.startsWith("http") && !documentbase.startsWith("file") ? server + "/" + documentbase : documentbase;
            if (!url.endsWith("/")) {
                url = url + "/";
            }

            if (!codebase.startsWith("http") && !codebase.startsWith("file")) {
                if (codebase.startsWith("/")) {
                    serverString = server + (codebase.compareTo(".") == 0 ? "" : codebase);
                } else {
                    serverString = url + (codebase.compareTo(".") == 0 ? "" : codebase);
                }
            } else {
                serverString = codebase;
            }

            if (!serverString.endsWith("/")) {
                serverString = serverString + "/";
            }

            loader.setCodebase(serverString);
            loader.setServer(server);
            loader.setRootclass(classNames);
            loader.setJarfile(jar);
            loader.readCacheFromFile();
            loader.setOwner(this);
            loader.setState(1);
            Class aClass = loader.resolveClass(classNames);
            loader.setState(5);
            objInst = aClass.newInstance();
        } catch (Throwable t) {
            JBLogger.log("BeanRunner invokeapplet " + t);
            loader.setState(4);
        }

        if (objInst == null) {
            hashCodes = "Can't start applet: \n" + classname + "\n\nCheck the applet properties.";
            JBee.showMessage(hashCodes, false);
            this.setIsDone(true);
        } else {
            hashCodes = (String) this.jbee_props.get("Window type");
            if (hashCodes == null) {
                hashCodes = "Frame";
            }

            String validUrl = loader.makeValidUrl(classname);
            if (hashCodes.equals("Small")) {
                this.frame = new JBSmallWindow(JBee.getRunningInstanceFrame());
            } else if (hashCodes.equals("Standard")) {
                this.frame = new AppletFrame(validUrl);
            }

            ((Container) this.frame).setSize(200, 200);
            if (objInst instanceof Applet) {
                this.applettorun = (Applet) objInst;
                this.mystub = null;

                try {
                    this.mystub = new OBStub(this.applettorun, (Container) this.frame,
                            new URL(serverString), new URL(url));
                    this.applettorun.setStub(this.mystub);
                    this.appletcontext = (OBContext) this.mystub.getAppletContext();
                    String useimagecache = JBee.getPreference("useimagecache");
                    if (useimagecache != null && useimagecache.compareTo("true") != 0) {
                        this.appletcontext.setUseImageCache(false);
                    } else {
                        this.appletcontext.setUseImageCache(true);
                    }
                } catch (Throwable t) {
                    JBLogger.log(t.toString());
                }

                Enumeration enumeration = this.parameters.keys();

                while (enumeration.hasMoreElements()) {
                    String var15 = (String) enumeration.nextElement();
                    String var16 = (String) this.parameters.get(var15);
                    this.mystub.addParameter(var15, var16);
                }

                Object xPos = this.jbee_props.get("xPos");
                Object yPos = this.jbee_props.get("yPos");
                int x = 0;
                int y = 0;
                if (xPos != null && yPos != null) {
                    x = Integer.parseInt((String) xPos);
                    y = Integer.parseInt((String) yPos);
                }

                Point point = new Point(x, y);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                String width = this.properties.get("width").toString();
                String height = this.properties.get("height").toString();

                int w2;
                int h2;
                try {
                    double w;
                    if (width.endsWith("%")) {
                        w = (double) Integer.parseInt(width.substring(0, width.lastIndexOf("%"))) / 100.0;
                        w2 = (int) ((double) screenSize.width * w);
                    } else {
                        w2 = Integer.parseInt(width);
                    }

                    if (height.endsWith("%")) {
                        w = (double) Integer.parseInt(height.substring(0, height.lastIndexOf("%"))) / 100.0;
                        h2 = (int) ((double) screenSize.height * w);
                    } else {
                        h2 = Integer.parseInt(height);
                    }
                } catch (Throwable var31) {
                    w2 = screenSize.width - 200;
                    h2 = screenSize.height - 200;
                    JBLogger.log("Applet dimension problem:" + var31);
                }

                Dimension dimension = new Dimension(w2, h2);
                if (point != null && dimension != null) {
                    JBLogger.log("Restoring window persistent state");
                    this.applettorun.setSize(dimension);
                    ((Container) this.frame).setLocation(point);
                } else {
                    if (this.applettorun.getSize().width == 0 || this.applettorun.getSize().height == 0) {
                        int h3 = 100;
                        int w3 = 100;

                        try {
                            w3 = Integer.parseInt((String) this.properties.get("width"));
                            h3 = Integer.parseInt((String) this.properties.get("height"));
                        } catch (NumberFormatException var30) {
                            JBLogger.log(var30.toString());
                        }

                        this.applettorun.setSize(new Dimension(w3, h3));
                    }

                    Dimension dimension1 = new Dimension(this.applettorun.getSize().width,
                            this.applettorun.getSize().height + AppletFrame.statusheight);
                    JBLogger.log("Applet size: " + dimension1);
                    ((Container) this.frame).setSize(dimension1);
                }

                this.frame.addApplet(this.applettorun);
                ((Container) this.frame).addNotify();
                this.applettorun.addNotify();
                if (this.frame instanceof JBSmallWindow) {
                    ((JBSmallWindow) this.frame).validate();
                }

                try {
                    this.applettorun.init();
                } catch (Throwable var29) {
                    JBLogger.log(var29.toString());
                }

                try {
                    this.applettorun.start();
                    this.mystub.setIsActive(true);
                } catch (Throwable var28) {
                    JBLogger.log(var28.toString());
                    this.setIsDone(true);
                }

                ((Container) this.frame).setVisible(true);
                ((Container) this.frame).validate();
                this.applettorun.requestFocus();
            }

        }
    }

    public void kill() {
        try {
            if (this.frame != null) {
                this.jbee_props.put("xPos", String.valueOf(((Container) this.frame).getLocation().x));
                this.jbee_props.put("yPos", String.valueOf(((Container) this.frame).getLocation().y));
                this.properties.put("height", String.valueOf(this.applettorun.getSize().height));
                this.properties.put("width", String.valueOf(this.applettorun.getSize().width));
                ((Container) this.frame).setVisible(false);
                JBLogger.log("Saving window persistent state");
            }

            if (this.loader != null) {
                this.loader.saveme();
                this.loader.dispose();
                this.loader.setState(6);
                Vector var2 = this.getAppletContext().getCacheNames();
                Vector var3 = this.loader.getCacheNames();
                Enumeration var4 = var3.elements();

                while (var4.hasMoreElements()) {
                    var2.addElement(var4.nextElement());
                }

                System.out.println("Resources: " + var2.toString());
                this.hidden_props.put("cachenames", var2);
                var2 = null;
            }

            ThreadGroup var10 = this.getThreadGroup();
            int var11 = var10.activeCount();
            Thread[] var12 = new Thread[var11 + 5];
            var11 = var10.enumerate(var12, true);

            for (int var5 = 0; var5 < var11; ++var5) {
                if (var12[var5] != this) {
                    JBLogger.log("Stopping: " + var12[var5].getName() + var12[var5].isAlive());
                    var12[var5].stop();
                    var12[var5] = null;
                }
            }
        } catch (Exception var8) {
            JBLogger.log("Beanrunner.kill " + var8.toString());
            this.setIsDone(true);
        } finally {
            this.frame.kill();
            if (this.frame instanceof Frame) {
                ((Frame) this.frame).dispose();
                ((Frame) this.frame).removeNotify();
            } else if (this.frame instanceof JBSmallWindow) {
                ((JBSmallWindow) this.frame).dispose();
                ((JBSmallWindow) this.frame).removeNotify();
            }

            this.setIsDone(true);
            this.appletcontext = null;
            this.mystub = null;
            this.applettorun = null;
            this.frame = null;
            this.loader = null;
            this.parameters = null;
            this.properties = null;
            System.gc();
            JBLogger.log("Kill in finally block");
        }

    }

    public void run() {
        try {
            this.setIsDone(false);
            this.invokeApplet(this.loader, this.classname, this.server, this.codebase, this.jar, this.parameters, this.documentbase);
            this.loader.setState(0);
            while (!this.getIsDone() && !this.frame.isDone()) {
                try {
                    Thread.sleep(this.delay);
                } catch (InterruptedException interruptedException) {
                    JBLogger.log("BeanRunner.run " + interruptedException.toString());
                    this.setIsDone(true);
                    break;
                }
            }
            JBLogger.log("BeanRunner.run start to kill");
            this.kill();
            JBLogger.log("BeanRunner.run done killing");
        } catch (Throwable throwable) {
            JBLogger.log("BeanRunner.run " + throwable.toString());
        }
    }

    public void set(JBURLClassloader jBURLClassloader, JBProperties jBProperties) {
        this.properties = jBProperties.getProps();
        this.parameters = jBProperties.getParameters();
        this.jbee_props = jBProperties.getJBeeProps();
        this.hidden_props = jBProperties.getHiddenProps();
        String string = (String) ((Hashtable) this.properties).get("code");
        String string2 = (String) ((Hashtable) this.properties).get("codebase");
        String string3 = (String) ((Hashtable) this.properties).get("host");
        String string4 = (String) ((Hashtable) this.properties).get("archive");
        String string5 = (String) ((Hashtable) this.properties).get("documentbase");
        String cfr_ignored_0 = (String) ((Hashtable) this.properties).get("width");
        String cfr_ignored_1 = (String) ((Hashtable) this.properties).get("height");
        String cfr_ignored_2 = (String) ((Hashtable) this.properties).get("object");
        String cfr_ignored_3 = (String) ((Hashtable) this.properties).get("alt");
        String cfr_ignored_4 = (String) ((Hashtable) this.properties).get("align");
        this.classname = string;
        this.server = string3;
        this.codebase = string2;
        this.jar = string4;
        this.documentbase = string5;
        this.loader = jBURLClassloader;
    }

    void setAppletcontext(OBContext oBContext) {
        this.appletcontext = oBContext;
    }

    public void showThreads() {
        ThreadGroup threadGroup = this.getThreadGroup();
        int n = threadGroup.activeCount();
        Thread[] threadArray = new Thread[n + 5];
        n = threadGroup.enumerate(threadArray, true);
        int n2 = 0;
        while (n2 < n) {
            JBLogger.log(n2 + " Thread: " + threadArray[n2].getName() + " " + threadArray[n2].isAlive());
            ++n2;
        }
    }
}

