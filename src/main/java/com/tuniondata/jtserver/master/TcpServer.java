package com.tuniondata.jtserver.master;

import com.tuniondata.jtserver.Decoder;
import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.utils.Constants;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Think on 2017/10/19.
 */
public class TcpServer {
    private static final Logger LOG = LoggerFactory.getLogger(TcpServer.class);

    private int port = Constants.DEFAULT_PORT;

    private ServerBootstrap bootstrap = null;

    private Executor bossExecutor = Executors.newCachedThreadPool();

    private Executor workerExecutor = Executors.newCachedThreadPool();

    public TcpServer()
    {
        init();
    }

    private void init() {

        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
            bossExecutor, workerExecutor, Constants.workerCount));
        bootstrap.setOption("tcpNoDelay", Constants.tcpNoDelay);
        bootstrap.setOption("connectTimeoutMillis", Constants.connectTimeoutMillis);
        bootstrap.setOption("reuseAddress", Constants.reuseAddress);
        bootstrap.setOption("keepAlive", Constants.keepAlive);

        // Set up the default event pipeline.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                  pipeline.addLast("loging", new LoggingHandler(InternalLogLevel.DEBUG)); //打印日志信息，上线稳定后可去掉
//                pipeline.addLast("timeout", new IdleStateHandler(new HashedWheelTimer(), 10, 60, 0));//设置空闲心跳机制
//                pipeline.addLast("heartbeat", new MasterHeartBeatHandler());//心跳发送包处理handler
                ChannelBuffer buffer = ChannelBuffers.buffer(1);
                buffer.writeByte(Message.MSG_TALL);//分包处理
                pipeline.addLast("framer", new DelimiterBasedFrameDecoder(2048,buffer));
                pipeline.addLast("decode", new Decoder());//解码
                pipeline.addLast("masterRecevie", new MasterRecevieHandler());//反馈数据处理
                return pipeline;
            }
        });
    }

    /**
     * 启动监听端口
     */
    public void open()
    {
        // Bind and start to accept incoming connections.
        Channel bind = bootstrap.bind(new InetSocketAddress(port));

        LOG.debug("Server已经启动，监听端口: " + bind.getLocalAddress() + "， 等待客户端注册。。。");
    }

    /**
     * 停止监听端口
     */
    public void shutdown()
    {
        bootstrap.shutdown();

        LOG.error("关闭监听端口");
    }
}
