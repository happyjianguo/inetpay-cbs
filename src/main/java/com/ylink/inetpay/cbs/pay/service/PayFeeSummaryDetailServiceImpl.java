package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayFeeSummaryDetailDtoMapper;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDetailDto;

@Service("payFeeSummaryDetailService")
public class PayFeeSummaryDetailServiceImpl implements PayFeeSummaryDetailService {

	private static Logger log = LoggerFactory.getLogger(PayFeeSummaryDetailServiceImpl.class);
	
	@Autowired
	private PayFeeSummaryDetailDtoMapper payFeeSummaryDetailDtoMapper;
	
	@Override
	public PageData<PayFeeSummaryDetailDto> listPage(PageData<PayFeeSummaryDetailDto> pageData,
			PayFeeSummaryDetailDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayFeeSummaryDetailDto> list = payFeeSummaryDetailDtoMapper.list(queryParam);
		Page<PayFeeSummaryDetailDto> page = (Page<PayFeeSummaryDetailDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public List<PayFeeSummaryDetailDto> queryAllBySearch(PayFeeSummaryDetailDto queryParam) {
		return payFeeSummaryDetailDtoMapper.list(queryParam);
	}

}
