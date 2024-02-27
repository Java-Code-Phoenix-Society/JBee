/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.applet;

import com.objectbox.runner.beans.DownloadView;
import com.objectbox.runner.gui.AppletFrame;
import com.objectbox.runner.gui.JBee;
import com.objectbox.runner.util.JBLogger;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class OBContext
        implements AppletContext {
    private static Hashtable applethash = new Hashtable();
    private Component frame = null;
    private boolean useImageCache = true;
    private Vector soundlist = new Vector();
    private Vector cachenames = new Vector();

    public OBContext(Applet applet, Component component) {
        this.frame = component;
    }

    public Applet getApplet(String string) {
        JBLogger.log("OBContext getApplet");
        return (Applet) applethash.get(string);
    }

    public Enumeration getApplets() {
        return applethash.elements();
    }

    public AudioClip getAudioClip(URL uRL) {
        try {
            AudioApp audioApp = new AudioApp(uRL.toString());
            this.soundlist.addElement(audioApp);
            this.cachenames.addElement(audioApp.getCacheName());
            return audioApp;
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
            return null;
        }
    }

    public Vector getCacheNames() {
        return this.cachenames;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Image getImage(URL uRL) {
        JBLogger.log("AppletContext: getImage " + uRL);
        Image image = null;
        try {
            String string;
            File file;
            String string2 = uRL.toString();
            String string3 = string2.hashCode() + "_" + string2.substring(string2.lastIndexOf("/") + 1, string2.length());
            String string4 = JBee.getPreference("javabee_home");
            if (!string4.endsWith(System.getProperty("file.separator"))) {
                string4 = String.valueOf(string4) + System.getProperty("file.separator");
            }
            if ((file = new File(string = String.valueOf(string4) + "cache", string3)).exists() && this.useImageCache) {
                JBLogger.log("Using cached image");
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] byArray = new byte[(int) file.length()];
                fileInputStream.read(byArray);
                fileInputStream.close();
                return Toolkit.getDefaultToolkit().createImage(byArray);
            }
            JBLogger.log("Downloading image");
            URLConnection uRLConnection = uRL.openConnection();
            InputStream inputStream = uRLConnection.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                while (true) {
                    byteArrayOutputStream.write(dataInputStream.readByte());
                    DownloadView.addBytes(1L);
                }
            } catch (IOException iOException) {
                byte[] byArray = byteArrayOutputStream.toByteArray();
                ((OutputStream) byteArrayOutputStream).close();
                if (!this.useImageCache) return Toolkit.getDefaultToolkit().createImage(byArray);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(byArray);
                fileOutputStream.close();
                this.cachenames.addElement(file.toString());
                return Toolkit.getDefaultToolkit().createImage(byArray);
            }
        } catch (Exception exception) {
            JBLogger.log("Exception i OBContext getImage " + exception.toString());
        }
        return image;
    }

    public boolean getUseImageCache() {
        return this.useImageCache;
    }

    public void setUseImageCache(boolean bl) {
        this.useImageCache = bl;
    }

    public void setContext(Applet applet, Component component) {
        this.frame = component;
    }

    public void showDocument(URL uRL) {
        JBLogger.log("AppletConext: showDocument");
        JBee.setUrlToShow(uRL.toString());
    }

    public void showDocument(URL uRL, String string) {
        JBLogger.log("AppletConext: showDocument");
        JBee.setUrlToShow(uRL.toString());
    }

    public void showStatus(String string) {
        if (this.frame instanceof AppletFrame) {
            ((AppletFrame) this.frame).setStatus(string);
        }
    }

    @Override
    public void setStream(String key, InputStream stream) throws IOException {

    }

    @Override
    public InputStream getStream(String key) {
        return null;
    }

    @Override
    public Iterator<String> getStreamKeys() {
        return null;
    }

    public void stopSounds() {
        Enumeration enumeration = this.soundlist.elements();
        while (enumeration.hasMoreElements()) {
            AudioApp audioApp = (AudioApp) enumeration.nextElement();
            audioApp.stop();
            JBLogger.log("Stopping applet sound");
            audioApp = null;
        }
        this.soundlist.removeAllElements();
    }
}

