/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.io.Serializable;

public class CustomTextArea
        extends TextArea {
    public CustomTextArea() {
        super("", 0, 0, 3);
        this.initialize();
    }

    public CustomTextArea(int n, int n2) {
        super(n, n2);
    }

    public CustomTextArea(String string) {
        super(string);
    }

    public CustomTextArea(String string, int n, int n2) {
        super(string, n, n2);
    }

    public CustomTextArea(String string, int n, int n2, int n3) {
        super(string, n, n2, n3);
    }

    public static void main(String[] stringArray) {
        try {
            Frame frame;
            Serializable serializable;
            try {
                serializable = Class.forName("com.ibm.uvm.abt.edit.TestFrame");
                frame = (Frame) ((Class) serializable).newInstance();
            } catch (Throwable throwable) {
                frame = new Frame();
            }
            serializable = new CustomTextArea();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of com.objectbox.vplan.gui.CustomTextArea");
        }
    }

    public Dimension getMinimumSize() {
        return new Dimension(250, 50);
    }

    public Dimension getPreferredSize() {
        return new Dimension(250, 50);
    }

    private void handleException(Throwable throwable) {
    }

    private void initialize() {
        this.setName("CustomTextArea");
        this.setSize(232, 72);
    }
}

