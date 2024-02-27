/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.util;

public interface Comparable {
    public boolean isEqualTo(Object var1) throws WrongTypeException;

    public boolean isLessThan(Object var1, BinaryPredicate var2) throws WrongTypeException;
}

