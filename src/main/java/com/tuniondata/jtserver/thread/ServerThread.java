package com.tuniondata.jtserver.thread;

import com.tuniondata.jtserver.master.TcpServer;

/**
 * Created by Think on 2017/10/24.
 */
public class ServerThread implements Runnable{

    private static final TcpServer tcpServer = new TcpServer();

    @Override
    public void run() {
        tcpServer.open();
    }
}
