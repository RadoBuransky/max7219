package com.buransky.max7219.impl;

import com.buransky.max7219.Register;
import com.buransky.max7219.SevenSegments;

/**
 * Adapter forwarding calls to fast LED matrix implementation. It's only about semantics.
 */
public class SevenSegmentsAdapter implements SevenSegments {
    private final FastLedMatrix ledMatrix;

    public SevenSegmentsAdapter(final int digitSegments, final int displayDigits, final int displayCount) {
        ledMatrix = new FastLedMatrix(displayDigits, digitSegments, 1, displayCount);
    }

    @Override
    public byte[] execute(final Register[] register) {
        return ledMatrix.execute(register);
    }

    @Override
    public byte[] executeAll(Register register) {
        return new byte[0];
    }

    @Override
    public byte[] draw() {
        return ledMatrix.draw();
    }

    @Override
    public void setSegmentStatus(int segment, int digit, int display, boolean segmentOn) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void setDigitValue(int digit, int display, int value) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void setDisplayValue(int display, int value) {
        throw new UnsupportedOperationException("TODO");
    }
}