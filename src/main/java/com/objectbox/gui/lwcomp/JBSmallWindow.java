/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.gui.lwcomp;

import com.objectbox.runner.gui.CloseableFrame;
import com.objectbox.runner.gui.IAppletWindow;
import com.objectbox.runner.util.JBLogger;

import java.applet.Applet;
import java.awt.*;

public class JBSmallWindow
        extends Window
        implements IAppletWindow {
    public static int header_height = 8;
    BorderPanel centerpanel = new BorderPanel(new BorderLayout());
    LWWindowMover mover = null;
    private boolean done = false;
    private Applet applettorun = null;

    public JBSmallWindow(Frame frame) {
        super(frame);
        this.setLayout(null);
        this.mover = new LWWindowMover(this, "");
        this.mover.setHeaderheight(header_height);
        this.add(this.mover, "North");
        this.mover.setLocation(0, 0);
        this.mover.setSize(this.getSize().width, header_height);
        this.add(this.centerpanel, "Center");
        this.centerpanel.setLocation(1, header_height);
        this.centerpanel.setSize(this.getSize().width - 2, this.getSize().height - header_height - 1);
    }

    public static void main(String[] args) {
        CloseableFrame closeableFrame = new CloseableFrame("Mother");
        ((Component) closeableFrame).setSize(100, 100);
        Button button = new Button("Run");
        JBSmallWindow jBSmallWindow = new JBSmallWindow(closeableFrame);
        ((Component) jBSmallWindow).setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - 200, 0);
        jBSmallWindow.setSize(200, 200);
        Panel panel = new Panel(new BorderLayout());
        panel.add(new Button("North"), "North");
        panel.add(new Button("Hei"), "Center");
        panel.add(new Button("Syden"), "South");
        panel.add(new Button("Vest"), "West");
        jBSmallWindow.getContentPanel().add(panel, "Center");
        ((Component) jBSmallWindow).setVisible(true);
        jBSmallWindow.validate();
        closeableFrame.add(button);
        ((Component) closeableFrame).setVisible(true);
    }

    public void addApplet(Applet applet) {
        this.applettorun = applet;
        this.setSize(this.applettorun.getSize().width + 2, this.applettorun.getSize().height + header_height + 1);
        this.getContentPanel().add(applet, "Center");
    }

    protected void finalize() throws Throwable {
        super.finalize();
        JBLogger.log("finalize JBSmallWindow");
    }

    public Container getContentPanel() {
        return this.centerpanel;
    }

    public boolean isDone() {
        return this.done;
    }

    public void kill() {
        try {
            try {
                if (this.applettorun != null) {
                    this.applettorun.stop();
                    this.applettorun.destroy();
                    this.remove(this.applettorun);
                }
                System.gc();
                this.done = true;
                JBLogger.log("JBSmallWindow Stopping applet");
            } catch (Exception exception) {
                JBLogger.log("JBSMallWindow.kill " + exception.toString());
            }
        } catch (Throwable throwable) {
            Object var1_3 = null;
            this.done = true;
            throw throwable;
        }
        Object var1_4 = null;
        this.done = true;
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(Color.black);
        graphics.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
    }

    public void setSize(int n, int n2) {
        super.setSize(n, n2);
        this.mover.setLocation(0, 0);
        this.mover.setSize(this.getSize().width, header_height);
        this.centerpanel.setLocation(1, header_height);
        this.centerpanel.setSize(this.getSize().width - 2, this.getSize().height - header_height - 1);
        JBLogger.log("SmallWindow.setSize(w,h) " + n + "," + n2);
    }

    public void setSize(Dimension dimension) {
        this.setSize(dimension.width, dimension.height);
        JBLogger.log("SmallWindow.setSize " + dimension);
    }

    public String toString() {
        return super.toString();
    }
}

