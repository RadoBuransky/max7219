package com.buransky.max7219.impl;

import com.buransky.max7219.DrawingResult;
import com.buransky.max7219.LedMatrix;
import com.buransky.max7219.Max7219;

import static com.google.common.base.Preconditions.checkArgument;

public class Max7219Impl implements Max7219 {
    public static int MAX_DISPLAY_ROWS = 8;
    public static int MAX_DISPLAY_COLUMNS = 8;

    private final int displayRows;
    private final int displayColumns;
    private final int displayCount;

    public Max7219Impl(final int displayRows, final int displayColumns, final int displayCount) {
        checkArgument(displayRows > 0);
        checkArgument(displayRows <= MAX_DISPLAY_ROWS);
        checkArgument(displayColumns > 0);
        checkArgument(displayColumns <= MAX_DISPLAY_COLUMNS);
        checkArgument(displayCount > 0);
        this.displayColumns = displayColumns;
        this.displayRows = displayRows;
        this.displayCount = displayCount;
    }

    @Override
    public DrawingResult clearScreen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DrawingResult setIntensity(int intensity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DrawingResult drawBitmap(final LedMatrix ledMatrix, final LedMatrix previous) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LedMatrix createLedMatrix(final LedMatrix from) {
        if (from == null)
            return new LongLedMatrix(displayRows, displayColumns, displayCount);
        return new LongLedMatrix(displayRows, displayColumns, from.getDisplaysArray());
    }
}
