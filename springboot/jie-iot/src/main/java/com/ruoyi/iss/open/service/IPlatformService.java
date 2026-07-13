package com.ruoyi.iss.open.service;


import com.ruoyi.iss.common.model.Result;
import com.ruoyi.iss.open.model.request.PlatformDeviceRequest;
import com.ruoyi.iss.open.model.request.ShowDeviceRequest;
import com.ruoyi.iss.open.model.response.DeviceInfoResponse;

/**
 * 对接IoT平台开发接口服务
 *
 * @author 178535
 * @date 2023-08-19 15:07
 */
public interface IPlatformService
{
	/**
	 * 新增设备
	 *
	 * @param platformDeviceRequest : platformDeviceRequest
	 * @return {@link Result}
	 * @date 2022/11/15 10:32 AM
	 * @author 178535
	 * @since 1.8
	 **/
	Result addDevice(PlatformDeviceRequest platformDeviceRequest);

	/**
	 * 根据设备ID查询物联网平台中指定设备的详细信息
	 *
	 * @param showDeviceRequest : showDeviceRequest
	 * @return {@link Result}
	 * @date 2022/8/30 10:48 AM
	 * @author 178535
	 * @since 1.8
	 **/
	DeviceInfoResponse showDevice(ShowDeviceRequest showDeviceRequest);
}
