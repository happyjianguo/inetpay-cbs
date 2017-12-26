package com.ylink.inetpay.cbs.chl.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbChlChannelBankMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannelBank;
@Service("cbsChannelBankService")
public class CbsChannelBankServiceImpl implements CbsChannelBankService {
	@Autowired
	private TbChlChannelBankMapper channelBankMapper;
	
	private static Logger _log = LoggerFactory.getLogger(CbsChannelBankServiceImpl.class);
	@Override
	public PageData<TbChlChannelBank> findListPage(PageData<TbChlChannelBank> pageDate,
			TbChlChannelBank tbChlBankDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<TbChlChannelBank> findListPage=channelBankMapper.findListPage(tbChlBankDto);
		Page<TbChlChannelBank> page=(Page<TbChlChannelBank>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public TbChlChannelBank details(String id) {
		return channelBankMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TbChlChannelBank> findListPage(TbChlChannelBank tbChlBankDto){
		return channelBankMapper.findListPage(tbChlBankDto);
	}
	@Override
	public TbChlChannelBank findById(String id){
		return channelBankMapper.selectByPrimaryKey(id);
	}
}
