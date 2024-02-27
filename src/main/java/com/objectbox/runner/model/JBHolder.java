/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import com.objectbox.runner.gui.BeanRunner;
import com.objectbox.runner.model.JBObjectWithProperties;
import com.objectbox.runner.model.JBProperties;

import java.io.Serializable;
import java.util.Hashtable;

public abstract class JBHolder
        implements JBObjectWithProperties,
        Serializable {
    static final long serialVersionUID = -123456789L;
    protected transient BeanRunner runner = null;
    protected JBProperties props = null;

    public void cleanup() {
        this.runner = null;
        this.props.cleanup();
        this.props = null;
    }

    public BeanRunner getBeanRunner() {
        return this.runner == null ? new BeanRunner() : this.runner;
    }

    public void setBeanRunner(BeanRunner beanRunner) {
        this.runner = beanRunner;
    }

    public String getName() {
        String string = (String) ((Hashtable) this.props.getProps()).get("code");
        return string == null ? "Unknown" : string;
    }

    public JBProperties getProperties() {
        return this.props;
    }

    public void setProperties(JBProperties jBProperties) {
        this.props = jBProperties;
    }

    public abstract BeanRunner run(ThreadGroup var1);
}

