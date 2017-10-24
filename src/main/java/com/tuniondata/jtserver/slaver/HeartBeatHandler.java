package com.tuniondata.jtserver.slaver;

import com.tuniondata.cores.framework.spring.SpringContextHolder;
import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.message.MessagePack;
import com.tuniondata.jtserver.utils.JT809Constants;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Think on 2017/10/19.
 */
public class HeartBeatHandler extends IdleStateAwareChannelHandler {
    private static Logger LOG = LoggerFactory.getLogger(HeartBeatHandler.class);

    private TcpClientBiz tcpClientBiz;

    @Override
    public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception{

        if(tcpClientBiz == null) {
            tcpClientBiz = (TcpClientBiz) SpringContextHolder.getBean("tcpClientBiz");
            if (tcpClientBiz == null) {
                LOG.error("tcpClientBiz is null!");
                return;
            }
        }

        if(StringUtils.isBlank(tcpClientBiz.LONGINSTATUS) || tcpClientBiz.LOGINING.equals(tcpClientBiz.LONGINSTATUS)){

            tcpClientBiz.login2Upsvr();
            LOG.info("利用空闲心跳去登录------ 开始登录");
        }else{
            tcpClientBiz.loginOut();
        }


        if(e.getState() == IdleState.WRITER_IDLE){
            LOG.info("链路空闲，发送心跳!");
            Message msg = new Message(JT809Constants.UP_LINKETEST_REQ);
            e.getChannel().write(MessagePack.buildMessage(msg));
            super.channelIdle(ctx, e);
        }
    }
}
