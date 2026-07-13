package com.ruoyi.iss.common.exception;

/**
 * 自定义异常
 *
 * @author 178535
 * @date 2023-08-25 20:15
 */
public class CustomException extends RuntimeException
{
	private int code;

	public CustomException()
	{
	}

	/**
	 * MyException
	 *
	 * @param code 错误代码
	 */
	public CustomException(int code)
	{
		super("code=" + code);
		this.code = code;
	}

	/**
	 * MyException
	 *
	 * @param message 错误消息
	 */
	public CustomException(String message)
	{
		super(message);
	}

	/**
	 * MyException
	 *
	 * @param cause 捕获的异常
	 */
	public CustomException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * MyException
	 *
	 * @param message 错误消息
	 * @param cause   捕获的异常
	 */
	public CustomException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * MyException
	 *
	 * @param code    错误代码
	 * @param message 错误消息
	 */
	public CustomException(int code, String message)
	{
		super(message);
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}
}
