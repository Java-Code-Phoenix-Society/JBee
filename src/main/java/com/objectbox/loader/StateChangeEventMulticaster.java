/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.loader;

import java.awt.*;

public class StateChangeEventMulticaster
        extends AWTEventMulticaster
        implements StateChangeListener {
    protected StateChangeEventMulticaster(StateChangeListener stateChangeListener, StateChangeListener stateChangeListener2) {
        super(stateChangeListener, stateChangeListener2);
    }

    public static StateChangeListener add(StateChangeListener stateChangeListener, StateChangeListener stateChangeListener2) {
        if (stateChangeListener == null) {
            return stateChangeListener2;
        }
        if (stateChangeListener2 == null) {
            return stateChangeListener;
        }
        return new StateChangeEventMulticaster(stateChangeListener, stateChangeListener2);
    }

    public static StateChangeListener remove(StateChangeListener stateChangeListener, StateChangeListener stateChangeListener2) {
        return (StateChangeListener) AWTEventMulticaster.removeInternal(stateChangeListener, stateChangeListener2);
    }

    public void onChange(StateChangeEvent stateChangeEvent) {
        ((StateChangeListener) this.a).onChange(stateChangeEvent);
        ((StateChangeListener) this.b).onChange(stateChangeEvent);
    }
}

