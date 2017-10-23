package com.tuniondata.jtserver.master;


import com.tuniondata.jtserver.bussiness.ClientHold;
import com.tuniondata.jtserver.bussiness.ClientLogin;
import com.tuniondata.jtserver.bussiness.ClientLogout;
import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.utils.JT809Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Think on 2017/10/23.
 */
@Component("messageDispose")
public class MessageDispose {

    private static Logger LOG = LoggerFactory.getLogger(MessageDispose.class);

    @Autowired
    private ClientLogin clientLogin;

    @Autowired
    private ClientLogout clientLogout;

    @Autowired
    private ClientHold clientHold;

    public ChannelBuffer msgDispose(Message msg)
    {
        ChannelBuffer resBuf = null;

        int msgType=msg.getMsgId();
        ChannelBuffer channelBuffer = msg.getMsgBody();

        switch (msgType)
        {
            case JT809Constants.UP_CONNECT_REQ:
                LOG.info("登录请求消息");
                resBuf = clientLogin.process(msg);
                break;
            case JT809Constants.UP_DICONNECE_REQ:
                LOG.info("注销请求消息");
                resBuf = clientLogout.process(msg);
                break;
            case JT809Constants.UP_LINKETEST_REQ:
                LOG.info("连接保持请求消息");
                resBuf = clientHold.process(msg);
                break;
            default:
                LOG.error("没有找到相应的消息处理体！");
                break;
        }

        return resBuf;
    }
}
