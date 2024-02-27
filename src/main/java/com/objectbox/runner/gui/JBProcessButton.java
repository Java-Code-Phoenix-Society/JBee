/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.FlatButton;
import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.io.Serializable;
import java.util.Hashtable;

public class JBProcessButton
        extends FlatButton {
    public static final int REMOTE_LOADING = 1;
    public static final int LOCAL_LOADING = 2;
    public static final int CLOSING = 3;
    public static final int ERRORED = 4;
    public static final int RUNNING = 5;
    public static final int ZOOMBIE = 6;
    public static final int DONE = 5;
    protected int state = 2;
    protected Hashtable images = new Hashtable();
    protected Hashtable keywords = new Hashtable();
    protected boolean label_appended = false;
    protected String appendstring = "";
    protected String originallabel = "";
    private boolean visual = false;

    public JBProcessButton() {
        this.initialize();
        this.setLabel("Applet");
    }

    public JBProcessButton(String string) {
        super(string);
        this.originallabel = string;
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
            serializable = new JBProcessButton();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of com.objectbox.runner.gui.JBProcessButton");
        }
    }

    protected void checkstate() {
        if (this.visual) {
            Image image = (Image) this.images.get(new Integer(this.state));
            if (image != null) {
                this.setImage(image, 3);
            }
        } else {
            this.setImage(null);
            this.appendstring = "(" + this.keywords.get(new Integer(this.state)) + ")";
            this.label_appended = true;
            this.setLabel(String.valueOf(this.originallabel) + this.appendstring);
        }
        this.repaint();
        this.validate();
    }

    public int getState() {
        return this.state;
    }

    public void setState(int n) {
        this.state = n;
        this.checkstate();
    }

    public boolean getVisual() {
        return this.visual;
    }

    public void setVisual(boolean bl) {
        this.visual = bl;
        this.checkstate();
    }

    private void handleException(Throwable throwable) {
    }

    private void initialize() {
        Image image;
        this.setName("JBProcessButton");
        this.setSize(150, 150);
        this.keywords.put(new Integer(2), "Loading");
        this.keywords.put(new Integer(1), "Fetching");
        this.keywords.put(new Integer(5), "Running");
        this.keywords.put(new Integer(3), "Closing");
        this.keywords.put(new Integer(4), "Error");
        this.keywords.put(new Integer(6), "Not responding");
        try {
            image = JBee.resources.getImageResource("/images/remote_state.gif");
            this.images.put(new Integer(1), image);
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
        }
        try {
            image = JBee.resources.getImageResource("/images/local_state.gif");
            this.images.put(new Integer(2), image);
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
        }
        try {
            image = JBee.resources.getImageResource("/images/running_state.gif");
            this.images.put(new Integer(5), image);
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
        }
        try {
            image = JBee.resources.getImageResource("/images/closing_state.gif");
            this.images.put(new Integer(3), image);
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
        }
        try {
            image = JBee.resources.getImageResource("/images/errored_state.gif");
            this.images.put(new Integer(4), image);
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
        }
    }

    public void setStateImage(Image image, int n) {
        this.images.put(new Integer(n), image);
    }
}

