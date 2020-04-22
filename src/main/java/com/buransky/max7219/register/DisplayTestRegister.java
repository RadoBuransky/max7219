package com.buransky.max7219.register;

import com.buransky.max7219.Register;

/**
 * Table 10. Display-Test Register Format (Address (Hex) = 0xXF)
 */
public enum DisplayTestRegister implements Register {
    NormalOperation((short)0x00),
    DisplayTestMode((short)0x01);

    private static final RegisterAddress registerAddress = RegisterAddress.DisplayTest;
    private final short data;

    DisplayTestRegister(final short data) {
        this.data = data;
    }

    @Override
    public short getAddress() {
        return registerAddress.getAddress();
    }

    public short getData() {
        return data;
    }
}