package com.buransky.max7219.register;

import com.buransky.max7219.Register;

/**
 * Table 10. Display-Test Register Format (Address (Hex) = 0xXF)
 */
public enum DisplayTestRegister implements Register {
    NormalOperation((byte)0x00),
    DisplayTestMode((byte)0x01);

    private static final RegisterAddress registerAddress = RegisterAddress.DisplayTest;
    private final byte data;

    DisplayTestRegister(final byte data) {
        this.data = data;
    }

    @Override
    public byte getAddress() {
        return registerAddress.getAddress();
    }

    public byte getData() {
        return data;
    }
}