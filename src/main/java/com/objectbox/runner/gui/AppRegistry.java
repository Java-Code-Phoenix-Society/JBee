/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import java.util.Enumeration;
import java.util.Hashtable;

public class AppRegistry {
    private static AppRegistry localreg = null;
    private Hashtable registry = new Hashtable();

    private AppRegistry() {
    }

    public static AppRegistry getInstance() {
        if (localreg == null) {
            localreg = new AppRegistry();
            Hashtable hashtable = new Hashtable(500);
            localreg.put("nodehash", hashtable);
        }
        return localreg;
    }

    public void clearAll() {
        if (localreg != null) {
            Enumeration enumeration = AppRegistry.localreg.registry.keys();
            while (enumeration.hasMoreElements()) {
                AppRegistry.localreg.registry.remove(AppRegistry.localreg.registry.get(enumeration.nextElement()));
            }
            AppRegistry.localreg.registry.clear();
        }
    }

    public Object lookup(String string) {
        return this.registry.get(string);
    }

    public void put(String string, Object object) {
        this.registry.put(string, object);
    }

    public void remove(Object object) {
        this.registry.remove(object);
    }

    public Object remove(String string) {
        return this.registry.remove(this.registry.get(string));
    }

    public String toString() {
        return this.registry.toString();
    }
}

