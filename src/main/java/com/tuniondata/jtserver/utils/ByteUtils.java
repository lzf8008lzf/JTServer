package com.tuniondata.jtserver.utils;

import org.jboss.netty.buffer.ChannelBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import java.text.NumberFormat;

/**
 * Created by Think on 2017/10/19.
 */
public class ByteUtils {
    /** * 16进制字符串转换成byte数组 * byte[] * @param hex */
    public static byte[] hexStringToByte(String hex) {
        hex = hex.toUpperCase();
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2; result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /** * 补全位数不够的定长参数 有些定长参数，实际值长度不够，在后面补0x00 * byte[]
     * * @param length
     * * @param pwdByte *
     * @return */
    public static byte[] getBytesWithLengthAfter(int length, byte[] pwdByte){
        byte[] lengthByte = new byte[length];
        for(int i = 0; i < pwdByte.length; i ++){
            lengthByte[i] = pwdByte[i];
        }
        for (int i = 0; i < (length- pwdByte.length); i++) {
            lengthByte[pwdByte.length + i] = 0x00;
        }return lengthByte;
    }

    /** * 发送报文转义 * void *
     * @param bytes *
     * @param formatBuffer
     * */
    public static void formatBuffer(
        byte[] bytes, ChannelBuffer formatBuffer){
        for (byte b : bytes) {
            switch(b){
                case 0x5b:
                    byte[] formatByte0x5b = new byte[2];
                    formatByte0x5b[0] = 0x5a;
                    formatByte0x5b[1] = 0x01;
                    formatBuffer.writeBytes(formatByte0x5b);
                    break;
                case 0x5a:
                    byte[] formatByte0x5a = new byte[2];
                    formatByte0x5a[0] = 0x5a;
                    formatByte0x5a[1] = 0x02;
                    formatBuffer.writeBytes(formatByte0x5a);
                    break;
                case 0x5d:
                    byte[] formatByte0x5d = new byte[2];
                    formatByte0x5d[0] = 0x5e;
                    formatByte0x5d[1] = 0x01;
                    formatBuffer.writeBytes(formatByte0x5d);
                    break;
                case 0x5e:
                    byte[] formatByte0x5e = new byte[2];
                    formatByte0x5e[0] = 0x5e;
                    formatByte0x5e[1] = 0x02;
                    formatBuffer.writeBytes(formatByte0x5e);
                    break;
                default:
                    formatBuffer.writeByte(b);
                    break;
            }
        }
    }

    /** * 接收报文转义
     * @param ChannelBuffer
     * @return ChannelBuffer
     * */
    public static ChannelBuffer reformatBuffer(ChannelBuffer buffer){
        ChannelBuffer formatBuffer = ChannelBuffers.dynamicBuffer(buffer.capacity());
        try {

            byte[] bytes = buffer.readBytes(buffer.capacity()).array();
            for (int i = 0; i < bytes.length; i++) {
                byte inByte = bytes[i];
                if (0x5A == inByte) {
                    byte inByte2 = bytes[i + 1];
                    if (0x01 == inByte2) {
                        inByte = 0x5B;
                        i++;
                    } else if (0x02 == inByte2) {
                        inByte = 0x5A;
                        i++;
                    }
                } else if (0x5E == inByte) {
                    byte inByte2 = bytes[i + 1];
                    if (0x01 == inByte2) {
                        inByte = 0x5D;
                        i++;
                    } else if (0x02 == inByte2) {
                        inByte = 0x5E;
                        i++;
                    }
                }

                formatBuffer.writeByte(inByte);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //必须处理，否则后续会出现解析异常
        ChannelBuffer retBuffer = formatBuffer.readBytes(formatBuffer.readableBytes());

        return retBuffer;
    }

    /*
    ChannelBuffer转换位16进制字符串返回
     */
    public static String convertChannelBuffer(ChannelBuffer cb)
    {
        String retStr = "";
        try {
            ChannelBuffer temp = cb.duplicate();
            for(int i=0;i<temp.capacity();i++)
            {
                retStr = retStr+String.format("%02X", temp.readByte());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retStr;
    }

    /** * 格式化经纬度,保留六位小数 * int *
     * @param needFormat *
     * @return */
    private int formatLonLat(Double needFormat) {
        NumberFormat numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(6);
        numFormat.setGroupingUsed(false);
        String fristFromat = numFormat.format(needFormat);
        Double formatedDouble = Double.parseDouble(fristFromat);
        numFormat.setMaximumFractionDigits(0);
        String formatedValue = numFormat.format(formatedDouble * 1000000);
        return Integer.parseInt(formatedValue);
    }
}
