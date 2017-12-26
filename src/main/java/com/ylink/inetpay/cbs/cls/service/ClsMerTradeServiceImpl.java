package com.ylink.inetpay.cbs.cls.service;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.cls.dao.ClsPayBillDao;
import com.ylink.inetpay.cbs.pay.dao.PayBookDtoMapper;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeDetailVo;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;

@Service("clsMerTradeService")
public class ClsMerTradeServiceImpl implements ClsMerTradeService{

	@Autowired
	private ClsPayBillDao payBillDao;
	@Autowired
	private PayBookDtoMapper  bookDtoMapper;
	
	public PageData<ClsTradeDetailVo> queryTradeDetail(PageData<ClsTradeDetailVo>  pageData,ClsTradeDetailVo detail){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
//		List<ClsTradeDetailVo> list=payBillDao.queryMerTradeDetail(detail);
		List<ClsTradeDetailVo> list=bookDtoMapper.queryMerTradeDetail(detail);
		Page<ClsTradeDetailVo> page=(Page<ClsTradeDetailVo>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	public PageData<ClsTradeDetailVo> queryClsMerTradeDetail(PageData<ClsTradeDetailVo>  pageData,ClsTradeDetailVo detail){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsTradeDetailVo> list=payBillDao.queryMerTradeDetail(detail);
//		List<ClsTradeDetailVo> list=bookDtoMapper.queryMerTradeDetail(detail);
		Page<ClsTradeDetailVo> page=(Page<ClsTradeDetailVo>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	public ReporHeadDto queryMerTradeDetailSummary(ClsTradeDetailVo detail){
		ReporHeadDto dto=payBillDao.queryMerTradeDetailSummaryAll(detail);
		ReporHeadDto dto2=payBillDao.queryMerTradeDetailSummarySuccess(detail);
		dto.setSuccNum(dto2.getSuccNum());
		dto.setSuccAmt(dto2.getSuccAmt());
		return dto;
	}
	
	public PageData<ClsTradeStationVo> queryTradeStation(PageData<ClsTradeStationVo> pageData,
			ClsTradeStationVo station){
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsTradeStationVo> list=bookDtoMapper.queryStation(station);
		DecimalFormat   format=new  DecimalFormat("#.0000");
		for(ClsTradeStationVo t:list){
			if(t.getSuccCount() == 0){
				t.setRate(0.0);
			}else{
				double result=new Double(t.getSuccCount())/new Double(t.getAllCount());
				t.setRate(Double.parseDouble(format.format(result)));
			}
		}
			
//		List<ClsTradeStationVo> list=new ArrayList<ClsTradeStationVo>(); 
//		List<String> tradeTypes=new ArrayList<String>(); 
//		ClsTradeStationVo st=null;
//		//业务类型：充值，交易类型：【余额充值】【红包充值】
//		tradeTypes.add(ETradeType.ACCOUNT_RECHARGE.getValue());	
//		tradeTypes.add(ETradeType.REDP_RECHARGE.getValue());	
//		station.setTradeTypes(tradeTypes);
//		station.setBusiType(EBusiType.RECHARGE);
//		st=payBillDao.queryStation(station);
//		if(st!=null && st.getAllCount()!=0){
//			list.add(st);
//		}
//		//提现 2种， 选【提现至提现中间户】
//		tradeTypes.clear();
//		tradeTypes.add(ETradeType.WITHDRAW_TO_TEMP_ACCOUNT.getValue());
//		station.setTradeTypes(tradeTypes);
//		station.setBusiType(EBusiType.WITHDRAW);
//		st=payBillDao.queryStation(station);
//		if(st!=null && st.getAllCount()!=0){
//			list.add(st);
//		}
//		//转账3种 【余额转账至余额】【余额转账至转账中间户】
//		tradeTypes.clear();
//		tradeTypes.add(ETradeType.ACCOUNT_TRANSFER_ACCOUNT.getValue());	
//		tradeTypes.add(ETradeType.ACCOUNT_TRANSFER_TEMP_ACCOUNT.getValue());	
//		station.setTradeTypes(tradeTypes);
//		station.setBusiType(EBusiType.TRANSFER);
//		st=payBillDao.queryStation(station);
//		if(st!=null && st.getAllCount()!=0){
//			list.add(st);
//		}
//		//支付2种【余额支付】【红包支付】
//		tradeTypes.clear();
//		tradeTypes.add(ETradeType.ACCOUNT_PAY.getValue());	
//		tradeTypes.add(ETradeType.REDP_PAY.getValue());	
//		station.setTradeTypes(tradeTypes);
//		station.setBusiType(EBusiType.PAY);
//		st=payBillDao.queryStation(station);
//		if(st!=null && st.getAllCount()!=0){
//			list.add(st);
//		}
//		//退款5种【支付退款至红包】【支付退款至余额】 
//		tradeTypes.clear();
//		tradeTypes.add(ETradeType.REFUND_TO_ACCOUNT.getValue());	
//		tradeTypes.add(ETradeType.REFUND_TO_REDP.getValue());	
//		station.setTradeTypes(tradeTypes);
//		station.setBusiType(EBusiType.REFUND);
//		st=payBillDao.queryStation(station);
//		if(st!=null && st.getAllCount()!=0){
//			list.add(st);
//		}
//		//红包发放【红包发放】
//		tradeTypes.clear();
//		tradeTypes.add(ETradeType.REDP_DISTRIBUTE.getValue());	
//		station.setTradeTypes(tradeTypes);
//		station.setBusiType(EBusiType.REDP_DISTRIBUTE);
//		st=payBillDao.queryStation(station);
//		if(st!=null && st.getAllCount()!=0){
//			list.add(st);
//		}
		Page<ClsTradeStationVo> page=(Page<ClsTradeStationVo>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
 
	
	@Override
	public PageData<ClsTradeStationVo> queryMerTradeSummary(
			PageData<ClsTradeStationVo> pageData, ClsTradeStationVo station,
			Integer type) {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ClsTradeStationVo> list=null;
		if(type ==1 ){
			list=bookDtoMapper.queryMerTradeSummary1(station);
		}else{
			list=bookDtoMapper.queryMerTradeSummary2(station);
		}
		Page<ClsTradeStationVo> page=(Page<ClsTradeStationVo>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
}
