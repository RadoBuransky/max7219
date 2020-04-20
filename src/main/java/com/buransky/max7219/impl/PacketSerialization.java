package com.buransky.max7219.impl;

public class PacketSerialization {
    private PacketSerialization() {
    }

    /**
     * packet[step][display]
     */
    public static byte[] serialize(final short[][] packets) {
        final byte[] result = new byte[packets.length*packets[0].length*16*3+2];
        result[0] = 0b100;
        int resultIndex = 0;
        // For each time step
        for (short[] step: packets) {
            // For each display
            for (short displayPacket: step) {
                resultIndex = packetToClkDin(displayPacket, result, resultIndex);
            }
        }
        result[resultIndex] = 0b100;

        return result;
    }

    /**
     * Result length = 16*3 = 48
     * CLK      = result & 0b010
     * DIN      = result & 0b001
     */
    private static int packetToClkDin(final short packet, final byte[] dest, final int destIndex) {
        short shiftedPacket = packet;
        for (int i = 0; i < 16; i++) {
            dest[destIndex + i*3] = (byte)(shiftedPacket & 1); // DIN is either high or low
            dest[destIndex + i*3 + 1] = 0b010; // CLK is high
            dest[destIndex + i*3 + 2] = 0b000; // CLK is low
            shiftedPacket >>>= 1;
        }
        return destIndex + 16*3;
    }
}
