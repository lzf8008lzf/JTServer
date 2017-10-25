package com.tuniondata.jtserver.bussiness;


import com.tuniondata.jtserver.master.IDataProcess;
import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.message.MessagePack;
import com.tuniondata.jtserver.utils.JT809Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Think on 2017/10/23.
 * 客户端连接请求处理
 */
@Component("clientLogin")
public class ClientLogin implements IDataProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ClientLogin.class);

    private int userID;         //用户ID
    private String password;    //用户密码
    private String downLinkIp;  //下级平台提供的从链路服务端IP地址
    private int downLinkPort;   //下级平台提供的从链路服务端口号
    private int msgGesscenterId;     //下级平台接入码

    @Override
    public ChannelBuffer process(Message msg) {

        ChannelBuffer resBuffer = null;
        ChannelBuffer channelBuffer = msg.getMsgBody();

        try {
            userID = (int)channelBuffer.readUnsignedInt();
            password = new String(channelBuffer.readBytes(8).array());
            downLinkIp = new String(channelBuffer.readBytes(32).array());
            downLinkPort = channelBuffer.readUnsignedShort();
            msgGesscenterId = msg.getMsgGesscenterId();

            //校验用户名及密码，通过后返回成功，否则失败
            int loginResult = loginCheck();

            Message resMsg = new Message(JT809Constants.UP_CONNECT_RSP);

            resBuffer = ChannelBuffers.buffer(5);
            resBuffer.writeByte(loginResult);
            resBuffer.writeInt(msg.getCrcCode());//校验码，还不知道返回啥内容
            resMsg.setMsgBody(resBuffer);

            resBuffer = MessagePack.buildMessage(resMsg);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resBuffer;
    }

    private int loginCheck()
    {
        LOG.info(this.toString());

        int iRet = JT809Constants.UP_CONNECT_RSP_ERROR_06;

        //用户名及密码校验

        //根据用户名查询，如果用户不存在返回 UP_CONNECT_RSP_ERROR_03

        //判断密码是否正确，如果错误返回 UP_CONNECT_RSP_ERROR_04

        //判断接入码是否正，错误返回 UP_CONNECT_RSP_ERROR_02

        //都通过验证 返回UP_CONNECT_RSP_SUCCESS

        iRet = JT809Constants.UP_CONNECT_RSP_SUCCESS;

        return  iRet;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+" [userID=" + userID + ", password=" + password + ", downLinkIp="
            + downLinkIp + ", downLinkPort=" + downLinkPort + ", msgGesscenterId=" + msgGesscenterId + "]";
    }
}
