/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class JBStreamTokenizer {
    public static final int TT_EOF = -1;
    public static final int TT_EOL = 10;
    public static final int TT_NUMBER = -2;
    public static final int TT_WORD = -3;
    public static final int TT_NOTHING = -4;
    private static final byte CT_WHITESPACE = 1;
    private static final byte CT_DIGIT = 2;
    private static final byte CT_ALPHA = 4;
    private static final byte CT_QUOTE = 8;
    private static final byte CT_COMMENT = 16;
    public int ttype = -4;
    public String sval;
    public double nval;
    private boolean inQoute = false;
    private Reader reader = null;
    private InputStream input = null;
    private char[] buf = new char[20];
    private int peekc;
    private boolean pushedBack;
    private boolean forceLower;
    private int LINENO = 1;
    private boolean eolIsSignificantP = false;
    private boolean slashSlashCommentsP = false;
    private boolean slashStarCommentsP = false;
    private byte[] ctype = new byte[256];

    private JBStreamTokenizer() {
        this.wordChars(97, 122);
        this.wordChars(65, 90);
        this.wordChars(160, 255);
        this.whitespaceChars(0, 32);
        this.commentChar(47);
        this.quoteChar(34);
        this.quoteChar(39);
    }

    public JBStreamTokenizer(InputStream inputStream) {
        this();
        this.input = inputStream;
    }

    public JBStreamTokenizer(Reader reader) {
        this();
        this.reader = reader;
    }

    public void commentChar(int n) {
        if (n >= 0 && n < this.ctype.length) {
            this.ctype[n] = 16;
        }
    }

    public void eolIsSignificant(boolean bl) {
        this.eolIsSignificantP = bl;
    }

    public int lineno() {
        return this.LINENO;
    }

    public void lowerCaseMode(boolean bl) {
        this.forceLower = bl;
    }

    public int nextToken() throws IOException {
        int n;
        if (this.pushedBack) {
            this.pushedBack = false;
            return this.ttype;
        }
        byte[] byArray = this.ctype;
        this.sval = null;
        if (this.ttype == -4) {
            n = this.read();
            if (n >= 0) {
                this.ttype = n;
            }
        } else {
            n = this.peekc;
        }
        if (n < 0) {
            this.ttype = -1;
            return -1;
        }
        int n2 = n < 256 ? byArray[n] : 4;
        while ((n2 & 1) != 0) {
            if (n == 13) {
                ++this.LINENO;
                n = this.read();
                if (n == 10) {
                    n = this.read();
                }
                if (this.eolIsSignificantP) {
                    this.peekc = n;
                    this.ttype = 10;
                    return 10;
                }
            } else {
                if (n == 10) {
                    ++this.LINENO;
                    if (this.eolIsSignificantP) {
                        this.peekc = this.read();
                        this.ttype = 10;
                        return 10;
                    }
                }
                n = this.read();
            }
            if (n < 0) {
                this.ttype = -1;
                return -1;
            }
            int n3 = n2 = n < 256 ? byArray[n] : 4;
        }
        if ((n2 & 2) != 0) {
            boolean bl = false;
            if (n == 45) {
                n = this.read();
                if (n != 46 && (n < 48 || n > 57)) {
                    this.peekc = n;
                    this.ttype = 45;
                    return 45;
                }
                bl = true;
            }
            double d = 0.0;
            int n4 = 0;
            int n5 = 0;
            while (true) {
                if (n == 46 && n5 == 0) {
                    n5 = 1;
                } else {
                    if (48 > n || n > 57) break;
                    d = d * 10.0 + (double) (n - 48);
                    n4 += n5;
                }
                n = this.read();
            }
            this.peekc = n;
            if (n4 != 0) {
                double d2 = 10.0;
                --n4;
                while (n4 > 0) {
                    d2 *= 10.0;
                    --n4;
                }
                d /= d2;
            }
            this.nval = bl ? -d : d;
            this.ttype = -2;
            return -2;
        }
        if ((n2 & 4) != 0) {
            int n6 = 0;
            do {
                if (n6 >= this.buf.length) {
                    char[] cArray = new char[this.buf.length * 2];
                    System.arraycopy(this.buf, 0, cArray, 0, this.buf.length);
                    this.buf = cArray;
                }
                this.buf[n6++] = (char) n;
                n = this.read();
                int n7 = n < 0 ? 1 : (n2 = n < 256 ? byArray[n] : 4);
                if (n != 34) continue;
                boolean bl = this.inQoute = !this.inQoute;
            } while ((n2 & 6) != 0 || this.inQoute && n != -1);
            this.peekc = n;
            this.sval = String.copyValueOf(this.buf, 0, n6);
            if (this.forceLower) {
                this.sval = this.sval.toLowerCase();
            }
            this.ttype = -3;
            return -3;
        }
        if ((n2 & 0x10) != 0) {
            while ((n = this.read()) != 10 && n != 13 && n >= 0) {
            }
            this.peekc = n;
            return this.nextToken();
        }
        if ((n2 & 8) != 0) {
            this.ttype = n;
            int n8 = 0;
            this.peekc = this.read();
            while (this.peekc >= 0 && this.peekc != this.ttype && this.peekc != 10 && this.peekc != 13) {
                if (this.peekc == 92) {
                    int n9 = n = this.read();
                    if (n >= 48 && n <= 55) {
                        n -= 48;
                        int n10 = this.read();
                        if (48 <= n10 && n10 <= 55) {
                            n = (n << 3) + (n10 - 48);
                            n10 = this.read();
                            if (48 <= n10 && n10 <= 55 && n9 <= 51) {
                                n = (n << 3) + (n10 - 48);
                                this.peekc = this.read();
                            } else {
                                this.peekc = n10;
                            }
                        } else {
                            this.peekc = n10;
                        }
                    } else {
                        switch (n) {
                            case 97: {
                                n = 7;
                                break;
                            }
                            case 98: {
                                n = 8;
                                break;
                            }
                            case 102: {
                                n = 12;
                                break;
                            }
                            case 110: {
                                n = 10;
                                break;
                            }
                            case 114: {
                                n = 13;
                                break;
                            }
                            case 116: {
                                n = 9;
                                break;
                            }
                            case 118: {
                                n = 11;
                            }
                        }
                        this.peekc = this.read();
                    }
                } else {
                    n = this.peekc;
                    this.peekc = this.read();
                }
                if (n8 >= this.buf.length) {
                    char[] cArray = new char[this.buf.length * 2];
                    System.arraycopy(this.buf, 0, cArray, 0, this.buf.length);
                    this.buf = cArray;
                }
                this.buf[n8++] = (char) n;
            }
            if (this.peekc == this.ttype) {
                this.peekc = this.read();
            }
            this.sval = String.copyValueOf(this.buf, 0, n8);
            return this.ttype;
        }
        if (n == 47 && (this.slashSlashCommentsP || this.slashStarCommentsP)) {
            n = this.read();
            if (n == 42 && this.slashStarCommentsP) {
                int n11 = 0;
                while ((n = this.read()) != 47 || n11 != 42) {
                    if (n == 13) {
                        ++this.LINENO;
                        n = this.read();
                        if (n == 10) {
                            n = this.read();
                        }
                    } else if (n == 10) {
                        ++this.LINENO;
                        n = this.read();
                    }
                    if (n < 0) {
                        this.ttype = -1;
                        return -1;
                    }
                    n11 = n;
                }
                this.peekc = this.read();
                return this.nextToken();
            }
            if (n == 47 && this.slashSlashCommentsP) {
                while ((n = this.read()) != 10 && n != 13 && n >= 0) {
                }
                this.peekc = n;
                return this.nextToken();
            }
            this.peekc = n;
            this.ttype = 47;
            return 47;
        }
        this.peekc = this.read();
        this.ttype = n;
        return this.ttype;
    }

    public void ordinaryChar(int n) {
        if (n >= 0 && n < this.ctype.length) {
            this.ctype[n] = 0;
        }
    }

    public void ordinaryChars(int n, int n2) {
        if (n < 0) {
            n = 0;
        }
        if (n2 >= this.ctype.length) {
            n2 = this.ctype.length - 1;
        }
        while (n <= n2) {
            this.ctype[n++] = 0;
        }
    }

    public void parseNumbers() {
        int n = 48;
        while (n <= 57) {
            int n2 = n++;
            this.ctype[n2] = (byte) (this.ctype[n2] | 2);
        }
        this.ctype[46] = (byte) (this.ctype[46] | 2);
        this.ctype[45] = (byte) (this.ctype[45] | 2);
    }

    public void pushBack() {
        if (this.ttype != -4) {
            this.pushedBack = true;
        }
    }

    public void quoteChar(int n) {
        if (n >= 0 && n < this.ctype.length) {
            this.ctype[n] = 8;
        }
    }

    private int read() throws IOException {
        if (this.reader != null) {
            return this.reader.read();
        }
        if (this.input != null) {
            return this.input.read();
        }
        throw new IllegalStateException();
    }

    public void resetSyntax() {
        int n = this.ctype.length;
        while (--n >= 0) {
            this.ctype[n] = 0;
        }
    }

    public void slashSlashComments(boolean bl) {
        this.slashSlashCommentsP = bl;
    }

    public void slashStarComments(boolean bl) {
        this.slashStarCommentsP = bl;
    }

    public String toString() {
        String string;
        switch (this.ttype) {
            case -1: {
                string = "EOF";
                break;
            }
            case 10: {
                string = "EOL";
                break;
            }
            case -3: {
                string = this.sval;
                break;
            }
            case -2: {
                string = "n=" + this.nval;
                break;
            }
            case -4: {
                string = "NOTHING";
                break;
            }
            default: {
                char[] cArray = new char[3];
                cArray[2] = 39;
                cArray[0] = 39;
                cArray[1] = (char) this.ttype;
                string = new String(cArray);
                break;
            }
        }
        return "Token[" + string + "], line " + this.LINENO;
    }

    public void whitespaceChars(int n, int n2) {
        if (n < 0) {
            n = 0;
        }
        if (n2 >= this.ctype.length) {
            n2 = this.ctype.length - 1;
        }
        while (n <= n2) {
            this.ctype[n++] = 1;
        }
    }

    public void wordChars(int n, int n2) {
        if (n < 0) {
            n = 0;
        }
        if (n2 >= this.ctype.length) {
            n2 = this.ctype.length - 1;
        }
        while (n <= n2) {
            int n3 = n++;
            this.ctype[n3] = (byte) (this.ctype[n3] | 4);
        }
    }
}

