package com.tuniondata.jtserver.slaver;

import com.tuniondata.jtserver.Decoder;
import com.tuniondata.jtserver.utils.Constants;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.tuniondata.jtserver.utils.Constants.*;

/**
 * Created by Think on 2017/10/19.
 */
@Component
public class TcpClient {
    private static final Logger LOG = LoggerFactory.getLogger(TcpClient.class);

    private ClientBootstrap bootstrap = null;
    private Executor bossExecutor = Executors.newCachedThreadPool();
    private Executor workerExecutor = Executors.newCachedThreadPool();

    private String address = Constants.DOWN_LINK_IP;
    private int port = Constants.DEFAULT_PORT;
    private Channel channel;

    public void init() {

        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
            bossExecutor, workerExecutor, Constants.workerCount));

        bootstrap.setOption("tcpNoDelay", tcpNoDelay);
        bootstrap.setOption("connectTimeoutMillis", Constants.connectTimeoutMillis);
        bootstrap.setOption("reuseAddress", reuseAddress);
        bootstrap.setOption("keepAlive", keepAlive);

    }

    public Channel getChannel() {
        if (null == channel || !channel.isOpen()) {
            bootstrap.setOption("writeBufferHighWaterMark", 64 * 1024);
            bootstrap.setOption("writeBufferLowWaterMark", 32 * 1024);
            bootstrap.setPipelineFactory(new ChannelPipelineFactory(){
                @Override
                public ChannelPipeline getPipeline() throws Exception {
                    ChannelPipeline pipeline = Channels.pipeline();
//                  pipeline.addLast("loging", new LoggingHandler(InternalLogLevel.ERROR)); 打印日志信息，上线稳定后可去掉
                    pipeline.addLast("timeout", new IdleStateHandler(new HashedWheelTimer(), 10, 60, 0));//设置空闲心跳机制
                    pipeline.addLast("heartbeat", new HeartBeatHandler());//心跳发送包处理handler
                    pipeline.addLast("decode", new Decoder());//解码
                    pipeline.addLast("loginHandler", new RecevieHandler());//反馈数据处理
                    return pipeline;
                }
            });

            ChannelFuture future = bootstrap.connect(new InetSocketAddress(
                address, port));

            future.awaitUninterruptibly();

            if (future.isSuccess()) {
                channel = future.getChannel();
            } else {
                throw new RuntimeException(future.getCause());
            }
        }
        return channel;
    }

}
