package com.ruoyi.iss.common.exception;

/**
 * 不支持协议异常
 *
 * @author 178535
 * @date 2023/8/19 3:47 PM
 * @since 1.8
 **/
public class UnSupportProtocolException extends Exception
{
	private static final long serialVersionUID = -2890601429416749704L;

	/**
	 * 异常对应的描述信息
	 */
	private String message;

	public UnSupportProtocolException(String message)
	{
		super(message);
		this.message = message;
	}
}
