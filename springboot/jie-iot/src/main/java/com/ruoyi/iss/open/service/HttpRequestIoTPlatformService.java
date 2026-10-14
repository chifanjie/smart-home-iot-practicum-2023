package com.ruoyi.iss.open.service;

import com.cloud.apigateway.sdk.utils.Client;
import com.cloud.apigateway.sdk.utils.Request;
import com.cloud.sdk.http.HttpMethodName;
import com.ruoyi.iss.open.constant.Constants;
import com.ruoyi.iss.open.util.CipherSuiteHelper;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

/**
 * Http请求服务
 *
 * @author 178535
 * @date 2023-08-19 15:08
 */
public class HttpRequestIoTPlatformService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestIoTPlatformService.class);

	/**
	 * 客户端类型 OpenApi的方式
	 */
	private static final String CLIENT_TYPE = "OpenApi";

	/**
	 * Content-Type 默认application/json
	 */
	private static final String DEFAULT_CONTENT_TYPE = "application/json";
	@Value("${iot.platform.api.access-key:}")
	private String accessKey;
	@Value("${iot.platform.api.secret-key:}")
	private String secretKey;
	@Value("${iot.platform.api.client-account:}")
	private String clientAccount;

	/**
	 * api地址 http://xx.xx.xx.xx:9999
	 */
	@Value("${iot.platform.api.url:}")
	private String apiAccessAddress;

	@Value("${iot.platform.api.enabled:false}")
	private boolean apiEnabled;

	/**
	 * 构造request请求对象
	 *
	 * @param uri  : uri
	 * @param body : body
	 * @return {@link Request}
	 * @date 2023/8/19 3:47 PM
	 * @author 178535
	 * @since 1.8
	 **/
	protected Request getDefaultRequest(String uri, String body)
	{
        if (!apiEnabled) {
            throw new IllegalStateException("External IoT API is disabled; set IOT_API_ENABLED=true to opt in");
        }
        if (accessKey.trim().isEmpty() || secretKey.trim().isEmpty()
                || clientAccount.trim().isEmpty() || apiAccessAddress.trim().isEmpty()) {
            throw new IllegalStateException("Configure IOT_API_ACCESS_KEY, IOT_API_SECRET_KEY, IOT_API_CLIENT_ACCOUNT and IOT_API_URL");
        }
		Request request = new Request();
		try
		{
			request.setKey(accessKey);
			request.setSecret(secretKey);
			request.setMethod(HttpMethodName.POST.name());
			request.setUrl(this.apiAccessAddress + uri);
			request.addHeader("Content-Type", DEFAULT_CONTENT_TYPE);
			request.addHeader("client_type", CLIENT_TYPE);
			request.addHeader("client_account", clientAccount);
			request.setBody(body);
		}
		catch (Exception e)
		{
			LOGGER.error("构建request对象error. e = {}", e);
		}
		return request;
	}

	/**
	 * 执行Http请求
	 *
	 * @param request : request
	 * @return {@link String}
	 * @date 2023/8/19 3:47 PM
	 * @author 178535
	 * @since 1.8
	 **/
	protected String sendHttpRequest(Request request)
	{
		try
		{
			okhttp3.Request signedRequest = Client.signOkhttp(request, Constants.SIGNATURE_ALGORITHM_SDK_HMAC_SHA256);
			OkHttpClient client = CipherSuiteHelper.createOkHttpClient(Constants.INTERNATIONAL_PROTOCOL);
			Response response = client.newCall(signedRequest).execute();
			LOGGER.info("status: " + response.code());
			ResponseBody body = response.body();
			String bodyStr = body.string();
			return bodyStr;
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		return null;
	}
}
