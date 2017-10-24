package com.tuniondata.jtserver;

import com.tuniondata.jtserver.slaver.TcpClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Think on 2017/10/20.
 */
public class TcpClientTest extends BaseTest {

    @Autowired
    private TcpClient tcpClient;

    @Test
    public void StartSlaverClient()
    {
        tcpClient.connect();
        while (true)
        {

        }
    }
}
