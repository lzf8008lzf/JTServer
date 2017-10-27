package com.tuniondata.jtserver.bussiness;

import com.tuniondata.jtserver.master.IDataProcess;
import com.tuniondata.jtserver.message.Message;
import com.tuniondata.jtserver.utils.JT809Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Think on 2017/10/24.
 * 车辆定位信息
 */
@Component
public class ClientVehicleLocation implements IDataProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ClientVehicleLocation.class);

    private String vehichleNo;       //21车牌号
    private int vehicleColor;      //车牌颜色
    private int dataType;            //子业务类型
    private long dataLength;          //后续数据长度
    private byte encrypt=0; //1BYTE 加密标识
    private String date;    //4    dmyy
    private String time;    //3    时分秒
    private double lon;       //4   经度1*10^-6
    private double lat;       //4   纬度
    private int vec1;       //2   速度㎞/h
    private int vec2;       //2   行驶记录速度
    private long vec3;       //4   车辆当前总里程数
    private int direction;  //2  方向0~359
    private int altitude;   //2  海拔高度
    private long state;      //4  车辆状态
    private long alarm;      //4  报警状态

    @Override
    public ChannelBuffer process(Message msg) {

        double unit=1000000.00; //经纬度除数
        ChannelBuffer resBuffer = null;

        ChannelBuffer channelBuffer = msg.getMsgBody();

        try {
            vehicleColor = channelBuffer.readByte();
            vehichleNo = new String(channelBuffer.readBytes(21).array(), "GBK");
            vehicleColor = channelBuffer.readByte();
            dataType = channelBuffer.readUnsignedShort();
            dataLength = channelBuffer.readUnsignedInt();

            //子业务类型
            if(dataType== JT809Constants.UP_EXG_MSG_REAL_LOCATION) {
                encrypt = channelBuffer.readByte();
                date = String.format("%02d-%02d-%04d",channelBuffer.readByte(),channelBuffer.readByte(),channelBuffer.readUnsignedShort());
                time = String.format("%02d:%02d:%02d",channelBuffer.readByte(),channelBuffer.readByte(),channelBuffer.readByte());
                lon = channelBuffer.readUnsignedInt()/unit;
                lat = channelBuffer.readUnsignedInt()/unit;
                vec1 = channelBuffer.readUnsignedShort();
                vec2 = channelBuffer.readUnsignedShort();
                vec3 = channelBuffer.readUnsignedInt();
                direction = channelBuffer.readUnsignedShort();
                altitude = channelBuffer.readUnsignedShort();
                state = channelBuffer.readUnsignedInt();
                alarm = channelBuffer.readUnsignedInt();

                //解析完成，将数据保存至数据库。
            }

            LOG.info(this.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resBuffer;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+" [vehichleNo=" + vehichleNo + ", vehicleColor=" + vehicleColor + ", dataType="+ dataType
            + ", dataLength=" + dataLength + ", encrypt=" + encrypt+ ", date=" + date+ ", time=" + time+ ", lon=" + lon
            + ", lat=" + lat+ ", vec1=" + vec1+ ", vec2=" + vec2+ ", vec3=" + vec3+ ", direction=" + direction+ ", altitude=" + altitude
            + ", state=" + state+ ", alarm=" + alarm+ "]";
    }
}
