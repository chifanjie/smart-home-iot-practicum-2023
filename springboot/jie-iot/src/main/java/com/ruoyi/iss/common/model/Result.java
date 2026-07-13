package com.ruoyi.iss.common.model;


import com.ruoyi.iss.common.constant.ApiCodeConstant;

import java.io.Serializable;

/**
 * 统一返回数据结果
 *
 * @author 178535
 * @date 2023/8/25 8:01 PM
 * @since 1.8
 **/
public class Result<T> implements Serializable
{
	private static final long serialVersionUID = 4993843308628984584L;

	/**
	 * 业务code码
	 */
	private int code;

	/**
	 * 消息
	 */
	private String msg;

	/**
	 * 数据
	 */
	private T data;

	public Result(int code)
	{
		this(code, "", null);
	}

	public Result(int code, String msg)
	{
		this(code, msg, null);
	}

	public Result(int code, String msg, T data)
	{
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	/**
	 * 返回成功
	 *
	 * @return {@link Result<T>}
	 * @date 2023/8/25 11:01 AM
	 * @author 178535
	 * @since 1.8
	 **/
	public static <T> Result<T> success()
	{
		return success(null);
	}

	/**
	 * 返回成功数据
	 *
	 * @param data : data
	 * @return {@link Result<T>}
	 * @date 2023/8/25 11:01 AM
	 * @author 178535
	 * @since 1.8
	 **/
	public static <T> Result<T> success(T data)
	{
		return success("操作成功", data);
	}

	/**
	 * 返回成功消息
	 *
	 * @param msg : msg
	 * @return {@link Result<T>}
	 * @date 2023/8/25 11:01 AM
	 * @author 178535
	 * @since 1.8
	 **/
	public static <T> Result<T> successMsg(String msg)
	{
		return success(msg, null);
	}

	/**
	 * 返回成功
	 *
	 * @param msg  : msg
	 * @param data : data
	 * @return {@link Result<T>}
	 * @date 2023/8/25 11:01 AM
	 * @author 178535
	 * @since 1.8
	 **/
	public static <T> Result<T> success(String msg, T data)
	{
		return new Result<>(ApiCodeConstant.SUCCESS, msg, data);
	}

	/**
	 * @param msg : msg
	 * @return {@link Result<T>}
	 * @date 2023/8/25 11:01 AM
	 * @author 178535
	 * @since 1.8
	 **/
	public static <T> Result<T> failure(String msg)
	{
		return failure(ApiCodeConstant.FAIL, msg);
	}

	/**
	 * 返回错误
	 *
	 * @param code : code
	 * @param msg  : msg
	 * @return {@link Result<T>}
	 * @date 2023/8/25 11:01 AM
	 * @author 178535
	 * @since 1.8
	 **/
	public static <T> Result<T> failure(int code, String msg)
	{
		return new Result<>(code, msg, null);
	}

	/**
	 * 返回错误
	 *
	 * @param data : data
	 * @return {@link Result<T>}
	 * @date 2023/8/25 11:01 AM
	 * @author 178535
	 * @since 1.8
	 **/
	public static <T> Result<T> failure(T data)
	{
		return new Result<>(ApiCodeConstant.FAIL, "操作失败", data);
	}

	/**
	 * 默认返回错误
	 *
	 * @return {@link Result<T>}
	 * @date 2022/6/24 4:20 PM
	 * @author 178535
	 * @since 1.8
	 **/
	public static <T> Result<T> failure()
	{
		return failure(ApiCodeConstant.FAIL, "操作失败");
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public T getData()
	{
		return data;
	}

	public void setData(T data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		return "Result{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
	}
}
