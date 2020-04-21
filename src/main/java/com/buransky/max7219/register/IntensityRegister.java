package com.buransky.max7219.register;

import com.buransky.max7219.Register;

/**
 * Table 7. Intensity Register Format (Address (Hex) = 0xXA)
 */
public class IntensityRegister implements Register {
    private static final RegisterAddress registerAddress = RegisterAddress.Intensity;
    private final byte data;

    public IntensityRegister(final byte data) {
        this.data = data;
    }

    @Override
    public byte getAddress() {
        return registerAddress.getAddress();
    }

    @Override
    public byte getData() {
        return data;
    }
}
