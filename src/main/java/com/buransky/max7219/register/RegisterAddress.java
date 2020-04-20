package com.buransky.max7219.register;

/**
 * Table 2. Register Address Map
 */
public enum RegisterAddress {
    NoOp((byte)0x00),
    Digit0((byte)0x01),
    Digit1((byte)0x02),
    Digit2((byte)0x03),
    Digit3((byte)0x04),
    Digit4((byte)0x05),
    Digit5((byte)0x06),
    Digit6((byte)0x07),
    Digit7((byte)0x08),
    DecodeMode((byte)0x09),
    Intensity((byte)0x0A),
    ScanLimit((byte)0x0B),
    Shutdown((byte)0x0C),
    DisplayTest((byte)0x0F);

    private final byte address;

    RegisterAddress(final byte address) {
        this.address = address;
    }

    public byte getAddress() {
        return address;
    }
}