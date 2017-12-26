package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayFeeSummaryDtoMapper;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDto;

@Service()
public class PayFeeSummaryServiceImpl implements PayFeeSummaryService {

	private static Logger log = LoggerFactory.getLogger(PayFeeSummaryServiceImpl.class);
	
	@Autowired
	private PayFeeSummaryDtoMapper payFeeSummaryDtoMapper;
	
	@Override
	public PageData<PayFeeSummaryDto> listPage(PageData<PayFeeSummaryDto> pageData, PayFeeSummaryDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayFeeSummaryDto> list = payFeeSummaryDtoMapper.list(queryParam);
		Page<PayFeeSummaryDto> page = (Page<PayFeeSummaryDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PayFeeSummaryDto selectedById(String busiId) {
		return payFeeSummaryDtoMapper.selectedById(busiId);
	}

	@Override
	public ReporHeadDto reportSumData(PayFeeSummaryDto queryParam) {
		return payFeeSummaryDtoMapper.reportSumData(queryParam);
	}

	@Override
	public List<PayFeeSummaryDto> queryAll(PayFeeSummaryDto queryParam) {
		return payFeeSummaryDtoMapper.list(queryParam);
	}

}
