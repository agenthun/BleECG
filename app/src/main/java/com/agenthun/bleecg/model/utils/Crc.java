package com.agenthun.bleecg.model.utils;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by agenthun on 16/3/11.
 */
public class Crc {
    private static final int[] CRC16Table = {
            0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50A5, 0x60C6, 0x70E7,
            0x8108, 0x9129, 0xA14A, 0xB16B, 0xC18C, 0xD1AD, 0xE1CE, 0xF1EF
    };

    public Crc() {
    }

    public static short getCRC16(byte[] data, int len) {
        int val = 0xffff;
        for (int i = 0; i < len; i++) {
            byte tmp = (byte) (val >> 12);
            val = (val << 4) & 0xffff;
            val ^= CRC16Table[tmp ^ ((data[i] & 0xff) >> 4)];

            tmp = (byte) (val >> 12);
            val = (val << 4) & 0xffff;
            val ^= CRC16Table[tmp ^ (data[i] & 0x0f)];
        }
        return (short) (val & 0xffff);
    }

    public static short getCRC16(short data1, short data2) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putShort(data1);
        buffer.putShort(data2);
        return getCRC16(buffer.array(), 4);
    }

    public static int simpleSumCRC(byte[] data, int len) {
        int simpleSum = 0;
        for (int i = 1; i < len; i++) {
            simpleSum += data[i];
        }
        simpleSum &= 0x00ff;
        return simpleSum;
    }

    public static int simpleSumReverseCRC(byte[] data) {
        int simpleSum = 0;
        for (int i = 0; i < data.length; i++) {
            simpleSum += data[i];
        }
        simpleSum &= 0x00ff;
        simpleSum = ~simpleSum & 0x00ff;
        return simpleSum;
    }

    public static int simpleSumReverseCRC(byte[] data, int len) {
        int simpleSum = 0;
        for (int i = 3; i < len - 1; i++) {
            simpleSum += data[i];
        }
        simpleSum &= 0x00ff;
        simpleSum = ~simpleSum & 0x00ff;
        return simpleSum;
    }

    public static int simpleSumReverseCRC(List<Byte> byteList, int len) {
        int simpleSum = 0;
        if (len > byteList.size()) return -1;
        for (int i = 3; i < len - 1; i++) {
            simpleSum += byteList.get(i);
        }
        simpleSum &= 0x00ff;
        simpleSum = ~simpleSum & 0x00ff;
        return simpleSum;
    }
}
