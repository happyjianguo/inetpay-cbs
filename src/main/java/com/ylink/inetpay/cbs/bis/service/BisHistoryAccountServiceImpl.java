package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActHistoryAccountMapper;
import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto;

/**
 * 
 * @author pst10
 *
 */
@Service("bisHistoryAccountService")
public class BisHistoryAccountServiceImpl implements BisHistoryAccountService {

	@Autowired
	private ActHistoryAccountMapper actHistoryAccountMapper;

	@Override
	public List<ActHistoryAccountDto> queryHisAccount(String accountDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageData<ActHistoryAccountDto> getList(PageData<ActHistoryAccountDto> pageDate,
			ActHistoryAccountDto actHistoryAccountDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<ActHistoryAccountDto> list = actHistoryAccountMapper.getList(actHistoryAccountDto);
		
		
		
		
		Page<ActHistoryAccountDto> page = (Page<ActHistoryAccountDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public ActHistoryAccountDto selectByAccountId(String accountId) {
		return actHistoryAccountMapper.selectByAccountId(accountId);
	}

	@Override
	public ActHistoryAccountDto selectByAccountIdAndAccountDate(String id, String accountDate) {
		return actHistoryAccountMapper.selectByAccountIdAndAccountDate(id,accountDate);
	}


}
