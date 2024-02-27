/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import java.awt.*;

public class SavePropertiesEventMulticaster
        extends AWTEventMulticaster
        implements SavePropertiesListener {
    protected SavePropertiesEventMulticaster(SavePropertiesListener savePropertiesListener, SavePropertiesListener savePropertiesListener2) {
        super(savePropertiesListener, savePropertiesListener2);
    }

    public static SavePropertiesListener add(SavePropertiesListener savePropertiesListener, SavePropertiesListener savePropertiesListener2) {
        if (savePropertiesListener == null) {
            return savePropertiesListener2;
        }
        if (savePropertiesListener2 == null) {
            return savePropertiesListener;
        }
        return new SavePropertiesEventMulticaster(savePropertiesListener, savePropertiesListener2);
    }

    public static SavePropertiesListener remove(SavePropertiesListener savePropertiesListener, SavePropertiesListener savePropertiesListener2) {
        return (SavePropertiesListener) AWTEventMulticaster.removeInternal(savePropertiesListener, savePropertiesListener2);
    }

    public void onSaveProperties(SavePropertiesEvent savePropertiesEvent) {
        ((SavePropertiesListener) this.a).onSaveProperties(savePropertiesEvent);
        ((SavePropertiesListener) this.b).onSaveProperties(savePropertiesEvent);
    }
}

