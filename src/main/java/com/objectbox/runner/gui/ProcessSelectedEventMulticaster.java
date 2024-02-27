/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import java.awt.*;

public class ProcessSelectedEventMulticaster
        extends AWTEventMulticaster
        implements ProcessSelectedListener {
    protected ProcessSelectedEventMulticaster(ProcessSelectedListener processSelectedListener, ProcessSelectedListener processSelectedListener2) {
        super(processSelectedListener, processSelectedListener2);
    }

    public static ProcessSelectedListener add(ProcessSelectedListener processSelectedListener, ProcessSelectedListener processSelectedListener2) {
        if (processSelectedListener == null) {
            return processSelectedListener2;
        }
        if (processSelectedListener2 == null) {
            return processSelectedListener;
        }
        return new ProcessSelectedEventMulticaster(processSelectedListener, processSelectedListener2);
    }

    public static ProcessSelectedListener remove(ProcessSelectedListener processSelectedListener, ProcessSelectedListener processSelectedListener2) {
        return (ProcessSelectedListener) AWTEventMulticaster.removeInternal(processSelectedListener, processSelectedListener2);
    }

    public void onProcessSelected(ProcessSelectedEvent processSelectedEvent) {
        ((ProcessSelectedListener) this.a).onProcessSelected(processSelectedEvent);
        ((ProcessSelectedListener) this.b).onProcessSelected(processSelectedEvent);
    }
}

