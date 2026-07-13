package com.ruoyi.iss.common.exception;

/**
 * 创建实例异常
 *
 * @author 178535
 * @date 2023/8/25 8:07 PM
 * @since 1.8
 **/
public class InstanceException extends RuntimeException
{
	/**
	 * @param message
	 */
	public InstanceException(String message)
	{
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InstanceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public InstanceException(Throwable cause)
	{
		super(cause);
	}
}
