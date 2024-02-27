/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import com.objectbox.runner.util.JBLogger;

public class MenuCloser
        extends Thread {
    protected boolean isRunning = true;
    protected boolean isFresh = true;
    private JBPopupMenu pm = null;

    public MenuCloser(JBPopupMenu jBPopupMenu) {
        this.pm = jBPopupMenu;
    }

    public MenuCloser(Runnable runnable) {
        super(runnable);
    }

    public MenuCloser(Runnable runnable, String string) {
        super(runnable, string);
    }

    public MenuCloser(String string) {
        super(string);
    }

    public MenuCloser(ThreadGroup threadGroup, Runnable runnable) {
        super(threadGroup, runnable);
    }

    public MenuCloser(ThreadGroup threadGroup, Runnable runnable, String string) {
        super(threadGroup, runnable, string);
    }

    public MenuCloser(ThreadGroup threadGroup, String string) {
        super(threadGroup, string);
    }

    public void run() {
        while (this.isRunning) {
            try {
                Thread.sleep(50L);
            } catch (Exception exception) {
                JBLogger.log("Ex" + exception);
            }
            boolean bl = true;
            while (this.pm.parent != null) {
                this.pm = this.pm.parent;
            }
            this.isFresh = true;
            if (this.pm.focus) {
                bl = false;
            }
            JBPopupMenu jBPopupMenu = this.pm;
            while (jBPopupMenu != null) {
                if (jBPopupMenu.focus) {
                    bl = false;
                    break;
                }
                jBPopupMenu = jBPopupMenu.opensubmenu;
            }
            try {
                Thread.sleep(50L);
            } catch (Exception exception) {
                JBLogger.log("Ex in MenuCloser" + exception);
            }
            if (!bl || !this.isFresh) continue;
            this.pm.setVisible(false);
            this.isRunning = false;
        }
    }
}

