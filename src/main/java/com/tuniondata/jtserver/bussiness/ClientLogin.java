package com.tuniondata.jtserver.bussiness;


import com.tuniondata.jtserver.master.IDataProcess;
import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.message.MessagePack;
import com.tuniondata.jtserver.utils.JT809Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.springframework.stereotype.Component;

/**
 * Created by Think on 2017/10/23.
 * 客户端连接请求处理
 */
@Component("clientLogin")
public class ClientLogin implements IDataProcess {

    private int userID;         //用户ID
    private String password;    //用户密码
    private String downLinkIp;  //下级平台提供的从链路服务端IP地址
    private int downLinkPort;   //下级平台提供的从链路服务端口号

    @Override
    public ChannelBuffer process(Message msg) {

        ChannelBuffer resBuffer = null;
        ChannelBuffer channelBuffer = msg.getMsgBody();

        try {
            userID = (int)channelBuffer.readUnsignedInt();
            password = new String(channelBuffer.readBytes(8).array());
            downLinkIp = new String(channelBuffer.readBytes(32).array());
            downLinkPort = channelBuffer.readUnsignedShort();

            //校验用户名及密码，通过后返回成功，否则失败

            Message resMsg = new Message(JT809Constants.UP_CONNECT_RSP);

            resBuffer = ChannelBuffers.buffer(5);
            resBuffer.writeByte(JT809Constants.UP_CONNECT_RSP_SUCCESS);
            resBuffer.writeInt(JT809Constants.UP_CONNECT_RSP_SUCCESS);
            resMsg.setMsgBody(resBuffer);

            resBuffer = MessagePack.buildMessage(resMsg);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resBuffer;
    }
}
