package com.ruoyi.iss.open.model.request;

/**
 * 请求参数类
 *
 * @author 178535
 * @date 2023-08-19 15:10
 */
public class PlatformDeviceRequest
{
	/**
	 * 资源空间id;资源空间表主键id
	 */
	private Long resourceSpaceId;

	/**
	 * 资源空间名
	 */
	private String resourceSpaceName;

	/**
	 * appId 对应空间资源appId
	 */
	private String appId;

	/**
	 * 所属产品;核心产品表主键id
	 */
	private Long productId;

	/**
	 * 所属产品名称
	 */
	private String productName;

	/**
	 * 核心产品表中的定制产品id
	 */
	private String definitionProductId;

	/**
	 * 设备标识码
	 */
	private String deviceCode;

	/**
	 * 设备名称
	 */
	private String deviceName;

	/**
	 * 设备id;产品id_设备标识码(definitionProductId_deviceCode)
	 */
	private String deviceId;

	/**
	 * 设备认证类型
	 */
	private String deviceAuthType;

	/**
	 * 秘钥
	 */
	private String deviceKey;

	/**
	 * 设备状态
	 */
	private String deviceState;

	/**
	 * 设备冻结前状态
	 */
	private String beforeFreeze;

	/**
	 * 设备描述
	 */
	private String description;

	public Long getResourceSpaceId()
	{
		return resourceSpaceId;
	}

	public void setResourceSpaceId(Long resourceSpaceId)
	{
		this.resourceSpaceId = resourceSpaceId;
	}

	public String getResourceSpaceName()
	{
		return resourceSpaceName;
	}

	public void setResourceSpaceName(String resourceSpaceName)
	{
		this.resourceSpaceName = resourceSpaceName;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getDefinitionProductId()
	{
		return definitionProductId;
	}

	public void setDefinitionProductId(String definitionProductId)
	{
		this.definitionProductId = definitionProductId;
	}

	public String getDeviceCode()
	{
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode)
	{
		this.deviceCode = deviceCode;
	}

	public String getDeviceName()
	{
		return deviceName;
	}

	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getDeviceAuthType()
	{
		return deviceAuthType;
	}

	public void setDeviceAuthType(String deviceAuthType)
	{
		this.deviceAuthType = deviceAuthType;
	}

	public String getDeviceKey()
	{
		return deviceKey;
	}

	public void setDeviceKey(String deviceKey)
	{
		this.deviceKey = deviceKey;
	}

	public String getDeviceState()
	{
		return deviceState;
	}

	public void setDeviceState(String deviceState)
	{
		this.deviceState = deviceState;
	}

	public String getBeforeFreeze()
	{
		return beforeFreeze;
	}

	public void setBeforeFreeze(String beforeFreeze)
	{
		this.beforeFreeze = beforeFreeze;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}