package com.ylink.inetpay.cbs.bis.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.cbs.mrs.service.MrsBackupsService;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;

public class CbsBackupsAppServiceTest  extends UCBaseTest {
	protected static final Logger logger = LoggerFactory.getLogger(CbsBackupsAppServiceTest.class);

	@Autowired
	private MrsBackupsService  mrsBackupsService;
	
	
	@Test
	public void test(){
			SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd");
			try {
				mrsBackupsService.backupsOperationLog(f.parse("20160909"));
			} catch (ParseException e) {
				logger.error("系统异常，异常原因：{}",ExceptionProcUtil.getExceptionDesc(e));
			}
	}
}
