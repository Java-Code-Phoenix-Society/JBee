/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.tree;

public class TreeNodeX
        extends TreeNode {
    int m_root_nFilterLevel = 0;

    public int getFilterLevel() {
        return ((TreeNodeX) this.getRoot()).m_root_nFilterLevel;
    }

    public TreeNode getFirstChild() {
        if (this.m_pFirstChild != null && this.getFilterLevel() > 0 && this.getUnfilteredDistanceFromRoot() == this.getFilterLevel() - 1) {
            return this.m_pFirstChild.m_pFirstChild;
        }
        return this.m_pFirstChild;
    }

    public TreeNode getNextSibling() {
        if (this.getFilterLevel() > 0 && this.getUnfilteredDistanceFromRoot() == this.getFilterLevel() + 1 && this.m_pNextSibling == null) {
            TreeNode treeNode = this.m_pParent;
            if (treeNode == null) {
                return null;
            }
            treeNode = treeNode.m_pNextSibling;
            if (treeNode == null) {
                return null;
            }
            treeNode = treeNode.m_pFirstChild;
            return treeNode;
        }
        return this.m_pNextSibling;
    }

    public TreeNode getParent() {
        if (this.getFilterLevel() > 0 && this.m_pParent != null && this.getUnfilteredDistanceFromRoot() == this.getFilterLevel() + 1) {
            return this.m_pParent.m_pParent;
        }
        return this.m_pParent;
    }

    public TreeNode getPrevSibling() {
        if (this.getFilterLevel() > 0 && this.getUnfilteredDistanceFromRoot() == this.getFilterLevel() + 1 &&
                super.m_pPrevSibling == null) {
            TreeNode mPParent = super.m_pParent;
            if (mPParent == null) {
                return null;
            } else {
                mPParent = mPParent.m_pPrevSibling;
                if (mPParent == null) {
                    return null;
                } else {
                    mPParent = mPParent.m_pFirstChild;
                    if (mPParent == null) {
                        return null;
                    } else {
                        while (mPParent.m_pNextSibling != null) {
                            mPParent = mPParent.m_pNextSibling;
                        }

                        return mPParent;
                    }
                }
            }
        } else {
            return super.m_pPrevSibling;
        }
    }

    public int getUnfilteredDistanceFromRoot() {
        TreeNode treeNode = this.m_pParent;
        int n = 0;
        while (treeNode != null) {
            treeNode = treeNode.m_pParent;
            ++n;
        }
        return n;
    }

    public boolean setFilterLevel(int n) {
        ((TreeNodeX) this.getRoot()).m_root_nFilterLevel = n;
        return true;
    }
}

