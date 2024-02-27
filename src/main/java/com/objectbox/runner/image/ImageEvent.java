/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.image;

import java.awt.*;

public class ImageEvent
        extends AWTEvent {
    public static final int IMAGE_CHANGED = 0;
    private static final long serialVersionUID = -7671078796273832149L;
    private SerializableImage image;

    public ImageEvent(Object object, int n, SerializableImage serializableImage) {
        super(object, n);
        this.image = serializableImage;
    }

    public SerializableImage getImage() {
        return this.image;
    }
}

