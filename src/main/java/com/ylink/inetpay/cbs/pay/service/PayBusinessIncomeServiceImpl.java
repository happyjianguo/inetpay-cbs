package com.ylink.inetpay.cbs.pay.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayFeeSummaryDtoMapper;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateBatchDto;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDto;
@Service("payBusinessIncomeService")
public class PayBusinessIncomeServiceImpl implements PayBusinessIncomeService {
	@Autowired
	private PayFeeSummaryDtoMapper payFeeSummaryDtoMapper;

	@Override
	public PageData<PayFeeSummaryDto> queryAll(PageData<PayFeeSummaryDto> pageData, PayFeeSummaryDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayFeeSummaryDto> list =payFeeSummaryDtoMapper.queryAll(queryParam);
		Page<PayFeeSummaryDto> page = (Page<PayFeeSummaryDto>)list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PayFeeSummaryDto findSumPayAmount(PayFeeSummaryDto queryParam) {
		return payFeeSummaryDtoMapper.findSumPayAmount(queryParam);
	}

	@Override
	public List<PayFeeSummaryDto> findAll(PayFeeSummaryDto queryParam) {
		return payFeeSummaryDtoMapper.queryAll(queryParam);
	}

}
