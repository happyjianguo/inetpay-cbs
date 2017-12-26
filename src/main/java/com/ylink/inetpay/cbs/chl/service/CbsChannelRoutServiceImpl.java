package com.ylink.inetpay.cbs.chl.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbChlChannelRouteMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannelRoute;
@Service("cbsChannelRouteService")
public class CbsChannelRoutServiceImpl implements CbsChannelRouteService {
	@Autowired
	private TbChlChannelRouteMapper tbChlChannelRouteMapper;
	
	private static Logger _log = LoggerFactory.getLogger(CbsChannelRoutServiceImpl.class);
	@Override
	public PageData<TbChlChannelRoute> findListPage(PageData<TbChlChannelRoute> pageDate,
			TbChlChannelRoute tbChlBankDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<TbChlChannelRoute> findListPage=tbChlChannelRouteMapper.findListPage(tbChlBankDto);
		Page<TbChlChannelRoute> page=(Page<TbChlChannelRoute>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public TbChlChannelRoute details(String id) {
		return tbChlChannelRouteMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TbChlChannelRoute> findListPage(TbChlChannelRoute tbChlBankDto){
		return tbChlChannelRouteMapper.findListPage(tbChlBankDto);
	}

	@Override
	public List<TbChlChannelRoute> findRouteByPayOreder(Long payOrder) {
		return tbChlChannelRouteMapper.findRouteByPayOreder(payOrder);
	}

}
