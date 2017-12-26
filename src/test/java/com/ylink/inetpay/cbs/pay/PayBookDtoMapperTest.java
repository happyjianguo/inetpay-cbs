package com.ylink.inetpay.cbs.pay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.pay.dao.PayBookDtoMapper;
import com.ylink.inetpay.cbs.util.DateUtil;
import com.ylink.inetpay.common.core.constant.EBusiType;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;

public class PayBookDtoMapperTest extends OtherBaseTest{

	
	@Autowired
	private PayBookDtoMapper bookDtoMapper;
	
	@Test
	public void queryActive(){
		List<ClsTradeStationVo> list= bookDtoMapper.queryActiveUserStation(null);
		System.out.println("所有记录："+list.size());
		bookDtoMapper.queryActiveUserStation(null);
		for(ClsTradeStationVo v:list){
			System.out.println(v.toString());
		}
		ClsTradeStationVo s=new ClsTradeStationVo();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		try {
			s.setStartTime(sdf.parse("20160801"));
			s.setEndTime(sdf.parse("20160820"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		s.setBusiType(EBusiType.PAY);
		System.out.println("####################################################################");
		System.out.println("####################################################################");
		System.out.println("####################################################################");
		List<ClsTradeStationVo> list2= bookDtoMapper.queryActiveUserStation(s);
		System.out.println("支付记录："+list2.size());
		for(ClsTradeStationVo v:list2){
			System.out.println(v.toString());
		}
		
		
	}
	
	@Test
	public void test(){
		bookDtoMapper.queryMerTradeDetail(null);
	}
}
