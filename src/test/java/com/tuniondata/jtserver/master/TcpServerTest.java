package com.tuniondata.jtserver.master;

import com.tuniondata.jtserver.BaseTest;
import com.tuniondata.jtserver.JTServerManage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Think on 2017/10/20.
 */
public class TcpServerTest extends BaseTest {

    @Autowired
    private JTServerManage serverManage;

    @Test
    public void StartServer()
    {
        serverManage.startServer();

        while (true)
        {

        }
    }
}
