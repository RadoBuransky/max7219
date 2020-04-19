package com.buransky.max7219;

public interface SevenSegments extends Max7219 {
    void setSegmentStatus(final int segment, final int digit, final int display, final boolean segmentOn);
    void setDigitValue(final int digit, final int display, final int value);
    void setDisplayValue(final int display, final int value);
}
