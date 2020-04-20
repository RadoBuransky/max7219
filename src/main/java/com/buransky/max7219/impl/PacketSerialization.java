package com.buransky.max7219.impl;

import java.util.ArrayList;
import java.util.List;

public class PacketSerialization {
    private PacketSerialization() {
    }

    /**
     * packet[step][display]
     */
    public static List<Byte> serialize(final short[] packets) {
        final ArrayList<Byte> result = new ArrayList<Byte>(packets.length*16*3+2);
        result.add(0, (byte)0b100);
        int resultIndex = 1;
        // For each display
        for (short displayPacket: packets) {
            resultIndex = packetToClkDin(displayPacket, result, resultIndex);
        }
        result.add(resultIndex, (byte)0b100);

        return result;
    }

    /**
     * Result length = 16*3 = 48
     * CLK      = result & 0b010
     * DIN      = result & 0b001
     */
    private static int packetToClkDin(final short packet, final ArrayList<Byte> dest, final int destIndex) {
        short shiftedPacket = packet;
        for (int i = 0; i < 16; i++) {
            dest.add(destIndex + i*3, (byte)(shiftedPacket & 1)); // DIN is either high or low
            dest.add(destIndex + i*3 + 1, (byte)0b010); // CLK is high
            dest.add(destIndex + i*3 + 2, (byte)0b000); // CLK is low
            shiftedPacket >>>= 1;
        }
        return destIndex + 16*3;
    }
}
