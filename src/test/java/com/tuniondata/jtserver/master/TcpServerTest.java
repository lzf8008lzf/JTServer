package com.tuniondata.jtserver.master;

import com.tuniondata.jtserver.BaseTest;
import org.junit.Test;

/**
 * Created by Think on 2017/10/20.
 */
public class TcpServerTest extends BaseTest {

    //@Autowired
//    private TcpServer tcpServer;

    @Test
    public void StartServer()
    {
        TcpServer tcpServer = new TcpServer();

        tcpServer.start();

        tcpServer.open();

        while (true)
        {

        }
    }
}
