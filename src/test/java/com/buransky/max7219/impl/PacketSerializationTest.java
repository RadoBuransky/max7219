package com.buransky.max7219.impl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PacketSerializationTest {
    private static final byte LOADCS_MASK = 0b100;
    private static final byte CLK_MASK = 0b010;
    private static final byte DIN_MASK = 0b001;
    private static final int DIN_BITS = 16;

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        PacketSerialization.serialize(null);
    }

    @Test
    public void testSingleDisplay() {
        // Execute
        final List<Byte> result = PacketSerialization.serialize(Collections.singletonList((short)0xAAAA));

        // Assert
        final List<Short> din = assertCommon(result, 1);
        assertEquals(1, din.size());
        assertEquals((short)0xAAAA, (short)din.get(0));
    }

    @Test
    public void testMultipleDisplays() {
        final List<Short> packets = Arrays.asList((short)0x0000, (short)0x060B, (short)0xFF01, (short)0x0C00);

        // Execute
        final List<Byte> result = PacketSerialization.serialize(packets);

        // Assert
        final List<Short> din = assertCommon(result, 4);
        assertEquals(4, din.size());
        assertEquals((short)0x0000, (short)din.get(0));
        assertEquals((short)0x060B, (short)din.get(1));
        assertEquals((short)0xFF01, (short)din.get(2));
        assertEquals((short)0x0C00, (short)din.get(3));
    }

    private List<Short> assertCommon(final List<Byte> result, final int displayCount) {
        assertEquals(1 + displayCount*DIN_BITS*3 + 1, result.size()); // Start + data + end
        assertStartAndEnd(result);
        return assertData(result, displayCount);
    }

    private void assertStartAndEnd(final List<Byte> result) {
        assertEquals((byte)0b000, (byte)result.get(0));
        assertEquals((byte)0b100, (byte)result.get(result.size() - 1));
    }

    private List<Short> assertData(final List<Byte> result, final int displayCount) {
        final ArrayList<Short> dins = new ArrayList<>();
        for (int display = 0; display < displayCount; display++) {
            short din = 0;
            for (int i = 0; i < DIN_BITS; i++) {
                final int baseIndex = 1 + (display*DIN_BITS + i)*3;
                final byte value0 = result.get(baseIndex);
                final byte value1 = result.get(baseIndex + 1);
                final byte value2 = result.get(baseIndex + 2);
                assertEquals(0b000, value0 & LOADCS_MASK);
                assertEquals(0b000, value0 & CLK_MASK);
                assertEquals(0b000, value1 & LOADCS_MASK);
                assertEquals(0b010, value1 & CLK_MASK);
                assertEquals(0b000, value2 & LOADCS_MASK);
                assertEquals(0b000, value2 & CLK_MASK);

                din <<= 1;
                din |= value0 & DIN_MASK;
            }
            dins.add(din);
        }
        return dins;
    }
}
