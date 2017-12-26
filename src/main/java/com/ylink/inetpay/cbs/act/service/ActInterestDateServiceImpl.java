package com.ylink.inetpay.cbs.act.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActInterestDateDtoMapper;
import com.ylink.inetpay.cbs.bis.service.BisActInterestDateAuditService;
import com.ylink.inetpay.common.project.account.dto.ActCustRateDto;
import com.ylink.inetpay.common.project.account.dto.ActInterestDateDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActCustRateAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActInterestDateAuditDto;

@Service("actInterestDateService")
public class ActInterestDateServiceImpl implements ActInterestDateService {

	@Autowired
	ActInterestDateDtoMapper actInterestDateDtoMapper;
	@Autowired
	private BisActInterestDateAuditService bisActInterestDateAuditService;
	
	@Override
	public PageData<ActInterestDateDto> findPage(PageData<ActInterestDateDto> pageData, 
			ActInterestDateDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ActInterestDateDto> items = actInterestDateDtoMapper.list(queryParam);
		isExistWaitAudit(items);
		Page<ActInterestDateDto> page = (Page<ActInterestDateDto>)items;
		
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		
		return pageData;
	}
	
	/**
	 * 判断是否存在待审核的记录
	 */
	public void isExistWaitAudit(List<ActInterestDateDto> custInterestDates){
		if(custInterestDates!=null && !custInterestDates.isEmpty()){
			ArrayList<String> accountIds = new ArrayList<String>();
			for (ActInterestDateDto custInterestDateDto : custInterestDates) {
				accountIds.add(custInterestDateDto.getAccountId());
			}
			
			List<BisActInterestDateAuditDto> actInterestDateAuditDtos=bisActInterestDateAuditService.findWaitAudit(accountIds);
			if(actInterestDateAuditDtos!=null && !actInterestDateAuditDtos.isEmpty()){
				HashMap<String, BisActInterestDateAuditDto> actInterestDateAuditMap = new HashMap<String,BisActInterestDateAuditDto>();
				for (BisActInterestDateAuditDto BisActInterestDateAuditDto : actInterestDateAuditDtos) {
					actInterestDateAuditMap.put(BisActInterestDateAuditDto.getAccountId(), BisActInterestDateAuditDto);
				}
				for (ActInterestDateDto actInterestDateDto : custInterestDates) {
					if(actInterestDateAuditMap.get(actInterestDateDto.getAccountId())!=null){
						actInterestDateDto.setIsExistWaitAudit(true);
					}
				}
			}
		}
	}

	@Override
	public ActInterestDateDto findById(String id) {
		return actInterestDateDtoMapper.selectByPrimaryKey(id);
	}

}
