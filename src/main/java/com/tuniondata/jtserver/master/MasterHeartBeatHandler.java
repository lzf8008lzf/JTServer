package com.tuniondata.jtserver.master;

import com.tuniondata.jtserver.slaver.HeartBeatHandler;
import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.message.MessagePack;
import com.tuniondata.jtserver.utils.JT809Constants;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Created by Think on 2017/10/19.
 */
public class MasterHeartBeatHandler extends IdleStateAwareChannelHandler {
    private static Logger LOG = LoggerFactory.getLogger(HeartBeatHandler.class);
    @Override
    public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception{
        if(e.getState() == IdleState.WRITER_IDLE){
            LOG.error("链路空闲，发送心跳!");
            Message msg = new Message(JT809Constants.UP_LINKETEST_REQ);
            e.getChannel().write(MessagePack.buildMessage(msg));
            super.channelIdle(ctx, e);
        }
    }
}
