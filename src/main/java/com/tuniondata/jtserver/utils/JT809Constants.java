package com.tuniondata.jtserver.utils;

/**
 * Created by Think on 2017/10/19.
 */
public class JT809Constants {
    public final static int UP_CONNECT_REQ = 0x1001;//主链路登录请求消息

    public final static int UP_CONNECT_RSP = 0x1002;//主链路登录应答消息
    public static int UP_CONNECT_RSP_SUCCESS = 0x00;//登录成功
    public static int UP_CONNECT_RSP_ERROR_01 = 0x01;//IP 地址不正确
    public static int UP_CONNECT_RSP_ERROR_02 = 0x02;//接入码不正确
    public static int UP_CONNECT_RSP_ERROR_03 = 0x03;//用户没注册
    public static int UP_CONNECT_RSP_ERROR_04 = 0x04;//密码错误
    public static int UP_CONNECT_RSP_ERROR_05 = 0x05;//资源紧张,稍后再连接(已经占 用);
    public static int UP_CONNECT_RSP_ERROR_06 = 0x06;//其他

    public final static int UP_DICONNECE_REQ = 0x1003;//主链路注销请求消息

    public final static int UP_DISCONNECT_RSP = 0x1004;//主链路注销应答消息

    public final static int UP_LINKETEST_REQ = 0x1005;//主链路连接保持请求消息

    public final static int UP_LINKTEST_RSP = 0x1006;//主链路连接保持应答消息

    public final static int UP_DISCONNECT_INFORM = 0x1007;//主链路断开通知消息

    public final static int UP_CLOSELINK_INFORM = 0x1008;//下级平台主动关闭链路通 知消息

    public final static int DOWN_CONNECT_REQ = 0x9001;//从链路连接请求消息

    public final static int DOWN_CONNECT_RSP = 0x9002;//从链路连接应答消息

    public final static int DOWN_DISCONNECT_REQ = 0x9003;//从链路注销请求消息

    public final static int DOWN_LINKETEST_REQ = 0x9005;//从链路连接保持请求消息

    public final static int DOWN_LINKETEST_RSP = 0x9006;//从链路连接保持应答消息

    public final static int DOWN_DISCONNECT_INFORM = 0x9007;//从链路断开通知消息

    public final static int DOWN_CLOSELINK_INFORM = 0x9008;//上级平台主动关闭链路通知消息

    public final static int DOWN_TOTAL_RECV_BAK_MSG = 0x9101;//从链路 接收定位信息数量通知

    public final static int UP_EXG_MSG = 0x1200;//主链路动态信息交换消息

    public final static int DOWN_EXG_MSG = 0x9200;//从链路动态信息交换消息

    public final static int UP_PLATFORM_MSG = 0x1300;//主链路平台信息交换消息

    public final static int DOWN_PLATFORM_MSG = 0x9300;//从链路平台信息交换消息

    public final static int UP_WARN_MSG = 0x1400;//主链路报警信息交换消息

    public final static int DOWN_WARN_MSG = 0x9400;//从链路报警信息交换消息

    public final static int UP_CTRL_MSG = 0x1500;//主链路车辆监管信息

    public final static int DOWN_CTRL_MSG = 0x9500;//从链路车辆监管信息

    public final static int UP_BASE_MSG = 0x1600;//主链路静态信息交换信息

    public final static int DOWN_BASE_MSG = 0x9600;//从链路静态信息交换信息

    public final static int UP_VEHICLE_GPS_MSG = 0x1200;     //车辆定位信息

    public final static int UP_EXG_MSG_REAL_LOCATION = 0x1202;//实时上传车辆定位信息
    public final static int UP_EXG_MSG_HISTORY_LOCATION = 0x1203;//车辆定位信息自动补报


}
