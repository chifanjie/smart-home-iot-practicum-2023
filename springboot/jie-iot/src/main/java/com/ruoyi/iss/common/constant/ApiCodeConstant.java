package com.ruoyi.iss.common.constant;

public interface ApiCodeConstant
{
	int SUCCESS = 20000;    //成功
	int FAIL = -1;    //失败
	int INTERNAL_SERVER_ERROR = 500; //服务器内部错误
	int RESULT_LOGIN_FAIL = 50001;  //用户名或者密码错误
	int RESULT_PERMISSION_EXCEPTION = 50002;         //自定义权限异常
	int RESULT_TOKEN_FAIL = 50014;         //token校验失败
	int RESULT_NULL_POINT_EXCEPTION = 50101;         //空指针异常
	int RESULT_OTHER_EXCEPTION = 50102;         //其他异常
	int RESULT_AK_SK_EXCEPTION = 50103;   //AK SK 验证签名错误
	int RESULT_ID_GEN_EXCEPTION = 502021;         //id派发异常
	int DATA_CONTENT_DUPLICATE_EXCEPTION = 503001;  //数据内容重复
	int DEFAULT_DATA_DELETE_EXCEPTION = 503002;   //默认数据删除异常
	int DEFAULT_DATA_SELECT_EXCEPTION = 503003;   //查询数据异常
	int DATA_PARAMETER_NULL_EXCEPTION = 60001;   //数据参数为空
	int MQTT_CMD_RESPONSE_TIMEOUT_EXCEPTION = 70001;  //下发命令给设备侧之后，设备侧回复超时异常
}
