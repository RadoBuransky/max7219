package com.buransky.max7219.register;

import com.buransky.max7219.Register;

/**
 * Table 8. Scan-Limit Register Format (Address (Hex) = 0xXB)
 */
public enum ScanLimitRegister implements Register {
    Digit0((byte)0x00),
    Digits0to1((byte)0x01),
    Digits0to2((byte)0x02),
    Digits0to3((byte)0x03),
    Digits0to4((byte)0x04),
    Digits0to5((byte)0x05),
    Digits0to6((byte)0x06),
    Digits0to7((byte)0x07);

    private static final RegisterAddress registerAddress = RegisterAddress.ScanLimit;
    private final byte data;

    ScanLimitRegister(final byte data) {
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
