/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import com.objectbox.loader.JBURLClassloader;
import com.objectbox.loader.StateChangeEvent;
import com.objectbox.loader.StateChangeListener;
import com.objectbox.runner.gui.BeanRunner;
import com.objectbox.runner.gui.JBProcessButton;
import com.objectbox.runner.util.JBLogger;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Observable;

public class JBProcessModel
        extends Observable
        implements StateChangeListener,
        ActionListener {
    private Hashtable holderobjects = new Hashtable();
    private BeanRunner activeRunner = null;
    private BeanRunner lastRemovedRunner = null;
    private Component lastRemovedComponent = null;

    public void actionPerformed(ActionEvent actionEvent) {
    }

    public void addBeanRunner(BeanRunner beanRunner, Component component) {
        try {
            ((JBProcessButton) component).addActionListener(this);
            this.holderobjects.put(component, beanRunner);
            this.holderobjects.put(beanRunner, component);
            this.activeRunner = beanRunner;
            beanRunner.getLoader().addStateChangeListener(this);
            this.setChanged();
            this.notifyObservers("add");
        } catch (Exception exception) {
            JBLogger.log(exception.toString());
        }
    }

    public BeanRunner getActiveRunner() {
        return this.activeRunner;
    }

    public BeanRunner getBeanRunner(Component component) {
        return (BeanRunner) this.holderobjects.get(component);
    }

    public Component getComponent(BeanRunner beanRunner) {
        return (Component) this.holderobjects.get(beanRunner);
    }

    public Component getLastRemovedComponent() {
        return this.lastRemovedComponent;
    }

    public BeanRunner getLastRemovedRunner() {
        return this.lastRemovedRunner;
    }

    public void onChange(StateChangeEvent stateChangeEvent) {
        JBURLClassloader jBURLClassloader = (JBURLClassloader) stateChangeEvent.getSource();
        if (jBURLClassloader.getOwner() instanceof BeanRunner) {
            JBLogger.log("Loader event " + stateChangeEvent.thecurrentstate);
            JBProcessButton jBProcessButton = null;
            switch (stateChangeEvent.thecurrentstate) {
                case 6: {
                    jBProcessButton = (JBProcessButton) this.holderobjects.get(jBURLClassloader.getOwner());
                    jBProcessButton.setState(3);
                    this.removeBeanRunner((BeanRunner) jBURLClassloader.getOwner());
                    break;
                }
                case 3: {
                    jBProcessButton = (JBProcessButton) this.holderobjects.get(jBURLClassloader.getOwner());
                    jBProcessButton.setState(2);
                    break;
                }
                case 2: {
                    jBProcessButton = (JBProcessButton) this.holderobjects.get(jBURLClassloader.getOwner());
                    jBProcessButton.setState(1);
                    break;
                }
                case 5: {
                    jBProcessButton = (JBProcessButton) this.holderobjects.get(jBURLClassloader.getOwner());
                    jBProcessButton.setState(5);
                    break;
                }
                case 4: {
                    jBProcessButton = (JBProcessButton) this.holderobjects.get(jBURLClassloader.getOwner());
                    jBProcessButton.setState(4);
                    break;
                }
                default: {
                    jBProcessButton = (JBProcessButton) this.holderobjects.get(jBURLClassloader.getOwner());
                    jBProcessButton.setState(2);
                }
            }
        }
    }

    public void removeBeanRunner(BeanRunner beanRunner) {
        if (beanRunner != null) {
            Component component = (Component) this.holderobjects.get(beanRunner);
            this.holderobjects.remove(beanRunner);
            this.holderobjects.remove(component);
            this.lastRemovedRunner = beanRunner;
            this.lastRemovedComponent = component;
            this.setChanged();
            this.notifyObservers("remove");
        }
    }

    public void setActiveBeanRunner(BeanRunner beanRunner) {
        this.activeRunner = beanRunner;
        this.setChanged();
        this.notifyObservers("active");
    }
}

