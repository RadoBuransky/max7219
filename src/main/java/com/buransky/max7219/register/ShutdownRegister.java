package com.buransky.max7219.register;

import com.buransky.max7219.Register;

/**
 * Table 3. Shutdown Register Format (Address (Hex) = 0xXC)
 */
public enum ShutdownRegister implements Register {
    ShutdownMode((byte)0x00),
    NormalOperation((byte)0x01);

    private static final RegisterAddress registerAddress = RegisterAddress.Shutdown;
    private final byte data;

    ShutdownRegister(final byte data) {
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
