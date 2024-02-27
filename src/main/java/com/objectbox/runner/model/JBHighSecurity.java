/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import com.objectbox.loader.JBSecurityManager;
import com.objectbox.loader.JBURLClassloader;
import com.objectbox.runner.gui.JBee;
import com.objectbox.runner.model.SecurityManagerIF;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Properties;

public class JBHighSecurity
        implements SecurityManagerIF {
    static final long serialVersionUID = -123456789L;
    transient Properties securitysettings = new Properties();
    transient JBSecurityManager sm = null;

    public JBHighSecurity(JBSecurityManager jBSecurityManager) {
        this.sm = jBSecurityManager;
        this.initstate();
    }

    public boolean checkAccept(String string, int n) {
        return true;
    }

    public boolean checkAccess(Thread thread) {
        return true;
    }

    public boolean checkAccess(ThreadGroup threadGroup) {
        return true;
    }

    public boolean checkAwtEventQueueAccess() {
        return false;
    }

    public boolean checkConnect(String string, int n) {
        JBURLClassloader jBURLClassloader = this.sm.preinit(this);
        String string2 = jBURLClassloader.getServer();
        if (string.equals(string2)) {
            return true;
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(string);
            InetAddress inetAddress2 = InetAddress.getByName(string2);
            if (inetAddress.equals(inetAddress2)) {
                return true;
            }
        } catch (UnknownHostException unknownHostException) {
        }
        return false;
    }

    public boolean checkConnect(String string, int n, Object object) {
        JBURLClassloader jBURLClassloader = this.sm.preinit(this);
        String string2 = jBURLClassloader.getServer();
        if (string.equals(string2)) {
            return true;
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(string);
            InetAddress inetAddress2 = InetAddress.getByName(string2);
            if (inetAddress.equals(inetAddress2)) {
                return true;
            }
        } catch (UnknownHostException unknownHostException) {
        }
        return false;
    }

    public boolean checkCreateClassLoader() {
        return false;
    }

    public boolean checkDelete(String string) {
        return false;
    }

    public boolean checkExec(String string) {
        return false;
    }

    public boolean checkExit(int n) {
        return false;
    }

    public boolean checkLink(String string) {
        return string.equals("mmedia");
    }

    public boolean checkListen(int n) {
        return false;
    }

    public boolean checkMemberAccess(Class clazz, int n) {
        return true;
    }

    public boolean checkMulticast(InetAddress inetAddress) {
        return false;
    }

    public boolean checkMulticast(InetAddress inetAddress, byte by) {
        return false;
    }

    public boolean checkPackageAccess(String string) {
        return false;
    }

    public boolean checkPackageDefinition(String string) {
        return false;
    }

    public boolean checkPrintJobAccess() {
        return false;
    }

    public boolean checkPropertiesAccess() {
        return true;
    }

    public boolean checkPropertyAccess(String string) {
        return true;
    }

    public boolean checkPropertyAccess(String string, String string2) {
        return true;
    }

    public boolean checkRead(FileDescriptor fileDescriptor) {
        return true;
    }

    public boolean checkRead(String string) {
        return false;
    }

    public boolean checkRead(String string, Object object) {
        return false;
    }

    public boolean checkSecurityAccess(String string) {
        return false;
    }

    public boolean checkSetFactory() {
        return false;
    }

    public boolean checkSystemClipboardAccess() {
        return false;
    }

    public boolean checkTopLevelWindow(Object object) {
        return true;
    }

    public boolean checkWrite(FileDescriptor fileDescriptor) {
        return true;
    }

    public boolean checkWrite(String string) {
        return false;
    }

    public int getID() {
        return 1;
    }

    public void initstate() {
        ((Hashtable) this.securitysettings).put("AcceptHosts", "*");
        ((Hashtable) this.securitysettings).put("ThreadAccess", "*");
        ((Hashtable) this.securitysettings).put("AwtEventQueueAccess", "Yes");
        ((Hashtable) this.securitysettings).put("Connect", "");
        ((Hashtable) this.securitysettings).put("CreateClassLoader", "No");
        String string = JBee.getPreference("javabee_home");
        ((Hashtable) this.securitysettings).put("SafeDirectories", string == null ? "" : string);
        ((Hashtable) this.securitysettings).put("Exit", "");
        ((Hashtable) this.securitysettings).put("Link", "");
        ((Hashtable) this.securitysettings).put("Listen", "");
        ((Hashtable) this.securitysettings).put("MemberAccess", "");
        ((Hashtable) this.securitysettings).put("Multicast", "");
        ((Hashtable) this.securitysettings).put("PackageAccess", "");
        ((Hashtable) this.securitysettings).put("PackageDefinition", "");
        ((Hashtable) this.securitysettings).put("PrintJobAccess", "");
        ((Hashtable) this.securitysettings).put("PropertiesAccess", "");
        ((Hashtable) this.securitysettings).put("PropertyAccess", "");
        ((Hashtable) this.securitysettings).put("Read", "");
        ((Hashtable) this.securitysettings).put("SecurityAccess", "");
        ((Hashtable) this.securitysettings).put("SetFactory", "");
        ((Hashtable) this.securitysettings).put("SystemClipboardAccess", "");
        ((Hashtable) this.securitysettings).put("TopLevelWindow", "");
        ((Hashtable) this.securitysettings).put("Write", "");
    }
}

