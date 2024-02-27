/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import java.awt.*;
import java.util.EventObject;

public class PopupItemSelectedEvent
        extends EventObject {
    protected Component component = null;

    public PopupItemSelectedEvent(Object object) {
        super(object);
    }

    public Component getComponent() {
        return this.component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}

