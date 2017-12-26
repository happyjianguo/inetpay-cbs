package com.ylink.inetpay.cbs.chl.service;
 
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActAccountDtoMapper;
import com.ylink.inetpay.cbs.act.dao.ActBillDtoMapper;
import com.ylink.inetpay.cbs.act.dao.ActHistoryAccountMapper;
import com.ylink.inetpay.cbs.chl.dao.TbChlBankAccountMapper;
import com.ylink.inetpay.cbs.pay.dao.PayBookDtoMapper;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto;
import com.ylink.inetpay.common.project.channel.dto.TbChlAccountChangeNotice;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankAccount;
import com.ylink.inetpay.common.project.pay.dto.PayBookDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;


@Service("chlActBillService")
public class ChlActBillServicempl implements ChlActBillService {

	@Autowired
	private TbChlBankAccountMapper tbChlBankAccountMapper;
	@Autowired
	private ActAccountDtoMapper actAccountDtoMapper;
	@Autowired
	private ActBillDtoMapper actBillDtoMapper;
	@Autowired
	private PayBookDtoMapper payBookDtoMapper ;
	
	@Autowired
	private ActHistoryAccountMapper actHistoryAccountDtoDao;
	
	@Override
	public List<TbChlBankAccount> findTbChlBankAccount() {
		 
		return tbChlBankAccountMapper.findTbChlBankAccount();
	}


	@Override
	public List<ActAccountDto> findActAccountDto(String custId) {
	 
		return actAccountDtoMapper.findActAccountDto(custId);
	}


	@Override
	public PageData<ActBillDto> findActBillDto(PageData<ActBillDto> pageData, ActBillDto queryParam,List<ActAccountDto> actAccountDtoList) {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<ActBillDto> findListPage= actBillDtoMapper.findAll(queryParam,actAccountDtoList);
		
		if(findListPage!=null && !findListPage.isEmpty()){
			Map<String,String> payIdMap =new HashMap<String, String>();
			for (ActBillDto actBillDto : findListPage) {
				payIdMap.put(actBillDto.getPayId(), actBillDto.getPayId());
			}
			Set<String> payIdSet=payIdMap.keySet();
			//根据流水号查询平台订单号
			List<PayBookDto> payBookDtoList = payBookDtoMapper.findBusIdByPayId(payIdSet);
			if(payBookDtoList!=null&&!payBookDtoList.isEmpty()){
				Map<String,String> busIdMap=new HashMap<String,String>();
				for (PayBookDto payBookDto : payBookDtoList) {
					busIdMap.put(payBookDto.getPayId(), payBookDto.getBusiId());
				}
				for (ActBillDto actBillDto : findListPage) {
					actBillDto.setBusiId(busIdMap.get(actBillDto.getPayId()));
					actBillDto.setAccountDate(DateUtils.stringToString(actBillDto.getAccountDate()));
				}
			}
		 }
		Page<ActBillDto> page =(Page<ActBillDto>) findListPage;
		pageData.setTotal(page.getTotal());
		pageData.setRows(findListPage);
		
		return pageData;
	}


	@Override
	public String getCashAmount(List<ActAccountDto> actAccountDtoList, ActBillDto queryParam) {
		List<ActHistoryAccountDto> actHistoryAccount =actHistoryAccountDtoDao.getCashAmount(actAccountDtoList,queryParam);
		Long amount = 0L;
		if(actHistoryAccount!=null && actHistoryAccount.isEmpty()){
			for (ActHistoryAccountDto  dto : actHistoryAccount) {
				amount+=dto.getCashAmount();
			}
		}
 
		return String.valueOf(amount) ;
	}


	@Override
	public List<ActBillDto> queryAll(ActBillDto queryParam,List<ActAccountDto> actAccountDtoList) {
		List<ActBillDto> findListPage= actBillDtoMapper.findAll(queryParam,actAccountDtoList);
		if(findListPage!=null && !findListPage.isEmpty()){
			Map<String,String> payIdMap =new HashMap<String, String>();
			for (ActBillDto actBillDto : findListPage) {
				payIdMap.put(actBillDto.getPayId(), actBillDto.getPayId());
			}
			Set<String> payIdSet=payIdMap.keySet();
			//根据流水号查询平台订单号
			List<PayBookDto> payBookDtoList = payBookDtoMapper.findBusIdByPayId(payIdSet);
			if(payBookDtoList!=null&&!payBookDtoList.isEmpty()){
				Map<String,String> busIdMap=new HashMap<String,String>();
				for (PayBookDto payBookDto : payBookDtoList) {
					busIdMap.put(payBookDto.getPayId(), payBookDto.getBusiId());
				}
				for (ActBillDto actBillDto : findListPage) {
					actBillDto.setBusiId(busIdMap.get(actBillDto.getPayId()));
					actBillDto.setAccountDate(DateUtils.stringToString(actBillDto.getAccountDate()));
				}
			}
		 }
		 return findListPage;
	}
	 
	
	 
}
