/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.image;

public class ImageNotFoundException
        extends RuntimeException {
    private Exception originalException = null;

    public ImageNotFoundException(Exception exception, String string) {
        super(string);
        if (exception != null) {
            this.originalException = exception;
        }
    }

    public ImageNotFoundException(String string) {
        this(null, string);
    }

    public Exception getOriginalException() {
        return this.originalException;
    }
}

