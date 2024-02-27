/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import com.objectbox.util.BinaryPredicate;
import com.objectbox.util.Comparable;
import com.objectbox.util.WrongTypeException;

import java.util.Vector;

public class PropertyRow
        extends Vector
        implements Comparable {
    public PropertyRow() {
    }

    public PropertyRow(int n) {
        super(n);
    }

    public PropertyRow(int n, int n2) {
        super(n, n2);
    }

    public boolean isEqualTo(Object object) throws WrongTypeException {
        String string;
        Vector vector = (Vector) object;
        String string2 = vector.firstElement().toString().toUpperCase();
        return string2.compareTo(string = this.firstElement().toString().toUpperCase()) == 0;
    }

    public boolean isLessThan(Object object, BinaryPredicate binaryPredicate) throws WrongTypeException {
        String string;
        Vector vector = (Vector) object;
        String string2 = vector.firstElement().toString().toUpperCase();
        return string2.compareTo(string = this.firstElement().toString().toUpperCase()) > 0;
    }
}

