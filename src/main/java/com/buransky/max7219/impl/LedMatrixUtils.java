package com.buransky.max7219.impl;

public class LedMatrixUtils {
    private LedMatrixUtils() {}

    public static int getDisplayIndex(final int column, final int displayColumns) {
        return column / displayColumns;
    }

    public static int getBitPosition(final int row, final int column, final int displayColumns) {
        return column + (row * displayColumns);
    }

    public static int getRow(final int bitPosition, final int displayColumns) {
        return bitPosition / displayColumns;
    }

    public static int getColumn(final int bitPosition, final int displayColumns) {
        return bitPosition % displayColumns;
    }

    public static boolean getBit(final long number, int position) {
        return ((number >> position) & 1) == 1;
    }

    public static long setBit(final long number, int position, boolean value) {
        if (value) {
            return position | (1 << position);
        }
        return number & (~(1 << position));
    }
}
