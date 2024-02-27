/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.command;

import com.objectbox.runner.command.AbstractCommand;
import com.objectbox.runner.gui.JBManagerPanel;
import com.objectbox.runner.gui.tree.Node;
import com.objectbox.runner.gui.tree.TreeBase;

public class RenameCommand
        extends AbstractCommand {
    int altered_node_index;
    String old_text;
    TreeBase treebase;
    JBManagerPanel jbman = null;

    public RenameCommand(TreeBase treeBase) {
        this.name = "rename";
        this.altered_node_index = treeBase.nodeIndex(treeBase.getSelectedNode());
        this.old_text = treeBase.getChangedItemName();
        this.treebase = treeBase;
    }

    public boolean doIt() {
        return true;
    }

    public boolean undoIt() {
        try {
            Node node = this.treebase.getNodeAt(this.altered_node_index);
            node.setText(this.old_text);
            this.treebase.validate();
        } catch (Exception exception) {
            return false;
        }
        return true;
    }
}

