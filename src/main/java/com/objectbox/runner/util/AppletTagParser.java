/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.util;

import com.objectbox.runner.beans.DownloadView;
import com.objectbox.runner.gui.JBSearchPanel;
import com.objectbox.runner.model.JBAppletProperties;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Vector;

public class AppletTagParser
        extends Thread {
    static URL firsturl = null;
    static String localHost = null;
    static Object handle = null;
    static Vector urlList = new Vector();
    static Hashtable visited = new Hashtable();
    static Hashtable noGoHash = new Hashtable(10);
    static int pagesChecked = 0;
    static int maxThreads;
    static int currThreads;
    static boolean checkRefs;
    static boolean isRunning;
    static boolean remoteCheck;
    static int localLevel;
    static int remoteLevel;
    static int appletsFound;

    static {
        currThreads = maxThreads = 7;
        checkRefs = false;
        isRunning = true;
        remoteCheck = false;
        localLevel = 5;
        remoteLevel = 1;
        appletsFound = 0;
    }

    Hyperlink startlink = null;
    Vector appletList = new Vector();

    public AppletTagParser() {
    }

    public AppletTagParser(ThreadGroup threadGroup, String string) {
        super(threadGroup, string);
    }

    private JBAppletProperties getAppletAttributes(String string) {
        int n = -1;
        int n2 = string.length();
        String string2 = "";
        String string3 = "";
        int n3 = 0;
        char c = ' ';
        JBAppletProperties jBAppletProperties = new JBAppletProperties();
        n3 = string.indexOf(61, n3 + 1);
        do {
            c = ' ';
            int n4 = n3 - 1;
            while (string.charAt(n4) == c & n4 >= 0) {
                n4 += n;
            }
            int n5 = n4 - 1;
            if (string.charAt(n5) == '\"') {
                c = '\"';
                --n5;
            }
            if (string.charAt(n5) == '\'') {
                c = '\'';
                --n5;
            }
            while (string.charAt(n5) != c & n5 >= 0) {
                n5 += n;
            }
            string2 = string.substring(n5, n4 + 1);
            c = ' ';
            n5 = n3 + 1;
            while (string.charAt(n5) == c & n5 < n2 - 1) {
                n5 -= n;
            }
            n4 = n5 + 1;
            if (string.charAt(n5) == '\"') {
                c = '\"';
                ++n5;
            }
            if (string.charAt(n5) == '\'') {
                c = '\'';
                ++n5;
            }
            while (string.charAt(n4) != c & n4 < n2 - 1) {
                n4 -= n;
            }
            string3 = string.substring(n5, n4);
            string2 = string2.trim();
            string2 = string2.toLowerCase();
            string3 = string3.trim();
            ((Hashtable) jBAppletProperties.getProps()).put(string2, string3);
        } while ((n3 = string.indexOf(61, n3 + 1)) != -1);
        return jBAppletProperties;
    }

    private void getAppletParameters(JBAppletProperties jBAppletProperties, String string) {
        int n = -1;
        int n2 = string.length();
        char c = ' ';
        String string2 = "";
        String string3 = "";
        int n3 = string.toUpperCase().indexOf("NAME");
        while (string.charAt(n3) != '=' & n3 < n2 - 1) {
            n3 -= n;
        }
        ++n3;
        while (string.charAt(n3) == ' ' & n3 < n2 - 1) {
            n3 -= n;
        }
        int n4 = n3 + 1;
        if (string.charAt(n3) == '\"') {
            c = '\"';
            ++n3;
        }
        if (string.charAt(n3) == '\'') {
            c = '\'';
            ++n3;
        }
        while (string.charAt(n4) != c & n4 < n2 - 1) {
            n4 -= n;
        }
        string3 = string.substring(n3, n4);
        c = ' ';
        n3 = string.toUpperCase().indexOf("VALUE");
        while (string.charAt(n3) != '=' & n3 < n2 - 1) {
            n3 -= n;
        }
        ++n3;
        while (string.charAt(n3) == ' ' & n3 < n2 - 1) {
            n3 -= n;
        }
        n4 = n3 + 1;
        if (string.charAt(n3) == '\"') {
            c = '\"';
            ++n3;
        }
        if (string.charAt(n3) == '\'') {
            c = '\'';
            ++n3;
        }
        while (string.charAt(n4) != c & n4 < n2 - 1) {
            n4 -= n;
        }
        string2 = string.substring(n3, n4);
        ((Hashtable) jBAppletProperties.getParameters()).put(string3, string2);
    }

    private String getTagAttribute(String string, String string2) {
        String string3 = "";
        try {
            boolean bl = false;
            int n = -1;
            int n2 = string.length();
            String string4 = "";
            int n3 = 0;
            char c = ' ';
            string2 = string2.toUpperCase();
            n3 = string.indexOf(61, n3 + 1);
            while (n3 != -1 & !bl) {
                c = ' ';
                int n4 = n3 - 1;
                while (string.charAt(n4) == c & n4 >= 0) {
                    n4 += n;
                }
                int n5 = n4 - 1;
                if (string.charAt(n5) == '\"') {
                    c = '\"';
                    --n5;
                }
                if (string.charAt(n5) == '\'') {
                    c = '\'';
                    --n5;
                }
                while (string.charAt(n5) != c & n5 >= 0) {
                    n5 += n;
                }
                string4 = string.substring(n5, n4 + 1);
                if (string2.equals((string4 = string4.trim()).toUpperCase())) {
                    bl = true;
                    c = ' ';
                    n5 = n3 + 1;
                    while (string.charAt(n5) == c & n5 < n2 - 1) {
                        n5 -= n;
                    }
                    n4 = n5 + 1;
                    if (string.charAt(n5) == '\"') {
                        c = '\"';
                        ++n5;
                    }
                    if (string.charAt(n5) == '\'') {
                        c = '\'';
                        ++n5;
                    }
                    while (string.charAt(n4) != c & n4 < n2 - 1) {
                        n4 -= n;
                    }
                    string3 = string.substring(n5, n4);
                    string3 = string3.trim();
                }
                n3 = string.indexOf(61, n3 + 1);
            }
        } catch (Exception exception) {
        }
        return string3;
    }

    public void kill() {
        isRunning = false;
        visited.clear();
        urlList.removeAllElements();
    }

    public void log(String string) {
        JBLogger.log(string);
    }

    public Vector parse() {
        URLConnection uRLConnection = null;
        try {
            Object object = "";
            URL uRL = new URL(this.startlink.url.toString());
            try {
                uRLConnection = uRL.openConnection();
                uRLConnection.setRequestProperty("User-Agent", "JavaBee");
                uRLConnection.setRequestProperty("Referer", "http://www.javabee.com");
                uRLConnection.setRequestProperty("Accept", "text/html");
                String string = String.valueOf(uRLConnection.getContentType()) + "";
                if (string.indexOf("htm") >= 0 || string.indexOf("unknown") >= 0) {
                    int n;
                    Object object2;
                    InputStream inputStream = uRLConnection.getInputStream();
                    uRL = uRLConnection.getURL();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    JBAppletProperties jBAppletProperties = new JBAppletProperties();
                    boolean bl = false;
                    String string2 = "";
                    String string3 = "";
                    String string4 = "";
                    object = "";
                    String string5 = "";
                    int n2 = uRL.getPort();
                    string3 = n2 == -1 ? "" : ":" + n2;
                    string5 = String.valueOf(uRL.getProtocol()) + "://" + uRL.getHost() + string3;
                    object = uRL.getFile();
                    if (((String) object).indexOf("http://") != 0) {
                        object = String.valueOf(string5) + (String) object;
                        string2 = string5;
                    } else {
                        object2 = new URL((String) object);
                        string2 = "http://" + ((URL) object2).getHost() + ((URL) object2).getPort();
                        object2 = null;
                    }
                    string4 = ((String) object).substring(((String) object).lastIndexOf("/"));
                    object = ((String) object).substring(0, ((String) object).lastIndexOf("/"));
                    if (((String) object).indexOf(".nsf") > 0) {
                        object = String.valueOf(uRL.getProtocol()) + "://" + uRL.getHost() + string3;
                    }
                    HTMLTokenizer hTMLTokenizer = new HTMLTokenizer(bufferedReader);
                    while ((n = hTMLTokenizer.nextHTML()) != HTMLTokenizer.HTML_EOF && isRunning) {
                        Hyperlink hyperlink;
                        boolean bl2;
                        URL uRL2;
                        if (n == HTMLTokenizer.TAG_BASE && ((String) (object2 = this.getTagAttribute(hTMLTokenizer.sval, "href"))).length() > 0) {
                            object = object2;
                        }
                        if (n == HTMLTokenizer.HTML_UNKNOWN) continue;
                        String string6 = hTMLTokenizer.sval;
                        DownloadView.addBytes(hTMLTokenizer.sval.length());
                        string6 = string6.trim();
                        string6 = this.remove(string6, '\n');
                        string6 = this.remove(string6, '\r');
                        string6 = string6.replace('\t', ' ');
                        string6 = string6.trim();
                        string6 = string6.concat(" ");
                        if (n == HTMLTokenizer.TAG_APPLET) {
                            string6.toUpperCase().indexOf("CODE");
                            jBAppletProperties = this.getAppletAttributes(string6);
                            ((Hashtable) jBAppletProperties.getProps()).put("documentbase", object);
                            ((Hashtable) jBAppletProperties.getProps()).put("host", string2);
                            ((Hashtable) jBAppletProperties.getProps()).put("webpage", string4);
                            bl = true;
                            ++appletsFound;
                        }
                        if (n == HTMLTokenizer.TAG_applet) {
                            this.appletList.addElement(jBAppletProperties);
                            bl = false;
                        } else if (n == HTMLTokenizer.TAG_PARAM & bl) {
                            this.getAppletParameters(jBAppletProperties, string6);
                        }
                        if (n == HTMLTokenizer.TAG_FRAME && (object2 = this.getTagAttribute(string6, "src")) != "") {
                            uRL2 = ((String) object2).startsWith("http") ? new URL((String) object2) : (((String) object2).startsWith("/") ? new URL(String.valueOf(string2) + (String) object2) : new URL(String.valueOf(object) + "/" + (String) object2));
                            if (visited.get(uRL2) == null) {
                                Hyperlink hyperlink2 = new Hyperlink();
                                hyperlink2.url = new URL(uRL2.toString());
                                hyperlink2.level = this.startlink.level;
                                urlList.addElement(hyperlink2);
                            }
                            visited.put(uRL2, new Boolean(false));
                        }
                        if (!(n == HTMLTokenizer.TAG_A & checkRefs) || (object2 = this.getTagAttribute(string6, "href")) == "" || ((String) object2).startsWith("mailto") || ((String) object2).endsWith("gif") || ((String) object2).endsWith("jpg"))
                            continue;
                        object2 = ((String) object2).replace('\"', ' ');
                        object2 = ((String) object2).replace('\\', ' ');
                        uRL2 = ((String) (object2 = ((String) object2).trim())).startsWith("http") ? new URL((String) object2) : (((String) object2).startsWith("/") ? new URL(String.valueOf(string2) + (String) object2) : new URL(String.valueOf(object) + "/" + (String) object2));
                        if (visited.get(uRL2) != null || noGoHash.get(uRL2.toString()) != null || uRL2.getHost().compareTo("jump.altavista.com") == 0 || uRL2.getHost().compareTo("www.doubleclick.net") == 0 || this.getThreadGroup().getName().compareTo("parsegroup" + ((JBSearchPanel) handle).getParsegroupNr()) != 0)
                            continue;
                        boolean bl3 = bl2 = localHost.compareTo(uRL2.getHost()) == 0;
                        if (this.startlink.local && this.startlink.level < localLevel && bl2) {
                            hyperlink = new Hyperlink();
                            hyperlink.url = new URL(uRL2.toString());
                            hyperlink.level = this.startlink.level + 1;
                            urlList.addElement(hyperlink);
                            visited.put(uRL2, new Boolean(false));
                            continue;
                        }
                        if (this.startlink.local && remoteCheck && !bl2) {
                            hyperlink = new Hyperlink();
                            hyperlink.url = new URL(uRL2.toString());
                            hyperlink.local = false;
                            hyperlink.level = 0;
                            urlList.addElement(hyperlink);
                            visited.put(uRL2, new Boolean(false));
                            continue;
                        }
                        if (this.startlink.local || this.startlink.level >= remoteLevel) continue;
                        hyperlink = new Hyperlink();
                        hyperlink.url = new URL(uRL2.toString());
                        hyperlink.local = false;
                        hyperlink.level = this.startlink.level + 1;
                        urlList.addElement(hyperlink);
                        visited.put(uRL2, new Boolean(false));
                    }
                    bufferedReader.close();
                    uRL = null;
                }
            } catch (Exception exception) {
                JBLogger.log("Couldn't open connection: " + exception + ", Url: " + uRL + ", docbase: " + (String) object);
            }
            if (this.appletList.size() > 0) {
                ((JBSearchPanel) handle).updateAppletList(this.appletList);
                this.appletList.removeAllElements();
            }
        } catch (Throwable throwable) {
            JBLogger.log("Ex: " + throwable);
        }
        return this.appletList;
    }

    private String remove(String string, char c) {
        StringBuffer stringBuffer = new StringBuffer();
        int n = 0;
        while (n < string.length()) {
            char c2 = string.charAt(n);
            if (c2 != c) {
                stringBuffer.append(c2);
            }
            ++n;
        }
        return stringBuffer.toString();
    }

    public void run() {
        try {
            this.parse();
            ++pagesChecked;
            if (isRunning) {
                ((JBSearchPanel) handle).updateStatus(pagesChecked, visited.size(), appletsFound, this.startlink.toString());
            }
            Thread.yield();
            Thread.sleep(1L);
            while (currThreads > 0 && !urlList.isEmpty() && isRunning && this.getThreadGroup().getName().compareTo("parsegroup" + ((JBSearchPanel) handle).getParsegroupNr()) == 0) {
                AppletTagParser appletTagParser = this;
                synchronized (appletTagParser) {
                    --currThreads;
                    this.startChildParser();
                    this.wait(1000L);
                }
            }
            ++currThreads;
        } catch (InterruptedException interruptedException) {
            JBLogger.log(interruptedException.toString());
            return;
        }
    }

    public void setHandle(Object object) {
        handle = object;
    }

    public void setNoGoList() {
        JBLogger.log("Setter noGoListen");
        noGoHash.put("http://www.altavista.com/av/content/about.htm", new Boolean(false));
        noGoHash.put("http://www.altavista.com/av/content/help.htm", new Boolean(false));
        noGoHash.put("http://www.altavista.com/av/content/questions.htm", new Boolean(false));
        noGoHash.put("http://www.doubleclick.net/advertisers/altavista/", new Boolean(false));
        noGoHash.put("http://www.altavista.com/cgi-bin/query?pg=addurl", new Boolean(false));
        noGoHash.put("http://www.altavista.com/av/content/disclaimer.htm", new Boolean(false));
        noGoHash.put("http://www.altavista.com/av/content/privacy.htm", new Boolean(false));
        noGoHash.put("http://image.altavista.com/AV_CopyrightPolicy.htm", new Boolean(false));
        noGoHash.put("http://www.altavista.com/av/content/av_network.html", new Boolean(false));
        noGoHash.put("http://www.altavista.com/cgi-bin/query?pg=tmpl&v=pref.html", new Boolean(false));
        noGoHash.put("http://www.altavista.com/cgi-bin/query", new Boolean(false));
        noGoHash.put("http://www.altavista.com/cgi-bin/query?text&pg=q&what=web", new Boolean(false));
        noGoHash.put("http://www.altavista.com/cgi-bin/query?pg=aq&what=web&text=yes", new Boolean(false));
        noGoHash.put("http://www.altavista.com/cgi-bin/query?text&pg=q&what=news", new Boolean(false));
        noGoHash.put("http://www.altavista.com/cgi-bin/query?pg=aq&what=news&text=yes", new Boolean(false));
        noGoHash.put("http://www.altavista.com/cgi-bin/query?pg=&text=yes", new Boolean(false));
        noGoHash.put("http://www.altavista.com/cgi-bin/query?pg=q&what=news&text=yes", new Boolean(false));
        noGoHash.put("http://www.altavista.com/", new Boolean(false));
        noGoHash.put("/av/content/about.htm", new Boolean(false));
        noGoHash.put("/av/content/help.htm", new Boolean(false));
        noGoHash.put("/av/content/questions.htm", new Boolean(false));
        noGoHash.put("/advertisers/altavista/", new Boolean(false));
        noGoHash.put("/cgi-bin/query?pg=addurl", new Boolean(false));
        noGoHash.put("/av/content/disclaimer.htm", new Boolean(false));
        noGoHash.put("/av/content/privacy.htm", new Boolean(false));
        noGoHash.put("/AV_CopyrightPolicy.htm", new Boolean(false));
        noGoHash.put("/av/content/av_network.html", new Boolean(false));
        noGoHash.put("/cgi-bin/query?pg=tmpl&v=pref.html", new Boolean(false));
        noGoHash.put("/cgi-bin/query", new Boolean(false));
        noGoHash.put("/cgi-bin/query?text&pg=q&what=web", new Boolean(false));
        noGoHash.put("/cgi-bin/query?pg=aq&what=web&text=yes", new Boolean(false));
        noGoHash.put("/cgi-bin/query?pg=&what=news&text=yes", new Boolean(false));
        noGoHash.put("/cgi-bin/query?text&pg=q&what=news", new Boolean(false));
        noGoHash.put("/cgi-bin/query?pg=aq&what=news&text=yes", new Boolean(false));
        noGoHash.put("/cgi-bin/query?pg=&text=yes", new Boolean(false));
    }

    public void setPrefs(boolean bl, boolean bl2, int n, int n2, URL uRL) {
        checkRefs = bl;
        remoteCheck = bl2;
        localLevel = n;
        remoteLevel = n2;
        firsturl = uRL;
        localHost = firsturl.getHost();
        pagesChecked = 0;
        appletsFound = 0;
        visited.clear();
        urlList.removeAllElements();
        isRunning = true;
        currThreads = maxThreads;
    }

    public void setStartURL(Hyperlink hyperlink) {
        this.startlink = hyperlink;
        visited.put(this.startlink.url, new Boolean(false));
    }

    public void startChildParser() {
        AppletTagParser appletTagParser = new AppletTagParser();
        Hyperlink hyperlink = (Hyperlink) urlList.elementAt(0);
        URL cfr_ignored_0 = hyperlink.url;
        urlList.removeElementAt(0);
        appletTagParser.setStartURL(hyperlink);
        appletTagParser.start();
    }
}

