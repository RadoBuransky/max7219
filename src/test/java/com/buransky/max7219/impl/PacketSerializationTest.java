package com.buransky.max7219.impl;

import com.buransky.max7219.Max7219.BitChange;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.buransky.max7219.Max7219.BitChange.CLK_HIGH;
import static com.buransky.max7219.Max7219.BitChange.CLK_LOW;
import static org.junit.Assert.*;

public class PacketSerializationTest {
    private static final int DIN_BITS = 16;

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        PacketSerialization.serialize(null);
    }

    @Test
    public void testSingleDisplay() {
        // Execute
        final List<BitChange> result = PacketSerialization.serialize(Collections.singletonList((short)0x060B));

        // Assert
        final List<Short> din = assertCommon(result, 1);
        assertEquals(1, din.size());
        assertEquals((short)0x060B, (short)din.get(0));
    }

    @Test
    public void testMultipleDisplays() {
        final List<Short> packets = Arrays.asList((short)0x0000, (short)0x060B, (short)0x0F01, (short)0x0C00);

        // Execute
        final List<BitChange> result = PacketSerialization.serialize(packets);

        // Assert
        final List<Short> din = assertCommon(result, 4);
        assertEquals(4, din.size());
        assertEquals((short)0x0000, (short)din.get(0)); // 0000 0000 0000 0000
        assertEquals((short)0x060B, (short)din.get(1)); // 0000 0110 0000 1011‬
        assertEquals((short)0x0F01, (short)din.get(2)); // 0000 1111 0000 0001‬
        assertEquals((short)0x0C00, (short)din.get(3)); // 0000 1100 0000 0000
    }

    private List<Short> assertCommon(final List<BitChange> result, final int displayCount) {
        assertTrue(1 + displayCount*DIN_BITS*3 + 1 >= result.size());
        assertStartAndEnd(result);
        return assertData(result, displayCount);
    }

    private void assertStartAndEnd(final List<BitChange> result) {
        assertEquals(BitChange.LOADCS_LOW, result.get(0));
        assertEquals(BitChange.LOADCS_HIGH, result.get(result.size() - 1));
    }

    private List<Short> assertData(final List<BitChange> result, final int displayCount) {
        final ArrayList<Short> dins = new ArrayList<>();
        int resultIndex = 1;
        for (int display = 0; display < displayCount; display++) {
            short din = 0;
            short dinBit = -1;
            for (int i = 0; i < DIN_BITS; i++) {
                final BitChange value0 = result.get(resultIndex++);
                switch (value0) {
                    case DIN_HIGH:
                        dinBit = 1;
                        break;
                    case DIN_LOW:
                        dinBit = 0;
                        break;
                    case CLK_HIGH:
                        resultIndex--;
                        break;
                    default :
                        fail("Unexpected bit value! [" + value0 + "]");
                        break;
                }
                assertEquals(CLK_HIGH, result.get(resultIndex++));
                assertEquals(CLK_LOW, result.get(resultIndex++));
                assertTrue("DIN must be either 0 or 1 [" + dinBit + "]", dinBit == 0 || dinBit == 1);

                din <<= 1;
                din |= dinBit;
            }
            dins.add(din);
        }
        return dins;
    }
}
