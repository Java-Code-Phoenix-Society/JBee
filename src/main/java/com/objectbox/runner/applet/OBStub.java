/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.applet;

import com.objectbox.gui.lwcomp.JBSmallWindow;
import com.objectbox.runner.applet.OBContext;
import com.objectbox.runner.gui.AppletFrame;
import com.objectbox.runner.util.JBLogger;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Component;
import java.net.URL;
import java.util.Hashtable;

public class OBStub
        implements AppletStub {
    private URL codeBase = null;
    private URL documentBase = null;
    private Hashtable parameterhash = null;
    private boolean isactive = false;
    private Applet applet = null;
    private Component parentframe = null;
    private OBContext appletcontext = null;

    public OBStub(Applet applet, Component component, URL uRL, URL uRL2) {
        this.applet = applet;
        this.parentframe = component;
        this.codeBase = uRL;
        this.documentBase = uRL2;
        this.parameterhash = new Hashtable();
    }

    public boolean addParameter(String string, String string2) {
        return this.parameterhash.put(string.toUpperCase(), string2) != null;
    }

    public void appletResize(int n, int n2) {
        if (this.parentframe instanceof AppletFrame) {
            this.parentframe.setSize(n + 4, n2 + AppletFrame.statusheight);
        } else if (this.parentframe instanceof JBSmallWindow) {
            this.parentframe.setSize(n, n2 + JBSmallWindow.header_height);
        }
        this.applet.resize(n, n2);
        JBLogger.log("OBStub appletResize " + n + "," + n2);
    }

    public void finalize() {
    }

    public AppletContext getAppletContext() {
        JBLogger.log("Applet Stub: getAppletContext");
        if (this.appletcontext == null) {
            this.appletcontext = new OBContext(this.applet, this.parentframe);
        } else {
            this.appletcontext.setContext(this.applet, this.parentframe);
        }
        return this.appletcontext;
    }

    public URL getCodeBase() {
        JBLogger.log("Applet Stub: getCodebase " + this.codeBase);
        return this.codeBase;
    }

    public URL getDocumentBase() {
        JBLogger.log("Applet Stub: getDocumentBase " + this.documentBase);
        return this.documentBase;
    }

    public String getParameter(String string) {
        JBLogger.log("Applet Stub: getParameter " + string);
        return (String) this.parameterhash.get(string.toUpperCase());
    }

    public boolean isActive() {
        JBLogger.log("Applet Stub: isActive " + this.isactive);
        return this.isactive;
    }

    public void setIsActive(boolean bl) {
        JBLogger.log("Applet Stub: setIsActive");
        this.isactive = bl;
    }
}

