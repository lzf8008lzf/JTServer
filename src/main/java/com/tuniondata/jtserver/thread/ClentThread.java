package com.tuniondata.jtserver.thread;

import com.tuniondata.jtserver.slaver.TcpClient;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Created by Think on 2017/10/24.
 */
public class ClentThread implements Runnable{

    private static final TcpClient tcpClient = new TcpClient();

    private String address;
    private int port;

    public ClentThread(String addr,int p)
    {
        address = addr;
        port = p;
    }

    @Override
    public void run() {
        tcpClient.connect();
    }

    public void sendMessage(ChannelBuffer channelBuffer)
    {
        tcpClient.sendMessage(channelBuffer);
    }
}
