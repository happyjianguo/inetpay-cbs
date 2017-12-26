package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.TbchlAccountChangeNoticeMapper;
import com.ylink.inetpay.common.project.channel.dto.TbChlAccountChangeNotice;
@Service("chlAccountChangeNotice")
public class ChlAccountChangeServiceImpl implements ChlAccountChangeNotice{
	
	@Autowired 
	private TbchlAccountChangeNoticeMapper tbChlAccountChangeNoticeMapper;
	

	@Override
	public PageData<TbChlAccountChangeNotice> findAll(PageData<TbChlAccountChangeNotice> 
	pageData,TbChlAccountChangeNotice param) throws RuntimeException {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<TbChlAccountChangeNotice> findListPage= tbChlAccountChangeNoticeMapper.findAll(param);
		Page<TbChlAccountChangeNotice> page =(Page<TbChlAccountChangeNotice>) findListPage;
		pageData.setTotal(page.getTotal());
		pageData.setRows(findListPage);
		return pageData;
	}


	@Override
	public TbChlAccountChangeNotice detail(String id) {
		return tbChlAccountChangeNoticeMapper.detail(id);
	}
}
