/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.command;

import com.objectbox.runner.command.CommandManager;

public abstract class AbstractCommand {
    public static final CommandManager manager = new CommandManager();
    protected String name = "";

    public abstract boolean doIt();

    public String getName() {
        return this.name;
    }

    public abstract boolean undoIt();
}

