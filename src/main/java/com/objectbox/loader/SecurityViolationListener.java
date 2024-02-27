/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.loader;

import java.util.EventListener;

public interface SecurityViolationListener
        extends EventListener {
    public void handleSecurityViolation(SecurityViolationEvent var1);
}

