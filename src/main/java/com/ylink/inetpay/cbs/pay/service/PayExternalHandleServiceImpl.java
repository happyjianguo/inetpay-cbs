package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayExternalHandleDtoMapper;
import com.ylink.inetpay.common.project.pay.dto.PayExternalHandleDto;

@Service("payExternalHandleService")
public class PayExternalHandleServiceImpl implements PayExternalHandleService{

	@Autowired
	private PayExternalHandleDtoMapper payExternalDtoMapper;
	@Override
	public PageData<PayExternalHandleDto> getByCond(PayExternalHandleDto payExternalDto, PageData<PayExternalHandleDto> pageData) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<PayExternalHandleDto> list = payExternalDtoMapper.getByCond(payExternalDto);
		Page<PayExternalHandleDto> page = (Page<PayExternalHandleDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public void insert(PayExternalHandleDto payExternalDto) {
		payExternalDtoMapper.insert(payExternalDto);
		
	}

	@Override
	public void update(PayExternalHandleDto payExternalDto) {
		payExternalDtoMapper.update(payExternalDto);
		
	}

	@Override
	public PayExternalHandleDto details(String id) {
		return payExternalDtoMapper.details(id);
	}
	
	
}
