/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import com.objectbox.runner.model.JBPropertyEditor;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public abstract class JBProperties
        implements Serializable {
    static final long serialVersionUID = -123456789L;
    protected Properties parameters = new Properties();
    protected Properties jbee_props = new Properties();
    protected Properties properties = new Properties();
    protected Properties hidden_props = new Properties();
    boolean hasMergedParamProps = false;

    public JBProperties() {
        this.init();
    }

    public JBProperties(JBProperties jBProperties) {
        this.parameters = this.copy(jBProperties.parameters);
        this.jbee_props = this.copy(jBProperties.jbee_props);
    }

    public void cleanup() {
        Object k;
        Enumeration enumeration;
        if (this.parameters != null) {
            enumeration = ((Hashtable) this.parameters).keys();
            while (enumeration.hasMoreElements()) {
                k = enumeration.nextElement();
                ((Hashtable) this.parameters).remove(k);
                k = null;
            }
            ((Hashtable) this.parameters).clear();
        }
        if (this.properties != null) {
            enumeration = ((Hashtable) this.properties).keys();
            while (enumeration.hasMoreElements()) {
                k = enumeration.nextElement();
                ((Hashtable) this.properties).remove(k);
                k = null;
            }
            ((Hashtable) this.properties).clear();
        }
        if (this.jbee_props != null) {
            enumeration = ((Hashtable) this.jbee_props).keys();
            while (enumeration.hasMoreElements()) {
                k = enumeration.nextElement();
                ((Hashtable) this.jbee_props).remove(k);
                k = null;
            }
            ((Hashtable) this.jbee_props).clear();
        }
    }

    private Properties copy(Properties properties) {
        Properties properties2 = new Properties();
        Enumeration enumeration = ((Hashtable) properties).keys();
        while (enumeration.hasMoreElements()) {
            String string = (String) enumeration.nextElement();
            String string2 = (String) ((Hashtable) properties).get(string);
            ((Hashtable) properties2).put(new String(string), new String(string2));
        }
        return properties2;
    }

    private void copy(Properties properties, Properties properties2) {
        Enumeration enumeration = ((Hashtable) properties).keys();
        while (enumeration.hasMoreElements()) {
            String string = (String) enumeration.nextElement();
            String string2 = (String) ((Hashtable) properties).get(string);
            ((Hashtable) properties2).put(new String(string), new String(string2));
        }
    }

    public Properties getHiddenProps() {
        if (this.hidden_props == null) {
            this.hidden_props = new Properties();
        }
        return this.hidden_props;
    }

    public Properties getJBeeProps() {
        return this.jbee_props;
    }

    public Properties getParameters() {
        if (!this.hasMergedParamProps) {
            this.copy(this.properties, this.parameters);
            this.hasMergedParamProps = true;
        }
        return this.parameters;
    }

    public abstract JBPropertyEditor getPropertyEditor();

    public Properties getProps() {
        return this.getParameters();
    }

    public void init() {
        ((Hashtable) this.jbee_props).put("Name", " ");
        ((Hashtable) this.jbee_props).put("Window type", "Standard");
        ((Hashtable) this.jbee_props).put("Security", "High");
        Enumeration enumeration = ((Hashtable) this.properties).keys();
        while (enumeration.hasMoreElements()) {
            String string = enumeration.nextElement().toString();
            Object v = ((Hashtable) this.properties).get(string);
            ((Hashtable) this.parameters).put(new String(string.toString()), new String(v.toString()));
        }
    }

    public String toString() {
        String string = String.valueOf(((Hashtable) this.parameters).toString()) + "\n" + ((Hashtable) this.properties).toString() + ((Hashtable) this.jbee_props).toString();
        return string;
    }
}

