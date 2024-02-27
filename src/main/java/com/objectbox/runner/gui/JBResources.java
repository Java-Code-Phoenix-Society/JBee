/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.gui;

import com.objectbox.runner.util.JBLogger;

import java.awt.*;
import java.io.*;

public final class JBResources {
    static Frame dummy = new Frame();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Image getImageResource(String string) {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(string);
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            if (inputStream == null) return null;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                while (true) {
                    byteArrayOutputStream.write(dataInputStream.readByte());
                }
            } catch (IOException iOException) {
                byte[] byArray = byteArrayOutputStream.toByteArray();
                ((OutputStream) byteArrayOutputStream).close();
                dataInputStream.close();
                Image image = Toolkit.getDefaultToolkit().createImage(byArray);
                MediaTracker mediaTracker = new MediaTracker(dummy);
                mediaTracker.addImage(image, 0);
                mediaTracker.waitForID(0);
                return image;
            }
        } catch (Throwable throwable) {
            JBLogger.log("Exception i getImageResourec: " + throwable);
        }
        return null;
    }
}

