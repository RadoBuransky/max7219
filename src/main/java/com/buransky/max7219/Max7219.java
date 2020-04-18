package com.buransky.max7219;

import com.buransky.max7219.impl.Max7219Impl;

/**
 * MAX7219 library API.
 *
 * Can be used to control 8-digit 7-segment LED displays or 8x8 LED matrix displays. Cascading of unlimited number of
 * displays is supported as well. I decided to use 8x8 matrix vocabulary so if you're using this  library to control a
 * 7-segment display then please note that "column" = "segment", "row" = "digit". Individual cascaded chips are referred
 * to as "displays". This API expects that display are connected linearly forming a horizontal line where number of rows
 * is "displayRows" number provided to the constructor and number of columns is "displayColumns"*"displayCount".
 * Consumer of this library is expected to execute returned pin commands using any library - typically Pi4j or WiringPi.
 */
public interface Max7219 {
    /**
     * Creates new instance implementing the Max7219 interface.
     * @param displayRows Number of rows of a single display in the LED matrix. Typically 8.
     * @param displayColumns Number of columns of a single display in the LED matrix. Typically 8.
     * @param displayCount Number of all cascaded MAX7219 chips connected together. Typically 1 - 4.
     */
    static Max7219 create(final int displayRows, final int displayColumns, final int displayCount) {
        return new Max7219Impl(displayRows, displayColumns, displayCount);
    }

    /**
     * Clears all displays.
     * @return New context which must be used to make the next drawing call.
     */
    DrawingResult clearScreen();

    /**
     * Sets new intensity level for all displays. Legal values are from 0 (lowest) to 15 (highest).
     * @param intensity New intensity level.
     * @return New context which must be used to make the next drawing call.
     */
    DrawingResult setIntensity(final int intensity);

    /**
     * Draws provided LED matrix.
     * @param ledMatrix Bitmap to paint.
     * @param previous Previous LED matrix to compute difference from or `null`.
     * @return New context which must be used to make the next drawing call.
     */
    DrawingResult drawBitmap(final LedMatrix ledMatrix, final LedMatrix previous);

    /**
     * Creates new LED matrix.
     * @param from Initial state of the LED matrix or `null` to initialize it with 0s.
     */
    LedMatrix createLedMatrix(final LedMatrix from);
}