/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.command;

import com.objectbox.runner.command.AbstractCommand;
import com.objectbox.runner.gui.JBManagerPanel;

import java.util.Vector;

public class DeleteCommand
        extends AbstractCommand {
    private JBManagerPanel jbmanager;
    private Vector backupVec = new Vector();

    public DeleteCommand(JBManagerPanel jBManagerPanel) {
        this.name = "delete";
        this.jbmanager = jBManagerPanel;
    }

    public boolean doIt() {
        try {
            this.jbmanager.delete(true);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public boolean undoIt() {
        return true;
    }
}

