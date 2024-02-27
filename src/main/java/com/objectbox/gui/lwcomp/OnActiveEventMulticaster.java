/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import java.awt.*;

public class OnActiveEventMulticaster
        extends AWTEventMulticaster
        implements OnActiveListener {
    protected OnActiveEventMulticaster(OnActiveListener onActiveListener, OnActiveListener onActiveListener2) {
        super(onActiveListener, onActiveListener2);
    }

    public static OnActiveListener add(OnActiveListener onActiveListener, OnActiveListener onActiveListener2) {
        if (onActiveListener == null) {
            return onActiveListener2;
        }
        if (onActiveListener2 == null) {
            return onActiveListener;
        }
        return new OnActiveEventMulticaster(onActiveListener, onActiveListener2);
    }

    public static OnActiveListener remove(OnActiveListener onActiveListener, OnActiveListener onActiveListener2) {
        return (OnActiveListener) AWTEventMulticaster.removeInternal(onActiveListener, onActiveListener2);
    }

    public void onActive(OnActiveEvent onActiveEvent) {
        ((OnActiveListener) this.a).onActive(onActiveEvent);
        ((OnActiveListener) this.b).onActive(onActiveEvent);
    }
}

