package com.ruoyi.iss.open.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cloud.apigateway.sdk.utils.Request;
import com.ruoyi.iss.common.model.Result;
import com.ruoyi.iss.open.constant.ApiConstants;
import com.ruoyi.iss.open.model.request.PlatformDeviceRequest;
import com.ruoyi.iss.open.model.request.ShowDeviceRequest;
import com.ruoyi.iss.open.model.response.DeviceInfoResponse;
import org.springframework.stereotype.Service;

/**
 * 调用通鸿物联平台接口
 *
 * @author 178535
 * @date 2023-08-19 15:07
 */
@Service("platformService")
public class PlatformServiceImpl extends HttpRequestIoTPlatformService implements IPlatformService
{


	@Override
	public Result addDevice(PlatformDeviceRequest platformDeviceRequest)
	{
		String body = JSON.toJSONString(platformDeviceRequest);
		Request defaultRequest = this.getDefaultRequest(ApiConstants.DEVICE_ADD_POST, body);
		String response = this.sendHttpRequest(defaultRequest);
		TypeReference<Result> type = new TypeReference<Result>()
		{
		};
		Result apiResult = JSON.parseObject(response, type);
		return apiResult;
	}

	@Override
	public DeviceInfoResponse showDevice(ShowDeviceRequest showDeviceRequest)
	{
		String body = JSON.toJSONString(showDeviceRequest);
		Request defaultRequest = this.getDefaultRequest(ApiConstants.DEVICES_SHOW_POST, body);
		String response = this.sendHttpRequest(defaultRequest);
		TypeReference<Result<DeviceInfoResponse>> type = new TypeReference<Result<DeviceInfoResponse>>()
		{
		};
		Result<DeviceInfoResponse> deviceInfoResponseApiResult = JSON.parseObject(response, type);
		return deviceInfoResponseApiResult.getData();
	}
}