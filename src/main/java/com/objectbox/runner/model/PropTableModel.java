package com.objectbox.runner.model;

import com.objectbox.runner.beans.AbstrTableModel;
import com.objectbox.runner.util.JBLogger;
import com.objectbox.util.BinaryPredicate;
import com.objectbox.util.SortedVector;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class PropTableModel
        extends AbstrTableModel {
    private String[] h = new String[]{"Name", "Value"};
    private int nrows = 0;
    private Properties props = null;
    private Properties param = null;
    private Properties jbprops = null;
    private JBProperties jbee_properties = null;
    private Vector dataVector = new Vector();

    public String computeCacheSize() {
        int n = 0;
        try {
            Object v;
            Properties properties;
            if (this.jbee_properties != null && (properties = this.jbee_properties.getHiddenProps()) != null && (v = ((Hashtable) properties).get("cachenames")) != null) {
                Vector vector = (Vector) v;
                Enumeration enumeration = vector.elements();
                while (enumeration.hasMoreElements()) {
                    String string = enumeration.nextElement().toString();
                    if (string.startsWith("http")) continue;
                    File file = new File(string);
                    n = (int) ((long) n + file.length());
                }
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in PropTableModel::computeCacheSize():" + throwable);
        }
        JBLogger.log("Cache size: " + n);
        return String.valueOf(n);
    }

    public void delete(int n) {
        Vector vector = (Vector) this.dataVector.elementAt(n - 1);
        String string = (String) vector.elementAt(2);
        if (string.compareTo("jbprops") == 0) {
            return;
        }
        this.dataVector.removeElement(vector);
        if (string.compareTo("param") == 0) {
            ((Hashtable) this.param).remove(vector.elementAt(0));
        }
        if (string.compareTo("props") == 0) {
            ((Hashtable) this.props).remove(vector.elementAt(0));
        }
    }

    public void deleteCache() {
        try {
            Object v;
            Properties properties;
            if (this.jbee_properties != null && (properties = this.jbee_properties.getHiddenProps()) != null && (v = ((Hashtable) properties).get("cachenames")) != null) {
                Vector vector = (Vector) v;
                Enumeration enumeration = vector.elements();
                while (enumeration.hasMoreElements()) {
                    File file;
                    String string = enumeration.nextElement().toString();
                    if (string.startsWith("http") || (file = new File(string)).isDirectory()) continue;
                    if (file.delete()) {
                        JBLogger.log("Deleted " + file + " from cache");
                        continue;
                    }
                    JBLogger.log("Could not delete" + file + " from cache");
                }
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception in PropTableModel::deleteCache():" + throwable);
        }
    }

    public String getCell(int n, int n2) {
        Vector vector = (Vector) this.dataVector.elementAt(n2 - 1);
        return (String) vector.elementAt(n);
    }

    public JBProperties getData() {
        return this.jbee_properties;
    }

    public void setData(JBProperties data) {
        this.jbee_properties = data;
        this.param = data.getParameters();
        this.jbprops = data.getJBeeProps();
        this.dataVector.removeAllElements();
        SortedVector vector = new SortedVector();
        Enumeration enumeration = this.jbprops.propertyNames();

        Object item;
        PropertyRow row;
        while (enumeration.hasMoreElements()) {
            item = enumeration.nextElement();
            row = new PropertyRow();
            row.addElement(item);
            row.addElement(this.jbprops.get(item));
            row.addElement("jbprops");
            vector.addElement(row);
        }

        vector.sort((BinaryPredicate) null);
        enumeration = vector.elements();

        while (enumeration.hasMoreElements()) {
            this.dataVector.addElement(enumeration.nextElement());
        }

        vector.removeAllElements();
        enumeration = this.param.propertyNames();

        while (enumeration.hasMoreElements()) {
            item = enumeration.nextElement();
            row = new PropertyRow();
            row.addElement(item);
            row.addElement(this.param.get(item));
            row.addElement("param");
            vector.addElement(row);
        }

        vector.sort((BinaryPredicate) null);
        enumeration = vector.elements();

        while (enumeration.hasMoreElements()) {
            this.dataVector.addElement(enumeration.nextElement());
        }

    }

    public String[] getHeaders() {
        return this.h;
    }

    public void setHeaders(String[] stringArray) {
        this.h = stringArray;
    }

    public int getNumberOfColumns() {
        return this.getHeaders().length;
    }

    public int getNumberOfRows() {
        return this.dataVector.size() + 1;
    }

    public void init(String string) {
        int n = 0;
        while (n < 10) {
            ++n;
        }
    }

    public void newParameter(String string, String string2) {
        ((Hashtable) this.param).put(string, string2);
        this.setData(this.jbee_properties);
    }

    public void newProperty(String string, String string2) {
        ((Hashtable) this.props).put(string, string2);
        this.setData(this.jbee_properties);
    }

    public void setCell(int n, int n2, String string) {
        Vector vector = (Vector) this.dataVector.elementAt(n2 - 1);
        vector.setElementAt(string, n);
        Object e = vector.elementAt(0);
        vector.elementAt(1);
        Object e2 = vector.elementAt(2);
        if (e2.equals("props")) {
            ((Hashtable) this.props).put(e, string);
        } else if (e2.equals("param")) {
            ((Hashtable) this.param).put(e, string);
        } else if (e2.equals("jbprops")) {
            ((Hashtable) this.jbprops).put(e, string);
        }
    }
}

