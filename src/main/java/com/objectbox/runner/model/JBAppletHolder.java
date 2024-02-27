/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import com.objectbox.loader.JBURLClassloader;
import com.objectbox.runner.gui.BeanRunner;
import com.objectbox.runner.gui.JBee;
import com.objectbox.runner.model.JBAppletProperties;
import com.objectbox.runner.model.JBHolder;
import com.objectbox.runner.model.JBProperties;
import com.objectbox.runner.util.JBLogger;

import java.util.Hashtable;
import java.util.Properties;

public class JBAppletHolder
        extends JBHolder {
    static final long serialVersionUID = -123456789L;

    public JBAppletHolder() {
        this.props = new JBAppletProperties();
    }

    public BeanRunner run(ThreadGroup threadGroup) {
        BeanRunner beanRunner = null;
        try {
            JBProperties jBProperties = this.getProperties();
            Properties properties = jBProperties.getProps();
            String string = (String) ((Hashtable) properties).get("code");
            ThreadGroup threadGroup2 = new ThreadGroup(threadGroup, string);
            beanRunner = new BeanRunner(threadGroup2, string);
            String string2 = JBee.getPreference("javabee_home");
            if (string2 == null) {
                string2 = "." + System.getProperty("file.separator");
            } else if (!string2.endsWith(System.getProperty("file.separator"))) {
                string2 = String.valueOf(string2) + System.getProperty("file.separator");
            }
            String string3 = String.valueOf(string2) + "cache";
            JBURLClassloader jBURLClassloader = new JBURLClassloader(string3);
            String string4 = JBee.getPreference("usebytecodecache");
            if (string4 == null || string4.compareTo("true") == 0) {
                jBURLClassloader.setCacheOn(true);
            } else {
                jBURLClassloader.setCacheOn(false);
            }
            String string5 = JBee.getPreference("checkversion");
            if (string5 == null || string5.compareTo("true") == 0) {
                jBURLClassloader.setChecklastmodified(true);
            } else {
                jBURLClassloader.setChecklastmodified(false);
            }
            beanRunner.set(jBURLClassloader, jBProperties);
            beanRunner.setDelay(3000L);
            beanRunner.start();
        } catch (Throwable throwable) {
            JBLogger.log(throwable.getMessage());
        }
        return beanRunner;
    }
}

