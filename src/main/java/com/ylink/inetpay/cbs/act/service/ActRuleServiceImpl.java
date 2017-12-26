package com.ylink.inetpay.cbs.act.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.act.dao.ActRuleDtoMapper;
import com.ylink.inetpay.common.project.account.dto.ActRuleDto;

@Service("actRuleService")
public class ActRuleServiceImpl implements ActRuleService {
	@Autowired
	ActRuleDtoMapper actRuleDtoMapper;

	@Override
	public PageData<ActRuleDto> queryAllData(PageData<ActRuleDto> pageDate,
			ActRuleDto actRuleDto) {

		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<ActRuleDto> list = actRuleDtoMapper.queryAllData(actRuleDto);
		Page<ActRuleDto> page = (Page<ActRuleDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public ActRuleDto findById(String id) {
		return actRuleDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<ActRuleDto> pageList() {
		return actRuleDtoMapper.pageList();
	}

}
