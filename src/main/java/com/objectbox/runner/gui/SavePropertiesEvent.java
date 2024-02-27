/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import java.util.EventObject;

public class SavePropertiesEvent
        extends EventObject {
    private String name = "default";

    public SavePropertiesEvent(Object object) {
        super(object);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }
}

