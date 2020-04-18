package com.buransky.max7219;

/**
 * Bit matrix where each bit represents a LED in the matrix. Bit value "1" means the LED is on, "0" means off.
 */
public interface LedMatrix {
    /**
     * Retrieves LED status at the provided position.
     * @param row Row in the LED matrix.
     * @param column Column in the LED matrix.
     * @return `true` if the LED is on, false otherwise.
     */
    boolean getLedStatus(final int row, final int column);

    /**
     * Sets LED status at the provided position.
     * @param row Row in the LED matrix.
     * @param column Column in the LED matrix.
     * @param ledOn `true` if the LED should be turned on, false otherwise.
     */
    void setLedStatus(final int row, final int column, final boolean ledOn);

    /**
     * Low-level access to binary representation for performance reasons.
     */
    long[] getDisplaysArray();
}
