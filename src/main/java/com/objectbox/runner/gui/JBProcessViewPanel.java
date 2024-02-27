/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.gui.lwcomp.DoubleBufferPanel;
import com.objectbox.gui.lwcomp.FlatButton;
import com.objectbox.runner.model.JBProcessModel;
import com.objectbox.runner.util.JBLogger;
import com.sun.java.swing.BoxLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public class JBProcessViewPanel
        extends Panel
        implements ActionListener,
        Observer {
    public final int MIN_PROCESS_LIST_SIZE = 6;
    protected transient PropertyChangeSupport propertyChange;
    protected transient ProcessSelectedListener aProcessSelectedListener = null;
    protected boolean rightclick = false;
    private DoubleBufferPanel ivjPanelButtonHolder = null;
    private ScrollPane ivjScrollPaneProcessView = null;
    private JBProcessModel fieldModel = new JBProcessModel();
    private BoxLayout ivjPanelButtonHolderBoxLayout = null;

    public JBProcessViewPanel() {
        this.initialize();
    }

    public JBProcessViewPanel(LayoutManager layoutManager) {
        super(layoutManager);
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
            serializable = new JBProcessViewPanel();
            frame.add("Center", (Component) serializable);
            ((Component) frame).setSize(((Component) serializable).getSize());
            ((Component) frame).setVisible(true);
        } catch (Throwable throwable) {
            JBLogger.log("Exception occurred in main() of java.awt.Panel");
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        BeanRunner beanRunner = this.getModel().getBeanRunner((Component) actionEvent.getSource());
        this.rightclick = ((FlatButton) actionEvent.getSource()).rightButtonPush;
        this.getModel().setActiveBeanRunner(beanRunner);
        this.fireOnProcessSelected(new ProcessSelectedEvent(this));
    }

    public void addProcessSelectedListener(ProcessSelectedListener processSelectedListener) {
        this.aProcessSelectedListener = ProcessSelectedEventMulticaster.add(this.aProcessSelectedListener, processSelectedListener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().addPropertyChangeListener(propertyChangeListener);
    }

    protected void fireOnProcessSelected(ProcessSelectedEvent processSelectedEvent) {
        if (this.aProcessSelectedListener == null) {
            return;
        }
        this.aProcessSelectedListener.onProcessSelected(processSelectedEvent);
    }

    public void firePropertyChange(String string, Object object, Object object2) {
        this.getPropertyChange().firePropertyChange(string, object, object2);
    }

    public Dimension getMaximumSize() {
        return new Dimension(100, 100);
    }

    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }

    public JBProcessModel getModel() {
        return this.fieldModel;
    }

    private DoubleBufferPanel getPanelButtonHolder() {
        if (this.ivjPanelButtonHolder == null) {
            try {
                this.ivjPanelButtonHolder = new DoubleBufferPanel();
                this.ivjPanelButtonHolder.setName("PanelButtonHolder");
                this.ivjPanelButtonHolder.setLayout(this.getPanelButtonHolderBoxLayout());
                this.ivjPanelButtonHolder.setBackground(SystemColor.control);
                this.ivjPanelButtonHolder.setBounds(0, 0, 71, 115);
                this.ivjPanelButtonHolder.setHasframe(false);
                this.ivjPanelButtonHolder.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjPanelButtonHolder;
    }

    private BoxLayout getPanelButtonHolderBoxLayout() {
        BoxLayout boxLayout = null;
        try {
            boxLayout = new BoxLayout(this.getPanelButtonHolder(), 1);
        } catch (Throwable throwable) {
            this.handleException(throwable);
        }
        return boxLayout;
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }

    protected PropertyChangeSupport getPropertyChange() {
        if (this.propertyChange == null) {
            this.propertyChange = new PropertyChangeSupport(this);
        }
        return this.propertyChange;
    }

    private ScrollPane getScrollPaneProcessView() {
        if (this.ivjScrollPaneProcessView == null) {
            try {
                this.ivjScrollPaneProcessView = new ScrollPane();
                this.ivjScrollPaneProcessView.setName("ScrollPaneProcessView");
                this.getScrollPaneProcessView().add((Component) this.getPanelButtonHolder(), this.getPanelButtonHolder().getName());
                this.ivjScrollPaneProcessView.setBackground(JBee.appcolor);
            } catch (Throwable throwable) {
                this.handleException(throwable);
            }
        }
        return this.ivjScrollPaneProcessView;
    }

    public void handleAddProcess(BeanRunner beanRunner) {
        if (beanRunner == null) {
            return;
        }
        FlatButton flatButton = (FlatButton) this.getModel().getComponent(beanRunner);
        flatButton.setFixedsize(new Dimension(this.getScrollPaneProcessView().getSize().width - 5, 20));
        flatButton.addActionListener(this);
        this.getPanelButtonHolder().add(flatButton);
        this.getParent().doLayout();
        this.getPanelButtonHolder().repaint();
        this.validate();
    }

    private void handleException(Throwable throwable) {
    }

    public void handleRemoveProcess(BeanRunner beanRunner) {
        if (beanRunner == null) {
            return;
        }
        FlatButton flatButton = (FlatButton) this.getModel().getLastRemovedComponent();
        this.getPanelButtonHolder().remove(flatButton);
        this.getPanelButtonHolder().repaint();
        this.validate();
    }

    private void initialize() {
        this.setName("ProcessViewPanel");
        this.setLayout(new BorderLayout());
        this.setSize(143, 125);
        this.add((Component) this.getScrollPaneProcessView(), "Center");
        this.getModel().addObserver(this);
        this.setBackground(JBee.appcolor);
    }

    public void removeProcessSelectedListener(ProcessSelectedListener processSelectedListener) {
        this.aProcessSelectedListener = ProcessSelectedEventMulticaster.remove(this.aProcessSelectedListener, processSelectedListener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.getPropertyChange().removePropertyChangeListener(propertyChangeListener);
    }

    public void update(Observable observable, Object object) {
        try {
            if (observable instanceof JBProcessModel) {
                JBProcessModel jBProcessModel = (JBProcessModel) observable;
                if (object instanceof String) {
                    Container container;
                    String string = (String) object;
                    if (string.compareTo("add") == 0) {
                        this.handleAddProcess(jBProcessModel.getActiveRunner());
                    }
                    if (string.compareTo("remove") == 0) {
                        this.handleRemoveProcess(jBProcessModel.getLastRemovedRunner());
                    }
                    if (string.compareTo("active") == 0 && (container = jBProcessModel.getActiveRunner().getFrame()) != null && container.isVisible() && container instanceof Frame) {
                        ((Frame) container).toFront();
                    }
                }
            }
        } catch (Exception exception) {
            JBLogger.log("JBProcessViewPanel.update " + exception.toString());
        }
    }
}

