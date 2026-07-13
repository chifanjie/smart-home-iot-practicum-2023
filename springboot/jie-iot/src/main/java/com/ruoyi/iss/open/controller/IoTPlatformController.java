package com.ruoyi.iss.open.controller;

import com.ruoyi.iss.common.model.Result;
import com.ruoyi.iss.open.model.request.ShowDeviceRequest;
import com.ruoyi.iss.open.model.response.DeviceInfoResponse;
import com.ruoyi.iss.open.service.IPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对接IoT平台开发接口
 *
 * @author 178535
 * @date 2023-08-19 15:41
 */
@RestController
@RequestMapping("/api/app/smart/home/iot/device")
public class IoTPlatformController
{
	private IPlatformService platformService;

	/**
	 * 根据设备ID查询物联网平台中指定设备的详细信息
	 *
	 * @param showDeviceRequest : showDeviceRequest
	 * @return {@link Result}
	 * @date 2023/8/19 3:44 PM
	 * @author 178535
	 * @since 1.8
	 **/
	@PostMapping("/show")
	public Result showDevice(@RequestBody ShowDeviceRequest showDeviceRequest)
	{
		DeviceInfoResponse deviceInfoResponse = this.platformService.showDevice(showDeviceRequest);
		return Result.success(deviceInfoResponse);
	}

	@Autowired
	public void setPlatformService(IPlatformService platformService)
	{
		this.platformService = platformService;
	}
}
