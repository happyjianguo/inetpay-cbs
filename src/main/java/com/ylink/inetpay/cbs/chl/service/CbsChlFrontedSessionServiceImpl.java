package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbChlFrontendSessionMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlFrontendSession;

@Service("cbsChlFrontedSessionService")
public class CbsChlFrontedSessionServiceImpl implements CbsChlFrontedSessionService {
	@Autowired
	TbChlFrontendSessionMapper TbChlFrontendSessionMapper;

	@Override
	public PageData<TbChlFrontendSession> queryAllData(
			PageData<TbChlFrontendSession> pageDate, TbChlFrontendSession TbChlFrontendSession) {

		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<TbChlFrontendSession> list = TbChlFrontendSessionMapper
				.queryAllData(TbChlFrontendSession);
		Page<TbChlFrontendSession> page = (Page<TbChlFrontendSession>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public TbChlFrontendSession selectByBusiId(String id) {
		return TbChlFrontendSessionMapper.selectByPrimaryKey(id);
	}

	

}
