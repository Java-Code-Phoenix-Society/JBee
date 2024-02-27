/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.command;

import com.objectbox.runner.command.AbstractCommand;
import com.objectbox.runner.command.Redo;
import com.objectbox.runner.command.Undo;

import java.util.Vector;

public class CommandManager {
    private int maxHistoryLength = 5;
    private Vector history = new Vector();
    private Vector redoList = new Vector();

    private void addToHistory(AbstractCommand abstractCommand) {
        this.history.insertElementAt(abstractCommand, 0);
        if (this.history.size() > this.maxHistoryLength) {
            this.history.removeElement(this.history.lastElement());
        }
    }

    public int getHistoryLength() {
        return this.history.size();
    }

    public String getLastCommandName() {
        if (this.history.size() > 0) {
            Object e = this.history.firstElement();
            return ((AbstractCommand) e).getName();
        }
        return "";
    }

    public void invokeCommand(AbstractCommand abstractCommand) {
        if (abstractCommand instanceof Undo) {
            this.undo();
            return;
        }
        if (abstractCommand instanceof Redo) {
            this.redo();
            return;
        }
        if (abstractCommand.doIt()) {
            this.addToHistory(abstractCommand);
        } else {
            this.history.removeAllElements();
        }
        if (this.redoList.size() > 0) {
            this.redoList.removeAllElements();
        }
    }

    private void redo() {
        if (this.redoList.size() > 0) {
            AbstractCommand abstractCommand = (AbstractCommand) this.redoList.firstElement();
            this.redoList.removeElement(abstractCommand);
            abstractCommand.doIt();
            this.history.insertElementAt(abstractCommand, 0);
        }
    }

    private void undo() {
        if (this.history.size() > 0) {
            AbstractCommand abstractCommand = (AbstractCommand) this.history.firstElement();
            this.history.removeElement(abstractCommand);
            abstractCommand.undoIt();
            this.redoList.insertElementAt(abstractCommand, 0);
        }
    }
}

