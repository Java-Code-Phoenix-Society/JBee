/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import java.io.FileDescriptor;
import java.io.Serializable;
import java.net.InetAddress;

public interface SecurityManagerIF
        extends Serializable {
    public boolean checkAccept(String var1, int var2);

    public boolean checkAccess(Thread var1);

    public boolean checkAccess(ThreadGroup var1);

    public boolean checkAwtEventQueueAccess();

    public boolean checkConnect(String var1, int var2);

    public boolean checkConnect(String var1, int var2, Object var3);

    public boolean checkCreateClassLoader();

    public boolean checkDelete(String var1);

    public boolean checkExec(String var1);

    public boolean checkExit(int var1);

    public boolean checkLink(String var1);

    public boolean checkListen(int var1);

    public boolean checkMemberAccess(Class var1, int var2);

    public boolean checkMulticast(InetAddress var1);

    public boolean checkMulticast(InetAddress var1, byte var2);

    public boolean checkPackageAccess(String var1);

    public boolean checkPackageDefinition(String var1);

    public boolean checkPrintJobAccess();

    public boolean checkPropertiesAccess();

    public boolean checkPropertyAccess(String var1);

    public boolean checkPropertyAccess(String var1, String var2);

    public boolean checkRead(FileDescriptor var1);

    public boolean checkRead(String var1);

    public boolean checkRead(String var1, Object var2);

    public boolean checkSecurityAccess(String var1);

    public boolean checkSetFactory();

    public boolean checkSystemClipboardAccess();

    public boolean checkTopLevelWindow(Object var1);

    public boolean checkWrite(FileDescriptor var1);

    public boolean checkWrite(String var1);

    public int getID();
}

