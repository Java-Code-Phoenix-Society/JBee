/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.loader;

import java.awt.*;

public class SecurityViolationEventMulticaster
        extends AWTEventMulticaster
        implements SecurityViolationListener {
    protected SecurityViolationEventMulticaster(SecurityViolationListener securityViolationListener, SecurityViolationListener securityViolationListener2) {
        super(securityViolationListener, securityViolationListener2);
    }

    public static SecurityViolationListener add(SecurityViolationListener securityViolationListener, SecurityViolationListener securityViolationListener2) {
        if (securityViolationListener == null) {
            return securityViolationListener2;
        }
        if (securityViolationListener2 == null) {
            return securityViolationListener;
        }
        return new SecurityViolationEventMulticaster(securityViolationListener, securityViolationListener2);
    }

    public static SecurityViolationListener remove(SecurityViolationListener securityViolationListener, SecurityViolationListener securityViolationListener2) {
        return (SecurityViolationListener) AWTEventMulticaster.removeInternal(securityViolationListener, securityViolationListener2);
    }

    public void handleSecurityViolation(SecurityViolationEvent securityViolationEvent) {
        ((SecurityViolationListener) this.a).handleSecurityViolation(securityViolationEvent);
        ((SecurityViolationListener) this.b).handleSecurityViolation(securityViolationEvent);
    }
}

