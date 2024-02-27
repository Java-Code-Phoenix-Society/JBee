/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.util;

import java.net.URL;

public class Hyperlink {
    public URL url = null;
    public int level = 0;
    public boolean local = true;

    public String toString() {
        return this.url.toString();
    }
}

