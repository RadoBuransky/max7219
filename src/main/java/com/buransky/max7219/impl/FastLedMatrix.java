package com.buransky.max7219.impl;

import com.buransky.max7219.LedMatrix;
import com.buransky.max7219.Register;
import com.buransky.max7219.register.DigitRegister;
import com.buransky.max7219.register.RegisterAddress;

import java.util.ArrayList;
import java.util.Arrays;

import static com.buransky.max7219.impl.LedMatrixUtils.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class FastLedMatrix implements LedMatrix {
    public static final int MAX_DISPLAY_ROWS = 8;
    public static final int MAX_DISPLAY_COLUMNS = 8;

    protected final int displayRows;
    protected final int displayColumns;
    protected final int displaysVertically;
    protected final int displaysHorizontally;
    protected final long[] displays;
    protected final long[] previousDisplays;
    protected boolean anyChange;

    public FastLedMatrix(final int displayRows,
                         final int displayColumns,
                         final int displaysVertically,
                         final int displaysHorizontally) {
        this(displayRows, displayColumns, displaysVertically, displaysHorizontally,
                new long[displaysVertically*displaysHorizontally]);
        checkArgument(displaysVertically > 0);
        checkArgument(displaysHorizontally > 0);
    }

    public FastLedMatrix(final int displayRows,
                         final int displayColumns,
                         final int displaysVertically,
                         final int displaysHorizontally,
                         final long[] displays) {
        checkArgument(displayRows > 0);
        checkArgument(displayRows <= MAX_DISPLAY_ROWS);
        checkArgument(displayColumns > 0);
        checkArgument(displayColumns <= MAX_DISPLAY_COLUMNS);
        this.displayColumns = displayColumns;
        this.displayRows = displayRows;
        this.displaysVertically = displaysVertically;
        this.displaysHorizontally = displaysHorizontally;
        this.displays = displays;
        this.previousDisplays = displays.clone();
        this.anyChange = false;
    }

    @Override
    public byte[] execute(final Register[] registers) {
        checkNotNull(registers);
        checkArgument(registers.length == displays.length);

        final short[][] packets = new short[displays.length][1];
        for (int i = 0; i < displays.length; i++) {
            packets[i][0] = registerToPacket(registers[i]);
        }

        return PacketSerialization.serialize(packets);
    }

    @Override
    public byte[] executeAll(final Register register) {
        checkNotNull(register);
        final Register[] registers = new Register[displays.length];
        Arrays.fill(registers, register);
        return execute(registers);
    }

    @Override
    public byte[] draw() {
        if (!anyChange) {
            return null;
        }

        final byte rowMask = (byte)(0xFF >>> (8 - displayColumns));
        final ArrayList<DigitRegister> digitRegisters = new ArrayList<>(128);
        for (int display = 0; display < displays.length; display++) {
            long displayData = displays[display];
            long displayDiffMask = displayData ^ previousDisplays[display];
            if (displayDiffMask > 0) {
                for (int row = 0; row < displayRows; row++) {
                    final RegisterAddress digitRegisterAddress = DigitRegister.DIGITS[row];
                    final byte rowMaskDiff = (byte) (displayDiffMask & rowMask);
                    if (rowMaskDiff > 0) {
                        final byte rowDiff = (byte) (displayData & rowMask);
                        final DigitRegister digitRegister = new DigitRegister(digitRegisterAddress, rowDiff);
                        digitRegisters.add(digitRegister);
                    }
                    displayData >>>= displayColumns;
                    displayDiffMask >>>= displayColumns;
                }
            }
        }

        anyChange = false;
        System.arraycopy(displays, 0, previousDisplays, 0, displays.length);
        return execute((DigitRegister[])digitRegisters.toArray());
    }

    @Override
    public boolean getLedStatus(final int row, final int column) {
        final int displayIndex = getDisplayIndex(column, displayColumns);
        final int bitPosition = getBitPosition(row, column, displayColumns);
        return getBit(displays[displayIndex], bitPosition);
    }

    @Override
    public void setLedStatus(final int row, final int column, final boolean ledOn) {
        final int displayIndex = getDisplayIndex(column, displayColumns);
        final int bitPosition = getBitPosition(row, column, displayColumns);
        displays[displayIndex] = setBit(displays[displayIndex], bitPosition, ledOn);
        anyChange = true;
    }

    private short registerToPacket(final Register register) {
        return (short)(((register.getAddress() & 0x0F) << 8) | (register.getData()));
    }
}
