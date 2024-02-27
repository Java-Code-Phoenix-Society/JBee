/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.loader;

import com.objectbox.runner.gui.JBee;
import com.objectbox.runner.model.SecurityManagerIF;
import com.objectbox.runner.util.JBLogger;

import java.io.FileDescriptor;
import java.net.InetAddress;

public class JBSecurityManager
        extends SecurityManager {
    protected transient SecurityViolationListener aSecurityViolationListener = null;

    public void addSecurityViolationListener(SecurityViolationListener securityViolationListener) {
        this.aSecurityViolationListener = SecurityViolationEventMulticaster.add(this.aSecurityViolationListener, securityViolationListener);
    }

    public void checkAccept(String string, int n) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkAccept failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkAccept(string, n)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkAccept failed"));
                throw new SecurityException("checkAccept for " + string + ":" + n + " failed");
            }
        }
    }

    public void checkAccess(Thread thread) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkAccess failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkAccess(thread)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkAccess failed"));
                throw new SecurityException("checkAccess failed");
            }
        }
    }

    public void checkAccess(ThreadGroup threadGroup) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkAccess failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkAccess(threadGroup)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkAccess failed"));
                throw new SecurityException("checkAccess failed");
            }
        }
    }

    public void checkAwtEventQueueAccess() {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkAwtEventQueueAccess failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkAwtEventQueueAccess()) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkAwtEventQueueAccess failed"));
                throw new SecurityException("checkAwtEventQueueAccess failed");
            }
        }
    }

    public void checkConnect(String string, int n) {
        try {
            JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
            this.inCheck = true;
            if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
                SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
                if (securityManagerIF == null) {
                    throw new SecurityException("checkConnect failed (faulty securityhandler)");
                }
                if (!securityManagerIF.checkConnect(string, n)) {
                    this.fireHandleSecurityViolation(new SecurityViolationEvent("checkConnect failed"));
                    throw new SecurityException("checkConnect failed");
                }
            }
        } catch (Throwable throwable) {
            Object var3_6 = null;
            this.inCheck = false;
            throw throwable;
        }
        Object var3_7 = null;
        this.inCheck = false;
    }

    public void checkConnect(String string, int n, Object object) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkConnect failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkConnect(string, n, object)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkConnect failed"));
                throw new SecurityException("checkConnect failed");
            }
        }
    }

    public void checkCreateClassLoader() {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkCreateClassLoader failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkCreateClassLoader()) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkCreateClassLoader failed"));
                throw new SecurityException("checkCreateClassLoader failed");
            }
        }
    }

    public void checkDelete(String string) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkDelete failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkDelete(string)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkDelete failed"));
                throw new SecurityException("checkDelete failed");
            }
        }
    }

    public void checkExec(String string) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkExec failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkExec(string)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkExec failed"));
                throw new SecurityException("checkExec failed");
            }
        }
    }

    public void checkExit(int n) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkExit failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkExit(n)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkExit failed"));
                throw new SecurityException("checkExit failed");
            }
        }
    }

    public void checkLink(String string) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkLink failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkLink(string)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkLink failed"));
                throw new SecurityException("checkLink failed");
            }
        }
    }

    public void checkListen(int n) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkListen failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkListen(n)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkListen failed"));
                throw new SecurityException("checkListen failed");
            }
        }
    }

    public void checkMemberAccess(Class clazz, int n) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("No member access (faulty securityhandler)");
            }
            if (!securityManagerIF.checkMemberAccess(clazz, n)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkMemberAccess failed"));
                throw new SecurityException("No member access");
            }
        }
    }

    public void checkMulticast(InetAddress inetAddress) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkMulticast failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkMulticast(inetAddress)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkMulticast failed"));
                throw new SecurityException("checkMulticast failed");
            }
        }
    }

    public void checkMulticast(InetAddress inetAddress, byte by) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkMulticast failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkMulticast(inetAddress, by)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkMulticast failed"));
                throw new SecurityException("checkMulticast failed");
            }
        }
    }

    public void checkPackageAccess(String string) {
        if (!string.startsWith("com.objectbox.runner") || !string.startsWith("com.objectbox.loader")) {
            return;
        }
        throw new SecurityException("checkPackageAccess failed");
    }

    public void checkPackageDefinition(String string) {
        if (!string.startsWith("java.")) {
            return;
        }
        if (!string.startsWith("com.objectbox.runner") || !string.startsWith("com.objectbox.loader")) {
            return;
        }
        throw new SecurityException("checkPackageDefinition failed");
    }

    public void checkPrintJobAccess() {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkPrintJobAccess failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkPrintJobAccess()) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkPrintJobAccess failed"));
                throw new SecurityException("checkPrintJobAccess failed");
            }
        }
    }

    public void checkPropertiesAccess() {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkPropertiesAccess failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkPropertiesAccess()) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkPropertiesAccess failed"));
                throw new SecurityException("checkPropertiesAccess failed");
            }
        }
    }

    public void checkPropertyAccess(String string) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkPropertyAccess failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkPropertyAccess(string)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkPropertyAccess failed"));
                throw new SecurityException("checkPropertyAccess failed");
            }
        }
    }

    public void checkPropertyAccess(String string, String string2) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkPropertyAccess failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkPropertyAccess(string, string2)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkPropertyAccess failed"));
                throw new SecurityException("checkPropertyAccess failed");
            }
        }
    }

    public void checkRead(FileDescriptor fileDescriptor) {
        if (this.inClass("com.objectbox.runner.applet.OBContext")) {
            return;
        }
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("No read access (faulty securityhandler)");
            }
            if (!securityManagerIF.checkRead(fileDescriptor)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkRead failed"));
                throw new SecurityException("No read access");
            }
        }
    }

    public void checkRead(String string) {
        if (this.inClass("com.objectbox.runner.applet.OBContext")) {
            return;
        }
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("No read access (faulty securityhandler)");
            }
            if (!securityManagerIF.checkRead(string)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkRead failed"));
                throw new SecurityException("No read access");
            }
        }
    }

    public void checkRead(String string, Object object) {
        if (this.inClass("com.objectbox.runner.applet.OBContext")) {
            return;
        }
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("No read access (faulty securityhandler)");
            }
            if (!securityManagerIF.checkRead(string, object)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkRead failed"));
                throw new SecurityException("No read access");
            }
        }
    }

    public void checkSecurityAccess(String string) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkSecurityAccess failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkSecurityAccess(string)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkSecurityAccess failed"));
                throw new SecurityException("checkSecurityAccess failed");
            }
        }
    }

    public void checkSetFactory() {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("checkSetFactory failed (faulty securityhandler)");
            }
            if (!securityManagerIF.checkSetFactory()) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkSetFactory failed"));
                throw new SecurityException("checkSetFactory failed");
            }
        }
    }

    public void checkSystemClipboardAccess() {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("No clipboard access(faulty securityhandler)");
            }
            if (!securityManagerIF.checkSystemClipboardAccess()) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkSystemClipboardAccess failed"));
                throw new SecurityException("No clipboard access");
            }
        }
    }

    public boolean checkTopLevelWindow(Object object) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                System.err.println("Top level window not allowed");
                return false;
            }
            if (!securityManagerIF.checkTopLevelWindow(object)) {
                System.err.println("Top level window not allowed");
                return false;
            }
        }
        return true;
    }

    public void checkWrite(FileDescriptor fileDescriptor) {
        if (this.inClass("com.objectbox.runner.applet.OBContext")) {
            return;
        }
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("No write access (faulty securityhandler)");
            }
            if (!securityManagerIF.checkWrite(fileDescriptor)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkWrite failed"));
                throw new SecurityException("No write access");
            }
        }
    }

    public void checkWrite(String string) {
        if (this.inClass("com.objectbox.runner.applet.OBContext")) {
            return;
        }
        JBURLClassloader jBURLClassloader = (JBURLClassloader) this.currentClassLoader();
        this.classLoaderDepth();
        if (jBURLClassloader != null && jBURLClassloader.getRootclass().compareTo("com.objectbox.runner.gui.JBee") != 0) {
            SecurityManagerIF securityManagerIF = JBee.getSecurityHandler(jBURLClassloader.getSecurityID());
            if (securityManagerIF == null) {
                throw new SecurityException("No write access (faulty securityhandler)");
            }
            if (!securityManagerIF.checkWrite(string)) {
                this.fireHandleSecurityViolation(new SecurityViolationEvent("checkWrite failed"));
                throw new SecurityException("No write access");
            }
        }
    }

    protected void fireHandleSecurityViolation(SecurityViolationEvent securityViolationEvent) {
        if (this.aSecurityViolationListener == null) {
            return;
        }
        this.aSecurityViolationListener.handleSecurityViolation(securityViolationEvent);
    }

    public void info() {
        JBLogger.log("--Context:" + this.getSecurityContext());
        Class<?>[] classArray = this.getClassContext();
        int n = 0;
        while (n < classArray.length) {
            JBLogger.log(String.valueOf(this.space(n)) + classArray[n]);
            ++n;
        }
        JBLogger.log(this.currentClassLoader() == null ? "system classloader" : this.currentClassLoader().toString());
    }

    public JBURLClassloader preinit(SecurityManagerIF securityManagerIF) {
        ClassLoader classLoader = this.currentClassLoader();
        if (classLoader != null) {
            return (JBURLClassloader) this.currentClassLoader();
        }
        return null;
    }

    public void removeSecurityViolationListener(SecurityViolationListener securityViolationListener) {
        this.aSecurityViolationListener = SecurityViolationEventMulticaster.remove(this.aSecurityViolationListener, securityViolationListener);
    }

    public String space(int n) {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        while (n2 < n) {
            stringBuffer.append(' ');
            ++n2;
        }
        return stringBuffer.toString();
    }
}

