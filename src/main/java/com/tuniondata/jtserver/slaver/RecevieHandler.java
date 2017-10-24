package com.tuniondata.jtserver.slaver;

import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.utils.Constants;
import com.tuniondata.jtserver.utils.JT809Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Think on 2017/10/19.
 */
public class RecevieHandler extends SimpleChannelHandler {

    private static Logger LOG = LoggerFactory.getLogger(RecevieHandler.class);


    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e){
        Message msg = (Message) e.getMessage();
        LOG.info("应答----------------" + "0x" + Integer.toHexString(msg.getMsgId()));
        if(msg.getMsgId() == JT809Constants.UP_CONNECT_RSP){
            ChannelBuffer msgBody = msg.getMsgBody();
            int result = msgBody.readByte();
            if(result == JT809Constants.UP_CONNECT_RSP_SUCCESS){
                TcpClientBiz.LONGINSTATUS = Constants.LOGIN_SUCCESS;
                LOG.info("------------------登录成功");
            }else if(result == JT809Constants.UP_DISCONNECT_RSP){
                LOG.info("------------------注销成功");
            }
            else{
                TcpClientBiz.LONGINSTATUS = "";
                LOG.error("------------------登录异常，请检查" + "0x0" + Integer.toHexString(result));
            }
        }
    }

    // 捕获异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
        throws Exception {
        LOG.error("exceptionCaught:"+e.getCause().getMessage());
        e.getChannel().close();
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
