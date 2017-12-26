package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayAmtAllocateBatchDtoMapper;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateBatchDto;
@Service("payAmtAllocateBatchService")
public class PayAmtAllocateBatchServiceImpl implements PayAmtAllocateBatchService {
	@Autowired
	private PayAmtAllocateBatchDtoMapper payAmtAllocateBatchDtoMapper;

	@Override
	public PageData<PayAmtAllocateBatchDto> findAll(PageData<PayAmtAllocateBatchDto> pageData,
			PayAmtAllocateBatchDto payAmtAllocateDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayAmtAllocateBatchDto> list = payAmtAllocateBatchDtoMapper.listAll(payAmtAllocateDto);
		Page<PayAmtAllocateBatchDto> page = (Page<PayAmtAllocateBatchDto>)list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}


}
