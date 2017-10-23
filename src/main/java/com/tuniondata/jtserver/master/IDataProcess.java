package com.tuniondata.jtserver.master;

import com.tuniondata.jtserver.message.Message;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Created by Think on 2017/10/23.
 */
public interface IDataProcess {
    /*
    解析数据包，并返回相应的处理信息
     */
    ChannelBuffer process(Message msg);
}
