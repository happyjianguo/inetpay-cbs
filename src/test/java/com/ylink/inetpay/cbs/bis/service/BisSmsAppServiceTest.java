package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.List;

import com.ylink.inetpay.cbs.bis.dao.BisTransferHandleDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.core.constant.EmessType;
import com.ylink.inetpay.common.core.constant.EsendChannel;

public class BisSmsAppServiceTest  extends UCBaseTest {

	
	@Autowired
	@Qualifier("bisSmsService")
	private BisSmsService smsApp;
    @Autowired
    private BisTransferHandleDao bisTransferHandleDao;
	@Test
	public void test() {
		String tel = "18665378385";
		EBisSmsSystem smsSystem = EBisSmsSystem.PORTAL;
		List<String> params = new ArrayList<>();
		params.add("123456");
		EBisTemplateCode smsTemplateCode = EBisTemplateCode.SMS_CODE;
		EmessType messType = EmessType.MESSAGE_NOTIFICATION;
		smsApp.sendSms(tel, smsSystem, params, smsTemplateCode, EsendChannel.SHOR_MESSAGE, messType);
	}


    @Test
    public void test111() {

        for (int i=0;i<100;i++){
            System.out.println(bisTransferHandleDao.getSeqenceVals());
        }
    }
}
