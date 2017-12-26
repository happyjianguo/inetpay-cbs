package com.ylink.inetpay.cbs.util;

import java.util.HashMap;
import java.util.Map;

import com.ylink.inetpay.common.core.constant.BaseEnum;

/**
 * 渠道处理状态
 */
public enum EChannelProcState implements BaseEnum<EChannelProcState, String> {
	SUCCESS("SUCCESS","处理成功"),
	PROC_FAILURE("PROC_FAILURE","处理失败"),
	SEND_FAILURE("SEND_FAILURE","发送失败"),
	PARSING_FAILURE("PARSING_FAILURE","数据解析异常"),
	CONVERT_FAILURE("CONVERT_FAILURE","数据转换失败"),
	SIGN_ERROR("SIGN_ERROR","签名验签名错误"),
	SYS_ERROR("SYS_ERROR","系统错误"),
	CHNL_RTN_ERROR("CHNL_ERROR","渠道返回错误"),
	CHNL_PARAM_ERROR("CHNL_PARAM_ERROR","渠道参数配置错误"),
	SYS_PARAM_ERROR("SYS_PARAM_ERROR","系统参数配置错误"),
	SOCKET_CONNECTION_FAILURE("SOCKET_CONNECTION_FAILURE","连接Socket服务失败"),
	FTL_PROCESS_FAILUER("FTL_PROCESS_FAILUER","模板与数据合并失败"),
	FRONT_MSG_PROC_FAILUER("FRONT_MSG_PROC_FAILUER","统一前置报文处理失败"),
	;

	private String value;
	private String displayName;
	
	public String getValue() {
		return this.value;
	}

	public String getDisplayName() {
		return this.displayName;
	}
	
	private static Map<String, EChannelProcState> valueMap = new HashMap<String, EChannelProcState>();

	static {
		for (EChannelProcState _enum : EChannelProcState.values()) {
			valueMap.put(_enum.value, _enum);
		}
	}
	
	private EChannelProcState(String value,String displayName){
		this.value = value;
		this.displayName = displayName;
	}

	/**
	 * 枚举转换
	 */
	public static EChannelProcState parseOf(String value) {
		for (EChannelProcState item : values())
			if (item.getValue().equals(value))
				return item;

		throw new IllegalArgumentException("渠道编码类型类型[" + value + "]不匹配!");
	}

}