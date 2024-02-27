/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.image;

import com.objectbox.runner.image.ImageEvent;

import java.util.EventListener;

public interface ImageListener
        extends EventListener {
    public void imageUpdated(ImageEvent var1);
}

