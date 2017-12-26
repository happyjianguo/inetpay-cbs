package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayFeeBillDetailQueryDtoMapper;
import com.ylink.inetpay.common.project.pay.dto.PayFeeBillDetailDto;

@Service("payFeeBillDetailQueryService")
public class PayFeeBillDetailQueryServiceImpl implements PayFeeBillDetailQueryService {

	private static Logger log = LoggerFactory.getLogger(PayFeeBillDetailQueryServiceImpl.class);
	
	@Autowired
	private PayFeeBillDetailQueryDtoMapper payFeeBillDetailQueryMapper;
	
	@Override
	public PageData<PayFeeBillDetailDto> listPage(PageData<PayFeeBillDetailDto> pageData,
			PayFeeBillDetailDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayFeeBillDetailDto> list = payFeeBillDetailQueryMapper.list(queryParam);
		Page<PayFeeBillDetailDto> page = (Page<PayFeeBillDetailDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

}
