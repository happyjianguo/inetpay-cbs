package com.ylink.inetpay.cbs.cls;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.alibaba.dubbo.config.annotation.Service;
import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.cls.service.ClsMerSettleService;
import com.ylink.inetpay.common.project.clear.dto.CLsMerchantSettleVo;


public class ClsMerSettleServiceTest extends OtherBaseTest {

	@Autowired
	private ClsMerSettleService merSettleService;
	
	@Test
	public void test(){
		PageData<CLsMerchantSettleVo> pageDate=new PageData<CLsMerchantSettleVo>();
		CLsMerchantSettleVo settle=new CLsMerchantSettleVo();
		settle.setStartTime(new Date());
		settle.setEndTime(new Date());
		pageDate.setPageSize(15);
		pageDate.setPageNumber(1);
		pageDate=merSettleService.queryClsMerSett(pageDate, settle);
		System.out.println("总金额--实际结算总金额--总手续费--总记录条数--");
		for(CLsMerchantSettleVo s:pageDate.getRows()){
			System.out.println(s.getTotalMoney()+"--"+s.getTotalRealMoney()+"--"+s.getFeeMoney()+"--"+s.getRecordCount());
		}
	}
}
