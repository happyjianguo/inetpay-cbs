package com.ylink.inetpay.cbs.cls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.cls.dao.ClsPayBillDao;
import com.ylink.inetpay.cbs.cls.dao.ClsShareReportDao;
import com.ylink.inetpay.cbs.pay.dao.PayBookDtoMapper;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.cbs.app.ClsReportAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsMerSett;
import com.ylink.inetpay.common.project.clear.dto.ClsProfitReport;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeDetailVo;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;

public class ClsReportServiceTest extends OtherBaseTest {

	@Autowired
	private ClsReportAppService clsReportAppService;
	@Autowired
	ClsPayBillDao clsPayBillDao;
	@Autowired
	PayBookDtoMapper payBookDtoMapper;
	@Autowired
	ClsShareReportDao shareReportDao;
	
	@Test
	public void test(){
		PageData<ClsTradeDetailVo> pageData=new PageData<>();
		pageData.setPageSize(15);
		pageData.setPageNumber(1);
		ClsTradeDetailVo payBill=new ClsTradeDetailVo();
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		payBill.setTradeEndTime(DateUtils.getCurDate());
		clsReportAppService.queryMerDeail(pageData, payBill);
	}
	
	@Test
	public void report() throws Exception{
//		ClsTradeStationVo s=new ClsTradeStationVo(); 
//		List<ClsTradeStationVo> list=clsPayBillDao.queryActiveUser(s);
//		for(ClsTradeStationVo t:list){
//			System.out.println(t.getPayerCustId()+"---"+t.getAllCount()+"---"+t.getSuccCount()+"---"+t.getFailCount()+"---"+t.getRate());
//		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		PageData<ClsTradeStationVo> pageData=new PageData<ClsTradeStationVo>();
		pageData.setPageNumber(1);
		pageData.setPageSize(15);
		ClsTradeStationVo summary=new ClsTradeStationVo();
		summary.setStartTime(sdf.parse("20160726"));
		summary.setEndTime(sdf.parse("20160801"));
		System.out.println("===================================");
		PageData<ClsTradeStationVo> d=clsReportAppService.queryPayBillSummary(pageData, summary);
		System.out.println("总金额==总笔数==成功金额==成功笔数==失败金额==失败笔数==比率==业务类型");
		for(ClsTradeStationVo s:d.getRows()){
			System.out.println(s.getAllAmt()+"--"+s.getAllCount()+"--"
					+s.getSuccAmt()+"--"+s.getSuccCount()+"--"
					+s.getFailAmt()+"--"+s.getFailCount()+"--"
					+s.getRate()+"--"+s.getBusiType()+"--");
		}
		System.out.println(d.getPageNumber()+"--"+d.getPageSize());
	}
	
	
	@Test
	public void queryMerReport() throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		ClsTradeStationVo station=new ClsTradeStationVo();
		station.setStartTime(sdf.parse("20160801"));
		station.setEndTime(sdf.parse("20160831"));
		PageData<ClsTradeStationVo> pageData=new PageData<ClsTradeStationVo>();
		pageData.setPageNumber(1);
		pageData.setPageSize(15);
		PageData<ClsTradeStationVo>  p1=clsReportAppService.queryMerSummary(pageData, station, 1);
		System.out.println("总金额==总笔数==成功金额==成功笔数==失败金额==失败笔数==比率==业务类型");
		for(ClsTradeStationVo s:p1.getRows()){
			System.out.println(s.getAllAmt()+"--"+s.getAllCount()+"--"
					+s.getSuccAmt()+"--"+s.getSuccCount()+"--"
					+s.getFailAmt()+"--"+s.getFailCount()+"--"
					+s.getRate()+"--"+s.getBusiType()+"--");
		}
		System.out.println("-----------+++++++++++++++++++-----------");
		PageData<ClsTradeStationVo>  d=clsReportAppService.queryMerSummary(pageData, station, 2);
		System.out.println("总金额==总笔数==成功金额==成功笔数==失败金额==失败笔数==比率==业务类型");
		for(ClsTradeStationVo s:d.getRows()){
			System.out.println(s.getAllAmt()+"--"+s.getAllCount()+"--"
					+s.getSuccAmt()+"--"+s.getSuccCount()+"--"
					+s.getFailAmt()+"--"+s.getFailCount()+"--"
					+s.getRate()+"--"+s.getBusiType()+"--");
		}
	}
	
	
	@Test
	public void queryActiveUser()throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		ClsTradeStationVo station=new ClsTradeStationVo();
		station.setStartTime(sdf.parse("20160801"));
		station.setEndTime(sdf.parse("20160812"));
		PageData<ClsTradeStationVo> pageData=new PageData<ClsTradeStationVo>();
		pageData.setPageNumber(1);
		pageData.setPageSize(15);
		PageData<ClsTradeStationVo>  d=clsReportAppService.queryActiveUser(pageData, station);
		System.out.println("总金额==总笔数==成功金额==成功笔数==失败金额==失败笔数==比率==业务类型");
		for(ClsTradeStationVo s:d.getRows()){
			System.out.println(s.getAllAmt()+"--"+s.getAllCount()+"--"
					+s.getSuccAmt()+"--"+s.getSuccCount()+"--"
					+s.getFailAmt()+"--"+s.getFailCount()+"--"
					+s.getRate()+"--"+s.getBusiType()+"--"+s.getPayerCustId());
		}
		System.out.println("---------");
	}
	
	@Test
	public void queryActiveMer()throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		ClsTradeStationVo station=new ClsTradeStationVo();
		station.setStartTime(sdf.parse("20160801"));
		station.setEndTime(sdf.parse("20160831"));
		station.setMerCode("8888888888888811");
		station.setMerName("欢乐海岸停车场");
		PageData<ClsTradeStationVo> pageData=new PageData<ClsTradeStationVo>();
		pageData.setPageNumber(1);
		pageData.setPageSize(25);
		PageData<ClsTradeStationVo>  d=clsReportAppService.queryActiveMer(pageData, station);
		System.out.println("总金额==总笔数==成功金额==成功笔数==失败金额==失败笔数==比率==业务类型");
		for(ClsTradeStationVo s:d.getRows()){
			System.out.println(s.getAllAmt()+"--"+s.getAllCount()+"--"
					+s.getSuccAmt()+"--"+s.getSuccCount()+"--"
					+s.getFailAmt()+"--"+s.getFailCount()+"--"
					+s.getRate()+"--"+s.getBusiType()+"--");
		}
	}
	
	@Test
	public void queryProfit(){
		PageData<ClsProfitReport> pageData=new PageData<ClsProfitReport>();
		pageData.setPageNumber(1);
		pageData.setPageSize(15);
		ClsProfitReport report=new ClsProfitReport();
		report.setAcctDate("201606");
		pageData = clsReportAppService.queryProfitReport(pageData, report);
		System.err.println(pageData.getTotal());
	}
	
	@Test
	public void queryP(){
		ClsProfitReport report=new ClsProfitReport();
		report.setAcctDate("201606");
//		System.out.println(shareReportDao.queryAmtByMonth(report.getAcctDate()));
	}
	
	
	@Test
	public void queryMerSett(){
		PageData<ClsMerSett> pageData=new PageData<ClsMerSett>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		pageData.setPageNumber(1);
		pageData.setPageSize(150);
		ClsMerSett queryParam=new ClsMerSett();
		try {
			queryParam.setStartTime(sdf.parse("20160801"));
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		queryParam.setEndTime(new Date());
		pageData = clsReportAppService.queryMerSett(pageData, queryParam);
		System.out.println("总笔数：--结算总金额：--实收金额：");
		Long merSettle=0L,merReal=0L,merFee=0L;
		Map<String,Integer> map=new HashMap<String,Integer>(); 
		for(ClsMerSett m:pageData.getRows()){
			System.out.println(m.getPayCount() +"--"+m.getPayMoney()+"--"+m.getSettMoney());
			if(map.containsKey(m.getMerCode())){
				map.put(m.getMerCode(), map.get(m.getMerCode())+1);
			}else{
				map.put(m.getMerCode(), 0);
			}
			merSettle+=(m.getPayMoney()-m.getRefnudMoney());
			merReal+=m.getSettMoney();
			merFee+=(m.getPayFee()-m.getTotalRefundFee());
		} 
		System.out.println(map.keySet().size()+"--"+merSettle+"--"+merReal+"--"+merFee);
		System.out.println("__________________________________________________________");
		ReporHeadDto h= clsReportAppService.queryMerSettSummary(queryParam);
		System.out.println("结算商户总数：--结算总金额：--实收金额：----手续费：			");
		System.out.println(h.getAllNum()+"---"+h.getAllAmt()+"--"+h.getSuccNum()+"---"+h.getSuccAmt());
	}
	
	
	@Test
	public void queryTradeStation(){
		ClsTradeStationVo station=new ClsTradeStationVo();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		try {
			station.setStartTime(sdf.parse("20160801"));
			station.setEndTime(sdf.parse("20160901"));
			List<ClsTradeStationVo> list=payBookDtoMapper.queryStation(station);
			for(ClsTradeStationVo t:list){
				System.out.println(t.toString());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
 
}
