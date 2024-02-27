/*
 * Decompiled with CFR 0.152.
 */
package com.sun.java.swing;

import java.io.Serializable;

public class SizeRequirements
        implements Serializable {
    public int minimum;
    public int preferred;
    public int maximum;
    public float alignment;

    public SizeRequirements() {
        this.minimum = 0;
        this.preferred = 0;
        this.maximum = 0;
        this.alignment = 0.5f;
    }

    public SizeRequirements(int min, int pref, int max, float a) {
        this.minimum = min;
        this.preferred = pref;
        this.maximum = max;
        this.alignment = a > 1.0f ? 1.0f : (a < 0.0f ? 0.0f : a);
    }

    public static void calculateAlignedPositions(int allocated, SizeRequirements total, SizeRequirements[] children, int[] offsets, int[] spans) {
        int totalAscent = (int) ((float) allocated * total.alignment);
        int totalDescent = allocated - totalAscent;
        int i = 0;
        while (i < children.length) {
            SizeRequirements req = children[i];
            int maxAscent = (int) ((float) req.maximum * req.alignment);
            int maxDescent = req.maximum - maxAscent;
            int ascent = Math.min(totalAscent, maxAscent);
            int descent = Math.min(totalDescent, maxDescent);
            offsets[i] = totalAscent - ascent;
            spans[i] = (int) Math.min((long) ascent + (long) descent, Integer.MAX_VALUE);
            ++i;
        }
    }

    public static void calculateTiledPositions(int allocated, SizeRequirements total, SizeRequirements[] children, int[] offsets, int[] spans) {
        if (allocated > total.preferred) {
            SizeRequirements.expandedTile(allocated, total, children, offsets, spans);
        } else {
            SizeRequirements.compressedTile(allocated, total, children, offsets, spans);
        }
    }

    private static void compressedTile(int allocated, SizeRequirements total, SizeRequirements[] request, int[] offsets, int[] spans) {
        int totalPlay = Math.min(total.preferred - allocated, total.preferred - total.minimum);
        float factor = total.preferred - total.minimum == 0 ? 0.0f : (float) totalPlay / (float) (total.preferred - total.minimum);
        int totalOffset = 0;
        int i = 0;
        while (i < spans.length) {
            offsets[i] = totalOffset;
            SizeRequirements req = request[i];
            int play = (int) (factor * (float) (req.preferred - req.minimum));
            spans[i] = req.preferred - play;
            totalOffset = (int) Math.min((long) totalOffset + (long) spans[i], Integer.MAX_VALUE);
            ++i;
        }
    }

    private static void expandedTile(int allocated, SizeRequirements total, SizeRequirements[] request, int[] offsets, int[] spans) {
        int totalPlay = Math.min(allocated - total.preferred, total.maximum - total.preferred);
        float factor = total.maximum - total.preferred == 0 ? 0.0f : (float) totalPlay / (float) (total.maximum - total.preferred);
        int totalOffset = 0;
        int i = 0;
        while (i < spans.length) {
            offsets[i] = totalOffset;
            SizeRequirements req = request[i];
            int play = (int) (factor * (float) (req.maximum - req.preferred));
            spans[i] = (int) Math.min((long) req.preferred + (long) play, Integer.MAX_VALUE);
            totalOffset = (int) Math.min((long) totalOffset + (long) spans[i], Integer.MAX_VALUE);
            ++i;
        }
    }

    public static SizeRequirements getAlignedSizeRequirements(SizeRequirements[] children) {
        SizeRequirements totalAscent = new SizeRequirements();
        SizeRequirements totalDescent = new SizeRequirements();
        int i = 0;
        while (i < children.length) {
            SizeRequirements req = children[i];
            int ascent = (int) (req.alignment * (float) req.minimum);
            int descent = req.minimum - ascent;
            totalAscent.minimum = Math.max(ascent, totalAscent.minimum);
            totalDescent.minimum = Math.max(descent, totalDescent.minimum);
            ascent = (int) (req.alignment * (float) req.preferred);
            descent = req.preferred - ascent;
            totalAscent.preferred = Math.max(ascent, totalAscent.preferred);
            totalDescent.preferred = Math.max(descent, totalDescent.preferred);
            ascent = (int) (req.alignment * (float) req.maximum);
            descent = req.maximum - ascent;
            totalAscent.maximum = Math.max(ascent, totalAscent.maximum);
            totalDescent.maximum = Math.max(descent, totalDescent.maximum);
            ++i;
        }
        int min = (int) Math.min((long) totalAscent.minimum + (long) totalDescent.minimum, Integer.MAX_VALUE);
        int pref = (int) Math.min((long) totalAscent.preferred + (long) totalDescent.preferred, Integer.MAX_VALUE);
        int max = (int) Math.min((long) totalAscent.maximum + (long) totalDescent.maximum, Integer.MAX_VALUE);
        float alignment = 0.0f;
        if (min > 0) {
            alignment = (float) totalAscent.minimum / (float) min;
            alignment = alignment > 1.0f ? 1.0f : (alignment < 0.0f ? 0.0f : alignment);
        }
        return new SizeRequirements(min, pref, max, alignment);
    }

    public static SizeRequirements getTiledSizeRequirements(SizeRequirements[] children) {
        SizeRequirements total = new SizeRequirements();
        int i = 0;
        while (i < children.length) {
            SizeRequirements req = children[i];
            total.minimum = (int) Math.min((long) total.minimum + (long) req.minimum, Integer.MAX_VALUE);
            total.preferred = (int) Math.min((long) total.preferred + (long) req.preferred, Integer.MAX_VALUE);
            total.maximum = (int) Math.min((long) total.maximum + (long) req.maximum, Integer.MAX_VALUE);
            ++i;
        }
        return total;
    }

    public String toString() {
        return "[" + this.minimum + "," + this.preferred + "," + this.maximum + "]@" + this.alignment;
    }
}

