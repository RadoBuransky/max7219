package com.buransky.max7219.impl;

import com.buransky.max7219.LedMatrix;
import com.buransky.max7219.Register;
import com.buransky.max7219.register.DigitRegister;
import com.buransky.max7219.register.NoOpRegister;
import com.buransky.max7219.register.RegisterAddress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class FastLedMatrix implements LedMatrix {
    public static final long MAX_DISPLAY_ROWS = 8;
    public static final long MAX_DISPLAY_COLUMNS = 8;

    protected final long displayRows;
    protected final long displayColumns;
    protected final long displaysVertically;
    protected final long displaysHorizontally;
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
    public List<Byte> execute(final Register[] registers) {
        checkNotNull(registers);
        checkArgument(registers.length == displays.length);

        final ArrayList<Short> packets = new ArrayList<Short>(displays.length);
        for (int i = 0; i < displays.length; i++) {
            packets.add(i, registerToPacket(registers[i]));
        }

        return PacketSerialization.serialize(packets);
    }

    @Override
    public List<Byte> executeAll(final Register register) {
        checkNotNull(register);
        final Register[] registers = new Register[displays.length];
        Arrays.fill(registers, register);
        return execute(registers);
    }

    @Override
    public List<Byte> draw() {
        final ArrayList<Byte> result = new ArrayList<>();

        if (!anyChange) {
            return result;
        }

        final long rowMask = (0xFFL >>> (8L - displayColumns));
        final ArrayList<ArrayList<Register>> digitRegisters = new ArrayList<>(displays.length);
        int maxDigitRegistersSize = 0;
        for (int display = 0; display < displays.length; display++) {
            long displayData = displays[display];
            long displayDiffMask = displayData ^ previousDisplays[display];
            final ArrayList<Register> displayDigitRegisters = new ArrayList<>(8);
            digitRegisters.add(display, displayDigitRegisters);
            if (displayDiffMask != 0) {
                for (int row = 0; row < displayRows; row++) {
                    final RegisterAddress digitRegisterAddress = DigitRegister.DIGITS[row];
                    final long rowMaskDiff = displayDiffMask & rowMask;
                    if (rowMaskDiff != 0) {
                        final long rowDiff = displayData & rowMask;
                        final DigitRegister digitRegister = new DigitRegister(digitRegisterAddress, (byte)rowDiff);
                        displayDigitRegisters.add(digitRegister);
                    }
                    displayData >>>= displayColumns;
                    displayDiffMask >>>= displayColumns;
                }

                if (displayDigitRegisters.size() > maxDigitRegistersSize) {
                    maxDigitRegistersSize = displayDigitRegisters.size();
                }
            }
        }

        anyChange = false;
        System.arraycopy(displays, 0, previousDisplays, 0, displays.length);

        for (int step = 0; step < maxDigitRegistersSize; step++) {
            final Register[] stepRegisters = new Register[displays.length];
            for (int display = 0; display < displays.length; display++) {
                final ArrayList<Register> displayRegisters = digitRegisters.get(display);
                if (step < displayRegisters.size()) {
                    stepRegisters[display] = displayRegisters.get(step);
                } else {
                    stepRegisters[display] = NoOpRegister.INSTANCE;
                }
            }
            result.addAll(execute(stepRegisters));
        }

        return result;
    }

    @Override
    public boolean getLedStatus(final int row, final int column) {
        final int displayIndex = (int)getDisplayIndex(column);
        final int bitPosition = (int)getBitPosition(row, column);
        return getBit(displays[displayIndex], bitPosition);
    }

    @Override
    public void setLedStatus(final int row, final int column, final boolean ledOn) {
        final int displayIndex = (int)getDisplayIndex(column);
        final int bitPosition = (int)getBitPosition(row, column);
        displays[displayIndex] = setBit(displays[displayIndex], bitPosition, ledOn);
        anyChange = true;
    }

    private short registerToPacket(final Register register) {
        return (short)(((register.getAddress() & 0x0F) << 8) | (register.getData()));
    }

    private long getDisplayIndex(final long column) {
        return column / displayColumns;
    }

    private long getBitPosition(final long row, final long column) {
        return column + (row * displayColumns);
    }

    private boolean getBit(final long number, final long position) {
        return ((number >> position) & 1L) == 1L;
    }

    private long setBit(final long number, final long position, final boolean value) {
        if (value) {
            return number | (1L << position);
        }
        return number & (~(1L << position));
    }
}
