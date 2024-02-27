/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.command;

import com.objectbox.runner.command.AbstractCommand;
import com.objectbox.runner.gui.JBManagerPanel;

public class PasteCommand
        extends AbstractCommand {
    byte[] buffer = null;
    private JBManagerPanel jbmanager;

    public PasteCommand(JBManagerPanel jBManagerPanel) {
        this.name = "paste";
        this.jbmanager = jBManagerPanel;
    }

    public boolean doIt() {
        try {
            this.jbmanager.paste();
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public boolean undoIt() {
        return true;
    }
}

