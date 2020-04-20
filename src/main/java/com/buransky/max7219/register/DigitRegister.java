package com.buransky.max7219.register;

import com.buransky.max7219.Register;

import static com.buransky.max7219.register.RegisterAddress.*;
import static com.google.common.base.Preconditions.checkArgument;

public class DigitRegister implements Register {
    public static final RegisterAddress[] DIGITS = { Digit0, Digit1, Digit2, Digit3, Digit4, Digit5, Digit6, Digit7 };
    private final RegisterAddress registerAddress;
    private final byte data;

    public DigitRegister(final RegisterAddress registerAddress, final byte data) {
        checkArgument(registerAddress.getAddress() >= Digit0.getAddress());
        checkArgument(registerAddress.getAddress() <= Digit7.getAddress());
        this.registerAddress = registerAddress;
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