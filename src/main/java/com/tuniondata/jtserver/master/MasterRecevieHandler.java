package com.tuniondata.jtserver.master;

import com.tuniondata.cores.framework.spring.SpringContextHolder;
import com.tuniondata.jtserver.JTServerManage;
import com.tuniondata.jtserver.message.Message;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

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
            LOG.info("收到来自"+getIpAndPort(ctx,e.getChannel())+"客户端消息：" + "0x" + Integer.toHexString(msg.getMsgId()));

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

    /*
    获取IP地址及端口号
     */
    private String getIpAndPort(ChannelHandlerContext ctx, Channel channel)
    {
        String retStr = "";
        if (channel != null) {
            String host = ((InetSocketAddress) ctx.getChannel()
                .getRemoteAddress()).getAddress().getHostAddress();
            int port = ((InetSocketAddress) ctx.getChannel().getRemoteAddress())
                .getPort();

            //将ip和host组装起来,放到map里,用于管理多个Client的连接
            retStr = host + ":" + port;
        }

        return retStr;
    }

    /*
    添加连接到队列
     */
    public void addChannel(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        JTServerManage.channelMap.put(getIpAndPort(ctx,e.getChannel()),e.getChannel());
    }

    /*
    从队列中删除连接
     */
    public void delChannel(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        JTServerManage.channelMap.remove(getIpAndPort(ctx,e.getChannel()));
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

        addChannel(ctx,e);

        super.channelConnected(ctx, e);
    }
    // 必须是链接已经建立，关闭通道的时候才会触发
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        LOG.info("channelDisconnected");

        delChannel(ctx,e);

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
