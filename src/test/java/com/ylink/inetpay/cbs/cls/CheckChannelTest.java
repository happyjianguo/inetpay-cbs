package com.ylink.inetpay.cbs.cls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.cls.dao.ClearJobDao;
import com.ylink.inetpay.cbs.cls.dao.ClsChannelBillDao;
import com.ylink.inetpay.cbs.cls.dao.ClsLiquidationJobDao;
import com.ylink.inetpay.cbs.cls.dao.ClsSettJobDao;
import com.ylink.inetpay.cbs.cls.service.ClsCheckChannelService;
import com.ylink.inetpay.cbs.cls.service.ClsCheckInnerService;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBillVo;
import com.ylink.inetpay.common.project.clear.dto.ClsSettJob;

public class CheckChannelTest  extends OtherBaseTest{

	@Autowired
	private ClsCheckInnerService service1;
	@Autowired
	private ClsCheckChannelService service2;
	@Autowired
	private ClsChannelBillDao channelBillDao;
	@Autowired
	private ClsLiquidationJobDao clsLiquidationJobDao;
	@Autowired
	private ClearJobDao clearJobDao;
	@Autowired
	private ClsSettJobDao clsSettJobDao;
	
	@Test
	public void test(){
//		PageData<ClsChannelBillVo> pageData=new PageData<ClsChannelBillVo>();
//		pageData.setPageSize(10);
//		pageData.setPageNumber(1);
//		PageData<ClsChannelBillVo> list=appService.findCheckChannel(pageData, null);
//		System.out.println(list.toString());
//		ClsRecordCheck check=new ClsRecordCheck();
//		check.setReportId("123456789012345678901234567890123456");
//		check.setReviewStatus(CLSReviewStatus.PASS);
//		check.setAuditer("123");
//		check.setAuditerName("321");
//		check.setCheckSugg("tonggguo");
//		service.review(check);
		
//		System.out.println(service1.queryPayBillSumarry(null));
//		PageData<ClsChannelBillVo> pageData=new PageData<ClsChannelBillVo>();
//		pageData.setPageSize(10);
//		pageData.setPageNumber(1);
//		System.out.println(service2.findCheckChannelReview(pageData, null));
//		ClsLiquidationJob param = new ClsLiquidationJob();
//		clsLiquidationJobDao.queryAll(param);
//		ClsClearJob param = new ClsClearJob();
//		List<ClsClearJob> cjClearJobs = clearJobDao.queryAll(param);
		
//		System.out.println(cjClearJobs.size());
		ClsSettJob clsSettJob = new ClsSettJob();
		clsSettJobDao.queryAll(clsSettJob);
	}
	@Ignore
	@Test
	public void queryChannelBillTest(){
		try {
			ClsChannelBillVo channel=new ClsChannelBillVo();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			channel.setStartTime(sdf.parse("20160701"));
			channel.setEndTime(sdf.parse("20160805"));
			List<ClsChannelBillVo> list=channelBillDao.queryChannel(channel);
			for(ClsChannelBillVo b:list){
				System.out.println(b.getId()+"---"+b.getPayId()+"---"+b.getCheckDay());
			}
			System.out.println("______________________________________");
			channel.setStartTime(sdf.parse("20150701"));
			channel.setEndTime(sdf.parse("20160805"));
			channel.setBankType("");
		    list=channelBillDao.queryChannel(channel);
			for(ClsChannelBillVo b:list){
				System.out.println(b.getId()+"---"+b.getPayId()+"---"+b.getCheckDay());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}