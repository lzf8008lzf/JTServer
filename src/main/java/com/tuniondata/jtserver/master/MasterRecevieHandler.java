package com.tuniondata.jtserver.master;

import com.tuniondata.cores.framework.spring.SpringContextHolder;
import com.tuniondata.jtserver.message.Message;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Think on 2017/10/20.
 */
public class MasterRecevieHandler extends SimpleChannelHandler {

    private static Logger LOG = LoggerFactory.getLogger(MasterRecevieHandler.class);

    private MessageDispose messageDispose;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e){
        try {
            Message msg = (Message) e.getMessage();
            LOG.info("收到客户端消息ID：" + "0x" + Integer.toHexString(msg.getMsgId()));

            messageDispose = (MessageDispose) SpringContextHolder.getBean("messageDispose");
            if(messageDispose == null)
            {
                LOG.error("messageDispose is null!");
                return;
            }

            //发送到消息处理模块进行处理
            ChannelBuffer resBuf = messageDispose.msgDispose(msg);
            if(resBuf!=null)
            {
                e.getChannel().write(resBuf);
            }

        }catch (Exception ex)
        {
            LOG.error(ex.getMessage());
        }

    }
    // 捕获异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
        throws Exception {
        LOG.error("exceptionCaught:"+e.getCause().getMessage());
        //super.exceptionCaught(ctx, e);
    }

    // 新连接
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
        throws Exception {
        LOG.info("channelConnected");
        super.channelConnected(ctx, e);
    }
    // 必须是链接已经建立，关闭通道的时候才会触发
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        LOG.info("channelDisconnected");
        super.channelDisconnected(ctx, e);
    }

    // channel关闭的时候触发
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
        throws Exception {
        LOG.info("channelClosed");
        super.channelClosed(ctx, e);
    }
}
