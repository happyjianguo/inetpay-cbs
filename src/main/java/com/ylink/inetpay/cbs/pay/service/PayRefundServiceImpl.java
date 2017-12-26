package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.pay.dao.PayRefundDtoMapper;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayRefundDto;

@Service("payRefundService")
public class PayRefundServiceImpl implements PayRefundService {
	@Autowired
	PayRefundDtoMapper payRefundDtoMapper;

	@Override
	public PageData<PayRefundDto> queryAllData(
			PageData<PayRefundDto> pageDate, PayRefundDto payRefundDto) {

		PageHelper.startPage(pageDate.getPageNumber(), pageDate.getPageSize());
		List<PayRefundDto> list = payRefundDtoMapper.queryAllData(payRefundDto);
		Page<PayRefundDto> page = (Page<PayRefundDto>) list;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(list);
		return pageDate;
	}

	@Override
	public PayRefundDto selectByBusiId(String busiId) {
		return payRefundDtoMapper.selectByBusiId(busiId);
	}
	@Override
	public ReporHeadDto reportSumData(PayRefundDto payRefundDto) {
		return payRefundDtoMapper.reportSumData(payRefundDto);
	}

	
}
