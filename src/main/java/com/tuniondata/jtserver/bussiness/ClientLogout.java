package com.tuniondata.jtserver.bussiness;

import com.tuniondata.jtserver.master.IDataProcess;
import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.message.MessagePack;
import com.tuniondata.jtserver.utils.JT809Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.springframework.stereotype.Component;

/**
 * Created by Think on 2017/10/23.
 * 客户端注销请求处理
 */
@Component("clientLogout")
public class ClientLogout implements IDataProcess {

    private int userID;         //用户ID
    private String password;    //用户密码

    @Override
    public ChannelBuffer process(Message msg) {

        ChannelBuffer resBuffer = null;
        ChannelBuffer channelBuffer = msg.getMsgBody();

        try {
            userID = (int)channelBuffer.readUnsignedInt();
            password = new String(channelBuffer.readBytes(8).array());

            Message resMsg = new Message(JT809Constants.UP_DISCONNECT_RSP);

            resBuffer = MessagePack.buildMessage(resMsg);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resBuffer;
    }
}
