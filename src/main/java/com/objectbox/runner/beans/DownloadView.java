/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.beans;

import java.awt.*;
import java.io.Serializable;
import java.text.DecimalFormat;

public class DownloadView
        extends Component {
    private static long currload;
    public boolean testmode = false;
    private boolean running = true;
    private Thread repaintthread = null;
    private Font font = new Font("SansSerif", 1, 10);
    private long bytes_pr_time = 0L;
    private long average_load = 0L;
    private int timespansec = 1;
    private double maxload = 0.0;
    private double mark = 0.0;
    private int mark_delay = 5;
    private long totalload = 0L;
    private long totaltime = 0L;
    private DecimalFormat format = new DecimalFormat();

    public DownloadView() {
        this.initialize();
    }

    public static final void addBytes(long l) {
        currload += l;
    }

    public static void main(String[] stringArray) {
        try {
            Frame frame;
            Serializable serializable;
            try {
                serializable = Class.forName("com.ibm.uvm.abt.edit.TestFrame");
                frame = (Frame) ((Class) serializable).newInstance();
            } catch (Throwable throwable) {
                frame = new Frame();
            }
            serializable = new DownloadView();
            ((DownloadView) serializable).testmode = true;
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            System.err.println("Exception occurred in main() of com.objectbox.runner.beans.ResourceView");
            throwable.printStackTrace(System.out);
        }
    }

    static int access$timespansec(DownloadView downloadView) {
        return downloadView.timespansec;
    }

    public void finalize() {
        if (this.repaintthread != null) {
            this.repaintthread.stop();
        }
    }

    public Dimension getMinimumSize() {
        return new Dimension(100, 20);
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, 20);
    }

    public boolean getRunning() {
        return this.running;
    }

    public void setRunning(boolean bl) {
        this.running = bl;
        if (this.running && this.repaintthread != null) {
            this.go();
        } else if (this.repaintthread != null) {
            this.repaintthread.stop();
        }
    }

    private void go() {
        class Repainter
                extends Thread {
            public boolean running;
            /* synthetic */ DownloadView this$0;

            Repainter(DownloadView downloadView) {
                this.this$0 = downloadView;
                this.running = true;
            }

            public void run() {
                while (this.running) {
                    this.this$0.repaint();
                    try {
                        Thread.sleep(DownloadView.access$timespansec(this.this$0) * 1000);
                        if (!this.this$0.testmode) continue;
                        DownloadView.addBytes((long) (Math.random() * 1024.0 * 1024.0));
                    } catch (InterruptedException interruptedException) {
                    }
                }
            }
        }
        this.repaintthread = new Repainter(this);
        this.repaintthread.start();
    }

    private void handleException(Throwable throwable) {
        System.out.println("--------- UNCAUGHT EXCEPTION ---------");
        throwable.printStackTrace(System.out);
    }

    private void initialize() {
        this.format.applyPattern("####.## 'kbs'");
        this.setName("DownloadView");
        this.setSize(188, 36);
        if (this.running) {
            this.go();
        }
    }

    public void paint(Graphics graphics) {
        graphics.setFont(this.font);
        double d = (double) currload / (double) this.timespansec / 1024.0;
        this.totalload += currload;
        this.totaltime += (long) this.timespansec;
        double d2 = d / this.maxload;
        int n = this.getSize().width;
        int n2 = this.getSize().height;
        double d3 = d2 * (double) n;
        String string = "Max." + this.format.format(this.maxload);
        graphics.clearRect(0, 0, n, n2);
        graphics.setColor(Color.orange);
        graphics.fill3DRect(0, 0, (int) d3, n2, true);
        graphics.setColor(Color.darkGray);
        graphics.drawString("" + this.format.format(d), 3, 10);
        graphics.drawString(string, n - graphics.getFontMetrics(this.font).stringWidth(string) - 2, n2 - 2);
        graphics.setColor(this.getBackground());
        graphics.draw3DRect(0, 0, n - 1, n2 - 1, true);
        if (this.totaltime > 60L) {
            this.totaltime = 0L;
            this.totalload = 0L;
            this.maxload = 0.0;
        }
        if (d > this.maxload) {
            this.maxload = d;
        }
        currload = 0L;
    }
}

