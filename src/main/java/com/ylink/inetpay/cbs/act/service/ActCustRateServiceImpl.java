package com.ylink.inetpay.cbs.act.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActCustRateDtoMapper;
import com.ylink.inetpay.cbs.bis.service.BisActCustRateAuditService;
import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.project.account.dto.ActCustRateDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActCustRateAuditDto;

@Service("actCustRateService")
public class ActCustRateServiceImpl implements ActCustRateService {

	@Autowired
	ActCustRateDtoMapper actCustRateDtoMapper;
	@Autowired
	BisSysParamService bisSysParamService;
	@Autowired
	private BisActCustRateAuditService bisActCustRateAuditService;
	
	@Override
	public ActCustRateDto findById(String id) {
		return actCustRateDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageData<ActCustRateDto> findPage(PageData<ActCustRateDto> pageData, ActCustRateDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ActCustRateDto> items = actCustRateDtoMapper.list(queryParam);
		bankIsExistWaitAudit(items);
		Page<ActCustRateDto> page = (Page<ActCustRateDto>) items;

		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}
	
	/**
	 * 判断是否存在待复核的记录 或者银行利率已经存在
	 */
	public void bankIsExistWaitAudit(List<ActCustRateDto> custRates){
		if(custRates!=null && !custRates.isEmpty()){
			ArrayList<String> bankAccNos = new ArrayList<String>();
			for (ActCustRateDto custRateDto : custRates) {
				bankAccNos.add(custRateDto.getBankCardNo());
			}
			List<BisActCustRateAuditDto> actCustRateAuditDtos=bisActCustRateAuditService.findWaitAuditByBankAccNos(bankAccNos);
			if(actCustRateAuditDtos!=null && !actCustRateAuditDtos.isEmpty()){
				HashMap<String, BisActCustRateAuditDto> actCustRateAuditMap = new HashMap<String,BisActCustRateAuditDto>();
				for (BisActCustRateAuditDto bisActCustRateAuditDto : actCustRateAuditDtos) {
					actCustRateAuditMap.put(bisActCustRateAuditDto.getBankCardNo(), bisActCustRateAuditDto);
				}
				for (ActCustRateDto custRateDto : custRates) {
					if(actCustRateAuditMap.get(custRateDto.getBankCardNo())!=null){
						custRateDto.setIsExistWaitAudit(true);
					}
				}
			}
			List<ActCustRateDto> actCustRates=actCustRateDtoMapper.findByBankCardNos(bankAccNos);
			if(actCustRates!=null && !actCustRates.isEmpty()){
				HashMap<String, ActCustRateDto> actCustRateMap = new HashMap<String,ActCustRateDto>();
				for (ActCustRateDto actCustRateDto : actCustRates) {
					actCustRateMap.put(actCustRateDto.getBankCardNo(), actCustRateDto);
				}
				for (ActCustRateDto custRateDto : custRates) {
					if(actCustRateMap.get(custRateDto.getBankCardNo())!=null){
						custRateDto.setIsExist(true);
					}
				}
			}
		}
	}

	@Override
	public ActCustRateDto findByAccountId(String accountId) {
		return actCustRateDtoMapper.findByAccountId(accountId);
	}

	@Override
	public ActCustRateDto findByBankCardNo(String bankCardNo) {
		return actCustRateDtoMapper.findByBankCardNo(bankCardNo);
	}

	@Override
	public PageData<ActCustRateDto> findCustRatePage(PageData<ActCustRateDto> pageData, ActCustRateDto actCustRateDto) {
		actCustRateDto.setBusiType(EActBusiRefSubBusiType.INTEREST_ACCOUNT);
		String custId = bisSysParamService.getValue("CUST_ID");
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		//排除平台用户
		List<ActCustRateDto> items = actCustRateDtoMapper.listCustRate(actCustRateDto,custId);
		isExistWaitAudit(items);
		Page<ActCustRateDto> page = (Page<ActCustRateDto>) items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		
		return pageData;
	}
	
	/**
	 * 判断是否存在待审核的记录
	 */
	public void isExistWaitAudit(List<ActCustRateDto> custRates){
		if(custRates!=null && !custRates.isEmpty()){
			ArrayList<String> accountIds = new ArrayList<String>();
			for (ActCustRateDto custRateDto : custRates) {
				accountIds.add(custRateDto.getAccountId());
			}
			List<BisActCustRateAuditDto> actCustRateAuditDtos=bisActCustRateAuditService.findWaitAudit(accountIds);
			if(actCustRateAuditDtos!=null && !actCustRateAuditDtos.isEmpty()){
				HashMap<String, BisActCustRateAuditDto> actCustRateAuditMap = new HashMap<String,BisActCustRateAuditDto>();
				for (BisActCustRateAuditDto bisActCustRateAuditDto : actCustRateAuditDtos) {
					actCustRateAuditMap.put(bisActCustRateAuditDto.getAccountId(), bisActCustRateAuditDto);
				}
				for (ActCustRateDto custRateDto : custRates) {
					if(actCustRateAuditMap.get(custRateDto.getAccountId())!=null){
						custRateDto.setIsExistWaitAudit(true);
					}
				}
			}
		}
	}

	@Override
	public Map<String, ActCustRateDto> findCustRateMap(List<String> accountIds) {
		ActCustRateDto actCustRateDto = new ActCustRateDto();
		actCustRateDto.setBusiType(EActBusiRefSubBusiType.INTEREST_ACCOUNT);
		String custId = bisSysParamService.getValue("CUST_ID");
		List<ActCustRateDto> custRateList = actCustRateDtoMapper.listCustRateByAccountIds(accountIds,actCustRateDto,custId);
		HashMap<String, ActCustRateDto> custRateMap = new HashMap<String ,ActCustRateDto>();
		if(custRateList!=null && !custRateList.isEmpty()){
			for (ActCustRateDto dto : custRateList) {
				custRateMap.put(dto.getAccountId(), dto);
			}
		}
		return custRateMap;
	}

}
