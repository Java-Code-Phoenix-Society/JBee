/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import com.objectbox.loader.JBSecurityManager;
import com.objectbox.runner.model.JBHighSecurity;
import com.objectbox.runner.model.JBMediumSecurity;
import com.objectbox.runner.model.JBNoSecurity;
import com.objectbox.runner.model.SecurityManagerIF;

import java.io.FileDescriptor;
import java.net.InetAddress;

public final class JBSecurityModel
        implements SecurityManagerIF {
    private static JBSecurityModel low = null;
    private static JBSecurityModel medium = null;
    private static JBSecurityModel high = null;
    private SecurityManagerIF managerobject = null;

    private JBSecurityModel() {
    }

    private static JBSecurityModel getHighSecurityModel() {
        if (high == null) {
            high = new JBSecurityModel();
            high.setHandler(new JBHighSecurity((JBSecurityManager) System.getSecurityManager()));
        }
        return high;
    }

    private static JBSecurityModel getLowSecurityModel() {
        if (low == null) {
            low = new JBSecurityModel();
            low.setHandler(new JBNoSecurity((JBSecurityManager) System.getSecurityManager()));
        }
        return low;
    }

    private static JBSecurityModel getMediumSecurityModel() {
        if (medium == null) {
            medium = new JBSecurityModel();
            medium.setHandler(new JBMediumSecurity((JBSecurityManager) System.getSecurityManager()));
        }
        return medium;
    }

    public static JBSecurityModel getSecurityModel(int n) {
        JBSecurityModel jBSecurityModel = null;
        switch (n) {
            case 1: {
                jBSecurityModel = JBSecurityModel.getHighSecurityModel();
                break;
            }
            case 2: {
                jBSecurityModel = JBSecurityModel.getMediumSecurityModel();
                break;
            }
            case 3: {
                jBSecurityModel = JBSecurityModel.getLowSecurityModel();
                break;
            }
            default: {
                jBSecurityModel = JBSecurityModel.getHighSecurityModel();
            }
        }
        return jBSecurityModel;
    }

    public boolean checkAccept(String string, int n) {
        if (this.managerobject != null) {
            return this.managerobject.checkAccept(string, n);
        }
        return false;
    }

    public boolean checkAccess(Thread thread) {
        if (this.managerobject != null) {
            return this.managerobject.checkAccess(thread);
        }
        return false;
    }

    public boolean checkAccess(ThreadGroup threadGroup) {
        if (this.managerobject != null) {
            return this.managerobject.checkAccess(threadGroup);
        }
        return false;
    }

    public boolean checkAwtEventQueueAccess() {
        if (this.managerobject != null) {
            return this.managerobject.checkAwtEventQueueAccess();
        }
        return false;
    }

    public boolean checkConnect(String string, int n) {
        if (this.managerobject != null) {
            return this.managerobject.checkConnect(string, n);
        }
        return false;
    }

    public boolean checkConnect(String string, int n, Object object) {
        if (this.managerobject != null) {
            return this.managerobject.checkConnect(string, n, object);
        }
        return false;
    }

    public boolean checkCreateClassLoader() {
        if (this.managerobject != null) {
            return this.managerobject.checkCreateClassLoader();
        }
        return false;
    }

    public boolean checkDelete(String string) {
        if (this.managerobject != null) {
            return this.managerobject.checkDelete(string);
        }
        return false;
    }

    public boolean checkExec(String string) {
        if (this.managerobject != null) {
            return this.managerobject.checkExec(string);
        }
        return false;
    }

    public boolean checkExit(int n) {
        if (this.managerobject != null) {
            return this.managerobject.checkExit(n);
        }
        return false;
    }

    public boolean checkLink(String string) {
        if (this.managerobject != null) {
            return this.managerobject.checkLink(string);
        }
        return false;
    }

    public boolean checkListen(int n) {
        if (this.managerobject != null) {
            return this.managerobject.checkListen(n);
        }
        return false;
    }

    public boolean checkMemberAccess(Class clazz, int n) {
        if (this.managerobject != null) {
            return this.managerobject.checkMemberAccess(clazz, n);
        }
        return false;
    }

    public boolean checkMulticast(InetAddress inetAddress) {
        if (this.managerobject != null) {
            return this.managerobject.checkMulticast(inetAddress);
        }
        return false;
    }

    public boolean checkMulticast(InetAddress inetAddress, byte by) {
        if (this.managerobject != null) {
            return this.managerobject.checkMulticast(inetAddress, by);
        }
        return false;
    }

    public boolean checkPackageAccess(String string) {
        if (this.managerobject != null) {
            return this.managerobject.checkPackageAccess(string);
        }
        return false;
    }

    public boolean checkPackageDefinition(String string) {
        if (this.managerobject != null) {
            return this.managerobject.checkPackageDefinition(string);
        }
        return false;
    }

    public boolean checkPrintJobAccess() {
        if (this.managerobject != null) {
            return this.managerobject.checkPrintJobAccess();
        }
        return false;
    }

    public boolean checkPropertiesAccess() {
        if (this.managerobject != null) {
            return this.managerobject.checkPropertiesAccess();
        }
        return false;
    }

    public boolean checkPropertyAccess(String string) {
        if (this.managerobject != null) {
            return this.managerobject.checkPropertyAccess(string);
        }
        return false;
    }

    public boolean checkPropertyAccess(String string, String string2) {
        if (this.managerobject != null) {
            return this.managerobject.checkPropertyAccess(string, string2);
        }
        return false;
    }

    public boolean checkRead(FileDescriptor fileDescriptor) {
        if (this.managerobject != null) {
            return this.managerobject.checkRead(fileDescriptor);
        }
        return false;
    }

    public boolean checkRead(String string) {
        if (this.managerobject != null) {
            return this.managerobject.checkRead(string);
        }
        return false;
    }

    public boolean checkRead(String string, Object object) {
        if (this.managerobject != null) {
            return this.managerobject.checkRead(string, object);
        }
        return false;
    }

    public boolean checkSecurityAccess(String string) {
        if (this.managerobject != null) {
            return this.managerobject.checkSecurityAccess(string);
        }
        return false;
    }

    public boolean checkSetFactory() {
        if (this.managerobject != null) {
            return this.managerobject.checkSetFactory();
        }
        return false;
    }

    public boolean checkSystemClipboardAccess() {
        if (this.managerobject != null) {
            return this.managerobject.checkSystemClipboardAccess();
        }
        return false;
    }

    public boolean checkTopLevelWindow(Object object) {
        if (this.managerobject != null) {
            return this.managerobject.checkTopLevelWindow(object);
        }
        return false;
    }

    public boolean checkWrite(FileDescriptor fileDescriptor) {
        if (this.managerobject != null) {
            return this.managerobject.checkWrite(fileDescriptor);
        }
        return false;
    }

    public boolean checkWrite(String string) {
        if (this.managerobject != null) {
            return this.managerobject.checkWrite(string);
        }
        return false;
    }

    public int getID() {
        if (this.managerobject != null) {
            return this.managerobject.getID();
        }
        return -1;
    }

    private void setHandler(SecurityManagerIF securityManagerIF) {
        this.managerobject = securityManagerIF;
    }
}

