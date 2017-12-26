package com.ylink.inetpay.cbs.act.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActBankInterestDtoMapper;
import com.ylink.inetpay.common.project.account.dto.ActBankInterestDto;

@Service("actBankInterestService")
public class ActBankInterestServiceImpl implements ActBankInterestService {

	@Autowired
	ActBankInterestDtoMapper actBankInterestDtoMapper;
	
	@Override
	public PageData<ActBankInterestDto> findPage(PageData<ActBankInterestDto> pageData, ActBankInterestDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<ActBankInterestDto> items = actBankInterestDtoMapper.list(queryParam);
		
		Page<ActBankInterestDto> page = (Page<ActBankInterestDto>)items;
		
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		
		return pageData;
	}

	@Override
	public ActBankInterestDto findById(String id) {
		return actBankInterestDtoMapper.selectByPrimaryKey(id);
	}

}
