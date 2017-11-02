package com.tuniondata.jtserver;

import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.utils.ByteUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Think on 2017/10/19.
 */
public class Decoder extends FrameDecoder {
    private static Logger LOG = LoggerFactory.getLogger(Decoder.class);

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        String logStr = ByteUtils.convertChannelBuffer(buffer);
        LOG.debug("receive data:"+logStr);

        //对接收的报文先进行转义，然后再解析
        ChannelBuffer msgBuf = ByteUtils.reformatBuffer(buffer);

//        logStr = ByteUtils.convertChannelBuffer(msgBuf);
//        LOG.debug("format data:"+logStr);

        int head = msgBuf.getByte(0);
        int tail = msgBuf.getByte(msgBuf.capacity() - 1);
        if( !(head == Message.MSG_HEAD )){
            return null;
        }
        msgBuf.skipBytes(1);//头标识MSG_HEAD
        Message msg = this.buildMessage(msgBuf);

        return msg;
    }

    private Message buildMessage(ChannelBuffer buffer){
        Message msg = new Message();
        try {
            msg.setMsgLength(buffer.readUnsignedInt());//4byte 数据长度
            msg.setMsgSn(buffer.readUnsignedInt());//4byte报文序列号
            msg.setMsgId(buffer.readUnsignedShort());//2byte业务数据类型
            msg.setMsgGesscenterId(buffer.readUnsignedInt());//4byte下级平台接入码
            msg.setVersionFlag(buffer.readBytes(msg.getVersionFlag().length).array());//3byte协议版本号
            msg.setEncryptFlag(buffer.readByte());//1byte报文加密标识
            msg.setEncryptKey(buffer.readUnsignedInt());//4byte数据加密密钥

//            LOG.debug("消息长度："+msg.getMsgLength()+";read"+buffer.readableBytes());
            int tailLen = 2; //两位校验码+一位尾标识
            if(buffer.readableBytes() >tailLen)
            {
                ChannelBuffer bodyBytes = buffer.readBytes(buffer.readableBytes() - tailLen);
                msg.setMsgBody(bodyBytes);
            }
            msg.setCrcCode(buffer.readUnsignedShort());//2byte 校验码
            //buffer.skipBytes(1);//尾标识MSG_TALL

        }catch (Exception e)
        {
            LOG.error(e.getMessage());
        }
        return msg;
    }

}
