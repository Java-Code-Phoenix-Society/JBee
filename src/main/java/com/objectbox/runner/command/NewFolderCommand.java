/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.command;

import com.objectbox.runner.command.AbstractCommand;
import com.objectbox.runner.gui.JBManagerPanel;
import com.objectbox.runner.model.JBObjectWithProperties;

public class NewFolderCommand
        extends AbstractCommand {
    byte[] buffer = null;
    String foldername = "";
    JBObjectWithProperties properties = null;
    private JBManagerPanel jbmanager;

    public NewFolderCommand(JBManagerPanel jBManagerPanel, String string, JBObjectWithProperties jBObjectWithProperties) {
        this.name = "newfolder";
        this.jbmanager = jBManagerPanel;
        this.properties = jBObjectWithProperties;
        this.foldername = string;
    }

    public boolean doIt() {
        try {
            this.jbmanager.newFolder(this.foldername, this.properties);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public boolean undoIt() {
        return true;
    }
}

