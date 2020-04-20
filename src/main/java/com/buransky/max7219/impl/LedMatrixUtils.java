package com.buransky.max7219.impl;

public class LedMatrixUtils {
    private LedMatrixUtils() {}

    public static long getDisplayIndex(final long column, final long displayColumns) {
        return column / displayColumns;
    }

    public static long getBitPosition(final long row, final long column, final long displayColumns) {
        return column + (row * displayColumns);
    }

    public static long getRow(final long bitPosition, final long displayColumns) {
        return bitPosition / displayColumns;
    }

    public static long getColumn(final long bitPosition, final long displayColumns) {
        return bitPosition % displayColumns;
    }

    public static boolean getBit(final long number, final long position) {
        return ((number >> position) & 1L) == 1L;
    }

    public static long setBit(final long number, final long position, final boolean value) {
        if (value) {
            return number | (1L << position);
        }
        return number & (~(1L << position));
    }
}
