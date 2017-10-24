package com.tuniondata.jtserver.message;

import com.tuniondata.jtserver.utils.ByteUtils;
import com.tuniondata.jtserver.utils.CRC16CCITT;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * Created by Think on 2017/10/20.
 * 封装成JT809协议标准的数据报文
 * 头标识(1byte)+数据头(22byte)+数据体(nbyte,可空)+尾标识(1byte)
 */
public class MessagePack {
    public static ChannelBuffer buildMessage(Message msg){
        int bodyLength = 0 ;
        if(null != msg.getMsgBody()){
            bodyLength = msg.getMsgBody().readableBytes();
        }

        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(bodyLength + Message.MSG_FIX_LENGTH);
        ChannelBuffer headBuffer = ChannelBuffers.buffer(22);//---数据头
        headBuffer.writeInt(buffer.capacity());//4
        headBuffer.writeInt((int)msg.getMsgSn());//4
        headBuffer.writeShort((short)msg.getMsgId());//2
        headBuffer.writeInt(msg.getMsgGesscenterId());//4
        headBuffer.writeBytes(msg.getVersionFlag());//3
        headBuffer.writeByte(0);//1
        headBuffer.writeInt((int)msg.getEncryptKey());//4
        buffer.writeBytes(headBuffer);//---数据体
        if(null != msg.getMsgBody()){
            buffer.writeBytes(msg.getMsgBody());
        }
        ChannelBuffer finalBuffer = ChannelBuffers.copiedBuffer(buffer);
        //--crc校验码
        byte[] b = ChannelBuffers.buffer(finalBuffer.readableBytes()).array();
        finalBuffer.getBytes(0, b);
        int crcValue = CRC16CCITT.crc16(b);
        finalBuffer.writeShort((short)crcValue);//2
        // 转义
        byte[] bytes = ChannelBuffers.copiedBuffer(finalBuffer).array();
        ChannelBuffer headFormatedBuffer = ChannelBuffers.dynamicBuffer(finalBuffer.readableBytes());
        ByteUtils.formatBuffer(bytes, headFormatedBuffer);
        ChannelBuffer buffera = ChannelBuffers.buffer(headFormatedBuffer.readableBytes() + 2);
        buffera.writeByte(Message.MSG_HEAD);
        buffera.writeBytes(headFormatedBuffer);
        buffera.writeByte(Message.MSG_TALL);
        return ChannelBuffers.copiedBuffer(buffera);
    }
}
