package com.buransky.max7219;

/**
 * Table 1. Serial-Data Format (16 Bits). D15 to D12 are "don't care" bits.
 */
public interface Register {
    /**
     * 4 bits - from D11 down to D8.
     */
    byte getAddress();

    /**
     * 8 bits - from D7 (MSG) down to D0 (LSB)
     */
    byte getData();
}
