package com.buransky.max7219.impl;

import com.buransky.max7219.LedMatrix;

import static com.buransky.max7219.impl.LedMatrixUtils.*;

/**
 * Unsafe implementation to maximize performance. No argument checking, no thread safety.
 */
public class LongLedMatrix implements LedMatrix {
    protected final int displayRows;
    protected final int displayColumns;
    protected final long[] displays;

    public LongLedMatrix(final int displayRows, final int displayColumns, final int displayCount) {
        this(displayRows, displayColumns, new long[displayCount]);
    }

    public LongLedMatrix(final int displayRows, final int displayColumns, final long[] displays) {
        this.displayColumns = displayColumns;
        this.displayRows = displayRows;
        this.displays = displays;
    }

    @Override
    public boolean getLedStatus(final int row, final int column) {
        final int displayIndex = getDisplayIndex(column, displayColumns);
        final int bitPosition = getBitPosition(row, column, displayColumns);
        return getBit(displays[displayIndex], bitPosition);
    }

    @Override
    public void setLedStatus(final int row, final int column, final boolean ledOn) {
        final int displayIndex = getDisplayIndex(column, displayColumns);
        final int bitPosition = getBitPosition(row, column, displayColumns);
        displays[displayIndex] = setBit(displays[displayIndex], bitPosition, ledOn);
    }

    @Override
    public long[] getDisplaysArray() {
        return displays;
    }
}
