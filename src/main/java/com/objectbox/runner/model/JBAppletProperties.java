/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.model;

import com.objectbox.runner.gui.AppletPropertiesEditorFrame;
import com.objectbox.runner.model.JBProperties;
import com.objectbox.runner.model.JBPropertyEditor;

import java.util.Hashtable;

public class JBAppletProperties
        extends JBProperties {
    static final long serialVersionUID = -123456789L;
    public static transient AppletPropertiesEditorFrame editorFrame = new AppletPropertiesEditorFrame();

    public JBAppletProperties() {
        ((Hashtable) this.parameters).put("width", "");
        ((Hashtable) this.parameters).put("height", "");
        ((Hashtable) this.parameters).put("code", "");
        ((Hashtable) this.parameters).put("codebase", "");
        ((Hashtable) this.parameters).put("documentbase", "");
        ((Hashtable) this.parameters).put("archive", "");
    }

    public JBAppletProperties(JBProperties jBProperties) {
        super(jBProperties);
    }

    public JBPropertyEditor getPropertyEditor() {
        return editorFrame;
    }
}

