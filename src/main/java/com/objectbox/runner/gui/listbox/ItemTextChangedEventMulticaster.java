/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.listbox;

import java.awt.*;

public class ItemTextChangedEventMulticaster
        extends AWTEventMulticaster
        implements ItemTextChangedListener {
    protected ItemTextChangedEventMulticaster(ItemTextChangedListener itemTextChangedListener, ItemTextChangedListener itemTextChangedListener2) {
        super(itemTextChangedListener, itemTextChangedListener2);
    }

    public static ItemTextChangedListener add(ItemTextChangedListener itemTextChangedListener, ItemTextChangedListener itemTextChangedListener2) {
        if (itemTextChangedListener == null) {
            return itemTextChangedListener2;
        }
        if (itemTextChangedListener2 == null) {
            return itemTextChangedListener;
        }
        return new ItemTextChangedEventMulticaster(itemTextChangedListener, itemTextChangedListener2);
    }

    public static ItemTextChangedListener remove(ItemTextChangedListener itemTextChangedListener, ItemTextChangedListener itemTextChangedListener2) {
        return (ItemTextChangedListener) AWTEventMulticaster.removeInternal(itemTextChangedListener, itemTextChangedListener2);
    }

    public void onItemTextChanged(ItemTextChangedEvent itemTextChangedEvent) {
        ((ItemTextChangedListener) this.a).onItemTextChanged(itemTextChangedEvent);
        ((ItemTextChangedListener) this.b).onItemTextChanged(itemTextChangedEvent);
    }
}

