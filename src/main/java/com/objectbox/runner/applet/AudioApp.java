/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  sun.audio.AudioData
 *  sun.audio.AudioDataStream
 *  sun.audio.AudioPlayer
 *  sun.audio.AudioStream
 *  sun.audio.ContinuousAudioDataStream
 */
package com.objectbox.runner.applet;

import com.objectbox.runner.beans.DownloadView;
import com.objectbox.runner.gui.JBee;
import com.objectbox.runner.util.JBLogger;

import java.applet.AudioClip;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public class AudioApp
        implements AudioClip {
    private AudioData audiodata;
    private AudioDataStream audiostream;
    private ContinuousAudioDataStream continuousaudiostream;
    private String cachename = "";

    public AudioApp() {
    }

    public AudioApp(String string) throws IOException {
        byte[] byArray;
        InputStream inputStream;
        String string2;
        File file;
        URL uRL = new URL(string);
        ByteArrayOutputStream byteArrayOutputStream = null;
        DataInputStream dataInputStream = null;
        String string3 = uRL.toString();
        String string4 = string3.hashCode() + "_" + string3.substring(string3.lastIndexOf("/") + 1, string3.length());
        String string5 = JBee.getPreference("javabee_home");
        if (!string5.endsWith(System.getProperty("file.separator"))) {
            string5 = String.valueOf(string5) + System.getProperty("file.separator");
        }
        if ((file = new File(string2 = String.valueOf(string5) + "cache", string4)).exists()) {
            JBLogger.log("Using cached sound");
            inputStream = new FileInputStream(file);
            byArray = new byte[(int) file.length()];
            inputStream.read(byArray);
            inputStream.close();
            JBLogger.log("Using cached sound file.");
        } else {
            dataInputStream = new DataInputStream(uRL.openConnection().getInputStream());
            byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                try {
                    byteArrayOutputStream.write(dataInputStream.readByte());
                    DownloadView.addBytes(1L);
                } catch (IOException iOException) {
                    byArray = byteArrayOutputStream.toByteArray();
                    ((OutputStream) byteArrayOutputStream).close();
                    dataInputStream.close();
                    break;
                }
            }
        }
        inputStream = new ByteArrayInputStream(byArray);
        AudioStream audioStream = new AudioStream(inputStream);
        this.audiodata = audioStream.getData();
        audioStream.close();
        this.cachename = file.toString();
        if (!file.exists()) {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byArray);
            fileOutputStream.close();
        }
        this.audiostream = null;
        this.continuousaudiostream = null;
    }

    public static void main(String[] stringArray) throws Exception {
        AudioApp audioApp = new AudioApp("http://box1/lyd/boing.au");
        audioApp.play();
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException interruptedException) {
        }
    }

    public void finalize() {
        try {
            try {
                if (this.audiostream != null) {
                    this.audiostream.close();
                }
                if (this.continuousaudiostream != null) {
                    this.continuousaudiostream.close();
                }
            } catch (Throwable throwable) {
            }
        } catch (Throwable throwable) {
            Object var1_2 = null;
            this.audiostream = null;
            this.continuousaudiostream = null;
            JBLogger.log("AudioApp " + this.toString() + " finalized");
            throw throwable;
        }
        Object var1_3 = null;
        this.audiostream = null;
        this.continuousaudiostream = null;
        JBLogger.log("AudioApp " + this.toString() + " finalized");
    }

    public String getCacheName() {
        return this.cachename;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void loadAudioFromResource(String string) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = null;
            DataInputStream dataInputStream = null;
            dataInputStream = new DataInputStream(this.getClass().getResourceAsStream(string));
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                while (true) {
                    byteArrayOutputStream.write(dataInputStream.readByte());
                    DownloadView.addBytes(1L);
                }
            } catch (IOException iOException) {
                byte[] byArray = byteArrayOutputStream.toByteArray();
                ((OutputStream) byteArrayOutputStream).close();
                dataInputStream.close();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
                AudioStream audioStream = new AudioStream((InputStream) byteArrayInputStream);
                this.audiodata = audioStream.getData();
                audioStream.close();
                this.audiostream = null;
                this.continuousaudiostream = null;
                return;
            }
        } catch (Exception exception) {
            JBLogger.log("Ex in loadAudiofromRes: " + exception);
        }
    }

    public void loop() {
        if (this.continuousaudiostream == null) {
            this.continuousaudiostream = new ContinuousAudioDataStream(this.audiodata);
        }
        AudioPlayer.player.start((InputStream) this.continuousaudiostream);
    }

    public void play() {
        this.audiostream = new AudioDataStream(this.audiodata);
        AudioPlayer.player.start((InputStream) this.audiostream);
    }

    public void stop() {
        if (this.audiostream != null) {
            AudioPlayer.player.stop((InputStream) this.audiostream);
        }
        if (this.continuousaudiostream != null) {
            AudioPlayer.player.stop((InputStream) this.continuousaudiostream);
        }
    }
}

