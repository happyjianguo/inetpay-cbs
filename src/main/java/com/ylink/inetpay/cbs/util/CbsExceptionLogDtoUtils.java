package com.ylink.inetpay.cbs.util;

import java.net.Inet4Address;

import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;

/**
 * 获取cbs中需要的日志监控对象
 * @author lyg
 *
 */
public class CbsExceptionLogDtoUtils {
	public static BisExceptionLogDto getBisExceptionLog(EBisExceptionLogNlevel nlevel,EBisExceptionLogType exceptionLogType,String content){
		BisExceptionLogDto	dto=new BisExceptionLogDto();
		dto.setSystem(EBisSmsSystem.CBS);
		dto.setNlevel(nlevel);
		dto.setType(exceptionLogType);
		dto.setContent(content);
		String addree = "";
		try {
			addree = Inet4Address.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dto.setAllpath(addree + CbsConfig.getLogFullPath());
		return dto;
	}
}
