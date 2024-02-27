/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

public class DelayedInvocation
        implements Runnable {
    private static DelayedInvocation instance = null;
    private long delay = 1000L;
    private DelayedInvocationCallBack callback = null;
    private Thread t = null;

    private DelayedInvocation() {
    }

    public static DelayedInvocation getInstance() {
        try {
            if (instance == null) {
                instance = new DelayedInvocation();
                DelayedInvocation.instance.t = new Thread(instance);
                DelayedInvocation.instance.t.start();
            }
            return instance;
        } catch (Exception exception) {
            System.err.println(exception);
            return null;
        }
    }

    public DelayedInvocationCallBack getCallback() {
        return this.callback;
    }

    public long getDelay() {
        return this.delay;
    }

    public void setDelay(long l) {
        this.delay = l;
    }

    public void run() {
        boolean stat = true;
        while (stat) {
            try {
                Thread.sleep(this.delay);
                if (this.callback != null) {
                    this.callback.delayedInvoke();
                    this.callback = null;
                }
            } catch (InterruptedException e) {
                // Restore the interrupted status
                Thread.currentThread().interrupt();
                break; // Exit the loop if interrupted
            } catch (Exception e) {
                e.printStackTrace(); // Handle other exceptions
            }
            stat = false; // Moved outside of the try-catch block to ensure loop termination
        }
    }

    public void setCallback(DelayedInvocationCallBack delayedInvocationCallBack, long l) {
        try {
            this.callback = null;
            this.delay = l;
            this.t.interrupt();
            this.callback = delayedInvocationCallBack;
        } catch (Exception exception) {
            System.err.println(exception);
        }
    }
}

