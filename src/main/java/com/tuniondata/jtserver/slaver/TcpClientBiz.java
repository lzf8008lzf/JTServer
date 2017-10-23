package com.tuniondata.jtserver.slaver;

import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.message.MessagePack;
import com.tuniondata.jtserver.utils.ByteUtils;
import com.tuniondata.jtserver.utils.Constants;
import com.tuniondata.jtserver.utils.JT809Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Think on 2017/10/19.
 */
@Component("tcpClientBiz")
public class TcpClientBiz {
    private static Logger LOG = LoggerFactory.getLogger(TcpClientBiz.class);
    public static int PLANT_CODE=8899;//公司接入码
    public static int ZUCHE_ID=8899;//公司用户名
    public static String ZUCHE_PWD="company";//公司密码
    public static String LONGINSTATUS = "";
    public static String LOGINING = "logining";

    private static int LOGIN_FLAG = 0;

    //初始化基类
    @Autowired
    private TcpClient tcpClient;

    /** * 判断是否登录 * boolean * @return */
    public boolean isLogined(){
        return Constants.LOGIN_SUCCESS.equals(LONGINSTATUS);
        //Constants常量类，自己随便定义就好，LOGIN_STATAUS="0x00"
    }
    /** * 登录接入平台 * boolean * @return */
    public boolean login2Upsvr(){
        boolean success = false;
        try {
            if (!Constants.LOGIN_SUCCESS.equals(LONGINSTATUS) && !LOGINING.equals(LONGINSTATUS)) {
                //开始登录 Message为数据对象，代码稍后给出
                Message msg = new Message(JT809Constants.UP_CONNECT_REQ);
                ChannelBuffer buffer = ChannelBuffers.buffer(46);
                buffer.writeInt(ZUCHE_ID);//4
                byte[] pwd = ByteUtils.getBytesWithLengthAfter(8, ZUCHE_PWD.getBytes());
                buffer.writeBytes(pwd);//8
                byte[] ip = ByteUtils.getBytesWithLengthAfter(32, Constants.DOWN_LINK_IP.getBytes());
                buffer.writeBytes(ip);//32
                buffer.writeShort((short) Constants.TCP_RESULT_PORT);//2
                msg.setMsgBody(buffer);
                Channel channel = tcpClient.getChannel();

                channel.write(MessagePack.buildMessage(msg));
                LONGINSTATUS = LOGINING;
            }
        }catch (Exception ex)
        {
            LOG.error(ex.getMessage());
        }
        return success;
    }

}
