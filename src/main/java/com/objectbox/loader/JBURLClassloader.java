/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.loader;

import com.objectbox.runner.beans.DownloadView;
import com.objectbox.runner.util.JBLogger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.*;

public class JBURLClassloader
        extends ClassLoader
        implements Serializable {
    private static final String bytecodecachefilename = "state.ser";
    static int counter1;
    protected transient StateChangeListener aStateChangeListener = null;
    private Vector cachenames_used = new Vector();
    private String host;
    private String protocol;
    private int port;
    private Class ourClass = null;
    private Hashtable classList = new Hashtable();
    private Hashtable bytecodeList = new Hashtable();
    private Hashtable resourceList = new Hashtable();
    private Hashtable resourceLoaded = new Hashtable();
    private int vector_counter;
    private String directory = null;
    private String server = null;
    private String className = null;
    private String UnresolvedURL = null;
    private String temporaryURL = null;
    private boolean flagClass;
    private String codebase;
    private String jarfilename;
    private boolean firstrun = true;
    private boolean jarloaded = false;
    private String rootclass;
    private String namespace = "";
    private boolean treeresolved = false;
    private boolean cacheOn = true;
    private boolean checklastmodified = true;
    private String filesep = System.getProperty("file.separator");
    private String cachedirectory = ".";
    private Object owner;
    private boolean caching = false;
    private String securityID = "";
    private boolean is_saved = false;

    public JBURLClassloader(String string) {
        this.setCachedirectory(string);
        if (this.classList == null) {
            this.classList = new Hashtable();
        }
    }

    public void addMetaData(String string, Object object) {
        this.resourceList.put(string, object);
    }

    public boolean addResource(String string, byte[] byArray) {
        this.resourceList.put(string, byArray);
        this.log(new String(byArray));
        return true;
    }

    public void addStateChangeListener(StateChangeListener stateChangeListener) {
        this.aStateChangeListener = StateChangeEventMulticaster.add(this.aStateChangeListener, stateChangeListener);
    }

    private synchronized Class createClassFromByteCodes(String string, byte[] byArray, boolean bl) {
        Class<?> clazz = null;
        try {
            this.log("defineClass " + string);
            clazz = this.defineClass(string, byArray, 0, byArray.length);
            this.classList.put(String.valueOf(this.namespace) + string, clazz);
            if (bl) {
                this.resolveClass(clazz);
                this.log("resolveClass " + string);
            } else {
                this.log("defineClass not resolved" + string);
            }
        } catch (ClassFormatError classFormatError) {
            this.log("Could not create class " + string + " " + classFormatError);
            return clazz;
        } catch (Throwable throwable) {
            this.log(throwable.getMessage());
            this.rethrowException(throwable);
            return clazz;
        }
        return clazz;
    }

    public synchronized void dispose() {
        this.nullHashtable(this.classList);
        this.nullHashtable(this.bytecodeList);
        this.nullHashtable(this.resourceList);
        this.nullHashtable(this.resourceLoaded);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private byte[] downloadByteCodesFromURL(String string, DataInputStream dataInputStream) {
        try {
            this.log("Loading classbytes for " + string + " from URL");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                while (true) {
                    byteArrayOutputStream.write(dataInputStream.readByte());
                    DownloadView.addBytes(1L);
                }
            } catch (IOException iOException) {
                byte[] byArray = byteArrayOutputStream.toByteArray();
                ((OutputStream) byteArrayOutputStream).close();
                this.bytecodeList.put(String.valueOf(this.namespace) + string, byArray);
                return byArray;
            }
        } catch (Throwable throwable) {
            this.log(throwable.getMessage());
            this.rethrowException(throwable);
            return null;
        }
    }

    protected void finalize() throws Throwable {
        this.log("classloader in finalize");
        if (!this.is_saved) {
            this.saveme();
            this.dispose();
        }
    }

    protected void fireOnChange(StateChangeEvent stateChangeEvent) {
        if (this.aStateChangeListener == null) {
            return;
        }
        this.aStateChangeListener.onChange(stateChangeEvent);
    }

    public String getCachedirectory() {
        return this.cachedirectory;
    }

    public void setCachedirectory(String string) {
        this.cachedirectory = string;
        File file = new File(this.cachedirectory);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
    }

    public Vector getCacheNames() {
        return this.cachenames_used;
    }

    public boolean getCacheOn() {
        return this.cacheOn;
    }

    public void setCacheOn(boolean bl) {
        this.cacheOn = bl;
    }

    public boolean getCaching() {
        return this.caching;
    }

    private void setCaching(boolean bl) {
        this.caching = bl;
    }

    public boolean getChecklastmodified() {
        return this.checklastmodified;
    }

    public void setChecklastmodified(boolean bl) {
        this.checklastmodified = bl;
    }

    public String getClassName() {
        return this.className;
    }

    public String getCodebase() {
        return this.codebase;
    }

    public void setCodebase(String string) {
        this.codebase = this.removeSlashes(string);
        this.namespace = String.valueOf(this.makeValidUrl("")) + "_";
    }

    public Object getMetaData(String string) {
        return this.resourceList.get(string);
    }

    public Object getOwner() {
        return this.owner;
    }

    public void setOwner(Object object) {
        this.owner = object;
    }

    public int getPort() {
        return this.port;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public URL getResource(String string) {
        try {
            URL uRL = this.getClass().getResource(string);
            if (uRL != null) {
                this.log("Loading URL resource from system " + string);
                return uRL;
            }
            this.log("Loading URL resource " + string);
            URL uRL2 = new URL(this.makeValidUrl(string));
            this.log(uRL2.toString());
            return uRL2;
        } catch (Throwable throwable) {
            this.log(throwable.getMessage());
            this.rethrowException(throwable);
            return null;
        }
    }

    public InputStream getResourceAsStream(String string) {
        try {
            if (this.resourceList.get(string) != null) {
                byte[] byArray = (byte[]) this.resourceList.get(string);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
                return byteArrayInputStream;
            }
            InputStream inputStream = this.getClass().getResourceAsStream(string);
            if (inputStream != null) {
                this.log("Loading resources from stream from system :" + string);
                return inputStream;
            }
            this.log("Loading resources from stream:" + string);
            URL uRL = new URL(this.makeValidUrl(string));
            this.log(uRL.toString());
            inputStream = uRL.openStream();
            return inputStream;
        } catch (Throwable throwable) {
            this.log(throwable.getMessage());
            this.rethrowException(throwable);
            return null;
        }
    }

    public String getRootclass() {
        return this.rootclass;
    }

    public void setRootclass(String string) {
        this.rootclass = string;
    }

    public String getSecurityID() {
        return this.securityID;
    }

    public void setSecurityID(String string) {
        this.securityID = string;
    }

    public String getServer() {
        try {
            if (!this.codebase.startsWith("http") && !this.codebase.startsWith("file")) {
                URL uRL = new URL(this.server);
                return uRL.getHost();
            }
            URL uRL = new URL(this.codebase);
            return uRL.getHost();
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
            return null;
        }
    }

    public void setServer(String string) {
        this.server = this.removeSlashes(string);
        this.namespace = String.valueOf(this.makeValidUrl("")) + "_";
    }

    public Class getTheClass() {
        return this.ourClass;
    }

    public final Class loadClass(String string, boolean bl) throws ClassNotFoundException {
        int n;
        Class clazz = null;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null && (n = string.lastIndexOf(46)) >= 0) {
            securityManager.checkPackageAccess(string.substring(0, n));
            securityManager.checkPackageDefinition(string.substring(0, n));
        }
        this.log("loadClass => " + string);
        try {
            clazz = this.findSystemClass(string);
            this.log("Resolved " + string + " locally");
            this.setState(3);
        } catch (ClassNotFoundException classNotFoundException) {
            this.log("Resolving Class: " + string);
            clazz = (Class) this.classList.get(string);
            if (clazz == null) {
                try {
                    this.log("Resolving " + string + " remotely");
                    this.setState(2);
                    clazz = this.loadClassName(string);
                    if (clazz == null) {
                        this.log("Could not find class " + string);
                        return null;
                    }
                    return clazz;
                } catch (NoClassDefFoundError noClassDefFoundError) {
                    this.log(noClassDefFoundError.getMessage());
                    return null;
                } catch (Throwable throwable) {
                    this.log(throwable.getMessage());
                    return null;
                }
            }
        } catch (NoClassDefFoundError noClassDefFoundError) {
            this.log(noClassDefFoundError.getMessage());
            return clazz;
        } catch (Throwable throwable) {
            this.log(throwable.getMessage());
            return clazz;
        }
        return clazz;
    }

    private Class loadClassFromCache(String string) throws Exception {
        try {
            this.setState(3);
            if (this.classList.get(String.valueOf(this.namespace) + string) != null) {
                this.log("Loading class from class cache." + string);
                return (Class) this.classList.get(String.valueOf(this.namespace) + string);
            }
            this.log(String.valueOf(string) + " is not in class cache");
            if (this.bytecodeList.get(String.valueOf(this.namespace) + string) != null) {
                byte[] byArray = (byte[]) this.bytecodeList.get(String.valueOf(this.namespace) + string);
                this.log("Loaded bytecodes from bytecode cache." + string);
                return this.createClassFromByteCodes(string, byArray, true);
            }
            this.log(String.valueOf(string) + " is not in bytecode cache");
        } catch (Throwable throwable) {
            this.log(throwable.getMessage());
            this.rethrowException(throwable);
            return null;
        }
        return null;
    }

    private Class loadClassFromHTTP(String string) throws Exception {
        this.setState(2);
        InputStream inputStream = null;
        URL uRL = null;
        FilterInputStream filterInputStream = null;
        HttpURLConnection httpURLConnection = null;
        Class clazz = null;
        try {
            String string2 = this.makeValidUrl(String.valueOf(string.replace('.', '/')) + ".class");
            uRL = new URL(string2);
            this.log("Retrieving class from:" + string2);
            this.port = uRL.getPort() == -1 ? 80 : uRL.getPort();
            this.protocol = uRL.getProtocol();
            httpURLConnection = (HttpURLConnection) uRL.openConnection();
            inputStream = httpURLConnection.getInputStream();
            filterInputStream = new DataInputStream(inputStream);
            byte[] byArray = this.downloadByteCodesFromURL(string, (DataInputStream) filterInputStream);
            filterInputStream.close();
            inputStream.close();
            inputStream = null;
            uRL = null;
            System.gc();
            clazz = this.createClassFromByteCodes(string, byArray, true);
            this.log("Loaded bytecodes from HTTP server " + this.server);
            return clazz;
        } catch (Throwable throwable) {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    if (filterInputStream != null) {
                        filterInputStream.close();
                    }
                }
            } catch (Throwable throwable2) {
                this.log(throwable2.getMessage());
                return null;
            }
            uRL = null;
            this.ourClass = null;
            this.log("Problems Defining Network Class " + throwable);
            return null;
        }
    }

    private Class loadClassName(String string) {
        this.log("loadClassName " + string);
        Class clazz = null;
        try {
            clazz = this.resolveClass(string);
            return clazz;
        } catch (Throwable throwable) {
            this.log(throwable.getMessage());
            this.rethrowException(throwable);
            return null;
        }
    }

    private boolean loadJARArchive(String jarFile) {
        if (this.resourceLoaded.get(jarFile) != null) {
            return true;
        } else {
            JBLogger.log("\nLoading jar archive " + jarFile);
            InputStream inputStream = null;
            URL url = null;
            String string = "";
            boolean classCheck = true;
            ZipInputStream zipInputStream = null;

            try {
                String validUrl = this.makeValidUrl(jarFile);
                String hashCode = validUrl.hashCode() + "_" + this.rootclass + ".jar";
                File file = new File(this.cachedirectory + this.filesep + hashCode);
                URLConnection urlConn = null;
                long lastModified = file.lastModified();
                long length = file.length();
                long webLength = 0L;
                long urlFileLastMod = 0L;
                JBLogger.log("Local file lastmodified:" + lastModified);
                int i;
                if (file.exists() && !this.getChecklastmodified()) {
                    JBLogger.log("Using local copy of jar file archive.");
                    this.setState(3);
                    if (this.restoreme()) {
                        return true;
                    }
                } else {
                    url = new URL(validUrl);
                    this.log(url.toString());
                    urlConn = url.openConnection();
                    urlFileLastMod = urlConn.getLastModified();
                    if (urlFileLastMod == 0L) {
                        JBLogger.log("Last modified not found.");
                    } else {
                        JBLogger.log("URL file lastmodified:" + urlFileLastMod);
                    }

                    JBLogger.log("content-type:" + urlConn.getContentType() + " content-length:" + urlConn.getContentLength());
                    webLength = urlConn.getContentLength();
                    if (!file.exists() || length != webLength) {
                        this.setState(2);
                        JBLogger.log("Copying file from server. " + length + "!=" + webLength);
                        JBLogger.log("URL file lastmodified:" + urlFileLastMod);
                        inputStream = urlConn.getInputStream();
                        FileOutputStream fileOutputStream = new FileOutputStream(this.cachedirectory + this.filesep + hashCode);
                        this.cachenames_used.addElement(file.toString());

                        while ((i = inputStream.read()) != -1) {
                            fileOutputStream.write(i);
                        }

                        fileOutputStream.close();
                    }
                }

                FileInputStream fileInputStream = new FileInputStream(this.cachedirectory + this.filesep + hashCode);
                CRC32 crc32 = new CRC32();
                CheckedInputStream checkedInputStream = new CheckedInputStream(fileInputStream, crc32);
                zipInputStream = new ZipInputStream(checkedInputStream);
                JBLogger.log("Checksum1=" + crc32.getValue());
                ZipEntry zipEntry = null;

                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int compressedSize = (int) zipEntry.getCompressedSize();
                    if (this.bytecodeList.get(zipEntry.getName()) != null) {
                        break;
                    }

                    while ((i = zipInputStream.read()) != -1) {
                        outputStream.write(i);
                        DownloadView.addBytes(1L);
                    }

                    classCheck = zipEntry.getName().endsWith(".class");
                    if (classCheck) {
                        string = zipEntry.getName().replace('/', '.');
                        string = string.substring(0, string.lastIndexOf(46));
                    }

                    byte[] streamByteArray = outputStream.toByteArray();
                    outputStream.close();
                    if (classCheck) {
                        JBLogger.log("Class: " + zipEntry.getName() + " " + compressedSize + " bytes");
                        this.bytecodeList.put(this.namespace + string, streamByteArray);
                    } else {
                        JBLogger.log("Resource: " + zipEntry.getName() + " " + compressedSize + " bytes");
                        this.resourceList.put(zipEntry.getName(), streamByteArray);
                    }
                }

                zipInputStream.close();
                JBLogger.log("Checksum2=" + crc32.getValue());
                if (!this.validateChecksum(crc32.getValue(), jarFile)) {
                    return false;
                }

                this.resourceLoaded.put(jarFile, jarFile);
                JBLogger.log(this.resourceLoaded.toString());
                JBLogger.log(this.resourceList.toString());
            } catch (Throwable t) {
                JBLogger.log("\nError:" + t.toString());
                this.jarloaded = false;
            }

            return false;
        }
    }

    public void loadJarfile(boolean bl) {
        StringTokenizer stringTokenizer = new StringTokenizer(this.jarfilename, ",");
        while (stringTokenizer.hasMoreTokens()) {
            this.loadJARArchive(stringTokenizer.nextToken());
        }
        this.jarloaded = bl;
    }

    private void log(String string) {
        JBLogger.log(string);
    }

    public void makeFile(String string, byte[] byArray) {
        this.log("makeFile " + string);
        try {
            File file = new File(String.valueOf(this.cachedirectory) + this.filesep + "resources", string);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byArray);
            fileOutputStream.close();
        } catch (Exception exception) {
            this.log(exception.getMessage());
        }
    }

    public boolean makeJARArchive(String string) throws Throwable {
        Object object;
        Object object2;
        String string2;
        FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(this.cachedirectory) + this.filesep + "testjar.jar");
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        Enumeration enumeration = this.bytecodeList.keys();
        while (enumeration.hasMoreElements()) {
            string2 = (String) enumeration.nextElement();
            object2 = String.valueOf(string2.replace('.', this.filesep.charAt(0))) + ".class";
            object = this.bytecodeList.get(string2);
            this.makeFile((String) object2, (byte[]) object);
            ZipEntry zipEntry = new ZipEntry((String) object2);
            zipEntry.setSize(((byte[]) object).length);
            zipEntry.setTime(System.currentTimeMillis());
            zipEntry.setMethod(8);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write((byte[]) object);
            ((FilterOutputStream) zipOutputStream).flush();
            zipOutputStream.closeEntry();
        }
        enumeration = this.resourceList.keys();
        while (enumeration.hasMoreElements()) {
            string2 = (String) enumeration.nextElement();
            object2 = this.resourceList.get(string2);
            this.makeFile(string2, (byte[]) object2);
            new String();
            this.log(String.valueOf(string2) + " " + String.valueOf(((byte[]) object2).length));
            object = new ZipEntry(string2);
            ((ZipEntry) object).setSize(((byte[]) object2).length);
            ((ZipEntry) object).setTime(System.currentTimeMillis());
            ((ZipEntry) object).setComment("Test kommentar");
            ((ZipEntry) object).setMethod(8);
            zipOutputStream.putNextEntry((ZipEntry) object);
            zipOutputStream.write((byte[]) object2);
            ((FilterOutputStream) zipOutputStream).flush();
            zipOutputStream.closeEntry();
        }
        zipOutputStream.setMethod(8);
        zipOutputStream.close();
        return false;
    }

    public String makeValidUrl(String string) {
        String string2 = "";
        if (string.startsWith("http:") || string.startsWith("file:")) {
            return string;
        }
        if (string.startsWith("/")) {
            return String.valueOf(this.server) + string;
        }
        string2 = !this.codebase.startsWith("http") && !this.codebase.startsWith("file") ? String.valueOf(this.server) + "/" + (this.codebase.startsWith(".") ? "" : this.codebase) + (this.codebase.length() > 0 ? "/" : "") + string : String.valueOf(this.codebase) + "/" + string;
        return string2;
    }

    private void nullHashtable(Hashtable hashtable) {
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            enumeration.nextElement();
        }
        hashtable.clear();
    }

    public void readCacheFromFile() {
        if (!this.restoreme()) {
            this.log("Creating new cache");
        } else {
            this.log("Using existing cache");
        }
    }

    public String removeSlashes(String string) {
        if (string.length() == 0) {
            return string;
        }
        String string2 = string;
        if (string.startsWith("/")) {
            string2 = string.substring(1, string.length());
        }
        if (string2.endsWith("/")) {
            string2 = string2.substring(0, string2.lastIndexOf("/"));
        }
        return string2;
    }

    public void removeStateChangeListener(StateChangeListener stateChangeListener) {
        this.aStateChangeListener = StateChangeEventMulticaster.remove(this.aStateChangeListener, stateChangeListener);
    }

    public Class resolveClass(String string) throws IOException {
        try {
            this.setState(1);
            Class clazz = null;
            if (string.endsWith(".class")) {
                string = string.substring(0, string.lastIndexOf("."));
            }
            if ((clazz = this.loadClassFromCache(string)) != null) {
                return clazz;
            }
            clazz = this.loadClassFromHTTP(string);
            return clazz;
        } catch (Throwable throwable) {
            JBLogger.log(throwable.toString());
            this.rethrowException(throwable);
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean restoreme() {
        try {
            try {
                this.caching = true;
                if (this.bytecodeList.size() > 0) {
                    Object var1_1 = null;
                    this.caching = false;
                    return true;
                }
                String string = String.valueOf(this.cachedirectory) + this.filesep + this.namespace.hashCode() + "_" + this.rootclass + "_" + bytecodecachefilename;
                File file = new File(string);
                this.cachenames_used.addElement(file.toString());
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, (int) file.length());
                ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
                this.rootclass = (String) objectInputStream.readObject();
                this.bytecodeList = (Hashtable) objectInputStream.readObject();
                this.resourceList = (Hashtable) objectInputStream.readObject();
                this.resourceLoaded = (Hashtable) objectInputStream.readObject();
                this.log("Restored bytecodes\n" + this.bytecodeList);
                this.log("Restored resources\n" + this.resourceList);
                this.log("Restored resourcesloaded\n" + this.resourceLoaded);
                objectInputStream.close();
            } catch (Throwable throwable) {
                JBLogger.log(throwable.toString());
                Object var1_3 = null;
                this.caching = false;
                return false;
            }
        } catch (Throwable throwable) {
            Object var1_4 = null;
            this.caching = false;
            throw throwable;
        }
        Object var1_2 = null;
        this.caching = false;
        return true;
    }

    private void rethrowException(Exception exception) {
    }

    private void rethrowException(Throwable throwable) {
    }

    public void saveme() {
        try {
            try {
                this.caching = true;
                this.log("Saving bytecodes\n" + this.bytecodeList);
                String string = String.valueOf(this.cachedirectory) + this.filesep + this.namespace.hashCode() + "_" + this.rootclass + "_" + bytecodecachefilename;
                File file = new File(string);
                this.cachenames_used.addElement(file.toString());
                if (!file.exists()) {
                    JBLogger.log("File does not exists ...");
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 10240);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
                objectOutputStream.writeObject(this.rootclass);
                objectOutputStream.writeObject(this.bytecodeList);
                objectOutputStream.writeObject(this.resourceList);
                objectOutputStream.writeObject(this.resourceLoaded);
                objectOutputStream.close();
                this.is_saved = true;
            } catch (IOException iOException) {
                this.log(iOException.getMessage());
            } catch (Throwable throwable) {
                this.log(throwable.getMessage());
            }
        } catch (Throwable throwable) {
            Object var1_9 = null;
            this.caching = false;
            throw throwable;
        }
        Object var1_10 = null;
        this.caching = false;
    }

    public void setJarfile(String string) {
        this.jarfilename = string;
        this.loadJarfile(true);
    }

    public void setState(int n) {
        this.fireOnChange(new StateChangeEvent(this, n));
    }

    public String toString() {
        return "objectBOX (c) 1999 JavaBee Classloader loading:" + this.codebase + "/" + this.rootclass;
    }

    public void uploadFile(String string) {
        try {
            Object var2_2 = null;
            URL uRL = new URL(String.valueOf(this.server) + "/" + this.codebase);
            URLConnection uRLConnection = uRL.openConnection();
            uRLConnection.setDoOutput(true);
            uRLConnection.setUseCaches(false);
            uRLConnection.setRequestProperty("Content-type", "application/octet-stream");
            OutputStream outputStream = uRLConnection.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(var2_2);
            objectOutputStream.close();
            InputStream inputStream = uRLConnection.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception exception) {
            this.log(exception.getMessage());
        }
    }

    public boolean validateChecksum(long l, String string) {
        return true;
    }
}

