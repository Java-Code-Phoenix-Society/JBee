/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.jadvertise;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Launcher {
    private static final int MRJ_2_0 = 0;
    private static final int MRJ_2_1 = 1;
    private static final int WINDOWS_NT = 2;
    private static final int WINDOWS_9x = 3;
    private static final int OTHER = -1;
    private static final String FINDER_TYPE = "FNDR";
    private static final String FINDER_CREATOR = "MACS";
    private static final String GURL_EVENT = "GURL";
    private static final String FIRST_WINDOWS_PARAMETER = "/c";
    private static final String SECOND_WINDOWS_PARAMETER = "start";
    private static final String NETSCAPE_OPEN_PARAMETER_START = " -remote 'openURL(";
    private static final String NETSCAPE_OPEN_PARAMETER_END = ")'";
    static Class class$java$io$File;
    static Class class$java$lang$String;
    private static int jvm;
    private static Object browser;
    private static boolean loadedWithoutErrors;
    private static Class mrjFileUtilsClass;
    private static Class mrjOSTypeClass;
    private static Class macOSErrorClass;
    private static Class aeDescClass;
    private static Constructor aeTargetConstructor;
    private static Constructor appleEventConstructor;
    private static Constructor aeDescConstructor;
    private static Method findFolder;
    private static Method getFileType;
    private static Method makeOSType;
    private static Method putParameter;
    private static Method sendNoReply;
    private static Object kSystemFolderType;
    private static Integer keyDirectObject;
    private static Integer kAutoGenerateReturnID;
    private static Integer kAnyTransactionID;
    private static String errorMessage;

    static {
        block7:
        {
            loadedWithoutErrors = true;
            String string = System.getProperty("os.name");
            if ("Mac OS".equals(string)) {
                String string2 = System.getProperty("mrj.version");
                String string3 = string2.substring(0, 3);
                try {
                    double d = Double.valueOf(string3);
                    if (d == 2.0) {
                        jvm = 0;
                        break block7;
                    }
                    if (d >= 2.1) {
                        jvm = 1;
                        break block7;
                    }
                    loadedWithoutErrors = false;
                    errorMessage = "Unsupported MRJ version: " + d;
                } catch (NumberFormatException numberFormatException) {
                    loadedWithoutErrors = false;
                    errorMessage = "Invalid MRJ version: " + string2;
                }
            } else {
                jvm = string.startsWith("Windows") ? (string.contains("9") ? 3 : 2) : -1;
            }
        }
        if (loadedWithoutErrors) {
            loadedWithoutErrors = Launcher.loadClasses();
        }
    }

    private Launcher() {
    }

    private static boolean loadClasses() {
        switch (jvm) {
            case 0: {
                try {
                    Class<?> clazz = Class.forName("com.apple.MacOS.AETarget");
                    macOSErrorClass = Class.forName("com.apple.MacOS.MacOSError");
                    Class<?> clazz2 = Class.forName("com.apple.MacOS.OSUtils");
                    Class<?> clazz3 = Class.forName("com.apple.MacOS.AppleEvent");
                    Class<?> clazz4 = Class.forName("com.apple.MacOS.ae");
                    aeDescClass = Class.forName("com.apple.MacOS.AEDesc");
                    aeTargetConstructor = clazz.getDeclaredConstructor(Integer.TYPE);
                    appleEventConstructor = clazz3.getDeclaredConstructor(Integer.TYPE, Integer.TYPE, clazz, Integer.TYPE, Integer.TYPE);
                    Class[] classArray = new Class[1];
                    Class<?> clazz5 = class$java$lang$String;
                    if (clazz5 == null) {
                        try {
                            clazz5 = class$java$lang$String = Class.forName("java.lang.String");
                        } catch (ClassNotFoundException classNotFoundException) {
                            throw new NoClassDefFoundError(classNotFoundException.getMessage());
                        }
                    }
                    classArray[0] = clazz5;
                    aeDescConstructor = aeDescClass.getDeclaredConstructor(classArray);
                    Class[] classArray2 = new Class[1];
                    Class<?> clazz6 = class$java$lang$String;
                    if (clazz6 == null) {
                        try {
                            clazz6 = class$java$lang$String = Class.forName("java.lang.String");
                        } catch (ClassNotFoundException classNotFoundException) {
                            throw new NoClassDefFoundError(classNotFoundException.getMessage());
                        }
                    }
                    classArray2[0] = clazz6;
                    makeOSType = clazz2.getDeclaredMethod("makeOSType", classArray2);
                    putParameter = clazz3.getDeclaredMethod("putParameter", Integer.TYPE, aeDescClass);
                    sendNoReply = clazz3.getDeclaredMethod("sendNoReply", new Class[0]);
                    Field field = clazz4.getDeclaredField("keyDirectObject");
                    keyDirectObject = (Integer) field.get(null);
                    Field field2 = clazz3.getDeclaredField("kAutoGenerateReturnID");
                    kAutoGenerateReturnID = (Integer) field2.get(null);
                    Field field3 = clazz3.getDeclaredField("kAnyTransactionID");
                    kAnyTransactionID = (Integer) field3.get(null);
                    break;
                } catch (ClassNotFoundException classNotFoundException) {
                    errorMessage = classNotFoundException.getMessage();
                    return false;
                } catch (NoSuchMethodException noSuchMethodException) {
                    errorMessage = noSuchMethodException.getMessage();
                    return false;
                } catch (NoSuchFieldException noSuchFieldException) {
                    errorMessage = noSuchFieldException.getMessage();
                    return false;
                } catch (IllegalAccessException illegalAccessException) {
                    errorMessage = illegalAccessException.getMessage();
                    return false;
                }
            }
            case 1: {
                try {
                    mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
                    mrjOSTypeClass = Class.forName("com.apple.mrj.MRJOSType");
                    Field field = mrjFileUtilsClass.getDeclaredField("kSystemFolderType");
                    kSystemFolderType = field.get(null);
                    findFolder = mrjFileUtilsClass.getDeclaredMethod("findFolder", mrjOSTypeClass);
                    Class[] classArray = new Class[1];
                    Class<?> clazz = class$java$io$File;
                    if (clazz == null) {
                        try {
                            clazz = class$java$io$File = Class.forName("java.io.File");
                        } catch (ClassNotFoundException classNotFoundException) {
                            throw new NoClassDefFoundError(classNotFoundException.getMessage());
                        }
                    }
                    classArray[0] = clazz;
                    getFileType = mrjFileUtilsClass.getDeclaredMethod("getFileType", classArray);
                    break;
                } catch (ClassNotFoundException classNotFoundException) {
                    errorMessage = classNotFoundException.getMessage();
                    return false;
                } catch (NoSuchFieldException noSuchFieldException) {
                    errorMessage = noSuchFieldException.getMessage();
                    return false;
                } catch (NoSuchMethodException noSuchMethodException) {
                    errorMessage = noSuchMethodException.getMessage();
                    return false;
                } catch (SecurityException securityException) {
                    errorMessage = securityException.getMessage();
                    return false;
                } catch (IllegalAccessException illegalAccessException) {
                    errorMessage = illegalAccessException.getMessage();
                    return false;
                }
            }
        }
        return true;
    }

    private static Object locateBrowser() {
        if (browser != null) {
            return browser;
        }
        switch (jvm) {
            case 0: {
                try {
                    Integer n = (Integer) makeOSType.invoke(null, FINDER_CREATOR);
                    Object t = aeTargetConstructor.newInstance(n);
                    Integer n2 = (Integer) makeOSType.invoke(null, GURL_EVENT);
                    return appleEventConstructor.newInstance(n2, n2, t, kAutoGenerateReturnID, kAnyTransactionID);
                } catch (IllegalAccessException | InstantiationException |
                         InvocationTargetException illegalAccessException) {
                    browser = null;
                    errorMessage = illegalAccessException.getMessage();
                    return browser;
                }
            }
            case 1: {
                File file;
                try {
                    file = (File) findFolder.invoke(null, kSystemFolderType);
                } catch (IllegalArgumentException illegalArgumentException) {
                    browser = null;
                    errorMessage = illegalArgumentException.getMessage();
                    return browser;
                } catch (IllegalAccessException illegalAccessException) {
                    browser = null;
                    errorMessage = illegalAccessException.getMessage();
                    return browser;
                } catch (InvocationTargetException invocationTargetException) {
                    browser = null;
                    errorMessage = invocationTargetException.getTargetException().getClass() + ": " + invocationTargetException.getTargetException().getMessage();
                    return browser;
                }
                String[] stringArray = file.list();
                int n = 0;
                while (true) {
                    assert stringArray != null;
                    if (!(n < stringArray.length)) break;
                    try {
                        File file2 = new File(file, stringArray[n]);
                        if (file2.isFile()) {
                            Object object = getFileType.invoke(null, file2);
                            if (FINDER_TYPE.equals(object.toString())) {
                                browser = file2.toString();
                                return browser;
                            }
                        }
                    } catch (IllegalArgumentException illegalArgumentException) {
                        errorMessage = illegalArgumentException.getMessage();
                        return null;
                    } catch (IllegalAccessException illegalAccessException) {
                        browser = null;
                        errorMessage = illegalAccessException.getMessage();
                        return browser;
                    } catch (InvocationTargetException invocationTargetException) {
                        browser = null;
                        errorMessage = invocationTargetException.getTargetException().getClass() + ": " + invocationTargetException.getTargetException().getMessage();
                        return browser;
                    }
                    ++n;
                }
                browser = null;
                break;
            }
            case 2: {
                browser = "cmd.exe";
                break;
            }
            case 3: {
                browser = "command.com";
                break;
            }
            default: {
                browser = "netscape";
            }
        }
        return browser;
    }

    public static void openURL(String string) throws IOException {
        if (!loadedWithoutErrors) {
            throw new IOException("Exception in finding browser: " + errorMessage);
        }
        Object object = Launcher.locateBrowser();
        if (object == null) {
            throw new IOException("Unable to locate browser: " + errorMessage);
        }
        switch (jvm) {
            case 0: {
                Object var2_2 = null;
                try {
                    try {
                        var2_2 = aeDescConstructor.newInstance(string);
                        putParameter.invoke(object, keyDirectObject, var2_2);
                        sendNoReply.invoke(object, new Object[0]);
                    } catch (InvocationTargetException invocationTargetException) {
                        throw new IOException("InvocationTargetException while creating AEDesc: " + invocationTargetException.getMessage());
                    } catch (IllegalAccessException illegalAccessException) {
                        throw new IOException("IllegalAccessException while building AppleEvent: " + illegalAccessException.getMessage());
                    } catch (InstantiationException instantiationException) {
                        throw new IOException("InstantiationException while creating AEDesc: " + instantiationException.getMessage());
                    }
                } catch (Throwable throwable) {
                    Object var3_9 = null;
                    var2_2 = null;
                    object = null;
                    throw throwable;
                }
                Object var3_10 = null;
                var2_2 = null;
                object = null;
                break;
            }
            case 1: {
                Runtime.getRuntime().exec(new String[]{(String) object, string});
                break;
            }
            case 2:
            case 3: {
                Runtime.getRuntime().exec(new String[]{(String) object, FIRST_WINDOWS_PARAMETER, SECOND_WINDOWS_PARAMETER, string});
                break;
            }
            case -1: {
                Process process = Runtime.getRuntime().exec(object + NETSCAPE_OPEN_PARAMETER_START + string + NETSCAPE_OPEN_PARAMETER_END);
                try {
                    int n = process.waitFor();
                    if (n == 0) break;
                    Runtime.getRuntime().exec(new String[]{(String) object, string});
                    break;
                } catch (InterruptedException interruptedException) {
                    throw new IOException("InterruptedException while launching browser: " + interruptedException.getMessage());
                }
            }
            default: {
                Runtime.getRuntime().exec(new String[]{(String) object, string});
            }
        }
    }
}

