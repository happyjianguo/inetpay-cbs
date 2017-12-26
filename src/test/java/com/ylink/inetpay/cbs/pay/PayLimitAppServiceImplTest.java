package com.ylink.inetpay.cbs.pay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.common.core.constant.EPayLimitBusinessType;
import com.ylink.inetpay.common.project.cbs.app.PayLimitAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayLimitDto;

/**
 * 支付限额测试类
 * @author haha
 *
 */
public class PayLimitAppServiceImplTest extends OtherBaseTest{
	@Autowired
	private PayLimitAppService payLimitAppService;
	@Test
	public void getPayLimitsTest() throws CbsCheckedException{
		List<PayLimitDto> payLimits = payLimitAppService.getPayLimits("0000000011111110");
		for (PayLimitDto payLimitDto : payLimits) {
			System.out.println(payLimitDto);
		}
	}
	@Test
	public void updatePayListTest()throws CbsCheckedException{
		PayLimitDto payLimitDto1 = new PayLimitDto();
		payLimitDto1.setCustId("0000000011111111");
		payLimitDto1.setId("ddcda405-62f7-49f8-83df-f325a8899623");
		//payLimitDto1.setId(UUID.randomUUID().toString());
		payLimitDto1.setCreateTime(new Date());
		//payLimitDto1.setBusiType(EPayLimitBusinessType.PARKING_PAYMENT);
		payLimitDto1.setSingle(100000L);
		payLimitDto1.setDayTotal(200000L);
		payLimitDto1.setMonthTotal(6000000L);
		payLimitDto1.setYearTotal(78000000L);
		PayLimitDto payLimitDto2 = new PayLimitDto();
		payLimitDto2.setCustId("0000000011111111");
		payLimitDto2.setId("36ba4a39-5728-414f-b8cb-6e162eb96cec");
		//payLimitDto2.setId(UUID.randomUUID().toString());
		payLimitDto2.setCreateTime(new Date());
		//payLimitDto2.setBusiType(EPayLimitBusinessType.LUQIAO_FEE);
		payLimitDto2.setSingle(100000L);
		payLimitDto2.setDayTotal(200000L);
		payLimitDto2.setMonthTotal(6000000L);
		payLimitDto2.setYearTotal(78000000L);	
		PayLimitDto payLimitDto3 = new PayLimitDto();
		payLimitDto3.setCustId("0000000011111111");
		payLimitDto3.setId("5f20ede7-0df7-41d4-9146-a29a1508b0a3");
		//payLimitDto3.setId(UUID.randomUUID().toString());
		payLimitDto3.setCreateTime(new Date());
		payLimitDto3.setBusiType(EPayLimitBusinessType.REFUND);	
		payLimitDto3.setSingle(100000L);
		payLimitDto3.setDayTotal(200000L);
		payLimitDto3.setMonthTotal(6000000L);
		payLimitDto3.setYearTotal(78000000L);
		ArrayList<PayLimitDto> arrayList = new ArrayList<>();
		arrayList.add(payLimitDto3);
		arrayList.add(payLimitDto2);
		arrayList.add(payLimitDto1);
		payLimitAppService.updatePayList(arrayList);	
	}
}
