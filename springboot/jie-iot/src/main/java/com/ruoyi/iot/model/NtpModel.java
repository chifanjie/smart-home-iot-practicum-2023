package com.ruoyi.iot.model;

/**
 * 产品分类的Id和名称输出
 *
 * @author chifanjie
 * @date 2023-8-27
 */
public class NtpModel
{
    private Long deviceSendTime;

    private Long serverRecvTime;

    private Long serverSendTime;

    public Long getDeviceSendTime() {
        return deviceSendTime;
    }

    public void setDeviceSendTime(Long deviceSendTime) {
        this.deviceSendTime = deviceSendTime;
    }

    public Long getServerRecvTime() {
        return serverRecvTime;
    }

    public void setServerRecvTime(Long serverRecvTime) {
        this.serverRecvTime = serverRecvTime;
    }

    public Long getServerSendTime() {
        return serverSendTime;
    }

    public void setServerSendTime(Long serverSendTime) {
        this.serverSendTime = serverSendTime;
    }
}
