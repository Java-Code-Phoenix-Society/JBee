/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.util;

import java.util.Enumeration;
import java.util.Vector;

public class SortedVector
        extends Vector {
    protected int heapSize = 0;
    protected BinaryPredicate lessThan;

    public SortedVector() {
    }

    public SortedVector(int n) {
        super(n);
    }

    public SortedVector(int n, int n2) {
        super(n, n2);
    }

    public SortedVector(Vector vector) {
        Enumeration enumeration = vector.elements();
        while (enumeration.hasMoreElements()) {
            this.addElement(enumeration.nextElement());
        }
    }

    private final void buildHeap() {
        this.heapSize = this.elementCount;
        int n = (this.elementCount - 1) / 2;
        while (n >= 0) {
            this.heapify(n);
            --n;
        }
    }

    private final void heapify(int n) {
        int n2 = 2 * n + 1;
        int n3 = 2 * n + 2;
        try {
            int n4 = n;
            if (n2 < this.heapSize && ((Comparable) this.elementData[n]).isLessThan(this.elementData[n2], this.lessThan)) {
                n4 = n2;
            }
            if (n3 < this.heapSize && ((Comparable) this.elementData[n4]).isLessThan(this.elementData[n3], this.lessThan)) {
                n4 = n3;
            }
            if (n4 != n) {
                Object object = this.elementData[n];
                this.elementData[n] = this.elementData[n4];
                this.elementData[n4] = object;
                this.heapify(n4);
            }
        } catch (WrongTypeException wrongTypeException) {
            System.out.println("Wrong data !");
            return;
        }
    }

    public SortedVector merge(SortedVector sortedVector, BinaryPredicate binaryPredicate) {
        int x = 0;
        int y = 0;
        this.lessThan = binaryPredicate;
        SortedVector vector = new SortedVector(50, 50);

        try {
            while (x < super.elementCount && y < sortedVector.elementCount) {
                if (((Comparable) super.elementData[x]).isLessThan(super.elementData[y], this.lessThan)) {
                    vector.addElement(super.elementData[x++]);
                } else {
                    vector.addElement(super.elementData[y++]);
                }
            }
        } catch (WrongTypeException var6) {
            System.out.println("Error in type: merge !");
            return null;
        }

        while (x < super.elementCount) {
            vector.addElement(super.elementData[x++]);
        }

        while (y < sortedVector.elementCount) {
            vector.addElement(super.elementData[y++]);
        }

        return vector;
    }

    public final void sort(BinaryPredicate binaryPredicate) {
        this.lessThan = binaryPredicate;
        this.buildHeap();
        int n = this.elementCount - 1;
        while (n >= 1) {
            Object object = this.elementData[0];
            this.elementData[0] = this.elementData[n];
            this.elementData[n] = object;
            --this.heapSize;
            this.heapify(0);
            --n;
        }
    }
}

