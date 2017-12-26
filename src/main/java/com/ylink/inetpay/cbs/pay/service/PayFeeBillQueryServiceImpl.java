package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayFeeBillQueryMapper;
import com.ylink.inetpay.common.project.pay.dto.PayFeeBillDto;

@Service("payFeeBillQueryService")
public class PayFeeBillQueryServiceImpl implements PayFeeBillQueryService {

	private static Logger log = LoggerFactory.getLogger(PayFeeBillQueryServiceImpl.class);
	
	@Autowired
	private PayFeeBillQueryMapper payFeeBillQueryMapper;
	
	@Override
	public PageData<PayFeeBillDto> listPage(PageData<PayFeeBillDto> pageData, PayFeeBillDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayFeeBillDto> list = payFeeBillQueryMapper.list(queryParam);
		Page<PayFeeBillDto> page = (Page<PayFeeBillDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}
	
	@Override
	public PayFeeBillDto selectedById(String busiId) {
		
		return payFeeBillQueryMapper.selectedById(busiId);
	}
}
