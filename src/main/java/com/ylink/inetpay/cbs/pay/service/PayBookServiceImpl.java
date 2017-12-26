package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayBookDtoMapper;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayBookDto;

@Service("payBookService")
public class PayBookServiceImpl implements PayBookService {
	@Autowired
	PayBookDtoMapper payBookDtoMapper;

	@Override
	public PageData<PayBookDto> queryAllData(PageData<PayBookDto> pageDate,
			PayBookDto payBookDto) {

		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayBookDto> list = payBookDtoMapper.queryAllData(payBookDto);
		Page<PayBookDto> page = (Page<PayBookDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	@Override
	public List<PayBookDto> listPayBook(PayBookDto payBookDto) {
		List<PayBookDto> list = payBookDtoMapper.queryAllData(payBookDto);
		return list;
	}
	@Override
	public PayBookDto selectByPayId(String busiId) {
		return payBookDtoMapper.selectByPayId(busiId);
	}
	@Override
	public ReporHeadDto reportSumData(PayBookDto payBookDto) {
		return payBookDtoMapper.reportSumData(payBookDto);
	}
	@Override
	public PageData<PayBookDto> queryAllDataForAccount(PageData<PayBookDto> pageDate, PayBookDto payBookDto) {
		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayBookDto> list = payBookDtoMapper.queryAllDataForAccount(payBookDto);
		Page<PayBookDto> page = (Page<PayBookDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}
	
}
