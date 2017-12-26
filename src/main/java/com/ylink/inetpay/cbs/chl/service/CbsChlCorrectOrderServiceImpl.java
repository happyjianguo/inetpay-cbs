package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.dao.ChlCorrectOrderMapper;
import com.ylink.inetpay.common.project.channel.dto.ChlCorrectOrderDto;
@Service("cbsChlCorrectOrderService")
public class CbsChlCorrectOrderServiceImpl implements CbsChlCorrectOrderService {

	@Autowired
	private ChlCorrectOrderMapper chlCorrectOrderMapper;
	@Override
	public PageData<ChlCorrectOrderDto> queryAllData(PageData<ChlCorrectOrderDto> pageDate,
			ChlCorrectOrderDto queryParam) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<ChlCorrectOrderDto> findListPage = chlCorrectOrderMapper.findAll(queryParam);
		Page<ChlCorrectOrderDto> page=(Page<ChlCorrectOrderDto>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}

	@Override
	public ChlCorrectOrderDto selectById(String id) {
		return chlCorrectOrderMapper.selectByPrimaryKey(id);
	}

}
