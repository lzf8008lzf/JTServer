package com.tuniondata.jtserver.utils;

/**
 * Created by Think on 2017/10/19.
 */
public class CRC16CCITT {

    private static long M1 = 7623974;
    private static long IA1 = 65487498;
    private static long IC1 = 75879254;

    public static byte[] encrypt(long key, byte[] buffer, int size) {
        int idx = 0;
        if (key == 0) {
            key = 1;
        }
        String lr = "";
        while (idx < size) {
            key = IA1 * (key % M1) + IC1;
            key &= 0xFFFFFFFFL;

            buffer[idx++] ^= (byte) ((key >> 20) & 0xFF);
            lr += String.valueOf(buffer[idx - 1] + ",");

        }
        System.out.println(lr);
        return buffer;
    }

    public static int crc16(byte[] bytes){
        int crc = 0xFFFF;
        for (int j = 0; j < bytes.length ; j++) {
            crc = ((crc  >>> 8) | (crc  << 8) )& 0xffff;
            crc ^= (bytes[j] & 0xff);//byte to int, trunc sign
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return crc;
    }

    public static int toLeft8(byte inByte) {
        return inByte << 8 & 0xFF00;
    }

    public static int toLeft16(byte inByte) {
        return inByte << 16 & 0xFF0000;
    }

    public static int toLeft24(byte inByte) {
        return inByte << 24 & 0xFF000000;
    }

    public static int toLeft32(byte inByte) {
        return inByte << 32 & 0xFF000000;
    }

    public static long toLeft40(byte inByte) {
        return (inByte & 0xFF) << 40;
    }

    public static long toLeft48(byte inByte) {
        return (inByte & 0xFF) << 48;
    }

    public static long toLeft56(byte inByte) {
        return (inByte & 0xFF) << 56;
    }

    public static int andOperation(byte inByte){
        return (inByte & 0xFF);
    }
}
