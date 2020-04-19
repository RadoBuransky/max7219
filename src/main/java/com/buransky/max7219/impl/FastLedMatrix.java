package com.buransky.max7219.impl;

import com.buransky.max7219.LedMatrix;

import static com.buransky.max7219.impl.LedMatrixUtils.*;
import static com.google.common.base.Preconditions.checkArgument;

public class FastLedMatrix implements LedMatrix {
    public static int MAX_DISPLAY_ROWS = 8;
    public static int MAX_DISPLAY_COLUMNS = 8;

    protected final int displayRows;
    protected final int displayColumns;
    protected final int displaysVertically;
    protected final int displaysHorizontally;
    protected final long[] displays;

    public FastLedMatrix(final int displayRows,
                         final int displayColumns,
                         final int displaysVertically,
                         final int displaysHorizontally) {
        this(displayRows, displayColumns, displaysVertically, displaysHorizontally,
                new long[displaysVertically*displaysHorizontally]);
        checkArgument(displaysVertically > 0);
        checkArgument(displaysHorizontally > 0);
    }

    public FastLedMatrix(final int displayRows,
                         final int displayColumns,
                         final int displaysVertically,
                         final int displaysHorizontally,
                         final long[] displays) {
        checkArgument(displayRows > 0);
        checkArgument(displayRows <= MAX_DISPLAY_ROWS);
        checkArgument(displayColumns > 0);
        checkArgument(displayColumns <= MAX_DISPLAY_COLUMNS);
        this.displayColumns = displayColumns;
        this.displayRows = displayRows;
        this.displaysVertically = displaysVertically;
        this.displaysHorizontally = displaysHorizontally;
        this.displays = displays;
    }

    @Override
    public byte[] clearScreen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] setIntensity(int intensity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] draw() {
        throw new UnsupportedOperationException();
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
}
