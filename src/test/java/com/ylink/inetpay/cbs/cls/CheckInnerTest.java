package com.ylink.inetpay.cbs.cls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.cls.dao.ClsPayBillDao;
import com.ylink.inetpay.cbs.pay.dao.PayBookDtoMapper;
import com.ylink.inetpay.common.core.constant.CLSCheckStatus;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBillVo;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;

public class CheckInnerTest  extends OtherBaseTest{

//	@Autowired
//	private ClsCheckInnerAppService appService;
//	
//	@Autowired
//	private ClsDebtReportService reportService;
//	@Autowired
//	private ClsProfitReportDao profitReportDao;
	@Autowired
	private ClsPayBillDao billDao;
	@Autowired
	private PayBookDtoMapper payBookDtoMapper;
 
	
	
	@Test
	public void test2(){
		List<ClsPayBillVo>  list=billDao.queryPayBillAdjust(null);
		for(ClsPayBillVo c:list){
			System.out.println(c.getPayId()+"--"+c.getAcctCheckDay());
		}
	}
	
	@Test
	public void test(){
		System.out.println("____------------#############");
		List<ClsTradeStationVo>  list=payBookDtoMapper.queryMerTradeSummary2(null);
		for(ClsTradeStationVo c:list){
			System.out.println(c);
		}
		System.out.println("____------------#############");
		list=payBookDtoMapper.queryMerTradeSummary1(null);
		for(ClsTradeStationVo c:list){
			System.out.println(c);
		}
	}
	
	@Test
	public void querySummary(){
		ClsPayBillVo payBill=new  ClsPayBillVo();
		payBill.setAcctCheckStatus(CLSCheckStatus.UN_CHECK);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		try {
			payBill.setStartTime(sdf.parse("20150719"));
			payBill.setEndTime(sdf.parse("20160919"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<ClsPayBillVo> list=billDao.queryPayBill(payBill);
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$(未对账)$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		for(ClsPayBillVo b:list){
			 System.out.println(b.getPayId()+"--"+b.getAcctCheckStatus());
		}
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$(不平帐)$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		payBill.setAcctCheckStatus(CLSCheckStatus.UN_BALENCE);
		billDao.queryPayBill(payBill);
		list=billDao.queryPayBill(payBill);
		for(ClsPayBillVo b:list){
			 System.out.println(b.getPayId()+"--"+b.getAcctCheckStatus());
		}
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$(平帐)$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		payBill.setAcctCheckStatus(CLSCheckStatus.BALENCE);
		billDao.queryPayBill(payBill);
		list=billDao.queryPayBill(payBill);
		for(ClsPayBillVo b:list){
			System.out.println(b.getPayId()+"--"+b.getAcctCheckStatus());
		}
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$(特殊平帐)$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		payBill.setAcctCheckStatus(CLSCheckStatus.SPECIAL_BALENCE);
		billDao.queryPayBill(payBill);
		list=billDao.queryPayBill(payBill);
		for(ClsPayBillVo b:list){
			System.out.println(b.getPayId()+"--"+b.getAcctCheckStatus());
		}
	}
}
