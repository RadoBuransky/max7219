package com.buransky.max7219.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms list of 16-bit packets into linear sequence of LOAD/CS, CLK and DIN triplets.
 */
class PacketSerialization {
    private PacketSerialization() {
    }

    /**
     * Transforms list of 16-bit packets (one for each display) into ordered sequence of LOAD/CS, CLK and DIN triplets.
     * @param packets One 16-bit packet for each display.
     * @return Ordered sequence of 3-bit triplets. Bit0 is LOAD/CS value, Bit1 is CLK and Bit2 is DIN.
     */
    static List<Byte> serialize(final List<Short> packets) {
        final int resultSize = packets.size()*16*3+2;
        final ArrayList<Byte> result = new ArrayList<Byte>(resultSize);
        while(result.size() < resultSize) result.add((byte)0);

        result.set(0, (byte)0b000); // LOAD/CS = low, CLK = low, DIN = low
        int resultIndex = 1;
        for (short displayPacket: packets) {
            resultIndex = packetToClkDin(displayPacket, result, resultIndex);
        }
        result.set(resultIndex, (byte)0b100); // LOAD/CS = high, CLK = low, DIN = low
        return result;
    }

    /**
     * Transforms single 16-bit into ordered sequence of LOAD/CS, CLK and DIN triplets. Result length = 16*3 = 48 bytes.
     */
    private static int packetToClkDin(final short packet, final ArrayList<Byte> dest, final int destIndex) {
        short shiftedPacket = packet;
        for (int i = 15; i >= 0; i--) {
            dest.set(destIndex + i*3, (byte)(shiftedPacket & 1)); // DIN is either high or low
            dest.set(destIndex + i*3 + 1, (byte)0b010); // CLK = high
            dest.set(destIndex + i*3 + 2, (byte)0b000); // CLK = low
            shiftedPacket >>>= 1;
        }
        return destIndex + 16*3;
    }
}
