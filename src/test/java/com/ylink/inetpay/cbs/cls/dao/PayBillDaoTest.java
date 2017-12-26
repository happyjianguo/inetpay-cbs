package com.ylink.inetpay.cbs.cls.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.util.DateUtil;
import com.ylink.inetpay.common.core.constant.CLSCheckStatus;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBillVo;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeDetailVo;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;

public class PayBillDaoTest  extends OtherBaseTest{

	@Autowired
	private ClsPayBillDao payBillDao;
	
	
	@Test
	public void test(){
//		ClsTradeDetailVo detail=new ClsTradeDetailVo();
//		detail.setMerCode("8888888888888811");
//		detail.setTradeStartTime(new Date());
//		detail.setTradeEndTime(new Date());
//		detail.setSettleEndTime(new Date());
//		detail.setSettleStartTime(new Date());
//		System.out.println("%%%%%%%"+payBillDao.queryMerTradeDetail(detail).toString());
//
		try {
			ClsPayBillVo payBill=new ClsPayBillVo();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			Date start=sdf.parse("20150804");
			Date end=sdf.parse("20160804");
			payBill.setStartTime(start);
			payBill.setEndTime(end);
			payBill.setAcctCheckStatus(CLSCheckStatus.UN_BALENCE);
			List<ClsPayBillVo> list=payBillDao.queryPayBill(payBill);
			for(ClsPayBillVo bill:list){
				System.out.println(bill.getPayId()+"--"+bill.getAcctCheckStatus()+"--"+bill.getExceptionReason()+"--"+bill.getOrderAmt());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	 
	@Test
	public void queryStation(){
		try {
			ClsTradeStationVo station=new ClsTradeStationVo();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			station.setStartTime(sdf.parse("20160705"));
			station.setEndTime(sdf.parse("20160805"));
			/*List<ClsTradeStationVo> list=payBillDao.queryStation(station);
			for(ClsTradeStationVo s:list){
				System.out.println(s.getBusiType()+"--"+s.getAllCount()+"--"+s.getSuccCount()+"--"+s.getFailCount()+"=="+s.getAllAmt()+"--"+s.getSuccAmt()+"--"+s.getFailAmt());
			}*/
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
}
