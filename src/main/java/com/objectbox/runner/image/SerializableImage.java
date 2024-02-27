/*
 * Decompiled with CFR 0.152.
 */
package com.objectbox.runner.image;

import com.objectbox.runner.image.ImageNotFoundException;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

public class SerializableImage
        extends Image
        implements Serializable {
    private transient Image image;
    private byte[] bytes;
    private String location;
    private boolean isLocationURL;

    public SerializableImage(byte[] byArray) {
        this.bytes = byArray;
        this.getImageFromBytes();
    }

    public SerializableImage(Image image) {
        this.image = image;
        SerializableImage.loadAllBits(image);
    }

    public SerializableImage(InputStream inputStream) {
        this.isLocationURL = false;
        try {
            this.bytes = new byte[inputStream.available()];
            inputStream.read(this.bytes);
            this.image = Toolkit.getDefaultToolkit().createImage(this.bytes);
            SerializableImage.loadAllBits(this.image);
        } catch (Exception exception) {
            if (inputStream == null) {
                throw new ImageNotFoundException("InputStream for image is null");
            }
            exception.printStackTrace();
        }
    }

    public SerializableImage(String string) {
        this(string, false);
    }

    public SerializableImage(String string, boolean bl) {
        this.isLocationURL = false;
        this.location = string;
        this.getImageFromFilesystem();
        if (bl) {
            this.getImageBytesFromFilesystem();
        }
    }

    public SerializableImage(URL uRL) {
        this(uRL, false);
    }

    public SerializableImage(URL uRL, boolean bl) {
        this.isLocationURL = true;
        try {
            this.location = uRL.toExternalForm();
        } catch (NullPointerException nullPointerException) {
            throw new ImageNotFoundException("URL for image is null");
        }
        this.getImageFromURL();
        if (bl) {
            this.getImageBytesFromURL();
        }
    }

    public static final boolean loadAllBits(Image image) {
        MediaTracker mediaTracker = new MediaTracker(new Canvas());
        mediaTracker.addImage(image, 0);
        try {
            mediaTracker.waitForID(0);
        } catch (InterruptedException interruptedException) {
        }
        return !mediaTracker.isErrorID(0);
    }

    public void flush() {
        this.image.flush();
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    final byte[] getBytesFromStream(InputStream inputStream) throws IOException {
        int n;
        Vector<Byte> vector = new Vector<Byte>();
        int n2 = 0;
        while ((n = inputStream.read()) != -1) {
            ++n2;
            vector.addElement(new Byte((byte) n));
        }
        byte[] byArray = new byte[n2];
        int n3 = 0;
        Enumeration enumeration = vector.elements();
        while (enumeration.hasMoreElements()) {
            byArray[n3++] = (Byte) enumeration.nextElement();
        }
        return byArray;
    }

    public Graphics getGraphics() {
        return this.image.getGraphics();
    }

    public int getHeight(ImageObserver imageObserver) {
        return this.image.getHeight(imageObserver);
    }

    public Image getImage() {
        return this.image;
    }

    final void getImageBytesFromFilesystem() {
        try {
            this.bytes = this.getBytesFromStream(new FileInputStream(this.location));
        } catch (IOException iOException) {
            throw new ImageNotFoundException(iOException, "Error loading bytes from file stream");
        }
    }

    final void getImageBytesFromURL() {
        try {
            URL uRL = new URL(this.location);
            this.bytes = this.getBytesFromStream(uRL.openStream());
        } catch (Exception exception) {
            throw new ImageNotFoundException(exception, "Error loading bytes from URL stream");
        }
    }

    final void getImageFromBytes() {
        this.image = Toolkit.getDefaultToolkit().createImage(this.bytes);
        SerializableImage.loadAllBits(this.image);
    }

    final void getImageFromFilesystem() throws ImageNotFoundException {
        this.image = Toolkit.getDefaultToolkit().getImage(this.location);
        if (!SerializableImage.loadAllBits(this.image)) {
            throw new ImageNotFoundException("File " + this.location + " was not found.");
        }
    }

    final void getImageFromURL() throws ImageNotFoundException {
        try {
            URL uRL = new URL(this.location);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            this.image = toolkit.getImage(uRL);
        } catch (Exception exception) {
            throw new ImageNotFoundException(exception, "URL" + this.location + " was not found.");
        }
        if (!SerializableImage.loadAllBits(this.image)) {
            throw new ImageNotFoundException("URL" + this.location + " was not found.");
        }
    }

    public String getLocation() {
        return this.location;
    }

    public Object getProperty(String string, ImageObserver imageObserver) {
        return this.image.getProperty(string, imageObserver);
    }

    public ImageProducer getSource() {
        return this.image.getSource();
    }

    public int getWidth(ImageObserver imageObserver) {
        return this.image.getWidth(imageObserver);
    }

    public boolean isLocationURL() {
        return this.isLocationURL;
    }

    public SerializableImage[] parse(int n) {
        SerializableImage[] serializableImageArray = new SerializableImage[n];
        int n2 = this.image.getWidth(null) / n;
        int n3 = this.image.getHeight(null);
        int n4 = 0;
        while (n4 < n) {
            CropImageFilter cropImageFilter = new CropImageFilter(n4 * n2, 0, n2, n3);
            serializableImageArray[n4] = new SerializableImage(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(this.image.getSource(), cropImageFilter)));
            SerializableImage.loadAllBits(serializableImageArray[n4].getImage());
            ++n4;
        }
        return serializableImageArray;
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        if (this.bytes != null) {
            this.getImageFromBytes();
        } else if (this.location != null) {
            if (this.isLocationURL()) {
                this.getImageFromURL();
            } else {
                this.getImageFromFilesystem();
            }
        } else {
            int n = objectInputStream.readInt();
            int n2 = objectInputStream.readInt();
            int[] nArray = (int[]) objectInputStream.readObject();
            MemoryImageSource memoryImageSource = new MemoryImageSource(n, n2, nArray, 0, n);
            Canvas canvas = new Canvas();
            this.image = canvas.createImage(memoryImageSource);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (this.bytes == null && this.location == null) {
            int n = this.image.getWidth(null);
            int n2 = this.image.getHeight(null);
            objectOutputStream.writeInt(n);
            objectOutputStream.writeInt(n2);
            int[] nArray = new int[n * n2];
            PixelGrabber pixelGrabber = new PixelGrabber(this.image, 0, 0, n, n2, nArray, 0, n);
            try {
                pixelGrabber.grabPixels();
            } catch (InterruptedException interruptedException) {
            }
            objectOutputStream.writeObject(nArray);
        }
    }
}

