/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

class HTMLTokenizer
        extends JBStreamTokenizer {
    static int HTML_TEXT = -1;
    static int HTML_UNKNOWN = -2;
    static int HTML_EOF = -3;
    static int TAG_APPLET = 0;
    static int TAG_applet = 1;
    static int TAG_PARAM = 2;
    static int TAG_param = 3;
    static int TAG_FRAME = 4;
    static int TAG_frame = 5;
    static int TAG_ALIGN = 6;
    static int TAG_align = 7;
    static int TAG_A = 8;
    static int TAG_a = 9;
    static int TAG_BASE = 10;
    static int TAG_base = 11;
    String[] tags = new String[]{"APPLET", "PARAM", "FRAME", "ALIGN", "A", "BASE"};
    boolean outsideTag = true;

    public HTMLTokenizer(InputStream inputStream) {
        super(inputStream);
    }

    public HTMLTokenizer(Reader reader) {
        super(reader);
        this.resetSyntax();
        this.wordChars(0, 255);
        this.ordinaryChars(60, 60);
        this.ordinaryChars(62, 62);
        this.outsideTag = true;
    }

    public int nextHTML() throws IOException {
        int n = this.nextToken();
        switch (n) {
            case -1: {
                return HTML_EOF;
            }
            case 60: {
                this.outsideTag = false;
                return this.nextHTML();
            }
            case 62: {
                this.outsideTag = true;
                return this.nextHTML();
            }
            case -3: {
                if (!this.outsideTag) {
                    return this.tagType();
                }
                return this.nextHTML();
            }
        }
        JBLogger.log("ERROR: unknown TT " + n);
        return HTML_UNKNOWN;
    }

    protected boolean onlyWhiteSpace(String string) {
        int n = 0;
        while (n < string.length()) {
            char c = string.charAt(n);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                return false;
            }
            ++n;
        }
        return true;
    }

    protected int tagType() {
        boolean bl = false;
        int n = 0;
        String string = this.sval;
        if (string.charAt(0) == '/') {
            ++n;
            bl = true;
        }
        int n2 = 0;
        while (n2 < this.tags.length) {
            if (string.regionMatches(true, n, this.tags[n2], 0, this.tags[n2].length())) {
                int n3 = n2 * 2 + (bl ? 1 : 0);
                return n3;
            }
            ++n2;
        }
        return HTML_UNKNOWN;
    }
}

