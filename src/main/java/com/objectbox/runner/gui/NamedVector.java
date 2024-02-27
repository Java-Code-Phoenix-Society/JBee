/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import java.util.Vector;

public class NamedVector
        extends Vector {
    public String name;

    public NamedVector() {
    }

    public NamedVector(int n) {
        super(n);
    }

    public NamedVector(int n, int n2) {
        super(n, n2);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }
}

