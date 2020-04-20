package com.buransky.max7219.register;

import com.buransky.max7219.Register;

public class NoOpRegister implements Register {
    public static final NoOpRegister INSTANCE = new NoOpRegister();
    private static final RegisterAddress registerAddress = RegisterAddress.NoOp;

    private NoOpRegister() {
    }

    @Override
    public byte getAddress() {
        return registerAddress.getAddress();
    }

    @Override
    public byte getData() {
        return 0;
    }
}