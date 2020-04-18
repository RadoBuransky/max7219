package com.buransky.max7219;

/**
 * Result containing commands to be executed.
 */
public interface DrawingResult {
    /**
     * Returns ordered sequence of commands to be executed.
     */
    Iterable<LoadClkDin> getLoadClkDins();
}
