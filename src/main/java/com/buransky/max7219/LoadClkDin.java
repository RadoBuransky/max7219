package com.buransky.max7219;

/**
 * Triplet of LOAD/CS, CLK and DIN values to be executed. "true" means value should be set to high, "false" means
 * low.
 */
public interface LoadClkDin {
    /**
     * Returns LOAD (Load-Data Input) / CS (Chip-Select Input) value.
     */
    boolean getLoad();

    /**
     * Returns CLK (Serial-Clock Input) value.
     */
    boolean getClk();

    /**
     * Returns DIN (Serial-Data Input) value.
     */
    boolean getDin();
}