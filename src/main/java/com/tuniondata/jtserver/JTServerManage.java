package com.tuniondata.jtserver;

import com.tuniondata.jtserver.thread.ServerThread;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Think on 2017/10/19.
 */
@Component
public class JTServerManage {

    private static Logger LOG = LoggerFactory.getLogger(JTServerManage.class);

    //管理主链路

    //线程安全map,处理服务器hold住客户端连接的channel
    public static ConcurrentMap<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();

    public JTServerManage()
    {
        //startServer();
    }

    public void startServer() {

        try {
            //启动服务器
            ServerThread svrThread = new ServerThread();
            Thread t = new Thread(svrThread);
            t.setName("server thread");
            t.start();
        } catch (Exception e) {
            LOG.error("Exception on server: " + e.getMessage());
        }
    }

}
