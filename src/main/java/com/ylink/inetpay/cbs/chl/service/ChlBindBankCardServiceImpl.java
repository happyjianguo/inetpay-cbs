package com.ylink.inetpay.cbs.chl.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbChlBindBankCardMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlBindBankCard;
@Service("chlBindBankCardService")
public class ChlBindBankCardServiceImpl implements ChlBindBankCardService {
	@Autowired
	private TbChlBindBankCardMapper tbChlBindBankCardMapper;
	
	private static Logger _log = LoggerFactory.getLogger(ChlBindBankCardServiceImpl.class);

	@Override
	public List<TbChlBindBankCard> findByChlBankProp(TbChlBindBankCard userBankDto) {
		return tbChlBindBankCardMapper.selectByParam(userBankDto);
	}
	
	@Override
	public TbChlBindBankCard findById(String id) {
		return tbChlBindBankCardMapper.selectByPrimaryKey(id);
	}

	@Override
	public PageData<TbChlBindBankCard> pageData(PageData<TbChlBindBankCard> pageData, TbChlBindBankCard searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<TbChlBindBankCard> list=tbChlBindBankCardMapper.list(searchDto);
		Page<TbChlBindBankCard> page = (Page<TbChlBindBankCard>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public List<TbChlBindBankCard> listBankCard(TbChlBindBankCard searchDto) {
		return tbChlBindBankCardMapper.list(searchDto);
	}
	
	
}
