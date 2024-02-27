/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui.tree;

import com.objectbox.runner.util.JBLogger;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

public class TreeNode
        implements Serializable {
    public static final int TREENODE_FIRST = 0;
    public static final int TREENODE_LAST = 1;
    public static final int TREENODE_SORT = 2;
    public static final Enumeration EMPTY_ENUMERATION = new Enumeration() {

        public boolean hasMoreElements() {
            return false;
        }

        public Object nextElement() {
            throw new NoSuchElementException("No more elements");
        }
    };
    protected TreeNode m_pParent = null;
    protected TreeNode m_pNextSibling = null;
    protected TreeNode m_pPrevSibling = null;
    protected TreeNode m_pFirstChild = null;

    public boolean addChild(TreeNode treeNode) {
        return this.addChild(treeNode, null);
    }

    public boolean addChild(TreeNode treeNode, int n) {
        if (n == 2) {
            JBLogger.log("WARNING:  haven't implemented TREENODE_SORT");
            n = 1;
        }
        treeNode.m_pParent = this;
        if (this.m_pFirstChild == null) {
            this.m_pFirstChild = treeNode;
            treeNode.m_pNextSibling = null;
            treeNode.m_pPrevSibling = null;
        } else if (n == 1) {
            TreeNodeX treeNodeX = (TreeNodeX) this.m_pFirstChild;
            while (treeNodeX.m_pNextSibling != null) {
                treeNodeX = (TreeNodeX) treeNodeX.m_pNextSibling;
            }
            treeNodeX.m_pNextSibling = treeNode;
            treeNode.m_pPrevSibling = treeNodeX;
            treeNode.m_pNextSibling = null;
        } else if (n == 0) {
            treeNode.m_pPrevSibling = null;
            treeNode.m_pNextSibling = this.m_pFirstChild;
            this.m_pFirstChild.m_pPrevSibling = treeNode;
            this.m_pFirstChild = treeNode;
        }
        return true;
    }

    public boolean addChild(TreeNode treeNode, TreeNode treeNode2) {
        if (treeNode2 == null) {
            return this.addChild(treeNode, 1);
        }
        treeNode.m_pParent = this;
        if (this.m_pFirstChild == null) {
            this.m_pFirstChild = treeNode;
            treeNode.m_pNextSibling = null;
            treeNode.m_pPrevSibling = null;
        } else {
            boolean bl = false;
            TreeNodeX treeNodeX = (TreeNodeX) this.m_pFirstChild;
            while (treeNodeX != null) {
                if (treeNodeX == treeNode2) {
                    bl = true;
                    break;
                }
                treeNodeX = (TreeNodeX) treeNodeX.m_pNextSibling;
            }
            if (!bl) {
                return false;
            }
            treeNode.m_pNextSibling = treeNode2.m_pNextSibling;
            treeNode.m_pPrevSibling = treeNode2;
            if (treeNode2.m_pNextSibling != null) {
                treeNode2.m_pNextSibling.m_pPrevSibling = treeNode;
            }
            treeNode2.m_pNextSibling = treeNode;
        }
        return true;
    }

    public Enumeration breadthFirstEnumeration() {
        return new BreadthFirstEnumeration(this, this);
    }

    public Enumeration children() {
        if (this.m_pFirstChild == null) {
            return EMPTY_ENUMERATION;
        }
        Vector<TreeNode> vector = new Vector<TreeNode>();
        TreeNode treeNode = this.m_pFirstChild;
        while (treeNode != null) {
            vector.addElement(treeNode);
            treeNode = treeNode.getNextSibling();
        }
        return vector.elements();
    }

    public void collapse() {
        this.expand(false);
    }

    public void deleteAllChildren() {
        TreeNode treeNode;
        while ((treeNode = this.getFirstChild()) != null) {
            treeNode.detachFromTree();
        }
    }

    public void deleteChildren() {
    }

    public void detachFromTree() {
        if (this.m_pNextSibling != null) {
            this.m_pNextSibling.m_pPrevSibling = this.m_pPrevSibling;
        }
        if (this.m_pPrevSibling != null) {
            this.m_pPrevSibling.m_pNextSibling = this.m_pNextSibling;
        }
        if (this.m_pParent != null && this.m_pParent.m_pFirstChild == this) {
            this.m_pParent.m_pFirstChild = this.m_pNextSibling;
        }
        this.m_pParent = null;
    }

    public void expand() {
        this.expand(true);
    }

    public void expand(boolean bl) {
    }

    public TreeNode getBottomLeftChild() {
        TreeNode treeNode = this.m_pFirstChild;
        if (treeNode != null) {
            while (treeNode.getFirstChild() != null) {
                treeNode = treeNode.getFirstChild();
            }
        }
        return treeNode;
    }

    public TreeNode getBottomRightChild() {
        TreeNode treeNode = this.getLastChild();
        if (treeNode != null) {
            return treeNode.getBottomRightChild();
        }
        return this;
    }

    public int getDistanceFromRoot() {
        TreeNode treeNode = this.getParent();
        int n = 0;
        while (treeNode != null) {
            treeNode = treeNode.getParent();
            ++n;
        }
        return n;
    }

    public TreeNode getFirstChild() {
        return this.m_pFirstChild;
    }

    public void setFirstChild(TreeNode treeNode) {
        this.m_pFirstChild = treeNode;
    }

    public TreeNode getFirstSibling() {
        TreeNode treeNode = this;
        while (treeNode.getPrevSibling() != null) {
            treeNode = treeNode.getPrevSibling();
        }
        return treeNode;
    }

    public TreeNode getLastChild() {
        TreeNode treeNode = this.getFirstChild();
        if (treeNode == null) {
            return null;
        }
        return treeNode.getLastSibling();
    }

    public TreeNode getLastSibling() {
        TreeNode treeNode = this;
        while (treeNode.getNextSibling() != null) {
            treeNode = treeNode.getNextSibling();
        }
        return treeNode;
    }

    public TreeNode getNextInDisplayOrder() {
        if (this.getFirstChild() != null) {
            return this.getFirstChild();
        }
        if (this.getNextSibling() != null) {
            return this.getNextSibling();
        }
        TreeNode treeNode = this;
        while (treeNode.getParent() != null) {
            if ((treeNode = treeNode.getParent()).getNextSibling() == null) continue;
            return treeNode.getNextSibling();
        }
        return null;
    }

    public TreeNode getNextSibling() {
        return this.m_pNextSibling;
    }

    public void setNextSibling(TreeNode treeNode) {
        this.m_pNextSibling = treeNode;
    }

    public int getNumDescendents() {
        int n = 0;
        TreeNode treeNode = this.getFirstChild();
        while (treeNode != null) {
            ++n;
            treeNode = treeNode.getNextInDisplayOrder();
        }
        return n;
    }

    public TreeNode getParent() {
        return this.m_pParent;
    }

    public void setParent(TreeNode treeNode) {
        this.m_pParent = treeNode;
    }

    public TreeNode getPrevInDisplayOrder() {
        if (this.getPrevSibling() != null) {
            TreeNode treeNode = this.getPrevSibling().getBottomRightChild();
            if (treeNode != null) {
                return treeNode;
            }
            return this.getPrevSibling();
        }
        return this.getParent();
    }

    public TreeNode getPrevSibling() {
        return this.m_pPrevSibling;
    }

    public void setPrevSibling(TreeNode treeNode) {
        this.m_pPrevSibling = treeNode;
    }

    public TreeNode getRoot() {
        TreeNode treeNode = this;
        while (treeNode.m_pParent != null) {
            treeNode = treeNode.m_pParent;
        }
        return treeNode;
    }

    public boolean hasChildren() {
        return this.m_pFirstChild != null;
    }

    public boolean isAncestor(TreeNode treeNode) {
        TreeNode treeNode2 = this;
        treeNode2 = this.getParent();
        while (treeNode2 != null) {
            if (treeNode2 == treeNode) {
                return true;
            }
            treeNode2 = treeNode2.getParent();
        }
        return false;
    }

    public boolean isDescendant(TreeNode treeNode) {
        if (treeNode == null) {
            return false;
        } else {
            while (treeNode.getParent() != null) {
                treeNode = treeNode.getParent();
                if (treeNode == this) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean isExpanded() {
        return false;
    }

    public boolean isParent(TreeNode treeNode) {
        return this.m_pParent == treeNode;
    }

    public boolean isSibling(TreeNode treeNode) {
        TreeNode treeNode2 = this.getFirstSibling();
        while (treeNode2 != null) {
            if (treeNode == treeNode2) {
                return true;
            }
            treeNode2 = treeNode2.getNextSibling();
        }
        return false;
    }

    public boolean onNextSearchNode(int n, TreeNode treeNode) {
        return false;
    }

    public TreeNode search(int n) {
        TreeNode treeNode = this.getBottomLeftChild();
        while (treeNode != null) {
            boolean bl = this.onNextSearchNode(n, treeNode);
            if (bl) {
                return treeNode;
            }
            if (treeNode.getNextSibling() != null) {
                treeNode = treeNode.getNextSibling();
                continue;
            }
            if (treeNode.getParent() == null || treeNode.getParent() == this) break;
            treeNode = treeNode.getParent();
        }
        return null;
    }

    final class BreadthFirstEnumeration
            implements Enumeration {
        protected Queue queue;
        /* synthetic */ TreeNode this$0;

        public BreadthFirstEnumeration(TreeNode treeNode, TreeNode treeNode2) {
            this.this$0 = treeNode;
            Vector<TreeNode> vector = new Vector<TreeNode>(1);
            vector.addElement(treeNode2);
            this.queue = new Queue(this);
            this.queue.enqueue(vector.elements());
        }

        public boolean hasMoreElements() {
            return !this.queue.isEmpty() && ((Enumeration) this.queue.firstObject()).hasMoreElements();
        }

        public Object nextElement() {
            Enumeration enumeration = (Enumeration) this.queue.firstObject();
            TreeNode treeNode = (TreeNode) enumeration.nextElement();
            Enumeration enumeration2 = treeNode.children();
            if (!enumeration.hasMoreElements()) {
                this.queue.dequeue();
            }
            if (enumeration2.hasMoreElements()) {
                this.queue.enqueue(enumeration2);
            }
            return treeNode;
        }

        final class Queue {
            /* synthetic */ BreadthFirstEnumeration this$1;
            QNode head;
            QNode tail;

            Queue(BreadthFirstEnumeration breadthFirstEnumeration) {
                this.this$1 = breadthFirstEnumeration;
            }

            public void enqueue(Object object) {
                if (this.head == null) {
                    this.head = this.tail = new QNode(this, object, null);
                } else {
                    this.tail = this.tail.next = new QNode(this, object, null);
                }
            }

            public Object dequeue() {
                if (this.head == null) {
                    throw new NoSuchElementException("No more elements");
                }
                Object object = this.head.object;
                QNode qNode = this.head;
                this.head = this.head.next;
                if (this.head == null) {
                    this.tail = null;
                } else {
                    qNode.next = null;
                }
                return object;
            }

            public Object firstObject() {
                if (this.head == null) {
                    throw new NoSuchElementException("No more elements");
                }
                return this.head.object;
            }

            public boolean isEmpty() {
                return this.head == null;
            }

            final class QNode {
                public Object object;
                public QNode next;
                /* synthetic */ Queue this$2;

                public QNode(Queue queue, Object object, QNode qNode) {
                    this.this$2 = queue;
                    this.object = object;
                    this.next = qNode;
                }
            }
        }
    }
}

