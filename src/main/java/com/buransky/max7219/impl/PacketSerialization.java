package com.buransky.max7219.impl;

import com.buransky.max7219.Max7219;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms list of 16-bit packets into linear sequence of LOAD/CS, CLK and DIN triplets.
 */
class PacketSerialization {
    private PacketSerialization() {
    }

    /**
     * Transforms list of 16-bit packets (one for each display) into bit state changes.
     * @param packets One 16-bit packet for each display.
     * @return Ordered sequence bit state changes.
     */
    static List<Max7219.BitChange> serialize(final List<Short> packets) {
        final ArrayList<Max7219.BitChange> result = new ArrayList<>(packets.size()*16*3+2);
        result.add(Max7219.BitChange.LOADCS_LOW);
        for (int i = packets.size() - 1; i >= 0; i--) {
            packetToClkDin(packets.get(i), result);
        }
        result.add(Max7219.BitChange.LOADCS_HIGH);
        return result;
    }

    /**
     * Transforms single 16-bit into bit state changes. Max result length = 16*3 = 48 bytes.
     */
    private static void packetToClkDin(final short packet, final ArrayList<Max7219.BitChange> dest) {
        short shiftedPacket = packet;
        short previousDin = -1;
        for (int i = 0; i < 16; i++) {
            final short din = (short)(shiftedPacket & 0x8000);
            if (din != previousDin) {
                dest.add((din != 0) ? Max7219.BitChange.DIN_HIGH : Max7219.BitChange.DIN_LOW);
                previousDin = din;
            }
            dest.add(Max7219.BitChange.CLK_HIGH);
            dest.add(Max7219.BitChange.CLK_LOW);
            shiftedPacket <<= 1;
        }
    }
}
