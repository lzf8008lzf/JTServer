package com.tuniondata.jtserver.bussiness;

import com.tuniondata.jtserver.master.IDataProcess;
import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.message.MessagePack;
import com.tuniondata.jtserver.utils.JT809Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.springframework.stereotype.Component;

/**
 * Created by Think on 2017/10/23.
 * 链路保持信息处理
 */
@Component
public class ClientHold implements IDataProcess {

    @Override
    public ChannelBuffer process(Message msg) {

        ChannelBuffer resBuffer = null;

        try {

            Message resMsg = new Message(JT809Constants.UP_LINKTEST_RSP);

            resBuffer = MessagePack.buildMessage(resMsg);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resBuffer;
    }
}
