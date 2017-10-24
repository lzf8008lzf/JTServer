package com.tuniondata.jtserver.bussiness;

import com.tuniondata.jtserver.master.IDataProcess;
import com.tuniondata.jtserver.message.Message;
import org.jboss.netty.buffer.ChannelBuffer;
import org.springframework.stereotype.Component;

/**
 * Created by Think on 2017/10/24.
 * 车辆定位信息
 */
@Component
public class ClientVehicleLocation implements IDataProcess {
    private byte encrypt=0; //1BYTE 加密标识
    private String date;    //4    dmyy
    private String time;    //3    时分秒
    private long lon;       //4   经度1*10^-6
    private long lat;       //4   纬度
    private int vec1;       //2   速度㎞/h
    private int vec2;       //2   行驶记录速度
    private long vec3;       //4   车辆当前总里程数
    private int direction;  //2  方向0~359
    private int altitude;   //2  海拔高度
    private long state;      //4  车辆状态
    private long alarm;      //4  报警状态

    @Override
    public ChannelBuffer process(Message msg) {
        ChannelBuffer resBuffer = null;

        ChannelBuffer channelBuffer = msg.getMsgBody();

        try {
            encrypt =  channelBuffer.readByte();
            date = new String(channelBuffer.readBytes(4).array());
            time = new String(channelBuffer.readBytes(3).array());
            lon = channelBuffer.readUnsignedInt();
            lat = channelBuffer.readUnsignedInt();
            vec1 = channelBuffer.readUnsignedShort();
            vec2 = channelBuffer.readUnsignedShort();
            vec3 = channelBuffer.readUnsignedInt();
            direction =  channelBuffer.readUnsignedShort();
            altitude =  channelBuffer.readUnsignedShort();
            state = channelBuffer.readUnsignedInt();
            alarm = channelBuffer.readUnsignedInt();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resBuffer;
    }
}
