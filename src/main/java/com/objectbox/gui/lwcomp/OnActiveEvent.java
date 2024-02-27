/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import java.util.EventObject;

public class OnActiveEvent
        extends EventObject {
    boolean exited = false;

    public OnActiveEvent(Object object) {
        super(object);
    }

    public OnActiveEvent(Object object, boolean bl) {
        super(object);
        this.exited = bl;
    }
}

