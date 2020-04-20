package com.buransky.max7219.register;

import com.buransky.max7219.Register;

/**
 * Table 4. Decode-Mode Register Examples (Address (Hex) = 0xX9)
 */
public enum DecodeModeRegister implements Register {
    NoDecode((byte)0x00),
    CodeBDecodeFor0((byte)0x01),
    CodeBDecodeFor3to0((byte)0x0F),
    CodeBDecodeFor7to0((byte)0xFF);

    private static final RegisterAddress registerAddress = RegisterAddress.DecodeMode;
    private final byte data;

    DecodeModeRegister(final byte data) {
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
