/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.command;

import com.objectbox.runner.command.AbstractCommand;
import com.objectbox.runner.gui.JBManagerPanel;

public class CutCommand
        extends AbstractCommand {
    byte[] buffer = null;
    private JBManagerPanel jbmanager;

    public CutCommand(JBManagerPanel jBManagerPanel) {
        this.name = "cut";
        this.jbmanager = jBManagerPanel;
    }

    public boolean doIt() {
        try {
            this.jbmanager.cut();
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public boolean undoIt() {
        return true;
    }
}

