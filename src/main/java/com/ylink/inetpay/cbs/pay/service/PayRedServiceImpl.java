package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayRedPDtoMapper;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;
import com.ylink.inetpay.common.project.pay.dto.PayRedPDto;
@Service("payRedService")
public class PayRedServiceImpl implements PayRedService {
	@Autowired
	private PayRedPDtoMapper payRedPDtoMapper;
	@Override
	public PageData<PayRedPDto> findAll(PageData<PayRedPDto> pageData,
			PayRedPDto queryparam) {
		PageHelper.startPage(pageData.getPageNumber(),pageData.getPageSize());
		List<PayRedPDto> list=payRedPDtoMapper.list(queryparam);
		Page<PayRedPDto> page=(Page<PayRedPDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PayRedPDto details(String id) {
		return payRedPDtoMapper.selectByPrimaryKey(id);
	}

}
