/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import com.objectbox.loader.JBSecurityManager;
import com.objectbox.runner.model.SecurityManagerIF;

import java.io.FileDescriptor;
import java.net.InetAddress;

public class JBNoSecurity
        implements SecurityManagerIF {
    static final long serialVersionUID = -123456789L;
    transient JBSecurityManager sm = null;

    public JBNoSecurity(JBSecurityManager jBSecurityManager) {
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
        return true;
    }

    public boolean checkConnect(String string, int n) {
        return true;
    }

    public boolean checkConnect(String string, int n, Object object) {
        return true;
    }

    public boolean checkCreateClassLoader() {
        return true;
    }

    public boolean checkDelete(String string) {
        return true;
    }

    public boolean checkExec(String string) {
        return true;
    }

    public boolean checkExit(int n) {
        return true;
    }

    public boolean checkLink(String string) {
        return true;
    }

    public boolean checkListen(int n) {
        return true;
    }

    public boolean checkMemberAccess(Class clazz, int n) {
        return true;
    }

    public boolean checkMulticast(InetAddress inetAddress) {
        return true;
    }

    public boolean checkMulticast(InetAddress inetAddress, byte by) {
        return true;
    }

    public boolean checkPackageAccess(String string) {
        return true;
    }

    public boolean checkPackageDefinition(String string) {
        return true;
    }

    public boolean checkPrintJobAccess() {
        return true;
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
        return true;
    }

    public boolean checkRead(String string, Object object) {
        return true;
    }

    public boolean checkSecurityAccess(String string) {
        return true;
    }

    public boolean checkSetFactory() {
        return true;
    }

    public boolean checkSystemClipboardAccess() {
        return true;
    }

    public boolean checkTopLevelWindow(Object object) {
        return true;
    }

    public boolean checkWrite(FileDescriptor fileDescriptor) {
        return true;
    }

    public boolean checkWrite(String string) {
        return true;
    }

    public int getID() {
        return 3;
    }

    public void initstate() {
    }
}

