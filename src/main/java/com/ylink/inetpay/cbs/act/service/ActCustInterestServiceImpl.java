package com.ylink.inetpay.cbs.act.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActCustInterestDtoMapper;
import com.ylink.inetpay.common.project.account.app.ActCustInterestAppService;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActCustInterestDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActAccountAppService;

@Service("actCustInterestService")
public class ActCustInterestServiceImpl implements ActCustInterestService {
	
	@Autowired
	ActCustInterestDtoMapper actCustInterestDtoMapper;
	@Autowired
	ActCustInterestAppService actCustInterestAppService;
	@Autowired
	CbsActAccountAppService cbsActAccountAppService;

	@Override
	public PageData<ActCustInterestDto> findPage(PageData<ActCustInterestDto> pageData, ActCustInterestDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ActCustInterestDto> items = actCustInterestDtoMapper.list(queryParam);
		
		Page<ActCustInterestDto> page = (Page<ActCustInterestDto>)items;
		
		ArrayList<String> accountIdList = new ArrayList<String>();
		for(ActCustInterestDto dto : items){
			accountIdList.add(dto.getAccountId());
		}
		List<ActAccountDto> accountList = null;
		if(!accountIdList.isEmpty()){
			accountList = cbsActAccountAppService.getAccountsByAccountIds(accountIdList);
		}
		if(accountList != null && !accountList.isEmpty()){
			HashMap<String, ActAccountDto> accountMap = new HashMap<String, ActAccountDto>();
			for(ActAccountDto accountDto : accountList){
				accountMap.put(accountDto.getAccountId(), accountDto);
			}
			for(ActCustInterestDto dto : items){
				ActAccountDto actAccountDto = accountMap.get(dto.getAccountId());
				if(actAccountDto != null){
					dto.setSubjectNo2Name(actAccountDto.getSubjectNo2Name());
				}
			}
		}
		
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		
		return pageData;
	}

	@Override
	public ActCustInterestDto findById(String id) {
		return actCustInterestDtoMapper.selectByPrimaryKey(id);
	}
}
